package com.jeremymabilangan.ui.contact.data

enum class ErrorType(val value: String) {

    NAME_IS_EMPTY("Name should not be empty"),
    NAME_ALREADY_EXIST("Name already exist"),
    NAME_ALREADY_IS_IN_HISTORY("Name already exist in history"),

    MOBILE_NUMBER_IS_EMPTY("Mobile number should not be empty"),
    MOBILE_NUMBER_IS_INVALID("Invalid mobile number"),
    MOBILE_NUMBER_ALREADY_EXIST("Mobile number already exist"),
    MOBILE_NUMBER_ALREADY_IS_IN_HISTORY("Mobile number already exist in history")
}