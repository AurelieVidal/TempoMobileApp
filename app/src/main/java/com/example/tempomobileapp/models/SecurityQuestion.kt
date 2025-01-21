package com.example.tempomobileapp.models

/**
 * Describe the relation between a user and it's security questions
 */
data class SecurityQuestion(
    val id: Int,
    val question: String,
    var response: String
)
