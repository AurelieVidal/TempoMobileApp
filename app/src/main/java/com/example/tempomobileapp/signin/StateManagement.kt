package com.example.tempomobileapp.signin

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.Job

internal var debounceJob: Job? = null
internal val selectedCountry = mutableStateOf(getDefaultCountry())

// Used in validation function
internal var isLoading = mutableStateOf(false)

// Handle username input
internal var usernameError = mutableStateOf<String?>(null)
internal var validUsername by mutableStateOf<Boolean?>(null)
internal var username by mutableStateOf("")

// Handle email input
internal var emailError by mutableStateOf<String?>(null)
internal var email by mutableStateOf("")

// Handle phone input
internal var phoneNumber by mutableStateOf("")
internal var phoneError by mutableStateOf<String?>(null)

// Handle password input
internal var passwordError by mutableStateOf<String?>(null)
internal var password by mutableStateOf("")

// Handle password confirmation input
internal var checkError by mutableStateOf<String?>(null)
internal var passwordCheck by mutableStateOf("")

// Handle security questions
internal var securityAnswers = mutableStateListOf<MutableState<String>>()
internal var securityErrors = mutableStateListOf<MutableState<String?>>()

// Global check
internal var userError = mutableStateOf(false)

internal fun resetSignInStates() {
    usernameError.value = null
    validUsername = null
    username = ""

    emailError = null
    email = ""

    phoneNumber = ""
    phoneError = null

    passwordError = null
    password = ""

    checkError = null
    passwordCheck = ""

    securityAnswers.clear()
    securityErrors.clear()

    userError.value = false
    isLoading.value = false
    debounceJob = null
}
