# ─────────────────────────────────────────────────────────────────────────────
# Stage 1 — Maven Build
# Uses the official Maven image to compile and package the application.
# Dependencies are cached in a separate layer for faster re-builds.
# ─────────────────────────────────────────────────────────────────────────────
FROM maven:3.9.6-eclipse-temurin-17-alpine AS build

WORKDIR /build

# Copy pom.xml first — allows Docker to cache the dependency download layer
COPY pom.xml .
RUN mvn dependency:go-offline -B --no-transfer-progress

# Copy source and run the full build + tests
COPY src ./src
RUN mvn clean package -B --no-transfer-progress -DskipTests=false

# ─────────────────────────────────────────────────────────────────────────────
# Stage 2 — Extract Layers (Spring Boot layered JARs)
# Optimises final image layer caching: dependencies rarely change but code does.
# ─────────────────────────────────────────────────────────────────────────────
FROM eclipse-temurin:17-jre-alpine AS layers

WORKDIR /app
COPY --from=build /build/target/*.jar application.jar
RUN java -Djarmode=layertools -jar application.jar extract

# ─────────────────────────────────────────────────────────────────────────────
# Stage 3 — Production Runtime
# Minimal JRE image. Non-root user. Layered JARs for fast startup.
# ─────────────────────────────────────────────────────────────────────────────
FROM eclipse-temurin:17-jre-alpine AS production

# Security: run as non-root
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

WORKDIR /app

# Copy layered JARs in dependency order (least → most volatile)
COPY --from=layers /app/dependencies/          ./
COPY --from=layers /app/spring-boot-loader/    ./
COPY --from=layers /app/snapshot-dependencies/ ./
COPY --from=layers /app/application/           ./

# Set correct ownership
RUN chown -R appuser:appgroup /app
USER appuser

# Expose application port
EXPOSE 8080

# JVM tuning for containers
ENV JAVA_OPTS="-XX:+UseContainerSupport \
               -XX:MaxRAMPercentage=75.0 \
               -XX:+UseG1GC \
               -Djava.security.egd=file:/dev/./urandom"

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=30s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8080/actuator/health || exit 1

# Launch via Spring Boot loader (supports layered JARs)
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS org.springframework.boot.loader.launch.JarLauncher"]
