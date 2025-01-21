package com.example.tempomobileapp.adapters

import android.util.Log
import com.example.tempomobileapp.exceptions.ApiException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Response
import java.security.MessageDigest

/**
 * ApiService is a singleton class that provides a Tempo API calling mechanism.
 */
class HIPBApiService private constructor(
    private val dispatcher: kotlinx.coroutines.CoroutineDispatcher = Dispatchers.IO
) {

    private val baseUrl = "https://api.pwnedpasswords.com"
    private var apiService: ApiService = ApiService.getInstance()

    fun setApiService(mockApiService: ApiService) {
        apiService = mockApiService
    }

    /**
     * Calls a specific route from the HIPB API.
     * @param route The API route.
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
                body = body,
            )
        )
    }

    /**
     * Singleton pattern for TempoApiService.
     * Ensures that only one instance of this class exists.
     */
    companion object {
        @Volatile
        private var INSTANCE: HIPBApiService? = null

        fun getInstance(): HIPBApiService {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: HIPBApiService().also { INSTANCE = it }
            }
        }
    }

    suspend fun checkPassword(password: String): Boolean = withContext(dispatcher) {
        val digest = MessageDigest.getInstance("SHA-1")
        val hashBytes = digest.digest(password.toByteArray())
        val hashed = hashBytes.joinToString("") { "%02x".format(it) }

        val hashBeginning = hashed.substring(0, 5)
        val hashEnd = hashed.substring(5).uppercase()

        Log.d("App", "hashed : $hashed")

        val response = callApi(
            route = "/range/$hashBeginning",
            method = "GET"
        )

        var weakPassword = false

        val isSuccessful = response?.isSuccessful == true
        if (isSuccessful) {
            val responseBody = response?.body?.string()
            val hashes = mutableListOf<String>()

            responseBody?.let {
                val regex = Regex("([A-F0-9]{35})(?=:)")
                val matchResults = regex.findAll(it)

                matchResults.forEach { match ->
                    hashes.add(match.value)
                }
            }

            if (hashes.contains(hashEnd)) {
                weakPassword = true
            }
        } else {
            Log.e("App", "Erreur API : ${response?.code}")
            throw ApiException("Erreur API : ${response?.code}")
        }

        weakPassword
    }
}
