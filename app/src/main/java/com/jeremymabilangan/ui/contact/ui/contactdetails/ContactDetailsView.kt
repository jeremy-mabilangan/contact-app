package com.jeremymabilangan.ui.contact.ui.contactdetails

import android.content.Intent
import com.jeremymabilangan.ui.contact.ui.contacts.dataclass.Contact

interface ContactDetailsView {

    fun displayContactDetails(contact: Contact)

    fun dialIntent(uri: Intent)

    fun messageIntent(uri: Intent)
}