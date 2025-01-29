package com.example.tempomobileapp.signin.validators

import com.example.tempomobileapp.adapters.HIBPApiService
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.Test

class PasswordValidatorTest {

    @Test
    fun checkPassword_should_return_true_if_password_is_valid() = runBlocking {
        val password = "Azerty124578"
        val mockHIBPApiService = mockk<HIBPApiService>()

        mockkObject(HIBPApiService)
        every { HIBPApiService.getInstance() } returns mockHIBPApiService
        coEvery { mockHIBPApiService.checkPassword(password) } returns false

        var message: String? = ""

        val isUserValid = checkPassword(
            password,
            "username",
            "email@fake.com"
        ) { error -> message = error }

        assertTrue(isUserValid)
        assertEquals(null, message)
    }

    @Test
    fun checkPassword_should_return_false_if_password_is_short() = runBlocking {
        val password = "short"

        val mockHIBPApiService = mockk<HIBPApiService>()

        mockkObject(HIBPApiService)
        every { HIBPApiService.getInstance() } returns mockHIBPApiService
        coEvery { mockHIBPApiService.checkPassword(password) } returns false

        var message: String? = ""

        val isUserValid = checkPassword(
            password,
            "username",
            "email@fake.com"
        ) { error -> message = error }
        assertFalse(isUserValid)
        assertEquals(
            message,
            "Ton mot de passe doit faire au moins 10 caractères."
        )
    }

    @Test
    fun checkPassword_should_return_false_if_password_has_repetitions() = runBlocking {
        val password = "passsworddd"
        val mockHIBPApiService = mockk<HIBPApiService>()

        mockkObject(HIBPApiService)
        every { HIBPApiService.getInstance() } returns mockHIBPApiService
        coEvery { mockHIBPApiService.checkPassword(password) } returns false

        var message: String? = ""

        val isUserValid = checkPassword(
            password,
            "username",
            "email@fake.com"
        ) { error -> message = error }
        assertFalse(isUserValid)
        assertEquals(
            message,
            "Tu ne peux pas avoir plus de 3 caractères identiques à la suite dans ton mot de passe."
        )
    }

    @Test
    fun checkPassword_should_return_false_if_password_has_series() = runBlocking {
        val password = "passwordabcdef"
        val mockHIBPApiService = mockk<HIBPApiService>()

        mockkObject(HIBPApiService)
        every { HIBPApiService.getInstance() } returns mockHIBPApiService
        coEvery { mockHIBPApiService.checkPassword(password) } returns false

        var message: String? = ""

        val isUserValid = checkPassword(
            password,
            "username",
            "email@fake.com"
        ) { error -> message = error }
        assertFalse(isUserValid)
        assertEquals(
            message,
            "Ton mot de passe ne doit pas contenir de séries consécutives comme '123', 'abc', etc."
        )
    }

    @Test
    fun checkPassword_should_return_false_if_password_has_no_upper() = runBlocking {
        val password = "passwordtest"
        val mockHIBPApiService = mockk<HIBPApiService>()

        mockkObject(HIBPApiService)
        every { HIBPApiService.getInstance() } returns mockHIBPApiService
        coEvery { mockHIBPApiService.checkPassword(password) } returns false

        var message: String? = ""

        val isUserValid = checkPassword(
            password,
            "username",
            "email@fake.com"
        ) { error -> message = error }
        assertFalse(isUserValid)
        assertEquals(message, "Ajoute au moins une lettre majuscule dans ton mot de passe.")
    }

    @Test
    fun checkPassword_should_return_false_if_password_has_no_lower() = runBlocking {
        val password = "PASSWORDTEST"
        val mockHIBPApiService = mockk<HIBPApiService>()

        mockkObject(HIBPApiService)
        every { HIBPApiService.getInstance() } returns mockHIBPApiService
        coEvery { mockHIBPApiService.checkPassword(password) } returns false

        var message: String? = ""

        val isUserValid = checkPassword(
            password,
            "username",
            "email@fake.com"
        ) { error -> message = error }
        assertFalse(isUserValid)
        assertEquals(message, "Assure-toi d'inclure au moins une lettre minuscule dans ton mot de passe.")
    }

    @Test
    fun checkPassword_should_return_false_if_password_has_no_number() = runBlocking {
        val password = "passwordTEST"
        val mockHIBPApiService = mockk<HIBPApiService>()

        mockkObject(HIBPApiService)
        every { HIBPApiService.getInstance() } returns mockHIBPApiService
        coEvery { mockHIBPApiService.checkPassword(password) } returns false

        var message: String? = ""

        val isUserValid = checkPassword(
            password,
            "username",
            "email@fake.com"
        ) { error -> message = error }
        assertFalse(isUserValid)
        assertEquals(message, "N'oublie pas d'ajouter au moins un chiffre dans ton mot de passe.")
    }

    @Test
    fun checkPassword_should_return_false_if_password_has_personal_info() = runBlocking {
        val password = "passwordTEST31username"
        val mockHIBPApiService = mockk<HIBPApiService>()

        mockkObject(HIBPApiService)
        every { HIBPApiService.getInstance() } returns mockHIBPApiService
        coEvery { mockHIBPApiService.checkPassword(password) } returns false

        var message: String? = ""

        val isUserValid = checkPassword(
            password,
            "username",
            "email@fake.com"
        ) { error -> message = error }
        assertFalse(isUserValid)
        assertEquals(
            message,
            "Évite d'utiliser des infos personnelles comme ton pseudo ou ton email dans le mot de passe."
        )
    }
}
