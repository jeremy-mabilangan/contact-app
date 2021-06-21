package com.jeremymabilangan.ui.contact.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jeremymabilangan.ui.contact.data.Contact
import java.util.ArrayList


/**
 * Created by Ralph Gabrielle Orden on 6/21/21.
 */

class ContactRepositoryImpl: ContactRepository {

    override suspend fun fetchContacts(): LiveData<ArrayList<Contact>> {
        //
        // API
        //

        // RETROFIT
        // suspend

        val liveData = MutableLiveData<ArrayList<Contact>>()
        val contacts = ArrayList<Contact>()

        contacts.add(Contact("Ralph", "12345"))
        contacts.add(Contact("Jeremy", "67890"))

        liveData.postValue(contacts)
        return liveData
    }

}