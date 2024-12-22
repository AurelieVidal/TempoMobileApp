package com.example.tempomobileapp.adapters

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Response

/**
 * ApiService is a singleton class that provides a Tempo API calling mechanism.
 */
class TempoApiService private constructor() {

    private val baseUrl = "https://tempoapi-8iou.onrender.com"
    private var apiService: ApiService = ApiService.getInstance()

    fun setApiService(mockApiService: ApiService) {
        apiService = mockApiService
    }

    /**
     * Calls a specific route from the Tempo API.
     * @param route The API route (e.g., "/health").
     * @param method The HTTP method (GET, POST, PUT, DELETE).
     * @param body The request body for POST/PUT methods.
     * @return A Response object or null in case of an error.
     */
    fun callApi(
        route: String,
        method: String = "GET",
        body: String? = null,
    ): Response? {
        val url = "$baseUrl$route"

        if (body != null) {
            Log.e("App", body)
        }

        return apiService.makeApiCall(
            ApiService.ApiRequest(
                url = url,
                method = method,
                headers = mapOf("accept" to "application/json"),
                body = body,
                timeout = 0,
            )
        )
    }

    /**
     * Singleton pattern for TempoApiService.
     * Ensures that only one instance of this class exists.
     */
    companion object {
        @Volatile
        private var INSTANCE: TempoApiService? = null

        fun getInstance(): TempoApiService {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: TempoApiService().also { INSTANCE = it }
            }
        }
    }

    suspend fun checkHealth(): Boolean = withContext(Dispatchers.IO) {
        val response = callApi(
            route = "/health",
            method = "GET"
        )

        if (response?.isSuccessful == true) {
            Log.d("App", "Réponse API : ${response.body?.string()}")
            return@withContext true
        } else {
            Log.e("App", "Erreur API : ${response?.code}")
            return@withContext false
        }
    }
}
