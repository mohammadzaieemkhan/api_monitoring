package com.example.central_collector_service.repository

import com.example.central_collector_service.ApiLog
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.Query

@Repository
interface ApiLogRepository : MongoRepository<ApiLog, String> {
    fun findByServiceNameAndApiEndpoint(serviceName: String, apiEndpoint: String, pageable: Pageable): Page<ApiLog>
    fun findByServiceName(serviceName: String, pageable: Pageable): Page<ApiLog>
    fun findByApiEndpoint(apiEndpoint: String, pageable: Pageable): Page<ApiLog>

    @Query("{ 'latency' : { '\$gt' : 500 } }")
    fun countSlowApis(): Long

    @Query("{ 'statusCode' : { '\$gte' : 500, '\$lte' : 599 } }")
    fun countBrokenApis(): Long

    @Query("{ 'eventType' : 'rate-limit-hit' }")
    fun countRateLimitViolations(): Long
}
