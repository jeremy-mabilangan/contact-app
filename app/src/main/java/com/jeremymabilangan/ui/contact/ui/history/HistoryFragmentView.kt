package com.jeremymabilangan.ui.contact.ui.history

import com.jeremymabilangan.ui.contact.ui.history.dataclass.History

interface HistoryFragmentView {

    fun displayHistoryList(history: ArrayList<History>)

    fun deleteHistory()

    fun restoreHistory()
}