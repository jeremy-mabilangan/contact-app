package com.jeremymabilangan.ui.contact.ui.contacts

import android.content.Context
import com.afollestad.materialdialogs.MaterialDialog
import com.jeremymabilangan.ui.contact.ui.contacts.dataclass.Contact
import com.jeremymabilangan.ui.contact.ui.history.dataclass.History
import com.jeremymabilangan.ui.contact.utils.GSONConverter

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

}