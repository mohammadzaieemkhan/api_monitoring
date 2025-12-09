package com.example.central_collector_service

import com.example.central_collector_service.repository.ApiLogRepository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/dashboard")
class DashboardController(
    private val apiLogRepository: ApiLogRepository
) {

    @GetMapping("/slow-api-count")
    fun getSlowApiCount(): Long {
        return apiLogRepository.countSlowApis()
    }

    @GetMapping("/broken-api-count")
    fun getBrokenApiCount(): Long {
        return apiLogRepository.countBrokenApis()
    }

    @GetMapping("/rate-limit-violations")
    fun getRateLimitViolations(): Long {
        return apiLogRepository.countRateLimitViolations()
    }
}
