package com.example.tempomobileapp.signin.validators

import android.util.Log
import com.example.tempomobileapp.adapters.HIPBApiService
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
        onError("Le mot de passe doit contenir au moins 10 caractères")
        return false
    }
    return true
}

private fun validateRepeatedCharacters(inputPassword: String, onError: (String?) -> Unit): Boolean {
    val regexIdentiques = Regex("(.)\\1{2,}")
    if (regexIdentiques.containsMatchIn(inputPassword)) {
        onError("Le mot de passe ne doit pas contenir plus de 3 caractères identiques consécutifs")
        return false
    }
    return true
}

private fun validateForbiddenSeries(inputPassword: String, onError: (String?) -> Unit): Boolean {
    val forbiddenSeries = generateSeries()
    for (series in forbiddenSeries) {
        if (inputPassword.contains(series)) {
            onError("Le mot de passe ne doit pas contenir de séries consécutives comme '123', 'abc', etc.")
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
        !inputPassword.any { it.isUpperCase() } -> "Le mot de passe doit contenir au moins une lettre majuscule"
        !inputPassword.any { it.isLowerCase() } -> "Le mot de passe doit contenir au moins une lettre minuscule"
        !inputPassword.any { it.isDigit() } -> "Le mot de passe doit contenir au moins un chiffre"
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
            onError("Le mot de passe ne doit pas contenir d'informations personnelles comme le pseudo ou l'email")
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

    Log.d("App", "Extracted info: $info")
    return info.toList()
}

private suspend fun validatePasswordStrength(inputPassword: String, onError: (String?) -> Unit): Boolean {
    val isPasswordWeak = withContext(Dispatchers.IO) {
        HIPBApiService.getInstance().checkPassword(inputPassword)
    }
    if (isPasswordWeak) {
        onError("Le mot de passe est trop faible")
        return false
    }
    return true
}
