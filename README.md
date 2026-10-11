# LinkedIn Clone — Microservices Backend

A backend-only, LinkedIn-inspired social platform built with **Java 21, Spring Boot, and microservices architecture**.

The project implements JWT-based authentication, connection management using Neo4j, post creation and likes using PostgreSQL, file uploads, and asynchronous event processing with Apache Kafka. Services communicate through Spring Cloud Gateway, OpenFeign, and Kafka.

> **Project status:** Learning and portfolio project. The backend has not been verified through a complete end-to-end test. Notifications are persisted by Kafka consumers; real-time push delivery and a notification retrieval API are not implemented.

## Features

| Area | Capability | Status |
|---|---|---|
| Authentication | User registration with BCrypt password hashing | Implemented |
| Authentication | Login with signed JWT | Implemented |
| API Gateway | JWT validation and user identity forwarding | Implemented |
| Connections | Send, accept, and reject connection requests | Implemented |
| Connections | List first-degree connections | Implemented |
| Posts | Create posts with text and a required image | Implemented |
| Posts | Retrieve a post and list posts by user | Implemented |
| Likes | Like and unlike posts | Implemented |
| Event processing | Publish and consume Kafka events | Implemented |
| Notifications | Persist notification records in PostgreSQL | Implemented |
| File uploads | Local-disk upload and download | Implemented |
| Service discovery | Eureka for local service discovery | Implemented |
| Deployment | Jib configuration and Kubernetes manifests | Present; deployment not verified end to end |

### Planned improvements

- Add a feed, comments, and post editing/deletion.
- Add notification retrieval and real-time delivery.
- Return uploaded image URLs from post APIs.
- Add pagination, request validation, and rate limiting.
- Improve Kafka reliability with idempotent consumers, retries, and dead-letter handling.
- Add unit, integration, and end-to-end tests.
- Improve security and deployment configuration.

## Architecture

The application consists of independent Spring Boot services:

| Service | Port | Responsibility |
|---|---:|---|
| API Gateway | 8080 | Request routing and JWT validation |
| Discovery Server | 8761 | Local service discovery |
| User Service | 9020 | Registration, login, and JWT issuance |
| Posts Service | 9010 | Post creation, retrieval, likes, and event publishing |
| Connections Service | 9030 | Connection graph management with Neo4j |
| Notification Service | 9040 | Kafka event consumption and notification persistence |
| Uploader Service | 9050 | Local file storage and retrieval |

Each service has its own Maven project and configuration. There is no parent Maven aggregator.

## Technology stack

- **Language:** Java 21
- **Framework:** Spring Boot
- **API Gateway:** Spring Cloud Gateway
- **Service discovery:** Netflix Eureka
- **Synchronous communication:** Spring Cloud OpenFeign
- **Asynchronous messaging:** Apache Kafka and Spring Kafka
- **Authentication:** JWT and BCrypt
- **Relational database:** PostgreSQL
- **Graph database:** Neo4j
- **Build:** Maven
- **Container image tooling:** Jib
- **Orchestration configuration:** Kubernetes manifests

## Data and event flow

- User registration stores the user and publishes a `user_created_topic` event.
- The Connections Service consumes the event and creates a person node in Neo4j.
- Post creation stores the post, retrieves the user's connections, and publishes notification events.
- The Notification Service consumes post-related events and stores notification records in PostgreSQL.
- Liking a post publishes a `post_liked_topic` event for the post owner.

Kafka processing is asynchronous. Successful API responses do not necessarily mean every downstream event has finished processing.

## Getting started

### Prerequisites

- JDK 21
- Maven Wrapper included in each service
- Docker, or separately configured PostgreSQL, Kafka, and Neo4j instances
- `curl` for API smoke tests

### Infrastructure

Start PostgreSQL with the required databases, Kafka, and Neo4j. Configure each service's database and messaging connection settings using environment variables or local configuration.

**Important:** The example credentials in development configuration are for local learning only. Replace them before shared deployment.

### Start the services

Start the Discovery Server and the supporting services, followed by the API Gateway. Ensure PostgreSQL, Kafka, and Neo4j are running and accessible first.

For example, from the relevant service directory:

```bash
./mvnw spring-boot:run
```

Start each service in a separate terminal. Configure the Notification Service's database credentials through environment variables if they are not set in your local configuration.

Once started, check the Eureka dashboard at `http://localhost:8761` and verify that the expected services have registered.

### API overview

Client requests go through `http://localhost:8080/api/v1`.

| Method | Endpoint | Purpose |
|---|---|---|
| POST | `/users/auth/signup` | Register a user |
| POST | `/users/auth/login` | Authenticate and obtain a JWT |
| POST | `/posts/core` | Create a post with an image |
| GET | `/posts/core/{postId}` | Retrieve a post |
| GET | `/posts/core/users/{userId}/allPosts` | List posts by user |
| POST | `/posts/likes/{postId}` | Like a post |
| DELETE | `/posts/likes/{postId}` | Unlike a post |
| POST | `/connections/core/request/{userId}` | Send a connection request |
| POST | `/connections/core/accept/{userId}` | Accept a connection request |
| POST | `/connections/core/reject/{userId}` | Reject a connection request |
| GET | `/connections/core/{userId}/first-degree` | List first-degree connections |

Protected endpoints require an `Authorization: Bearer <JWT>` header. Consult the controller implementations and service configuration for exact request formats and responses.

## Testing

The current test suite primarily contains Spring Boot context-loading tests. Business logic, security, Kafka processing, and inter-service communication need additional unit and integration coverage.

A complete end-to-end run is still required before claiming that the entire application has been verified.

## Security considerations

- Store JWT signing keys and database credentials outside source control.
- Rotate any secrets that have previously been committed.
- Validate authentication and authorization at appropriate service boundaries.
- Restrict direct access to internal service ports.
- Use TLS and managed secrets in shared deployments.
- Avoid exposing development credentials in Kubernetes manifests.

## Known limitations

- No frontend.
- No notification retrieval endpoint or real-time push delivery.
- Uploaded files are stored locally; persistent object storage is not configured.
- Post APIs do not currently return the stored image URL.
- No feed, comments, or post editing/deletion.
- No pagination or comprehensive request validation.
- Kafka publishing and consumption lack a complete reliability strategy, including an outbox, consumer idempotency, and dead-letter handling.
- Concurrent operations may create duplicate records where database constraints are missing.
- Automated business-logic and end-to-end tests are incomplete.
- Spring Boot and Spring Cloud versions vary across modules.
- Kubernetes deployment has not been verified end to end.

## Project structure

```text
.
├── APIGateway/
├── ConnectionsService/
├── DiscoveryServer/
├── notification-service/
├── postsService/
├── uploader-service/
├── userService/
└── k8s/
```

> Adjust the uploader directory name in this tree if you have not renamed the existing `uploder-service` folder.

## License

No license has been specified. Add a license file only after choosing the terms under which you want others to use the project.
