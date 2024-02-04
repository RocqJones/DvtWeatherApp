package com.rocqjones.dvt.weatherapp.ui.design.vm

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.rocqjones.dvt.weatherapp.logic.executors.IExecute
import com.rocqjones.dvt.weatherapp.logic.models.response.CurrentWeatherResponseModel
import com.rocqjones.dvt.weatherapp.logic.models.response.ForecastWeatherResponseModel
import com.rocqjones.dvt.weatherapp.logic.network.repo.ApiRepository
import kotlinx.coroutines.launch
import retrofit2.Response

/**
 * "viewModelScope.launch" - Launches a new coroutine without blocking the current thread and returns
 *                           a reference to the coroutine as a Job.
 */
class WeatherApiViewModel(private val repository: ApiRepository) : ViewModel() {

    private val tag = "WeatherViewModel"

    fun fetchCurrentWeather(
        params: MutableMap<String, String>
    ): MutableLiveData<CurrentWeatherResponseModel?> {
        val fetchCurrentWeatherResponse : MutableLiveData<CurrentWeatherResponseModel?> = MutableLiveData()
        return try {
            viewModelScope.launch {
                repository.fetchCurrentWeather(
                    params,
                    object : IExecute<CurrentWeatherResponseModel> {
                        override fun run(
                            result: Response<CurrentWeatherResponseModel?>?,
                            t: Throwable?
                        ) {
                            Log.d(tag, "currentWeather: result: $result")
                            when {
                                result != null -> {
                                    if (result.isSuccessful) {
                                        Log.e(tag, "currentWeather data:" + Gson().toJson(
                                            result.body()
                                        ))
                                        fetchCurrentWeatherResponse.value = result.body()
                                    }
                                }

                                else -> {
                                    if (t != null) {
                                        fetchCurrentWeatherResponse.value = null
                                    }
                                }
                            }
                        }
                    }
                )
            }
            fetchCurrentWeatherResponse
        } catch (e: Exception) {
            Log.e(tag, "fetchCurrentWeather Error:", e)
            fetchCurrentWeatherResponse
        }
    }

    fun fetchForecastWeather(
        params: MutableMap<String, String>
    ): MutableLiveData<ForecastWeatherResponseModel?> {
        val fetchForecastWeatherResponse : MutableLiveData<ForecastWeatherResponseModel?> = MutableLiveData()
        return try {
            viewModelScope.launch {
                repository.fetchForecastWeather(
                    params,
                    object : IExecute<ForecastWeatherResponseModel> {
                        override fun run(
                            result: Response<ForecastWeatherResponseModel?>?,
                            t: Throwable?
                        ) {
                            Log.d(tag, "forecastWeather: result: $result")
                            when {
                                result != null -> {
                                    if (result.isSuccessful) {
                                        Log.e(tag, "forecastWeather data:" + Gson().toJson(
                                            result.body()
                                        ))
                                        fetchForecastWeatherResponse.value = result.body()
                                        // IResult.Success(result.body())
                                    }
                                }

                                else -> {
                                    if (t != null) {
                                        fetchForecastWeatherResponse.value = null
                                        // IResult.Error(t)
                                    }
                                }
                            }
                        }
                    }
                )
            }
            fetchForecastWeatherResponse
        } catch (e: Exception) {
            Log.e(tag, "fetchForecastWeather Error:", e)
            fetchForecastWeatherResponse
        }
    }
}

class WeatherApiViewModelFactory(private val repository: ApiRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WeatherApiViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return WeatherApiViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}