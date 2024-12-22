package com.example.tempomobileapp.adapters

import android.util.Log
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import okhttp3.Response
import okhttp3.ResponseBody
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.*
import org.mockito.kotlin.verify
import org.mockito.kotlin.any
import org.mockito.kotlin.eq

class TempoApiServiceTest {

    private lateinit var tempoApiService: TempoApiService
    private lateinit var mockApiService: ApiService


    @Before
    fun setup() {
        // Créer une instance mock d'ApiService
        mockApiService = mock(ApiService::class.java)

        // Injecter le mock ApiService dans l'instance de TempoApiService
        tempoApiService = TempoApiService.getInstance()
        tempoApiService.setApiService(mockApiService)
    }

    @Test
    fun `callApi should call makeApiCall with correct parameters for GET`() {
        // Simuler une réponse de la méthode makeApiCall
        val mockResponse = mock(Response::class.java)
        `when`(
            mockApiService.makeApiCall(
                anyString(),
                eq("GET"),
                any(),
                any(),
                any(),
                any(),
                any()
            )
        ).thenReturn(mockResponse)

        // Appeler la méthode callApi
        val route = "/health"
        tempoApiService.callApi(route, "GET")

        // Vérifier que makeApiCall a été appelé avec les bons paramètres
        verify(mockApiService).makeApiCall(
            eq("https://tempoapi-8iou.onrender.com/health"), // Vérifie l'URL
            eq("GET"),  // Vérifie la méthode HTTP
            eq(mapOf("accept" to "application/json")), // Vérifie les headers
            isNull(),  // Le body devrait être nul pour une requête GET
            eq(0), // Vérifie le connectTimeout
            eq(0), // Vérifie le readTimeout
            eq(0)  // Vérifie le writeTimeout
        )
    }

    @Test
    fun `callApi should call makeApiCall with correct parameters for POST`() {
        // Simuler une réponse de la méthode makeApiCall
        val mockResponse = mock(Response::class.java)
        `when`(
            mockApiService.makeApiCall(
                anyString(),
                eq("POST"),
                any(),
                any(),
                any(),
                any(),
                any()
            )
        ).thenReturn(mockResponse)

        // Appeler la méthode callApi avec un body pour POST
        val route = "/create"
        val body = """{"name": "example"}"""
        tempoApiService.callApi(route, "POST", body)

        // Vérifier que makeApiCall a été appelé avec les bons paramètres
        verify(mockApiService).makeApiCall(
            eq("https://tempoapi-8iou.onrender.com/create"), // Vérifie l'URL
            eq("POST"),  // Vérifie la méthode HTTP
            eq(mapOf("accept" to "application/json")), // Vérifie les headers
            eq(body),  // Vérifie le body
            eq(0), // Vérifie le connectTimeout
            eq(0), // Vérifie le readTimeout
            eq(0)  // Vérifie le writeTimeout
        )
    }

    @Test
    fun `callApi should call makeApiCall with default GET method when no method provided`() {
        // Simuler une réponse de la méthode makeApiCall
        val mockResponse = mock(Response::class.java)
        `when`(
            mockApiService.makeApiCall(
                anyString(),
                eq("GET"),
                any(),
                any(),
                any(),
                any(),
                any()
            )
        ).thenReturn(mockResponse)

        // Appeler la méthode callApi sans spécifier de méthode (par défaut "GET")
        val route = "/status"
        tempoApiService.callApi(route)

        // Vérifier que makeApiCall a été appelé avec les bons paramètres
        verify(mockApiService).makeApiCall(
            eq("https://tempoapi-8iou.onrender.com/status"), // Vérifie l'URL
            eq("GET"),  // Vérifie la méthode HTTP
            eq(mapOf("accept" to "application/json")), // Vérifie les headers
            isNull(),  // Le body devrait être nul pour une requête GET
            eq(0), // Vérifie le connectTimeout
            eq(0), // Vérifie le readTimeout
            eq(0)  // Vérifie le writeTimeout
        )
    }

    @Test
    fun `checkHealth should return true when API response is successful`() = runBlocking {
        // Créer un mock de la réponse
        val mockResponse = mock(Response::class.java)
        val mockResponseBody = mock(ResponseBody::class.java)

        // Configurer la réponse mockée
        `when`(mockResponse.isSuccessful).thenReturn(true)
        `when`(mockResponse.code).thenReturn(200)
        `when`(mockResponse.body).thenReturn(mockResponseBody)

        // Créer un mock de ApiService
        val mockApiService = mock(ApiService::class.java)

        // Simuler l'appel API pour renvoyer la réponse simulée
        val headers = mapOf("accept" to "application/json")
        `when`(mockApiService.makeApiCall(eq("https://tempoapi-8iou.onrender.com/health"), eq("GET"), eq(headers), isNull(), eq(0), eq(0), eq(0)))
            .thenReturn(mockResponse)

        // Obtenir l'instance de TempoApiService via getInstance()
        val tempoApiService = TempoApiService.getInstance()

        // Injecter le mockApiService dans l'instance singleton
        tempoApiService.setApiService(mockApiService)

        // Appeler la méthode checkHealth
        val isHealthy = tempoApiService.checkHealth()

        // Vérifier que la méthode makeApiCall a été appelée
        verify(mockApiService).makeApiCall(anyString(), eq("GET"), any(), isNull(), any(), any(), any())

        // Vérification du résultat attendu
        assertTrue(isHealthy)
    }

    @Test
    fun `checkHealth should return false when API response is failure`() = runBlocking {
        // Créer un mock de la réponse
        val mockResponse = mock(Response::class.java)
        val mockResponseBody = mock(ResponseBody::class.java)

        // Configurer la réponse mockée
        `when`(mockResponse.isSuccessful).thenReturn(false)
        `when`(mockResponse.code).thenReturn(500)
        `when`(mockResponse.body).thenReturn(mockResponseBody)

        // Créer un mock de ApiService
        val mockApiService = mock(ApiService::class.java)

        // Simuler l'appel API pour renvoyer la réponse simulée
        val headers = mapOf("accept" to "application/json")
        `when`(mockApiService.makeApiCall(eq("https://tempoapi-8iou.onrender.com/health"), eq("GET"), eq(headers), isNull(), eq(0), eq(0), eq(0)))
            .thenReturn(mockResponse)

        // Obtenir l'instance de TempoApiService via getInstance()
        val tempoApiService = TempoApiService.getInstance()

        // Injecter le mockApiService dans l'instance singleton
        tempoApiService.setApiService(mockApiService)

        // Appeler la méthode checkHealth
        val isHealthy = tempoApiService.checkHealth()

        // Vérifier que la méthode makeApiCall a été appelée
        verify(mockApiService).makeApiCall(anyString(), eq("GET"), any(), isNull(), any(), any(), any())

        // Vérification du résultat attendu
        assertFalse(isHealthy)
    }


}

