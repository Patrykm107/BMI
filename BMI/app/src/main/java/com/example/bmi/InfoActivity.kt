package com.example.bmi

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_info.*

class InfoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContentView(R.layout.activity_info)
        title = getString(R.string.info)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        val bmi = intent.extras!!.getString(MainActivity.bmiTextKey)
        leftText.append(" $bmi")
        val bmiVal = bmi.toString().replace(",", ".")
            .toDouble() //Kotlin przy zamianie double na string daje przecinek jako separator cz. dziesiętnych
        when {
            bmiVal < 18.5 -> {
                rightText.text = getString(R.string.underweightDesc)
            }
            bmiVal < 25.0 -> {
                rightText.text = getString(R.string.normalWeightDesc)
            }
            bmiVal < 30.0 -> {
                rightText.text = getString(R.string.overweightDesc)
            }
            bmiVal < 35.0 -> {
                rightText.text = getString(R.string.obeseDesc)
            }
            else -> {
                rightText.text = getString(R.string.severelyObeseDesc)
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
