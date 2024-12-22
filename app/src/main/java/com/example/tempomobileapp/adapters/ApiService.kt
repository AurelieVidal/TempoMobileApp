package com.example.tempomobileapp.adapters

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import java.io.IOException

class ApiService private constructor() {
    // Client par défaut, peut être remplacé pour les tests
    private var client: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(10, java.util.concurrent.TimeUnit.SECONDS)
        .readTimeout(10, java.util.concurrent.TimeUnit.SECONDS)
        .writeTimeout(10, java.util.concurrent.TimeUnit.SECONDS)
        .build()

    /**
     * Permet de définir un client personnalisé (utile pour les tests).
     * @param customClient Le client OkHttp à utiliser.
     */
    fun setClient(customClient: OkHttpClient) {
        client = customClient
    }

    /**
     * Fonction générique pour faire un appel API avec timeouts configurables.
     * @param url URL complète de l'API.
     * @param method Méthode HTTP (GET, POST, PUT, DELETE).
     * @param headers Headers optionnels.
     * @param body Corps de la requête pour les méthodes POST/PUT.
     * @param connectTimeout Timeout de connexion en secondes (par défaut 10s).
     * @param readTimeout Timeout de lecture en secondes (par défaut 10s).
     * @param writeTimeout Timeout d'écriture en secondes (par défaut 10s).
     * @return Réponse de type Response ou null en cas d'erreur.
     */
    fun makeApiCall(
        url: String,
        method: String = "GET",
        headers: Map<String, String> = emptyMap(),
        body: String? = null,
        connectTimeout: Long = 10,
        readTimeout: Long = 10,
        writeTimeout: Long = 10
    ): Response? {
        // Si les timeouts sont modifiés, créer un nouveau client
        if (connectTimeout != 10L || readTimeout != 10L || writeTimeout != 10L) {
            client = client.newBuilder()
                .connectTimeout(connectTimeout, java.util.concurrent.TimeUnit.SECONDS)
                .readTimeout(readTimeout, java.util.concurrent.TimeUnit.SECONDS)
                .writeTimeout(writeTimeout, java.util.concurrent.TimeUnit.SECONDS)
                .build()
        }

        val requestBuilder = Request.Builder().url(url)

        // Ajouter les headers
        headers.forEach { (key, value) ->
            requestBuilder.addHeader(key, value)
        }

        // Définir la méthode
        when (method.uppercase()) {
            "GET" -> requestBuilder.get()
            "POST" -> requestBuilder.post(body?.toRequestBody() ?: "".toRequestBody())
            "PUT" -> requestBuilder.put(body?.toRequestBody() ?: "".toRequestBody())
            "DELETE" -> requestBuilder.delete()
            else -> throw IllegalArgumentException("Méthode HTTP non supportée : $method")
        }

        // Construire la requête
        val request = requestBuilder.build()

        // Exécuter la requête
        return try {
            val response = client.newCall(request).execute()
            if (!response.isSuccessful) {
                // Lever une exception pour une réponse non réussie
                throw IOException("Erreur HTTP ${response.code}: ${response.message}")
            }
            response
        } catch (e: IOException) {
            e.printStackTrace()
            Log.e("ApiService", "Erreur lors de l'appel API : ${e.message}")
            null
        }
    }

    // Singleton
    companion object {
        private var instance: ApiService? = null
        fun getInstance(): ApiService {
            if (instance == null) {
                instance = ApiService()
            }
            return instance!!
        }
    }
}
