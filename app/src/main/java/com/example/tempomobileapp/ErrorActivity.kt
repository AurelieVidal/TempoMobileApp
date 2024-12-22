package com.example.tempomobileapp

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.tempomobileapp.ui.theme.tempoMobileAppTheme
import errorLayout
import kotlin.system.exitProcess

/**
 * ErrorActivity is the main activity for the app.
 * This activity is activated when the user log in and contains access to all components of the app.
 */
class ErrorActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("App", "error activity")
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            tempoMobileAppTheme {
                errorLayout()
            }
        }
    }

    /**
     * Companion object for holding shared static properties or methods
     * related to ErrorActivity. The `isTesting` variable is used to adjust
     * behavior during testing (e.g., avoid exiting the app completely).
     */
    companion object {
        var isTesting = false
    }

    fun closeApplication(activity: Activity) {
        Log.d("App", "Closing application")
        if (!isTesting) {
            activity.finishAffinity()
            exitProcess(0)
        } else {
            activity.finish()
        }
    }
}
