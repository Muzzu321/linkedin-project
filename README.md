# LinkedIn Microservices Platform

A distributed, microservice-based professional networking platform inspired by
LinkedIn, built with Java and Spring Boot.

The platform separates user management, professional connections, post
management, media uploads, and notification processing into independently
deployable services.

Synchronous service communication is handled through Spring Cloud OpenFeign,
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
```
## Service Communication

The platform uses both synchronous and asynchronous communication between
microservices.

Synchronous communication is used for request-driven operations that require
an immediate response, while Apache Kafka is used for asynchronous,
event-driven workflows.

### Synchronous Communication

Spring Cloud OpenFeign is used for service-to-service REST communication.

```text
Client
  │
  ▼
API Gateway
  │
  ▼
Business Service
  │
  │ OpenFeign / REST
  ▼
Another Microservice
```
### Asynchronous Communication

Apache Kafka is used to publish and consume application events between
services.

```text
Posts Service ───────┐
                     │
Connections Service ─┼──► Kafka ──► Notification Service
                     │
User Service ────────┘
## Authentication & Authorization

The platform uses JWT-based authentication and authorization to secure access
to protected APIs.

Authentication is implemented using JWT-based credentials, with protected
requests routed through the API Gateway to the appropriate services.

### Authentication Flow

```text
Client
  │
  │ Login / API Request
  ▼
API Gateway
  │
  ▼
User Service
  │
  ├──► Authenticate User
  │
  └──► Generate / Validate JWT
             │
             ▼
       Protected Services
```
## Event-Driven Notifications

The Notification Service processes application events asynchronously through
Apache Kafka.

Instead of coupling notification creation directly to user requests, services
publish relevant events that can be consumed by the Notification Service.

### Notification Flow

```text
User / Connection / Posts Service
              │
              │ Application Event
              ▼
           Kafka
              │
              ▼
    Notification Service
              │
              ▼
       Notification Record
```
## File Upload & Media Handling

Media uploads are handled by a dedicated Uploader Service, keeping file
handling separate from the core user and post management services.

The Uploader Service processes image upload requests and provides the media
handling functionality required by the platform.

### Upload Flow

```text
Client
  │
  │ Image Upload
  ▼
API Gateway
  │
  ▼
Uploader Service
  │
  ▼
Media Storage
  │
  ▼
Uploaded Image
```
## Kubernetes Deployment

The microservices are containerized using Docker and deployed as independent
workloads within Kubernetes.

Kubernetes provides the runtime environment for managing the distributed
services and their internal communication.

### Deployment Architecture

```text
                    Kubernetes Cluster
                           │
        ┌──────────────────┼──────────────────┐
        │                  │                  │
        ▼                  ▼                  ▼
   API Gateway       Business Services   Infrastructure
        │                  │                  │
        │          ┌───────┼────────┐     Kafka / DB
        │          │       │        │
        ▼          ▼       ▼        ▼
      User      Posts  Connections  Notifications
     Service    Service   Service      Service
```
## Observability

The platform includes centralized logging and distributed tracing to provide
visibility across the microservice architecture.

### Observability Stack

- **ELK Stack** — centralized application and service logging
- **Zipkin** — distributed request tracing across microservices

### Logging

Application logs from the distributed services can be collected and
centralized through the ELK stack, making it easier to inspect service
behavior and troubleshoot requests across the platform.

### Distributed Tracing

Zipkin provides request tracing across service boundaries, helping follow
requests as they move through the API Gateway and backend microservices.

```text
Client
  │
  ▼
API Gateway
  │
  ├────► User Service
  │
  ├────► Posts Service
  │
  └────► Connections Service
              │
              ▼
          Notification Service

              │
              ▼
            Zipkin
```
## Design Patterns

The platform uses several architectural patterns to support service
independence, asynchronous processing, and maintainability.

| Pattern | Purpose |
|---|---|
| API Gateway | Provides a single entry point for client requests and routes traffic to backend services |
| Service Discovery | Enables services to discover registered service instances |
| OpenFeign | Provides declarative REST clients for synchronous service communication |
| Event-Driven Architecture | Decouples asynchronous operations through Apache Kafka |
| Microservice Architecture | Separates business capabilities into independently deployable services |
| JWT Authentication | Provides stateless authentication across protected APIs |
| Dedicated Media Service | Separates file-upload responsibilities from core business services |

## Tech Stack

| Category | Technologies |
|---|---|
| Language | Java |
| Framework | Spring Boot, Spring Cloud |
| API | REST |
| Service Communication | OpenFeign |
| Messaging | Apache Kafka |
| Database | PostgreSQL |
| Security | JWT |
| Service Discovery | Eureka |
| Containers | Docker |
| Orchestration | Kubernetes |
| Logging | ELK Stack |
| Distributed Tracing | Zipkin |
| Build | Maven |
