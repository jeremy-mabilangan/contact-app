package com.jeremymabilangan.ui.contact.ui.contacts

import androidx.recyclerview.widget.RecyclerView
import com.jeremymabilangan.ui.contact.ui.contacts.dataclass.Contact
import com.jeremymabilangan.ui.contact.ui.history.dataclass.History

interface ContactsFragmentPresenter {

    fun validateToDeleteHistory(historyArray: ArrayList<History>, rawJSONString: String)

    fun validateToRestoreHistory(historyArray: ArrayList<History>, contactArray: ArrayList<Contact>, rawJSONString: String)

    fun loadContacts(rawJSONString: String)

    fun loadHistory(rawJSONString: String)

    fun createContactObject(name: String?, mobileNumber: String?)

    fun updateContact(name: String?, mobileNumber: String?)

    fun deleteContact(contact: Contact, index: Int, validateView: () -> Unit, rvContacts: RecyclerView)

    fun deleteAllContacts()
}