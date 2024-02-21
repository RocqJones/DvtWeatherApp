package com.rocqjones.dvt.weatherapp.ui.design.screens

import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.rocqjones.dvt.weatherapp.R
import com.rocqjones.dvt.weatherapp.logic.models.DataFactory
import com.rocqjones.dvt.weatherapp.logic.models.PlacesModel
import com.rocqjones.dvt.weatherapp.logic.models.sealed.Screen
import com.rocqjones.dvt.weatherapp.logic.providers.PlacesProvider

@Composable
fun SearchPlacesScreen(
    navController: NavHostController,
    placesProvider: PlacesProvider
) {
    val tag = "PlacesScreen|MAP"
    val context = LocalContext.current

    // LiveData to track initialization status
    val isInitialized = remember { mutableStateOf(false) }

    DisposableEffect(Unit) {
        if (!placesProvider.isInitialized()) {
            placesProvider.initialize(context, context.getString(R.string.api_key))
        }

        isInitialized.value = placesProvider.isInitialized()

        onDispose {
            // Shutdown Places SDK resources
            placesProvider.deInitialize()
        }
    }

    // Display after initialization is complete
    if (isInitialized.value) {
        PlacesContent(tag, context, navController)
    }
}

@Composable
fun PlacesContent(tag: String, context: Context, navController: NavHostController) {

    val intentLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { it ->
        try {
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

                        Toast.makeText(context, "${place.name} added successfully", Toast.LENGTH_SHORT).show()

                        // Move to the next screen
                        navController.navigate(Screen.FavouritePlacesScreen.route)
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
        }  catch (e: Exception) {
            Log.e(tag, "intentLauncher Error", e)
        }
    }

    val launchMapInputOverlay = {
        val fields = listOf(
            Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG
        )

        // Show Google Search Dialog
        val intent = Autocomplete.IntentBuilder(
            AutocompleteActivityMode.OVERLAY, fields
        ).build(context)
        intentLauncher.launch(intent)
    }

    try {

    } catch (e: Exception) {
        Log.e(tag, "launchMapInputOverlay Error", e)
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = launchMapInputOverlay
        ) {
            Text("Tap here to search Location...")
        }
    }
}
