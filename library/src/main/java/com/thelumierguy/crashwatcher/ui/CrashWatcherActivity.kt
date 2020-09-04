package com.thelumierguy.crashwatcher.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ShareCompat
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.GsonBuilder
import com.thelumierguy.crashwatcher.EXTRA_ACTIVITY_HISTORY
import com.thelumierguy.crashwatcher.EXTRA_CRASH_LOG
import com.thelumierguy.crashwatcher.EXTRA_FRAGMENT_HISTORY
import com.thelumierguy.crashwatcher.data.*
import com.thelumierguy.crashwatcher.databinding.ActivityCrashwatcherBinding
import com.thelumierguy.crashwatcher.ui.adapters.CrashWatcherPagerAdapter

class CrashWatcherActivity : AppCompatActivity() {

    private val crashDataItems = mutableListOf<DisplayItem>()

    private val gson by lazy {
        GsonBuilder().setPrettyPrinting().create()
    }

    private val logs by lazy {
        intent.getStringExtra(EXTRA_CRASH_LOG) ?: ""
    }

    private val activityHistoryData: ActivityHistoryData? by lazy {
        intent.getParcelableExtra(EXTRA_ACTIVITY_HISTORY)
    }

    private val fragmentHistoryData: FragmentHistoryData? by lazy {
        intent.getParcelableExtra(EXTRA_FRAGMENT_HISTORY)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityCrashwatcherBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getData()
        initViews(binding)
    }

    private fun getData() {
        crashDataItems.add(CrashLogsData(logs))
        activityHistoryData?.let { crashDataItems.add(it) }
        fragmentHistoryData?.let { crashDataItems.add(it) }
    }

    private fun initViews(binding: ActivityCrashwatcherBinding) {
        binding.viewpager.adapter =
            CrashWatcherPagerAdapter(crashDataItems, supportFragmentManager, lifecycle)

        TabLayoutMediator(
            binding.tlTabs,
            binding.viewpager
        ) { tab, position ->
            if (crashDataItems.isNotEmpty()) {
                tab.text = crashDataItems[position].title
                binding.viewpager.setCurrentItem(tab.position, true)
            }
        }.attach()
        binding.fabShare.setOnClickListener {
            val shareData = getShareData()
            ShareCompat.IntentBuilder.from(this)
                .setType("text/plain")
                .setText(shareData)
                .startChooser()

        }
    }

    private fun getShareData(): String {
        val shareData = ShareData(
            stackTrace = logs,
            activityList = activityHistoryData?.activityList,
            fragmentList = fragmentHistoryData?.fragmentList
        )
        return gson.toJson(shareData).replace("\\", "")
    }

}