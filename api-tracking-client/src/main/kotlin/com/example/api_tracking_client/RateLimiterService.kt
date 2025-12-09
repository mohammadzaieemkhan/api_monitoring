package com.example.api_tracking_client

import com.google.common.util.concurrent.RateLimiter
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.stereotype.Service
import java.util.concurrent.ConcurrentHashMap

@Service
class RateLimiterService(private val rateLimitProperties: RateLimitProperties) {

    private val rateLimiters = ConcurrentHashMap<String, RateLimiter>()

    @ConstructorBinding
    @ConfigurationProperties(prefix = "monitoring.rate-limit")
    data class RateLimitProperties(
        val defaultRate: Double = 100.0, // Default to 100 requests/second
        val services: Map<String, Double> = emptyMap() // Per-service overrides
    )

    fun tryAcquire(serviceName: String): Boolean {
        val limiter = rateLimiters.computeIfAbsent(serviceName) {
            val rate = rateLimitProperties.services[serviceName] ?: rateLimitProperties.defaultRate
            RateLimiter.create(rate)
        }
        return limiter.tryAcquire()
    }
}
