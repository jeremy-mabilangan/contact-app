package com.jeremymabilangan.ui.contact.ui.contacts

import android.app.Activity
import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.afollestad.materialdialogs.MaterialDialog
import com.jeremymabilangan.ui.contact.R
import com.jeremymabilangan.ui.contact.base.BaseFragment
import com.jeremymabilangan.ui.contact.extra.afterSearchViewTextChange
import com.jeremymabilangan.ui.contact.ui.addcontact.AddContactActivity
import com.jeremymabilangan.ui.contact.ui.contactdetails.ContactDetailsActivity
import com.jeremymabilangan.ui.contact.ui.contacts.adapter.ContactAdapter
import com.jeremymabilangan.ui.contact.ui.contacts.dataclass.Contact
import com.jeremymabilangan.ui.contact.ui.history.dataclass.History
import com.jeremymabilangan.ui.contact.utils.GSONConverter
import com.jeremymabilangan.ui.contact.utils.PreferenceManager
import com.jeremymabilangan.ui.contact.utils.SaveToPreference
import kotlinx.android.synthetic.main.activity_main.bAddContact
import kotlinx.android.synthetic.main.activity_main.bDeleteAllContacts
import kotlinx.android.synthetic.main.activity_main.rvContacts
import kotlinx.android.synthetic.main.activity_main.svSearchContact
import kotlinx.android.synthetic.main.activity_main.tvNoContactFound
import kotlinx.android.synthetic.main.fragment_contacts.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.collections.ArrayList

@Suppress("DEPRECATION")
class ContactsFragment : BaseFragment(), ContactsFragmentView {

    private val requestCodeAddContact = 1001
    private val requestCodeEditContact = 1002
    private val requestCodeHistory = 1003
    private val requestCodeViewDetails = 1004

    private var editContactPosition: Int? = null

    private var contactArray = ArrayList<Contact>()
    private var historyArray = ArrayList<History>()

    private val gsonConverter = GSONConverter()
    private val saveToPreference = SaveToPreference()

    private lateinit var preferenceManager : PreferenceManager
    private lateinit var contactsFragmentPresenter: ContactsFragmentPresenter


    override fun layoutId(): Int {
        return R.layout.fragment_contacts
    }

    override fun viewCreated() {
        contactsFragmentPresenter = ContactsFragmentPresenterImpl(this, gsonConverter)

        initRecyclerView()
        listenToEvents()
        initPreferenceManager()
    }

    private fun listenToEvents() {
        bAddContact.setOnClickListener {
            createContact()
        }

        bDeleteAllContacts.setOnClickListener {
            deleteAllContacts()
        }
    }

    private fun validateHistory() {
        validateToDeleteHistory()
        validateToRestoreHistory()
        validateContactView()

        rvContacts?.adapter?.notifyDataSetChanged()
    }

    private fun initPreferenceManager() {
        preferenceManager = PreferenceManager(context = requireContext())

        loadContacts()
        loadHistory()

        validateHistory()
    }

    private fun initRecyclerView() {
        rvContacts.apply {
            visibility = View.VISIBLE
            adapter = ContactAdapter(context, contactArray) { contact: Contact, position: Int ->
                createDialog(contact, position)
            }

            layoutManager = LinearLayoutManager(activity)
            setHasFixedSize(true)

            itemAnimator.let {
                if (it is DefaultItemAnimator) {
                    it.supportsChangeAnimations = false
                }
            }

            (this.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false

            initRecyclerViewFilter(adapter = adapter as ContactAdapter)
        }
    }

    private fun initRecyclerViewFilter(adapter: ContactAdapter) {
        svSearchContact.afterSearchViewTextChange {
            adapter.filter.filter(it)
        }
    }

    private fun validateToDeleteHistory() {
        val rawJSONString = preferenceManager.loadString("delete_history")

        contactsFragmentPresenter.validateToDeleteHistory(historyArray = historyArray, rawJSONString = rawJSONString)
    }

    private fun validateToRestoreHistory() {
        val rawJSONString = preferenceManager.loadString("restore_history")

        contactsFragmentPresenter.validateToRestoreHistory(historyArray, contactArray, rawJSONString)
    }

    override fun saveContactToPrefManager(contactArray: ArrayList<Contact>) {
        saveContactToPreferenceManager(contactArray)
    }

    override fun saveHistoryToPrefManager(historyArray: ArrayList<History>) {
        saveHistoryToPreferenceManager(historyArray)
    }

    override fun saveToHistoryToDelete(toDeleteHistory: ArrayList<History>) {
        saveToPreference.deleteHistory(preferenceManager =  preferenceManager, gsonConverter = gsonConverter, historyToDelete =  toDeleteHistory)
    }

    override fun saveToHistoryToRestore(toRestoreHistory: ArrayList<History>) {
        saveToPreference.restoreHistory(preferenceManager = preferenceManager, gsonConverter = gsonConverter, historyToRestore =  toRestoreHistory)
    }

    override fun addToContactArrayList(contactFromPreferenceManager: ArrayList<Contact>) {
        contactArray.addAll(contactFromPreferenceManager)
    }

    override fun addToHistoryList(historyFromPreferenceManager: ArrayList<History>) {
        historyArray = historyFromPreferenceManager
    }

    override fun updateContactList(contact: Contact) {
        val index = editContactPosition

        contactsFragmentPresenter.updateContactList(index = index!!, contact = contact)
    }

    override fun saveToContacts(name: String, mobileNumber: String) {
        contactArray.add(Contact(contactName = name, contactMobileNumber = mobileNumber))

        saveContactToPreferenceManager(contactArray)

        validateContactView()

        rvContacts?.adapter?.notifyDataSetChanged()
    }

    override fun deleteContacts(contact: Contact, contactArray: ArrayList<Contact>) {
        addToHistory(contact)
        saveContactToPreferenceManager(contactArray)
    }

    override fun deleteOnContactArray(index: Int): ArrayList<Contact> {
        contactArray.removeAt(index)

        return contactArray
    }

    override fun deleteAllContacts(historyArray: ArrayList<History>) {
        contactArray.clear()

        saveContactToPreferenceManager(contactArray)
        saveHistoryToPreferenceManager(historyArray)

        rvContacts?.adapter?.notifyDataSetChanged()

        validateContactView()
    }

    override fun addToHistoryList2(history: History) {
        historyArray.add(history)

        saveHistoryToPreferenceManager(historyArray)
    }

    override fun updateContactList(index: Int, contact: Contact) {
        contactArray[index] = contact

        saveContactToPreferenceManager(contactArray)

        rvContacts?.adapter?.notifyItemRangeChanged(index, contactArray.size)
    }

    private fun createDialog(contact: Contact, position: Int) {
        MaterialDialog(requireContext()).show {
            title(text = "Contact")
            message(text = "What do you want to this contact: " + contact.contactName + "?")
            cancelOnTouchOutside
            neutralButton(text = "View") {
                viewContactDetails(contact)
            }
            positiveButton(text = "Edit") {
                goToEditContact(contact, position)
            }
            negativeButton(text = "Delete") {
                deleteContact(contact, position)
            }
        }
    }

    private fun createContact() {
        val intent = Intent(requireContext(), AddContactActivity::class.java)

        startActivityForResult(intent, requestCodeAddContact)
    }

    private fun goToEditContact(contact: Contact, position: Int) {
        contact.apply {

            editContactPosition = position

            val intent = Intent(requireContext(), AddContactActivity::class.java)

            intent.putExtra("name", contactName)
            intent.putExtra("mobilenumber", contactMobileNumber)

            startActivityForResult(intent, requestCodeEditContact)
        }
    }

    private fun viewContactDetails(contact: Contact) {
        val rawJSONString = gsonConverter.jsonToString(contact)

        val intent = Intent(requireContext(), ContactDetailsActivity::class.java)
        intent.putExtra("contact", rawJSONString)

        startActivityForResult(intent, requestCodeViewDetails)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                requestCodeAddContact -> {
                    data?.let {
                        updateContact(it)
                    }
                }

                requestCodeEditContact -> {
                    data?.let {
                        editContact(it)
                    }
                }

                requestCodeHistory -> {
                    validateHistory()
                }
            }
        }
    }

    private fun loadContacts() {
        val rawJSONString = preferenceManager.loadString("contact")

        contactsFragmentPresenter.loadContacts(rawJSONString)
    }

    private fun loadHistory() {
        val rawJSONString = preferenceManager.loadString("history")

        contactsFragmentPresenter.loadHistory(rawJSONString)
    }

    private fun editContact(intent: Intent) {
        val name = intent.getStringExtra("name")
        val mobileNumber = intent.getStringExtra("mobilenumber")

        contactsFragmentPresenter.createContactObject(name = name, mobileNumber = mobileNumber)
    }

    private fun updateContact(intent: Intent) {
        val name = intent.getStringExtra("name")
        val mobileNumber = intent.getStringExtra("mobilenumber")

        contactsFragmentPresenter.updateContact(name = name, mobileNumber = mobileNumber)
    }

    private fun deleteContact(contact: Contact, index: Int) {
        var count = 0

        rvContacts.adapter?.apply {
            notifyItemRemoved(index)
            notifyItemRangeChanged(index, contactArray.size)
            count = itemCount
        }

        contactsFragmentPresenter.deleteContact(contact =  contact, index = index, validateView = { validateContactView() }, count = count)
    }

    private fun deleteAllContacts() {
        contactsFragmentPresenter.deleteAllContacts(contactArray = contactArray, historyArray = historyArray)
    }

    private fun addToHistory(contact: Contact) {
        val history = History(historyName = contact.contactName, historyMobileNumber = contact.contactMobileNumber)

        contactsFragmentPresenter.addToHistory(history = history)
    }

    private fun validateContactView() {
        val contactListCount = rvContacts?.adapter?.itemCount
        if (contactListCount == 0) {
            tvNoContactFound.visibility = View.VISIBLE
            ivIconNoContactFound.visibility = View.VISIBLE
            bDeleteAllContacts.visibility = View.GONE
            svSearchContact.visibility = View.GONE
            rvContacts.visibility = View.GONE
        } else {
            tvNoContactFound.visibility = View.GONE
            ivIconNoContactFound.visibility = View.GONE
            bDeleteAllContacts.visibility = View.VISIBLE
            svSearchContact.visibility = View.VISIBLE
            rvContacts.visibility = View.VISIBLE
        }
    }

    private fun saveContactToPreferenceManager(contactArray: ArrayList<Contact>) {
        contactArray.sortBy {
            it.contactName
        }

        val toString = gsonConverter.jsonToString(contactArray)

        preferenceManager.saveString(key = "contact", string = toString)
    }

    private fun saveHistoryToPreferenceManager(historyArray: ArrayList<History>) {
        historyArray.sortBy {
            it.historyName
        }

        val toString = gsonConverter.jsonToString(historyArray)

        preferenceManager.saveString(key = "history", string = toString)
    }
}