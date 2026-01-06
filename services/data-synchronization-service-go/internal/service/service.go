package service

import (
	"context"
	"net/http"

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

	// Initialize database connection
	if s.config.DatabaseURL != "" {
		s.db, err = pgxpool.New(context.Background(), s.config.DatabaseURL)
		if err != nil {
			return errors.Wrap(err, "failed to connect to database")
		}
	}

	// Initialize Redis connection
	if s.config.RedisURL != "" {
		opt, err := redis.ParseURL(s.config.RedisURL)
		if err != nil {
			return errors.Wrap(err, "failed to parse Redis URL")
		}
		s.redis = redis.NewClient(opt)
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
