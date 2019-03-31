package com.example.bmi

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bmi.logic.HistoryEntry
import com.example.bmi.logic.HistoryQueue
import com.google.gson.Gson
import java.text.DateFormat.getDateTimeInstance
import java.util.*

class HistoryActivity : AppCompatActivity() {

    private var historyQueue = HistoryQueue()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        title = getString(R.string.history)


        val sharedPref = getSharedPreferences(MainActivity.historySharedPrefKey, Context.MODE_PRIVATE)
        if (sharedPref.contains(MainActivity.historyKey)) {
            val gson = Gson()
            val json = sharedPref.getString(MainActivity.historyKey, "")
            historyQueue = gson.fromJson<HistoryQueue>(json, HistoryQueue::class.java)
        }

        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = Adapter(historyQueue.toArrayList())
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }


    class Adapter(private val values: ArrayList<HistoryEntry>) : RecyclerView.Adapter<Adapter.ViewHolder>() {

        override fun getItemCount(): Int = values.size

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Adapter.ViewHolder {
            val itemView = LayoutInflater.from(parent.context).inflate(R.layout.history_row_layout, parent, false)
            return ViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val entry = values[position]
            holder.bmiText?.text = entry.bmiVal
            holder.bmiText?.setTextColor(entry.resultColorCode)
            if (!entry.isMetric) {
                holder.heightText?.setText(R.string.height_in)
                holder.massText?.setText(R.string.mass_lb)
            }
            holder.heightText?.append(": ${entry.height}")
            holder.massText?.append(": ${entry.mass}")
            holder.dateText?.text = getDateTimeInstance().format(entry.date)
        }

        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var bmiText: TextView? = null
            var heightText: TextView? = null
            var massText: TextView? = null
            var dateText: TextView? = null

            init {
                bmiText = itemView.findViewById(R.id.bmiHistory)
                heightText = itemView.findViewById(R.id.heightHistoryEntry)
                massText = itemView.findViewById(R.id.massHistoryEntry)
                dateText = itemView.findViewById(R.id.dateHistoryEntry)
            }
        }
    }


}
