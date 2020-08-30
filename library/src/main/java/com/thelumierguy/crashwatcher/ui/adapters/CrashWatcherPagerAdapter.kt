package com.thelumierguy.crashwatcher.ui.adapters

import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.thelumierguy.crashwatcher.CrashWatcher
import com.thelumierguy.crashwatcher.data.CrashLogsData
import com.thelumierguy.crashwatcher.data.DisplayItem
import com.thelumierguy.crashwatcher.data.ScreenHistoryData
import com.thelumierguy.crashwatcher.ui.fragments.CrashLogsFragment
import com.thelumierguy.crashwatcher.ui.fragments.ScreenHistoryFragment

class CrashWatcherPagerAdapter(
    private val crashDataItems: MutableList<DisplayItem>,
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
                            Pair(CrashWatcher.EXTRA_CRASH_LOG, crashDataItems[position])
                        )
                    }
                }
                is ScreenHistoryData -> {
                    ScreenHistoryFragment().apply {
                        arguments = bundleOf(
                            Pair(CrashWatcher.EXTRA_SCREEN_HISTORY, crashDataItems[position])
                        )
                    }
                }
                else -> {
                    CrashLogsFragment()
                }
            }
        }
        return CrashLogsFragment()
    }
}