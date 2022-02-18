package com.thelumierguy.crashwatcher.ui.adapters

import java.text.SimpleDateFormat
import java.util.*

internal fun getFormattedDate(timestamp: Long): String? {
    return try {
        SimpleDateFormat("hh:mm:ss", Locale.US).format(timestamp)
    } catch (e: java.lang.Exception) {
        null
    }
}