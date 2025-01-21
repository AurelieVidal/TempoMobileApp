package com.example.tempomobileapp.signin

import android.content.Context
import android.provider.Settings
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.tempomobileapp.R
import com.example.tempomobileapp.adapters.TempoApiService
import com.example.tempomobileapp.adapters.UserCreate
import com.example.tempomobileapp.enums.Country
import com.example.tempomobileapp.enums.countries
import com.example.tempomobileapp.models.SecurityQuestion
import com.example.tempomobileapp.signin.components.bottomDecoration
import com.example.tempomobileapp.signin.components.midTexts
import com.example.tempomobileapp.signin.components.successDialog
import com.example.tempomobileapp.signin.components.topDecoration
import com.example.tempomobileapp.signin.components.topTexts
import com.example.tempomobileapp.signin.components.userErrorText
import com.example.tempomobileapp.signin.components.valitationButton
import com.example.tempomobileapp.ui.theme.Main1
import com.example.tempomobileapp.ui.theme.background
import com.spr.jetpack_loading.components.indicators.BallPulseSyncIndicator
import confirmPasswordField
import emailField
import kotlinx.coroutines.CoroutineScope
import passwordField
import phoneField
import securityQuestionsFields
import usernameField
import java.util.Locale

@Composable
fun signInLayout(
    securityQuestions: List<SecurityQuestion> = emptyList(),
    navController: NavHostController
) {
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        resetSignInStates()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(background)
    ) {
        topDecoration()

        if (securityQuestions.isNotEmpty()) {
            val coroutineScope = rememberCoroutineScope()

            securityQuestions.forEach { _ ->
                securityAnswers.add(mutableStateOf(""))
                securityErrors.add(mutableStateOf(null))
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 90.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 32.dp),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    formFields(coroutineScope, securityQuestions)

                    if (userError == true) {
                        userErrorText()
                    }

                    valitationButton(securityQuestions, context)
                }
            }
            if (openDialog) {
                successDialog(navController)
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 90.dp),
                contentAlignment = Alignment.Center
            ) {
                BallPulseSyncIndicator(color = Main1, spaceBetweenBalls = 40f)
            }
        }
        bottomDecoration()
    }
}

@Composable
private fun formFields(coroutineScope: CoroutineScope, securityQuestions: List<SecurityQuestion>) {
    topTexts()

    usernameField(coroutineScope)
    emailField()
    phoneField()
    passwordField(coroutineScope)
    confirmPasswordField()

    midTexts()

    securityQuestionsFields(securityQuestions)

    Spacer(modifier = Modifier.height(32.dp))
}

fun getDefaultCountry(): Country {
    val defaultCountryCode = Locale.getDefault().country
    return countries.firstOrNull { it.code == defaultCountryCode }
        ?: Country("Format libre", "", "", R.drawable.flag_white, "")
}

internal suspend fun createUser(securityQuestions: List<SecurityQuestion>, context: Context) {
    Log.d("App", "Creating user")

    userError = false
    openDialog = true

    Log.d("App", "Valid user")
    Log.d("App", "Username: $username")
    Log.d("App", "Email: $email")
    Log.d("App", "Phone: $phoneNumber")
    Log.d("App", "Password: $password")
    Log.d("App", "Password Check: $passwordCheck")
    Log.d(
        "App",
        "Security Answers: ${
            securityAnswers.joinToString(", ") { it.value }
        }"
    )

    securityQuestions.forEachIndexed { index, securityQuestion ->
        securityQuestion.response = securityAnswers[index].value
    }

    val deviceId = Settings.Secure.getString(
        context.contentResolver,
        Settings.Secure.ANDROID_ID
    )
    Log.d("App", "Device ID: $deviceId")

    TempoApiService.getInstance().createUser(
        UserCreate(

            username,
            password,
            email,
            phoneNumber,
            securityQuestions,
            deviceId
        )
    )
}
