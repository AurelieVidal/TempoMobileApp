package com.example.tempomobileapp.signin.validators

import android.util.Log
import com.example.tempomobileapp.adapters.TempoApiService
import com.example.tempomobileapp.enums.Country
import com.example.tempomobileapp.models.SecurityQuestion
import com.example.tempomobileapp.signin.checkError
import com.example.tempomobileapp.signin.email
import com.example.tempomobileapp.signin.emailError
import com.example.tempomobileapp.signin.password
import com.example.tempomobileapp.signin.passwordCheck
import com.example.tempomobileapp.signin.passwordError
import com.example.tempomobileapp.signin.phoneError
import com.example.tempomobileapp.signin.phoneNumber
import com.example.tempomobileapp.signin.securityAnswers
import com.example.tempomobileapp.signin.securityErrors
import com.example.tempomobileapp.signin.selectedCountry
import com.example.tempomobileapp.signin.username
import com.example.tempomobileapp.signin.usernameError
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

private suspend fun isUsernameValid(
    username: String,
    onError: (String?) -> Unit,
    dispatcher: CoroutineDispatcher = Dispatchers.IO
): Boolean {
    val errorMessage = when {
        username.isBlank() -> "Tu dois choisir un pseudo."
        withContext(dispatcher) { !TempoApiService.getInstance().checkIfUserAvailable(username) } ->
            "Ce pseudo est déjà utilisé."
        else -> null
    }

    onError(errorMessage)
    return errorMessage == null
}

private fun isEmailValid(
    email: String,
    onError: (String?) -> Unit
): Boolean {
    val errorMessage = when {
        email.isBlank() ->
            "Tu dois renseigner un email."
        !email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex()) ->
            "Cet email n'est pas valide."
        else -> null
    }

    onError(errorMessage)
    return errorMessage == null
}

private fun isPhoneNumberValid(
    phoneNumber: String,
    selectedCountry: Country,
    onError: (String?) -> Unit
): Boolean {
    val isValid = when {
        phoneNumber.isBlank() -> {
            onError("Tu dois renseigner ton numéro de téléphone.")
            false
        }
        selectedCountry.phoneFormat == "" -> {
            onError(null)
            true
        }
        !selectedCountry.phoneFormat
            .replace("#", "\\d")
            .replace("+", "\\+")
            .toRegex()
            .matches(phoneNumber) -> {
            onError("Le numéro de téléphone que tu as entré n'est pas valide...")
            false
        }
        else -> {
            onError(null)
            true
        }
    }

    return isValid
}

internal suspend fun validateUserInputs(securityQuestions: List<SecurityQuestion>): Boolean {
    var isValid = true

    if (!isUsernameValid(username, { error -> usernameError.value = error })) {
        isValid = false
    }

    if (!isEmailValid(email) { error -> emailError = error }) {
        isValid = false
    }

    if (!isPhoneNumberValid(phoneNumber, selectedCountry.value) { error -> phoneError = error }) {
        isValid = false
    }

    if (!checkPassword(password, username, email) { error -> passwordError = error }) {
        isValid = false
    }

    if (passwordCheck != password) {
        checkError = "Les mots de passe ne correspondent pas."
        isValid = false
    }

    securityQuestions.forEachIndexed { index, _ ->
        Log.d("App", "security question : ${securityQuestions[index].question}")
        if (securityAnswers[index].value.isBlank()) {
            securityErrors[index].value = "Il faut que tu répondes à cette question."
            isValid = false
        }
    }

    return isValid
}
