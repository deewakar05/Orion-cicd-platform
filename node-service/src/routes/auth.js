/**
 * Authentication Routes
 * Handles user login and signup with JWT generation.
 */

const express = require('express');
const router = express.Router();
const jwt = require('jsonwebtoken');
const { validateLogin, validateSignup } = require('../middleware/validators');
const logger = require('../utils/logger');

// In-memory user store (replace with a real DB in production)
const users = new Map();

const JWT_SECRET = process.env.JWT_SECRET || 'devops-platform-secret-key';
const JWT_EXPIRES_IN = process.env.JWT_EXPIRES_IN || '1h';

/**
 * POST /api/auth/signup
 * Register a new user
 */
router.post('/signup', validateSignup, (req, res) => {
  try {
    const { username, email, password, role } = req.body;

    if (users.has(email)) {
      return res.status(409).json({
        success: false,
        message: 'User with this email already exists',
      });
    }

    // Store user (in production, hash the password with bcrypt)
    const newUser = {
      id: Date.now().toString(),
      username,
      email,
      password, // NOTE: hash this in production!
      role: role || 'user',
      createdAt: new Date().toISOString(),
    };
    users.set(email, newUser);

    const token = jwt.sign(
      { id: newUser.id, email: newUser.email, role: newUser.role },
      JWT_SECRET,
      { expiresIn: JWT_EXPIRES_IN }
    );

    logger.info(`New user registered: ${email}`);

    return res.status(201).json({
      success: true,
      message: 'User registered successfully',
      data: {
        user: { id: newUser.id, username, email, role: newUser.role },
        token,
      },
    });
  } catch (err) {
    logger.error(`Signup error: ${err.message}`);
    return res.status(500).json({ success: false, message: 'Internal server error' });
  }
});

/**
 * POST /api/auth/login
 * Authenticate a user and return a JWT
 */
router.post('/login', validateLogin, (req, res) => {
  try {
    const { email, password } = req.body;
    const user = users.get(email);

    if (!user || user.password !== password) {
      return res.status(401).json({
        success: false,
        message: 'Invalid email or password',
      });
    }

    const token = jwt.sign(
      { id: user.id, email: user.email, role: user.role },
      JWT_SECRET,
      { expiresIn: JWT_EXPIRES_IN }
    );

    logger.info(`User logged in: ${email}`);

    return res.status(200).json({
      success: true,
      message: 'Login successful',
      data: {
        user: { id: user.id, username: user.username, email: user.email, role: user.role },
        token,
        expiresIn: JWT_EXPIRES_IN,
      },
    });
  } catch (err) {
    logger.error(`Login error: ${err.message}`);
    return res.status(500).json({ success: false, message: 'Internal server error' });
  }
});

/**
 * POST /api/auth/logout
 * Invalidate session (client should delete the token)
 */
router.post('/logout', (req, res) => {
  // Stateless JWT — instruct client to discard token
  res.status(200).json({ success: true, message: 'Logged out successfully' });
});

module.exports = router;
