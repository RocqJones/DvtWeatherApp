package com.rocqjones.dvt.weatherapp.logic.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rocqjones.dvt.weatherapp.logic.models.entities.FavouriteWeatherModel
import kotlinx.coroutines.flow.Flow

@Dao
interface FavouriteDao {
    @Query("SELECT * FROM t_favourite ORDER BY id ASC")
    fun getAllFavouriteWeather(): Flow<List<FavouriteWeatherModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(favouriteWeatherModel: FavouriteWeatherModel)

    @Query("DELETE FROM t_favourite WHERE id = :id")
    suspend fun deleteFavouriteItem(id: Int)
}