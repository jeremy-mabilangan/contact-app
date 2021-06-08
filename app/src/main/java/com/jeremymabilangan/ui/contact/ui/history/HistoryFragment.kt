package com.jeremymabilangan.ui.contact.ui.history

import android.util.Log
import android.view.View
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.afollestad.materialdialogs.MaterialDialog
import com.jeremymabilangan.ui.contact.R
import com.jeremymabilangan.ui.contact.base.BaseFragment
import com.jeremymabilangan.ui.contact.ui.history.adapter.HistoryAdapter
import com.jeremymabilangan.ui.contact.ui.history.dataclass.History
import com.jeremymabilangan.ui.contact.utils.GSONConverter
import com.jeremymabilangan.ui.contact.utils.PreferenceManager
import com.jeremymabilangan.ui.contact.utils.SaveToPreference
import kotlinx.android.synthetic.main.fragment_history.*

class HistoryFragment : BaseFragment(), HistoryFragmentView {

    private var historyArray = ArrayList<History>()
    private val historyToRestore = ArrayList<History>()
    private val historyToDelete = ArrayList<History>()

    private val gsonConverter = GSONConverter()
    private val saveToPreference = SaveToPreference()

    private lateinit var preferenceManager : PreferenceManager
    private lateinit var historyFragmentPresenter: HistoryFragmentPresenter

    override fun layoutId(): Int {
        return R.layout.fragment_history
    }

    override fun viewCreated() {
        historyFragmentPresenter = HistoryFragmentPresenterImpl(this, gsonConverter = gsonConverter)

        initRecyclerView()
        initPreferenceManager()
        loadHistory()

        validateHistoryView()
    }

    private fun initPreferenceManager() {
        preferenceManager = PreferenceManager(requireContext())
    }

    private fun loadHistory() {
        val rawJSONString = preferenceManager.loadString("history")

        Log.d(requireContext().toString(), "loadHistory => $rawJSONString")

        historyFragmentPresenter.convertHistoryStringToObject(rawJSONString = rawJSONString)
    }

    override fun displayHistoryList(history: ArrayList<History>) {
        historyArray.addAll(history)
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

    private fun createDialog(history: History, position: Int) {
        MaterialDialog(requireContext()).show {
            title(text = "History")
            message(text = "What do you want to this contact: " + history.historyName + "?")
            cancelOnTouchOutside
            positiveButton(text = "Restore") {
                toRestoreHistory(position)
            }
            negativeButton(text = "Delete") {
                toDeleteHistory(position)
            }
        }
    }

    private fun toDeleteHistory(index: Int) {
        historyToDelete.add(historyArray[index])

        historyArray.removeAt(index)

        var historyCountList = 0

        rvHistory?.adapter?.apply {
            notifyItemRemoved(index)
            notifyItemRangeChanged(index, historyArray.size)
            historyCountList = itemCount
        }

        historyFragmentPresenter.validateHistoryCount(count = historyCountList, validateHistoryView = { validateHistoryView() })
    }

    override fun deleteHistory() {
        saveToPreference.deleteHistory(preferenceManager = preferenceManager, gsonConverter = gsonConverter, historyToDelete =  historyToDelete)
    }

    private fun toRestoreHistory(index: Int) {
        historyToRestore.add(historyArray[index])

        historyArray.removeAt(index)

        var historyCountList = 0

        rvHistory?.adapter?.apply {
            notifyItemRemoved(index)
            notifyItemRangeChanged(index, historyArray.size)
            historyCountList = itemCount
        }

        historyFragmentPresenter.validateRestoreHistoryCount(count = historyCountList, validateHistoryView = { validateHistoryView() })

    }

    override fun restoreHistory() {
        saveToPreference.restoreHistory(preferenceManager = preferenceManager, gsonConverter = gsonConverter, historyToRestore = historyToRestore)
    }

    private fun validateHistoryView() {
        val historyCountList = rvHistory?.adapter?.itemCount
        if (historyCountList == 0) {
            rvHistory.visibility = View.GONE
            ivIconNoHistoryFound.visibility = View.VISIBLE
            tvNoHistoryFound.visibility = View.VISIBLE
        } else {
            rvHistory.visibility = View.VISIBLE
            ivIconNoHistoryFound.visibility = View.GONE
            tvNoHistoryFound.visibility = View.GONE
        }
    }

}