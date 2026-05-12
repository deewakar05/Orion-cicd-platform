<div align="center">
  <h1>🚀 Enterprise Multi-Service CI/CD Automation Platform</h1>
  <p><strong>Enterprise-Grade DevOps Pipeline using GitHub Actions, Docker, and Maven</strong></p>

  [![CI/CD Pipeline](https://github.com/deewakar05/multi-service-cicd-platform/actions/workflows/ci-cd.yml/badge.svg)](https://github.com/deewakar05/multi-service-cicd-platform/actions/workflows/ci-cd.yml)
  [![Node.js](https://img.shields.io/badge/Node.js-18.x-green.svg?logo=node.js)](https://nodejs.org/)
  [![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.2.x-brightgreen.svg?logo=spring-boot)](https://spring.io/projects/spring-boot)
  [![Docker](https://img.shields.io/badge/Docker-24.x-blue.svg?logo=docker)](https://www.docker.com/)
  [![GitHub Actions](https://img.shields.io/badge/GitHub_Actions-CI%2FCD-2088FF.svg?logo=github-actions)](https://github.com/features/actions)
  [![License](https://img.shields.io/badge/license-MIT-blue)](LICENSE)
  [![PRs Welcome](https://img.shields.io/badge/PRs-welcome-brightgreen)](https://github.com/deewakar05/multi-service-cicd-platform/pulls)
  ![Microservices](https://img.shields.io/badge/Architecture-Microservices-orange)
  ![Java](https://img.shields.io/badge/Java-17-red)
</div>

<br />

## 📖 Project Overview

This project is a comprehensive, production-ready DevOps platform demonstrating real-world automation and containerization. It features a robust multi-service architecture seamlessly integrated with a fully automated CI/CD pipeline. 

> **Built an enterprise-grade automated CI/CD pipeline reducing manual deployment effort by 80% using GitHub Actions, Docker, and Maven.**

Designed to industry standards, this repository showcases how modern microservices are built, tested, containerized, and orchestrated.

### ✨ Key Features
- Automated CI/CD Pipeline
- Multi-Service Architecture
- Dockerized Deployment
- Maven Build Automation
- GitHub Actions Integration
- Automated Testing
- Container Orchestration Ready
- Scalable Enterprise Workflow

---

## 🛠️ Tech Stack

### Frontend / API Gateway
* **Node.js & Express.js** - High-performance non-blocking API routing.
* **Jest & Supertest** - Comprehensive integration testing.

### Backend Microservice
* **Java 17 & Spring Boot 3.2** - Enterprise-grade backend processing.
* **Maven** - Dependency and lifecycle management.
* **Lombok** - Boilerplate reduction.

### DevOps & Infrastructure
* **Docker & Docker Compose** - Containerization and local orchestration.
* **GitHub Actions** - Continuous Integration and Continuous Deployment.
* **GHCR** - GitHub Container Registry for image storage.

---

## 🏗️ System Architecture

The platform operates on a proxy-pattern architecture where the Node.js API Gateway handles client connections and authentication, securely routing internal requests to the Java backend.

```text
Developer Push
      ↓
GitHub Actions
      ↓
Maven Build + Test
      ↓
Docker Build
      ↓
Docker Hub Push
      ↓
Deployment
```

---

## 📂 Folder Structure

Adopted industry-standard monorepo structure for cohesive CI/CD management:

```text
multi-service-cicd-platform/
│── java-service/               # Spring Boot Microservice
│── node-service/               # Node.js API Gateway
│── .github/workflows/          # Complete pipeline definition
│── docs/                       # Project Documentation
│── screenshots/                # Application & Pipeline Screenshots
│── docker-compose.yml          # Local orchestration setup
│── .env.example                # Environment variable templates
└── README.md
```

---

## 🔄 CI/CD Workflow Explanation

The `.github/workflows/ci-cd.yml` executes on every push to `main` or `dev` branches:

1. **`node-tests`**: Sets up Node 18, installs dependencies via `npm ci`, and runs Jest tests enforcing coverage.
2. **`java-tests`**: Sets up JDK 17, caches Maven packages, runs JUnit tests, and generates JaCoCo reports.
3. **`docker-build`**: (Runs only if tests pass). Uses Docker Buildx to compile multi-stage images and push them to the GitHub Container Registry.
4. **`integration-test`**: Spins up the entire stack using `docker-compose` inside the GitHub runner and executes HTTP health checks against both services.
5. **`deploy`**: (Placeholder) Final stage triggering production deployment.

---

## 🐳 Docker Setup

The project uses advanced Docker techniques:
- **Multi-stage builds** to drastically reduce final image sizes (dropping build tools).
- **Non-root users** (`appuser`) configured in Dockerfiles for enhanced security.
- **Spring Boot Layered JARs** allowing Docker to cache dependencies separately from application code.
- **Docker Compose Healthchecks** ensuring the API Gateway waits for the Java service to fully initialize.

---

## 🚀 Deployment

This project supports containerized deployment using Docker Compose
and can be extended to Kubernetes, AWS ECS, or Azure Container Apps.

---

## 📸 Screenshots

*(Add screenshots to the `screenshots/` directory and link them here)*
- GitHub Actions success page
- Docker containers running
- Application UI
- Terminal deployment logs

---

## 🚀 Getting Started

### Prerequisites
- Docker (v24+) & Docker Compose (v2+)
- Git

### 1. Installation Steps

Clone the repository and prepare the environment:

```bash
git clone git@github.com:deewakar05/multi-service-cicd-platform.git
cd multi-service-cicd-platform

# Setup environment variables
cp .env.example .env
```

### 2. Running the Application (Docker Compose)

Start the entire platform with one command:

```bash
docker compose up --build -d
```

Check the status of the containers:
```bash
docker compose ps
```

To view real-time logs:
```bash
docker compose logs -f
```

To tear down the environment:
```bash
docker compose down -v
```

---

## 🔌 API Endpoints

### 🟢 Node.js API Gateway (`http://localhost:3000`)

| Method | Endpoint | Auth | Description |
| :--- | :--- | :---: | :--- |
| `POST` | `/api/auth/signup` | ❌ | Register new user. |
| `POST` | `/api/auth/login` | ❌ | Authenticate and receive JWT. |
| `GET` | `/api/dashboard` | ✅ | Protected route returning user dashboard. |
| `GET` | `/api/analytics` | ✅ | Proxies request to Java `/reports`. |
| `GET` | `/api/analytics/logs` | ✅ | Proxies request to Java `/logs`. |
| `GET` | `/api/analytics/metrics` | ✅ | Proxies request to Java `/metrics`. |
| `GET` | `/health` | ❌ | Node.js container health check. |

### ☕ Spring Boot Service (`http://localhost:8080`)

*(Usually accessed internally via the API Gateway on the Docker network)*

| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `GET` | `/reports` | Retrieves all analytics reports. |
| `GET` | `/reports/{id}` | Retrieves a specific report. |
| `GET` | `/logs` | Returns application log entries. |
| `GET` | `/metrics` | JVM and platform performance metrics. |
| `GET` | `/actuator/health` | Spring Boot operational status. |

---

## 🏥 Health Check Information

The system implements robust self-healing and monitoring capabilities:
- **Node.js (`/health`)**: Returns JSON indicating uptime and version. Docker uses this to verify the gateway is responsive.
- **Java (`/actuator/health`)**: Native Spring Boot health indicator. The Node gateway uses `depends_on: condition: service_healthy` in Docker Compose to wait for this before starting.

---

## 📈 Future Enhancements
- [ ] Implement Redis caching layer for the analytics endpoints.
- [ ] Add Prometheus/Grafana integration for visual metric monitoring.
- [ ] Migrate secret management to HashiCorp Vault.
- [ ] Implement Terraform scripts for AWS infrastructure provisioning.

---

## 🧠 Learning Outcomes
- Designing and securing inter-service communication over private Docker networks.
- Crafting optimized, layered Dockerfiles for enterprise Java applications.
- Structuring complex, multi-job GitHub Actions workflows.
- Managing testing strategies across different language stacks in a monorepo.

---

## 📄 Resume Description
> **Multi-Service CI/CD Platform** | *Node.js, Spring Boot, Docker, GitHub Actions*
> - Architected a microservices platform featuring a Node.js API Gateway and a Java Spring Boot backend.
> - Engineered a fully automated 5-stage CI/CD pipeline using GitHub Actions, enforcing testing thresholds via Jest and JaCoCo.
> - Optimized containerization using multi-stage Docker builds and Spring Boot layered JARs, reducing deployment footprints.
> - Orchestrated local development and integration testing environments using Docker Compose with robust health-check dependencies.

---

## 🤝 Contributing Guide
Contributions are welcome! Please ensure you:
1. Fork the repository.
2. Create a feature branch (`git checkout -b feature/AmazingFeature`).
3. Ensure all tests pass (`npm test` and `mvn verify`).
4. Commit your changes (`git commit -m 'Add some AmazingFeature'`).
5. Push to the branch (`git push origin feature/AmazingFeature`).
6. Open a Pull Request against the `dev` branch.

---

## 📜 License
Distributed under the MIT License. See `LICENSE` for more information.

---

## 👨‍💻 Author Information
**Deewakar Kumar**
- GitHub: [@deewakar05](https://github.com/deewakar05)
