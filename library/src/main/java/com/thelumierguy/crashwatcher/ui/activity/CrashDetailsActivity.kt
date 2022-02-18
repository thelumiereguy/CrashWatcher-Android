package com.thelumierguy.crashwatcher.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ShareCompat
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.GsonBuilder
import com.thelumierguy.crashwatcher.data.CrashWatcherShareableData
import com.thelumierguy.crashwatcher.databinding.ActivityCrashwatcherBinding
import com.thelumierguy.crashwatcher.ui.adapters.CrashWatcherPagerAdapter
import com.thelumierguy.crashwatcher.ui.adapters.data.*

internal class CrashDetailsActivity : AppCompatActivity() {

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
        initViews(binding)
    }

    private fun initViews(binding: ActivityCrashwatcherBinding) {
        val crashDataItems = listOfNotNull(
            CrashLogsData(logs),
            activityHistoryData,
            fragmentHistoryData
        )

        binding.viewpager.adapter = CrashWatcherPagerAdapter(
            crashDataItems,
            supportFragmentManager,
            lifecycle
        )

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
        val shareData = CrashWatcherShareableData(
            app_crash_trace = logs,
            screen_activities = activityHistoryData?.activityList,
            screen_fragments = fragmentHistoryData?.fragmentList
        )
        return gson.toJson(shareData).replace("\\", "")
    }

    companion object {

        fun startActivity(
            context: Context,
            stackTraceString: String,
            activityList: MutableList<ActivityData>,
            fragmentList: MutableList<FragmentData>,
        ) {
            val intent = Intent(context, CrashDetailsActivity::class.java).apply {
                putExtra(EXTRA_CRASH_LOG, stackTraceString)

                if (activityList.isNotEmpty()) {
                    putExtra(
                        EXTRA_ACTIVITY_HISTORY,
                        ActivityHistoryData(activityList)
                    )
                }
                if (fragmentList.isNotEmpty()) {
                    putExtra(
                        EXTRA_FRAGMENT_HISTORY,
                        FragmentHistoryData(fragmentList)
                    )
                }

                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            context.startActivity(intent)
        }

        internal const val EXTRA_CRASH_LOG = "EXTRA_CRASH_LOG"
        internal const val EXTRA_ACTIVITY_HISTORY = "EXTRA_ACTIVITY_HISTORY"
        internal const val EXTRA_FRAGMENT_HISTORY = "EXTRA_FRAGMENT_HISTORY"
    }
}