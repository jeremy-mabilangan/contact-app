package com.jeremymabilangan.ui.contact.ui.contacts

import com.jeremymabilangan.ui.contact.ui.contacts.dataclass.Contact
import com.jeremymabilangan.ui.contact.ui.history.dataclass.History

interface ContactsFragmentView {

    fun saveContactToPrefManager(contactArray: ArrayList<Contact>)

    fun saveHistoryToPrefManager(historyArray: ArrayList<History>)

    fun saveToHistoryToDelete(toDeleteHistory: ArrayList<History>)

    fun saveToHistoryToRestore(toRestoreHistory: ArrayList<History>)

    fun addToContactArrayList(contactFromPreferenceManager: ArrayList<Contact>)

    fun addToHistoryList(historyFromPreferenceManager: ArrayList<History>)

    fun updateContactList(contact: Contact)

    fun saveToContacts(name: String, mobileNumber: String)

    fun deleteContacts(contact: Contact, contactArray: ArrayList<Contact>)

    fun deleteOnContactArray(index: Int): ArrayList<Contact>
}