package com.thelumierguy.crashwatcher.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.thelumierguy.crashwatcher.R
import com.thelumierguy.crashwatcher.databinding.FragmentScreenHistoryBinding
import com.thelumierguy.crashwatcher.ui.activity.CrashDetailsActivity.Companion.EXTRA_FRAGMENT_HISTORY
import com.thelumierguy.crashwatcher.ui.adapters.FragmentHistoryAdapter
import com.thelumierguy.crashwatcher.ui.adapters.data.FragmentHistoryData


internal class FragmentHistoryFragment : Fragment(R.layout.fragment_screen_history) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val fragmentHistoryData =
            arguments?.getParcelable<FragmentHistoryData>(EXTRA_FRAGMENT_HISTORY)

        FragmentScreenHistoryBinding.bind(view).apply {
            rvScreenHistory.layoutManager = LinearLayoutManager(requireContext())
            fragmentHistoryData?.let {
                rvScreenHistory.adapter = FragmentHistoryAdapter(it)
            }
        }
    }
}