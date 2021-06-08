package com.jeremymabilangan.ui.contact.ui.contactdetails

import android.content.Context

interface ContactDetailsPresenter {

    fun validateIntent(contact: String)

    fun callContact(mobileNumber: String)

    fun messageContact(mobileNumber: String, context: Context)

}