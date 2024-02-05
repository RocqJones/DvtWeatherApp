package com.rocqjones.dvt.weatherapp.logic.room.repo

import android.util.Log
import androidx.annotation.WorkerThread
import com.rocqjones.dvt.weatherapp.logic.models.entities.FavouriteWeatherModel
import com.rocqjones.dvt.weatherapp.logic.room.dao.FavouriteDao
import kotlinx.coroutines.flow.Flow

class FavouriteRepository(private val favouriteDao: FavouriteDao) {

    private val tag = "FavouriteRepo"

    val getAllFavouriteWeather : Flow<List<FavouriteWeatherModel>> = favouriteDao.getAllFavouriteWeather()

    @WorkerThread
    suspend fun insert(favouriteWeatherModel: FavouriteWeatherModel) {
        try {
            favouriteDao.insert(favouriteWeatherModel)
        } catch (e: Exception) {
            Log.e(tag, "insert Error: ", e)
        }
    }

    @WorkerThread
    suspend fun deleteFavouriteItem(id: Int) {
        try {
            favouriteDao.deleteFavouriteItem(id)
        } catch (e: Exception) {
            Log.e(tag, "deleteFavouriteItem Error: ", e)
        }
    }
}