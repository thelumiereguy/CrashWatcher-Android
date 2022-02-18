package com.thelumierguy.crashwatcher.data

import com.thelumierguy.crashwatcher.ui.adapters.data.ActivityData
import com.thelumierguy.crashwatcher.ui.adapters.data.FragmentData

internal data class CrashWatcherShareableData(
    val app_crash_trace: String,
    val screen_activities: List<ActivityData>?,
    val screen_fragments: List<FragmentData>?
)