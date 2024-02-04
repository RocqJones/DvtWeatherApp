package com.rocqjones.dvt.weatherapp.logic.network.repo

import com.rocqjones.dvt.weatherapp.logic.executors.IExecute
import com.rocqjones.dvt.weatherapp.logic.models.response.CurrentWeatherResponseModel
import com.rocqjones.dvt.weatherapp.logic.models.response.ForecastWeatherResponseModel
import com.rocqjones.dvt.weatherapp.logic.network.interfaces.ApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * enqueue - Asynchronously send the request and notify callback of its response or if an error
 * occurred talking to the server, creating the request, or processing the response.
 */
class ApiRepository {

    fun fetchCurrentWeather(
        params: MutableMap<String, String>,
        callback: IExecute<CurrentWeatherResponseModel>
    ) {
        try {
            ApiInterface.weatherApi.fetchCurrentWeather(params)?.enqueue(
                object : Callback<CurrentWeatherResponseModel?> {
                    override fun onResponse(
                        call: Call<CurrentWeatherResponseModel?>,
                        response: Response<CurrentWeatherResponseModel?>
                    ) {
                        callback.run(response, null)
                    }

                    override fun onFailure(call: Call<CurrentWeatherResponseModel?>, t: Throwable) {
                        callback.run(null, t)
                    }
                }
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun fetchForecastWeather(
        params: MutableMap<String, String>,
        callback: IExecute<ForecastWeatherResponseModel>
    ) {
        try {
            ApiInterface.weatherApi.fetchForecastWeather(params)?.enqueue(
                object : Callback<ForecastWeatherResponseModel?> {
                    override fun onResponse(
                        call: Call<ForecastWeatherResponseModel?>,
                        response: Response<ForecastWeatherResponseModel?>
                    ) {
                        callback.run(response, null)
                    }

                    override fun onFailure(call: Call<ForecastWeatherResponseModel?>, t: Throwable) {
                        callback.run(null, t)
                    }
                }
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}