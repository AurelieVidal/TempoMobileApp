package com.example.tempomobileapp.adapters

import okhttp3.Call
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.kotlin.any
import java.io.IOException

class ApiServiceTest {
    private lateinit var mockClient: OkHttpClient
    private lateinit var mockBuilder: OkHttpClient.Builder

    @Before
    fun setup() {
        mockClient = Mockito.mock(OkHttpClient::class.java)
        mockBuilder = Mockito.mock(OkHttpClient.Builder::class.java)

        val apiService = ApiService.getInstance()
        apiService.setClient(mockClient)
    }

    @Test
    fun makeApiCall_should_return_success_response() {
        val mockResponse = Response.Builder()
            .request(Request.Builder().url("https://example.com/test").build())
            .protocol(Protocol.HTTP_1_1)
            .code(200)
            .message("OK")
            .body("Mock response body".toResponseBody("text/plain".toMediaTypeOrNull()))
            .build()

        val mockCall = Mockito.mock(Call::class.java)
        Mockito.`when`(mockCall.execute()).thenReturn(mockResponse)
        Mockito.`when`(mockClient.newCall(any())).thenReturn(mockCall)

        val apiService = ApiService.getInstance()
        val response =
            apiService.makeApiCall(ApiService.ApiRequest("https://example.com/test", "GET"))

        assertEquals(200, response?.code)
        assertEquals("Mock response body", response?.body?.string())
    }

    @Test
    fun makeApiCall_should_return_success_response_with_custom_timeouts() {
        val mockBuilder = Mockito.mock(OkHttpClient.Builder::class.java)
        Mockito.`when`(mockClient.newBuilder()).thenReturn(mockBuilder)

        Mockito.`when`(mockBuilder.connectTimeout(any(), any())).thenReturn(mockBuilder)
        Mockito.`when`(mockBuilder.readTimeout(any(), any())).thenReturn(mockBuilder)
        Mockito.`when`(mockBuilder.writeTimeout(any(), any())).thenReturn(mockBuilder)
        Mockito.`when`(mockBuilder.build()).thenReturn(mockClient)

        val mockResponse = Response.Builder()
            .request(Request.Builder().url("https://example.com/test").build())
            .protocol(Protocol.HTTP_1_1)
            .code(200)
            .message("OK")
            .body("Mock response body".toResponseBody("text/plain".toMediaTypeOrNull()))
            .build()

        val mockCall = Mockito.mock(Call::class.java)

        Mockito.`when`(mockCall.execute()).thenReturn(mockResponse)
        Mockito.`when`(mockClient.newCall(any())).thenReturn(mockCall)

        val apiService = ApiService.getInstance()
        val response = apiService.makeApiCall(
            ApiService.ApiRequest(
                "https://example.com/test",
                "GET",
                timeout = 0,
            )
        )

        assertEquals(200, response?.code)
        assertEquals("Mock response body", response?.body?.string())
    }

    @Test
    fun makeApiCall_should_return_success_response_default_args() {
        val mockResponse = Response.Builder()
            .request(Request.Builder().url("https://example.com/test").build())
            .protocol(Protocol.HTTP_1_1)
            .code(200)
            .message("OK")
            .body("Mock response body".toResponseBody("text/plain".toMediaTypeOrNull()))
            .build()

        val mockCall = Mockito.mock(Call::class.java)
        Mockito.`when`(mockCall.execute()).thenReturn(mockResponse)
        Mockito.`when`(mockClient.newCall(any())).thenReturn(mockCall)

        val apiService = ApiService.getInstance()
        val response = apiService.makeApiCall(ApiService.ApiRequest("https://example.com/test"))

        assertEquals(200, response?.code)
        assertEquals("Mock response body", response?.body?.string())
    }

    @Test
    fun makeApiCall_should_return_success_response_for_post() {
        val mockResponse = Response.Builder()
            .request(Request.Builder().url("https://example.com/test").build())
            .protocol(Protocol.HTTP_1_1)
            .code(201)
            .message("Created")
            .body("Mock POST response body".toResponseBody("text/plain".toMediaTypeOrNull()))
            .build()

        val mockCall = Mockito.mock(Call::class.java)
        Mockito.`when`(mockCall.execute()).thenReturn(mockResponse)
        Mockito.`when`(mockClient.newCall(any())).thenReturn(mockCall)

        val headers =
            mapOf("Authorization" to "Bearer token123", "Content-Type" to "application/json")
        val body = """{"key": "value"}"""

        val apiService = ApiService.getInstance()
        val response = apiService.makeApiCall(
            ApiService.ApiRequest(
                url = "https://example.com/test",
                method = "POST",
                headers = headers,
                body = body
            )
        )

        assertEquals(201, response?.code)
        assertEquals("Mock POST response body", response?.body?.string())
    }

    @Test
    fun makeApiCall_should_return_success_response_for_put() {
        val mockResponse = Response.Builder()
            .request(Request.Builder().url("https://example.com/test").build())
            .protocol(Protocol.HTTP_1_1)
            .code(200)
            .message("OK")
            .body("Mock PUT response body".toResponseBody("text/plain".toMediaTypeOrNull()))
            .build()

        val mockCall = Mockito.mock(Call::class.java)
        Mockito.`when`(mockCall.execute()).thenReturn(mockResponse)
        Mockito.`when`(mockClient.newCall(any())).thenReturn(mockCall)

        val headers =
            mapOf("Authorization" to "Bearer token123", "Content-Type" to "application/json")
        val body = """{"updateKey": "newValue"}"""
        val apiService = ApiService.getInstance()
        val response = apiService.makeApiCall(
            ApiService.ApiRequest(
                url = "https://example.com/test",
                method = "PUT",
                headers = headers,
                body = body
            )
        )

        assertEquals(200, response?.code)
        assertEquals("Mock PUT response body", response?.body?.string())
    }

    @Test
    fun makeApiCall_should_return_success_response_for_delete() {
        val mockResponse = Response.Builder()
            .request(Request.Builder().url("https://example.com/test").build())
            .protocol(Protocol.HTTP_1_1)
            .code(204)
            .message("No Content")
            .body(null)
            .build()

        val mockCall = Mockito.mock(Call::class.java)
        Mockito.`when`(mockCall.execute()).thenReturn(mockResponse)
        Mockito.`when`(mockClient.newCall(any())).thenReturn(mockCall)

        val headers = mapOf("Authorization" to "Bearer token123")
        val apiService = ApiService.getInstance()
        val response = apiService.makeApiCall(
            ApiService.ApiRequest(
                url = "https://example.com/test",
                method = "DELETE",
                headers = headers
            )
        )

        assertEquals(204, response?.code)
        assertNull(response?.body)
    }

    @Test
    fun makeApiCall_should_return_null_on_http_500_error() {
        val mockResponse = Response.Builder()
            .request(Request.Builder().url("https://example.com/test").build())
            .protocol(Protocol.HTTP_1_1)
            .code(500)
            .message("No Content")
            .body(null)
            .build()

        val mockCall = Mockito.mock(Call::class.java)
        Mockito.`when`(mockCall.execute()).thenReturn(mockResponse)
        Mockito.`when`(mockClient.newCall(any())).thenReturn(mockCall)

        val apiService = ApiService.getInstance()
        apiService.setClient(mockClient)
        val response =
            apiService.makeApiCall(ApiService.ApiRequest("https://example.com/test", "GET"))

        assertNull(response)
    }

    @Test
    fun makeApiCall_should_return_null_on_io_exception() {
        val mockCall = Mockito.mock(Call::class.java)
        Mockito.`when`(mockCall.execute()).thenThrow(IOException("Network error"))
        Mockito.`when`(mockClient.newCall(any())).thenReturn(mockCall)

        val apiService = ApiService.getInstance()
        val response = apiService.makeApiCall(ApiService.ApiRequest("https://example.com/test"))

        assertNull(response)
    }

    @Test
    fun makeApiCall_should_return_null_on_invalid_http_method() {
        try {
            val apiService = ApiService.getInstance()
            apiService.makeApiCall(
                ApiService.ApiRequest(
                    "https://example.com/test",
                    method = "INVALID"
                )
            )
            fail("Exception should have been thrown for unsupported HTTP method")
        } catch (e: IllegalArgumentException) {
            assertEquals("Unsupported HTTP method: INVALID", e.message)
        }
    }
}
