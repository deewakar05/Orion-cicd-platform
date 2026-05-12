# Multi-Service CI/CD Automation Platform

> **Using GitHub Actions · Docker · Maven · Node.js · Spring Boot**

A production-style, multi-service DevOps platform demonstrating a complete real-world CI/CD workflow. Two containerized microservices communicate over a private Docker network, tested end-to-end by a five-stage GitHub Actions pipeline.

---

## 📐 Architecture

```
                         ┌──────────────────────────────┐
    Client               │   Docker Compose Network      │
    (curl / Postman)     │                              │
         │               │  ┌────────────────────────┐  │
         │   :3000        │  │   Node.js API Gateway  │  │
         └──────────────►│  │   (Express.js)         │  │
                         │  │   POST /api/auth/login  │  │
                         │  │   POST /api/auth/signup │  │
                         │  │   GET  /api/dashboard   │  │
                         │  │   GET  /api/analytics   │  │
                         │  └───────────┬────────────┘  │
                         │              │ HTTP proxy     │
                         │  ┌───────────▼────────────┐  │
                         │  │  Java Spring Boot       │  │
                         │  │  Analytics Service      │  │
                         │  │  GET  /reports          │  │
                         │  │  GET  /logs             │  │
                         │  │  GET  /metrics          │  │
                         │  └────────────────────────┘  │
                         └──────────────────────────────┘
```

---

## 🗂️ Project Structure

```
devops-project/
│
├── node-service/                   # Node.js API Gateway
│   ├── src/
│   │   ├── index.js                # Express app entry point
│   │   ├── routes/
│   │   │   ├── auth.js             # POST /login, /signup, /logout
│   │   │   ├── dashboard.js        # GET /dashboard
│   │   │   └── analytics.js        # GET /analytics (proxies to Java)
│   │   ├── middleware/
│   │   │   ├── auth.js             # JWT authentication guard
│   │   │   ├── validators.js       # Input validation
│   │   │   └── errorHandler.js     # Global error handler
│   │   └── utils/
│   │       └── logger.js           # Structured logger
│   ├── tests/
│   │   └── app.test.js             # Jest + Supertest integration tests
│   ├── package.json
│   ├── Dockerfile                  # Multi-stage Node.js image
│   └── .dockerignore
│
├── java-service/                   # Spring Boot Analytics Microservice
│   ├── src/
│   │   ├── main/java/com/devops/platform/
│   │   │   ├── JavaAnalyticsServiceApplication.java
│   │   │   ├── controller/
│   │   │   │   ├── ReportController.java   # GET /reports
│   │   │   │   ├── LogController.java      # GET /logs
│   │   │   │   └── MetricsController.java  # GET /metrics
│   │   │   ├── service/
│   │   │   │   ├── ReportService.java
│   │   │   │   ├── LoggingService.java
│   │   │   │   └── MetricsService.java
│   │   │   ├── model/
│   │   │   │   ├── ApiResponse.java        # Generic response wrapper
│   │   │   │   ├── ReportDTO.java
│   │   │   │   ├── LogEntryDTO.java
│   │   │   │   └── MetricsDTO.java
│   │   │   └── exception/
│   │   │       ├── GlobalExceptionHandler.java
│   │   │       └── ResourceNotFoundException.java
│   │   ├── main/resources/
│   │   │   └── application.yml
│   │   └── test/java/com/devops/platform/
│   │       ├── JavaAnalyticsServiceApplicationTests.java
│   │       └── controller/
│   │           ├── ReportControllerTest.java
│   │           ├── LogControllerTest.java
│   │           └── MetricsControllerTest.java
│   ├── pom.xml                     # Maven build with Spring Boot 3.2 + JaCoCo
│   ├── Dockerfile                  # Multi-stage Spring Boot image (layered JARs)
│   └── .dockerignore
│
├── docker-compose.yml              # Multi-container orchestration
├── .github/
│   └── workflows/
│       └── ci-cd.yml               # 5-stage GitHub Actions pipeline
├── .env.example                    # Template environment file
├── .gitignore
└── README.md
```

---

## 🚀 Getting Started

### Prerequisites

| Tool           | Minimum Version |
|----------------|-----------------|
| Docker         | 24.x            |
| Docker Compose | 2.x             |
| Node.js        | 18.x            |
| Java (JDK)     | 17              |
| Maven          | 3.9.x           |

---

### 1️⃣ Clone and Configure

```bash
git clone https://github.com/your-org/devops-platform.git
cd devops-platform

# Copy and review environment variables
cp .env.example .env
```

---

### 2️⃣ Run with Docker Compose (Recommended)

```bash
# Build images and start both services
docker compose up --build

# Run in background (detached)
docker compose up --build -d

# View logs from all services
docker compose logs -f

# View logs for a single service
docker compose logs -f node-service
docker compose logs -f java-service

# Stop all services
docker compose down

# Stop and remove volumes
docker compose down --volumes
```

Services will be available at:
- **Node.js API Gateway** → http://localhost:3000
- **Java Analytics Service** → http://localhost:8080
- **Spring Boot Actuator** → http://localhost:8080/actuator/health

---

### 3️⃣ Run Services Locally (Without Docker)

**Node.js Service:**
```bash
cd node-service
npm install
npm run dev          # Development with hot-reload
# or
npm start            # Production mode
```

**Java Service:**
```bash
cd java-service
mvn spring-boot:run
```

---

## 🌐 API Reference

### Node.js Service (Port 3000)

| Method | Endpoint              | Auth Required | Description                  |
|--------|-----------------------|---------------|------------------------------|
| GET    | `/health`             | No            | Service health check         |
| POST   | `/api/auth/signup`    | No            | Register a new user          |
| POST   | `/api/auth/login`     | No            | Authenticate and get JWT     |
| POST   | `/api/auth/logout`    | No            | Logout (stateless)           |
| GET    | `/api/dashboard`      | ✅ JWT         | User dashboard summary       |
| GET    | `/api/analytics`      | ✅ JWT         | Proxy → Java /reports        |
| GET    | `/api/analytics/logs` | ✅ JWT         | Proxy → Java /logs           |
| GET    | `/api/analytics/metrics` | ✅ JWT     | Proxy → Java /metrics        |

### Java Service (Port 8080)

| Method | Endpoint                    | Description                  |
|--------|-----------------------------|------------------------------|
| GET    | `/actuator/health`          | Spring Boot health check     |
| GET    | `/reports`                  | List all analytics reports   |
| GET    | `/reports/{id}`             | Get a single report by ID    |
| GET    | `/reports/summary`          | Aggregated report statistics |
| GET    | `/logs`                     | Recent log entries           |
| GET    | `/logs/errors`              | Error-level logs only        |
| GET    | `/logs/services/{name}`     | Logs filtered by service     |
| GET    | `/metrics`                  | Full platform metrics        |
| GET    | `/metrics/build`            | CI/CD build metrics          |
| GET    | `/metrics/system`           | JVM system metrics           |

---

## 🔐 Authentication Flow

```bash
# 1. Sign up
curl -X POST http://localhost:3000/api/auth/signup \
  -H "Content-Type: application/json" \
  -d '{"username":"devuser","email":"dev@example.com","password":"MyPass@123"}'

# 2. Login and extract token
TOKEN=$(curl -s -X POST http://localhost:3000/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"dev@example.com","password":"MyPass@123"}' \
  | python3 -c "import sys,json; print(json.load(sys.stdin)['data']['token'])")

# 3. Access protected dashboard
curl -H "Authorization: Bearer $TOKEN" http://localhost:3000/api/dashboard

# 4. Access analytics (proxied to Java service)
curl -H "Authorization: Bearer $TOKEN" http://localhost:3000/api/analytics
```

---

## 🧪 Testing

### Node.js Tests

```bash
cd node-service
npm test                    # Run all tests with coverage
npm run test:watch          # Watch mode for development
```

### Java Tests

```bash
cd java-service
mvn test                    # Run JUnit 5 tests
mvn verify                  # Tests + JaCoCo coverage report
```
Coverage report: `java-service/target/site/jacoco/index.html`

---

## 🔄 CI/CD Pipeline

The GitHub Actions pipeline at `.github/workflows/ci-cd.yml` runs automatically on every push to `main` or `develop`:

```
Push to main
     │
     ├─► 🟢 node-tests      → npm install → npm test (Jest + coverage)
     │
     ├─► ☕ java-tests       → mvn verify (JUnit 5 + JaCoCo)
     │
     ├─► 🐳 docker-build     → Build + push images to GHCR (on main only)
     │        (needs: node-tests + java-tests)
     │
     ├─► 🔗 integration-test → docker compose up → curl health checks
     │        (needs: node-tests + java-tests)
     │
     └─► 🚀 deploy           → Production deployment
              (needs: docker-build + integration-test, main only)
```

---

## 🐳 Docker Details

### Image Tags

| Service       | Image                              |
|---------------|------------------------------------|
| Node.js       | `ghcr.io/your-org/node-service:latest` |
| Java          | `ghcr.io/your-org/java-service:latest` |

### Useful Docker Commands

```bash
# Check container status
docker compose ps

# Inspect health
docker inspect --format='{{.State.Health.Status}}' node-api-gateway
docker inspect --format='{{.State.Health.Status}}' java-analytics-service

# Shell into a running container
docker exec -it node-api-gateway sh
docker exec -it java-analytics-service sh

# Rebuild a single service
docker compose up --build java-service
```

---

## ⚙️ Environment Variables

| Variable              | Service | Default                          | Description              |
|-----------------------|---------|----------------------------------|--------------------------|
| `PORT`                | Node    | `3000`                           | HTTP port                |
| `JWT_SECRET`          | Node    | `devops-platform-secret-key`     | JWT signing secret       |
| `JWT_EXPIRES_IN`      | Node    | `1h`                             | Token lifetime           |
| `JAVA_SERVICE_URL`    | Node    | `http://java-service:8080`       | Java service base URL    |
| `LOG_LEVEL`           | Node    | `info`                           | Logging level            |
| `PORT`                | Java    | `8080`                           | HTTP port                |
| `APP_ENV`             | Java    | `development`                    | Application environment  |

> ⚠️ **Never commit your `.env` file!** Use the `.env.example` as a template.

---

## 📚 Tech Stack

| Layer       | Technology               | Version  |
|-------------|--------------------------|----------|
| API Gateway | Node.js + Express.js     | 18 LTS   |
| Microservice| Spring Boot              | 3.2.x    |
| Build Tool  | Apache Maven             | 3.9.x    |
| Runtime     | JDK (Eclipse Temurin)    | 17       |
| Container   | Docker                   | 24.x     |
| Orchestration | Docker Compose         | 2.x      |
| CI/CD       | GitHub Actions           | Latest   |
| Testing (JS)| Jest + Supertest         | Latest   |
| Testing (Java) | JUnit 5 + MockMvc   | Latest   |
| Coverage (Java) | JaCoCo             | 0.8.11   |

---

## 👨‍💻 Author

Built as a production-grade academic DevOps project demonstrating real-world multi-service architecture, containerization, and CI/CD automation.
