package com.thelumierguy.crashwatchertest

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.thelumierguy.crashwatcher.data.ScreenData

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<Button>(R.id.btn_navigate).setOnClickListener {
            startActivity(
                Intent(
                    this,
                    SecondActivity::class.java
                ).apply {
                    putExtra("Key1", "Value") // string test
                    putExtra("Key2", 1) //int test
                    putExtra(
                        "Key3",
                        ScreenData("Value", System.currentTimeMillis())
                    ) //parcelable test
                    putExtra("Key4", arrayOf("Value", "Value")) //array test
                }
            )
        }
    }

}