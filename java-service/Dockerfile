# ─────────────────────────────────────────────────────────────────────────────
# Stage 1 — Maven Build
# Uses the official Maven image to compile and package the application.
# Dependencies are cached in a separate layer for faster re-builds.
# ─────────────────────────────────────────────────────────────────────────────
FROM maven:3.9.6-eclipse-temurin-17 AS build

WORKDIR /build

# Copy pom.xml first — allows Docker to cache the dependency download layer
COPY pom.xml .
RUN mvn dependency:go-offline -B --no-transfer-progress

# Copy source and package (tests already run in the dedicated CI job)
COPY src ./src
RUN mvn clean package -B --no-transfer-progress -DskipTests=true

# ─────────────────────────────────────────────────────────────────────────────
# Stage 2 — Extract Layers (Spring Boot layered JARs)
# Optimises final image layer caching: dependencies rarely change but code does.
# ─────────────────────────────────────────────────────────────────────────────
FROM eclipse-temurin:17-jre-jammy AS layers

WORKDIR /app
COPY --from=build /build/target/*.jar application.jar
RUN java -Djarmode=layertools -jar application.jar extract

# ─────────────────────────────────────────────────────────────────────────────
# Stage 3 — Production Runtime
# Minimal JRE image. Non-root user. Layered JARs for fast startup.
# ─────────────────────────────────────────────────────────────────────────────
FROM eclipse-temurin:17-jre-jammy AS production

# Security: run as non-root
RUN addgroup --system appgroup && adduser --system --ingroup appgroup appuser

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

# Health check — generous start_period for JVM + JPA/PostgreSQL init time
HEALTHCHECK --interval=15s --timeout=10s --start-period=90s --retries=5 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8080/actuator/health || exit 1

# Launch via Spring Boot loader (supports layered JARs)
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS org.springframework.boot.loader.launch.JarLauncher"]
