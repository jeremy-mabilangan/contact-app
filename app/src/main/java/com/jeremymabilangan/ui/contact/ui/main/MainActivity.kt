package com.jeremymabilangan.ui.contact.ui.main

import android.R.attr
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.jeremymabilangan.ui.contact.R
import com.jeremymabilangan.ui.contact.base.BaseActivity
import com.jeremymabilangan.ui.contact.base.BaseFragment
import com.jeremymabilangan.ui.contact.base.BaseFragment.Companion.newInstance
import com.jeremymabilangan.ui.contact.extra.emptyString
import com.jeremymabilangan.ui.contact.ui.contacts.ContactsFragment
import com.jeremymabilangan.ui.contact.ui.history2.HistoryFragments
import kotlinx.android.synthetic.main.activity_main2.*


class MainActivity : BaseActivity() {

    private var lastBackStackName = emptyString()

    override fun layoutId(): Int {
        return R.layout.activity_main2
    }

    override fun viewCreated() {
        listenToEvents()

        openFragment(newInstance(ContactsFragment::class.java), "Contacts")
    }

    // onOptionCreated(1, 2)
    // Image (url)
    // Option Dialog
    // enum
    // MVVM
    // Retrofit (suspend)
    // Gradle
    // Variant
    // Build config constant
    // Build.gradle vs project/build.gradle
    // Android Manifest
    // Local properties

    private fun listenToEvents() {
        bottom_navigation.setOnNavigationItemSelectedListener { item ->
            when(item.itemId) {
                R.id.contact_page -> {
                    openFragment(newInstance(ContactsFragment::class.java), "Contacts")
                }
                R.id.history_page -> {
                    openFragment(newInstance(HistoryFragments::class.java), "History")
                }
            }

            true
        }
    }

    private fun openFragment(fragment: Fragment?, backStackName: String) {

        if (lastBackStackName == backStackName) return

       fragment?.apply {

            val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()

            if (supportFragmentManager.findFragmentByTag(backStackName) != null) {
                val baseFragment = supportFragmentManager.findFragmentByTag(backStackName) as BaseFragment
                transaction
                    .show(baseFragment)
                    .commit()
            } else {
                transaction
                    .add(R.id.fragmentContainer, this, backStackName)
                    .commit()
            }

           supportFragmentManager.findFragmentByTag(lastBackStackName)?.apply {
               supportFragmentManager.beginTransaction()
                   .hide(this)
                   .commit()
           }
        }

        lastBackStackName = backStackName
    }
}