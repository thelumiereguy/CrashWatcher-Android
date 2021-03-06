package com.thelumierguy.crashwatcher

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Process
import android.os.Process.myPid
import android.util.Log
import androidx.annotation.RestrictTo
import com.google.gson.GsonBuilder
import com.thelumierguy.crashwatcher.data.ActivityData
import com.thelumierguy.crashwatcher.data.ActivityHistoryData
import com.thelumierguy.crashwatcher.data.FragmentData
import com.thelumierguy.crashwatcher.data.FragmentHistoryData
import com.thelumierguy.crashwatcher.ui.CrashWatcherActivity
import com.thelumierguy.crashwatcher.ui.callbacks.CustomActivityCallbacks
import com.thelumierguy.crashwatcher.ui.callbacks.CustomFragmentLifecycleCallbacks
import com.thelumierguy.crashwatcher.utils.TrackingList
import com.thelumierguy.crashwatcher.utils.toKeyValuesListPair
import java.io.*
import java.lang.ref.WeakReference
import kotlin.system.exitProcess


object CrashWatcher {
    private const val MAX_STACK_TRACE_SIZE = 131071

    val TAG = "CrashWatcher"

    private val gson by lazy(LazyThreadSafetyMode.NONE) {
        GsonBuilder().setPrettyPrinting().create()
    }

    private const val TIME_TO_CONSIDER_ACTIVITY_CREATED_MS = 100
    private var lastActivityCreatedTimestamp = 0L
    private lateinit var lastActivityCreated: WeakReference<Activity>


    private val activityList = TrackingList<ActivityData>(5)
    private val fragmentList = TrackingList<FragmentData>(5)

    private var application: Application? = null

    val customFragmentLifecycleCallbacks = CustomFragmentLifecycleCallbacks {
        fragmentList.add(it)
    }

    @RestrictTo(RestrictTo.Scope.LIBRARY)
    fun initCrashWatcher(context: Context?) {
        try {
            context?.let {
                val defaultExceptionHandler = Thread.getDefaultUncaughtExceptionHandler()
                application = context.applicationContext as Application
                setCustomExceptionHandler(defaultExceptionHandler)
                initActivityTracker()
            }
        } catch (t: Throwable) {
            Log.e(
                TAG,
                "An unknown error occurred while installing CustomActivityOnCrash, " +
                        "it may not have been properly initialized. Please report this as a bug if needed.",
                t
            )
        }
    }

    private fun setCustomExceptionHandler(defaultExceptionHandler: Thread.UncaughtExceptionHandler?) {
        Thread.setDefaultUncaughtExceptionHandler(object : Thread.UncaughtExceptionHandler {
            override fun uncaughtException(p0: Thread, p1: Throwable) {
                if (isStackTraceConflicting(p1)) {
                    Log.e(
                        TAG,
                        "Your application class or your error activity have crashed, the custom activity will not be launched!"
                    )
                    if (defaultExceptionHandler != null) {
                        defaultExceptionHandler.uncaughtException(p0, p1)
                        return
                    }
                } else if (System.currentTimeMillis() - lastActivityCreatedTimestamp >= TIME_TO_CONSIDER_ACTIVITY_CREATED_MS) {
                    startCrashReportActivity(p1)
                } else {
                    if (defaultExceptionHandler != null) {
                        defaultExceptionHandler.uncaughtException(p0, p1)
                        return
                    }
                }
                if (::lastActivityCreated.isInitialized) {
                    val lastActivity = lastActivityCreated.get()
                    if (lastActivity != null) {
                        //We finish the activity, this solves a bug which causes infinite recursion.
                        //See: https://github.com/ACRA/acra/issues/42
                        lastActivity.finish()
                        lastActivityCreated.clear()
                    }
                }
                Process.killProcess(myPid())
                exitProcess(10)
            }
        })
    }

    private fun startCrashReportActivity(throwable: Throwable) {
        val sw = StringWriter()
        val pw = PrintWriter(sw)
        throwable.printStackTrace(pw)
        val stackTraceString = sw.toString().apply {
            trimStackTrace(this)
        }
        val intent = Intent(application, CrashWatcherActivity::class.java).apply {
            putExtra(EXTRA_CRASH_LOG, stackTraceString)
            if (activityList.isNotEmpty()) {
                putExtra(EXTRA_ACTIVITY_HISTORY, ActivityHistoryData(activityList))
            }
            if (fragmentList.isNotEmpty()) {
                putExtra(EXTRA_FRAGMENT_HISTORY, FragmentHistoryData(fragmentList))
            }
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        application?.startActivity(intent);
    }

    private fun trimStackTrace(stackTraceString: String): String {
        var stackTraceString1 = stackTraceString
        if (stackTraceString1.length > MAX_STACK_TRACE_SIZE) {
            val disclaimer = " [stack trace too large]";
            stackTraceString1 = stackTraceString1.substring(
                0,
                MAX_STACK_TRACE_SIZE - disclaimer.length
            ) + disclaimer;
        }
        return stackTraceString1
    }

    private fun initActivityTracker(): Unit? {
        return application?.registerActivityLifecycleCallbacks(
            object : CustomActivityCallbacks() {

                override fun onActivityPreCreated(activity: Activity, savedInstanceState: Bundle?) {
                    super.onActivityPreCreated(activity, savedInstanceState)
                    customFragmentLifecycleCallbacks.registerFragmentLifecycleCallbacks(activity)
                }

                override fun onActivityCreated(
                    activity: Activity,
                    savedInstanceState: Bundle?
                ) {
                    if (activity.javaClass != CrashWatcherActivity::class.java) {
                        // Copied from ACRA:
                        // Ignore activityClass because we want the last
                        // application Activity that was started so that we can
                        // explicitly kill it off.
                        lastActivityCreated = WeakReference(activity)
                        lastActivityCreatedTimestamp = System.currentTimeMillis()
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


    private fun isStackTraceConflicting(
        throwable: Throwable
    ): Boolean {
//        var process: String?
//        try {
//            val br = BufferedReader(FileReader("/proc/self/cmdline"))
//            process = br.readLine().trim()
//            br.close()
//        } catch (e: IOException) {
//            process = null
//        }
//        if (process != null && process.endsWith(":error_activity")) {
//            //Error happened in the error activity process - conflictive, so use default handler
//            return true
//        }
        do {
            val stackTrace = throwable.stackTrace
            for (element in stackTrace) {
                if (element.className == "android.app.ActivityThread" && element.methodName == "handleBindApplication") {
                    return true
                }
            }
        } while (throwable.cause != null)
        return false
    }
}

const val EXTRA_CRASH_LOG = "EXTRA_CRASH_LOG"
const val EXTRA_ACTIVITY_HISTORY = "EXTRA_ACTIVITY_HISTORY"
const val EXTRA_FRAGMENT_HISTORY = "EXTRA_FRAGMENT_HISTORY"
