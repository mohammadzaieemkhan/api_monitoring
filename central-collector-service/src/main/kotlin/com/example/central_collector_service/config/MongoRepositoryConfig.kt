package com.example.central_collector_service.config

import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.FilterType
import com.example.central_collector_service.repository.ApiLogRepository
import com.example.central_collector_service.repository.ServiceMetadataRepository
import com.example.central_collector_service.repository.AlertRepository

@Configuration
@EnableMongoRepositories(
    basePackages = ["com.example.central_collector_service.repository"],
    mongoTemplateRef = "primaryMongoTemplate",
    includeFilters = [
        ComponentScan.Filter(
            type = FilterType.ASSIGNABLE_TYPE,
            classes = [ApiLogRepository::class]
        )
    ]
)
class PrimaryMongoRepositoryConfig

@Configuration
@EnableMongoRepositories(
    basePackages = ["com.example.central_collector_service.repository"],
    mongoTemplateRef = "secondaryMongoTemplate",
    includeFilters = [
        ComponentScan.Filter(
            type = FilterType.ASSIGNABLE_TYPE,
            classes = [
                ServiceMetadataRepository::class,
                AlertRepository::class
            ]
        )
    ]
)
class SecondaryMongoRepositoryConfig
