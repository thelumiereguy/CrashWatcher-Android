package com.thelumierguy.crashwatcher.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.ChangeBounds
import androidx.transition.TransitionManager
import androidx.transition.TransitionSet
import com.thelumierguy.crashwatcher.data.ActivityData
import com.thelumierguy.crashwatcher.data.ScreenDataState
import com.thelumierguy.crashwatcher.data.ActivityHistoryData
import com.thelumierguy.crashwatcher.databinding.LayoutScreenHistoryListItemBinding
import com.thelumierguy.crashwatcher.utils.getFormattedDate


class ActivityHistoryAdapter(private val activityHistoryData: ActivityHistoryData) :

    RecyclerView.Adapter<ActivityHistoryAdapter.ScreenHistoryViewHolder>() {

    private val transition by lazy {
        TransitionSet().addTransition(
            ChangeBounds().setDuration(100L)
        )
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScreenHistoryViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = LayoutScreenHistoryListItemBinding.inflate(layoutInflater, parent, false)
        return ScreenHistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ScreenHistoryViewHolder, position: Int) {
        val screen = activityHistoryData.activityList[position]
        var screenDataState = activityHistoryData.screenDataStateList[position]
        val shouldShowExtrasData = screen.intentKeys.isNullOrEmpty().not()
        holder.viewBinding.apply {
            tvScreenName.text = screen.screenName
            tvOpenedAt.text = getFormattedDate(screen.lastOpenedTimeStamp)
            holder.viewBinding.ivToggle.isVisible = shouldShowExtrasData
            if (shouldShowExtrasData) {
                initScreenData(holder, screen)
                updateScreenDataListState(screenDataState, holder)
                holder.viewBinding.ivToggle.setOnClickListener {
                    screenDataState = screenDataState.toggle()
                    updateScreenDataListState(screenDataState, holder)
                    activityHistoryData.screenDataStateList[position] = screenDataState
                    holder.viewBinding.clRoot.post {
                        rotateToggle(screenDataState, holder)
                    }
                }
            }
        }

    }

    private fun initScreenData(
        holder: ScreenHistoryViewHolder,
        activityData: ActivityData
    ) {
        holder.viewBinding.rvScreenData.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = IntentDataAdapter(activityData.intentKeys, activityData.intentValues)
        }
    }

    private fun updateScreenDataListState(
        screenDataState: ScreenDataState,
        holder: ScreenHistoryViewHolder
    ) {
        TransitionManager.beginDelayedTransition(holder.viewBinding.root, transition)
        holder.viewBinding.rvScreenData.isVisible = screenDataState == ScreenDataState.EXPANDED
    }


    private fun rotateToggle(screenDataState: ScreenDataState, holder: ScreenHistoryViewHolder) {
        holder.viewBinding.ivToggle.animate().rotation(screenDataState.degreesToRotate)
            .setDuration(200).start()
    }

    override fun getItemCount(): Int {
        return activityHistoryData.activityList.size
    }

    class ScreenHistoryViewHolder(val viewBinding: LayoutScreenHistoryListItemBinding) :
        RecyclerView.ViewHolder(viewBinding.root)
}