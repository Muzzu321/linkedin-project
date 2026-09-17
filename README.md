# LinkedIn Microservices Platform

A distributed, microservice-based professional networking platform inspired by
LinkedIn, built with Java and Spring Boot.

The platform separates user management, professional connections, post
management, media uploads, and notification processing into independently
deployable services.

Synchronous service communication is handled through Spring Cloud and OpenFeign,
while Apache Kafka is used for asynchronous event processing between services.

## Features

- User registration, login and profile management
- Create and like posts
- Send and accept connection requests
- Connection-based notifications
- Image upload support
- JWT-based authentication and authorization
- Asynchronous event processing using Kafka

## Architecture

The application is split into independent microservices:

- API Gateway
- User Service
- Posts Service
- Connections Service
- Notification Service
- Uploader Service
- Service Discovery

## Technologies

- Java & Spring Boot
- Spring Cloud / OpenFeign
- Apache Kafka
- JWT
- Docker & Kubernetes
- PostgreSQL
- ELK Stack
- Zipkin

## Architecture Diagram

<img width="1200" alt="LinkedIn Microservices Architecture" src="https://github.com/user-attachments/assets/0433da9c-f062-48ab-a511-5029293517d3" />

## Core Workflows

The platform supports three primary workflows across the microservices:
user management, social interactions, and event-driven notifications.

### User & Profile Workflow

User registration, authentication, and profile management are handled by the
User Service.

1. The client sends a request through the API Gateway.
2. The API Gateway routes the request to the User Service.
3. The User Service processes the user or profile operation.
4. User data is persisted in PostgreSQL.
5. The response is returned through the API Gateway.

### Post Workflow

Posts are managed by the Posts Service, while media uploads are handled by the
Uploader Service.

1. A user submits a post through the API Gateway.
2. The Posts Service creates and persists the post.
3. Images are uploaded through the Uploader Service when required.
4. Users can like posts.
5. Relevant events are published through Kafka.

### Connection Workflow

Connection requests are managed by the Connections Service.

1. A user sends a connection request to another user.
2. The API Gateway routes the request to the Connections Service.
3. The connection request is persisted.
4. The recipient can accept the request.
5. Connection events are published through Kafka.
6. Notification Service processes the corresponding event.

### Notification Workflow

Notification processing is handled asynchronously through Apache Kafka.

Notifications can be triggered by events such as:

- Connection requests
- Accepted connection requests
- New posts from connections
- Likes on posts

```text
Posts Service ───────┐
                     │
Connections Service ─┼──► Kafka ──► Notification Service
                     │
User Service ────────┘
```
## Data Model & Persistence

The platform uses PostgreSQL for persistent service data. Each business service
maintains its own domain-specific data and exposes access through its service
APIs rather than sharing database access across services.

Key persistence areas include:

- **Users & Profiles** — user accounts, authentication data, and profile information
- **Posts** — user-created posts and post metadata
- **Connections** — connection requests and professional relationships
- **Notifications** — notification records generated from application events
- **Media Metadata** — references and metadata associated with uploaded images

The service-level separation keeps user, post, connection, and notification data
within their respective business domains.

### Persistence Flow

```text
User Service ──────────► PostgreSQL
Posts Service ─────────► PostgreSQL
Connections Service ───► PostgreSQL
Notification Service ──► PostgreSQL
