package com.example.central_collector_service.repository

import com.example.central_collector_service.ServiceMetadata
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface ServiceMetadataRepository : MongoRepository<ServiceMetadata, String>
