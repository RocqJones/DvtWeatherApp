package com.rocqjones.dvt.weatherapp.logic.room.repo

import android.util.Log
import androidx.annotation.WorkerThread
import com.rocqjones.dvt.weatherapp.logic.models.entities.CurrentWeatherModel
import com.rocqjones.dvt.weatherapp.logic.room.dao.CurrentDao
import kotlinx.coroutines.flow.Flow

class CurrentRepository(private val currentDao: CurrentDao) {

    private val tag = "CurrentRepo"

    val getAllCurrentWeather : Flow<List<CurrentWeatherModel>> = currentDao.getAllCurrentWeather()

    @WorkerThread
    suspend fun insert(currentWeatherModel: CurrentWeatherModel) {
        try {
            currentDao.insert(currentWeatherModel)
        } catch (e: Exception) {
            Log.e(tag, "insert Error: ", e)
        }
    }

    @WorkerThread
    suspend fun update(currentWeatherModel: CurrentWeatherModel) {
        try {
            currentDao.update(currentWeatherModel)
        } catch (e: Exception) {
            Log.e(tag, "update Error: ", e)
        }
    }

    @WorkerThread
    suspend fun deleteAllCurrentDetails() {
        try {
            currentDao.deleteAllCurrentDetails()
        } catch (e: Exception) {
            Log.e(tag, "deleteAllCurrentDetails Error: ", e)
        }
    }
}