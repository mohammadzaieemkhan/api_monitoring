package com.example.central_collector_service

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth")
class AuthController {

    @PostMapping("/login")
    fun login(@RequestBody authRequest: AuthRequest): AuthResponse {
        // For demonstration purposes, just return a dummy token
        // In a real application, you would validate credentials and generate a real JWT.
        if (authRequest.username == "user" && authRequest.password == "password") {
            return AuthResponse("dummy-jwt-token-for-${authRequest.username}")
        }
        throw RuntimeException("Invalid credentials")
    }
}
