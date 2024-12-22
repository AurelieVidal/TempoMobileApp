package com.example.tempomobileapp.adapters

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response

class TempoApiService private constructor() {

    private val BASE_URL = "https://tempoapi-8iou.onrender.com"
    private var apiService: ApiService = ApiService.getInstance()

    // Fonction pour injecter un mock ApiService
    fun setApiService(mockApiService: ApiService) {
        apiService = mockApiService
    }

    /**
     * Appelle une route spécifique de l'API Tempo.
     * @param route La route de l'API (ex: "/health").
     * @param method La méthode HTTP (GET, POST, PUT, DELETE).
     * @param body Corps de la requête pour les méthodes POST/PUT.
     * @return Réponse de type Response ou null en cas d'erreur.
     */
    fun callApi(
        route: String,
        method: String = "GET",
        body: String? = null,
    ): Response? {
        val url = "$BASE_URL$route" // Construire l'URL complète

        if (body != null) {
            Log.e("App", body)
        }

        // Effectuer l'appel API en utilisant ApiService.makeApiCall
        return apiService.makeApiCall(
            url = url,
            method = method,
            headers = mapOf("accept" to "application/json"),
            body = body,
            connectTimeout = 0,
            readTimeout = 0,
            writeTimeout = 0
        )
    }

    // Singleton
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

        Log.d("App", "Réponse API : ${response}")

        if (response?.isSuccessful == true) {
            Log.d("App", "Réponse API : ${response.body?.string()}")
            return@withContext true
        } else {
            Log.e("App", "Erreur API : ${response?.code}")
            return@withContext false
        }
    }
}


