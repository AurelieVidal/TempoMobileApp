package com.example.tempomobileapp.signin

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tempomobileapp.ErrorActivity
import com.example.tempomobileapp.LoginActivity
import com.example.tempomobileapp.R
import com.example.tempomobileapp.adapters.TempoApiService
import com.example.tempomobileapp.exceptions.ApiException
import com.example.tempomobileapp.models.SecurityQuestion
import com.example.tempomobileapp.ui.theme.tempoMobileAppTheme
import kotlinx.coroutines.launch

/**
 * SignInActivity is the main activity for the app.
 * This activity is activated when the user want to sign in to the app, from log in screen.
 */
class SignInActivity(
    private val securityQuestionsProvider: suspend () -> List<SecurityQuestion> = {
        TempoApiService.getInstance().getSecurityQuestions()
    }
) : ComponentActivity() {

    /**
     * DependencyInjector is used to provide dependencies for `SignInActivity`.
     * The `securityQuestionsProvider` allows overriding the default security questions implementation,
     * which can be useful for testing or specific configurations.
     */
    object DependencyInjector {
        var securityQuestionsProvider: (suspend () -> List<SecurityQuestion>)? = null
    }

    /**
     * Companion object for utility functions related to SignInActivity.
     * Creates an intent for launching SignInActivity with a specific security questions provider.
     * For test purposes.
     */
    companion object {
        fun createIntent(
            context: Context,
            securityQuestionsProvider: suspend () -> List<SecurityQuestion>
        ): Intent {
            val intent = Intent(context, SignInActivity::class.java)
            DependencyInjector.securityQuestionsProvider = securityQuestionsProvider
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val injectedSecurityQuestionsProvider =
            DependencyInjector.securityQuestionsProvider ?: securityQuestionsProvider

        val securityQuestionsState = mutableStateOf<List<SecurityQuestion>>(emptyList())
        val isLoading = mutableStateOf(true)

        lifecycleScope.launch {
            try {
                val questions = injectedSecurityQuestionsProvider()
                Log.d("App", "Questions récupérées : $questions")
                securityQuestionsState.value = questions
            } catch (e: ApiException) {
                Log.e("App", "Erreur lors de la récupération des questions", e)
                showError()
            } finally {
                isLoading.value = false
            }
        }

        setContent {
            tempoMobileAppTheme {
                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = "signin") {
                    composable(
                        route = "signin",
                    ) {
                        signInLayout(
                            securityQuestions = securityQuestionsState.value,
                            navController
                        )
                    }
                    composable(
                        route = "login"
                    ) {
                        val context = LocalContext.current
                        LaunchedEffect(Unit) {
                            val intent = Intent(context, LoginActivity::class.java)
                            val options = ActivityOptionsCompat.makeCustomAnimation(
                                context,
                                R.anim.slide_in_bottom,
                                R.anim.slide_out_top
                            )
                            context.startActivity(intent, options.toBundle())
                            (context as? ComponentActivity)?.finish()
                        }
                    }
                }
            }
        }
    }

    private fun showError() {
        startActivity(Intent(this, ErrorActivity::class.java))
        finish()
    }

    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        resetSignInStates()
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}
