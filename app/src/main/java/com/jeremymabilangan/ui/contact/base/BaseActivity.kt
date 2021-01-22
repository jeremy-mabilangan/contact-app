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
import com.jeremymabilangan.ui.contact.util.PreferenceManager
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

    inline fun <reified T> convertStringToJSON(string: String): T {
        val typeToken = object : TypeToken<T>() {}.type

        return Gson().fromJson(string, typeToken)
    }

    inline fun <reified T> convertStringToObject(contactString: String): T {
        return Gson().fromJson(contactString, T::class.java)
    }

    fun convertJSONToString(json: Any?): String {
        return Gson().toJson(json)
    }

    fun saveDeleteHistoryToPreferenceManager(preferenceManager: PreferenceManager, historyToDelete: ArrayList<History>) {
        val toString = convertJSONToString(historyToDelete)

        preferenceManager.saveString(key = "delete_history", string = toString)
    }

    fun saveRestoreHistoryToPreferenceManager(preferenceManager: PreferenceManager, historyToRestore: ArrayList<History>) {
        val toString = convertJSONToString(historyToRestore)

        preferenceManager.saveString(key = "restore_history", string = toString)
    }
}