package com.jeremymabilangan.ui.contact.ui.history

interface HistoryFragmentPresenter{

    fun convertHistoryStringToObject(rawJSONString: String)

    fun validateHistoryCount(count: Int, validateHistoryView: () -> Unit)

    fun validateRestoreHistoryCount(count: Int, validateHistoryView: () -> Unit)
}