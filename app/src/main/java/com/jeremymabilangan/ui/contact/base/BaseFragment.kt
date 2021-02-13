package com.jeremymabilangan.ui.contact.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment


abstract class BaseFragment: Fragment(), ConstructView {

    companion object {
        fun <T : BaseFragment?> newInstance(clazz: Class<T>, vararg params: Pair<String, Any?>): T? {
            try {
                val newInstance = clazz.newInstance()

                Bundle().let { bundle ->
                    params.forEach {
                        when (val value = it.second) {
                            is String ->  bundle.putString(it.first, value)
                            is Int -> bundle.putInt(it.first, value)
                            is Double -> bundle.putDouble(it.first, value)
                            is Boolean -> bundle.putBoolean(it.first, value)
                        }
                    }

                    newInstance!!.arguments = bundle
                }
                return newInstance
            } catch (e: java.lang.InstantiationException) {
                e.printStackTrace()
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            }
            return null
        }

        fun <T : BaseFragment?> createInstance(clazz: Class<T>): T? {
            return newInstance(clazz, "string" to 1)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        layoutId().let {
            return inflater.inflate(it, container, false)
        }

        return null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewCreated()
    }
}