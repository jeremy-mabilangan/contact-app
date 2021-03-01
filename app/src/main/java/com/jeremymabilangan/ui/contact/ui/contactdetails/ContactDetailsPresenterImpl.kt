package com.jeremymabilangan.ui.contact.ui.contactdetails

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Telephony
import com.jeremymabilangan.ui.contact.ui.contacts.dataclass.Contact
import com.jeremymabilangan.ui.contact.utils.GSONConverter

class ContactDetailsPresenterImpl(private var contactDetailsView: ContactDetailsView,
                                  private var gsonConverter: GSONConverter
) : ContactDetailsPresenter {

    override fun validateIntent(contact: String) {
        val contactFromObject = gsonConverter.stringToObject(contact) as Contact

        contactDetailsView.displayContactDetails(contactFromObject)
    }

    override fun callContact(mobileNumber: String) {
        val dialIntent = Intent(Intent.ACTION_DIAL)
        dialIntent.data = Uri.parse("tel:$mobileNumber")

        contactDetailsView.dialIntent(dialIntent)
    }

    override fun messageContact(mobileNumber: String, context: Context) {
        val uri= Uri.parse(java.lang.String.format("smsto:%s", mobileNumber))
        val smsIntent = Intent(Intent.ACTION_SENDTO, uri)

        smsIntent.putExtra("sms_body", "")
        smsIntent.setPackage(Telephony.Sms.getDefaultSmsPackage(context))

        contactDetailsView.messageIntent(smsIntent)
    }

}