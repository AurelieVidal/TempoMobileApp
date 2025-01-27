package com.example.tempomobileapp.adapters

import android.util.Log
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import java.io.IOException

/**
 * ApiService is a singleton class that provides a generic API calling mechanism with configurable timeouts.
 * It supports setting a custom OkHttpClient for testing and handles various HTTP methods.
 */
class ApiService private constructor() {

    private var client: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(10, java.util.concurrent.TimeUnit.SECONDS)
        .readTimeout(10, java.util.concurrent.TimeUnit.SECONDS)
        .writeTimeout(10, java.util.concurrent.TimeUnit.SECONDS)
        .build()

    /**
     * Allows setting a custom OkHttpClient (useful for testing).
     * @param customClient The OkHttp client to use.
     */
    fun setClient(customClient: OkHttpClient) {
        client = customClient
    }

    /**
     * Data needed to do a request
     */
    data class ApiRequest(
        val url: String,
        val method: String = "GET",
        val headers: Map<String, String> = emptyMap(),
        val body: String? = null,
        val timeout: Long = 10
    )

    /**
     * Generic function to make an API call with configurable timeouts.
     * @param url Complete API URL.
     * @param method HTTP method (GET, POST, PUT, DELETE).
     * @param headers Optional headers.
     * @param body Request body for POST/PUT methods.
     * @param timeout timeout in seconds (default 10s).
     * @return Response of type Response or null in case of an error.
     */
    fun makeApiCall(
        apiRequest: ApiRequest
    ): Response? {
        configureTimeouts(apiRequest.timeout)

        val request = buildRequest(apiRequest.url, apiRequest.method, apiRequest.headers, apiRequest.body)

        return executeRequest(request)
    }

    private fun configureTimeouts(timeout: Long) {
        if (timeout != 10L) {
            client = client.newBuilder()
                .connectTimeout(timeout, java.util.concurrent.TimeUnit.SECONDS)
                .readTimeout(timeout, java.util.concurrent.TimeUnit.SECONDS)
                .writeTimeout(timeout, java.util.concurrent.TimeUnit.SECONDS)
                .build()
        }
    }

    private fun buildRequest(
        url: String,
        method: String,
        headers: Map<String, String>,
        body: String?
    ): Request {
        val requestBuilder = Request.Builder().url(url)

        headers.forEach { (key, value) ->
            requestBuilder.addHeader(key, value)
        }

        when (method.uppercase()) {
            "GET" -> requestBuilder.get()
            "POST" -> {
                requestBuilder.addHeader("Content-Type", "application/json")
                requestBuilder.post(body?.toRequestBody("application/json".toMediaType()) ?: "".toRequestBody())
            }
            "PUT" -> {
                requestBuilder.addHeader("Content-Type", "application/json")
                requestBuilder.put(body?.toRequestBody("application/json".toMediaType()) ?: "".toRequestBody())
            }
            "DELETE" -> requestBuilder.delete()
            else -> throw IllegalArgumentException("Unsupported HTTP method: $method")
        }

        return requestBuilder.build()
    }

    private fun executeRequest(request: Request): Response? {
        Log.d("App", "Executing request: $request")
        var attempt = 0
        val maxRetries = 5
        var lastResponse: Response? = null

        while (attempt < maxRetries) {
            try {
                val response = client.newCall(request).execute()
                if (response.isSuccessful) {
                    return response
                } else {
                    lastResponse = response
                    Log.e("App", "HTTP error ${response.code}: ${response.message}")
                }
            } catch (e: IOException) {
                Log.e("App", "Error during API call (attempt ${attempt + 1}): ${e.message}")
            }

            attempt++
            if (attempt < maxRetries) {
                Thread.sleep(1000)
            }
        }

        Log.e("App", "API call failed after $maxRetries attempts")
        return lastResponse

    }

    /**
     * Singleton companion object for accessing the single instance of ApiService.
     */
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
