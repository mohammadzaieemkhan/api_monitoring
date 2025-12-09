package com.example.central_collector_service.config

import org.springframework.boot.autoconfigure.mongo.MongoProperties
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.data.mongodb.MongoDatabaseFactory
import org.springframework.data.mongodb.MongoTransactionManager
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory

@Configuration
class PrimaryMongoConfig {

    @Primary
    @Bean(name = ["primaryMongoProperties"])
    @ConfigurationProperties(prefix = "spring.data.mongodb.primary")
    fun primaryMongoProperties(): MongoProperties = MongoProperties()

    @Primary
    @Bean(name = ["primaryMongoTemplate"])
    fun primaryMongoTemplate(): MongoTemplate {
        return MongoTemplate(primaryMongoDatabaseFactory(primaryMongoProperties()))
    }

    @Primary
    @Bean(name = ["primaryMongoDatabaseFactory"])
    fun primaryMongoDatabaseFactory(mongoProperties: MongoProperties): MongoDatabaseFactory {
        return SimpleMongoClientDatabaseFactory(mongoProperties.uri ?: "mongodb://${mongoProperties.host}:${mongoProperties.port}/${mongoProperties.database}")
    }

    @Primary
    @Bean(name = ["primaryMongoTransactionManager"])
    fun primaryMongoTransactionManager(primaryMongoDatabaseFactory: MongoDatabaseFactory): MongoTransactionManager {
        return MongoTransactionManager(primaryMongoDatabaseFactory)
    }
}
