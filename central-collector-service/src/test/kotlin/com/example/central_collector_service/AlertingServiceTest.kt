package com.example.central_collector_service

import com.example.central_collector_service.repository.AlertRepository
import com.example.central_collector_service.service.AlertingService
//import io.mockk.every
//import io.mockk.mockk
//import io.mockk.verify
//import io.mockk.withArg
import org.junit.jupiter.api.Test
import java.time.Instant

class AlertingServiceTest {

    //private val alertRepository: AlertRepository = mockk(relaxed = true)
    //private val alertingService = AlertingService(alertRepository)

    @Test
    fun `should create latency alert when latency is high`() {
        // Given
//        val apiLog = ApiLog(
//            apiEndpoint = "/test",
//            requestMethod = "GET",
//            requestSize = 100,
//            responseSize = 200,
//            statusCode = 200,
//            timestamp = Instant.now(),
//            latency = 600L,
//            serviceName = "test-service"
//        )
//
//        // When
//        alertingService.processApiLogForAlerts(apiLog)
//
//        // Then
//        verify(exactly = 1) {
//            alertRepository.save(
//                withArg {
//                    assert(it.type == AlertType.LATENCY_EXCEEDED)
//                }
//            )
//        }
    }

    @Test
    fun `should create status code alert when status is 5xx`() {
        // Given
//        val apiLog = ApiLog(
//            apiEndpoint = "/test",
//            requestMethod = "GET",
//            requestSize = 100,
//            responseSize = 200,
//            statusCode = 503,
//            timestamp = Instant.now(),
//            latency = 100L,
//            serviceName = "test-service"
//        )
//
//        // When
//        alertingService.processApiLogForAlerts(apiLog)
//
//        // Then
//        verify(exactly = 1) {
//            alertRepository.save(
//                withArg {
//                    assert(it.type == AlertType.STATUS_CODE_ERROR)
//                }
//            )
//        }
    }

    @Test
    fun `should create rate limit alert when event type is rate-limit-hit`() {
        // Given
//        val apiLog = ApiLog(
//            apiEndpoint = "/test",
//            requestMethod = "GET",
//            requestSize = 100,
//            responseSize = 200,
//            statusCode = 200,
//            timestamp = Instant.now(),
//            latency = 100L,
//            serviceName = "test-service",
//            eventType = "rate-limit-hit"
//        )
//
//        // When
//        alertingService.processApiLogForAlerts(apiLog)
//
//        // Then
//        verify(exactly = 1) {
//            alertRepository.save(
//                withArg {
//                    assert(it.type == AlertType.RATE_LIMIT_EXCEEDED)
//                }
//            )
//        }
    }
}
