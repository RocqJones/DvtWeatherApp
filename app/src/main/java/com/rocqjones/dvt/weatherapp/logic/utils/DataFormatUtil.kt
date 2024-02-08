package com.rocqjones.dvt.weatherapp.logic.utils

import android.util.Log
import java.text.SimpleDateFormat
import java.util.Locale

object DataFormatUtil {

    private const val tag = "DataFormatUtil"

    fun convertKelvinToCelsius(k: Double): Double {
        return try {
            val c = 273.15
            val res = k - c
            // ans to 1 decimal place
            return String.format("%.1f", res).toDouble()
        } catch (e: Exception) {
            Log.e(tag, "convertKelvinToCelsius Error: ", e)
            k
        }
    }


    fun dateTimeConverter(dateTimeStr: String): String {
        return try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            val outputFormat = SimpleDateFormat("EEE, MMM d, h a", Locale.getDefault())
            val date = inputFormat.parse(dateTimeStr)
            outputFormat.format(date!!)
        } catch (e: Exception) {
            Log.e(tag, "dateTimeConverter Error: ", e)
            dateTimeStr
        }
    }
}