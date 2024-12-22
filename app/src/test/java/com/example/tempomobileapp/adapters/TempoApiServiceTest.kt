package com.example.tempomobileapp.adapters

import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import okhttp3.Response
import okhttp3.ResponseBody
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.verify

class TempoApiServiceTest {

    private lateinit var tempoApiService: TempoApiService
    private lateinit var mockApiService: ApiService

    @Before
    fun setup() {
        mockApiService = mock(ApiService::class.java)
        tempoApiService = TempoApiService.getInstance()
        tempoApiService.setApiService(mockApiService)
    }

    @Test
    fun callApi_should_call_makeApiCall_with_correct_parameters_for_GET() {
        val mockResponse = mock(Response::class.java)
        `when`(
            mockApiService.makeApiCall(
                any()
            )
        ).thenReturn(mockResponse)

        val route = "/health"
        tempoApiService.callApi(route, "GET")

        verify(mockApiService).makeApiCall(
            eq(
                ApiService.ApiRequest(
                    "https://tempoapi-8iou.onrender.com/health",
                    "GET",
                    mapOf("accept" to "application/json"),
                    null,
                    0
                )
            )
        )
    }

    @Test
    fun callApi_should_call_makeApiCall_with_correct_parameters_for_POST() {
        val mockResponse = mock(Response::class.java)
        `when`(
            mockApiService.makeApiCall(any())
        ).thenReturn(mockResponse)

        val route = "/create"
        val body = """{"name": "example"}"""
        tempoApiService.callApi(route, "POST", body)

        verify(mockApiService).makeApiCall(
            eq(
                ApiService.ApiRequest(
                    "https://tempoapi-8iou.onrender.com/create",
                    "POST",
                    mapOf("accept" to "application/json"),
                    body,
                    0
                )
            )
        )
    }

    @Test
    fun callApi_should_call_makeApiCall_with_default_GET_method_when_no_method_provided() {
        val mockResponse = mock(Response::class.java)
        `when`(
            mockApiService.makeApiCall(any())
        ).thenReturn(mockResponse)

        val route = "/status"
        tempoApiService.callApi(route)

        verify(mockApiService).makeApiCall(
            eq(
                ApiService.ApiRequest(
                    "https://tempoapi-8iou.onrender.com/status",
                    "GET",
                    mapOf("accept" to "application/json"),
                    null,
                    0
                )
            )
        )
    }

    @Test
    fun checkHealth_should_return_true_when_API_response_is_successful() = runBlocking {
        val mockResponse = mock(Response::class.java)
        val mockResponseBody = mock(ResponseBody::class.java)

        `when`(mockResponse.isSuccessful).thenReturn(true)
        `when`(mockResponse.code).thenReturn(200)
        `when`(mockResponse.body).thenReturn(mockResponseBody)

        val mockApiService = mock(ApiService::class.java)

        `when`(mockApiService.makeApiCall(any()))
            .thenReturn(mockResponse)

        val tempoApiService = TempoApiService.getInstance()

        tempoApiService.setApiService(mockApiService)

        val isHealthy = tempoApiService.checkHealth()

        verify(mockApiService).makeApiCall(any())

        assertTrue(isHealthy)
    }

    @Test
    fun checkHealth_should_return_false_when_API_response_is_failure() = runBlocking {
        val mockResponse = mock(Response::class.java)
        val mockResponseBody = mock(ResponseBody::class.java)

        `when`(mockResponse.isSuccessful).thenReturn(false)
        `when`(mockResponse.code).thenReturn(500)
        `when`(mockResponse.body).thenReturn(mockResponseBody)

        val mockApiService = mock(ApiService::class.java)

        `when`(mockApiService.makeApiCall(any()))
            .thenReturn(mockResponse)

        val tempoApiService = TempoApiService.getInstance()

        tempoApiService.setApiService(mockApiService)

        val isHealthy = tempoApiService.checkHealth()

        verify(mockApiService).makeApiCall(any())

        assertFalse(isHealthy)
    }
}
