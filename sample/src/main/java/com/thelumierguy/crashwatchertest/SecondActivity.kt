package com.thelumierguy.crashwatchertest

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.thelumierguy.crashwatcher.data.ScreenData

class SecondActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        findViewById<Button>(R.id.navigate).setOnClickListener {
            startActivity(
                Intent(
                    this,
                    ThirdActivity::class.java
                ).apply {
                    putExtra("Key1", "Value") // string test
                    putExtra("Key4", arrayOf("Value", "Value")) //array test
                }
            )

        }
    }
}