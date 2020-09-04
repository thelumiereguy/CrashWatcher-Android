package com.thelumierguy.crashwatcher.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ActivityHistoryData(
    val activityList: List<ActivityData>,
    val screenDataStateList: MutableList<ScreenDataState> = activityList.map {
        ScreenDataState.COLLAPSED
    }.toMutableList(),
    override val title: String = ACTIVITY_TRACE
) : DisplayItem

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

data class ShareData(
    val stackTrace: String,
    val activityList: List<ActivityData>?,
    val fragmentList: List<FragmentData>?
)
