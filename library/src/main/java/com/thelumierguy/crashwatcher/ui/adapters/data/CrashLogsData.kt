package com.thelumierguy.crashwatcher.ui.adapters.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


internal interface DisplayItem {
    val title: String
}

@Parcelize
internal data class CrashLogsData(
    val trace: String,
    override val title: String = CRASH_LOGS
) : DisplayItem, Parcelable

private const val CRASH_LOGS = "Crash Logs"
