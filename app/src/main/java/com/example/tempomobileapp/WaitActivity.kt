package com.example.tempomobileapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.example.tempomobileapp.ui.theme.tempoMobileAppTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * WaitActivity is the activity that is displayed when the app is launched.
 * This activity is used during a few seconds, to ensure the API is activated
 */
class WaitActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        installSplashScreen()
        setContent {
            tempoMobileAppTheme {
                waitLayout()
            }
        }

        // Launch coroutine to navigate after delay -- temporary
        lifecycleScope.launch {
            delay(30000)
            startActivity(Intent(this@WaitActivity, LoginActivity::class.java))
            finish()
        }
    }
}
