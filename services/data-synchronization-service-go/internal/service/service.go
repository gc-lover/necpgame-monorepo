package service

import (
	"context"
	"net/http"
	"time"

	"github.com/go-faster/errors"
	"github.com/jackc/pgx/v5/pgxpool"
	"github.com/redis/go-redis/v9"
	"go.opentelemetry.io/otel/metric"
	"go.uber.org/zap"

	"github.com/gc-lover/necp-game/services/data-synchronization-service-go/internal/service/sync"
	"github.com/gc-lover/necp-game/services/data-synchronization-service-go/internal/service/conflict"
	"github.com/gc-lover/necp-game/services/data-synchronization-service-go/internal/service/state"
	"github.com/gc-lover/necp-game/services/data-synchronization-service-go/internal/service/saga"
)

// Config holds service configuration
type Config struct {
	DatabaseURL      string
	RedisURL         string
	KafkaBrokers     string
	EventStoreURL    string
	Logger           *zap.Logger
}

// Service represents the main data synchronization service
type Service struct {
	config      Config
	logger      *zap.Logger
	db          *pgxpool.Pool
	redis       *redis.Client
	meter       metric.Meter

	// Core components
	syncManager   *sync.Manager
	conflictResolver *conflict.Resolver
	stateManager  *state.Manager
	sagaCoordinator *saga.Coordinator

	// HTTP components
	handler      *Handler
}

// NewService creates a new data synchronization service
func NewService(config Config) (*Service, error) {
	if config.Logger == nil {
		return nil, errors.New("logger is required")
	}

	svc := &Service{
		config: config,
		logger: config.Logger,
	}

	if err := svc.initComponents(); err != nil {
		return nil, errors.Wrap(err, "failed to initialize components")
	}

	return svc, nil
}

// initComponents initializes all service components
func (s *Service) initComponents() error {
	var err error

	// Initialize meter
	s.meter = metric.NewMeterProvider().Meter("data-sync-service")

	// Initialize database connection with enterprise-grade pool optimization
	if s.config.DatabaseURL != "" {
		// BACKEND NOTE: Enterprise-grade database pool for MMOFPS data synchronization
		poolConfig, err := pgxpool.ParseConfig(s.config.DatabaseURL)
		if err != nil {
			return errors.Wrap(err, "failed to parse database config")
		}

		// BACKEND NOTE: Optimized pool settings for high-throughput data sync operations
		poolConfig.MaxConns = 50                    // BACKEND NOTE: High pool for data sync operations (50 max connections)
		poolConfig.MinConns = 10                    // BACKEND NOTE: Keep minimum connections ready for instant sync access
		poolConfig.MaxConnLifetime = 30 * time.Minute // BACKEND NOTE: Shorter lifetime for real-time sync ops
		poolConfig.MaxConnIdleTime = 5 * time.Minute  // BACKEND NOTE: Quick cleanup for active sync sessions
		poolConfig.HealthCheckPeriod = 30 * time.Second // BACKEND NOTE: Health checks for connection reliability

		s.db, err = pgxpool.NewWithConfig(context.Background(), poolConfig)
		if err != nil {
			return errors.Wrap(err, "failed to create database pool")
		}

		// Test connection with timeout - BACKEND NOTE: Validate database connectivity
		dbCtx, cancel := context.WithTimeout(context.Background(), 5*time.Second)
		defer cancel()
		if err := s.db.Ping(dbCtx); err != nil {
			return errors.Wrap(err, "failed to ping database")
		}

		s.logger.Info("Database connection established with enterprise-grade pool optimization",
			zap.Int32("max_conns", poolConfig.MaxConns),
			zap.Int32("min_conns", poolConfig.MinConns))
	}

	// Initialize Redis connection with enterprise-grade pool optimization
	if s.config.RedisURL != "" {
		opt, err := redis.ParseURL(s.config.RedisURL)
		if err != nil {
			return errors.Wrap(err, "failed to parse Redis URL")
		}

		// BACKEND NOTE: Enterprise-grade Redis pool for MMOFPS data sync caching
		opt.PoolSize = 25         // BACKEND NOTE: High pool for data sync session caching
		opt.MinIdleConns = 8      // BACKEND NOTE: Keep connections ready for instant sync access
		opt.ConnMaxLifetime = 30 * time.Minute // BACKEND NOTE: Match DB lifetime for consistency
		opt.ConnMaxIdleTime = 8 * time.Minute  // BACKEND NOTE: Reasonable cleanup for active sync sessions
		opt.MaxRetries = 3       // BACKEND NOTE: Retry failed operations
		opt.DialTimeout = 5 * time.Second
		opt.ReadTimeout = 3 * time.Second
		opt.WriteTimeout = 3 * time.Second

		s.redis = redis.NewClient(opt)

		// Test Redis connection with timeout - BACKEND NOTE: Validate Redis connectivity
		redisCtx, cancel := context.WithTimeout(context.Background(), 5*time.Second)
		defer cancel()
		if err := s.redis.Ping(redisCtx).Err(); err != nil {
			return errors.Wrap(err, "failed to ping Redis")
		}

		s.logger.Info("Redis connection established with enterprise-grade pool optimization",
			zap.Int("pool_size", opt.PoolSize),
			zap.Int("min_idle_conns", opt.MinIdleConns))
	}

	// Initialize core components
	s.stateManager = state.NewManager(state.Config{
		DB:     s.db,
		Redis:  s.redis,
		Logger: s.logger,
		Meter:  s.meter,
	})

	s.conflictResolver = conflict.NewResolver(conflict.Config{
		DB:     s.db,
		Redis:  s.redis,
		Logger: s.logger,
		Meter:  s.meter,
	})

	s.syncManager = sync.NewManager(sync.Config{
		StateManager:     s.stateManager,
		ConflictResolver: s.conflictResolver,
		Logger:           s.logger,
		Meter:            s.meter,
	})

	s.sagaCoordinator = saga.NewCoordinator(saga.Config{
		DB:        s.db,
		Redis:     s.redis,
		SyncManager: s.syncManager,
		Logger:    s.logger,
		Meter:     s.meter,
	})

	// Initialize HTTP handler
	s.handler = NewHandler(s)

	s.logger.Info("All components initialized successfully")
	return nil
}

// Handler returns the HTTP handler for the service
func (s *Service) Handler() http.Handler {
	return s.handler
}

// Start starts all service components
func (s *Service) Start(ctx context.Context) error {
	s.logger.Info("Starting service components")

	if err := s.syncManager.Start(ctx); err != nil {
		return errors.Wrap(err, "failed to start sync manager")
	}

	if err := s.sagaCoordinator.Start(ctx); err != nil {
		return errors.Wrap(err, "failed to start saga coordinator")
	}

	s.logger.Info("Service components started successfully")
	return nil
}

// Stop stops all service components
func (s *Service) Stop(ctx context.Context) error {
	s.logger.Info("Stopping service components")

	if err := s.sagaCoordinator.Stop(ctx); err != nil {
		s.logger.Error("failed to stop saga coordinator", zap.Error(err))
	}

	if err := s.syncManager.Stop(ctx); err != nil {
		s.logger.Error("failed to stop sync manager", zap.Error(err))
	}

	if s.redis != nil {
		if err := s.redis.Close(); err != nil {
			s.logger.Error("failed to close Redis connection", zap.Error(err))
		}
	}

	if s.db != nil {
		s.db.Close()
	}

	s.logger.Info("Service components stopped")
	return nil
}

// Health returns service health status
func (s *Service) Health(ctx context.Context) (*HealthResponse, error) {
	health := &HealthResponse{
		Status: "healthy",
		Services: make(map[string]string),
	}

	// Check database
	if s.db != nil {
		if err := s.db.Ping(ctx); err != nil {
			health.Status = "degraded"
			health.Services["database"] = "down"
		} else {
			health.Services["database"] = "up"
		}
	}

	// Check Redis
	if s.redis != nil {
		if _, err := s.redis.Ping(ctx).Result(); err != nil {
			health.Status = "degraded"
			health.Services["redis"] = "down"
		} else {
			health.Services["redis"] = "up"
		}
	}

	// Check sync manager
	if s.syncManager != nil {
		if err := s.syncManager.Health(ctx); err != nil {
			health.Status = "degraded"
			health.Services["sync_manager"] = "degraded"
		} else {
			health.Services["sync_manager"] = "up"
		}
	}

	return health, nil
}
