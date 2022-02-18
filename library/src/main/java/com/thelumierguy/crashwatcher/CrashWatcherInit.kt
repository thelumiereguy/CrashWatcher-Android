package com.thelumierguy.crashwatcher

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Process
import android.os.Process.myPid
import android.util.Log
import androidx.annotation.RestrictTo
import com.thelumierguy.crashwatcher.data.StackTrace
import com.thelumierguy.crashwatcher.tracking.ActivityObserverCallback
import com.thelumierguy.crashwatcher.tracking.ActivityTracker
import com.thelumierguy.crashwatcher.ui.activity.CrashDetailsActivity
import java.io.PrintWriter
import java.io.StringWriter
import java.lang.ref.WeakReference
import kotlin.system.exitProcess


internal class CrashWatcherInit : ActivityObserverCallback {

    companion object {
        private const val TAG = "CrashWatcher"
        private const val TIME_TO_CONSIDER_ACTIVITY_CREATED_MS = 100
    }


    private val activityTracker by lazy(LazyThreadSafetyMode.NONE) {
        ActivityTracker(this)
    }

    private var lastActivityCreated: WeakReference<Activity>? = null

    private var lastActivityCreatedTimestamp = 0L

    private var application: Application? = null

    @RestrictTo(RestrictTo.Scope.LIBRARY)
    fun initCrashWatcher(context: Context?) {
        try {
            context?.let {
                setCustomExceptionHandler(Thread.getDefaultUncaughtExceptionHandler())
                application = context.applicationContext as Application
                activityTracker.track(application)
            }
        } catch (t: Throwable) {
            Log.e(
                TAG,
                "Error initializing CrashWatcher",
                t
            )
        }
    }

    private fun setCustomExceptionHandler(defaultExceptionHandler: Thread.UncaughtExceptionHandler?) {
        Thread.setDefaultUncaughtExceptionHandler(object : Thread.UncaughtExceptionHandler {
            override fun uncaughtException(p0: Thread, throwable: Throwable) {
                if (isStackTraceConflicting(throwable)) {
                    Log.e(
                        TAG,
                        "App crashed"
                    )
                    if (defaultExceptionHandler != null) {
                        defaultExceptionHandler.uncaughtException(p0, throwable)
                        return
                    }
                } else if (System.currentTimeMillis() - lastActivityCreatedTimestamp >= TIME_TO_CONSIDER_ACTIVITY_CREATED_MS) {
                    val sw = StringWriter()
                    val pw = PrintWriter(sw)
                    throwable.printStackTrace(pw)

                    val stackTraceString = StackTrace(sw.toString())
                        .trimStackTrace()

                    application?.let { appContext ->
                        CrashDetailsActivity.startActivity(
                            appContext,
                            stackTraceString,
                            activityTracker.activityList,
                            activityTracker.fragmentList
                        )
                    }
                } else {
                    if (defaultExceptionHandler != null) {
                        defaultExceptionHandler.uncaughtException(p0, throwable)
                        return
                    }
                }
                lastActivityCreated?.get()?.let { lastActivity ->
                    lastActivity.finish()
                    lastActivityCreated?.clear()
                }
                Process.killProcess(myPid())
                exitProcess(10)
            }
        })
    }

    private fun isStackTraceConflicting(
        throwable: Throwable
    ): Boolean {
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

    override fun onActivityCreated(activity: Activity, openedTimestamp: Long) {
        lastActivityCreated = WeakReference(activity)
        lastActivityCreatedTimestamp = openedTimestamp
    }
}