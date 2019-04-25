package com.example.bmi

import com.example.bmi.logic.BmiForKgCm
import com.example.bmi.logic.BmiForLbIn
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    @Test
    fun for_valid_data_return_bmi() {
        val bmi = BmiForKgCm(65, 170)

        assertEquals(22.491, bmi.countBmi(), 0.001)
    }

    @Test
    fun for_valid_imperial_data_return_bmi(){
        val bmi = BmiForLbIn(80,50)

        assertEquals(22.496, bmi.countBmi(), 0.001)
    }
}
