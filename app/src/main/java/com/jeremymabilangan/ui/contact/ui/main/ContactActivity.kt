package com.jeremymabilangan.ui.contact.ui.main

import android.app.Activity
import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.jeremymabilangan.ui.contact.R
import com.jeremymabilangan.ui.contact.base.BaseActivity
import com.jeremymabilangan.ui.contact.extra.afterSearchViewTextChange
import com.jeremymabilangan.ui.contact.ui.addcontact.AddContactActivity
import com.jeremymabilangan.ui.contact.ui.contactdetails.ContactDetailsActivity
import com.jeremymabilangan.ui.contact.ui.history.HistoryActivity
import com.jeremymabilangan.ui.contact.ui.history.dataclass.History
import com.jeremymabilangan.ui.contact.ui.contacts.adapter.ContactAdapter
import com.jeremymabilangan.ui.contact.ui.contacts.dataclass.Contact
import com.jeremymabilangan.ui.contact.utils.GSONConverter
import com.jeremymabilangan.ui.contact.utils.PreferenceManager
import com.jeremymabilangan.ui.contact.utils.SaveToPreference
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.intentFor

class ContactActivity : BaseActivity() {

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

    /**
     * Edit
     * Delete One Contact
     * Delete All
     * Search
     *
     * naming convention
     */
    override fun layoutId(): Int {
        return R.layout.activity_main
    }

    override fun viewCreated() {
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

        bContactsHistory.setOnClickListener {
            showContactHistory()
        }
    }

    private fun validateToDeleteHistory() {
        val rawJSONString = preferenceManager.loadString("delete_history")

        if (rawJSONString.isNotEmpty()) {
            val toDeleteHistory = gsonConverter.stringToJSON(rawJSONString) as ArrayList<History>

            for (history in toDeleteHistory) {
                historyArray.removeAll {
                    it.historyName == history.historyName
                }
            }

            saveHistoryToPreferenceManager(historyArray)

            toDeleteHistory.clear()
            saveToPreference.deleteHistory(preferenceManager =  preferenceManager, gsonConverter = gsonConverter, historyToDelete =  toDeleteHistory)
        }
    }

    private fun validateToRestoreHistory() {
        val rawJSONString = preferenceManager.loadString("restore_history")

        if (rawJSONString.isNotEmpty()) {
            val toRestoreHistory = gsonConverter.stringToJSON(rawJSONString) as ArrayList<History>

            for (history in toRestoreHistory) {
                historyArray.removeAll {
                    it.historyName == history.historyName
                }

                val contact = Contact(contactName = history.historyName, contactMobileNumber = history.historyMobileNumber)
                contactArray.add(contact)
            }

            saveContactToPreferenceManager(contactArray)
            saveHistoryToPreferenceManager(historyArray)

            toRestoreHistory.clear()
            saveToPreference.restoreHistory(preferenceManager = preferenceManager, gsonConverter = gsonConverter, historyToRestore =  toRestoreHistory)
        }
    }

    private fun validateHistory() {
        validateToDeleteHistory()
        validateToRestoreHistory()
        validateContactView()
        validateShowContactHistoryButton()

        rvContacts?.adapter?.notifyDataSetChanged()
    }

    private fun initPreferenceManager() {
        preferenceManager = PreferenceManager(context = this)

        loadContact()
        loadHistory()

        validateHistory()
    }

    private fun initRecyclerViewFilter(adapter: ContactAdapter) {
        svSearchContact.afterSearchViewTextChange {
            adapter.filter.filter(it)
        }
    }

    private fun initRecyclerView() {
        rvContacts.apply {
            visibility = View.VISIBLE
            adapter = ContactAdapter(this@ContactActivity, contactArray, {
                viewContactDetails(it)
            }, { contact: Contact, position: Int ->
                deleteContact(contact, position)
            }, { contact: Contact, position: Int ->
                goToEditContact(contact, position)
            })

            layoutManager = LinearLayoutManager(this@ContactActivity)
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

    private fun showContactHistory() {
        val toString = gsonConverter.jsonToString(historyArray)

        startActivityForResult(
            intentFor<HistoryActivity>("history" to toString),
            requestCodeHistory
        )
    }

    private fun createContact() {
        startActivityForResult(
            intentFor<AddContactActivity>(),
            requestCodeAddContact
        )
    }

    private fun goToEditContact(contact: Contact, position: Int) {
        contact.apply {

            editContactPosition = position

            startActivityForResult(
                intentFor<AddContactActivity>("name" to contactName, "mobilenumber" to contactMobileNumber),
                requestCodeEditContact
            )
        }
    }

    private fun viewContactDetails(contact: Contact) {
        val rawJSONString = gsonConverter.jsonToString(contact)

        startActivityForResult(
            intentFor<ContactDetailsActivity>("contact" to rawJSONString),
            requestCodeViewDetails
        )
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

    private fun loadContact() {
        val rawJSONString = preferenceManager.loadString("contact")

        if (rawJSONString.isNotEmpty()) {
            val contactFromPreferenceManager = gsonConverter.stringToJSON(rawJSONString) as ArrayList<Contact>

            for (contact in contactFromPreferenceManager) {
                contactArray.add(contact)
            }
        }
    }

    private fun loadHistory() {
        val rawJSONString = preferenceManager.loadString("history")

        if (rawJSONString.isNotEmpty()) {
            val historyFromPreferenceManager = gsonConverter.stringToJSON(rawJSONString) as ArrayList<History>
            historyArray = historyFromPreferenceManager
        }
    }

    private fun saveContact(contact: Contact) {
        contactArray.add(contact)

        saveContactToPreferenceManager(contactArray)

        validateContactView()

        rvContacts?.adapter?.notifyDataSetChanged()
    }

    private fun editContact(intent: Intent) {
        val index = editContactPosition

        val name = intent.getStringExtra("name")
        val mobileNumber = intent.getStringExtra("mobilenumber")

        val contact = Contact(contactName = name!!, contactMobileNumber = mobileNumber!!)

        contactArray[index!!] = contact

        saveContactToPreferenceManager(contactArray)

        rvContacts?.adapter?.notifyDataSetChanged()
    }

    private fun updateContact(intent: Intent) {
        val name = intent.getStringExtra("name")
        val mobileNumber = intent.getStringExtra("mobilenumber")

        if (name != null && mobileNumber != null) {
            saveContact(Contact(contactName = name, contactMobileNumber = mobileNumber))
        }
    }

    private fun deleteContact(contact: Contact, index: Int) {
        contactArray.removeAt(index)

        rvContacts?.adapter?.notifyDataSetChanged()

        saveContactToPreferenceManager(contactArray)

        addToHistory(contact)

        validateShowContactHistoryButton()

        val contactListCount = rvContacts?.adapter?.itemCount
        if (contactListCount == 0) {
            validateContactView()
        }
    }

    private fun deleteAllContacts() {
        for (contact in contactArray) {
            val history = History(historyName = contact.contactName, historyMobileNumber = contact.contactMobileNumber)

            historyArray.add(history)
        }

        contactArray.clear()

        saveContactToPreferenceManager(contactArray)
        saveHistoryToPreferenceManager(historyArray)

        rvContacts?.adapter?.notifyDataSetChanged()

        validateContactView()

        validateShowContactHistoryButton()
    }

    private fun addToHistory(contact: Contact) {
        val history = History(historyName = contact.contactName, historyMobileNumber = contact.contactMobileNumber)

        historyArray.add(history)

        saveHistoryToPreferenceManager(historyArray)
    }

    private fun validateContactView() {
        val contactListCount = rvContacts?.adapter?.itemCount
        if (contactListCount == 0) {
            tvNoContactFound.visibility = View.VISIBLE
            bDeleteAllContacts.visibility = View.GONE
            svSearchContact.visibility = View.INVISIBLE
        } else {
            tvNoContactFound.visibility = View.INVISIBLE
            bDeleteAllContacts.visibility = View.VISIBLE
            svSearchContact.visibility = View.VISIBLE
        }
    }

    private fun validateShowContactHistoryButton() {
        if (historyArray.isNotEmpty()) {
            if (bContactsHistory.visibility == View.VISIBLE) {
                return
            }

            bContactsHistory.visibility = View.VISIBLE
        } else {
            bContactsHistory.visibility = View.INVISIBLE
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
