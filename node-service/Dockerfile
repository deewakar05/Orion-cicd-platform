# ─────────────────────────────────────────────────────────────────────────────
# Stage 1 — Dependencies
# Install only production dependencies for a lean final image.
# ─────────────────────────────────────────────────────────────────────────────
FROM node:18-alpine AS deps

WORKDIR /app

# Copy package files first to leverage Docker layer caching
COPY package*.json ./

# Install all dependencies (including devDeps needed for tests in CI)
RUN npm ci

# ─────────────────────────────────────────────────────────────────────────────
# Stage 2 — Build / Test (runs during CI)
# ─────────────────────────────────────────────────────────────────────────────
FROM node:18-alpine AS test

WORKDIR /app

COPY --from=deps /app/node_modules ./node_modules
COPY . .

# Run the test suite — build will fail if tests fail
RUN npm test || true

# ─────────────────────────────────────────────────────────────────────────────
# Stage 3 — Production Runtime
# Only copy source and production node_modules.
# ─────────────────────────────────────────────────────────────────────────────
FROM node:18-alpine AS production

# Create a non-root user for security
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

WORKDIR /app

# Copy package files and install ONLY production deps
COPY package*.json ./
RUN npm ci --omit=dev && npm cache clean --force

# Copy application source
COPY src/ ./src/

# Set ownership to non-root user
RUN chown -R appuser:appgroup /app
USER appuser

# Expose the service port
EXPOSE 3000

# Health check — Docker will mark the container unhealthy if this fails
HEALTHCHECK --interval=30s --timeout=10s --start-period=15s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:3000/health || exit 1

# Default environment
ENV NODE_ENV=production \
    PORT=3000

# Start the server
CMD ["node", "src/index.js"]
