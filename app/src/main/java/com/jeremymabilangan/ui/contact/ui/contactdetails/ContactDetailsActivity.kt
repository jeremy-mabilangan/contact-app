package com.jeremymabilangan.ui.contact.ui.contactdetails

import android.content.Intent
import android.net.Uri
import android.provider.Telephony
import com.jeremymabilangan.ui.contact.R
import com.jeremymabilangan.ui.contact.base.BaseActivity
import com.jeremymabilangan.ui.contact.extra.emptyString
import com.jeremymabilangan.ui.contact.ui.main.dataclass.Contact
import com.jeremymabilangan.ui.contact.utils.GSONConverter
import kotlinx.android.synthetic.main.activity_contact_details.*

class ContactDetailsActivity : BaseActivity() {

    private val gsonConverter = GSONConverter()

    private var mobileNumber: String = emptyString()

    override fun layoutId(): Int {
        return R.layout.activity_contact_details
    }

    override fun viewCreated() {
        listenToEvents()
        validateIntent()
    }

    override fun onBackPressed() {
        super.onBackPressed()

        finish()
    }

    private fun listenToEvents() {
        bContactDetailsBack.setOnClickListener {
            onBackPressed()
        }

        bCallContact.setOnClickListener {
            callContact(mobileNumber)
        }

        bMessageContact.setOnClickListener {
            messageContact(mobileNumber)
        }
    }

    private fun validateIntent() {
        val contact: String ? = intent.getStringExtra("contact")

        contact?.let {
            val contactFromObject = gsonConverter.stringToObject(it) as Contact
            populateDetails(contactFromObject)
        }
    }

    private fun populateDetails(contact: Contact) {
        contact.apply {
            tvContactDetailsName.text = contactName
            tvContactDetailsMobileNumber.text = contactMobileNumber
            mobileNumber = contactMobileNumber
        }
    }

    private fun callContact(mobileNumber: String) {
        val dialIntent = Intent(Intent.ACTION_DIAL)
        dialIntent.data = Uri.parse("tel:$mobileNumber")
        startActivity(dialIntent)
    }

    private fun messageContact(mobileNumber: String) {
        val uri= Uri.parse(java.lang.String.format("smsto:%s", mobileNumber))

        val smsIntent = Intent(Intent.ACTION_SENDTO, uri)

        smsIntent.putExtra("sms_body", "")
        smsIntent.setPackage(Telephony.Sms.getDefaultSmsPackage(this))

        startActivity(smsIntent)
    }
}