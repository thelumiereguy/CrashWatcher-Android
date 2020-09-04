package com.thelumierguy.crashwatcher.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


interface DisplayItem : Parcelable {
    val title: String
}

@Parcelize
data class CrashLogsData(
    val trace: String,
    override val title: String = CRASH_LOGS
) : DisplayItem

const val CRASH_LOGS = "Crash Logs"
const val ACTIVITY_TRACE = "Activity Trace"
const val FRAGMENT_TRACE = "Fragment Trace"
