package com.jeremymabilangan.ui.contact.ui.history

import com.jeremymabilangan.ui.contact.ui.history.dataclass.History
import com.jeremymabilangan.ui.contact.utils.GSONConverter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HistoryFragmentPresenterImpl(private var historyFragmentView: HistoryFragmentView,
                                   private var gsonConverter: GSONConverter
) : HistoryFragmentPresenter {

    override fun convertHistoryStringToObject(rawJSONString: String) {
        if (rawJSONString.isNotEmpty()) {
            val historyFromPreferenceManager = gsonConverter.stringToJSON(rawJSONString) as ArrayList<History>

            historyFragmentView.displayHistoryList(history = historyFromPreferenceManager)
        }
    }

    override fun validateHistoryCount(count: Int, validateHistoryView: () -> Unit) {
        GlobalScope.launch(Dispatchers.Main) {

            delay(500)

            if (count == 0) {
                validateHistoryView()
            }
        }

        historyFragmentView.deleteHistory()
    }

    override fun validateRestoreHistoryCount(count: Int, validateHistoryView: () -> Unit) {
        GlobalScope.launch(Dispatchers.Main) {

            delay(500)

            if (count == 0) {
                validateHistoryView()
            }
        }

        historyFragmentView.restoreHistory()
    }

}