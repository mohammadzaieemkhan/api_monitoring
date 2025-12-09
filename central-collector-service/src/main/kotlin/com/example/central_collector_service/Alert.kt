package com.example.central_collector_service

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

enum class AlertType {
    LATENCY_EXCEEDED,
    STATUS_CODE_ERROR,
    RATE_LIMIT_EXCEEDED
}

enum class AlertStatus {
    ACTIVE,
    RESOLVED
}

@Document("alerts")
data class Alert(
    @Id
    val id: String? = null,
    val type: AlertType,
    val message: String,
    val timestamp: Instant,
    val serviceName: String,
    val apiEndpoint: String,
    val status: AlertStatus = AlertStatus.ACTIVE
)
