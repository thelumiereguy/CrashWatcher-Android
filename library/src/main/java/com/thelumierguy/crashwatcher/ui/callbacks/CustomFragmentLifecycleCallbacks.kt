package com.thelumierguy.crashwatcher.ui.callbacks

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.google.gson.GsonBuilder
import com.thelumierguy.crashwatcher.data.FragmentData
import com.thelumierguy.crashwatcher.utils.toKeyValuesListPair

class CustomFragmentLifecycleCallbacks(val onFragmentAdded: (FragmentData) -> Unit) {

    private val gson = GsonBuilder().setPrettyPrinting().create()

    private val fragmentLifecycleCallbacks = object : FragmentManager.FragmentLifecycleCallbacks() {


        override fun onFragmentViewCreated(
            fm: FragmentManager,
            f: Fragment,
            v: View,
            savedInstanceState: Bundle?
        ) {
            val args = f.arguments.toKeyValuesListPair(gson)
            onFragmentAdded(
                FragmentData(
                    f.javaClass.canonicalName,
                    System.currentTimeMillis(),
                    args.first,
                    args.second
                )
            )
            super.onFragmentViewCreated(fm, f, v, savedInstanceState)
        }
    }


    fun registerFragmentLifecycleCallbacks(activity: Activity) {
        if (activity is FragmentActivity) {
            activity.supportFragmentManager.registerFragmentLifecycleCallbacks(
                fragmentLifecycleCallbacks,
                false
            )
        }
    }

    fun unregisterFragmentLifecycleCallbacks(activity: Activity) {
        if (activity is FragmentActivity) {
            activity.supportFragmentManager.unregisterFragmentLifecycleCallbacks(
                fragmentLifecycleCallbacks
            )
        }
    }
}