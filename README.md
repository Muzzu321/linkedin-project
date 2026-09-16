# LinkedIn Microservices

A LinkedIn-style social media application built using a microservice architecture.

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
