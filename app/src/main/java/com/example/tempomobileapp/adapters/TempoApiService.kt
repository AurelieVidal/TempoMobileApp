package com.example.tempomobileapp.adapters

import android.util.Log
import com.example.tempomobileapp.models.SecurityQuestion
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import okhttp3.Response

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
        Log.d("App", "Récupération des questions de sécurité")

        val response = callApi(
            route = "/security/question/random/3",
            method = "GET"
        )

        // Vérifie si la réponse est réussie
        if (response?.isSuccessful == true && response.body != null) {
            val jsonString = response.body?.string()

            //Log.d("App", "Réponse API : $jsonString")
            val questions = parseQuestions(jsonString ?: "")
            Log.d("App", "Questions récupérées : $questions")
            // Parse la réponse JSON
            return@withContext parseQuestions(jsonString ?: "")
        } else {
            Log.e("App", "Erreur API : ${response?.code}")
        }

        // Renvoie une liste vide en cas d'erreur
        return@withContext emptyList()
    }

    fun parseQuestions(jsonString: String): List<SecurityQuestion> {
        val gson = Gson()
        val jsonObject = gson.fromJson(jsonString, JsonObject::class.java) // Parse l'objet JSON

        // Vérifie si la clé "questions" existe
        if (!jsonObject.has("questions")) {
            throw JsonSyntaxException("Unexpected response format")
        }

        // Récupère la liste sous forme de JsonArray
        val questionsJsonArray = jsonObject.getAsJsonArray("questions")

        // Convertit le JsonArray en List<SecurityQuestion>
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
                throw IllegalStateException("Erreur inattendue de l'API : Réponse null")
            }

            Log.d("App", "Réponse API : ${response.body?.string()}")

            return@withContext when (response.code) {
                404 -> true
                200 -> false
                else -> {
                    Log.e("App", "Code de réponse inattendu : ${response.code}")
                    throw IllegalStateException("Erreur inattendue de l'API : Code ${response.code}")
                }
            }
        } catch (e: Exception) {
            Log.e("App", "Erreur lors de l'appel API : ${e.message}")
            throw IllegalStateException("Erreur inattendue de l'API : ${e.message}", e)
        }
    }

    suspend fun createUser(username: String, password: String, email: String, phoneNumber: String, securityQuestions: List<SecurityQuestion>, deviceId: String) = withContext(dispatcher) {


        var password = password.trimEnd()
        var email = email.trimEnd()
        var phone = phoneNumber.replace(" ", "")
        val questions = securityQuestions.map { question ->
            question.copy(response = question.response.trimEnd())
        }



        val payload = mapOf(
            "username" to username,
            "password" to password,
            "email" to email,
            "phone" to phone,
            "device" to deviceId,
            "questions" to questions.map { question ->
                mapOf(
                    "questionId" to question.id,
                    "response" to question.response
                )
            } // Exemple si `SecurityQuestion` a une méthode `toMap()`
        )

        Log.d("App", "Payload : $payload")


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


