package com.example.central_collector_service

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document("service_metadata")
data class ServiceMetadata(
    @Id
    val id: String, // Service name will be the ID
    val rateLimit: Double? = null,
    // Other metadata fields can be added here
)
