# CrackerArchery

> A Spring Boot REST API that determines whether an arrow (line segment) intersects with a cracker target (square) based on provided geometric coordinates.
> In the future can determine interaction with other targets.
> For authenticated user provides information about how many requests are currently being processed by the application.

---

## Table of Contents

- [Overview](#overview)
- [Architecture](#architecture)
- [Tech Stack](#tech-stack)
- [Getting Started](#getting-started)
- [Endpoints](#endpoints)
- [Error Handling](#error-handling)
- [Project Structure](#project-structure)

---

## Overview

CrackerArchery exposes two endpoints:

- A **public** endpoint that accepts coordinates of a square (cracker) and a line segment (arrow) and returns their intersection points.
- A **protected** endpoint that returns information about how many requests are currently being processed by the application.

---

## Architecture

```
HTTP Request
    → IntersectionController  (receives and validates input format)
        → Mapper              (converts API model → domain model)
            → IntersectionService  (executes business logic and validation)
                → Domain      (pure geometric models: Target, Cracker, Arrow, Point)
```

### Layer Responsibilities

| Layer | Responsibility |
|---|---|
| `api/controller` | Receives HTTP requests, delegates to service |
| `api/model` | Request/response shapes with input validation annotations |
| `service` | Business logic, geometric calculations, domain validation |
| `domain` | Pure models — no Spring, no annotations, no framework dependencies |
| `exception` | Centralized error handling via `GlobalExceptionHandler` |
| `security` | Basic Auth configuration via Spring Security |

---

## Tech Stack

| Library | Version | Why |
|---|---|---|
| Spring Boot | 4.0.6 | Production-grade application framework with minimal setup |
| Spring Security | 7.0.5 | Industry standard for securing endpoints; |
| SpringDoc OpenAPI | 2.5.0 | Auto-generates Swagger UI from annotations — no manual API docs maintenance |
| Spring `ProblemDetail` | built-in | RFC 7807 compliant error responses; no extra dependency, consistent error shape across all endpoints |
| Lombok | 1.18.x | Eliminates boilerplate; keeps domain models concise |
| Java Records | Java 21 | Used for request/response models to enforce immutability and reduce noise |
| Abstract `Target` class | — | Enables future target shapes (e.g. circle, triangle) without modifying service logic |

---

## Getting Started

### Prerequisites

- Java 21
- Gradle

### Credentials Setup

To run the application locally, request the credential files from the project author and set the following environment variables pointing to their location on your machine:

| Variable | Description |
|---|---|
| `USERNAME_FILE` | Absolute path to the file containing the username |
| `PASSWORD_FILE` | Absolute path to the file containing the password |

**IntelliJ:** Run Configuration → Modify Options → Environment Variables

### Run

```bash
./gradlew bootRun
```

### Swagger UI

```
http://localhost:8080/swagger-ui/index.html
```

---

## Endpoints

| Method | Path | Access | Description |
|---|---|---|---|
| `GET` | `/archery/intersection/check` | Public | Returns intersection points between arrow and cracker |
| `GET` | `/archery/stats/request-count` | 🔒 Basic Auth | Returns total processed request count since startup |

### Example Request — Intersection Check

```json
{
  "cracker": {
    "topLeft":     { "x": 0.0, "y": 4.0 },
    "topRight":    { "x": 4.0, "y": 4.0 },
    "bottomLeft":  { "x": 0.0, "y": 0.0 },
    "bottomRight": { "x": 4.0, "y": 0.0 }
  },
  "arrow": {
    "start": { "x": -1.0, "y": 2.0 },
    "end":   { "x": 5.0,  "y": 2.0 }
  }
}
```

### Example Response

```json
{
  "intersectionPoints": [
    { "x": 0.0, "y": 2.0 },
    { "x": 4.0, "y": 2.0 }
  ]
}
```

---

## Error Handling

All errors return an RFC 7807 `ProblemDetail` response:

```json
{
  "type": "about:blank",
  "title": "Bad Request",
  "status": 400,
  "detail": "Provided coordinates do not form a valid square",
  "instance": "/archery/intersection/check"
}
```

| Status | Cause |
|---|---|
| `400` | Invalid input format or coordinates do not form valid geometric shapes |
| `401` | Missing or invalid credentials on protected endpoint |
| `500` | Unexpected server error |

---