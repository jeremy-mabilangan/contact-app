package com.jeremymabilangan.ui.contact.ui.addcontact

interface AddContactPresenter {

    fun validateIntent(name: String?, mobileNumber: String?)

    fun validateName(name: String, rawContactString: String, rawHistoryString: String)

    fun validateMobileNumber(mobileNumber: String, rawContactString: String, rawHistoryString: String)

}