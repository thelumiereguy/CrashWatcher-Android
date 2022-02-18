package com.thelumierguy.crashwatcher.tracking

import android.app.Activity

internal interface ActivityObserverCallback {
    fun onActivityCreated(activity: Activity, openedTimestamp: Long)
}