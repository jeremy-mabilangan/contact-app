package com.jeremymabilangan.ui.contact.ui.calculator

import android.annotation.SuppressLint
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.jeremymabilangan.ui.contact.R
import com.jeremymabilangan.ui.contact.base.BaseActivity
import com.jeremymabilangan.ui.contact.data.DatabaseManager
import com.jeremymabilangan.ui.contact.extra.toNumber
import com.jeremymabilangan.ui.contact.factory.ColorFactory
import kotlinx.android.synthetic.main.activity_calculator.*


/**
 * Created by Ralph Gabrielle Orden on 6/21/21.
 */

class CalculatorActivity: BaseActivity() {

   private lateinit var viewModel: CalculatorViewModel

    override fun layoutId() = R.layout.activity_calculator

    override fun viewCreated() {
        viewModel = ViewModelProvider(this).get(CalculatorViewModel::class.java)

        //
        // Singleton design pattern
        //

        // Factory design pattern

        listenUserEvents()
        observeEvents()

        //
        // repository pattern
        // code discipline
        //

        //
        // data
        // network (api)
        //

        //
        // S (single responsibility) O. L. I. D
        //

        //
        // Local
        // Network
        //


        // Activity
        // Fragment1 give data to activity
        //
        // Activity give data to Fragment2
        // Fragment2 (onResume) load

        // Design Pattern - Creating of instances of an object
        // Singleton
        //

        exampleSingleton()
        exampleFactory()

        // To Add

        // Add Notes
        // Edit Notes
        // Delete Notes
        // Read Notes

        // Room

        // Dependency Injection
    }

    private fun exampleFactory() {
        val factory = ColorFactory()
        val color = factory.createColor("Red")
    }

    private fun exampleSingleton() {
        val dbManager = DatabaseManager.createInstance()
    }

    private fun observeEvents() {
        viewModel.totalViewModel.observe(this, Observer {
            // it?:return@Observer
            // if null observe again

            // end
            displayTotal(it)
        })
    }

    private fun listenUserEvents() {
        bCalculate.setOnClickListener {
            calculate()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun displayTotal(total: Int) {
        tvTotalValue.text = "The total number is $total"
    }

    private fun calculate() {
        val firstNumber = etFirstNumber.toNumber()
        val secondNumber = etSecondNumber.toNumber()

        viewModel.calculate(firstNumber, secondNumber)
    }
}