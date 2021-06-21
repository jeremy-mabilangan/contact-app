package com.jeremymabilangan.ui.contact.ui.calculator

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch


/**
 * Created by Ralph Gabrielle Orden on 6/21/21.
 */

class CalculatorViewModel: ViewModel() {

    //
    // Model View - ViewModel
    //

    //
    // Live Data
    // - Will be consume by View (Activity / Fragment)
    //

    //
    // Mutable Live Data
    // - Will be updated by ViewModel
    //

    private val mutableTotalViewModel = MutableLiveData<Int>()

    val totalViewModel: LiveData<Int>
         get() = mutableTotalViewModel

    fun calculate(firstNumber: Int, secondNumber: Int) {
        val totalValue = firstNumber + secondNumber
        mutableTotalViewModel.postValue(totalValue)
    }

    fun fetchContacts() {
        // api call
        // RETROFIT

        // Coroutine
        // Service background
        // Simplified multi threading
        // light weight

        viewModelScope.launch {
            Log.d("Ralph 1", "fetchContacts: ")
            //contactRepository.fetchContacts()
            Log.d("Ralph 2", "fetchContacts: ")
        }

        Log.d("Ralph 3", "fetchContacts: ")

        // solo/individual
        // shared

        // ways to use mvvm
        // ** RxJava / RxKotlin **
        // LiveData - Coroutine
    }

    fun loadContacts() {

        // load sql contacts

        // SELECT *
    }

}