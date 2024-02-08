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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.rocqjones.dvt.weatherapp.logic.models.sealed.Screen
import com.rocqjones.dvt.weatherapp.ui.design.base.BaseActivity
import com.rocqjones.dvt.weatherapp.ui.design.screens.FavouritePlacesScreen
import com.rocqjones.dvt.weatherapp.ui.design.screens.HomeScreen
import com.rocqjones.dvt.weatherapp.ui.design.screens.SearchPlacesScreen
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
                    MyAppMain()
                }
            }
        }
    }

    override fun activityContext(): Activity {
        return this
    }
}

@Composable
fun MyAppMain() {
    val navController = rememberNavController()

    NavHost(
        navController, startDestination = Screen.HomeScreen.route
    ) {
        composable(Screen.HomeScreen.route) {
            HomeScreen(navController)
        }
        composable(Screen.SearchPlacesScreen.route) {
            SearchPlacesScreen(navController)
        }
        composable(Screen.FavouritePlacesScreen.route) {
            FavouritePlacesScreen(navController)
        }
    }
}

@Preview(
    showBackground = true,
    widthDp = 250,
    heightDp = 450
)
@Composable
fun GreetingPreview() {
    DvtWeatherAppTheme {
        MyAppMain()
    }
}