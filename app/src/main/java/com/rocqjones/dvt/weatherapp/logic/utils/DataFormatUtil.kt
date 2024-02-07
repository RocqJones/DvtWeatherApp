package com.rocqjones.dvt.weatherapp.logic.utils

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.ui.graphics.Color
import com.rocqjones.dvt.weatherapp.R
import com.rocqjones.dvt.weatherapp.ui.theme.cloudyBg
import com.rocqjones.dvt.weatherapp.ui.theme.rainyBg
import com.rocqjones.dvt.weatherapp.ui.theme.sunnyBg
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

object DataFormatUtil {

    fun convertKelvinToCelsius(k: Double): Double {
        return try {
            val c = 273.15
            val res = k - c
            // ans to 1 decimal place
            return String.format("%.1f", res).toDouble()
        } catch (e: Exception) {
            k
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun dateTimeConverter(dateTimeStr: String): String {
        return try {
            val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("EEE, MMM d, yyyy")
            val pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z")
            val zonedDateTime = ZonedDateTime.parse("$dateTimeStr Z", pattern)
            // println("Date Time $zonedDateTime")
            // println("Date Time ${formatter.format(zonedDateTime)}")
            return formatter.format(zonedDateTime)
        } catch (e: Exception) {
            dateTimeStr
        }
    }

    // Provides Bg Color based on the Weather
    fun getBgColor(it: String?): Color {
        return when {
            it.equals("Clouds") -> cloudyBg
            it.equals("Rain") -> rainyBg
            else -> sunnyBg
        }
    }

    // Provides Bg Drawable based on the Weather
    fun getBgDrawable(it: String?): Int {
        return when {
            it.equals("Clouds") -> R.drawable.forest_cloudy
            it.equals("Rain") -> R.drawable.forest_rainy
            else -> R.drawable.forest_sunny
        }
    }
}