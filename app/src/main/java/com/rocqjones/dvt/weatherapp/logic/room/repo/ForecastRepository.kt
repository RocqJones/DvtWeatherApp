package com.rocqjones.dvt.weatherapp.logic.room.repo

import android.util.Log
import androidx.annotation.WorkerThread
import com.rocqjones.dvt.weatherapp.logic.models.entities.ForecastWeatherModel
import com.rocqjones.dvt.weatherapp.logic.room.dao.ForecastDao
import kotlinx.coroutines.flow.Flow

class ForecastRepository(private val forecastDao: ForecastDao) {

    private val tag = "ForecastRepo"

    val getAllForecastWeather : Flow<List<ForecastWeatherModel>> = forecastDao.getAllForecastWeather()

    @WorkerThread
    suspend fun insert(forecastWeatherModel: ForecastWeatherModel) {
        try {
            forecastDao.insert(forecastWeatherModel)
        } catch (e: Exception) {
            Log.e(tag, "insert Error: ", e)
        }
    }

    @WorkerThread
    suspend fun deleteAllForecastDetails() {
        try {
            forecastDao.deleteAllForecastDetails()
        } catch (e: Exception) {
            Log.e(tag, "deleteAllForecastDetails Error: ", e)
        }
    }
}