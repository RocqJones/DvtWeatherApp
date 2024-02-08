package com.rocqjones.dvt.weatherapp.logic.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.location.Location
import android.location.LocationListener
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.google.android.gms.location.FusedLocationProviderClient
import com.rocqjones.dvt.weatherapp.configs.Constants
import com.rocqjones.dvt.weatherapp.logic.listeners.LocationModelListener
import com.rocqjones.dvt.weatherapp.logic.models.LocationModel

class LocationUtil(
    private val activityContext: Activity,
    private val listener : LocationModelListener?
) : LocationListener {

    var tag = "LocationUtil"

    @SuppressLint("MissingPermission")
    fun getCurrentKnownLocation(fusedLocationProviderClient: FusedLocationProviderClient) {
        try {
            fusedLocationProviderClient.getCurrentLocation(
                Constants.priority,
                Constants.cancellationTokenSource.token
            ).addOnSuccessListener { location ->
                when {
                    location != null -> {
                        // Logic to handle location object
                        Log.d(tag, "getCurrentLocation latLog: ${location.latitude}, ${location.longitude}")
                        Handler(Looper.getMainLooper()).post {
                            listener?.onResponse(
                                LocationModel(
                                    latitude = location.latitude,
                                    longitude = location.longitude
                                )
                            )
                        }
                    }

                    else -> {
                        Log.d(tag, "getCurrentLocation: Returned Null")
                        Handler(Looper.getMainLooper()).post { listener?.onResponse(null) }
                    }
                }
            }.addOnFailureListener { exception ->
                Log.e(tag, "getCurrentLocation Exception: ", exception)
                // is location - GPS services enabled
                Handler(Looper.getMainLooper()).post { listener?.onResponse(null) }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onLocationChanged(p0: Location) {
        try {
            Log.e(tag, "onLocationChanged latitude: ${p0.latitude}, longitude: ${p0.longitude}")
            Handler(Looper.getMainLooper()).post {
                listener?.onResponse(
                    LocationModel(
                        latitude = p0.latitude,
                        longitude = p0.longitude
                    )
                )
            }
        } catch (e: Exception) {
            Log.e(tag, "onLocationChanged Error", e)
            Handler(Looper.getMainLooper()).post { listener?.onResponse(null) }
        }
    }
}