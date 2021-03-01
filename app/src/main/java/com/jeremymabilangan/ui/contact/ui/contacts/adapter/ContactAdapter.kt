package com.jeremymabilangan.ui.contact.ui.contacts.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.jeremymabilangan.ui.contact.R
import com.jeremymabilangan.ui.contact.ui.contacts.dataclass.Contact
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.row_contact.view.*
import java.util.*


@Suppress("NAME_SHADOWING")
class ContactAdapter(private var context: Context, private val contacts: List<Contact>,
                     private var onOptionDialog : (contact: Contact, position: Int) -> Unit
): RecyclerView.Adapter<ContactAdapter.ContactViewHolder>(), Filterable {

    private var contactFilterList = contacts

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.row_contact,
            parent, false)

        return ContactViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        Log.d("ContactAdapter", "contactFilterList $contactFilterList")

        val contact = contactFilterList[position]

        holder.displayInformation(contact)

        holder.selectContact {
            onOptionDialog(contact, position)
        }
    }

    override fun getItemCount() = contactFilterList.size

    class ContactViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        @SuppressLint("SetTextI18n")
        fun displayInformation(contact: Contact) {
            itemView.tvContactName.text = contact.contactName
            itemView.tvContactMobileNumber.text = contact.contactMobileNumber
        }

        fun selectContact(onSelected: () -> Unit) {
            itemView.containerContact.setOnClickListener {
                onSelected()
            }
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()

                contactFilterList = if (charSearch.isEmpty()) {
                    contacts
                } else {
                    val resultList = ArrayList<Contact>()
                    for (row in contacts) {
                        if (row.contactName.toLowerCase(Locale.ROOT).contains(charSearch.toLowerCase(Locale.ROOT)) or
                            row.contactMobileNumber.contains(charSearch)) {
                            resultList.add(row)
                        }
                    }
                    resultList
                }

                Log.d("ContactAdapter", contactFilterList.toString())

                return FilterResults().apply { values = contactFilterList }
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                contactFilterList = results?.values as ArrayList<Contact>

                notifyDataSetChanged()
            }
        }
    }
}