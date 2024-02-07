package com.rocqjones.dvt.weatherapp.ui.design.screens

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.rocqjones.dvt.weatherapp.R
import com.rocqjones.dvt.weatherapp.configs.BaseAppConfig
import com.rocqjones.dvt.weatherapp.logic.utils.DataFormatUtil
import com.rocqjones.dvt.weatherapp.logic.vm.CurrentViewModelFactory
import com.rocqjones.dvt.weatherapp.logic.vm.ViewModelCurrent

@Composable
fun HomeScreen() {
    Column {
        CurrentContentView()
        //ForecastContentView()
    }
}

@Composable
fun CurrentContentView() {
    val viewModelCurrent: ViewModelCurrent = viewModel(
        factory = CurrentViewModelFactory(
            (LocalContext.current.applicationContext as BaseAppConfig).currentRepository
        )
    )

    // Observe the LiveData and convert it to a state
    val data by viewModelCurrent.getAllCurrentWeather.observeAsState(initial = emptyList())

    Box(
        modifier = Modifier.fillMaxSize().paint(
            painterResource(id = R.drawable.forest_sunny),
            contentScale = ContentScale.FillBounds
        ),
        contentAlignment = Alignment.Center
    ) {
        if (data.toMutableList().isNotEmpty()) {
            val it = data[0]
            Log.d("loadCurrentObj", "$it")

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val tempC = "${DataFormatUtil.convertKelvinToCelsius(it.temperature ?: 0.0)}℃"
                val description = it.weatherMain ?: ""
                Text(
                    text = tempC,
                    style = MaterialTheme.typography.headlineLarge,
                    color = Color.White,
                )

                Text(
                    text = description,
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White,
                )

            }
        }
    }
}

/*
@Composable
fun ForecastContentView() {
}*/
