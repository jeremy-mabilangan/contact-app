package com.jeremymabilangan.ui.contact.ui.history

import android.app.Activity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.jeremymabilangan.ui.contact.R
import com.jeremymabilangan.ui.contact.base.BaseActivity
import com.jeremymabilangan.ui.contact.ui.history.adapter.HistoryAdapter
import com.jeremymabilangan.ui.contact.ui.history.dataclass.History
import com.jeremymabilangan.ui.contact.util.PreferenceManager
import kotlinx.android.synthetic.main.activity_history.*

class HistoryActivity : BaseActivity() {

    private var historyArray = ArrayList<History>()
    private val historyToRestore = ArrayList<History>()
    private val historyToDelete = ArrayList<History>()

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
            val newHistoryArray = convertHistoryStringToJSON(this)
            historyArray = newHistoryArray
        }
    }

    private fun initPreferenceManager() {
        preferenceManager = PreferenceManager(context = this)
    }

    private fun initRecyclerView() {
        rvHistory.apply {

            adapter = HistoryAdapter(this@HistoryActivity, historyArray , {
                deleteHistory(it)
            }, { position: Int ->
                restoreHistory(position)
            })

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

        saveDeleteHistoryToPreferenceManager(historyToDelete)
    }

    private fun restoreHistory(index: Int) {
        historyToRestore.add(historyArray[index])

        historyArray.removeAt(index)
        rvHistory?.adapter?.notifyDataSetChanged()

        saveRestoreHistoryToPreferenceManager(historyToRestore)
    }

    private fun saveDeleteHistoryToPreferenceManager(historyToDelete: ArrayList<History>) {
        val toString = convertJSONToString(historyToDelete)

        preferenceManager.saveString(key = "delete_history", string = toString)
    }

    private fun saveRestoreHistoryToPreferenceManager(restoreHistory: ArrayList<History>) {
        val toString = convertJSONToString(restoreHistory)

        preferenceManager.saveString(key = "restore_history", string = toString)
    }
}