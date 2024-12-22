package com.example.tempomobileapp

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.example.tempomobileapp.ui.theme.tempoMobileAppTheme
import errorLayout
import kotlin.system.exitProcess

/**
 * HomeActivity is the main activity for the app.
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

    companion object {
        var isTesting = false
    }

    // Méthode pour fermer l'application
    fun closeApplication(activity: Activity) {
        Log.d("App", "Closing application")
        if (!isTesting) {
            activity.finishAffinity() // Ferme toutes les activités
            exitProcess(0)  // Termine le processus
        } else {
            activity.finish() // Ferme uniquement l'activité pour les tests
        }
    }
}

