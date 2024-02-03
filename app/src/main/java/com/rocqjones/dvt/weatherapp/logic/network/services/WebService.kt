package com.rocqjones.dvt.weatherapp.logic.network.services

import com.rocqjones.dvt.weatherapp.configs.Constants
import com.rocqjones.dvt.weatherapp.logic.models.response.CurrentWeatherResponseModel
import com.rocqjones.dvt.weatherapp.logic.models.response.ForecastWeatherResponseModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface WebService {
    @GET(Constants.weather_endpoint)
    suspend fun fetchCurrentWeather(
        @QueryMap params: MutableMap<String, String>
    ): Call<CurrentWeatherResponseModel?>?

    @GET(Constants.forecast_endpoint)
    suspend fun fetchForecastWeather(
        @QueryMap params: MutableMap<String, String>
    ): Call<ForecastWeatherResponseModel?>?
}