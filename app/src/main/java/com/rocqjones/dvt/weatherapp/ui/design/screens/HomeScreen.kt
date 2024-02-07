package com.rocqjones.dvt.weatherapp.ui.design.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.rocqjones.dvt.weatherapp.configs.BaseAppConfig
import com.rocqjones.dvt.weatherapp.logic.models.entities.CurrentWeatherModel
import com.rocqjones.dvt.weatherapp.logic.models.entities.ForecastWeatherModel
import com.rocqjones.dvt.weatherapp.logic.utils.DataFormatUtil
import com.rocqjones.dvt.weatherapp.logic.vm.CurrentViewModelFactory
import com.rocqjones.dvt.weatherapp.logic.vm.ForecastViewModelFactory
import com.rocqjones.dvt.weatherapp.logic.vm.ViewModelCurrent
import com.rocqjones.dvt.weatherapp.logic.vm.ViewModelForecast

@Composable
fun HomeScreen() {
    val viewModelCurrent: ViewModelCurrent = viewModel(
        factory = CurrentViewModelFactory(
            (LocalContext.current.applicationContext as BaseAppConfig).currentRepository
        )
    )

    val viewModelForecast: ViewModelForecast = viewModel(
        factory = ForecastViewModelFactory(
            (LocalContext.current.applicationContext as BaseAppConfig).forecastRepository
        )
    )

    // Observe the LiveData and convert it to a state
    val dataCurrent by viewModelCurrent.getAllCurrentWeather.observeAsState(initial = emptyList())
    val dataForecast by viewModelForecast.getAllForecastWeather.observeAsState(initial = emptyList())

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        CurrentContentView(dataCurrent, modifier = Modifier.weight(1f))
        CurrentTempContentView(dataCurrent, modifier = Modifier.weight(1f))
        ForecastContentView(dataForecast, modifier = Modifier.weight(1f))
    }
}

@Composable
fun CurrentContentView(
    data: List<CurrentWeatherModel>,
    modifier: Modifier
) {
    if (data.toMutableList().isNotEmpty()) {
        val it = data[0]
        Log.d("loadCurrentObj", "$it")
        // Bg Drawable
        val bgDrawable : Int by remember {
            mutableStateOf(DataFormatUtil.getBgDrawable(it.weatherMain))
        }

        Box(
            modifier = modifier.fillMaxSize().paint(
                painterResource(id = bgDrawable),
                contentScale = ContentScale.FillBounds
            ),
            contentAlignment = Alignment.Center
        ) {

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val tempC = "${DataFormatUtil.convertKelvinToCelsius(it.temperature ?: 0.0)}℃"
                val description = (it.weatherMain ?: "").uppercase()

                Text(
                    text = "Nairobi Ke",
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.White,
                )

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

@Composable
fun CurrentTempContentView(
    dataCurrent: List<CurrentWeatherModel>,
    modifier: Modifier
) {
    if (dataCurrent.toMutableList().isNotEmpty()) {
        val it = dataCurrent[0]
        Log.d("loadCurrentObj", "$it")

        // Bg color
        val bgColor : Color by remember { mutableStateOf(DataFormatUtil.getBgColor(it.weatherMain)) }

        Column(
            modifier = modifier.background(bgColor)
        ) {
            // Temp
            val minTemp = "${DataFormatUtil.convertKelvinToCelsius(it.temp_min ?: 0.0)}℃"
            val currentTemp = "${DataFormatUtil.convertKelvinToCelsius(it.temperature ?: 0.0)}℃"
            val maxTemp = "${DataFormatUtil.convertKelvinToCelsius(it.temp_max ?: 0.0)}℃"

            Spacer(modifier = Modifier.padding(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = minTemp,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )

                Text(
                    text = currentTemp,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )

                Text(
                    text = maxTemp,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.padding(2.dp))

            // Desc
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Min",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "Current",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "Max",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.padding(4.dp))
            Divider()
        }
    }
}

@Composable
fun ForecastContentView(
    dataForecast: List<ForecastWeatherModel>,
    modifier: Modifier
) {
}