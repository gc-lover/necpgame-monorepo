package service

import (
	"context"
	"net/http"

	"github.com/go-faster/errors"
	"github.com/gorilla/websocket"
	"github.com/jackc/pgx/v5/pgxpool"
	"github.com/redis/go-redis/v9"
	"go.opentelemetry.io/otel/metric"
	"go.uber.org/zap"

	"github.com/gc-lover/necp-game/services/realtime-gateway-service-go/internal/service/buffer"
	"github.com/gc-lover/necp-game/services/realtime-gateway-service-go/internal/service/protobuf"
	"github.com/gc-lover/necp-game/services/realtime-gateway-service-go/internal/service/session"
)

// Config holds service configuration
type Config struct {
	HTTPAddr      string
	WebSocketAddr string
	DatabaseURL   string
	RedisURL      string
	KafkaBrokers  string
	EventStoreURL string
	Logger        *zap.Logger
}

// Service represents the main realtime gateway service
type Service struct {
	config      Config
	logger      *zap.Logger
	db          *pgxpool.Pool
	redis       *redis.Client
	meter       metric.Meter

	// Core components
	sessionManager *session.Manager
	protobufHandler *protobuf.Handler
	bufferPool    *buffer.Pool

	// HTTP handlers
	httpHandler   *HTTPHandler
	wsHandler     *WebSocketHandler

	// WebSocket upgrader
	upgrader      websocket.Upgrader
}

// NewService creates a new realtime gateway service
func NewService(config Config) (*Service, error) {
	if config.Logger == nil {
		return nil, errors.New("logger is required")
	}

	svc := &Service{
		config: config,
		logger: config.Logger,
		upgrader: websocket.Upgrader{
			ReadBufferSize:  1024,
			WriteBufferSize: 1024,
			CheckOrigin: func(r *http.Request) bool {
				return svc.checkOrigin(r)
			},
		},
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
	s.meter = metric.NewMeterProvider().Meter("realtime-gateway-service")

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
	s.bufferPool = buffer.NewPool(buffer.Config{
		InitialSize: 1000,
		MaxSize:     10000,
		Logger:      s.logger,
	})

	s.protobufHandler = protobuf.NewHandler(protobuf.Config{
		BufferPool:     s.bufferPool,
		Logger:         s.logger,
		Meter:          s.meter,
		SessionManager: nil, // Will be set after sessionManager creation
	})

	s.sessionManager = session.NewManager(session.Config{
		DB:          s.db,
		Redis:       s.redis,
		ProtobufHandler: s.protobufHandler,
		Logger:      s.logger,
		Meter:       s.meter,
	})

	// Set session manager in protobuf handler for message sending
	if ph, ok := s.protobufHandler.(*protobuf.Handler); ok {
		ph.SetSessionManager(s.sessionManager)
	}

	// Initialize HTTP handlers
	s.httpHandler = NewHTTPHandler(s)
	s.wsHandler = NewWebSocketHandler(s)

	s.logger.Info("All components initialized successfully")
	return nil
}

// HTTPHandler returns the HTTP handler for the service
func (s *Service) HTTPHandler() http.Handler {
	return s.httpHandler
}

// WebSocketHandler returns the WebSocket handler for the service
func (s *Service) WebSocketHandler() http.Handler {
	return s.wsHandler
}

// checkOrigin validates WebSocket connection origins for security
func (s *Service) checkOrigin(r *http.Request) bool {
	origin := r.Header.Get("Origin")

	// Allow localhost for development
	if origin == "http://localhost:3000" || origin == "http://localhost:8080" {
		return true
	}

	// Allow production domains
	allowedOrigins := []string{
		"https://necpgame.com",
		"https://www.necpgame.com",
		"https://api.necpgame.com",
		"https://staging.necpgame.com",
	}

	for _, allowed := range allowedOrigins {
		if origin == allowed {
			return true
		}
	}

	s.logger.Warn("rejected WebSocket connection from unauthorized origin",
		zap.String("origin", origin),
		zap.String("remote_addr", r.RemoteAddr))

	return false
}

// Start starts all service components
func (s *Service) Start(ctx context.Context) error {
	s.logger.Info("Starting service components")

	if err := s.sessionManager.Start(ctx); err != nil {
		return errors.Wrap(err, "failed to start session manager")
	}

	s.logger.Info("Service components started successfully")
	return nil
}

// Stop stops all service components
func (s *Service) Stop(ctx context.Context) error {
	s.logger.Info("Stopping service components")

	if err := s.sessionManager.Stop(ctx); err != nil {
		s.logger.Error("failed to stop session manager", zap.Error(err))
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

	return health, nil
}

// HealthResponse represents health check response
type HealthResponse struct {
	Status   string            `json:"status"`
	Services map[string]string `json:"services,omitempty"`
}
