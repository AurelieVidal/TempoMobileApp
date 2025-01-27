package com.example.tempomobileapp.adapters

import com.example.tempomobileapp.exceptions.ApiException
import com.example.tempomobileapp.models.SecurityQuestion
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import okhttp3.Response
import okhttp3.ResponseBody
import org.junit.Assert.assertThrows
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test
import java.io.IOException

class TempoApiServiceTest {

    private lateinit var tempoApiService: TempoApiService
    private lateinit var mockApiService: ApiService

    @Before
    fun setup() {
        mockApiService = mockk()
        tempoApiService = TempoApiService.getInstance()
        tempoApiService.setApiService(mockApiService)
    }

    @Test
    fun callApi_should_call_makeApiCall_with_correct_parameters_for_GET() {
        val mockResponse = mockk<Response>()
        every { mockApiService.makeApiCall(any()) } returns mockResponse

        val route = "/health"
        tempoApiService.callApi(route, "GET")

        verify {
            mockApiService.makeApiCall(
                ApiService.ApiRequest(
                    "https://tempoapi-8iou.onrender.com/health",
                    "GET",
                    mapOf("accept" to "application/json"),
                    null,
                    0
                )
            )
        }
    }

    @Test
    fun callApi_should_call_makeApiCall_with_correct_parameters_for_POST() {
        val mockResponse = mockk<Response>()
        every { mockApiService.makeApiCall(any()) } returns mockResponse

        val route = "/create"
        val body = """{"name": "example"}"""
        tempoApiService.callApi(route, "POST", body)

        verify {
            mockApiService.makeApiCall(
                ApiService.ApiRequest(
                    "https://tempoapi-8iou.onrender.com/create",
                    "POST",
                    mapOf("accept" to "application/json"),
                    body,
                    0
                )
            )
        }
    }

    @Test
    fun callApi_should_call_makeApiCall_with_default_GET_method_when_no_method_provided() {
        val mockResponse = mockk<Response>()
        every { mockApiService.makeApiCall(any()) } returns mockResponse

        val route = "/status"
        tempoApiService.callApi(route)

        verify {
            mockApiService.makeApiCall(
                ApiService.ApiRequest(
                    "https://tempoapi-8iou.onrender.com/status",
                    "GET",
                    mapOf("accept" to "application/json"),
                    null,
                    0
                )
            )
        }
    }

    @Test
    fun checkHealth_should_return_true_when_API_response_is_successful() = runBlocking {
        val mockResponse = mockk<Response>()
        val mockResponseBody = mockk<ResponseBody>()

        every { mockResponse.isSuccessful } returns true
        every { mockResponse.code } returns 200
        every { mockResponse.body } returns mockResponseBody
        every { mockResponseBody.string() } returns "Mock body content"

        val mockApiService = mockk<ApiService>()

        every { mockApiService.makeApiCall(any()) } returns mockResponse

        val tempoApiService = TempoApiService.getInstance()

        tempoApiService.setApiService(mockApiService)

        val isHealthy = tempoApiService.checkHealth()

        verify { mockApiService.makeApiCall(any()) }

        assertTrue(isHealthy)
    }

    @Test
    fun checkHealth_should_return_false_when_API_response_is_failure() = runBlocking {
        val mockResponse = mockk<Response>()
        val mockResponseBody = mockk<ResponseBody>()

        every { mockResponse.isSuccessful } returns false
        every { mockResponse.code } returns 500
        every { mockResponse.body } returns mockResponseBody
        every { mockResponseBody.string() } returns "Error response body"

        val mockApiService = mockk<ApiService>()

        every { mockApiService.makeApiCall(any()) } returns mockResponse

        val tempoApiService = TempoApiService.getInstance()

        tempoApiService.setApiService(mockApiService)

        val isHealthy = tempoApiService.checkHealth()

        verify { mockApiService.makeApiCall(any()) }

        assertFalse(isHealthy)
    }

    @Test
    fun getSecurityQuestions_should_return_list_of_3_questions() = runBlocking {
        val mockResponse = mockk<Response>()
        val mockResponseBody = mockk<ResponseBody>()

        every { mockResponseBody.string() } returns """
        {
          "questions": [
            {
              "id": 7,
              "question": "Quel est ton plat préféré à cuisiner ou à manger ?"
            },
            {
              "id": 10,
              "question": "Quel est ton animal préféré ou un animal qui te représente ?"
            },
            {
              "id": 13,
              "question": "Quelle est la saison de l'année que tu préfères ?"
            }
          ]
        }
        """.trimIndent()

        every { mockResponse.isSuccessful } returns true
        every { mockResponse.code } returns 200
        every { mockResponse.body } returns mockResponseBody

        val mockApiService = mockk<ApiService>()

        every { mockApiService.makeApiCall(any()) } returns mockResponse

        val tempoApiService = TempoApiService.getInstance()

        tempoApiService.setApiService(mockApiService)

        val questions = tempoApiService.getSecurityQuestions()

        verify { mockApiService.makeApiCall(any()) }

        assertEquals(
            questions,
            listOf(
                SecurityQuestion(
                    id = 7,
                    question = "Quel est ton plat préféré à cuisiner ou à manger ?"
                ),
                SecurityQuestion(
                    id = 10,
                    question = "Quel est ton animal préféré ou un animal qui te représente ?"
                ),
                SecurityQuestion(
                    id = 13,
                    question = "Quelle est la saison de l'année que tu préfères ?"
                )
            )
        )
    }


    @Test
    fun getSecurityQuestions_should_throw_json_exception_if_response_in_incorrect_format() =
        runBlocking {
            val mockResponse = mockk<Response>()
            val mockResponseBody = mockk<ResponseBody>()

            every { mockResponseBody.string() } returns """
                {
                  "bad_format": [
                    {
                      "id": 7,
                      "question": "Quel est ton plat préféré à cuisiner ou à manger ?"
                    },]
                }
            """.trimIndent()

            every { mockResponse.isSuccessful } returns true
            every { mockResponse.code } returns 200
            every { mockResponse.body } returns mockResponseBody

            val mockApiService = mockk<ApiService>()

            every { mockApiService.makeApiCall(any()) } returns mockResponse

            val tempoApiService = TempoApiService.getInstance()

            tempoApiService.setApiService(mockApiService)

            val exception = assertThrows(JsonSyntaxException::class.java) {
                runBlocking {
                    tempoApiService.getSecurityQuestions()
                }
            }

            verify { mockApiService.makeApiCall(any()) }

            assertEquals("Unexpected response format", exception.message)
        }

    @Test
    fun getSecurityQuestions_should_throw_api_exception_if_response_error() = runBlocking {
        val mockResponse = mockk<Response>()
        val mockResponseBody = mockk<ResponseBody>()

        every { mockResponse.isSuccessful } returns false
        every { mockResponse.code } returns 500
        every { mockResponse.body } returns mockResponseBody

        val mockApiService = mockk<ApiService>()

        every { mockApiService.makeApiCall(any()) } returns mockResponse

        val tempoApiService = TempoApiService.getInstance()

        tempoApiService.setApiService(mockApiService)

        val exception = assertThrows(ApiException::class.java) {
            runBlocking {
                tempoApiService.getSecurityQuestions()
            }
        }

        verify { mockApiService.makeApiCall(any()) }

        assertEquals("Unable to get security questions", exception.message)
    }


    @Test
    fun checkIfUserAvailable_should_return_false_if_user_found() = runBlocking {
        val mockResponse = mockk<Response>()
        val mockResponseBody = mockk<ResponseBody>()

        every { mockResponse.isSuccessful } returns true
        every { mockResponse.code } returns 200
        every { mockResponse.body } returns mockResponseBody

        val mockApiService = mockk<ApiService>()

        every { mockApiService.makeApiCall(any()) } returns mockResponse

        val tempoApiService = TempoApiService.getInstance()

        tempoApiService.setApiService(mockApiService)

        val isAvailable = tempoApiService.checkIfUserAvailable(username = "test")

        verify { mockApiService.makeApiCall(any()) }

        assertFalse(isAvailable)
    }

    @Test
    fun checkIfUserAvailable_should_return_true_if_user_not_found() = runBlocking {
        val mockResponse = mockk<Response>()
        val mockResponseBody = mockk<ResponseBody>()

        every { mockResponse.isSuccessful } returns false
        every { mockResponse.code } returns 404
        every { mockResponse.body } returns mockResponseBody

        val mockApiService = mockk<ApiService>()

        every { mockApiService.makeApiCall(any()) } returns mockResponse

        val tempoApiService = TempoApiService.getInstance()

        tempoApiService.setApiService(mockApiService)

        val isAvailable = tempoApiService.checkIfUserAvailable(username = "test")

        verify { mockApiService.makeApiCall(any()) }

        assertTrue(isAvailable)
    }

    @Test
    fun checkIfUserAvailable_should_throw_exception_if_invalid_response_code() = runBlocking {
        val mockResponse = mockk<Response>()
        val mockResponseBody = mockk<ResponseBody>()

        every { mockResponse.isSuccessful } returns false
        every { mockResponse.code } returns 403
        every { mockResponse.body } returns mockResponseBody

        val mockApiService = mockk<ApiService>()

        every { mockApiService.makeApiCall(any()) } returns mockResponse

        val tempoApiService = TempoApiService.getInstance()

        tempoApiService.setApiService(mockApiService)

        val exception = assertThrows(ApiException::class.java) {
            runBlocking {
                tempoApiService.checkIfUserAvailable(username = "test")
            }
        }

        verify { mockApiService.makeApiCall(any()) }

        assertEquals("Erreur inattendue de l'API : Code 403", exception.message)

    }

    @Test
    fun checkIfUserAvailable_should_throw_exception_if_unexpected_ioexception() = runBlocking {
        val mockApiService = mockk<ApiService>()

        coEvery { mockApiService.makeApiCall(any()) } throws IOException("test")

        val tempoApiService = TempoApiService.getInstance()
        tempoApiService.setApiService(mockApiService)

        val exception = assertThrows(IllegalStateException::class.java) {
            runBlocking {
                tempoApiService.checkIfUserAvailable(username = "test")
            }
        }

        coVerify { mockApiService.makeApiCall(any()) }

        assertEquals("Erreur inattendue de l'API : test", exception.message)
    }

    @Test
    fun createUser_should_return_true() = runBlocking {
        val mockApiService = mockk<ApiService>()

        val mockResponse = mockk<Response>()
        val mockResponseBody = mockk<ResponseBody>()

        every { mockResponse.isSuccessful } returns true
        every { mockResponse.body } returns mockResponseBody
        every { mockResponseBody.string() } returns "{}"

        coEvery { mockApiService.makeApiCall(any()) } returns mockResponse

        val tempoApiService = TempoApiService.getInstance()
        tempoApiService.setApiService(mockApiService)

        val userCreate = UserCreate(
            username = "testUser",
            password = "password123 ",
            email = "test@example.com ",
            phoneNumber = "123 456 789",
            deviceId = "device123",
            securityQuestions = listOf(
                SecurityQuestion(id = 1, question = "Question1", response = "Answer1 "),
                SecurityQuestion(id = 2, question = "Question2", response = "Answer2 ")
            )
        )

        val result = tempoApiService.createUser(userCreate)

        val captor = slot<ApiService.ApiRequest>()
        verify { mockApiService.makeApiCall(capture(captor)) }

        val capturedRequest = captor.captured
        assertEquals("https://tempoapi-8iou.onrender.com/users", capturedRequest.url)
        assertEquals("POST", capturedRequest.method)

        val expectedPayload = mapOf(
            "username" to "testUser",
            "password" to "password123",
            "email" to "test@example.com",
            "phone" to "123456789",
            "device" to "device123",
            "questions" to listOf(
                mapOf("questionId" to 1.0, "response" to "Answer1"),
                mapOf("questionId" to 2.0, "response" to "Answer2")
            )
        )
        val actualPayload = Gson().fromJson(capturedRequest.body, Map::class.java)
        assertEquals(expectedPayload, actualPayload)

        assertTrue(result)
    }

    @Test
    fun createUser_should_throw_error_is_response_not_successful() = runBlocking {
        val mockApiService = mockk<ApiService>()
        val mockResponse = mockk<Response>()
        val mockResponseBody = mockk<ResponseBody>()

        every { mockResponse.isSuccessful } returns false
        every { mockResponse.code } returns 400
        every { mockResponse.body } returns mockResponseBody

        every { mockResponseBody.string() } returns "Erreur lors de la création de l'utilisateur"

        coEvery { mockApiService.makeApiCall(any()) } returns mockResponse

        val tempoApiService = TempoApiService.getInstance()
        tempoApiService.setApiService(mockApiService)

        val userCreate = UserCreate(
            username = "testUser",
            password = "password123 ",
            email = "test@example.com ",
            phoneNumber = "123 456 789",
            deviceId = "device123",
            securityQuestions = listOf(
                SecurityQuestion(id = 1, question = "Question1", response = "Answer1 "),
                SecurityQuestion(id = 2, question = "Question2", response = "Answer2 ")
            )
        )

        try {
            tempoApiService.createUser(userCreate)
            fail("Expected ApiException to be thrown")
        } catch (e: ApiException) {
            assertEquals("Erreur lors de la création de l'utilisateur", e.message)
        }

        verify { mockApiService.makeApiCall(any()) }
    }

}
