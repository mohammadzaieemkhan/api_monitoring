package com.example.api_tracking_client

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties

@SpringBootApplication
@EnableConfigurationProperties(RateLimiterService.RateLimitProperties::class)
class ApiTrackingClientApplication

fun main(args: Array<String>) {
	runApplication<ApiTrackingClientApplication>(*args)
}
