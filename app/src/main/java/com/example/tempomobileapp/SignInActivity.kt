package com.example.tempomobileapp

import SignInLayout
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tempomobileapp.adapters.TempoApiService
import com.example.tempomobileapp.models.SecurityQuestion
import com.example.tempomobileapp.ui.theme.tempoMobileAppTheme
import kotlinx.coroutines.launch

/**
 * HomeActivity is the main activity for the app.
 * This activity is activated when the user log in and contains access to all components of the app.
 */
class SignInActivity(
    private val securityQuestionsProvider: suspend () -> List<SecurityQuestion> = {
        TempoApiService.getInstance().getSecurityQuestions()
    }
) : ComponentActivity() {

    object DependencyInjector {
        var securityQuestionsProvider: (suspend () -> List<SecurityQuestion>)? = null
    }

    companion object {
        fun createIntent(context: Context, securityQuestionsProvider: suspend () -> List<SecurityQuestion>): Intent {
            val intent = Intent(context, SignInActivity::class.java)
            DependencyInjector.securityQuestionsProvider = securityQuestionsProvider
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("App", "onCreate appelé")
        enableEdgeToEdge()

        val injectedSecurityQuestionsProvider =
            DependencyInjector.securityQuestionsProvider ?: securityQuestionsProvider

        // Mutable states for loading and questions
        val securityQuestionsState = mutableStateOf<List<SecurityQuestion>>(emptyList())
        val isLoading = mutableStateOf(true)

        lifecycleScope.launch {
            try {
                val questions = injectedSecurityQuestionsProvider()
                Log.d("App", "Questions récupérées : $questions")
                securityQuestionsState.value = questions
            } catch (e: Exception) {
                Log.e("App", "Erreur lors de la récupération des questions", e)
                showError()
            } finally {
                isLoading.value = false

            }
        }

        setContent {
            Log.d("App", "Composition de l'interface")
            tempoMobileAppTheme {

                    // Affichage du SignInLayout après le chargement




                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "signin") {
                    composable(
                        route = "signin",
                    ) {

                        SignInLayout(securityQuestions = securityQuestionsState.value, navController)
                    }
                    composable(
                        route = "login"
                    ) {
                        val context = LocalContext.current
                        LaunchedEffect(Unit) {
                            val intent = Intent(context, LoginActivity::class.java)
                            val options = ActivityOptionsCompat.makeCustomAnimation(
                                context,
                                R.anim.slide_in_bottom, // Animation d'entrée
                                R.anim.slide_out_top  // Animation de sortie
                            )
                            context.startActivity(intent, options.toBundle())
                            (context as? ComponentActivity)?.finish() // Termine LoginActivity



                        }
                    }

                }

            }
        }


    }

    private fun showError() {
        Log.d("App", "error func")
        startActivity(Intent(this, ErrorActivity::class.java))
        finish()
    }

    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        Log.d("App", "Bouton retour appuyé")

        // Naviguer vers LoginActivity
        val intent = Intent(this, LoginActivity::class.java)
        //intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)

        // Optionnel : Termine l'activité actuelle
        finish()
    }


}