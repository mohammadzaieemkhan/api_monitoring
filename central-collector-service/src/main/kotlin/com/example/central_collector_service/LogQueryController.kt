package com.example.central_collector_service

import com.example.central_collector_service.repository.ApiLogRepository
import org.springframework.data.domain.PageRequest
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/query/logs")
class LogQueryController(
    private val apiLogRepository: ApiLogRepository
) {

    @GetMapping
    fun getAllApiLogs(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int,
        @RequestParam(required = false) serviceName: String?,
        @RequestParam(required = false) apiEndpoint: String?
    ): List<ApiLog> {
        val pageable = PageRequest.of(page, size)
        return when {
            serviceName != null && apiEndpoint != null -> apiLogRepository.findByServiceNameAndApiEndpoint(serviceName, apiEndpoint, pageable).content
            serviceName != null -> apiLogRepository.findByServiceName(serviceName, pageable).content
            apiEndpoint != null -> apiLogRepository.findByApiEndpoint(apiEndpoint, pageable).content
            else -> apiLogRepository.findAll(pageable).content
        }
    }
}
