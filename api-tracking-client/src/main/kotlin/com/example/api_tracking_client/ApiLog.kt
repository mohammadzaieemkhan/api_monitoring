package com.example.api_tracking_client

import java.time.Instant

data class ApiLog(
    val apiEndpoint: String,
    val requestMethod: String,
    val requestSize: Long,
    val responseSize: Long,
    val statusCode: Int,
    val timestamp: Instant,
    val latency: Long, // in milliseconds
    val serviceName: String,
    val eventType: String = "api-log"
)
