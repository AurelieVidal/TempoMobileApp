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
            "Le mot de passe doit contenir au moins 10 caractères"
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
            "Le mot de passe ne doit pas contenir plus de 3 caractères identiques consécutifs"
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
            "Le mot de passe ne doit pas contenir de séries consécutives comme '123', 'abc', etc."
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
        assertEquals(message, "Le mot de passe doit contenir au moins une lettre majuscule")
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
        assertEquals(message, "Le mot de passe doit contenir au moins une lettre minuscule")
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
        assertEquals(message, "Le mot de passe doit contenir au moins un chiffre")
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
            "Le mot de passe ne doit pas contenir d'informations personnelles comme le pseudo ou l'email"
        )
    }

}