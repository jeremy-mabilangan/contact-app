package com.jeremymabilangan.ui.contact.ui.history2

import com.jeremymabilangan.ui.contact.ui.history.dataclass.History

interface HistoryFragmentView {

    fun displayHistoryList(history: ArrayList<History>)

    fun deleteHistory()

    fun restoreHistory()
}