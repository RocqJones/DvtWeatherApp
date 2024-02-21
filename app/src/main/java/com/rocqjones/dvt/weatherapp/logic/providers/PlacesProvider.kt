package com.rocqjones.dvt.weatherapp.logic.providers

import android.content.Context

interface PlacesProvider {
    fun isInitialized(): Boolean
    fun initialize(context: Context, apiKey: String)
    fun deInitialize()
}