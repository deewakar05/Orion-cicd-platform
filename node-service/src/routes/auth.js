/**
 * Authentication Routes
 * Handles user login and signup with bcrypt password hashing and JWT generation.
 *
 * NOTE: User data is stored in-memory for demo/portfolio purposes.
 *       In a full production deployment, replace `users` Map with a
 *       PostgreSQL or MongoDB database integration (see Future Enhancements in README).
 */

const express = require('express');
const router = express.Router();
const jwt = require('jsonwebtoken');
const bcrypt = require('bcrypt');
const { validateLogin, validateSignup } = require('../middleware/validators');
const logger = require('../utils/logger');

// In-memory user store — swap with DB client (pg, mongoose, etc.) for persistence
const users = new Map();

const BCRYPT_SALT_ROUNDS = 10;
const JWT_SECRET = process.env.JWT_SECRET || 'devops-platform-secret-key';
const JWT_EXPIRES_IN = process.env.JWT_EXPIRES_IN || '1h';

/**
 * POST /api/auth/signup
 * Register a new user. Password is hashed with bcrypt before storage.
 */
router.post('/signup', validateSignup, async (req, res) => {
  try {
    const { username, email, password, role } = req.body;

    if (users.has(email)) {
      return res.status(409).json({
        success: false,
        message: 'User with this email already exists',
      });
    }

    // Hash the password before storing — bcrypt adds automatic salting
    const hashedPassword = await bcrypt.hash(password, BCRYPT_SALT_ROUNDS);

    const newUser = {
      id: Date.now().toString(),
      username,
      email,
      password: hashedPassword,
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
 * Authenticate a user. Uses bcrypt.compare to validate the hashed password.
 */
router.post('/login', validateLogin, async (req, res) => {
  try {
    const { email, password } = req.body;
    const user = users.get(email);

    if (!user) {
      return res.status(401).json({
        success: false,
        message: 'Invalid email or password',
      });
    }

    // Secure constant-time comparison — prevents timing attacks
    const passwordMatch = await bcrypt.compare(password, user.password);
    if (!passwordMatch) {
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
 * Stateless JWT logout — instructs client to discard the token.
 */
router.post('/logout', (req, res) => {
  res.status(200).json({ success: true, message: 'Logged out successfully' });
});

module.exports = router;
