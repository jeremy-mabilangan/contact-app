package com.jeremymabilangan.ui.contact.util

import android.content.Context
import android.content.SharedPreferences
import com.jeremymabilangan.ui.contact.extra.emptyString
 
class PreferenceManager constructor(context: Context) {

    companion object {
        const val SHARED_PREFERENCE_NAME = "com.jeremymabilangan.ui.contact.SHARED_PREFERENCE_NAME"
    }

    private var pref: SharedPreferences

    init {
        pref = context.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE)
    }

    fun clearAll() {
        pref.edit().apply {
            clear()
            apply()
        }
    }

    fun saveString(string: String, key: String) {
        pref.edit().apply {
            putString(key, string)
            apply()
        }
    }

    fun loadString(key: String): String {
        return pref.getString(key, emptyString())!!
    }

    fun saveBoolean(value: Boolean, key: String) {
        pref.edit().apply {
            putBoolean(key, value)
            apply()
        }
    }

    fun set(pair: Pair<String, Any?>) {
        pref.edit().apply{
            when (val value = pair.second) {
                is String -> putString(pair.first, value)
                is Int -> putInt(pair.first, value)
                is Boolean -> putBoolean(pair.first, value)
            }

            apply()
        }
    }

    fun <T> get(key: String, defaultValue: T) : Any? {
        return when (defaultValue) {
            is String -> return pref.getString(key, defaultValue)
            is Int -> return pref.getInt(key, defaultValue)
            is Boolean -> return pref.getBoolean(key, defaultValue)
            else -> null
        }
    }
}