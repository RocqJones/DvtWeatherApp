package com.rocqjones.dvt.weatherapp.logic.helpers

import android.content.Context
import com.google.android.libraries.places.api.Places
import com.rocqjones.dvt.weatherapp.logic.providers.PlacesProvider

class DefaultPlacesProvider : PlacesProvider {
    override fun isInitialized(): Boolean {
        return Places.isInitialized()
    }

    override fun initialize(context: Context, apiKey: String) {
        Places.initialize(context, apiKey)
    }

    override fun deInitialize() {
        Places.deinitialize()
    }
}