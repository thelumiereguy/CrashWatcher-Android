package com.thelumierguy.crashwatcher.utils

import android.content.Intent
import android.os.Bundle
import com.google.gson.Gson

fun Intent?.toKeyValuesListPair(gson: Gson): Pair<List<String>, List<String>> {
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

fun Bundle?.toKeyValuesListPair(gson: Gson): Pair<List<String>, List<String>> {
    val keys = mutableListOf<String>()
    val values = mutableListOf<String>()
    this?.keySet()?.forEach { key ->
        try {
            get(key)?.let { value ->
                keys.add(key)
                values.add(gson.toJson(value))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    return Pair(keys, values)
}