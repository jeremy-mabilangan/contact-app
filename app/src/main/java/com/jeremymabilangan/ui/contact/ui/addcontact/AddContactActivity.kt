package com.jeremymabilangan.ui.contact.ui.addcontact

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import com.jeremymabilangan.ui.contact.R
import com.jeremymabilangan.ui.contact.base.BaseActivity
import com.jeremymabilangan.ui.contact.extra.afterTextChanged
import com.jeremymabilangan.ui.contact.extra.readText
import com.jeremymabilangan.ui.contact.utils.GSONConverter
import com.jeremymabilangan.ui.contact.utils.PreferenceManager
import kotlinx.android.synthetic.main.activity_add_contact.*

@Suppress("DEPRECATED_IDENTITY_EQUALS")
class AddContactActivity : BaseActivity(), AddContactView {

    private lateinit var addContactPresenter: AddContactPresenter
    private lateinit var preferenceManager : PreferenceManager

    private val gsonConverter = GSONConverter()

    override fun layoutId(): Int {
        return R.layout.activity_add_contact
    }

    override fun viewCreated() {
        addContactPresenter = AddContactPresenterImpl(this, gsonConverter)

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

        addContactPresenter.validateIntent(name, mobileNumber)
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
            validateName(it)
        }

        etContactNumber.afterTextChanged {
            validateNumber(it)
        }
    }

    private fun validateName(name: String) {
        val rawContactString = preferenceManager.loadString("contact")
        val rawHistoryString = preferenceManager.loadString("history")

        addContactPresenter.validateName(name, rawContactString, rawHistoryString)
    }

    private fun validateNumber(mobileNumber: String) {
        val rawContactString = preferenceManager.loadString("contact")
        val rawHistoryString = preferenceManager.loadString("history")

        addContactPresenter.validateMobileNumber(mobileNumber, rawContactString, rawHistoryString)
    }

    private fun validateValue() {
        val name = etContactName.readText()
        val mobileNumber = etContactNumber.readText()

        Log.d("AddContactActivity", "name === $name")
        Log.d("AddContactActivity", "mobileNum === $mobileNumber")

        validateName(name)
        validateNumber(mobileNumber)

        etContactName.error?.let {
            return
        }

        etContactNumber.error?.let {
            return
        }

        goToContacts(name, mobileNumber)
    }

    private fun goToContacts(name: String, mobileNumber: String) {
        val intent = Intent()
        intent.putExtra("name", name)
        intent.putExtra("mobilenumber", mobileNumber)

        setResult(Activity.RESULT_OK, intent)

        finish()
    }

    override fun displayValidatedContactToEdit(name: String, mobileNumber: String) {
        etContactName.setText(name)
        etContactNumber.setText(mobileNumber)
    }

    override fun displayContactNameError(error: String) {
        etContactName.error = error
    }

    override fun displayContactNumberError(error: String) {
        etContactNumber.error = error
    }
}