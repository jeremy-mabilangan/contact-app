package com.jeremymabilangan.ui.contact.base

import android.content.Context
import android.os.Bundle
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity : AppCompatActivity(), ConstructView {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        layoutId().let {
            setContentView(it)
            viewCreated()
        }
    }

    // onCreateView -> creating of fragment UI view -> layoutId()
    // onViewCreated -> fragment UI view is created -> viewCreated()

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
}