/**
 * Global Error Handler Middleware
 * Catches any unhandled errors and returns a consistent JSON response.
 */

const logger = require('../utils/logger');

const errorHandler = (err, req, res, next) => {
  const statusCode = err.statusCode || err.status || 500;
  const message = err.message || 'An unexpected error occurred';

  logger.error(`[${req.method}] ${req.path} - ${statusCode}: ${message}`);

  res.status(statusCode).json({
    success: false,
    error: {
      message,
      ...(process.env.NODE_ENV !== 'production' && { stack: err.stack }),
    },
    timestamp: new Date().toISOString(),
  });
};

module.exports = { errorHandler };
