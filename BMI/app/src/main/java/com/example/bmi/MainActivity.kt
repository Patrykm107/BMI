package com.example.bmi

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.view.Menu
import android.view.MenuItem
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.example.bmi.logic.BmiForKgCm
import com.example.bmi.logic.BmiForLbIn
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.IllegalArgumentException
import java.text.DecimalFormat

class MainActivity : AppCompatActivity() {
    private var isMetric = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        calculateBMI.setOnClickListener {
            countBmi()
        }

        infoButton.setOnClickListener{
            val intent = Intent(this, InfoActivity::class.java)
            intent.putExtra("bmi", BMItext.text)

            startActivity(intent)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when(item.itemId){
            R.id.UnitsToggle -> {
                isMetric = !isMetric
                massText.setText("")
                heightText.setText("")
                BMItext.text = ""
                categoryText.text = ""
                infoButton.isVisible=false

                if(!isMetric){
                    massPrompt.setText(R.string.mass_lb)
                    heightPrompt.setText(R.string.height_in)
                }else{
                    massPrompt.setText(R.string.mass_kg)
                    heightPrompt.setText(R.string.height_cm)
                }
                true
            }
            R.id.aboutMeButton -> {
                val intent = Intent(this,AboutMeActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    }

    override fun onSaveInstanceState(outState: Bundle?) {
        outState?.run{
            putString("mass", massText.text.toString())
            putString("height", heightText.text.toString())
            putString("massPrompt", massPrompt.text.toString())
            putString("heightPrompt", heightPrompt.text.toString())
            putBoolean("isMetric", isMetric)
            putString("bmi", BMItext.text.toString())
            putInt("color", BMItext.currentTextColor)
            putString("category", categoryText.text.toString())
            putBoolean("infoButtonVisible", infoButton.isVisible)
        }
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        massText.setText(savedInstanceState.getString("mass"))
        heightText.setText(savedInstanceState.getString("height"))
        massPrompt.text=savedInstanceState.getString("massPrompt")
        heightPrompt.text=savedInstanceState.getString("heightPrompt")
        isMetric=savedInstanceState.getBoolean("isMetric")
        BMItext.text = savedInstanceState.getString("bmi")
        BMItext.setTextColor(savedInstanceState.getInt("color"))
        categoryText.text=savedInstanceState.getString("category")
        infoButton.isVisible=savedInstanceState.getBoolean("infoButtonVisible")

        super.onRestoreInstanceState(savedInstanceState)
    }

    private fun countBmi(){
        categoryText.text=""
        BMItext.text=""
        infoButton.isVisible=false
        var goodInput = false
        when {
            massText.text.isEmpty() -> massText.error = getString(R.string.emptyError)
            massText.text.toString().toInt()==0 -> massText.error = getString(R.string.noZeroError)
            else -> goodInput = true
        }
        when {
            heightText.text.isEmpty() -> {
                heightText.error = getString(R.string.emptyError)
                goodInput = false
            }
            heightText.text.toString().toInt()==0 -> {
                heightText.error = getString(R.string.noZeroError)
                goodInput = false
            }
        }

        if(goodInput) {

            val bmi = if (isMetric) {
                BmiForKgCm(massText.text.toString().toInt(), heightText.text.toString().toInt())
            } else {
                BmiForLbIn(massText.text.toString().toInt(), heightText.text.toString().toInt())
            }

            val bmiVal = bmi.countBmi()
            val format = DecimalFormat("#.##")
            BMItext.text = format.format(bmiVal)

            setDescAndColor(bmiVal)
            infoButton.isVisible=true
        }
    }

    private fun setDescAndColor(bmiVal: Double){
        when {
            bmiVal<18.5 -> {
                categoryText.text=getString(R.string.underweight)
                BMItext.setTextColor(ContextCompat.getColor(applicationContext,R.color.lapisLazuli))
            }
            bmiVal<25.0 -> {
                categoryText.text=getString(R.string.normalWeight)
                BMItext.setTextColor(ContextCompat.getColor(applicationContext,R.color.grynszpan))
            }
            bmiVal<30.0 -> {
                categoryText.text = getString(R.string.overweight)
                BMItext.setTextColor(ContextCompat.getColor(applicationContext, R.color.gold))
            }
            bmiVal<35.0 ->{
                categoryText.text = getString(R.string.obese)
                BMItext.setTextColor(ContextCompat.getColor(applicationContext, R.color.orange))
            }
            else -> {
                categoryText.text=getString(R.string.severelyObese)
                BMItext.setTextColor(ContextCompat.getColor(applicationContext,R.color.rozPompejanski))
            }
        }

    }
}

