package com.jeremymabilangan.ui.contact.ui.history

import android.app.Activity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.jeremymabilangan.ui.contact.R
import com.jeremymabilangan.ui.contact.base.BaseActivity
import com.jeremymabilangan.ui.contact.ui.history.adapter.HistoryAdapter
import com.jeremymabilangan.ui.contact.ui.history.dataclass.History
import com.jeremymabilangan.ui.contact.utils.GSONConverter
import com.jeremymabilangan.ui.contact.utils.PreferenceManager
import com.jeremymabilangan.ui.contact.utils.SaveToPreference
import kotlinx.android.synthetic.main.activity_history.*

class HistoryActivity : BaseActivity() {

    private var historyArray = ArrayList<History>()
    private val historyToRestore = ArrayList<History>()
    private val historyToDelete = ArrayList<History>()

    private val gsonConverter = GSONConverter()
    private val saveToPreference = SaveToPreference()

    private lateinit var preferenceManager : PreferenceManager

    override fun layoutId(): Int {
        return R.layout.activity_history
    }

    override fun viewCreated() {
        validateIntent()
        listenToEvents()
        initRecyclerView()
        initPreferenceManager()
    }

    private fun listenToEvents() {
        bHistoryBack.setOnClickListener {
            goToContacts()
        }
    }

    private fun validateIntent() {
        val history: String ? = intent.getStringExtra("history")

        history?.apply {
            val newHistoryArray = gsonConverter.stringToJSON(this) as ArrayList<History>
            historyArray = newHistoryArray
        }
    }

    private fun initPreferenceManager() {
        preferenceManager = PreferenceManager(context = this)
    }

    private fun initRecyclerView() {
        rvHistory.apply {

            adapter = HistoryAdapter(this@HistoryActivity, historyArray) { history: History, position: Int ->
//                createDialog(history, position)
            }

            layoutManager = LinearLayoutManager(this@HistoryActivity)
            setHasFixedSize(true)

            itemAnimator.let {
                if (it is DefaultItemAnimator) {
                    it.supportsChangeAnimations = false
                }
            }

            (this.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        }
    }

    private fun goToContacts() {
        setResult(
            Activity.RESULT_OK
        )

        finish()
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