package com.example.tempomobileapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.launch
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.example.tempomobileapp.ui.theme.TempoMobileAppTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class WaitActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        installSplashScreen()
        setContent {
            TempoMobileAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    WaitLayout(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }

        // Launch coroutine to navigate after delay
        lifecycleScope.launch {
            delay(30000) // Wait for 30 seconds
            startActivity(Intent(this@WaitActivity, LoginActivity::class.java))
            finish() // Finish WaitActivity
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview3() {
    TempoMobileAppTheme {
        //Greeting2("Android")
    }
}