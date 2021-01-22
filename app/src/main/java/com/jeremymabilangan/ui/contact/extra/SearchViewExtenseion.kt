package com.jeremymabilangan.ui.contact.extra

import androidx.appcompat.widget.SearchView

fun SearchView.afterSearchViewTextChange(whenQueryTextChange: (String) -> Unit) {
    this.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
        override fun onQueryTextSubmit(query: String?): Boolean {
            return false
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            whenQueryTextChange(newText.toString())
            return false
        }
    })
}