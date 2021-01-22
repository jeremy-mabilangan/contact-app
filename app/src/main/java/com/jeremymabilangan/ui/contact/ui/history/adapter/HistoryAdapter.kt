package com.jeremymabilangan.ui.contact.ui.history.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.jeremymabilangan.ui.contact.R
import com.jeremymabilangan.ui.contact.ui.history.dataclass.History
import kotlinx.android.synthetic.main.history_list.view.*

@Suppress("NAME_SHADOWING")
class HistoryAdapter(private val context: Context,
                     private val histories: List<History>,
                     private val onDeleteHistory: (Int) -> Unit,
                     private val onRestoreHistory: (Int) -> Unit
): RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.history_list,
            parent, false)

        return HistoryViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val history = histories[position]

        holder.displayInformation(history)

        holder.deleteHistory(position) {
            onDeleteHistory(it)
        }

        holder.restoreHistory(position) {
            onRestoreHistory(it)
        }
    }

    override fun getItemCount() = histories.size

    class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun displayInformation(history: History) {
            itemView.tvHistoryName.text = history.historyName
            itemView.tvHistoryMobileNumber.text = history.historyMobileNumber
        }

        fun deleteHistory(position: Int, onDeleteHistory: (Int) -> Unit) {
            itemView.bDeleteHistory.setOnClickListener {
                onDeleteHistory(position)
            }
        }

        fun restoreHistory(position: Int, onRestoreHistory: (Int) -> Unit) {
            itemView.bRestoreHistory.setOnClickListener {
                onRestoreHistory(position)
            }
        }
    }
}