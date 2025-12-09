package com.example.api_tracking_client

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.postForEntity

@Service
class ApiClient(
    private val restTemplate: RestTemplate,
    @Value("\${collector.service.url}") private val collectorServiceUrl: String
) {

    fun sendApiLog(apiLog: ApiLog) {
        try {
            val response = restTemplate.postForEntity<String>("$collectorServiceUrl/api/logs", apiLog)
            if (response.statusCode.is2xxSuccessful) {
                println("API Log sent successfully: $apiLog")
            } else {
                System.err.println("Failed to send API Log. Status: ${response.statusCode}, Body: ${response.body}")
            }
        } catch (e: Exception) {
            System.err.println("Error sending API Log: ${e.message}")
        }
    }
}
