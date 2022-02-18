package com.thelumierguy.crashwatcher.ui.adapters

import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.thelumierguy.crashwatcher.ui.activity.CrashDetailsActivity.Companion.EXTRA_ACTIVITY_HISTORY
import com.thelumierguy.crashwatcher.ui.activity.CrashDetailsActivity.Companion.EXTRA_CRASH_LOG
import com.thelumierguy.crashwatcher.ui.activity.CrashDetailsActivity.Companion.EXTRA_FRAGMENT_HISTORY
import com.thelumierguy.crashwatcher.ui.adapters.data.CrashLogsData
import com.thelumierguy.crashwatcher.ui.adapters.data.DisplayItem
import com.thelumierguy.crashwatcher.ui.adapters.data.FragmentHistoryData
import com.thelumierguy.crashwatcher.ui.fragments.ActivityHistoryFragment
import com.thelumierguy.crashwatcher.ui.fragments.CrashLogsFragment
import com.thelumierguy.crashwatcher.ui.fragments.FragmentHistoryFragment

internal class CrashWatcherPagerAdapter(
    private val crashDataItems: List<DisplayItem>,
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return crashDataItems.size
    }

    override fun createFragment(position: Int): Fragment {
        if (crashDataItems.isNotEmpty()) {
            return when (crashDataItems[position]) {
                is CrashLogsData -> {
                    CrashLogsFragment().apply {
                        arguments = bundleOf(
                            Pair(EXTRA_CRASH_LOG, crashDataItems[position])
                        )
                    }
                }
                is FragmentHistoryData -> {
                    FragmentHistoryFragment().apply {
                        arguments = bundleOf(
                            Pair(EXTRA_FRAGMENT_HISTORY, crashDataItems[position])
                        )
                    }
                }
                else -> {
                    ActivityHistoryFragment().apply {
                        arguments = bundleOf(
                            Pair(EXTRA_ACTIVITY_HISTORY, crashDataItems[position])
                        )
                    }
                }
            }
        }
        return CrashLogsFragment()
    }
}