package com.jeremymabilangan.ui.contact.ui.history2

import android.app.Activity
import android.util.Log
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.afollestad.materialdialogs.MaterialDialog
import com.jeremymabilangan.ui.contact.R
import com.jeremymabilangan.ui.contact.base.BaseFragment
import com.jeremymabilangan.ui.contact.ui.contacts.dataclass.Contact
import com.jeremymabilangan.ui.contact.ui.history.adapter.HistoryAdapter
import com.jeremymabilangan.ui.contact.ui.history.dataclass.History
import com.jeremymabilangan.ui.contact.utils.GSONConverter
import com.jeremymabilangan.ui.contact.utils.PreferenceManager
import com.jeremymabilangan.ui.contact.utils.SaveToPreference
import kotlinx.android.synthetic.main.activity_history.*

class HistoryFragments : BaseFragment(), HistoryView {

    private var historyArray = ArrayList<History>()
    private val historyToRestore = ArrayList<History>()
    private val historyToDelete = ArrayList<History>()

    private val gsonConverter = GSONConverter()
    private val saveToPreference = SaveToPreference()

    private lateinit var preferenceManager : PreferenceManager

    override fun onResume() {
        super.onResume()

        Log.d(requireContext().toString(), "SECOND FRAGMENT")
    }

    override fun layoutId(): Int {
        return R.layout.fragment_history
    }

    override fun viewCreated() {
//        validateIntent()
//        listenToEvents()
        initRecyclerView()
        initPreferenceManager()
        loadHistory()
    }

//    private fun validateIntent() {
//        val history: String ? = intent.getStringExtra("history")
//
//        history?.apply {
//            val newHistoryArray = gsonConverter.stringToJSON(this) as ArrayList<History>
//            historyArray = newHistoryArray
//        }
//    }

    private fun initPreferenceManager() {
        preferenceManager = PreferenceManager(requireContext())
    }

    private fun loadHistory() {
        val rawJSONString = preferenceManager.loadString("history")

        Log.d(requireContext().toString(), "loadHistory => $rawJSONString")

        if (rawJSONString.isNotEmpty()) {

            val historyFromPreferenceManager = gsonConverter.stringToJSON(rawJSONString) as ArrayList<History>

            for (history in historyFromPreferenceManager) {
                historyArray.add(history)
            }
        }
    }

    private fun initRecyclerView() {
        rvHistory.apply {

            adapter = HistoryAdapter(requireContext(), historyArray) { history: History, position: Int ->
                createDialog(history, position)
            }

            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)

            itemAnimator.let {
                if (it is DefaultItemAnimator) {
                    it.supportsChangeAnimations = false
                }
            }

            (this.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        }
    }

//    private fun goToContacts() {
//        setResult(
//            Activity.RESULT_OK
//        )
//
//        finish()
//    }

    private fun createDialog(history: History, position: Int) {
        MaterialDialog(requireContext()).show {
            title(text = "History")
            message(text = "What do you want to this contact: " + history.historyName + "?")
            cancelOnTouchOutside
            positiveButton(text = "Restore") {
                restoreHistory(position)
            }
            negativeButton(text = "Delete") {
                deleteHistory(position)
            }
        }
    }

    private fun deleteHistory(index: Int) {
        historyToDelete.add(historyArray[index])

        historyArray.removeAt(index)
        rvHistory?.adapter?.notifyDataSetChanged()

        saveToPreference.deleteHistory(preferenceManager = preferenceManager, gsonConverter = gsonConverter, historyToDelete =  historyToDelete)
    }

    private fun restoreHistory(index: Int) {
        historyToRestore.add(historyArray[index])

        historyArray.removeAt(index)
        rvHistory?.adapter?.notifyDataSetChanged()

        saveToPreference.restoreHistory(preferenceManager = preferenceManager, gsonConverter = gsonConverter, historyToRestore = historyToRestore)
    }
}