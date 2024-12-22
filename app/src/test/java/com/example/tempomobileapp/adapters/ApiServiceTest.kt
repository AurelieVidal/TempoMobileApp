package com.example.tempomobileapp.adapters


import okhttp3.Call
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.ArgumentMatchers.anyLong
import org.mockito.Mockito
import org.mockito.Mockito.mockStatic
import org.mockito.kotlin.any
import java.io.IOException
import java.util.concurrent.TimeUnit


class ApiServiceTest {
    private lateinit var mockClient: OkHttpClient
    private lateinit var mockBuilder: OkHttpClient.Builder

    @Before
    fun setup() {
        // Crée un client OkHttp mock
        mockClient = Mockito.mock(OkHttpClient::class.java)
        mockBuilder = Mockito.mock(OkHttpClient.Builder::class.java)


        // Injecte le client mock dans l'ApiService
        val apiService = ApiService.getInstance()  // Obtient l'instance singleton
        apiService.setClient(mockClient)  // Maintenant tu peux utiliser setClient sur l'instance


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

        val apiService = ApiService.getInstance()  // Obtient l'instance singleton
        val response = apiService.makeApiCall("https://example.com/test", "GET")  // Appelle makeApiCall sur l'instance


        assertEquals(200, response?.code)
        assertEquals("Mock response body", response?.body?.string())
    }

    @Test
    fun makeApiCall_should_return_success_response_with_custom_timeouts() {
        // Simuler un Builder d'OkHttpClient
        val mockBuilder = Mockito.mock(OkHttpClient.Builder::class.java)
        Mockito.`when`(mockClient.newBuilder()).thenReturn(mockBuilder)

        // Configurer les méthodes chaînables du Builder
        Mockito.`when`(mockBuilder.connectTimeout(any(), any())).thenReturn(mockBuilder)
        Mockito.`when`(mockBuilder.readTimeout(any(), any())).thenReturn(mockBuilder)
        Mockito.`when`(mockBuilder.writeTimeout(any(), any())).thenReturn(mockBuilder)
        Mockito.`when`(mockBuilder.build()).thenReturn(mockClient) // Retourne le client simulé

        // Simuler une réponse HTTP 200
        val mockResponse = Response.Builder()
            .request(Request.Builder().url("https://example.com/test").build())
            .protocol(Protocol.HTTP_1_1)
            .code(200) // HTTP 200 OK
            .message("OK")
            .body("Mock response body".toResponseBody("text/plain".toMediaTypeOrNull()))
            .build()

        // Simuler un objet Call
        val mockCall = Mockito.mock(okhttp3.Call::class.java)

        // Configurer le mock pour qu'il retourne la réponse simulée
        Mockito.`when`(mockCall.execute()).thenReturn(mockResponse)

        // Configurer le client OkHttp pour qu'il retourne l'objet Call simulé
        Mockito.`when`(mockClient.newCall(any())).thenReturn(mockCall)

        // Appeler la méthode à tester
        val apiService = ApiService.getInstance()  // Obtient l'instance singleton
        val response = apiService.makeApiCall(
            "https://example.com/test",
            "GET",
            connectTimeout = 0,
            readTimeout = 0,
            writeTimeout = 0
        )

        // Vérifier que la réponse est correcte
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

        val apiService = ApiService.getInstance()  // Obtient l'instance singleton
        val response = apiService.makeApiCall("https://example.com/test")  // Appelle makeApiCall sur l'instance


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

        val headers = mapOf("Authorization" to "Bearer token123", "Content-Type" to "application/json")
        val body = """{"key": "value"}"""

        val apiService = ApiService.getInstance()  // Obtient l'instance singleton
        val response = apiService.makeApiCall(
            url = "https://example.com/test",
            method = "POST",
            headers = headers,
            body = body
        )  // Appelle makeApiCall sur l'instance

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

        val headers = mapOf("Authorization" to "Bearer token123", "Content-Type" to "application/json")
        val body = """{"updateKey": "newValue"}"""
        val apiService = ApiService.getInstance()
        val response = apiService.makeApiCall(
            url = "https://example.com/test",
            method = "PUT",
            headers = headers,
            body = body
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
            url = "https://example.com/test",
            method = "DELETE",
            headers = headers
        )

        assertEquals(204, response?.code)
        assertNull(response?.body)
    }

    @Test
    fun makeApiCall_should_return_null_on_http_500_error() {
        // Crée une réponse 500 mockée
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

        // Mock du client HTTP
        //Mockito.`when`(mockClient.newCall(Mockito.any(Request::class.java))).thenReturn(mockCall)

        // Appel de la méthode à tester
        val apiService = ApiService.getInstance()
        apiService.setClient(mockClient) // Assure-toi d'injecter le mockClient
        val response = apiService.makeApiCall("https://example.com/test", "GET")

        // Vérifie que la réponse est nulle en cas d'erreur 500
        assertNull(response)
    }

    @Test
    fun makeApiCall_should_return_null_on_io_exception() {
        val mockCall = Mockito.mock(Call::class.java)
        Mockito.`when`(mockCall.execute()).thenThrow(IOException("Network error"))
        Mockito.`when`(mockClient.newCall(any())).thenReturn(mockCall)

        val apiService = ApiService.getInstance()
        val response = apiService.makeApiCall("https://example.com/test")

        assertNull(response)
    }

    @Test
    fun makeApiCall_should_return_null_on_invalid_http_method() {
        try {
            val apiService = ApiService.getInstance()
            apiService.makeApiCall("https://example.com/test", method = "INVALID")
            fail("Exception should have been thrown for unsupported HTTP method")
        } catch (e: IllegalArgumentException) {
            assertEquals("Méthode HTTP non supportée : INVALID", e.message)
        }
    }
}
