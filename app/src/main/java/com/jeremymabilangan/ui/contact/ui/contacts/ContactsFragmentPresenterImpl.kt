package com.jeremymabilangan.ui.contact.ui.contacts

import androidx.recyclerview.widget.RecyclerView
import com.jeremymabilangan.ui.contact.ui.contacts.dataclass.Contact
import com.jeremymabilangan.ui.contact.ui.history.dataclass.History
import com.jeremymabilangan.ui.contact.utils.GSONConverter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ContactsFragmentPresenterImpl(private var contactsFragmentView: ContactsFragmentView,
                                    private var gsonConverter: GSONConverter
) : ContactsFragmentPresenter {

    override fun validateToDeleteHistory(historyArray: ArrayList<History>, rawJSONString: String) {
        if (rawJSONString.isNotEmpty()) {
            val toDeleteHistory = gsonConverter.stringToJSON(rawJSONString) as ArrayList<History>

            for (history in toDeleteHistory) {
                historyArray.removeAll {
                    it.historyName == history.historyName
                }
            }

            contactsFragmentView.saveHistoryToPrefManager(historyArray = historyArray)

            toDeleteHistory.clear()

            contactsFragmentView.saveToHistoryToDelete(toDeleteHistory = toDeleteHistory)
        }
    }

    override fun validateToRestoreHistory(historyArray: ArrayList<History>, contactArray: ArrayList<Contact>, rawJSONString: String ) {
        if (rawJSONString.isNotEmpty()) {
            val toRestoreHistory = gsonConverter.stringToJSON(rawJSONString) as ArrayList<History>

            for (history in toRestoreHistory) {
                historyArray.removeAll {
                    it.historyName == history.historyName
                }

                val contact = Contact(contactName = history.historyName, contactMobileNumber = history.historyMobileNumber)
                contactArray.add(contact)
            }

            contactsFragmentView.saveContactToPrefManager(contactArray = contactArray)
            contactsFragmentView.saveHistoryToPrefManager(historyArray = historyArray)

            toRestoreHistory.clear()

            contactsFragmentView.saveToHistoryToRestore(toRestoreHistory = toRestoreHistory)
        }
    }

    override fun loadContacts(rawJSONString: String) {
        if (rawJSONString.isNotEmpty()) {
            val contactFromPreferenceManager = gsonConverter.stringToJSON(rawJSONString) as ArrayList<Contact>

            contactsFragmentView.addToContactArrayList(contactFromPreferenceManager = contactFromPreferenceManager)
        }
    }

    override fun loadHistory(rawJSONString: String) {
        if (rawJSONString.isNotEmpty()) {
            val historyFromPreferenceManager = gsonConverter.stringToJSON(rawJSONString) as ArrayList<History>

            contactsFragmentView.addToHistoryList(historyFromPreferenceManager = historyFromPreferenceManager)
        }
    }

    override fun createContactObject(name: String?, mobileNumber: String?) {
        val contact = Contact(contactName = name!!, contactMobileNumber = mobileNumber!!)

        contactsFragmentView.updateContactList(contact = contact)
    }

    override fun updateContact(name: String?, mobileNumber: String?) {
        if (name != null && mobileNumber != null) {
            contactsFragmentView.saveToContacts(name = name, mobileNumber = mobileNumber)
        }
    }

    override fun deleteContact(contact: Contact, index: Int, validateView: () -> Unit, rvContacts: RecyclerView) {

        val contactArray = contactsFragmentView.deleteOnContactArray(index = index)

        rvContacts.adapter?.apply {
            notifyItemRemoved(index)
            notifyItemRangeChanged(index, contactArray.size)

            GlobalScope.launch(Dispatchers.Main) {

                delay(500)

                if (itemCount == 0) validateView()
            }
        }

        contactsFragmentView.deleteContacts(contact, contactArray)
    }

    override fun deleteAllContacts() {

    }

}