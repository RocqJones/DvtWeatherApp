package com.rocqjones.dvt.weatherapp.ui.design

import android.app.Activity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.rocqjones.dvt.weatherapp.ui.design.base.BaseActivity
import com.rocqjones.dvt.weatherapp.ui.design.screens.HomeScreen
import com.rocqjones.dvt.weatherapp.ui.theme.DvtWeatherAppTheme

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            DvtWeatherAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    HomeScreen()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        getCurrentWeather()
        getWeatherForecast()
    }

    override fun activityContext(): Activity {
        return this
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    DvtWeatherAppTheme {
        HomeScreen()
    }
}