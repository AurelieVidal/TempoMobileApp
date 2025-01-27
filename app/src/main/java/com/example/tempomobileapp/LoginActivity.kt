package com.example.tempomobileapp

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
import com.example.tempomobileapp.signin.SignInActivity
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

                        // Démarrer une nouvelle instance de SignInActivity
                        val intent = Intent(context, SignInActivity::class.java).apply {
                            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                        }
                        context.startActivity(intent)

                        // Terminer l'activité actuelle pour éviter des instances superposées
                        (context as? ComponentActivity)?.finish()
                    }
                }
            }
        }
    }
}
