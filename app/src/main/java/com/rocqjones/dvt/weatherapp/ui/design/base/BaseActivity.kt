package com.rocqjones.dvt.weatherapp.ui.design.base

import android.app.Activity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import com.google.gson.Gson
import com.rocqjones.dvt.weatherapp.configs.Constants
import com.rocqjones.dvt.weatherapp.logic.network.repo.ApiRepository
import com.rocqjones.dvt.weatherapp.logic.utils.LocationUtil
import com.rocqjones.dvt.weatherapp.ui.design.vm.WeatherApiViewModel
import com.rocqjones.dvt.weatherapp.ui.design.vm.WeatherApiViewModelFactory

abstract class BaseActivity : ComponentActivity() {

    private val tag = "BaseActivity"

    lateinit var activityContext: Activity
    lateinit var locationUtil: LocationUtil

    private val apiViewModel: WeatherApiViewModel by viewModels {
        WeatherApiViewModelFactory(ApiRepository())
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
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}