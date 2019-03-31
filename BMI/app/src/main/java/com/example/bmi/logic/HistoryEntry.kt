package com.example.bmi.logic

import java.util.*

data class HistoryEntry(
    val bmiVal: String, val resultColorCode: Int, val height: String,
    val mass: String, val isMetric: Boolean, val date: Date
)