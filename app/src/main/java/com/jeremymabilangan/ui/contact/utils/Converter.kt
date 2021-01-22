package com.jeremymabilangan.ui.contact.utils

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converter {
    inline fun <reified T> stringToJSON(string: String): T {
        return Gson().fromJson(string,  object : TypeToken<T>() {}.type)
    }

    inline fun <reified T> stringToObject(string: String): T {
        return Gson().fromJson(string, T::class.java)
    }

    fun jsonToString(json: Any?): String {
        return Gson().toJson(json)
    }
}