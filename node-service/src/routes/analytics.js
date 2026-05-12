/**
 * Analytics Routes — API Gateway Proxy
 * Forwards analytics requests to the Java Spring Boot microservice.
 */

const express = require('express');
const router = express.Router();
const axios = require('axios');
const { authenticate } = require('../middleware/auth');
const logger = require('../utils/logger');

const JAVA_SERVICE_URL = process.env.JAVA_SERVICE_URL || 'http://java-service:8080';
const REQUEST_TIMEOUT = parseInt(process.env.JAVA_REQUEST_TIMEOUT || '5000', 10);

/**
 * Generic proxy helper — forwards request to Java service and returns response.
 */
const proxyToJava = async (path, res) => {
  try {
    const response = await axios.get(`${JAVA_SERVICE_URL}${path}`, {
      timeout: REQUEST_TIMEOUT,
      headers: { 'X-Gateway-Source': 'node-api-gateway' },
    });
    return res.status(response.status).json(response.data);
  } catch (err) {
    if (err.code === 'ECONNREFUSED' || err.code === 'ECONNABORTED') {
      logger.error(`Java service unavailable at ${JAVA_SERVICE_URL}: ${err.message}`);
      return res.status(503).json({
        success: false,
        message: 'Analytics service is temporarily unavailable',
        downstream: 'java-service',
      });
    }
    logger.error(`Proxy error for ${path}: ${err.message}`);
    return res.status(500).json({ success: false, message: 'Gateway proxy error' });
  }
};

/**
 * GET /api/analytics
 * Proxies to GET /reports on the Java service — general analytics overview.
 */
router.get('/', authenticate, async (req, res) => {
  logger.info(`Analytics overview requested by: ${req.user.email}`);
  await proxyToJava('/reports', res);
});

/**
 * GET /api/analytics/logs
 * Proxies to GET /logs on the Java service.
 */
router.get('/logs', authenticate, async (req, res) => {
  logger.info(`Logs requested by: ${req.user.email}`);
  await proxyToJava('/logs', res);
});

/**
 * GET /api/analytics/metrics
 * Proxies to GET /metrics on the Java service.
 */
router.get('/metrics', authenticate, async (req, res) => {
  logger.info(`Metrics requested by: ${req.user.email}`);
  await proxyToJava('/metrics', res);
});

module.exports = router;
