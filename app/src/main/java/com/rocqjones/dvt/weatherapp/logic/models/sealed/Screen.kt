package com.rocqjones.dvt.weatherapp.logic.models.sealed

/**
 * The Type-Safe sealed Screen here represents a closed set of screen
 * states which is useful for handling navigation in our app.
 */
sealed class Screen(val route: String) {
    object HomeScreen : Screen("homeScreen")
    object SearchPlacesScreen : Screen("searchPlacesScreen")
}