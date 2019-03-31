package com.example.bmi

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.example.bmi.logic.BmiForKgCm
import com.example.bmi.logic.BmiForLbIn
import com.example.bmi.logic.HistoryEntry
import com.example.bmi.logic.HistoryQueue
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import java.text.DecimalFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    private var isMetric = true

    companion object {
        const val massTextKey = "mass"
        const val heightTextKey = "height"
        const val massPromptKey = "massPrompt"
        const val heightPromptKey = "heightPrompt"
        const val isMetricKey = "isMetric"
        const val bmiTextKey = "bmi"
        const val bmiTextColorKey = "color"
        const val categoryTextKey = "category"
        const val infoButtonVisibleKey = "infoButtonVisible"
        const val historySharedPrefKey = "history"
        const val historyKey = "historyElem"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        calculateBMI.setOnClickListener {
            if (countBmi()) {
                val entry = HistoryEntry(
                    BMItext.text.toString(), BMItext.currentTextColor, heightText.text.toString(),
                    massText.text.toString(), isMetric, Date()
                )

                var entryQueue = HistoryQueue()
                val sharedPref = getSharedPreferences(historySharedPrefKey, Context.MODE_PRIVATE)

                if (sharedPref.contains(historyKey)) {
                    val gson = Gson()
                    val json = sharedPref.getString(historyKey, "")
                    entryQueue = gson.fromJson<HistoryQueue>(json, HistoryQueue::class.java)
                }
                entryQueue.add(entry)


                val prefsEditor = sharedPref.edit()
                val gson = Gson()
                val json = gson.toJson(entryQueue)
                prefsEditor.putString(historyKey, json)
                prefsEditor.apply()
            }
        }


        infoButton.setOnClickListener {
            val intent = Intent(this, InfoActivity::class.java)
            intent.putExtra(bmiTextKey, BMItext.text)

            startActivity(intent)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.unitsToggle -> {
                isMetric = !isMetric
                massText.setText("")
                heightText.setText("")
                BMItext.text = ""
                categoryText.text = ""
                infoButton.isVisible = false

                if (!isMetric) {
                    massPrompt.setText(R.string.mass_lb)
                    heightPrompt.setText(R.string.height_in)
                } else {
                    massPrompt.setText(R.string.mass_kg)
                    heightPrompt.setText(R.string.height_cm)
                }
                true
            }
            R.id.aboutMeButton -> {
                val intent = Intent(this, AboutMeActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.historyButton -> {
                val intent = Intent(this, HistoryActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    }

    override fun onSaveInstanceState(outState: Bundle?) {
        outState?.run {
            putString(massTextKey, massText.text.toString())
            putString(heightTextKey, heightText.text.toString())
            putString(massPromptKey, massPrompt.text.toString())
            putString(heightPromptKey, heightPrompt.text.toString())
            putBoolean(isMetricKey, isMetric)
            putString(bmiTextKey, BMItext.text.toString())
            putInt(bmiTextColorKey, BMItext.currentTextColor)
            putString(categoryTextKey, categoryText.text.toString())
            putBoolean(infoButtonVisibleKey, infoButton.isVisible)
        }
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        massText.setText(savedInstanceState.getString(massTextKey))
        heightText.setText(savedInstanceState.getString(heightTextKey))
        massPrompt.text = savedInstanceState.getString(massPromptKey)
        heightPrompt.text = savedInstanceState.getString(heightPromptKey)
        isMetric = savedInstanceState.getBoolean(isMetricKey)
        BMItext.text = savedInstanceState.getString(bmiTextKey)
        BMItext.setTextColor(savedInstanceState.getInt(bmiTextColorKey))
        categoryText.text = savedInstanceState.getString(categoryTextKey)
        infoButton.isVisible = savedInstanceState.getBoolean(infoButtonVisibleKey)

        super.onRestoreInstanceState(savedInstanceState)
    }

    private fun countBmi(): Boolean {
        categoryText.text = ""
        BMItext.text = ""
        infoButton.isVisible = false
        var goodInput = false
        when {
            massText.text.isEmpty() -> massText.error = getString(R.string.emptyError)
            massText.text.toString().toInt() == 0 -> massText.error = getString(R.string.noZeroError)
            massText.text.toString().toInt() >= 1000 -> massText.error = getString(R.string.over999Error)
            else -> goodInput = true
        }
        when {
            heightText.text.isEmpty() -> {
                heightText.error = getString(R.string.emptyError)
                goodInput = false
            }
            heightText.text.toString().toInt() == 0 -> {
                heightText.error = getString(R.string.noZeroError)
                goodInput = false
            }
        }

        if (goodInput) {

            val bmi = if (isMetric) {
                BmiForKgCm(massText.text.toString().toInt(), heightText.text.toString().toInt())
            } else {
                BmiForLbIn(massText.text.toString().toInt(), heightText.text.toString().toInt())
            }

            val bmiVal = bmi.countBmi()
            val format = DecimalFormat("#.##")
            BMItext.text = format.format(bmiVal)

            setDescAndColor(bmiVal)
            infoButton.isVisible = true
        }

        return goodInput
    }

    private fun setDescAndColor(bmiVal: Double) {
        when {
            bmiVal < 18.5 -> {
                categoryText.text = getString(R.string.underweight)
                BMItext.setTextColor(ContextCompat.getColor(applicationContext, R.color.lapisLazuli))
            }
            bmiVal < 25.0 -> {
                categoryText.text = getString(R.string.normalWeight)
                BMItext.setTextColor(ContextCompat.getColor(applicationContext, R.color.grynszpan))
            }
            bmiVal < 30.0 -> {
                categoryText.text = getString(R.string.overweight)
                BMItext.setTextColor(ContextCompat.getColor(applicationContext, R.color.gold))
            }
            bmiVal < 35.0 -> {
                categoryText.text = getString(R.string.obese)
                BMItext.setTextColor(ContextCompat.getColor(applicationContext, R.color.orange))
            }
            else -> {
                categoryText.text = getString(R.string.severelyObese)
                BMItext.setTextColor(ContextCompat.getColor(applicationContext, R.color.rozPompejanski))
            }
        }

    }


}

