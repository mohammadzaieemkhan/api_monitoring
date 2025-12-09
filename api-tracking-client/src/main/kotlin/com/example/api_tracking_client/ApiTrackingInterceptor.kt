package com.example.api_tracking_client

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.servlet.ModelAndView
import java.time.Instant

@Component
class ApiTrackingInterceptor(
    private val apiClient: ApiClient,
    private val rateLimiterService: RateLimiterService
) : HandlerInterceptor {

    private val requestStartTime = ThreadLocal<Long>()

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        requestStartTime.set(System.currentTimeMillis())
        return true
    }

    override fun postHandle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
        modelAndView: ModelAndView?
    ) {
        // Not used for metric capture as final status and response size are available in afterCompletion
    }

    override fun afterCompletion(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
        ex: Exception?
    ) {
        val startTime = requestStartTime.get()
        requestStartTime.remove()

        val endTime = System.currentTimeMillis()
        val latency = endTime - startTime

        val apiEndpoint = request.requestURI
        val requestMethod = request.method
        val statusCode = response.status
        val serviceName = request.contextPath.ifEmpty { "default-service" }

        val responseSize = 0L // Placeholder for now
        val requestSize = 0L // Placeholder for now

        // Check rate limit
        val isRateLimited = !rateLimiterService.tryAcquire(serviceName)

        val apiLog = ApiLog(
            apiEndpoint = apiEndpoint,
            requestMethod = requestMethod,
            requestSize = requestSize,
            responseSize = responseSize,
            statusCode = statusCode,
            timestamp = Instant.ofEpochMilli(startTime),
            latency = latency,
            serviceName = serviceName,
            eventType = if (isRateLimited) "rate-limit-hit" else "api-log"
        )

        apiClient.sendApiLog(apiLog)
    }
}
