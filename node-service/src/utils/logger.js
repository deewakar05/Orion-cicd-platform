/**
 * Application Logger
 * Simple structured logger using console with timestamps and log levels.
 * In production, swap this for Winston or Pino for file transports.
 */

const LOG_LEVEL = process.env.LOG_LEVEL || 'info';
const LEVELS = { error: 0, warn: 1, info: 2, debug: 3 };

const currentLevel = LEVELS[LOG_LEVEL] ?? LEVELS.info;

const format = (level, message) => {
  const timestamp = new Date().toISOString();
  return `[${timestamp}] [${level.toUpperCase()}] ${message}`;
};

const logger = {
  error: (msg) => LEVELS.error <= currentLevel && console.error(format('error', msg)),
  warn:  (msg) => LEVELS.warn  <= currentLevel && console.warn(format('warn',  msg)),
  info:  (msg) => LEVELS.info  <= currentLevel && console.log(format('info',  msg)),
  debug: (msg) => LEVELS.debug <= currentLevel && console.log(format('debug', msg)),
};

module.exports = logger;
