package com.jeremymabilangan.ui.contact.ui.contacts

import com.jeremymabilangan.ui.contact.ui.contacts.dataclass.Contact
import com.jeremymabilangan.ui.contact.ui.history.dataclass.History

interface ContactsFragmentView {

    fun saveContactToPrefManager(contactArray: ArrayList<Contact>)

    fun saveHistoryToPrefManager(historyArray: ArrayList<History>)

    fun saveToHistoryToDelete(toDeleteHistory: ArrayList<History>)

    fun saveToHistoryToRestore(toRestoreHistory: ArrayList<History>)
}