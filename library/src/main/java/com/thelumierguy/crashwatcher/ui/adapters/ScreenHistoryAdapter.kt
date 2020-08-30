package com.thelumierguy.crashwatcher.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.ChangeBounds
import androidx.transition.TransitionManager
import com.thelumierguy.crashwatcher.data.ScreenData
import com.thelumierguy.crashwatcher.data.ScreenDataState
import com.thelumierguy.crashwatcher.data.ScreenHistoryData
import com.thelumierguy.crashwatcher.databinding.LayoutScreenHistoryListItemBinding
import com.thelumierguy.crashwatcher.utils.getFormattedDate

class ScreenHistoryAdapter(private val screenHistoryData: ScreenHistoryData) :

    RecyclerView.Adapter<ScreenHistoryAdapter.ScreenHistoryViewHolder>() {

    private val transition by lazy {
        ChangeBounds().setDuration(500L)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScreenHistoryViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = LayoutScreenHistoryListItemBinding.inflate(layoutInflater, parent, false)
        return ScreenHistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ScreenHistoryViewHolder, position: Int) {
        holder.viewBinding.apply {
            val screen = screenHistoryData.screenList[position]
            tvScreenName.text = screen.screenName
            tvOpenedAt.text = getFormattedDate(screen.lastOpenedTimeStamp)
            var screenDataState = screenHistoryData.screenDataStateList[position]
            if (!screen.intentKeys.isNullOrEmpty()) {
                initScreenData(holder, screen)
                holder.viewBinding.ivToggle.visibility = View.VISIBLE
                updateScreenDataListState(screenDataState, holder)
                holder.viewBinding.ivToggle.setOnClickListener {
                    screenDataState = screenDataState.toggle()
                    updateScreenDataListState(screenDataState, holder)
                    rotateToggle(screenDataState, holder)
                    screenHistoryData.screenDataStateList[position] = screenDataState
                }
            }
        }
    }

    private fun initScreenData(
        holder: ScreenHistoryViewHolder,
        screenData: ScreenData
    ) {
        holder.viewBinding.rvScreenData.apply {
            recycledViewPool
            layoutManager = LinearLayoutManager(context)
            adapter = IntentDataAdapter(screenData.intentKeys, screenData.intentValues)
        }
    }

    private fun updateScreenDataListState(
        screenDataState: ScreenDataState,
        holder: ScreenHistoryViewHolder
    ) {
        TransitionManager.beginDelayedTransition(
            holder.viewBinding.root,
            transition
        )
        if (screenDataState == ScreenDataState.COLLAPSED) {
            holder.viewBinding.rvScreenData.visibility = View.GONE
            holder.viewBinding.viewDivider.visibility = View.INVISIBLE
        } else {
            holder.viewBinding.rvScreenData.visibility = View.VISIBLE
            holder.viewBinding.viewDivider.visibility = View.VISIBLE
        }
    }

    private fun rotateToggle(screenDataState: ScreenDataState, holder: ScreenHistoryViewHolder) {
        holder.viewBinding.ivToggle.animate().rotation(screenDataState.degreesToRotate)
            .setDuration(500).start()
    }

    override fun getItemCount(): Int {
        return screenHistoryData.screenList.size
    }

    class ScreenHistoryViewHolder(val viewBinding: LayoutScreenHistoryListItemBinding) :
        RecyclerView.ViewHolder(viewBinding.root)
}