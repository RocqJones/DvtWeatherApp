package com.rocqjones.dvt.weatherapp.logic.room.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.rocqjones.dvt.weatherapp.logic.models.entities.CurrentWeatherModel
import com.rocqjones.dvt.weatherapp.logic.models.entities.FavouriteWeatherModel
import com.rocqjones.dvt.weatherapp.logic.models.entities.ForecastWeatherModel
import com.rocqjones.dvt.weatherapp.logic.room.dao.CurrentDao
import com.rocqjones.dvt.weatherapp.logic.room.dao.FavouriteDao
import com.rocqjones.dvt.weatherapp.logic.room.dao.ForecastDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(
    entities = [
        CurrentWeatherModel::class,
        ForecastWeatherModel::class,
        FavouriteWeatherModel::class
    ],
    version = 2,
    exportSchema = false
)
abstract class WeatherRoomDb : RoomDatabase() {

    abstract fun currentDao(): CurrentDao
    abstract fun forecastDao(): ForecastDao
    abstract fun favouriteDao(): FavouriteDao

    companion object {

        @Volatile
        private var INSTANCE: WeatherRoomDb? = null

        fun getDatabase(
            context: Context,
            scope: CoroutineScope
        ): WeatherRoomDb {
            // If the INSTANCE is not null, then return it, if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WeatherRoomDb::class.java,
                    "forecast_db" // db name
                ).fallbackToDestructiveMigration().addCallback(
                    RoomDatabaseCallback(scope)
                ).build() // 'fallbackToDestructiveMigration()' replaces the db if there is conflict

                INSTANCE = instance

                instance
            }
        }
    }

    /** Uses coroutines to provide an instance of our db if available */
    class RoomDatabaseCallback(private val scope: CoroutineScope) : Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { _ -> scope.launch {} }
        }
    }
}