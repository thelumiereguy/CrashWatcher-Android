package com.thelumierguy.crashwatcher

import android.content.Context

public object CrashWatcher {

    private val crashWatcherInit = CrashWatcherInit()

    fun initCrashWatcher(context: Context?) {
        crashWatcherInit.initCrashWatcher(context)
    }
}