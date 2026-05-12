/**
 * Multi-Service CI/CD Automation Platform
 * Node.js API Gateway Service
 *
 * This service acts as an API Gateway that:
 * - Handles user authentication (login/signup)
 * - Proxies analytics requests to the Java Spring Boot microservice
 * - Serves the dashboard endpoint
 */

const express = require('express');
const cors = require('cors');
const morgan = require('morgan');
const axios = require('axios');

const authRoutes = require('./routes/auth');
const dashboardRoutes = require('./routes/dashboard');
const analyticsRoutes = require('./routes/analytics');
const { errorHandler } = require('./middleware/errorHandler');
const logger = require('./utils/logger');

const app = express();

// ─── Middleware ───────────────────────────────────────────────────────────────
app.use(cors());
app.use(express.json());
app.use(express.urlencoded({ extended: true }));
app.use(morgan('combined', { stream: { write: (msg) => logger.info(msg.trim()) } }));

// ─── Health Check ─────────────────────────────────────────────────────────────
app.get('/health', (req, res) => {
  res.status(200).json({
    status: 'UP',
    service: 'node-api-gateway',
    timestamp: new Date().toISOString(),
    version: process.env.npm_package_version || '1.0.0',
  });
});

// ─── Routes ──────────────────────────────────────────────────────────────────
app.use('/api/auth', authRoutes);
app.use('/api/dashboard', dashboardRoutes);
app.use('/api/analytics', analyticsRoutes);

// ─── 404 Handler ─────────────────────────────────────────────────────────────
app.use((req, res) => {
  res.status(404).json({ error: 'Route not found', path: req.originalUrl });
});

// ─── Global Error Handler ─────────────────────────────────────────────────────
app.use(errorHandler);

// ─── Start Server ─────────────────────────────────────────────────────────────
// Only start the HTTP server when this file is run directly.
// When required by tests (supertest), the server is not bound to a port.
/* istanbul ignore next */
if (require.main === module) {
  const PORT = process.env.PORT || 3000;
  app.listen(PORT, () => {
    logger.info(`🚀 Node.js API Gateway running on port ${PORT}`);
    logger.info(`🔗 Java microservice URL: ${process.env.JAVA_SERVICE_URL || 'http://java-service:8080'}`);
  });
}

module.exports = app; // exported for testing
