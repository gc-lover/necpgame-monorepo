import { createServer } from 'http';
import { config } from './config';
import { logger } from './utils/logger';
import { database } from './config/database';
import { redis } from './config/redis';
import { WebSocketServer } from './websocket/server';
import { AudioProcessorWorker } from './workers/audioProcessor';
import { CampaignExecutorWorker } from './workers/campaignExecutor';
import { AnalyticsCalculatorWorker } from './workers/analyticsCalculator';
import createApp from './app';

// Global references for graceful shutdown
let server: ReturnType<typeof createServer>;
let webSocketServer: WebSocketServer;
let audioProcessorWorker: AudioProcessorWorker;
let campaignExecutorWorker: CampaignExecutorWorker;
let analyticsCalculatorWorker: AnalyticsCalculatorWorker;

async function validateEnvironment(): Promise<void> {
  const requiredEnvVars = [
    'DATABASE_URL',
    'JWT_ACCESS_SECRET',
    'JWT_REFRESH_SECRET',
    'REDIS_URL'
  ];

  const missingVars = requiredEnvVars.filter(varName => !process.env[varName]);

  if (missingVars.length > 0) {
    throw new Error(`Missing required environment variables: ${missingVars.join(', ')}`);
  }

  // Validate critical configuration values
  if (config.app.port < 1000 || config.app.port > 65535) {
    throw new Error(`Invalid port number: ${config.app.port}`);
  }

  if (!config.database.url) {
    throw new Error('Database URL is required');
  }

  if (!config.redis.url) {
    throw new Error('Redis URL is required');
  }

  logger.info('Environment validation passed');
}

async function initializeDatabase(): Promise<void> {
  try {
    logger.info('Initializing database connection...');

    await database.connect();
    await database.healthCheck();

    logger.info('Database connection established');
  } catch (error) {
    logger.error('Failed to initialize database', { error: error.message });
    throw error;
  }
}

async function initializeRedis(): Promise<void> {
  try {
    logger.info('Initializing Redis connection...');

    await redis.connect();
    await redis.healthCheck();

    logger.info('Redis connection established');
  } catch (error) {
    logger.error('Failed to initialize Redis', { error: error.message });
    throw error;
  }
}

async function initializeWebSocket(httpServer: ReturnType<typeof createServer>): Promise<void> {
  try {
    logger.info('Initializing WebSocket server...');

    webSocketServer = new WebSocketServer(httpServer);

    // Store reference globally for access from other modules
    (global as any).io = webSocketServer;

    logger.info('WebSocket server initialized');
  } catch (error) {
    logger.error('Failed to initialize WebSocket server', { error: error.message });
    throw error;
  }
}

async function initializeWorkers(): Promise<void> {
  try {
    logger.info('Initializing background workers...');

    // Audio processing worker
    audioProcessorWorker = new AudioProcessorWorker();
    logger.info('Audio processor worker initialized');

    // Campaign executor worker
    campaignExecutorWorker = new CampaignExecutorWorker();
    logger.info('Campaign executor worker initialized');

    // Analytics calculator worker
    analyticsCalculatorWorker = new AnalyticsCalculatorWorker();
    logger.info('Analytics calculator worker initialized');

    logger.info('All background workers initialized');
  } catch (error) {
    logger.error('Failed to initialize workers', { error: error.message });
    throw error;
  }
}

async function startServer(): Promise<void> {
  try {
    // Validate environment before starting
    await validateEnvironment();

    // Initialize database
    await initializeDatabase();

    // Initialize Redis
    await initializeRedis();

    // Create Express app
    const app = createApp();

    // Create HTTP server
    server = createServer(app);

    // Initialize WebSocket server
    await initializeWebSocket(server);

    // Initialize background workers
    await initializeWorkers();

    // Start listening
    server.listen(config.app.port, config.app.host, () => {
      logger.info('Server started successfully', {
        host: config.app.host,
        port: config.app.port,
        environment: config.app.env,
        nodeVersion: process.version,
        pid: process.pid
      });

      // Log available endpoints
      logger.info('Available endpoints:', {
        health: `http://${config.app.host}:${config.app.port}/health`,
        apiHealth: `http://${config.app.host}:${config.app.port}/api/health`,
        apiDocs: config.app.env !== 'production' ? `http://${config.app.host}:${config.app.port}/api-docs` : 'N/A'
      });
    });

    // Handle server errors
    server.on('error', (error) => {
      logger.error('Server error', { error: error.message });
      process.exit(1);
    });

  } catch (error) {
    logger.error('Failed to start server', { error: error.message });
    process.exit(1);
  }
}

async function gracefulShutdown(signal: string): Promise<void> {
  logger.info(`Received ${signal}, starting graceful shutdown...`);

  try {
    // Stop accepting new connections
    if (server) {
      server.close((error) => {
        if (error) {
          logger.error('Error closing HTTP server', { error: error.message });
        } else {
          logger.info('HTTP server closed');
        }
      });
    }

    // Close WebSocket server
    if (webSocketServer) {
      await webSocketServer.close();
    }

    // Close workers
    const workerPromises = [];
    if (audioProcessorWorker) {
      workerPromises.push(audioProcessorWorker.close());
    }
    if (campaignExecutorWorker) {
      workerPromises.push(campaignExecutorWorker.close());
    }
    if (analyticsCalculatorWorker) {
      workerPromises.push(analyticsCalculatorWorker.close());
    }

    await Promise.allSettled(workerPromises);
    logger.info('Background workers closed');

    // Close database connection
    await database.close();
    logger.info('Database connection closed');

    // Close Redis connection
    await redis.close();
    logger.info('Redis connection closed');

    logger.info('Graceful shutdown completed');
    process.exit(0);

  } catch (error) {
    logger.error('Error during graceful shutdown', { error: error.message });
    process.exit(1);
  }
}

// Handle shutdown signals
process.on('SIGTERM', () => gracefulShutdown('SIGTERM'));
process.on('SIGINT', () => gracefulShutdown('SIGINT'));

// Handle uncaught exceptions
process.on('uncaughtException', (error) => {
  logger.error('Uncaught exception', { error: error.message, stack: error.stack });
  gracefulShutdown('uncaughtException');
});

// Handle unhandled promise rejections
process.on('unhandledRejection', (reason, promise) => {
  logger.error('Unhandled promise rejection', {
    reason: reason instanceof Error ? reason.message : reason,
    promise: promise.toString()
  });
  gracefulShutdown('unhandledRejection');
});

// Handle process warnings
process.on('warning', (warning) => {
  logger.warn('Process warning', {
    message: warning.message,
    name: warning.name,
    stack: warning.stack
  });
});

// Start the server
startServer().catch((error) => {
  logger.error('Failed to start server', { error: error.message });
  process.exit(1);
});
