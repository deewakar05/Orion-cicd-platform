/**
 * Dashboard Routes
 * Returns summary data for the authenticated user's dashboard.
 *
 * Note: summary counters are demo/seed data for portfolio demonstration.
 * In a production system, these would be queried from the PostgreSQL database.
 */

const express = require('express');
const router = express.Router();
const { authenticate } = require('../middleware/auth');
const logger = require('../utils/logger');

/**
 * GET /api/dashboard
 * Protected — requires a valid JWT token.
 * Returns aggregated platform summary data.
 */
router.get('/', authenticate, (req, res) => {
  try {
    logger.info(`Dashboard accessed by user: ${req.user.email}`);

    // Demo data — illustrates the expected response shape.
    // Replace with real DB queries when connecting Node.js to PostgreSQL.
    const dashboardData = {
      success: true,
      data: {
        user: req.user,
        summary: {
          totalDeployments: 12,
          successfulBuilds: 11,
          failedBuilds: 1,
          averageBuildTime: '2m 34s',
          activeContainers: 3,
          servicesRunning: ['node-service', 'java-service', 'postgres'],
        },
        recentActivity: [
          {
            id: 1,
            action: 'Build triggered',
            service: 'node-service',
            status: 'success',
            timestamp: new Date(Date.now() - 5 * 60 * 1000).toISOString(),
          },
          {
            id: 2,
            action: 'Docker image pushed',
            service: 'java-service',
            status: 'success',
            timestamp: new Date(Date.now() - 12 * 60 * 1000).toISOString(),
          },
          {
            id: 3,
            action: 'Deployment completed',
            service: 'docker-compose',
            status: 'success',
            timestamp: new Date(Date.now() - 20 * 60 * 1000).toISOString(),
          },
          {
            id: 4,
            action: 'Integration tests run',
            service: 'java-service',
            status: 'success',
            timestamp: new Date(Date.now() - 35 * 60 * 1000).toISOString(),
          },
        ],
        systemHealth: {
          nodeService: 'healthy',
          javaService: 'healthy',
          database: 'healthy',
        },
        timestamp: new Date().toISOString(),
      },
    };

    res.status(200).json(dashboardData);
  } catch (err) {
    logger.error(`Dashboard error: ${err.message}`);
    res.status(500).json({ success: false, message: 'Failed to load dashboard data' });
  }
});

module.exports = router;
