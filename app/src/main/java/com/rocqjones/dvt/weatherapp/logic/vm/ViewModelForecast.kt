package com.rocqjones.dvt.weatherapp.logic.vm

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.rocqjones.dvt.weatherapp.logic.models.entities.ForecastWeatherModel
import com.rocqjones.dvt.weatherapp.logic.room.repo.ForecastRepository
import kotlinx.coroutines.launch

class ViewModelForecast(private val repository: ForecastRepository) : ViewModel() {

    private val tag = "ViewModelForecast"

    val getAllForecastWeather : LiveData<List<ForecastWeatherModel>> =
        repository.getAllForecastWeather.asLiveData()

    fun insert(
        forecastWeatherModel: ForecastWeatherModel
    ) = viewModelScope.launch {
        try {
            repository.insert(forecastWeatherModel)
        } catch (e: Exception) {
            Log.e(tag, "insert Error: ", e)
        }
    }

    fun deleteAllForecastDetails() = viewModelScope.launch {
        try {
            repository.deleteAllForecastDetails()
        } catch (e: Exception) {
            Log.e(tag, "delete Error: ", e)
        }
    }
}
class ForecastViewModelFactory(
    private val repository: ForecastRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ViewModelForecast::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ViewModelForecast(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}