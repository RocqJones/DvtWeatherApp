package com.rocqjones.dvt.weatherapp.configs

import android.app.Application
import com.rocqjones.dvt.weatherapp.logic.room.db.WeatherRoomDb
import com.rocqjones.dvt.weatherapp.logic.room.repo.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class BaseAppConfig : Application() {

    private val applicationScope = CoroutineScope(SupervisorJob())

    // db
    private val database by lazy {
        WeatherRoomDb.getDatabase(this, applicationScope)
    }

    // Repos
    val currentRepository by lazy { CurrentRepository(database.currentDao()) }
    val forecastRepository by lazy { ForecastRepository(database.forecastDao()) }
    val favouriteRepository by lazy { FavouriteRepository(database.favouriteDao()) }
}