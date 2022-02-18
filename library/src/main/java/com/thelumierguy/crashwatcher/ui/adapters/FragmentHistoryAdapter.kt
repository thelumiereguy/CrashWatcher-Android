package com.thelumierguy.crashwatcher.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.ChangeBounds
import androidx.transition.TransitionManager
import androidx.transition.TransitionSet
import com.thelumierguy.crashwatcher.databinding.LayoutScreenHistoryListItemBinding
import com.thelumierguy.crashwatcher.ui.adapters.data.FragmentData
import com.thelumierguy.crashwatcher.ui.adapters.data.FragmentHistoryData
import com.thelumierguy.crashwatcher.ui.adapters.data.ScreenDataState

class FragmentHistoryAdapter(private val fragmentHistoryData: FragmentHistoryData) :

    RecyclerView.Adapter<FragmentHistoryAdapter.ScreenHistoryViewHolder>() {

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
        val screen = fragmentHistoryData.fragmentList[position]
        var screenDataState = fragmentHistoryData.screenDataStateList[position]
        val shouldShowExtrasData = screen.bundleKeys.isNullOrEmpty().not()
        holder.viewBinding.apply {
            tvScreenName.text = screen.screenName
            tvOpenedAt.text = getFormattedDate(screen.lastOpenedTimeStamp)
            holder.viewBinding.ivToggle.isVisible = shouldShowExtrasData
            if (shouldShowExtrasData) {
                initScreenData(holder, screen)
                updateScreenDataListState(screenDataState, holder)
                holder.viewBinding.clRoot.setOnClickListener {
                    screenDataState = screenDataState.toggle()
                    updateScreenDataListState(screenDataState, holder)
                    fragmentHistoryData.screenDataStateList[position] = screenDataState
                    holder.viewBinding.clRoot.post {
                        rotateToggle(screenDataState, holder)
                    }
                }
            }
        }

    }

    private fun initScreenData(
        holder: ScreenHistoryViewHolder,
        activityData: FragmentData
    ) {
        holder.viewBinding.rvScreenData.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = IntentDataAdapter(activityData.bundleKeys, activityData.bundleValues)
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
        return fragmentHistoryData.fragmentList.size
    }

    class ScreenHistoryViewHolder(val viewBinding: LayoutScreenHistoryListItemBinding) :
        RecyclerView.ViewHolder(viewBinding.root)
}