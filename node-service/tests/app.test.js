/**
 * Node.js Service — Test Suite
 * Tests auth routes and core API behavior using Jest + Supertest.
 */

const request = require('supertest');
const app = require('../src/index');

describe('Health Check', () => {
  it('GET /health should return 200 and service status', async () => {
    const res = await request(app).get('/health');
    expect(res.statusCode).toBe(200);
    expect(res.body.status).toBe('UP');
    expect(res.body.service).toBe('node-api-gateway');
  });
});

describe('Auth Routes', () => {
  const testUser = {
    username: 'testuser',
    email: 'test@devops.com',
    password: 'Test@1234',
    role: 'developer',
  };

  let authToken = '';

  it('POST /api/auth/signup — should register a new user', async () => {
    const res = await request(app).post('/api/auth/signup').send(testUser);
    expect(res.statusCode).toBe(201);
    expect(res.body.success).toBe(true);
    expect(res.body.data.token).toBeDefined();
    authToken = res.body.data.token;
  });

  it('POST /api/auth/signup — should reject duplicate email', async () => {
    const res = await request(app).post('/api/auth/signup').send(testUser);
    expect(res.statusCode).toBe(409);
    expect(res.body.success).toBe(false);
  });

  it('POST /api/auth/signup — should reject invalid data', async () => {
    const res = await request(app).post('/api/auth/signup').send({ email: 'bad-email', password: '123' });
    expect(res.statusCode).toBe(400);
    expect(res.body.errors).toBeDefined();
  });

  it('POST /api/auth/login — should authenticate registered user', async () => {
    const res = await request(app)
      .post('/api/auth/login')
      .send({ email: testUser.email, password: testUser.password });
    expect(res.statusCode).toBe(200);
    expect(res.body.success).toBe(true);
    expect(res.body.data.token).toBeDefined();
    authToken = res.body.data.token;
  });

  it('POST /api/auth/login — should reject wrong credentials', async () => {
    const res = await request(app)
      .post('/api/auth/login')
      .send({ email: testUser.email, password: 'wrongpassword' });
    expect(res.statusCode).toBe(401);
    expect(res.body.success).toBe(false);
  });

  it('GET /api/dashboard — should return data with valid token', async () => {
    const res = await request(app)
      .get('/api/dashboard')
      .set('Authorization', `Bearer ${authToken}`);
    expect(res.statusCode).toBe(200);
    expect(res.body.data.summary).toBeDefined();
  });

  it('GET /api/dashboard — should reject request without token', async () => {
    const res = await request(app).get('/api/dashboard');
    expect(res.statusCode).toBe(401);
    expect(res.body.success).toBe(false);
  });

  it('POST /api/auth/logout — should return success', async () => {
    const res = await request(app).post('/api/auth/logout');
    expect(res.statusCode).toBe(200);
    expect(res.body.success).toBe(true);
  });
});

describe('404 Handling', () => {
  it('Unknown route should return 404', async () => {
    const res = await request(app).get('/unknown-route');
    expect(res.statusCode).toBe(404);
    expect(res.body.error).toBeDefined();
  });
});
