package com.example.central_collector_service

data class AuthRequest(
    val username: String,
    val password: String
)

data class AuthResponse(
    val token: String
)
