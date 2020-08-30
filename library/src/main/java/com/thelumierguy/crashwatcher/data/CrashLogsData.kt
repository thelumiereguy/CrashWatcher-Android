package com.thelumierguy.crashwatcher.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


interface DisplayItem : Parcelable

@Parcelize
data class CrashLogsData(
    val trace: String
) : DisplayItem
