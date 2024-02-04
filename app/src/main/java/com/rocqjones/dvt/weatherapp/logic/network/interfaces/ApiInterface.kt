package com.rocqjones.dvt.weatherapp.logic.network.interfaces

import com.rocqjones.dvt.weatherapp.configs.Constants.BASE_URL
import com.rocqjones.dvt.weatherapp.logic.network.services.WebService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiInterface {
    private val retrofit by lazy {
        Retrofit.Builder().baseUrl(
            BASE_URL
        ).addConverterFactory(
            GsonConverterFactory.create()
        ).build()
    }

    // provides instance of Weather API interface
    val weatherApi: WebService by lazy {
        retrofit.create(WebService::class.java)
    }
}