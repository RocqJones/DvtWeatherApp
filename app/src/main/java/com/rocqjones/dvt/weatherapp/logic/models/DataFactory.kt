package com.rocqjones.dvt.weatherapp.logic.models

object DataFactory {
    private var placesModel: PlacesModel? = null

    fun setPlaceModel(model: PlacesModel?) {
        placesModel = model
    }

    fun getPlaceModel(): PlacesModel? {
        if (placesModel == null) {
            placesModel = null
        }
        return placesModel
    }
}