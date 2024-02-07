package com.rocqjones.dvt.weatherapp.logic.listeners

import com.rocqjones.dvt.weatherapp.logic.models.LocationModel

interface LocationModelListener {
    fun onResponse(model: LocationModel?)
}