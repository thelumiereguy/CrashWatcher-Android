package com.thelumierguy.crashwatcher.utils

import android.content.Intent
import android.os.Bundle
import com.google.gson.Gson

fun Intent?.toKeyValuesListPair(gson: Gson): Pair<List<String>, List<String>> {
    return this?.extras?.toKeyValuesListPair(gson) ?: Pair(listOf(), listOf())
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