package com.example.tempomobileapp.signin.validators

import android.util.Log
import com.example.tempomobileapp.adapters.TempoApiService
import com.example.tempomobileapp.enums.Country
import com.example.tempomobileapp.models.SecurityQuestion
import com.example.tempomobileapp.signin.email
import com.example.tempomobileapp.signin.emailError
import com.example.tempomobileapp.signin.password
import com.example.tempomobileapp.signin.passwordCheck
import com.example.tempomobileapp.signin.passwordCheckError
import com.example.tempomobileapp.signin.passwordError
import com.example.tempomobileapp.signin.phoneError
import com.example.tempomobileapp.signin.phoneNumber
import com.example.tempomobileapp.signin.securityAnswers
import com.example.tempomobileapp.signin.securityErrors
import com.example.tempomobileapp.signin.selectedCountry
import com.example.tempomobileapp.signin.username
import com.example.tempomobileapp.signin.usernameError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

private suspend fun isUsernameValid(
    username: String,
    onError: (String?) -> Unit
): Boolean {
    val errorMessage = when {
        username.isBlank() -> "Le pseudo est obligatoire."
        withContext(Dispatchers.IO) { !TempoApiService.getInstance().checkIfUserAvailable(username) } ->
            "Ce pseudo est déjà pris."
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
        email.isBlank() -> "L'email est obligatoire."
        !email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex()) ->
            "L'email n'est pas valide."
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
            onError("Le numéro de téléphone est obligatoire.")
            false
        }
        !selectedCountry.phoneFormat
            .replace("#", "\\d")
            .replace("+", "\\+")
            .toRegex()
            .matches(phoneNumber) -> {
            onError("Le numéro de téléphone n'est pas valide ...")
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

    if (!isUsernameValid(username) { error -> usernameError.value = error }) {
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
        passwordCheckError = "Les mots de passe ne correspondent pas."
        isValid = false
    }

    Log.d("App", "security questions : $securityQuestions")
    Log.d("App", "security answers : $securityAnswers")
    securityQuestions.forEachIndexed { index, _ ->
        if (securityAnswers[index].value.isBlank()) {
            securityErrors[index].value = "Veuillez répondre à la question."
            isValid = false
        }
    }
    Log.d("App", "security erroirs : $securityErrors")

    return isValid
}
