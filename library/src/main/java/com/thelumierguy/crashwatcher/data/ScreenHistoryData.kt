package com.thelumierguy.crashwatcher.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ScreenHistoryData(
    val screenList: List<ScreenData>,
    val screenDataStateList: MutableList<ScreenDataState> = screenList.map {
        ScreenDataState.COLLAPSED
    }.toMutableList()
) : DisplayItem

@Parcelize
data class ScreenData(
    val screenName: String,
    val lastOpenedTimeStamp: Long,
    val intentKeys: List<String> = listOf(),
    val intentValues: List<String> = listOf()
) : Parcelable

data class ShareData(
    val stackTrace: String,
    val screensList: List<ScreenData>
)

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