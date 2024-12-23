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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * WaitActivity is the activity that is displayed when the app is launched.
 * This activity is used during a few seconds, to ensure the API is activated
 */
class WaitActivity(
    private val healthCheckProvider: suspend () -> Boolean = {
        TempoApiService.getInstance().checkHealth()
    }
) : ComponentActivity() {

    /**
     * DependencyInjector is used to provide dependencies for `WaitActivity`.
     * The `healthCheckProvider` allows overriding the default health check implementation,
     * which can be useful for testing or specific configurations.
     */
    object DependencyInjector {
        var healthCheckProvider: (suspend () -> Boolean)? = null
    }

    /**
     * Companion object for utility functions related to WaitActivity.
     * Creates an intent for launching WaitActivity with a specific health check provider.
     */
    companion object {
        fun createIntent(context: Context, healthCheckProvider: suspend () -> Boolean): Intent {
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

        val injectedHealthCheckProvider =
            DependencyInjector.healthCheckProvider ?: healthCheckProvider

        lifecycleScope.launch {
            val startTime = System.currentTimeMillis()
            val isHealthy = injectedHealthCheckProvider()
            val elapsedTime = System.currentTimeMillis() - startTime

            if (isHealthy) {
                delayIfNeeded(elapsedTime)
                navigateToLogin()
            } else {
                showError()
            }
        }
    }

    private suspend fun delayIfNeeded(elapsedTime: Long) {
        val minimumWaitTime = 8000L
        val remainingTime = minimumWaitTime - elapsedTime
        if (remainingTime > 0) {
            delay(remainingTime)
        }
    }

    private fun navigateToLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    private fun showError() {
        Log.d("App", "error func")
        startActivity(Intent(this, ErrorActivity::class.java))
        finish()
    }
}
