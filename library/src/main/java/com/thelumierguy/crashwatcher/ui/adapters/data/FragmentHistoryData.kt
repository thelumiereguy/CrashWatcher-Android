package com.thelumierguy.crashwatcher.ui.adapters.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FragmentHistoryData(
    val fragmentList: List<FragmentData>,
    val screenDataStateList: MutableList<ScreenDataState> = fragmentList.map {
        ScreenDataState.COLLAPSED
    }.toMutableList(),
    override val title: String = FRAGMENT_TRACE
) : DisplayItem, Parcelable

@Parcelize
data class FragmentData(
    val screenName: String,
    val lastOpenedTimeStamp: Long,
    val bundleKeys: List<String> = listOf(),
    val bundleValues: List<String> = listOf()
) : Parcelable

private const val FRAGMENT_TRACE = "Fragment Trace"