package com.thelumierguy.crashwatcher.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ShareCompat
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson
import com.thelumierguy.crashwatcher.CrashWatcher.EXTRA_CRASH_LOG
import com.thelumierguy.crashwatcher.CrashWatcher.EXTRA_SCREEN_HISTORY
import com.thelumierguy.crashwatcher.data.CrashLogsData
import com.thelumierguy.crashwatcher.data.DisplayItem
import com.thelumierguy.crashwatcher.data.ScreenHistoryData
import com.thelumierguy.crashwatcher.data.ShareData
import com.thelumierguy.crashwatcher.databinding.ActivityCrashwatcherBinding
import com.thelumierguy.crashwatcher.ui.adapters.CrashWatcherPagerAdapter

class CrashWatcherActivity : AppCompatActivity() {

    private val tabTitles = listOf("Crash Logs", "Screen trace")
    private val crashDataItems = mutableListOf<DisplayItem>()

    private val logs by lazy {
        intent.getStringExtra(EXTRA_CRASH_LOG) ?: ""
    }

    val gson by lazy {
        Gson()
    }

    private val screenTraceData by lazy {
        intent.getParcelableExtra(EXTRA_SCREEN_HISTORY) ?: ScreenHistoryData(
            listOf()
        )
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
        crashDataItems.add(screenTraceData)
    }

    private fun initViews(binding: ActivityCrashwatcherBinding) {
        binding.viewpager.adapter =
            CrashWatcherPagerAdapter(crashDataItems, supportFragmentManager, lifecycle)

        TabLayoutMediator(
            binding.tlTabs,
            binding.viewpager
        ) { tab, position ->
            tab.text = tabTitles[position]
            binding.viewpager.setCurrentItem(tab.position, true)
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
            screensList = screenTraceData.screenList
        )
        return gson.toJson(shareData)
    }

}