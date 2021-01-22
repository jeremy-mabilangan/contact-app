package com.jeremymabilangan.ui.contact.base

import android.content.Context
import android.os.Bundle
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jeremymabilangan.ui.contact.ui.history.dataclass.History
import com.jeremymabilangan.ui.contact.ui.main.dataclass.Contact
import kotlin.collections.ArrayList

abstract class BaseActivity : AppCompatActivity(), ConstructView {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        layoutId().let {
            setContentView(it)
            viewCreated()
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        clearFocusOnOutsideClick()
        return super.dispatchTouchEvent(ev)
    }

    private fun clearFocusOnOutsideClick() {
        currentFocus?.apply {
            if (this is EditText) {
                clearFocus()
            }

            val imm: InputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }

    fun convertContactStringToJSON(string: String) : ArrayList<Contact> {
        val arrayContactType = object : TypeToken<ArrayList<Contact>>() {}.type

        return Gson().fromJson(string, arrayContactType)
    }

    fun convertHistoryStringToJSON(string: String) : ArrayList<History> {
        val arrayContactType = object : TypeToken<ArrayList<History>>() {}.type

        return Gson().fromJson(string, arrayContactType)
    }

    fun convertJSONToString(json: Any?): String {
        return Gson().toJson(json)
    }

    fun convertStringToObject(contactString: String): Contact {

        return Gson().fromJson(contactString, Contact::class.java)
    }
}