package com.jeremymabilangan.ui.contact.ui.lesson

import android.widget.Toast
import com.jeremymabilangan.ui.contact.R
import com.jeremymabilangan.ui.contact.base.BaseActivity
import com.jeremymabilangan.ui.contact.extra.readTextToInt
import kotlinx.android.synthetic.main.activity_lesson.*


/**
 * Created by Ralph Gabrielle Orden on 2/1/21.
 */

class LessonActivity : BaseActivity(), LessonView {

    private lateinit var lessonPresenter : LessonPresenter

    private var numberValue = NumberValue()

    override fun layoutId() = R.layout.activity_lesson

    override fun viewCreated() {
        lessonPresenter = LessonPresenterImpl(this)

        listenUserEvents()
        numberValue.totalNumber()
    }

    private fun listenUserEvents() {
        bDone.setOnClickListener {
            computeValues()
        }
    }

    private fun computeValues() {
        val value1 = etValue1.readTextToInt()
        val value2 = etValue2.readTextToInt()

        lessonPresenter.computeValues(value1, value2)
    }

    override fun displayTotal(total: Int) {
        Toast.makeText(this, "Your total is : $total", Toast.LENGTH_SHORT).show()
    }

}