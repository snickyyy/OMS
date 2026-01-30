# Order Management Service (OMS)

**Order Management Service** is a highly reliable backend service for managing orders and payments, built on the principles of **Clean Architecture** and **Domain-Driven Design (DDD)**. The service guarantees idempotent operations, is ready for distributed microservice environments, and is designed for production use.

---

## Technology Stack

| Category               | Technologies           |
| ---------------------- | ---------------------- |
| **Language**           | Java 21                |
| **Framework**          | Spring Boot 3.x        |
| **Database Access**    | Spring Data JPA        |
| **Database**           | PostgreSQL             |
| **Build Tool**         | Gradle                 |
| **Containerization**   | Docker + Docker Compose|
| **Linters**            | Checkstyle, Spotbugs   |

---

## Architecture

<img width="400" alt="code-architecture-diagram" src="docs/images/code-architecture-diagram.png">

The project is built according to the principles of:

- **Clean Architecture** — Clear separation of layers and independence of business logic from infrastructure.
- **Domain-Driven Design (DDD)** — Focus on domain models and business processes.
- **Hexagonal Architecture** — Ports and adapters to isolate the core.

### Project Structure

```
├── application/          # Application layer (use-cases)
│   ├── dtos/             # DTOs between layers
│   └── usecases/         # Use cases
├── domain/               # Domain core
│   ├── exceptions/       # Domain exceptions
│   ├── models/           # Business models
│   │   └── values/       # Value Objects
│   ├── repositories/     # Repository interfaces
│   └── services/         # Domain services
└── infrastructure/       # External world
    ├── controllers/      # REST API
    ├── dtos/             # DTOs for API
    │   ├── requests/
    │   └── responses/
    └── persistence/      # Database layer
        ├── entities/
        └── repositories/
```

---

## Configuration

**Spring Profiles** are used for different environments:

| Configuration File     | Purpose |
|------------------------|---------|
| `application.yml`      | General configuration |
| `application-dev.yml`  | Development settings |
| `application-prod.yml` | Production settings |
