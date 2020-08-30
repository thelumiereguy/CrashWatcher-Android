package com.thelumierguy.crashwatcher.utils

import android.content.Intent
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.*

fun Intent?.toList(gson: Gson): Pair<List<String>, List<String>> {
    val keys = mutableListOf<String>()
    val values = mutableListOf<String>()
    this?.extras?.keySet()?.forEach { key ->
        try {
            extras?.get(key)?.let { value ->
                keys.add(key)
                values.add(gson.toJson(value))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    return Pair(keys, values)
}


val dateFormat = SimpleDateFormat("hh:mm:ss", Locale.US)

fun getFormattedDate(timestamp: Long): String? {
    return try {
        dateFormat.format(timestamp)
    } catch (e: java.lang.Exception) {
        null
    }
}