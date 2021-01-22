package com.jeremymabilangan.ui.contact.extra

import android.widget.EditText

fun EditText.readText(): String {
    return this.text.toString()
}

fun emptyString() = ""