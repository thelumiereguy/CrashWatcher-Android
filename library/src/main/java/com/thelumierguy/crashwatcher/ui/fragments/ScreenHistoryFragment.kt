package com.thelumierguy.crashwatcher.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.thelumierguy.crashwatcher.CrashWatcher
import com.thelumierguy.crashwatcher.R
import com.thelumierguy.crashwatcher.data.ScreenHistoryData
import com.thelumierguy.crashwatcher.databinding.FragmentScreenHistoryBinding
import com.thelumierguy.crashwatcher.ui.adapters.ScreenHistoryAdapter


class ScreenHistoryFragment : Fragment(R.layout.fragment_screen_history) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val crashLogsData =
            arguments?.getParcelable<ScreenHistoryData>(CrashWatcher.EXTRA_SCREEN_HISTORY)
        FragmentScreenHistoryBinding.bind(view).apply {
            crashLogsData?.let {
                rvScreenHistory.layoutManager = LinearLayoutManager(requireContext())
                rvScreenHistory.adapter = ScreenHistoryAdapter(it)
            }
        }
    }
}