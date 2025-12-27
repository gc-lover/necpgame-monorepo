import express from 'express';
import cors from 'cors';
import helmet from 'helmet';
import compression from 'compression';
import { createServer } from 'http';
import swaggerUi from 'swagger-ui-express';
import { config } from './config';
import { logger } from './utils/logger';

// Import middleware
import { errorHandler, notFoundHandler } from './api/middlewares/errorHandler';
import { requestLogger } from './api/middlewares/requestLogger';
import { rateLimiter } from './api/middlewares/rateLimiter';

// Import routes
import { router as authRoutes } from './api/routes/auth';
import { router as userRoutes } from './api/routes/users';
import { router as campaignRoutes } from './api/routes/campaigns';
import { router as flowRoutes } from './api/routes/flows';
import { router as sessionRoutes } from './api/routes/sessions';
import { router as analyticsRoutes } from './api/routes/analytics';
import { router as webhookRoutes } from './api/routes/webhooks';
import { router as configRoutes } from './api/routes/config';

export function createApp() {
  const app = express();

  // Security middleware
  app.use(helmet({
    contentSecurityPolicy: {
      directives: {
        defaultSrc: ["'self'"],
        styleSrc: ["'self'", "'unsafe-inline'"],
        scriptSrc: ["'self'"],
        imgSrc: ["'self'", "data:", "https:"],
      },
    },
    hsts: {
      maxAge: 31536000,
      includeSubDomains: true,
      preload: true
    }
  }));

  // CORS configuration
  app.use(cors({
    origin: config.cors.allowedOrigins,
    credentials: true,
    methods: ['GET', 'POST', 'PUT', 'DELETE', 'PATCH', 'OPTIONS'],
    allowedHeaders: [
      'Content-Type',
      'Authorization',
      'X-Requested-With',
      'Accept',
      'Origin'
    ],
    exposedHeaders: ['X-Total-Count', 'X-Rate-Limit-Remaining'],
    maxAge: 86400 // 24 hours
  }));

  // Compression middleware
  app.use(compression({
    level: 6, // Good balance between compression and performance
    threshold: 1024, // Only compress responses larger than 1KB
    filter: (req, res) => {
      // Don't compress responses with this request header
      if (req.headers['x-no-compression']) {
        return false;
      }
      // Use compression for all other responses
      return compression.filter(req, res);
    }
  }));

  // Body parsing middleware
  app.use(express.json({
    limit: '10mb',
    strict: true,
    verify: (req, res, buf) => {
      // Store raw body for webhook signature verification
      (req as any).rawBody = buf;
    }
  }));

  app.use(express.urlencoded({
    extended: true,
    limit: '10mb'
  }));

  // Request logging middleware
  app.use(requestLogger);

  // Rate limiting middleware
  app.use('/api', rateLimiter);

  // Health check endpoint (no authentication required)
  app.get('/health', (req, res) => {
    res.status(200).json({
      status: 'healthy',
      timestamp: new Date().toISOString(),
      uptime: process.uptime(),
      version: process.env.npm_package_version || '1.0.0',
      environment: config.app.env
    });
  });

  // API documentation endpoint
  if (config.app.env !== 'production') {
    // Load OpenAPI spec dynamically
    try {
      const openApiSpec = require('../proto/openapi/main.yaml');
      app.use('/api-docs', swaggerUi.serve, swaggerUi.setup(openApiSpec, {
        explorer: true,
        swaggerOptions: {
          docExpansion: 'none',
          filter: true,
          showRequestHeaders: true
        }
      }));

      // Serve raw OpenAPI spec
      app.get('/api-docs.json', (req, res) => {
        res.setHeader('Content-Type', 'application/json');
        res.send(openApiSpec);
      });

      logger.info('OpenAPI documentation enabled at /api-docs');
    } catch (error) {
      logger.warn('OpenAPI spec not found, documentation disabled', { error: error.message });
    }
  }

  // API versioning
  const apiVersion = '/api/v1';

  // Mount routes with versioning
  app.use(`${apiVersion}/auth`, authRoutes);
  app.use(`${apiVersion}/users`, userRoutes);
  app.use(`${apiVersion}/campaigns`, campaignRoutes);
  app.use(`${apiVersion}/flows`, flowRoutes);
  app.use(`${apiVersion}/sessions`, sessionRoutes);
  app.use(`${apiVersion}/analytics`, analyticsRoutes);
  app.use(`${apiVersion}/webhooks`, webhookRoutes);
  app.use(`${apiVersion}/config`, configRoutes);

  // Legacy API routes (redirect to v1)
  app.use('/api/auth', authRoutes);
  app.use('/api/users', userRoutes);
  app.use('/api/campaigns', campaignRoutes);
  app.use('/api/flows', flowRoutes);
  app.use('/api/sessions', sessionRoutes);
  app.use('/api/analytics', analyticsRoutes);
  app.use('/api/webhooks', webhookRoutes);
  app.use('/api/config', configRoutes);

  // Public API health check
  app.get('/api/health', (req, res) => {
    res.status(200).json({
      status: 'healthy',
      timestamp: new Date().toISOString(),
      uptime: process.uptime(),
      version: process.env.npm_package_version || '1.0.0',
      environment: config.app.env,
      api: {
        version: 'v1',
        baseUrl: `${req.protocol}://${req.get('host')}${apiVersion}`
      }
    });
  });

  // Static file serving for uploads (if needed)
  // app.use('/uploads', express.static(config.uploadPath));

  // 404 handler - must be after all routes
  app.use(notFoundHandler);

  // Global error handler - must be last
  app.use(errorHandler);

  logger.info('Express application configured', {
    corsOrigins: config.cors.allowedOrigins.length,
    apiVersion,
    environment: config.app.env,
    compression: true,
    helmet: true
  });

  return app;
}

// For testing purposes, export app creation function
export default createApp;
