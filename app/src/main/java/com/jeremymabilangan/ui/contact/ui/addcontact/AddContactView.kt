package com.jeremymabilangan.ui.contact.ui.addcontact

interface AddContactView {

    fun displayValidatedContactToEdit(name: String, mobileNumber: String)

    fun displayContactNameError(error: String)

    fun displayContactNumberError(error: String)
}