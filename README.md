# Hotel Reservation System with Spring Boot

A comprehensive hotel reservation system built with Spring Boot, providing a robust platform for managing hotel bookings, room availability, user accounts, and more.

## Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Configuration](#configuration)
- [Running the Application](#running-the-application)
- [API Documentation](#api-documentation)
- [Database Setup](#database-setup)
- [Project Structure](#project-structure)
- [Payment Integration](#payment-integration)
- [Contributing](#contributing)
- [License](#license)

## Overview

This Hotel Reservation System is a full-featured application designed to manage the entire hotel booking process. It includes user management, hotel and room management, booking processing, payment integration, and more. The system is built using Spring Boot and follows best practices for application architecture.

## Features

### Core Features

- **User Management**
  - Registration and authentication
  - Role-based access control (Admin, Hotel Owner, Client)
  - JWT-based authentication

- **Hotel Management**
  - Hotel information and details
  - Room types and availability
  - Photo galleries
  - Reviews and ratings

- **Booking System**
  - Real-time availability checking
  - Booking creation and management
  - Confirmation emails
  - Cancellation handling

- **Payment Processing**
  - Integration with Stripe
  - Support for multiple payment methods
  - Simulated payment option for testing

- **Search and Filtering**
  - Search hotels by location
  - Filter by availability, price, amenities
  - Sort by ratings, price, etc.

### Advanced Features

- **Internationalization**
  - Support for multiple languages (English, French, Spanish, German, Chinese)

- **Reporting**
  - Booking statistics
  - Revenue reports
  - Occupancy rates

- **API Access**
  - RESTful API with comprehensive documentation
  - Secure endpoints with JWT authentication

- **Responsive Design**
  - Mobile-friendly interface
  - Accessible UI components

## Prerequisites

Before you begin, ensure you have the following installed:

- Java Development Kit (JDK) 21 or later
- Maven 3.6.0 or later
- MySQL 8.0 or later (or MariaDB as an alternative)
- Git (optional, for cloning the repository)
- Stripe CLI (optional, for payment testing)

## Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/NinjaShadowBoy/Hotel-Reservation-System-with-Spring-Boot.git
   cd Hotel-Reservation-System-with-Spring-Boot
   ```

2. Build the project:
   ```bash
   mvn clean install
   ```

3. Create a MySQL database:
   ```sql
   CREATE DATABASE hotel_reservation;
   ```

4. (Optional) Import sample data:
   ```bash
   mysql -u username -p hotel_reservation < hotel_reservation_mock_data.sql
   ```

## Configuration

### Database Configuration

Edit `src/main/resources/application.properties` to configure your database connection:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/hotel_reservation?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### Email Configuration

Configure email settings for sending booking confirmations and notifications:

```properties
spring.mail.host=your_smtp_server
spring.mail.port=587
spring.mail.username=your_email
spring.mail.password=your_password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
```

### Stripe Integration

For payment processing with Stripe, configure your API keys:

```properties
stripe.secret.server.key=your_stripe_secret_key
stripe.webhook.secret=your_stripe_webhook_secret
```

### File Upload Configuration

Configure the directories for storing uploaded files:

```properties
hotelphoto.upload.dir=uploads/hotelphotos
roomphoto.upload.dir=uploads/roomphotos
```

## Running the Application

### Development Mode

Run the application in development mode:

```bash
mvn spring-boot:run
```

Or with a specific profile:

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

### Production Mode

For production deployment, build a WAR file:

```bash
mvn clean package
```

Then deploy the WAR file to your application server or run it directly:

```bash
java -jar target/hotel-reservation.war
```

### Stripe Webhook Testing

For testing Stripe webhooks locally:

```bash
stripe login
stripe listen --forward-to localhost:8080/api/stripe/webhook
```

## API Documentation

The API documentation is available through Swagger UI when the application is running:

- Swagger UI: http://localhost:8080/swagger-ui
- API Docs: http://localhost:8080/api-docs

## Database Setup

The application uses Hibernate's `ddl-auto=update` setting, which automatically creates or updates the database schema based on the entity classes. However, for production use, it's recommended to use database migration tools like Flyway or Liquibase.

## Project Structure

The project follows a standard Spring Boot application structure:

```
src/main/java/cm/sji/hotel_reservation/
├── config/           # Configuration classes
├── controllers/      # REST and view controllers
│   ├── api/          # REST API controllers
│   └── view/         # Web page controllers
├── docs/             # Documentation
├── dtos/             # Data Transfer Objects
├── entities/         # JPA entity classes
├── exceptions/       # Custom exceptions
├── repositories/     # Spring Data JPA repositories
└── services/         # Business logic services

src/main/resources/
├── static/           # Static resources (CSS, JS, images)
├── templates/        # Thymeleaf templates
│   ├── admin/        # Admin panel templates
│   ├── client/       # Client-facing templates
│   ├── email/        # Email templates
│   └── owner/        # Hotel owner templates
└── application.properties  # Application configuration


```

### Key Components

- **Entities**: Domain model classes like Hotel, Room, Booking, User, etc.
- **Repositories**: Data access interfaces for each entity
- **Services**: Business logic implementation
- **Controllers**: Request handling and response generation
- **DTOs**: Data transfer objects for API requests and responses
- **Exception Handling**: Centralized exception handling with custom exceptions

## Payment Integration

The system integrates with Stripe for payment processing. To test payments:

1. Set up a Stripe account and get your API keys
2. Configure the keys in `application.properties`
3. Run the Stripe CLI to forward webhook events:
   ```bash
   stripe login
   stripe listen --forward-to localhost:8080/api/stripe/webhook
   ```

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details.
