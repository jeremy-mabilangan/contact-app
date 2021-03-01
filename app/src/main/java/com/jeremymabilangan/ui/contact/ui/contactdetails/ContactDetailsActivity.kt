package com.jeremymabilangan.ui.contact.ui.contactdetails

import android.content.Intent
import android.net.Uri
import android.provider.Telephony
import com.jeremymabilangan.ui.contact.R
import com.jeremymabilangan.ui.contact.base.BaseActivity
import com.jeremymabilangan.ui.contact.extra.emptyString
import com.jeremymabilangan.ui.contact.ui.contacts.dataclass.Contact
import com.jeremymabilangan.ui.contact.utils.GSONConverter
import kotlinx.android.synthetic.main.activity_contact_details.*

class ContactDetailsActivity : BaseActivity(), ContactDetailsView {

    private lateinit var contactDetailsPresenter: ContactDetailsPresenter
    private val gsonConverter = GSONConverter()

    private var mobileNumber: String = emptyString()

    override fun layoutId(): Int {
        return R.layout.activity_contact_details
    }

    override fun viewCreated() {
        contactDetailsPresenter = ContactDetailsPresenterImpl(this, gsonConverter)

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
            contactDetailsPresenter.validateIntent(it)
        }
    }

    private fun callContact(mobileNumber: String) {
        contactDetailsPresenter.callContact(mobileNumber)
    }

    private fun messageContact(mobileNumber: String) {
        contactDetailsPresenter.messageContact(mobileNumber, this)

    }

    override fun displayContactDetails(contact: Contact) {
        contact.apply {
            tvContactDetailsName.text = contactName
            tvContactDetailsMobileNumber.text = contactMobileNumber
            mobileNumber = contactMobileNumber
        }
    }

    override fun dialIntent(uri: Intent) {
        startActivity(uri)
    }

    override fun messageIntent(uri: Intent) {
        startActivity(uri)
    }
}