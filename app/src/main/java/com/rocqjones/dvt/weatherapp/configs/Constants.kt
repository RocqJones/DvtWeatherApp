package com.rocqjones.dvt.weatherapp.configs

import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource

object Constants {
    const val APP_ID = "54eed0cc270401f0c1d374a8f1f6301f"
    const val BASE_URL = "https://api.openweathermap.org/data/2.5/"

    // API Endpoints
    const val weather_endpoint = "weather"
    const val forecast_endpoint = "forecast"

    /**
     * Current Location params
     * Priority.PRIORITY_HIGH_ACCURACY - required for higher accuracy
     * CancellationTokenSource() - Activity has been closed by user
     */
    const val priority = Priority.PRIORITY_HIGH_ACCURACY
    val cancellationTokenSource = CancellationTokenSource()

    // NRB
    const val defaultLat  = -1.28638
    const val defaultLong  = 36.817223
}