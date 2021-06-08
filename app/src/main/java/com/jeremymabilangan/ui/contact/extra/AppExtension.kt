package com.jeremymabilangan.ui.contact.extra

import android.widget.EditText

fun EditText.readText(): String {
    return this.text.toString()
}

fun EditText.readTextToInt(): Int {
    if (this.readText().isEmpty()) return 0
    return this.readText().toInt()
}

fun emptyString() = ""