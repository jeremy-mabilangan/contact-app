package com.jeremymabilangan.ui.contact.data.repository

import androidx.lifecycle.LiveData
import com.jeremymabilangan.ui.contact.data.Contact
import java.util.ArrayList


/**
 * Created by Ralph Gabrielle Orden on 6/21/21.
 */

interface ContactRepository {

    suspend fun fetchContacts(): LiveData<ArrayList<Contact>>

}