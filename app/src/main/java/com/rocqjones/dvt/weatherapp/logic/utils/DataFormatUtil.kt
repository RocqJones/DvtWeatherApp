package com.rocqjones.dvt.weatherapp.logic.utils

import androidx.compose.ui.graphics.Color
import com.rocqjones.dvt.weatherapp.R
import com.rocqjones.dvt.weatherapp.ui.theme.cloudyBg
import com.rocqjones.dvt.weatherapp.ui.theme.rainyBg
import com.rocqjones.dvt.weatherapp.ui.theme.sunnyBg
import java.text.SimpleDateFormat
import java.util.Locale

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


    fun dateTimeConverter(dateTimeStr: String): String {
        return try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            val outputFormat = SimpleDateFormat("EEE, MMM d, h a", Locale.getDefault())
            val date = inputFormat.parse(dateTimeStr)
            outputFormat.format(date!!)
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

    // Provides List Drawable Icon based on the Weather
    fun getBgIcon(it: String?): Int {
        return when {
            it.equals("Clouds") -> R.drawable.partlysunny_3x
            it.equals("Rain") -> R.drawable.rain_3x
            else -> R.drawable.clear_3x
        }
    }
}