package com.rocqjones.dvt.weatherapp.logic.utils

import android.app.Activity
import android.view.View
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.rocqjones.dvt.weatherapp.R

class ToastUtil(activity : Activity) {

    private lateinit var snackBar: Snackbar
    private var activityContext: Activity

    init {
        this.activityContext = activity
    }

    fun showSnackBar(message: String) {
        try {
            val contentView = activityContext.findViewById<View>(android.R.id.content)
            snackBar = Snackbar.make(
                contentView, message, Snackbar.LENGTH_SHORT
            ).setBackgroundTint(
                ContextCompat.getColor(activityContext, R.color.blue)
            )
            snackBar.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun showSnackBarError(message: String) {
        try {
            val contentView = activityContext.findViewById<View>(android.R.id.content)
            snackBar = Snackbar.make(
                contentView, message, Snackbar.LENGTH_LONG
            ).setBackgroundTint(
                ContextCompat.getColor(activityContext, R.color.red)
            )
            snackBar.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}