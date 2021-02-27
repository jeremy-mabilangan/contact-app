package com.jeremymabilangan.ui.contact.ui.addcontact

import android.text.TextUtils
import com.jeremymabilangan.ui.contact.data.ErrorType
import com.jeremymabilangan.ui.contact.extra.emptyString
import com.jeremymabilangan.ui.contact.ui.contacts.dataclass.Contact
import com.jeremymabilangan.ui.contact.ui.history.dataclass.History
import com.jeremymabilangan.ui.contact.utils.GSONConverter

class AddContactPresenterImpl(private var addContactView: AddContactView,
                              private var gsonConverter: GSONConverter
) : AddContactPresenter {

    override fun validateIntent(name: String?, mobileNumber: String?) {
        if (name != null && mobileNumber != null) {
            addContactView.displayValidatedContactToEdit(name, mobileNumber)
        }
    }

    override fun validateName(name: String, rawContactString: String, rawHistoryString: String) {
        var result : String = emptyString()

        if (name.isEmpty()) {
            result = ErrorType.NAME_IS_EMPTY.value
        } else {

            if (rawContactString.isNotEmpty()) {
                val contactFromPreferenceManager
                        = gsonConverter.stringToJSON(rawContactString) as ArrayList<Contact>

                for (contact in contactFromPreferenceManager) {
                    if (contact.contactName == name) {
                        result = ErrorType.NAME_ALREADY_EXIST.value
                    }
                }
            }

            if(rawHistoryString.isNotEmpty()) {
                val historyFormPreferenceManager
                        = gsonConverter.stringToJSON(rawHistoryString) as ArrayList<History>

                for (history in historyFormPreferenceManager) {
                    if (history.historyName == name) {
                        result = ErrorType.NAME_ALREADY_IS_IN_HISTORY.value
                    }
                }
            }
        }

        if (result != emptyString()) {
            addContactView.displayContactNameError(result)
        }
    }

    override fun validateMobileNumber(mobileNumber: String, rawContactString: String, rawHistoryString: String) {
        var result : String = emptyString()

        if (mobileNumber.isEmpty()) {
            result = ErrorType.MOBILE_NUMBER_IS_EMPTY.value
        } else if (mobileNumber.length != 11 || mobileNumber.substring(0, 2) != "09" || !TextUtils.isDigitsOnly(mobileNumber)) {
            result = ErrorType.MOBILE_NUMBER_IS_INVALID.value
        } else {

            if (rawContactString.isNotEmpty()) {
                val contactFromPreferenceManager = gsonConverter.stringToJSON(rawContactString) as ArrayList<Contact>

                for (contact in contactFromPreferenceManager) {
                    if (contact.contactMobileNumber == mobileNumber) {
                        result = ErrorType.MOBILE_NUMBER_ALREADY_EXIST.value
                    }
                }
            }

            if(rawHistoryString.isNotEmpty()) {
                val historyFormPreferenceManager = gsonConverter.stringToJSON(rawHistoryString) as ArrayList<History>

                for (history in historyFormPreferenceManager) {
                    if (history.historyMobileNumber == mobileNumber) {
                        result = ErrorType.MOBILE_NUMBER_ALREADY_IS_IN_HISTORY.value
                    }
                }
            }
        }

        if (result != emptyString()) {
            addContactView.displayContactNumberError(result)
        }
    }

}