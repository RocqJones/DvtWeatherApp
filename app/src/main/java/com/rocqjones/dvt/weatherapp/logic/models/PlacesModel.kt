package com.rocqjones.dvt.weatherapp.logic.models

import com.google.android.gms.maps.model.LatLng

data class PlacesModel (
    var id: String? = null,
    var placeName: String? = null,
    var placeLatLong: LatLng? = null
)
