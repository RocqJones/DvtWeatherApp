package com.rocqjones.dvt.weatherapp.logic.vm

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.rocqjones.dvt.weatherapp.logic.models.entities.CurrentWeatherModel
import com.rocqjones.dvt.weatherapp.logic.room.repo.CurrentRepository
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class ViewModelCurrent(private val repository: CurrentRepository) : ViewModel() {

    private val tag = "ViewModelCurrent"

    val getAllCurrentWeather: LiveData<List<CurrentWeatherModel>> = repository.getAllCurrentWeather.asLiveData()

    /** Launching a new coroutine to insert/update the data in a non-blocking way */
    fun insert(
        currentWeatherModel: CurrentWeatherModel
    ) = viewModelScope.launch {
        try {
            repository.insert(currentWeatherModel)
        } catch (e: Exception) {
            Log.e(tag, "insert Error: ", e)
        }
    }

    fun update(
        currentWeatherModel: CurrentWeatherModel
    ) = viewModelScope.launch {
        try {
            repository.update(currentWeatherModel)
        } catch (e: Exception) {
            Log.e(tag, "insert Error: ", e)
        }
    }

    fun deleteAllCurrentDetails() = viewModelScope.launch {
        try {
            repository.deleteAllCurrentDetails()
        } catch (e: Exception) {
            Log.e(tag, "deleteAll Error: ", e)
        }
    }
}

class CurrentViewModelFactory(
    private val repository: CurrentRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ViewModelCurrent::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ViewModelCurrent(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}