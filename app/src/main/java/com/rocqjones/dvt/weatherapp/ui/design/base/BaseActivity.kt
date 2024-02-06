package com.rocqjones.dvt.weatherapp.ui.design.base

import android.app.Activity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import com.google.gson.Gson
import com.rocqjones.dvt.weatherapp.configs.BaseAppConfig
import com.rocqjones.dvt.weatherapp.configs.Constants
import com.rocqjones.dvt.weatherapp.logic.models.entities.CurrentWeatherModel
import com.rocqjones.dvt.weatherapp.logic.models.entities.ForecastWeatherModel
import com.rocqjones.dvt.weatherapp.logic.models.response.CurrentWeatherResponseModel
import com.rocqjones.dvt.weatherapp.logic.models.response.ForecastWeatherResponseModel
import com.rocqjones.dvt.weatherapp.logic.network.repo.ApiRepository
import com.rocqjones.dvt.weatherapp.logic.utils.LocationUtil
import com.rocqjones.dvt.weatherapp.logic.vm.CurrentViewModelFactory
import com.rocqjones.dvt.weatherapp.logic.vm.FavouriteViewModelFactory
import com.rocqjones.dvt.weatherapp.logic.vm.ForecastViewModelFactory
import com.rocqjones.dvt.weatherapp.logic.vm.ViewModelCurrent
import com.rocqjones.dvt.weatherapp.logic.vm.ViewModelFavourite
import com.rocqjones.dvt.weatherapp.logic.vm.ViewModelForecast
import com.rocqjones.dvt.weatherapp.logic.vm.WeatherApiViewModel
import com.rocqjones.dvt.weatherapp.logic.vm.WeatherApiViewModelFactory

abstract class BaseActivity : ComponentActivity() {

    private val tag = "BaseActivity"

    lateinit var activityContext: Activity
    lateinit var locationUtil: LocationUtil

    private val apiViewModel: WeatherApiViewModel by viewModels {
        WeatherApiViewModelFactory(ApiRepository())
    }

    /** current room ViewModel */
    private val viewModelCurrent: ViewModelCurrent by viewModels {
        CurrentViewModelFactory((this.applicationContext as BaseAppConfig).currentRepository)
    }

    /** forecast room ViewModel */
    private val viewModelForecast: ViewModelForecast by viewModels {
        ForecastViewModelFactory((this.applicationContext as BaseAppConfig).forecastRepository)
    }

    /** favourite room ViewModel */
    private val viewModelFavourite: ViewModelFavourite by viewModels {
        FavouriteViewModelFactory((this.applicationContext as BaseAppConfig).favouriteRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)

        initializeVariables()
    }

    private fun initializeVariables() {
        try {
            activityContext = activityContext()
            locationUtil = LocationUtil(activityContext)
        } catch (e: Exception) {
            Log.e(tag, "initializeVariables", e)
        }
    }

    protected abstract fun activityContext(): Activity

    fun getCurrentWeather() {
        try {
            val params: MutableMap<String, String> = HashMap()
            params["lat"] = "-1.2240624585163389"
            params["lon"] = "36.919931302314055"
            params["appid"] = Constants.APP_ID

            apiViewModel.fetchCurrentWeather(params).observe(this) {
                if (it != null) {
                    Log.d(tag, "currentWeather: ${Gson().toJson(it)}")
                    viewModelCurrent.deleteAllCurrentDetails()
                    insertCurrentToRoom(it)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /** Insert Current Weather */
    private fun insertCurrentToRoom(body: CurrentWeatherResponseModel?) {
        try {
            viewModelCurrent.insert(
                CurrentWeatherModel(
                    null,
                    body?.name,
                    body?.userCoordinates?.latitude,
                    body?.userCoordinates?.longitude,
                    body?.mainDetails?.temp_current,
                    body?.mainDetails?.temp_min,
                    body?.mainDetails?.temp_max,
                    body?.weatherDetails?.get(0)?.main,
                    body?.weatherDetails?.get(0)?.description
                )
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun loadCurrentFromRoom(): List<CurrentWeatherModel>? {
        var m : List<CurrentWeatherModel> = listOf()
        return try {
            viewModelCurrent.getAllCurrentWeather.observe(this) { current ->
                current.let {
                    Log.d("loadCurrentFromRoom", "$it")
                    if (it != null) {
                        m = it
                    }
                }
            }
            m
        } catch (e: Exception) {
            e.printStackTrace()
            listOf()
        }
    }

    fun getWeatherForecast() {
        try {
            val params: MutableMap<String, String> = HashMap()
            params["lat"] = "-1.2240624585163389"
            params["lon"] = "36.919931302314055"
            params["appid"] = Constants.APP_ID

            apiViewModel.fetchForecastWeather(params).observe(this) {
                if (it != null) {
                    Log.d(tag, "forecastWeather: ${Gson().toJson(it)}")
                    viewModelForecast.deleteAllForecastDetails()
                    insertForecastToRoom(it)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /** Insert forecast Weather */
    private fun insertForecastToRoom(body: ForecastWeatherResponseModel) {
        try {
            body.foreCastList?.toMutableList()?.map {
                ForecastWeatherModel(
                    null,
                    body.city?.name,
                    body.city?.userCoordinates?.latitude,
                    body.city?.userCoordinates?.longitude,
                    it.mainDetails?.temp_current,
                    it.mainDetails?.temp_min,
                    it.mainDetails?.temp_max,
                    it.weatherDetails?.get(0)?.main,
                    it.weatherDetails?.get(0)?.description,
                    it.dt_txt
                )
            }?.forEach { viewModelForecast.insert(it) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}