package com.example.tempomobileapp.signin.validators

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import com.example.tempomobileapp.R
import com.example.tempomobileapp.adapters.HIBPApiService
import com.example.tempomobileapp.adapters.TempoApiService
import com.example.tempomobileapp.enums.Country
import com.example.tempomobileapp.models.SecurityQuestion
import com.example.tempomobileapp.signin.email
import com.example.tempomobileapp.signin.emailError
import com.example.tempomobileapp.signin.password
import com.example.tempomobileapp.signin.passwordCheck
import com.example.tempomobileapp.signin.checkError
import com.example.tempomobileapp.signin.passwordError
import com.example.tempomobileapp.signin.phoneError
import com.example.tempomobileapp.signin.phoneNumber
import com.example.tempomobileapp.signin.securityAnswers
import com.example.tempomobileapp.signin.securityErrors
import com.example.tempomobileapp.signin.selectedCountry
import com.example.tempomobileapp.signin.username
import com.example.tempomobileapp.signin.usernameError
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class InputValidatorsTest {

    @Before
    fun setup() {
        username = "testUsername"
        usernameError.value = null
        email = "test@example.com"
        emailError = null
        phoneNumber = "+33 1 02 04 03 05"
        selectedCountry.value =
            Country("France", "FR", "+33", R.drawable.flag_france, "+33 # ## ## ## ##")
        phoneError = null
        password = "tetPassword91"
        passwordError = null
        passwordCheck = "tetPassword91"
        checkError = null
        securityAnswers = mutableStateListOf(
            mutableStateOf("answer1"),
            mutableStateOf("answer2"),
            mutableStateOf("answer3")
        )
        securityErrors =
            mutableStateListOf(mutableStateOf(""), mutableStateOf(""), mutableStateOf(""))
    }

    @Test
    fun isUserValid_should_return_true_when_user_is_creatable() = runBlocking {
        val mockTempoApiService = mockk<TempoApiService>()
        coEvery { mockTempoApiService.checkIfUserAvailable(username) } returns true
        TempoApiService.setInstanceForTesting(mockTempoApiService)

        val mockHIBPApiService = mockk<HIBPApiService>()
        coEvery { mockHIBPApiService.checkPassword(password) } returns false
        HIBPApiService.setInstanceForTesting(mockHIBPApiService)

        val securityQuestions = listOf(
            SecurityQuestion(1, "Question 1"),
            SecurityQuestion(1, "Question 2"),
            SecurityQuestion(1, "Question 3"),
        )

        val isUserValid = validateUserInputs(securityQuestions)

        assertTrue(isUserValid)
    }

    @Test
    fun isUserValid_should_return_false_when_fields_are_in_error() = runBlocking {
        email = "testexample.com"
        phoneNumber = "+33 1 02 04 03"
        passwordCheck = "different"
        securityAnswers = mutableStateListOf(
            mutableStateOf(""),
            mutableStateOf("answer2"),
            mutableStateOf("answer3")
        )

        val mockTempoApiService = mockk<TempoApiService>()
        coEvery { mockTempoApiService.checkIfUserAvailable(username) } returns false
        TempoApiService.setInstanceForTesting(mockTempoApiService)

        val mockHIBPApiService = mockk<HIBPApiService>()
        coEvery { mockHIBPApiService.checkPassword(password) } returns true
        HIBPApiService.setInstanceForTesting(mockHIBPApiService)

        val securityQuestions = listOf(
            SecurityQuestion(1, "Question 1"),
            SecurityQuestion(1, "Question 2"),
            SecurityQuestion(1, "Question 3"),
        )

        val isUserValid = validateUserInputs(securityQuestions)

        assertFalse(isUserValid)
    }

    @Test
    fun isUserValid_should_return_false_when_empty_fields() = runBlocking {
        username = ""
        phoneNumber = ""
        email = ""

        val mockHIBPApiService = mockk<HIBPApiService>()
        coEvery { mockHIBPApiService.checkPassword(password) } returns true
        HIBPApiService.setInstanceForTesting(mockHIBPApiService)

        val securityQuestions = listOf(
            SecurityQuestion(1, "Question 1"),
            SecurityQuestion(1, "Question 2"),
            SecurityQuestion(1, "Question 3"),
        )

        val isUserValid = validateUserInputs(securityQuestions)

        assertFalse(isUserValid)
    }
}
