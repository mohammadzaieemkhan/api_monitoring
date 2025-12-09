package com.example.central_collector_service

import com.example.central_collector_service.repository.ApiLogRepository
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.Instant
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

@SpringBootTest
class CentralCollectorServiceApplicationTests {

	@Autowired
	private lateinit var logController: LogController

	@Autowired
	private lateinit var apiLogRepository: ApiLogRepository

	@Test
	fun contextLoads() {
	}

	@Test
	fun `should handle 50 concurrent log writes without failure`() {
		val numberOfThreads = 50
		val latch = CountDownLatch(numberOfThreads)
		val executorService = Executors.newFixedThreadPool(numberOfThreads)

		// Clear previous logs to ensure a clean test
		apiLogRepository.deleteAll()

		for (i in 0 until numberOfThreads) {
			executorService.submit {
				try {
					val apiLog = ApiLog(
						apiEndpoint = "/test/$i",
						requestMethod = "GET",
						requestSize = 100,
						responseSize = 200,
						statusCode = 200,
						timestamp = Instant.now(),
						latency = (10 + i).toLong(),
						serviceName = "test-service-$i"
					)
					logController.receiveApiLog(apiLog)
				} catch (e: Exception) {
					System.err.println("Error during concurrent log write: ${e.message}")
				} finally {
					latch.countDown()
				}
			}
		}

		// Wait for all threads to complete, with a timeout
		latch.await(10, TimeUnit.SECONDS)

		// Verify that all logs were attempted to be written (basic check)
		// Actual verification would involve checking the database count and content
		// For this simulation, we're primarily testing if no exceptions are thrown.
		Thread.sleep(2000) // Give MongoDB some time to persist
		val logsCount = apiLogRepository.count()
		println("Number of logs after concurrent writes: $logsCount")
		assert(logsCount.toInt() == numberOfThreads) { "Expected $numberOfThreads logs, but found $logsCount" }

		executorService.shutdown()
	}

}
