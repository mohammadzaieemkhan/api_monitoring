package com.example.central_collector_service.repository

import com.example.central_collector_service.Alert
import com.example.central_collector_service.AlertStatus
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface AlertRepository : MongoRepository<Alert, String> {
    fun findByStatus(status: AlertStatus, pageable: Pageable): Page<Alert>
}
