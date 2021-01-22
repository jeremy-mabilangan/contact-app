package com.jeremymabilangan.ui.contact.base

import android.content.Context
import android.os.Bundle
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.jeremymabilangan.ui.contact.ui.history.dataclass.History
import com.jeremymabilangan.ui.contact.utils.PreferenceManager
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

    fun saveDeleteHistoryToPreferenceManager(preferenceManager: PreferenceManager, historyToDelete: ArrayList<History>) {
        val toString = convertJSONToString(historyToDelete)

        preferenceManager.saveString(key = "delete_history", string = toString)
    }

    fun saveRestoreHistoryToPreferenceManager(preferenceManager: PreferenceManager, historyToRestore: ArrayList<History>) {
        val toString = convertJSONToString(historyToRestore)

        preferenceManager.saveString(key = "restore_history", string = toString)
    }

    private fun convertJSONToString(json: Any?): String {
        return Gson().toJson(json)
    }
}