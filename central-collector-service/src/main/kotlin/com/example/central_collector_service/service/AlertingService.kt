package com.example.central_collector_service.service

import com.example.central_collector_service.Alert
import com.example.central_collector_service.AlertStatus
import com.example.central_collector_service.AlertType
import com.example.central_collector_service.ApiLog
import com.example.central_collector_service.repository.AlertRepository
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class AlertingService(
    private val alertRepository: AlertRepository
) {
    // Define alert thresholds
    private val LATENCY_THRESHOLD = 500L // milliseconds
    private val STATUS_CODE_ERROR_RANGE_START = 500
    private val STATUS_CODE_ERROR_RANGE_END = 599

    fun processApiLogForAlerts(apiLog: ApiLog) {
        val alerts = mutableListOf<Alert>()

        // Rule 1: API latency > 500ms
        if (apiLog.latency > LATENCY_THRESHOLD) {
            alerts.add(
                Alert(
                    type = AlertType.LATENCY_EXCEEDED,
                    message = "High latency detected for API: ${apiLog.apiEndpoint} (${apiLog.latency}ms)",
                    timestamp = Instant.now(),
                    serviceName = apiLog.serviceName,
                    apiEndpoint = apiLog.apiEndpoint
                )
            )
        }

        // Rule 2: Status code is 5xx
        if (apiLog.statusCode in STATUS_CODE_ERROR_RANGE_START..STATUS_CODE_ERROR_RANGE_END) {
            alerts.add(
                Alert(
                    type = AlertType.STATUS_CODE_ERROR,
                    message = "5xx status code detected for API: ${apiLog.apiEndpoint} (Status: ${apiLog.statusCode})",
                    timestamp = Instant.now(),
                    serviceName = apiLog.serviceName,
                    apiEndpoint = apiLog.apiEndpoint
                )
            )
        }

        // Rule 3: Rate limit exceeded
        if (apiLog.eventType == "rate-limit-hit") {
            alerts.add(
                Alert(
                    type = AlertType.RATE_LIMIT_EXCEEDED,
                    message = "Rate limit hit for service: ${apiLog.serviceName} on API: ${apiLog.apiEndpoint}",
                    timestamp = Instant.now(),
                    serviceName = apiLog.serviceName,
                    apiEndpoint = apiLog.apiEndpoint
                )
            )
        }

        alerts.forEach {
            alertRepository.save(it)
            println("ALERT FIRED: ${it.message}")
        }
    }
}
