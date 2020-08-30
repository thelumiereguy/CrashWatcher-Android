package com.thelumierguy.crashwatcher.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.thelumierguy.crashwatcher.CrashWatcher
import com.thelumierguy.crashwatcher.R
import com.thelumierguy.crashwatcher.data.CrashLogsData


class CrashLogsFragment : Fragment(R.layout.fragment_crash_logs) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val crashLogsData = arguments?.getParcelable<CrashLogsData>(CrashWatcher.EXTRA_CRASH_LOG)
        view.findViewById<TextView>(R.id.trace).text = crashLogsData?.trace
    }
}