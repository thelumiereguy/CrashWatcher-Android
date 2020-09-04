package com.thelumierguy.crashwatchertest

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class SecondActivity : AppCompatActivity() {
    var counter = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        supportFragmentManager.beginTransaction().add(
            R.id.fl_container,
            FragmentOne.newInstance("activity2", "fragment1")
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
            FragmentTwo.newInstance("activity2", "fragment2")
        ).commit()
        findViewById<Button>(R.id.navigate).text = "Go to Next Activity"
        counter++
    }

    private fun startNextActivity() {
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