package com.thelumierguy.crashwatchertest.initializer

import android.content.Context
import android.util.Log
import androidx.startup.Initializer
import com.thelumierguy.crashwatcher.CrashWatcher

class CrashWatcherInitializer : Initializer<Unit> {

    override fun create(context: Context) {
        CrashWatcher.initCrashWatcher(context)
        Log.d("CrashWatcherInitializer", "Timber initialized")
    }

    override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()
}