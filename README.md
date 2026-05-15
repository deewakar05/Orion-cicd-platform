<div align="center">
  <h1>🚀 Multi-Service CI/CD Automation Platform</h1>
  <p><strong>A DevOps portfolio project demonstrating automated pipelines, Docker orchestration, and secure multi-service architecture</strong></p>

  [![CI/CD Pipeline](https://github.com/deewakar05/Orion-cicd-platform/actions/workflows/ci-cd.yml/badge.svg?branch=main&event=push)](https://github.com/deewakar05/Orion-cicd-platform/actions/workflows/ci-cd.yml)
  [![React](https://img.shields.io/badge/React-18.x-61DAFB.svg?logo=react&logoColor=black)](https://reactjs.org/)
  [![Node.js](https://img.shields.io/badge/Node.js-18.x-green.svg?logo=node.js)](https://nodejs.org/)
  [![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.2-brightgreen.svg?logo=spring-boot)](https://spring.io/projects/spring-boot)
  [![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-blue.svg?logo=postgresql)](https://www.postgresql.org/)
  [![Docker](https://img.shields.io/badge/Docker-Compose-blue.svg?logo=docker)](https://www.docker.com/)
  [![Nginx](https://img.shields.io/badge/Nginx-Proxy-009639.svg?logo=nginx)](https://nginx.org/)
  [![License](https://img.shields.io/badge/license-MIT-blue)](LICENSE)
</div>

<br />

## 📖 Overview

This project is a portfolio-grade DevOps platform built to demonstrate real-world CI/CD automation, Docker orchestration, and secure microservice design. It consists of a React frontend, two backend services (Node.js and Spring Boot) connected to a shared PostgreSQL database, fully containerized with Docker, and routed through an Nginx proxy.

> Built as part of a university DevOps coursework project, later enhanced with a full React frontend, production-grade security, health checks, and pipeline reliability improvements.

---

## 🎯 Why This Project?

This project was built to simulate a production-grade full-stack microservices and DevOps workflow used in modern software teams. It demonstrates my ability to not just write code, but architect, containerize, test, and deploy a complete ecosystem that communicates securely. It bridges the gap between backend development, frontend integration, and cloud-native DevOps practices.

---

## 📸 Screenshots

*(Replace these placeholders with actual screenshots of your running application)*

- **Frontend Dashboard**: `![Dashboard Demo](link-to-dashboard.png)`
- **Docker Compose Status (`docker compose ps`)**: `![Docker Status](link-to-docker.png)`
- **GitHub Actions Pipeline Success**: `![Pipeline Success](link-to-pipeline.png)`

---

## ✨ Features

- **5-Stage GitHub Actions Pipeline** — automated test → build → integration → deploy
- **Multi-Stage Docker Builds** — optimized final images with non-root users
- **PostgreSQL Integration** — persistent database with health-check–guarded startup ordering
- **Secure Authentication** — JWT-based auth with bcrypt password hashing (10 salt rounds)
- **API Gateway Pattern** — Node.js proxies authenticated requests to the Java analytics service
- **Spring Boot Actuator** — live `/actuator/health` endpoint for container health probing
- **Test Coverage Enforcement** — Jest coverage thresholds (65%) + JaCoCo on Maven verify
- **Docker Compose Orchestration** — full local stack launch with one command

---

## 🏗️ Architecture

## 🏗️ Architecture

```mermaid
graph TD
    Client([Client Browser]) -->|HTTP 80| Nginx[Nginx Reverse Proxy]
    
    subgraph Frontend Layer
        Nginx -->|Route /| React[React Dashboard]
    end
    
    subgraph API Layer
        Nginx -->|Route /api/*| NodeGW[Node.js API Gateway]
    end
    
    subgraph Service Layer
        NodeGW -->|JWT Validated| JavaAuth[Spring Boot Analytics Service]
    end
    
    subgraph Data Layer
        JavaAuth -->|JDBC| DB[(PostgreSQL 15)]
    end

    classDef proxy fill:#009639,stroke:#333,stroke-width:2px,color:#fff;
    classDef react fill:#61DAFB,stroke:#333,stroke-width:2px,color:#000;
    classDef node fill:#339933,stroke:#333,stroke-width:2px,color:#fff;
    classDef java fill:#6DB33F,stroke:#333,stroke-width:2px,color:#fff;
    classDef pg fill:#336791,stroke:#333,stroke-width:2px,color:#fff;

    class Nginx proxy;
    class React react;
    class NodeGW node;
    class JavaAuth java;
    class DB pg;
```

### Startup Ordering (Docker Compose)
```
postgres (healthy) → java-service (healthy) → node-service
```
Each service waits for its dependency to pass a health check before starting.

---

## 🛠️ Tech Stack

| Layer | Technology |
| :--- | :--- |
| **Frontend Dashboard** | React 18, Vite, Tailwind CSS, Axios |
| **Reverse Proxy** | Nginx |
| **API Gateway** | Node.js 18, Express.js |
| **Backend Service** | Java 17, Spring Boot 3.2, Spring Data JPA |
| **Database** | PostgreSQL 15 |
| **Authentication** | JWT (`jsonwebtoken`) + bcrypt |
| **Containerization** | Docker, Docker Compose |
| **CI/CD** | GitHub Actions |
| **Container Registry** | GitHub Container Registry (GHCR) |
| **Testing** | Jest, Supertest, JUnit 5, JaCoCo |

---

## 🧠 Design Decisions

- **React + Tailwind Frontend**: Chosen to provide a modern, responsive user experience directly interacting with the backend APIs, transitioning the project to a full-stack system.
- **Nginx Reverse Proxy**: Used to cleanly route frontend and backend traffic on the same port, avoiding CORS issues and mirroring a standard production network layout.
- **Node.js API Gateway**: Acts as a lightweight proxy and authentication layer, handling JWTs efficiently before requests reach the heavy-lifting services.
- **Spring Boot for Analytics**: Selected due to the strong JVM ecosystem for data processing and enterprise readiness.
- **Docker Compose Orchestration**: Chosen for local orchestration simplicity and perfect parity between local development and CI/CD environments.

---

## 📁 Project Structure

```
multi-service-cicd-platform/
├── .github/
│   └── workflows/
│       └── ci-cd.yml          # 5-stage GitHub Actions pipeline
├── java-service/
│   ├── src/
│   │   ├── main/              # Spring Boot controllers, services, models
│   │   └── test/              # JUnit 5 + MockMvc test suite
│   ├── Dockerfile             # Multi-stage Maven → JRE build
│   └── pom.xml                # Dependencies: Web, JPA, Actuator, PostgreSQL
├── node-service/
│   ├── src/
│   │   ├── routes/            # auth.js, dashboard.js, analytics.js
│   │   ├── middleware/        # JWT auth, validators, error handler
│   │   └── utils/             # Structured logger
│   ├── tests/
│   │   └── app.test.js        # Jest + Supertest integration tests
│   ├── Dockerfile             # Multi-stage Node.js build
│   └── package.json
├── docker-compose.yml         # Full stack: postgres + java + node
├── .env.example               # Environment variable template
├── .gitignore
├── LICENSE
└── README.md
```

---

## 🔄 CI/CD Pipeline

The pipeline is defined in [`.github/workflows/ci-cd.yml`](.github/workflows/ci-cd.yml) and runs on every push to `main` or `dev`.

```
1. 🟢 node-tests      — npm ci, Jest test suite with coverage report
2. ☕ java-tests       — mvn clean verify, JUnit tests, JaCoCo report
3. 🐳 docker-build     — multi-stage Docker image build + GHCR push
4. 🔗 integration-test — Docker Compose full stack, health check polling
5. 🚀 deploy          — deployment summary (customisable for any target)
```

**Jobs 3 and 5 require jobs 1 and 2 to pass first.** The integration test job starts all three containers, waits for each to become healthy via polling, then validates all API endpoints with `curl`.

---

## 🐳 Docker & Local Setup

### Prerequisites
- Docker Desktop (v24+)
- Git

### 1. Clone and configure

```bash
git clone https://github.com/deewakar05/multi-service-cicd-platform.git
cd multi-service-cicd-platform

# Create your local environment file
cp .env.example .env
# Edit .env — set a strong JWT_SECRET and POSTGRES_PASSWORD
```

### 2. Start the full stack

```bash
docker compose up --build -d
```

This builds both service images and starts PostgreSQL, the Java service, and the Node.js gateway in the correct order.

### 3. Verify health

```bash
# Container status
docker compose ps

# Node.js gateway
curl http://localhost:3000/health

# Java service
curl http://localhost:8080/actuator/health

# Live logs
docker compose logs -f
```

### 4. Tear down

```bash
docker compose down -v   # -v removes the postgres data volume
```

---

## 🔌 API Endpoints

### Node.js Gateway (`localhost:3000`)

| Method | Endpoint | Auth | Description |
| :--- | :--- | :--- | :--- |
| `GET` | `/health` | None | Service health status |
| `POST` | `/api/auth/signup` | None | Register a new user |
| `POST` | `/api/auth/login` | None | Login, returns JWT |
| `POST` | `/api/auth/logout` | None | Logout (client-side) |
| `GET` | `/api/dashboard` | Bearer JWT | Dashboard summary |
| `GET` | `/api/analytics` | Bearer JWT | Proxy → Java `/reports` |
| `GET` | `/api/analytics/metrics` | Bearer JWT | Proxy → Java `/metrics` |
| `GET` | `/api/analytics/logs` | Bearer JWT | Proxy → Java `/logs` |

### Java Analytics Service (`localhost:8080`)

| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `GET` | `/actuator/health` | Spring Boot health (used by Docker health check) |
| `GET` | `/reports` | List of analytics reports |
| `GET` | `/reports/{id}` | Single report by ID |
| `GET` | `/reports/summary` | Report statistics summary |
| `GET` | `/metrics` | Platform + JVM metrics |
| `GET` | `/metrics/build` | CI/CD build metrics |
| `GET` | `/metrics/system` | JVM and system metrics |
| `GET` | `/logs` | Platform log entries |

---

## 🔑 Environment Variables

Copy `.env.example` to `.env` and configure before running locally.

| Variable | Default | Description |
| :--- | :--- | :--- |
| `JWT_SECRET` | `change-this-...` | Secret key for JWT signing — **change in production** |
| `JWT_EXPIRES_IN` | `1h` | JWT token validity |
| `POSTGRES_DB` | `devopsdb` | Database name |
| `POSTGRES_USER` | `devops_admin` | Database username |
| `POSTGRES_PASSWORD` | `changeme_in_production` | Database password — **change in production** |
| `JPA_DDL_AUTO` | `update` | Hibernate schema strategy |
| `LOG_LEVEL` | `debug` | Node.js log verbosity |
| `NODE_ENV` | `development` | Node.js environment |
| `APP_ENV` | `development` | Spring Boot environment label |

---

## 🌐 Deployment

This project is ready to deploy on any Docker-compatible platform.

### Render / Railway (Recommended for quick demo)
1. Connect your GitHub repository
2. Set environment variables in the platform's dashboard (do **not** commit `.env`)
3. Set the start command to `docker compose up`
4. Expose ports `3000` and `8080`

### AWS EC2
1. Launch an EC2 instance (t2.micro or t3.small is sufficient)
2. Install Docker and Docker Compose
3. Clone the repo and create `.env` with production values
4. Run `docker compose up -d`
5. Configure a security group to allow ports 3000 and 8080

### Important production checklist
- [ ] Set a strong, random `JWT_SECRET` (32+ chars)
- [ ] Set a strong `POSTGRES_PASSWORD`
- [ ] Set `NODE_ENV=production` and `APP_ENV=production`
- [ ] Set `JPA_DDL_AUTO=validate` (after initial `update` run)
- [ ] Configure a reverse proxy (Nginx) in front of the services

---

## 🔒 Security Notes

- Passwords are hashed with **bcrypt** (10 salt rounds) before storage — plaintext passwords are never persisted
- JWT tokens are signed with a configurable secret and expire after 1 hour
- Docker containers run as **non-root users** (`appuser`)
- No secrets are committed to the repository — all sensitive values are injected via environment variables
- The `.env` file is listed in `.gitignore`

---

## 🐛 Troubleshooting

**Java service never becomes healthy**
```bash
docker compose logs java-service
```
Usually caused by PostgreSQL not being fully ready. The `start_period: 90s` healthcheck window should handle this. If it persists, increase `start_period` in `docker-compose.yml`.

**`pg_isready` health check fails**
Ensure `POSTGRES_USER` and `POSTGRES_DB` are set in your `.env` file and match the values in `docker-compose.yml`.

**Port already in use**
```bash
# Find what is using port 3000 or 8080
lsof -i :3000
lsof -i :8080
```

**Rebuild from scratch**
```bash
docker compose down -v --remove-orphans
docker compose up --build -d
```

---

## 📈 Future Enhancements

- [ ] Connect Node.js auth service to PostgreSQL using the `pg` client (replace in-memory user store)
- [ ] Add Spring Data JPA repositories to persist report and metrics data
- [ ] Add Prometheus metrics exporter + Grafana dashboard
- [ ] Introduce database migrations with Flyway
- [ ] Add rate limiting to auth endpoints

---

## 🤝 Contributing

Contributions are welcome! Please:
1. Fork the repository
2. Create a feature branch (`git checkout -b feature/your-feature`)
3. Ensure all tests pass (`npm test` and `mvn verify`)
4. Commit your changes with a clear message
5. Open a Pull Request against the `dev` branch

---

## 📜 License

Distributed under the MIT License. See [`LICENSE`](LICENSE) for details.

---

## 👨‍💻 Author

**Deewakar Kumar**
- GitHub: [@deewakar05](https://github.com/deewakar05)
