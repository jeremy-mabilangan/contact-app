package com.jeremymabilangan.ui.contact.ui.contacts

import android.content.Context
import com.jeremymabilangan.ui.contact.ui.contacts.dataclass.Contact
import com.jeremymabilangan.ui.contact.ui.history.dataclass.History

interface ContactsFragmentPresenter {

    fun validateToDeleteHistory(historyArray: ArrayList<History>, rawJSONString: String)

    fun validateToRestoreHistory(historyArray: ArrayList<History>, contactArray: ArrayList<Contact>, rawJSONString: String)
}