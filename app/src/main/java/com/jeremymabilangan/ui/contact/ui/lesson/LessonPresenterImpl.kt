package com.jeremymabilangan.ui.contact.ui.lesson


/**
 * Created by Ralph Gabrielle Orden on 2/1/21.
 */

class LessonPresenterImpl(private var lessonView : LessonView) : LessonPresenter {

    //
    // polymorphism
    // abstraction
    // inheritance
    // encapsulation (public, private, protected)

    // Mid :
    //
    // OOP
    // MVP / MVVM
    // UI ()
    // API Integration (JSON)
    // Google Maps, Crashlytics
    // Facebook / Google Social Media (Twitter, Instagram) SDK
    // Shared Pref.
    //
    // AndroidX
    // Livedata (MVVM)


    // Senior
    //
    // RxKotlin or RxJava
    // Kotlin
    // Coroutine
    // DI

    override fun computeValues(value1: Int, value2: Int) {
        val total = value1 + value2

        // Main Thread / UI Thread -> UI update/changes
        // Background Thread -> Process (API) -> UI Change to Main thread

        // Local Broadcast
        // Service

        // call display total
        lessonView.displayTotal(total)
    }
}