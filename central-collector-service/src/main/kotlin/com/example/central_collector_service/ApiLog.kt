package com.example.central_collector_service

import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

@Document("api_logs")
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
