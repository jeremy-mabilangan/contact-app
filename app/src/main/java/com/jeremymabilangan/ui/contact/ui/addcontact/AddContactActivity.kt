package com.jeremymabilangan.ui.contact.ui.addcontact

import android.app.Activity
import android.text.TextUtils
import android.util.Log
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import com.jeremymabilangan.ui.contact.R
import com.jeremymabilangan.ui.contact.base.BaseActivity
import com.jeremymabilangan.ui.contact.extra.afterTextChanged
import com.jeremymabilangan.ui.contact.extra.emptyString
import com.jeremymabilangan.ui.contact.extra.readText
import com.jeremymabilangan.ui.contact.ui.history.dataclass.History
import com.jeremymabilangan.ui.contact.ui.main.ContactActivity
import com.jeremymabilangan.ui.contact.ui.main.dataclass.Contact
import com.jeremymabilangan.ui.contact.utils.GSONConverter
import com.jeremymabilangan.ui.contact.utils.PreferenceManager
import kotlinx.android.synthetic.main.activity_add_contact.*
import org.jetbrains.anko.intentFor

@Suppress("DEPRECATED_IDENTITY_EQUALS")
class AddContactActivity : BaseActivity() {

    private var nameToEdit: String = emptyString()
    private var mobileNumberToEdit: String = emptyString()

    private val gsonConverter = GSONConverter()

    private lateinit var preferenceManager : PreferenceManager

    override fun layoutId(): Int {
        return R.layout.activity_add_contact
    }

    override fun viewCreated() {
        listenToEvents()
        initPreferenceManager()
        validateIntent()
    }

    override fun onBackPressed() {
        super.onBackPressed()

        finish()
    }

    private fun initPreferenceManager() {
        preferenceManager = PreferenceManager(context = this)
    }

    private fun validateIntent() {
        val name: String ? = intent.getStringExtra("name")
        val mobileNumber: String ? = intent.getStringExtra("mobilenumber")

        if (name != null && mobileNumber != null) {
            Log.d("AddContactActivity", "EDIT CONTACT")

            nameToEdit = name
            mobileNumberToEdit = mobileNumber

            etContactName.setText(name)
            etContactNumber.setText(mobileNumber)
        } else {
            Log.d("AddContactActivity", "ADD CONTACT")
        }
    }

    private fun listenToEvents() {
        textInputNumber.editText?.setOnEditorActionListener { v, actionId, event ->
            if (event != null && event.keyCode === KeyEvent.KEYCODE_ENTER || actionId == EditorInfo.IME_ACTION_DONE) {
                v.clearFocus()
                validateValue()
            }
            return@setOnEditorActionListener false
        }

        bSaveContact.setOnClickListener {
            validateValue()
        }

        bAddContactBack.setOnClickListener {
            onBackPressed()
        }

        // Polymorphism
        // tieName is EditText
        // EditText is TextView
        // tieName is TextView

        etContactName.afterTextChanged {
            val result = validateName(it)
            etContactName.error = result
        }

        etContactNumber.afterTextChanged {
            val result = validateNumber(it)
            etContactNumber.error = result
        }
    }

    private fun validateName(name: String): String? {
        var result: String? = null

        val rawContactString = preferenceManager.loadString("contact")
        val rawHistoryString = preferenceManager.loadString("history")

        if (name.isEmpty()) {
            result = "Name should not be empty"
        } else {
            if (nameToEdit.isNotEmpty()) {
                if (nameToEdit == name) {
                    return "No changes"
                }
            }

            if (rawContactString.isNotEmpty()) {
                val contactFromPreferenceManager = gsonConverter.stringToJSON(rawContactString) as ArrayList<Contact>

                for (contact in contactFromPreferenceManager) {
                    if (contact.contactName == name) {
                        return "Name already exist"
                    }
                }
            }

            if(rawHistoryString.isNotEmpty()) {
                val historyFormPreferenceManager = gsonConverter.stringToJSON(rawHistoryString) as ArrayList<History>

                for (history in historyFormPreferenceManager) {
                    if (history.historyName == name) {
                        return "Name already exist in history"
                    }
                }
            }
        }

        return result
    }

    private fun validateNumber(mobileNumber: String): String? {
        var result: String? = null

        val rawContactString = preferenceManager.loadString("contact")
        val rawHistoryString = preferenceManager.loadString("history")

        if (mobileNumber.isEmpty()) {
            result = "Mobile number should not be empty"
        } else if (mobileNumber.length != 11 || mobileNumber.substring(0, 2) != "09" || !TextUtils.isDigitsOnly(mobileNumber)) {
            result = "Invalid mobile number"
        } else {
            if (mobileNumberToEdit.isNotEmpty()) {
                if (mobileNumberToEdit == mobileNumber) {
                    return "No changes"
                }
            }

            if (rawContactString.isNotEmpty()) {
                val contactFromPreferenceManager = gsonConverter.stringToJSON(rawContactString) as ArrayList<Contact>

                for (contact in contactFromPreferenceManager) {
                    if (contact.contactMobileNumber == mobileNumber) {
                        return "Mobile number already exist"
                    }
                }
            }

            if(rawHistoryString.isNotEmpty()) {
                val historyFormPreferenceManager = gsonConverter.stringToJSON(rawHistoryString) as ArrayList<History>

                for (history in historyFormPreferenceManager) {
                    if (history.historyMobileNumber == mobileNumber) {
                        return "Mobile number already exist in history"
                    }
                }
            }
        }

        return result
    }

    private fun validateValue() {
        val name = etContactName.readText()
        val mobileNumber = etContactNumber.readText()

        Log.d("AddContactActivity", "name === $name")
        Log.d("AddContactActivity", "mobileNum === $mobileNumber")

        etContactName.error = validateName(name)
        etContactNumber.error = validateNumber(mobileNumber)

        etContactName.error?.let {
            return
        }

        etContactNumber.error?.let {
            return
        }

        goToContacts(name, mobileNumber)
    }

    private fun goToContacts(name: String, mobileNumber: String) {
        setResult(Activity.RESULT_OK,
            intentFor<ContactActivity>("name" to name, "mobilenumber" to mobileNumber)
        )

        finish()
    }
}