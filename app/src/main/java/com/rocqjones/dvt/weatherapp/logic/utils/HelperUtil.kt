package com.rocqjones.dvt.weatherapp.logic.utils

import androidx.compose.ui.graphics.Color
import com.rocqjones.dvt.weatherapp.R
import com.rocqjones.dvt.weatherapp.ui.theme.cloudyBg
import com.rocqjones.dvt.weatherapp.ui.theme.rainyBg
import com.rocqjones.dvt.weatherapp.ui.theme.sunnyBg

object HelperUtil {

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