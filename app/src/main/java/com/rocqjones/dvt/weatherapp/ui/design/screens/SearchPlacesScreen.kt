package com.rocqjones.dvt.weatherapp.ui.design.screens

import android.app.Activity
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.gson.Gson
import com.rocqjones.dvt.weatherapp.R
import com.rocqjones.dvt.weatherapp.configs.BaseAppConfig
import com.rocqjones.dvt.weatherapp.configs.Constants
import com.rocqjones.dvt.weatherapp.logic.models.DataFactory
import com.rocqjones.dvt.weatherapp.logic.models.PlacesModel
import com.rocqjones.dvt.weatherapp.logic.models.entities.FavouriteWeatherModel
import com.rocqjones.dvt.weatherapp.logic.models.sealed.Screen
import com.rocqjones.dvt.weatherapp.logic.network.repo.ApiRepository
import com.rocqjones.dvt.weatherapp.logic.vm.FavouriteViewModelFactory
import com.rocqjones.dvt.weatherapp.logic.vm.ViewModelFavourite
import com.rocqjones.dvt.weatherapp.logic.vm.WeatherApiViewModel
import com.rocqjones.dvt.weatherapp.logic.vm.WeatherApiViewModelFactory

@Composable
fun SearchPlacesScreen(navController: NavHostController) {
    val context = LocalContext.current
    val tag = "PlacesScreen|MAP"

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

    val intentLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { it ->
        when (it.resultCode) {

            Activity.RESULT_OK -> {
                it.data?.let {
                    val place = Autocomplete.getPlaceFromIntent(it)
                    Log.i(tag, "Place: ${place.name}, ${place.latLng}, ${place.id}")
                    DataFactory.setPlaceModel(
                        PlacesModel(
                            id = place.id,
                            placeName = place.name,
                            placeLatLong = place.latLng
                        )
                    )
                }
            }

            AutocompleteActivity.RESULT_ERROR -> {
                it.data?.let {
                    val status = Autocomplete.getStatusFromIntent(it)
                    DataFactory.setPlaceModel(null)
                    Log.i(tag, "Place Error status: ${status.status}, Msg ${status.statusMessage}")
                }
            }

            Activity.RESULT_CANCELED -> {
                // The user canceled the operation.
                DataFactory.setPlaceModel(null)
            }
        }
    }

    val launchMapInputOverlay = {
        // init
        Places.initialize(context, context.getString(R.string.api_key))
        val fields = listOf(
            Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG
        )

        // Show Google Search Dialog
        val intent = Autocomplete.IntentBuilder(
            AutocompleteActivityMode.OVERLAY, fields
        ).build(context)
        intentLauncher.launch(intent)

        // Move this code to the next screen
        when {
            DataFactory.getPlaceModel() != null -> {
                // Get Location Details from API and got to maps
                val params: MutableMap<String, String> = HashMap()
                params["lat"] = DataFactory.getPlaceModel()?.placeLatLong?.latitude.toString()
                params["lon"] = DataFactory.getPlaceModel()?.placeLatLong?.longitude.toString()
                params["appid"] = Constants.APP_ID
                apiViewModel.fetchCurrentWeather(params).observe(context as LifecycleOwner) {
                    if (it != null) {
                        Log.d(tag, "currentWeather: ${Gson().toJson(it)}")
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

                        navController.navigate(Screen.HomeScreen.route)
                    }
                }
            }

            else -> {
                navController.navigate(Screen.HomeScreen.route)
            }
        }
    }

    DisposableEffect(Unit) {
        // Cleanup code when the composable is removed from the composition
        onDispose {
            Places.deinitialize()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = launchMapInputOverlay,
        ) {
            Text("Tap here to search Location...")
        }
    }
}