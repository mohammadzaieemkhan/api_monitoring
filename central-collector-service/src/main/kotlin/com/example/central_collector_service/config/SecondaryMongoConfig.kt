package com.example.central_collector_service.config

import org.springframework.boot.autoconfigure.mongo.MongoProperties
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.MongoDatabaseFactory
import org.springframework.data.mongodb.MongoTransactionManager
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory

@Configuration
class SecondaryMongoConfig {

    @Bean(name = ["secondaryMongoProperties"])
    @ConfigurationProperties(prefix = "spring.data.mongodb.secondary")
    fun secondaryMongoProperties(): MongoProperties = MongoProperties()

    @Bean(name = ["secondaryMongoTemplate"])
    fun secondaryMongoTemplate(): MongoTemplate {
        return MongoTemplate(secondaryMongoDatabaseFactory(secondaryMongoProperties()))
    }

    @Bean(name = ["secondaryMongoDatabaseFactory"])
    fun secondaryMongoDatabaseFactory(mongoProperties: MongoProperties): MongoDatabaseFactory {
        return SimpleMongoClientDatabaseFactory(mongoProperties.uri ?: "mongodb://${mongoProperties.host}:${mongoProperties.port}/${mongoProperties.database}")
    }

    @Bean(name = ["secondaryMongoTransactionManager"])
    fun secondaryMongoTransactionManager(secondaryMongoDatabaseFactory: MongoDatabaseFactory): MongoTransactionManager {
        return MongoTransactionManager(secondaryMongoDatabaseFactory)
    }
}
