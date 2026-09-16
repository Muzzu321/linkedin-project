# LinkedIn Microservices

A LinkedIn-style social media application built using a microservice architecture.

## Features

- User registration, login and profile management
- Create posts and like posts
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

### Technologies

- Java & Spring Boot
- Spring Cloud / OpenFeign
- Apache Kafka
- JWT
- Docker & Kubernetes
- ELK Stack
- Zipkin
- PostgreSQL

## Architecture Diagram

<img width="6788" height="2828" alt="Linked_In_Microserivce_Components_56249736c8" src="https://github.com/user-attachments/assets/0433da9c-f062-48ab-a511-5029293517d3" />
<img width="2996" height="2301" alt="Microservice_vs_Monolith_e2a23473bf" src="https://github.com/user-attachments/assets/b4a7366a-f57d-4a1d-bc70-17e96b3af00b" />
