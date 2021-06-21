package com.jeremymabilangan.ui.contact.data


/**
 * Created by Ralph Gabrielle Orden on 6/21/21.
 */

data class Contact(

    val contactName: String,

    val contactNumber: String

)

class DatabaseManager {

    companion object {

        private var instance: DatabaseManager? = null

        fun createInstance(): DatabaseManager {
            if (instance == null) {
                instance = DatabaseManager()
            }

            return instance!!
        }
    }
}