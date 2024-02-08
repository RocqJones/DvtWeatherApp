package com.rocqjones.dvt.weatherapp.api

import com.google.gson.Gson
import com.rocqjones.dvt.weatherapp.configs.Constants
import com.rocqjones.dvt.weatherapp.logic.network.interfaces.ApiInterface
import junit.framework.TestCase
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

internal class ApiTest {

    private lateinit var retroInstance: Retrofit
    private lateinit var apiInterface : ApiInterface

    @Before
    @Throws(Exception::class)
    fun setUp() {
        retroInstance = Retrofit.Builder().baseUrl(
            Constants.BASE_URL
        ).addConverterFactory(
            GsonConverterFactory.create()
        ).build()

        apiInterface = ApiInterface
    }

    @Test
    fun testRetrofitInstance() {
        //Assert that, Retrofit's base url matches to our BASE_URL
        assert(retroInstance.baseUrl().toString() == Constants.BASE_URL)
    }

    @Test
    fun testGetCurrentWeather() {
        val params: MutableMap<String, String> = HashMap()
        params["lat"] = Constants.defaultLat.toString()
        params["lon"] = Constants.defaultLong.toString()
        params["appid"] = Constants.APP_ID

        println("testGetCurrentWeather request: ${Gson().toJson(params)}")

        val response = apiInterface.weatherApi.fetchCurrentWeather(params)?.execute()
        println("testListGadgetQuotes response: ${Gson().toJson(response?.body())}")
        // check the api call went through
        TestCase.assertEquals("code is 200", 200, response?.code())
        val responseBody = response?.body()
        // check for success
        TestCase.assertEquals("name is 200", 200, responseBody?.code)
        TestCase.assertEquals("name is Nairobi", "Nairobi", responseBody?.name)
    }

    @Test
    fun testGetWeatherForecast() {
        val params: MutableMap<String, String> = HashMap()
        params["lat"] = Constants.defaultLat.toString()
        params["lon"] = Constants.defaultLong.toString()
        params["appid"] = Constants.APP_ID

        println("testGetWeatherForecast request: ${Gson().toJson(params)}")

        val response = apiInterface.weatherApi.fetchForecastWeather(params)?.execute()
        println("testListGadgetQuotes response: ${Gson().toJson(response?.body())}")
        // check the api call went through
        TestCase.assertEquals("code is 200", 200, response?.code())
        val responseBody = response?.body()
        // check for success
        TestCase.assertEquals("name is 200", 200, responseBody?.code)
    }
}