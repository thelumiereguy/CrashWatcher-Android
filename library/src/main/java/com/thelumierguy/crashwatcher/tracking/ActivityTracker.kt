package com.thelumierguy.crashwatcher.tracking

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.google.gson.GsonBuilder
import com.thelumierguy.crashwatcher.ui.adapters.data.ActivityData
import com.thelumierguy.crashwatcher.ui.adapters.data.FragmentData
import com.thelumierguy.crashwatcher.ui.activity.CrashDetailsActivity
import com.thelumierguy.crashwatcher.ui.callbacks.CustomActivityCallbacks
import com.thelumierguy.crashwatcher.ui.callbacks.CustomFragmentLifecycleCallbacks
import com.thelumierguy.crashwatcher.utils.toKeyValuesListPair

internal class ActivityTracker(private val activityObserverCallback: ActivityObserverCallback) {

    private val gson by lazy(LazyThreadSafetyMode.NONE) {
        GsonBuilder().setPrettyPrinting().create()
    }

    val activityList = mutableListOf<ActivityData>()

    val fragmentList = mutableListOf<FragmentData>()


    private val customFragmentLifecycleCallbacks = CustomFragmentLifecycleCallbacks {
        fragmentList.add(it)
    }

    fun track(application: Application?) {
        application?.registerActivityLifecycleCallbacks(
            object : CustomActivityCallbacks() {

                override fun onActivityPreCreated(activity: Activity, savedInstanceState: Bundle?) {
                    super.onActivityPreCreated(activity, savedInstanceState)
                    customFragmentLifecycleCallbacks.registerFragmentLifecycleCallbacks(activity)
                }

                override fun onActivityCreated(
                    activity: Activity,
                    savedInstanceState: Bundle?
                ) {
                    // Copied from ACRA:
                    // Ignore activityClass because we want the last
                    // application Activity that was started so that we can
                    // explicitly kill it off.
                    if (activity.javaClass != CrashDetailsActivity::class.java) {

                        activityObserverCallback.onActivityCreated(
                            activity,
                            System.currentTimeMillis()
                        )
                        val intentData = activity.intent.toKeyValuesListPair(gson)
                        activityList.add(
                            ActivityData(
                                activity::class.java.simpleName,
                                System.currentTimeMillis(),
                                intentKeys = intentData.first,
                                intentValues = intentData.second
                            )
                        )
                    }
                }

                override fun onActivityDestroyed(activity: Activity) {
                    customFragmentLifecycleCallbacks.unregisterFragmentLifecycleCallbacks(activity)
                }
            })
    }
}

