package com.rocqjones.dvt.weatherapp.logic.vm

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.rocqjones.dvt.weatherapp.logic.models.entities.FavouriteWeatherModel
import com.rocqjones.dvt.weatherapp.logic.room.repo.FavouriteRepository
import kotlinx.coroutines.launch

class ViewModelFavourite(private val repository: FavouriteRepository) : ViewModel() {

    private val tag = "ViewModelFavourite"

    val getAllFavouriteWeather : LiveData<List<FavouriteWeatherModel>> =
        repository.getAllFavouriteWeather.asLiveData()

    fun insert(
        favouriteWeatherModel: FavouriteWeatherModel
    ) = viewModelScope.launch {
        try {
            repository.insert(favouriteWeatherModel)
        } catch (e: Exception) {
            Log.e(tag, "insert Error: ", e)
        }
    }

    fun deleteFavouriteItem(
        id: Int
    ) = viewModelScope.launch {
        try {
            repository.deleteFavouriteItem(id)
        } catch (e: Exception) {
            Log.e(tag, "delete Error: ", e)
        }
    }
}
class FavouriteViewModelFactory(
    private val repository: FavouriteRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ViewModelFavourite::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ViewModelFavourite(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}