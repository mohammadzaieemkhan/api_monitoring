package com.example.central_collector_service

import com.example.central_collector_service.Alert
import com.example.central_collector_service.AlertStatus
import com.example.central_collector_service.repository.AlertRepository
import org.springframework.data.domain.PageRequest
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/query/alerts")
class AlertQueryController(
    private val alertRepository: AlertRepository
) {

    @GetMapping
    fun getAllAlerts(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int,
        @RequestParam(required = false) status: AlertStatus?
    ): List<Alert> {
        val pageable = PageRequest.of(page, size)
        return if (status != null) {
            alertRepository.findByStatus(status, pageable).content
        } else {
            alertRepository.findAll(pageable).content
        }
    }

    @PostMapping("/{id}/resolve")
    fun resolveAlert(@PathVariable id: String): Alert {
        val alert = alertRepository.findById(id).orElseThrow { RuntimeException("Alert not found") }
        val updatedAlert = alert.copy(status = AlertStatus.RESOLVED)
        return alertRepository.save(updatedAlert)
    }
}
