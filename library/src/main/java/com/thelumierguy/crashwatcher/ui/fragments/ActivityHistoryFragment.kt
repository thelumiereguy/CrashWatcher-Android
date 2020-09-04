package com.thelumierguy.crashwatcher.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.thelumierguy.crashwatcher.EXTRA_ACTIVITY_HISTORY
import com.thelumierguy.crashwatcher.EXTRA_FRAGMENT_HISTORY
import com.thelumierguy.crashwatcher.R
import com.thelumierguy.crashwatcher.data.ActivityHistoryData
import com.thelumierguy.crashwatcher.data.FragmentHistoryData
import com.thelumierguy.crashwatcher.databinding.FragmentScreenHistoryBinding
import com.thelumierguy.crashwatcher.ui.adapters.ActivityHistoryAdapter
import com.thelumierguy.crashwatcher.ui.adapters.FragmentHistoryAdapter


class ActivityHistoryFragment : Fragment(R.layout.fragment_screen_history) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val activityHistoryData =
            arguments?.getParcelable<ActivityHistoryData>(EXTRA_ACTIVITY_HISTORY)

        FragmentScreenHistoryBinding.bind(view).apply {
            rvScreenHistory.layoutManager = LinearLayoutManager(requireContext())
            activityHistoryData?.let {
                rvScreenHistory.adapter = ActivityHistoryAdapter(it)
            }
        }
    }
}