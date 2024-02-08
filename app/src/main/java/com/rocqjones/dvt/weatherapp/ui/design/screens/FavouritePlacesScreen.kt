package com.rocqjones.dvt.weatherapp.ui.design.screens

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.rocqjones.dvt.weatherapp.configs.BaseAppConfig
import com.rocqjones.dvt.weatherapp.configs.Constants
import com.rocqjones.dvt.weatherapp.logic.models.DataFactory
import com.rocqjones.dvt.weatherapp.logic.models.entities.FavouriteWeatherModel
import com.rocqjones.dvt.weatherapp.logic.network.repo.ApiRepository
import com.rocqjones.dvt.weatherapp.logic.vm.FavouriteViewModelFactory
import com.rocqjones.dvt.weatherapp.logic.vm.ViewModelFavourite
import com.rocqjones.dvt.weatherapp.logic.vm.WeatherApiViewModel
import com.rocqjones.dvt.weatherapp.logic.vm.WeatherApiViewModelFactory

@Composable
fun FavouritePlacesScreen(navController: NavHostController) {

    val tag = "FavouritePlacesScreen"

    val context = LocalContext.current

    // VM
    val apiViewModel: WeatherApiViewModel = viewModel(
        factory = WeatherApiViewModelFactory(
            ApiRepository()
        )
    )

    val viewModelFavourite: ViewModelFavourite = viewModel(
        factory = FavouriteViewModelFactory(
            (context.applicationContext as BaseAppConfig).favouriteRepository
        )
    )

    // Check for pending cached favourite Locations
    when {
        DataFactory.getPlaceModel() != null -> {
            Log.d(tag, "getPlaceModel: ${Gson().toJson(DataFactory.getPlaceModel())}")
            // Get Location Details from API and got to maps
            val params: MutableMap<String, String> = HashMap()
            params["lat"] = DataFactory.getPlaceModel()?.placeLatLong?.latitude.toString()
            params["lon"] = DataFactory.getPlaceModel()?.placeLatLong?.longitude.toString()
            params["appid"] = Constants.APP_ID
            apiViewModel.fetchCurrentWeather(params).observe(context as LifecycleOwner) {
                if (it != null) {
                    Log.d(tag, "favouriteWeather: ${Gson().toJson(it)}")
                    viewModelFavourite.insert(
                        FavouriteWeatherModel(
                            null,
                            DataFactory.getPlaceModel()?.id.toString(),
                            it.name,
                            it.userCoordinates?.latitude,
                            it.userCoordinates?.longitude,
                            it.mainDetails?.temp_current,
                            it.mainDetails?.temp_min,
                            it.mainDetails?.temp_max,
                            it.weatherDetails?.get(0)?.main,
                            it.weatherDetails?.get(0)?.description
                        )
                    )
                }
            }
        }

        else -> {
            // None: Display existing

        }
    }

    SetupMapContentView()
}

@Composable
fun SetupMapContentView() {
    // Maps states
    var uiSettings by remember { mutableStateOf(MapUiSettings()) }
    val properties by remember {
        mutableStateOf(MapProperties(mapType = MapType.NORMAL))
    }
    val defaultLocation = LatLng(Constants.defaultLat, Constants.defaultLong)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(defaultLocation, 10f)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.matchParentSize(),
            properties = properties,
            uiSettings = uiSettings,
            cameraPositionState = cameraPositionState
        ) {
            Marker(
                state = MarkerState(position = defaultLocation),
                title = "Nairobi",
                snippet = "Marker in Nairobi Kenya"
            )
        }

        // Enable/Disable settings
        Switch(
            checked = uiSettings.zoomControlsEnabled,
            onCheckedChange = {
                uiSettings = uiSettings.copy(zoomControlsEnabled = it)
            }
        )
    }
}
