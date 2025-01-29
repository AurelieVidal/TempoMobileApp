package com.example.tempomobileapp.adapters

import com.example.tempomobileapp.exceptions.ApiException
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import okhttp3.Response
import okhttp3.ResponseBody
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test

class HIBPApiServiceTest {

    private lateinit var hibpApiService: HIBPApiService
    private lateinit var mockApiService: ApiService

    @Before
    fun setup() {
        mockApiService = mockk()
        hibpApiService = HIBPApiService.getInstance()
        hibpApiService.setApiService(mockApiService)
    }

    @Test
    fun callApi_should_call_makeApiCall_with_correct_parameters_for_GET() {
        val mockResponse = mockk<Response>()

        every {
            mockApiService.makeApiCall(any())
        } returns mockResponse

        val route = "/range"

        hibpApiService.callApi(route, "GET")

        verify {
            mockApiService.makeApiCall(
                ApiService.ApiRequest(
                    "https://api.pwnedpasswords.com/range",
                    "GET",
                    emptyMap(),
                    null,
                    10
                )
            )
        }
    }

    @Test
    fun callApi_should_call_makeApiCall_with_correct_parameters_using_default() {
        val mockResponse = mockk<Response>()

        every {
            mockApiService.makeApiCall(any())
        } returns mockResponse

        val route = "/range"
        hibpApiService.callApi(route)

        verify {
            mockApiService.makeApiCall(
                ApiService.ApiRequest(
                    "https://api.pwnedpasswords.com/range",
                    "GET",
                    emptyMap(),
                    null,
                    10
                )
            )
        }
    }

    @Test
    fun checkPassword_should_return_false_when_password_is_strong() = runBlocking {
        val mockResponse = mockk<Response>()
        val mockResponseBody = mockk<ResponseBody>()

        every {
            mockResponseBody.string()
        } returns """
            C7008F9CAB4083784BBD1874F76618D2A97:5
            C4FDAC6008F9CAB4083784CBD1D74F76618:10
            D6008F9CAB408378ACBD1874F76618D2A97:2
        """.trimIndent()

        every { mockResponse.isSuccessful } returns true
        every { mockResponse.code } returns 200
        every { mockResponse.body } returns mockResponseBody

        val mockApiService = mockk<ApiService>()

        every { mockApiService.makeApiCall(any()) } returns mockResponse

        val hibpApiService = HIBPApiService.getInstance()

        hibpApiService.setApiService(mockApiService)

        val isStrong = hibpApiService.checkPassword("password123")

        verify { mockApiService.makeApiCall(any()) }

        assertFalse(isStrong)
    }

    @Test
    fun checkPassword_should_return_true_when_password_is_weak() = runBlocking {
        val mockResponse = mockk<Response>()
        val mockResponseBody = mockk<ResponseBody>()

        every {
            mockResponseBody.string()
        } returns """
            C6008F9CAB4083784CBD1874F76618D2A97:5
            C6008F9CAB4083784CBD1874F76618D2A97:150
            c6008f9cab4083784cbd1874f76618d2a97:2
        """.trimIndent()

        every { mockResponse.isSuccessful } returns true
        every { mockResponse.code } returns 200
        every { mockResponse.body } returns mockResponseBody

        val mockApiService = mockk<ApiService>()

        every { mockApiService.makeApiCall(any()) } returns mockResponse

        val hibpApiService = HIBPApiService.getInstance()

        hibpApiService.setApiService(mockApiService)

        val isStrong = hibpApiService.checkPassword("password123")

        verify { mockApiService.makeApiCall(any()) }

        assertTrue(isStrong)
    }

    @Test
    fun checkPassword_should_throw_APIException_when_API_call_fails() = runBlocking {
        val mockResponse = mockk<Response>()
        val mockResponseBody = mockk<ResponseBody>()

        every { mockResponse.isSuccessful } returns false
        every { mockResponse.code } returns 500
        every { mockResponse.body } returns mockResponseBody

        val mockApiService = mockk<ApiService>()

        every { mockApiService.makeApiCall(any()) } returns mockResponse

        val hibpApiService = HIBPApiService.getInstance()

        hibpApiService.setApiService(mockApiService)

        val exception = assertThrows(ApiException::class.java) {
            runBlocking {
                hibpApiService.checkPassword("password123")
            }
        }

        verify { mockApiService.makeApiCall(any()) }
        assertEquals("Erreur API : 500", exception.message)
    }
}
