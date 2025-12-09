package com.example.central_collector_service

import com.example.central_collector_service.repository.ApiLogRepository
import com.example.central_collector_service.service.AlertingService
import jakarta.annotation.PostConstruct
import jakarta.annotation.PreDestroy
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

@RestController
@RequestMapping("/api/logs")
class LogController(
    private val apiLogRepository: ApiLogRepository,
    private val alertingService: AlertingService
) {

    private val logQueue = ConcurrentLinkedQueue<ApiLog>()
    private val executor = Executors.newSingleThreadScheduledExecutor()

    @PostConstruct
    fun init() {
        // Start a background task to process the queue
        executor.scheduleWithFixedDelay({ processQueue() }, 1, 1, TimeUnit.SECONDS)
    }

    @PostMapping
    fun receiveApiLog(@RequestBody apiLog: ApiLog) {
        println("Received API Log: $apiLog")
        logQueue.add(apiLog)
    }

    private fun processQueue() {
        while (logQueue.isNotEmpty()) {
            val apiLog = logQueue.poll()
            if (apiLog != null) {
                try {
                    apiLogRepository.save(apiLog)
                    alertingService.processApiLogForAlerts(apiLog)
                } catch (e: Exception) {
                    // Handle exceptions, e.g., log the error
                    System.err.println("Failed to process API log: $apiLog, Error: ${e.message}")
                }
            }
        }
    }

    @PreDestroy
    fun destroy() {
        // Shutdown the executor gracefully
        executor.shutdown()
        try {
            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                executor.shutdownNow()
            }
        } catch (e: InterruptedException) {
            executor.shutdownNow()
        }
        // Process any remaining logs in the queue before shutting down
        processQueue()
    }
}

