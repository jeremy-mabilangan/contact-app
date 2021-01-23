package com.jeremymabilangan.ui.contact.utils

import com.jeremymabilangan.ui.contact.ui.history.dataclass.History

class SaveToPreference {

    fun deleteHistory(preferenceManager: PreferenceManager, gsonConverter: GSONConverter, historyToDelete: ArrayList<History>) {
        val toString = gsonConverter.jsonToString(historyToDelete)

        preferenceManager.saveString(key = "delete_history", string = toString)
    }

    fun restoreHistory(preferenceManager: PreferenceManager, gsonConverter: GSONConverter, historyToRestore: ArrayList<History>) {
        val toString = gsonConverter.jsonToString(historyToRestore)

        preferenceManager.saveString(key = "restore_history", string = toString)
    }
}