package com.example.tempomobileapp.signin.validators

import com.example.tempomobileapp.adapters.HIBPApiService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

internal suspend fun checkPassword(
    inputPassword: String,
    username: String,
    email: String,
    onError: (String?) -> Unit
): Boolean {
    val syncValidations = listOf(
        { validatePasswordLength(inputPassword, onError) },
        { validateRepeatedCharacters(inputPassword, onError) },
        { validateForbiddenSeries(inputPassword, onError) },
        { validateCharacterRequirements(inputPassword, onError) },
        { validatePersonalInfo(inputPassword, username, email, onError) }
    )

    val isValid = syncValidations.all { it() } && validatePasswordStrength(inputPassword, onError)

    if (isValid) onError(null)
    return isValid
}

private fun validatePasswordLength(inputPassword: String, onError: (String?) -> Unit): Boolean {
    if (inputPassword.length < 10) {
        onError("Ton mot de passe doit faire au moins 10 caractères.")
        return false
    }
    return true
}

private fun validateRepeatedCharacters(inputPassword: String, onError: (String?) -> Unit): Boolean {
    val regexIdentiques = Regex("(.)\\1{2,}")
    if (regexIdentiques.containsMatchIn(inputPassword)) {
        onError(
            "Tu ne peux pas avoir plus de 3 caractères identiques à la suite dans ton mot de passe."
        )
        return false
    }
    return true
}

private fun validateForbiddenSeries(inputPassword: String, onError: (String?) -> Unit): Boolean {
    val forbiddenSeries = generateSeries()
    for (series in forbiddenSeries) {
        if (inputPassword.contains(series)) {
            onError("Ton mot de passe ne doit pas contenir de séries consécutives comme '123', 'abc', etc.")
            return false
        }
    }
    return true
}

private fun generateSeries(): List<String> {
    val series = mutableListOf<String>()

    for (i in 0..8) {
        series.add((i..i + 2).joinToString("") { it.toString() })
    }

    for (i in 'a'..'y') {
        series.add((i..i + 2).joinToString("") { it.toString() })
    }

    for (i in 'A'..'Y') {
        series.add((i..i + 2).joinToString("") { it.toString() })
    }

    return series
}

private fun validateCharacterRequirements(
    inputPassword: String,
    onError: (String?) -> Unit
): Boolean {
    val errorMessage = when {
        !inputPassword.any { it.isUpperCase() } -> "Ajoute au moins une lettre majuscule dans ton mot de passe."
        !inputPassword.any { it.isLowerCase() } ->
            "Assure-toi d'inclure au moins une lettre minuscule dans ton mot de passe."
        !inputPassword.any { it.isDigit() } -> "N'oublie pas d'ajouter au moins un chiffre dans ton mot de passe."
        else -> null
    }

    onError(errorMessage)
    return errorMessage == null
}

private fun validatePersonalInfo(
    inputPassword: String,
    username: String,
    email: String,
    onError: (String?) -> Unit
): Boolean {
    val personalInfo = extractPersonalInfo(username, email)
    for (info in personalInfo) {
        if (info.length >= 4 && inputPassword.contains(info, ignoreCase = true)) {
            onError("Évite d'utiliser des infos personnelles comme ton pseudo ou ton email dans le mot de passe.")
            return false
        }
    }
    return true
}

private fun extractPersonalInfo(username: String, email: String): List<String> {
    val info = mutableSetOf<String>()

    fun generateSubstrings(input: String) {
        val length = input.length
        for (i in 0 until length) {
            for (j in i + 4..length) {
                info.add(input.substring(i, j))
            }
        }
    }

    generateSubstrings(username)

    val emailParts = email.split("@")
    if (emailParts.size == 2) {
        val beforeAt = emailParts[0]
        val afterAt = emailParts[1].substringBefore(".")

        generateSubstrings(beforeAt)
        generateSubstrings(afterAt)
    }

    return info.toList()
}

private suspend fun validatePasswordStrength(
    inputPassword: String,
    onError: (String?) -> Unit,
    dispatcher: CoroutineDispatcher = Dispatchers.IO
): Boolean {
    val isPasswordWeak = withContext(dispatcher) {
        HIBPApiService.getInstance().checkPassword(inputPassword)
    }
    if (isPasswordWeak) {
        onError("Ton mot de passe est un peu trop simple, essaie d'en choisir un plus fort !")
        return false
    }
    return true
}
