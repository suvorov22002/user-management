# Customer Management API

![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.4.4-green.svg)  
![Java](https://img.shields.io/badge/Java-21-blue.svg)  

A comprehensive customer management system featuring:
- Spring Boot Rest API
- Database-agnostic architecture
- Swagger API documentation
- Unit and integration tests

## Table of Contents

- [Features](#features)
- [Technologies](#technologies)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Configuration](#configuration)
- [Running the Application](#running-the-application)
- [API Documentation](#api-documentation)
- [Testing](#testing)
- [Deployment](#deployment)
- [Project Structure](#project-structure)
- [License](#license)

## Features

### Backend (Spring Boot)
- **User Management**:
  - Create, read, update, and delete customers
  - Paginated listing of cutomers
  - Search by ID or email
- **Data Validation**:
  - Entity-level validation
  - DTO-level validation
- **Documentation**:
  - Swagger/OpenAPI documentation

## Technologies

### Backend
- **Framework**: Spring Boot 3.4.4
- **Language**: Java 21
- **Database**: H2 (configurable to PostgreSQL/MySQL)
- **Documentation**: SpringDoc OpenAPI 2.8.3
- **Build**: MMaven

## Prerequisites

- Java 21 JDK
- H2 (or PostgreSQL/MySQL)
- Maven 3.8+

## Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/suvorov22002/user-management.git
   cd user-management  
   
2. Backend setup:
   ```bash
   mvn clean install  

## Configuration

## Running the application

1. Backend:
   ```bash
   mvn spring-boot:run
The application will be available at:
* Backend: http://localhost:8080
* Swagger UI: http://localhost:8080/swagger-ui.html

## API Documentation
The API is documented using Swagger/OpenAPI 3.0. After starting the application:  
1. Access swagger UI at: http://localhost:8080/swagger-ui.html  
2. Explore available endpoints with examples

## Testing

#### Backend Tests
   ```bash
   mvn test
   ```

#### Integration Tests
The project includes:
* Unit tests for services and controllers
* Integration tests for API endpoints
* Security tests for protected routes

## Deployment

### Backend
1. Build the JAR
   ```bash
   mvn clean package
2. Run the JAR
   ```bash
   java -jar target/user-management-0.0.1-SNAPSHOT.jar

### Docker
Example Dockerfile for backend
   ```bash
   FROM eclipse-temurin:17-jdk-jammy
   WORKDIR /app
   COPY target/user-management-0.0.1-SNAPSHOT.jar app.jar
   ENTRYPOINT ["java", "-jar", "app.jar"]
   ```

## Project Structure
   ```bash
    user-management/
    ├── src/
    │   ├── main/
    │   │   ├── java/com/pyramid/usermanagement/
    │   │   │    ├── core/
    │   │   │    │    ├── configs/    # Configuration classes
    │   │   │    │    ├── exceptions/ # Custom exceptions
    │   │   │    ├── domain/
    │   │   │    │    ├── user/
    │   │   │    │    │   ├── controller/  # REST controllers
    │   │   │    │    │   ├── dto/         # Data Transfer Objects
    │   │   │    │    │   ├── model/       # JPA entities
    │   │   │    │    │   ├── repository/  # JPA repositories       
    │   │   │    │    │   ├── services/    # Business logic
    │   │   │    │      
    │   │   │    └── UserManagementApplication.java
    │   │   └── resources/
    │   │       ├── static/ 
    │   │       ├── application.properties
    │   │       └── templates/
    │   └── test/                    # Unit and integration tests
    └── pom.xml                      # Maven configuration
   ```

## License
This project is free.




