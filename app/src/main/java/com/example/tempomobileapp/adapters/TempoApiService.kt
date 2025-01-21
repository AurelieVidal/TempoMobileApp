package com.example.tempomobileapp.adapters

import android.util.Log
import com.example.tempomobileapp.exceptions.ApiException
import com.example.tempomobileapp.models.SecurityQuestion
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Response
import java.io.IOException

/**
 * ApiService is a singleton class that provides a Tempo API calling mechanism.
 */
class TempoApiService private constructor(
    private val dispatcher: kotlinx.coroutines.CoroutineDispatcher = Dispatchers.IO
) {

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

    suspend fun checkHealth(): Boolean = withContext(dispatcher) {
        val response = callApi(
            route = "/health",
            method = "GET"
        )

        val isSuccessful = response?.isSuccessful == true
        if (isSuccessful) {
            Log.d("App", "Réponse API : ${response?.body?.string()}")
        } else {
            Log.e("App", "Erreur API : ${response?.code}")
        }
        isSuccessful
    }

    suspend fun getSecurityQuestions(): List<SecurityQuestion> = withContext(dispatcher) {
        val response = callApi(
            route = "/security/question/random/3",
            method = "GET"
        )

        if (response?.isSuccessful == true && response.body != null) {
            val jsonString = response.body?.string()
            return@withContext parseQuestions(jsonString ?: "")
        } else {
            Log.e("App", "Erreur API : ${response?.code}")
            throw ApiException("Unable to get security questions")
        }

        return@withContext emptyList()
    }

    fun parseQuestions(jsonString: String): List<SecurityQuestion> {
        val gson = Gson()
        val jsonObject = gson.fromJson(jsonString, JsonObject::class.java)

        if (!jsonObject.has("questions")) {
            throw JsonSyntaxException("Unexpected response format")
        }

        val questionsJsonArray = jsonObject.getAsJsonArray("questions")

        val type = object : TypeToken<List<SecurityQuestion>>() {}.type
        return gson.fromJson(questionsJsonArray, type)
    }

    suspend fun checkIfUserAvailable(username: String): Boolean = withContext(dispatcher) {
        try {
            val response = callApi(
                route = "/users/$username",
                method = "GET"
            )

            if (response == null) {
                Log.e("App", "Réponse API null")
                throw ApiException("Erreur inattendue de l'API : Réponse null")
            }

            return@withContext when (response.code) {
                404 -> true
                200 -> false
                else -> {
                    Log.e("App", "Code de réponse inattendu : ${response.code}")
                    throw ApiException("Erreur inattendue de l'API : Code ${response.code}")
                }
            }
        } catch (e: IOException) {
            Log.e("App", "Erreur lors de l'appel API : ${e.message}")
            throw IllegalStateException("Erreur inattendue de l'API : ${e.message}", e)
        }
    }

    suspend fun createUser(userCreate: UserCreate) = withContext(dispatcher) {
        val password = userCreate.password.trimEnd()
        val email = userCreate.email.trimEnd()
        val phone = userCreate.phoneNumber.replace(" ", "")
        val questions = userCreate.securityQuestions.map { question ->
            question.copy(response = question.response.trimEnd())
        }

        val payload = mapOf(
            "username" to userCreate.username,
            "password" to password,
            "email" to email,
            "phone" to phone,
            "device" to userCreate.deviceId,
            "questions" to questions.map { question ->
                mapOf(
                    "questionId" to question.id,
                    "response" to question.response
                )
            }
        )

        val response = callApi(
            route = "/users",
            method = "POST",
            body = Gson().toJson(payload)
        )

        val isSuccessful = response?.isSuccessful == true
        if (isSuccessful) {
            Log.d("App", "Réponse API : ${response?.body?.string()}")
        } else {
            Log.e("App", "Erreur API : ${response?.code}")
        }
    }
}

/**
 * Data class that contains all necessary information to create a user
 */
data class UserCreate(
    val username: String,
    val password: String,
    val email: String,
    val phoneNumber: String,
    val securityQuestions: List<SecurityQuestion>,
    val deviceId: String,
)
