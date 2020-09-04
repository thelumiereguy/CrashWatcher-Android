package com.thelumierguy.crashwatcher.utils

import java.text.SimpleDateFormat
import java.util.*


val dateFormat = SimpleDateFormat("hh:mm:ss", Locale.US)

fun getFormattedDate(timestamp: Long): String? {
    return try {
        dateFormat.format(timestamp)
    } catch (e: java.lang.Exception) {
        null
    }
}