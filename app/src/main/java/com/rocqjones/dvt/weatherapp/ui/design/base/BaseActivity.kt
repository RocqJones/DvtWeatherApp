package com.rocqjones.dvt.weatherapp.ui.design.base

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.gson.Gson
import com.rocqjones.dvt.weatherapp.configs.BaseAppConfig
import com.rocqjones.dvt.weatherapp.configs.Constants
import com.rocqjones.dvt.weatherapp.logic.listeners.LocationModelListener
import com.rocqjones.dvt.weatherapp.logic.models.LocationModel
import com.rocqjones.dvt.weatherapp.logic.models.entities.CurrentWeatherModel
import com.rocqjones.dvt.weatherapp.logic.models.entities.ForecastWeatherModel
import com.rocqjones.dvt.weatherapp.logic.models.response.CurrentWeatherResponseModel
import com.rocqjones.dvt.weatherapp.logic.models.response.ForecastWeatherResponseModel
import com.rocqjones.dvt.weatherapp.logic.network.repo.ApiRepository
import com.rocqjones.dvt.weatherapp.logic.utils.HelperUtil
import com.rocqjones.dvt.weatherapp.logic.utils.LocationUtil
import com.rocqjones.dvt.weatherapp.logic.vm.CurrentViewModelFactory
import com.rocqjones.dvt.weatherapp.logic.vm.FavouriteViewModelFactory
import com.rocqjones.dvt.weatherapp.logic.vm.ForecastViewModelFactory
import com.rocqjones.dvt.weatherapp.logic.vm.ViewModelCurrent
import com.rocqjones.dvt.weatherapp.logic.vm.ViewModelFavourite
import com.rocqjones.dvt.weatherapp.logic.vm.ViewModelForecast
import com.rocqjones.dvt.weatherapp.logic.vm.WeatherApiViewModel
import com.rocqjones.dvt.weatherapp.logic.vm.WeatherApiViewModelFactory
import kotlin.system.exitProcess

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

    private val locationRequestCode = 99
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    override fun onStart() {
        super.onStart()
        // initialize fused client
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        initializeVariables()
    }

    private fun initializeVariables() {
        try {
            activityContext = activityContext()
        } catch (e: Exception) {
            Log.e(tag, "initializeVariables", e)
        }
    }

    protected abstract fun activityContext(): Activity

    override fun onResume() {
        super.onResume()
        checkNetwork()
        checkLocationPermissionStatus()
    }

    private fun checkNetwork() {
        try {
            when {
                HelperUtil.isConnectedToInternet(this) -> {
                    // Location logic
                    locationUtil = LocationUtil(
                        activityContext,
                        object : LocationModelListener {
                            override fun onResponse(
                                model: LocationModel?
                            ) {
                                Log.e(tag, "locationResponse lat: ${model?.latitude}, long: ${model?.longitude}")
                                when {
                                    model != null -> {
                                        getCurrentWeather(
                                            latitude = model.latitude ?: Constants.defaultLat,
                                            longitude = model.longitude ?: Constants.defaultLong
                                        )
                                        getWeatherForecast(
                                            latitude = model.latitude ?: Constants.defaultLat,
                                            longitude = model.longitude ?: Constants.defaultLong
                                        )
                                    }

                                    else -> {
                                        checkLocationPermissionStatus()
                                    }
                                }
                            }
                        }
                    )
                }

                else -> {
                    // No internet
                }
            }
        } catch (e: Exception) {
            Log.e(tag, "checkNetwork Error: ", e)
        }
    }

    fun getCurrentWeather(latitude: Double, longitude: Double) {
        try {
            val params: MutableMap<String, String> = HashMap()
            params["lat"] = latitude.toString()
            params["lon"] = longitude.toString()
            params["appid"] = Constants.APP_ID

            apiViewModel.fetchCurrentWeather(params).observe(this) {
                if (it != null) {
                    Log.d(tag, "currentWeather: ${Gson().toJson(it)}")
                    viewModelCurrent.deleteAllCurrentDetails()
                    insertCurrentToRoom(it)
                }
            }
        } catch (e: Exception) {
            Log.e(tag, "getCurrentWeather", e)
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
            Log.e(tag, "insertCurrentToRoom", e)
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
            Log.e(tag, "loadCurrentFromRoom", e)
            listOf()
        }
    }

    fun getWeatherForecast(latitude: Double, longitude: Double) {
        try {
            val params: MutableMap<String, String> = HashMap()
            params["lat"] = latitude.toString()
            params["lon"] = longitude.toString()
            params["appid"] = Constants.APP_ID

            apiViewModel.fetchForecastWeather(params).observe(this) {
                if (it != null) {
                    Log.d(tag, "forecastWeather: ${Gson().toJson(it)}")
                    viewModelForecast.deleteAllForecastDetails()
                    insertForecastToRoom(it)
                }
            }
        } catch (e: Exception) {
            Log.e(tag, "getWeatherForecast", e)
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
            Log.e(tag, "insertForecastToRoom", e)
        }
    }

    fun checkLocationPermissionStatus() {
        try {
            when {
                ContextCompat.checkSelfPermission(
                    this, Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                    this, Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED -> {
                    when {
                        ActivityCompat.shouldShowRequestPermissionRationale(
                            this, Manifest.permission.ACCESS_FINE_LOCATION
                        ) && ActivityCompat.shouldShowRequestPermissionRationale(
                            this, Manifest.permission.ACCESS_COARSE_LOCATION
                        ) -> {
                            // Show an explanation to the user *asynchronously* -- don't block
                            // this thread waiting for the user's response! After the user
                            ActivityCompat.requestPermissions(
                                this,
                                arrayOf(
                                    Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.ACCESS_COARSE_LOCATION
                                ),
                                locationRequestCode
                            )
                        }

                        else -> {
                            // We can request the permission.
                            ActivityCompat.requestPermissions(
                                this, arrayOf(
                                    Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.ACCESS_COARSE_LOCATION
                                ),
                                locationRequestCode
                            )
                        }
                    }
                }
                else -> {
                    // Permission previously granted
                    getCurrentKnownLocation()
                }
            }
        } catch (e: Exception) {
            Log.e(tag, "checkLocationPermissionStatus", e)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            locationRequestCode -> if (grantResults.isNotEmpty()
                && grantResults[0] == PackageManager.PERMISSION_GRANTED
            ) {
                // permission was granted, do location-related task you need to do.
                when (PackageManager.PERMISSION_GRANTED) {
                    ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) -> {
                        getCurrentKnownLocation()
                    }
                }
            }

            else {
                // permission denied! Disable the functionality that depends on this permission.
                exitProcess(0)
            }
        }
    }

    private fun getCurrentKnownLocation() {
        try {
            locationUtil.getCurrentKnownLocation(
                fusedLocationProviderClient
            )
        } catch (e: Exception) {
            Log.e(tag, "getCurrentKnownLocation", e)
        }
    }
}