/**
 * Dashboard Routes
 * Returns summary data for the authenticated user's dashboard.
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

    const dashboardData = {
      success: true,
      data: {
        user: req.user,
        summary: {
          totalDeployments: 142,
          successfulBuilds: 138,
          failedBuilds: 4,
          averageBuildTime: '2m 34s',
          activeContainers: 6,
          servicesRunning: ['node-service', 'java-service'],
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
            action: 'Test suite run',
            service: 'java-service',
            status: 'failed',
            timestamp: new Date(Date.now() - 35 * 60 * 1000).toISOString(),
          },
        ],
        systemHealth: {
          nodeService: 'healthy',
          javaService: 'healthy',
          database: 'healthy',
          cache: 'healthy',
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
