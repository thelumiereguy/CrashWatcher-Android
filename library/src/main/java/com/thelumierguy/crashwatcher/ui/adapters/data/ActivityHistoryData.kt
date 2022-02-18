package com.thelumierguy.crashwatcher.ui.adapters.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ActivityHistoryData(
    val activityList: List<ActivityData>,
    val screenDataStateList: MutableList<ScreenDataState> = activityList.map {
        ScreenDataState.COLLAPSED
    }.toMutableList(),
    override val title: String = ACTIVITY_TRACE
) : DisplayItem, Parcelable

@Parcelize
data class ActivityData(
    val screenName: String,
    val lastOpenedTimeStamp: Long,
    val intentKeys: List<String> = listOf(),
    val intentValues: List<String> = listOf()
) : Parcelable

enum class ScreenDataState(val degreesToRotate: Float) {
    EXPANDED(180F),
    COLLAPSED(0F);

    fun toggle(): ScreenDataState {
        return if (this == EXPANDED) {
            COLLAPSED
        } else {
            EXPANDED
        }
    }
}

private const val ACTIVITY_TRACE = "Activity Trace"