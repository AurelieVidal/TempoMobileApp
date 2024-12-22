package com.example.tempomobileapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.example.tempomobileapp.adapters.TempoApiService
import com.example.tempomobileapp.ui.theme.tempoMobileAppTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Response

/**
 * WaitActivity is the activity that is displayed when the app is launched.
 * This activity is used during a few seconds, to ensure the API is activated
 */

class WaitActivity(private val healthCheckProvider: suspend () -> Boolean = { TempoApiService.getInstance().checkHealth() }) : ComponentActivity() {

    object DependencyInjector {
        var healthCheckProvider: (suspend () -> Boolean)? = null
    }

    companion object {
        fun createIntent(context: Context, healthCheckProvider: suspend () -> Boolean): Intent {
            // Utilisation d'une clé statique ou injection d'une dépendance spécifique
            val intent = Intent(context, WaitActivity::class.java)
            DependencyInjector.healthCheckProvider = healthCheckProvider
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        installSplashScreen()
        setContent {
            tempoMobileAppTheme {
                waitLayout()
            }
        }

        // Utiliser le provider injecté s'il existe
        val injectedHealthCheckProvider = DependencyInjector.healthCheckProvider ?: healthCheckProvider

        lifecycleScope.launch {
            val startTime = System.currentTimeMillis()
            val isHealthy = injectedHealthCheckProvider() // Appeler la dépendance injectée
            val elapsedTime = System.currentTimeMillis() - startTime

            if (isHealthy) {
                delayIfNeeded(elapsedTime)
                navigateToLogin()
            } else {
                showError()
            }
        }
    }

    /**
     * Delay execution if needed to meet the 8-second minimum wait time.
     * @param elapsedTime Time already spent waiting for the API response in milliseconds.
     */
    private suspend fun delayIfNeeded(elapsedTime: Long) {
        val minimumWaitTime = 8000L // 8 seconds in milliseconds
        val remainingTime = minimumWaitTime - elapsedTime
        if (remainingTime > 0) {
            delay(remainingTime)
        }
    }

    /**
     * Navigate to the LoginActivity.
     */
    private fun navigateToLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    /**
     * Show an error message if the API is not ready.
     */
    private fun showError() {
        // You can customize this behavior (e.g., show a dialog or retry button).
        Log.d("App", "error func")
        startActivity(Intent(this, ErrorActivity::class.java))
        finish()
    }
}
