package com.jeremymabilangan.ui.contact.ui.contacts.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.jeremymabilangan.ui.contact.R
import com.jeremymabilangan.ui.contact.ui.contacts.dataclass.Contact
import kotlinx.android.synthetic.main.row_contact.view.*
import java.util.*

@Suppress("NAME_SHADOWING")
class ContactAdapter(private var context: Context, private val contacts: List<Contact>,
                     private val onSelectContact: (Contact) -> Unit,
                     private val onDeleteContact: (Contact, Int) -> Unit,
                     private val onGoToEditContact: (Contact, Int) -> Unit
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

        holder.selectContact(contact) {
            onSelectContact(it)
        }

        holder.deleteContact(contact, position) { contact: Contact, position: Int ->
            onDeleteContact(contact, position)
        }

        holder.goToEditContact(contact, position) { contact: Contact, position: Int ->
            onGoToEditContact(contact, position)
        }

        holder.selectContact {
            Toast.makeText(context, "test", Toast.LENGTH_LONG).show()
        }
    }

    override fun getItemCount() = contactFilterList.size

    class ContactViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        @SuppressLint("SetTextI18n")
        fun displayInformation(contact: Contact) {
            itemView.tvContactName.text = contact.contactName
            itemView.tvContactMobileNumber.text = contact.contactMobileNumber
        }

        fun selectContact(contact: Contact, onSelectContact: (Contact) -> Unit) {
            itemView.setOnClickListener {
                onSelectContact(contact)
            }
        }

        fun deleteContact(contact: Contact, position: Int, onDeleteContact: (Contact, Int) -> Unit) {
            itemView.bDeleteContact.setOnClickListener {
                onDeleteContact(contact, position)
            }
        }

        fun goToEditContact(contact: Contact, position: Int, onGoToEditContact: (Contact, Int) -> Unit) {
            itemView.bEditContact.setOnClickListener {
                onGoToEditContact(contact, position)
            }
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