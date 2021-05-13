package com.jeremymabilangan.ui.contact.ui.history.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jeremymabilangan.ui.contact.R
import com.jeremymabilangan.ui.contact.ui.history.dataclass.History
import kotlinx.android.synthetic.main.row_history.view.*


@Suppress("NAME_SHADOWING")
class HistoryAdapter(private val context: Context,
                     private val histories: List<History>,
                     private var onOptionDialog : (history: History, position: Int) -> Unit
): RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.row_history,
            parent, false)

        return HistoryViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val history = histories[position]

        holder.displayInformation(history)

        holder.selectHistory {
            onOptionDialog(history, position)
        }
    }

    override fun getItemCount() = histories.size

    class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun displayInformation(history: History) {
            itemView.tvHistoryName.text = history.historyName
            itemView.tvHistoryMobileNumber.text = history.historyMobileNumber
        }

        fun selectHistory(onSelected: () -> Unit) {
            itemView.containerHistory.setOnClickListener {
                onSelected()
            }
        }
    }
}