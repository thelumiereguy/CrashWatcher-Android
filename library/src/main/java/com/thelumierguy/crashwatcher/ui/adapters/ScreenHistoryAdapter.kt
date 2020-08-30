package com.thelumierguy.crashwatcher.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.thelumierguy.crashwatcher.data.ScreenData
import com.thelumierguy.crashwatcher.data.ScreenDataState
import com.thelumierguy.crashwatcher.data.ScreenHistoryData
import com.thelumierguy.crashwatcher.databinding.LayoutScreenHistoryListItemBinding
import com.thelumierguy.crashwatcher.utils.getFormattedDate


class ScreenHistoryAdapter(private val screenHistoryData: ScreenHistoryData) :

    RecyclerView.Adapter<ScreenHistoryAdapter.ScreenHistoryViewHolder>() {

//    private val transition by lazy {
//        TransitionSet().addTransition(
//            ChangeBounds().setDuration(600L)
//        ).addTransition(
//            Fade().setDuration(200L)
//        )
//    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScreenHistoryViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = LayoutScreenHistoryListItemBinding.inflate(layoutInflater, parent, false)
        return ScreenHistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ScreenHistoryViewHolder, position: Int) {
        val screen = screenHistoryData.screenList[position]
        var screenDataState = screenHistoryData.screenDataStateList[position]
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
                    screenHistoryData.screenDataStateList[position] = screenDataState
                    holder.viewBinding.clRoot.post {
                        rotateToggle(screenDataState, holder)
                    }
                }
            }
        }

    }

    private fun initScreenData(
        holder: ScreenHistoryViewHolder,
        screenData: ScreenData
    ) {
        holder.viewBinding.rvScreenData.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = IntentDataAdapter(screenData.intentKeys, screenData.intentValues)
        }
    }

    private fun updateScreenDataListState(
        screenDataState: ScreenDataState,
        holder: ScreenHistoryViewHolder
    ) {
        holder.viewBinding.rvScreenData.isVisible = screenDataState == ScreenDataState.EXPANDED
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