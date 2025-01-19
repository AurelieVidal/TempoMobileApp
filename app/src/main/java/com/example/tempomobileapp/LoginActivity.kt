package com.example.tempomobileapp

import SignInLayout
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tempomobileapp.ui.theme.tempoMobileAppTheme
import loginLayout

/**
 * LogInActivity is the first Activity of the app.
 * This activity is used for the user to log in
 */
class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            tempoMobileAppTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "login") {
                    composable(
                        route = "login",
                    ) { loginLayout(navController) }
                    composable(
                        route = "home"
                    ) {
                        greeting(name = "Welcome to Home")
                    }
                    composable(
                        route = "signin"
                    ) {
                        val context = LocalContext.current

                        // Utilisation d'un effet unique pour lancer l'activité
                        LaunchedEffect(Unit) {
                            context.startActivity(Intent(context, SignInActivity::class.java))
                            (context as? ComponentActivity)?.finish() // Termine LoginActivity
                        }
                    }

                }
            }
        }
    }
}
