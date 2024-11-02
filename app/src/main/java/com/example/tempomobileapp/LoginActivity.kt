package com.example.tempomobileapp

import LoginLayout
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.tempomobileapp.ui.theme.TempoMobileAppTheme
import androidx.compose.material3.Button
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController


class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            TempoMobileAppTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "login") {
                    composable(
                        route = "login",
                        /*enterTransition = { slideInHorizontally(initialOffsetX = { -300 }) }, // Slide in from left
                        exitTransition = { slideOutHorizontally(targetOffsetX = { 300 }) }*/ // Slide out to right
                    ) { LoginLayout(navController) }
                    composable(
                        route = "home"
                        /*enterTransition = { fadeIn() }, // Fade in
                        exitTransition = { fadeOut() }*/ // Fade out
                    ) {
                        Greeting(name = "Welcome to Home")
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
    TempoMobileAppTheme {
        val navController = rememberNavController()
        NavHost(navController = navController, startDestination = "login") {
            composable("login") { LoginLayout(navController) } // Pass navController
            composable("home") {
                Greeting(name = "Welcome to Home")
            }
        } // Content of HomePageActivity
        /*Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            LoginLayout()
        }*/
    }
}