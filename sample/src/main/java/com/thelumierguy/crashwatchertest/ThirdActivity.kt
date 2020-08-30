package com.thelumierguy.crashwatchertest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import java.lang.RuntimeException

class ThirdActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_third)
        findViewById<Button>(R.id.trace2).setOnClickListener {
            throw RuntimeException()
        }
    }
}