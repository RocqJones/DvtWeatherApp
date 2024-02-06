package com.rocqjones.dvt.weatherapp.logic.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.rocqjones.dvt.weatherapp.logic.models.entities.CurrentWeatherModel
import kotlinx.coroutines.flow.Flow

@Dao
interface CurrentDao {
    @Query("SELECT * FROM t_current ORDER BY id ASC")
    fun getAllCurrentWeather(): Flow<List<CurrentWeatherModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(currentWeatherModel: CurrentWeatherModel)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(currentWeatherModel: CurrentWeatherModel)

    @Query("DELETE FROM t_current")
    suspend fun deleteAllCurrentDetails()
}