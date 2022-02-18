package com.thelumierguy.crashwatchertest

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.thelumierguy.crashwatcher.ui.adapters.data.ActivityData

class MainActivity : AppCompatActivity() {

    var counter = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager.beginTransaction().add(
            R.id.fl_container,
            FragmentOne.newInstance("activity1", "fragment1")
        ).commit()
        findViewById<Button>(R.id.navigate).setOnClickListener {
            if (counter == 1) {
                startNextFragment()
            } else {
                startNextActivity()
            }
        }
    }

    private fun startNextFragment() {
        supportFragmentManager.beginTransaction().add(
            R.id.fl_container,
            FragmentTwo.newInstance("activity1", "fragment2")
        ).commit()
        findViewById<Button>(R.id.navigate).text = "Go to Next Activity"
        counter++
    }

    private fun startNextActivity() {
        startActivity(
            Intent(
                this,
                SecondActivity::class.java
            ).apply {
                putExtra("Key1", "Value") // string test
                putExtra("Key2", 1) //int test
                putExtra(
                    "Key3",
                    ActivityData("Value", System.currentTimeMillis())
                ) //parcelable test
                putExtra("Key4", arrayOf("Value", "Value")) //array test
            }
        )
    }

}