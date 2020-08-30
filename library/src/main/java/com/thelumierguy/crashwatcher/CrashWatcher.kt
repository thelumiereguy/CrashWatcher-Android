package com.thelumierguy.crashwatcher

import android.app.Activity
import android.app.Application
import android.app.Application.ActivityLifecycleCallbacks
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Process.myPid
import android.util.Log
import androidx.annotation.RestrictTo
import com.google.gson.Gson
import com.thelumierguy.crashwatcher.data.ScreenData
import com.thelumierguy.crashwatcher.data.ScreenHistoryData
import com.thelumierguy.crashwatcher.data.TrackingList
import com.thelumierguy.crashwatcher.ui.CrashWatcherActivity
import com.thelumierguy.crashwatcher.utils.toList
import java.io.*
import java.lang.ref.WeakReference
import kotlin.system.exitProcess


object CrashWatcher {
    private const val MAX_STACK_TRACE_SIZE = 131071
    val gson by lazy { Gson() }

    private const val TIME_TO_CONSIDER_FOREGROUND_MS = 1000
    private var lastActivityCreatedTimestamp = 0L
    private lateinit var lastActivityCreated: WeakReference<Activity>
    const val EXTRA_CRASH_LOG = "EXTRA_CRASH_LOG"
    const val EXTRA_SCREEN_HISTORY = "EXTRA_SCREEN_HISTORY"

    val screensList = TrackingList<ScreenData>(5)

    private var application: Application? = null

    @RestrictTo(RestrictTo.Scope.LIBRARY)
    fun initCrashWatcher(context: Context?) {
        try {
            context?.let {
                val defaultExceptionHandler = Thread.getDefaultUncaughtExceptionHandler()
                application = context.applicationContext as Application

                Thread.setDefaultUncaughtExceptionHandler(object : Thread.UncaughtExceptionHandler {
                    override fun uncaughtException(p0: Thread, p1: Throwable) {
                        if (isStackTraceLikelyConflictive(p1)) {
                            Log.e(
                                "CrashWatcher",
                                "Your application class or your error activity have crashed, the custom activity will not be launched!"
                            );
                            if (defaultExceptionHandler != null) {
                                defaultExceptionHandler.uncaughtException(p0, p1)
                                return
                            }
                        } else if (System.currentTimeMillis() - lastActivityCreatedTimestamp >= TIME_TO_CONSIDER_FOREGROUND_MS) {
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
                        android.os.Process.killProcess(myPid())
                        exitProcess(10);
                    }
                })
                initActivityTracker()
            }
        } catch (t: Throwable) {
            Log.e(
                "CrashWatcher",
                "An unknown error occurred while installing CustomActivityOnCrash, it may not have been properly initialized. Please report this as a bug if needed.",
                t
            )
        }
    }

    private fun startCrashReportActivity(throwable: Throwable) {
        val intent = Intent(application, CrashWatcherActivity::class.java);
        val sw = StringWriter();
        val pw = PrintWriter(sw);
        throwable.printStackTrace(pw);
        var stackTraceString = sw.toString();

        if (stackTraceString.length > MAX_STACK_TRACE_SIZE) {
            val disclaimer = " [stack trace too large]";
            stackTraceString = stackTraceString.substring(
                0,
                MAX_STACK_TRACE_SIZE - disclaimer.length
            ) + disclaimer;
        }
        intent.putExtra(EXTRA_CRASH_LOG, stackTraceString)
        intent.putExtra(EXTRA_SCREEN_HISTORY, ScreenHistoryData(screensList))
        intent.flags =
            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        application?.startActivity(intent);
    }

    private fun initActivityTracker(): Unit? {
        return application?.registerActivityLifecycleCallbacks(
            object : ActivityLifecycleCallbacks {

                override fun onActivityCreated(
                    activity: Activity,
                    savedInstanceState: Bundle?
                ) {
                    if (activity.javaClass != CrashWatcherActivity::class.java) {
                        // Copied from ACRA:
                        // Ignore activityClass because we want the last
                        // application Activity that was started so that we can
                        // explicitly kill it off.
                        lastActivityCreated =
                            WeakReference(activity)
                        lastActivityCreatedTimestamp = System.currentTimeMillis()
                        val intentData = activity.intent.toList(gson)
                        screensList.add(
                            ScreenData(
                                activity.localClassName,
                                System.currentTimeMillis(),
                                intentKeys = intentData.first,
                                intentValues = intentData.second
                            )
                        )
                    }
                }

                override fun onActivityStarted(activity: Activity) {
                }

                override fun onActivityResumed(activity: Activity) {
                }

                override fun onActivityPaused(activity: Activity) {

                }

                override fun onActivityStopped(activity: Activity) {
                }

                override fun onActivitySaveInstanceState(
                    activity: Activity,
                    outState: Bundle
                ) {
                }

                override fun onActivityDestroyed(activity: Activity) {
                }
            })
    }


    private fun isStackTraceLikelyConflictive(
        throwable: Throwable
    ): Boolean {
        var process: String?
        try {
            val br = BufferedReader(FileReader("/proc/self/cmdline"))
            process = br.readLine().trim()
            br.close()
        } catch (e: IOException) {
            process = null
        }
        if (process != null && process.endsWith(":error_activity")) {
            //Error happened in the error activity process - conflictive, so use default handler
            return true
        }
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