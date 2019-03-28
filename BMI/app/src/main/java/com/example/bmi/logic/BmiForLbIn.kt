package com.example.bmi.logic

class BmiForLbIn(var mass: Int, var height: Int) : Bmi{

    override fun countBmi(): Double {
        val bmi : Double = mass*703.0/(height*height)
        return bmi
    }
}