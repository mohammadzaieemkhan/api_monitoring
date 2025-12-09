# API Monitoring & Observability Platform

This project is a complete platform that tracks API requests across multiple microservices, stores their performance metrics, analyzes issues, and displays them on a dashboard.

## Architecture

The platform consists of two main components:

-   **API Tracking Client (`api-tracking-client`)**: A reusable library/interceptor that can be added to any Spring Boot service. It tracks API requests, captures metrics, and sends them to the Central Collector Service.
-   **Central Collector Service (`central-collector-service`)**: A Spring Boot application that receives logs from multiple microservices, stores them in MongoDB, analyzes them for alerts, and provides a REST API for a frontend dashboard.

## Modules

### API Tracking Client (`api-tracking-client`)

This module is a Spring Boot starter that can be included as a dependency in any Spring Boot application. It provides an interceptor that automatically captures and sends API metrics.

**Features:**

-   Captures API endpoint, request method, request/response size, status code, timestamp, and latency.
-   Sends captured logs to the Central Collector Service via REST.
-   Includes a per-service rate limiter that can be configured via `application.yaml`.

### Central Collector Service (`central-collector-service`)

This module is a standalone Spring Boot application that acts as the central hub for collecting and analyzing API metrics.

**Features:**

-   Receives API logs from multiple services.
-   Stores logs and metadata in two separate MongoDB databases.
-   Analyzes logs for alerting rules (high latency, 5xx status codes, rate limit hits).
-   Provides a REST API for querying logs, alerts, and managing alerts.

## How to Build and Run

### Prerequisites

-   Java 17
-   Maven
-   MongoDB

### Build

To build the project, run the following command from the root directory:

```bash
mvn clean install
```

### Run

**1. Central Collector Service:**

Navigate to the `central-collector-service` directory and run:

```bash
mvn spring-boot:run
```

The service will be available at `http://localhost:8081`.

**2. API Tracking Client:**

The `api-tracking-client` is a library and is meant to be included as a dependency in another Spring Boot application.

To use it:

1.  Add the `api-tracking-client` as a Maven dependency to your project.
2.  Configure the `collector.service.url` in your `application.properties`:
    ```properties
    collector.service.url=http://localhost:8081
    ```

## Database Schemas

The platform uses two separate MongoDB databases:

-   **`api-logs` (Primary)**: Stores raw API logs (`api_logs` collection).
-   **`api-metadata` (Secondary)**: Stores metadata such as alerts (`alerts` collection) and service-specific configurations (`service_metadata` collection).

## Dual MongoDB Setup

The `central-collector-service` is configured to connect to two MongoDB instances. This is achieved by:

1.  Defining two sets of MongoDB connection properties in `application.yaml` (`spring.data.mongodb.primary` and `spring.data.mongodb.secondary`).
2.  Creating two `MongoTemplate` beans (`primaryMongoTemplate` and `secondaryMongoTemplate`), each configured for its respective database.
3.  Using `@EnableMongoRepositories` with `includeFilters` to explicitly associate repositories with their corresponding `MongoTemplate`.

## Rate Limiter

The `api-tracking-client` includes a per-service rate limiter. It can be configured in the `application.yaml` of the service using the client:

```yaml
monitoring:
  rate-limit:
    default-rate: 100 # Default requests/second
    services:
      orders: 120
      users: 50
```

If the rate limit is exceeded for a service, a log with `eventType: "rate-limit-hit"` is sent to the collector, but the request is still allowed to proceed normally.

## API Endpoints (Collector Service)

-   `POST /api/logs`: Receives API logs from the tracking client.
-   `GET /api/query/logs`: Retrieves API logs with pagination.
-   `GET /api/query/alerts`: Retrieves alerts with pagination and optional filtering by status.
-   `POST /api/query/alerts/{id}/resolve`: Marks an alert as "Resolved".
