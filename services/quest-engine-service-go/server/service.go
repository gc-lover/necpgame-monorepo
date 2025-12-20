// Package server Issue: #176
package server

import (
	"context"
	"fmt"
	"net/http"
	"sync"
	"time"

	"github.com/jackc/pgx/v5/pgxpool"
	"github.com/valyala/fasthttp"
	"go.uber.org/zap"
	"golang.org/x/sync/semaphore"
	"golang.org/x/time/rate"
)

// Config holds service configuration with performance optimizations
type Config struct {
	HTTPPort        int
	WebSocketPort   int
	DatabaseURL     string
	KafkaBrokers    string
	RedisURL        string
	PrometheusPort  int
	Environment     string
	MaxConnections  int
	WorkerPoolSize  int
	BufferSize      int
	ContextTimeout  time.Duration
	ReadTimeout     time.Duration
	WriteTimeout    time.Duration
	MaxRequestSize  int64
	RateLimitRPM    int
	EnableMetrics   bool
	EnableTracing   bool
	EnableDebugLogs bool
}

// Service represents the quest engine service
type Service struct {
	config     Config
	logger     *zap.SugaredLogger
	db         *pgxpool.Pool
	kafka      *kafka.Writer
	httpServer *fasthttp.Server
	wsServer   *fasthttp.Server

	// Performance optimizations
	rateLimiter *rate.Limiter
	semaphore   *semaphore.Weighted
	workerPool  chan func()
	bufferPool  *sync.Pool
	contextPool *sync.Pool

	// Quest system managers
	questManager     *QuestManager
	objectiveManager *ObjectiveManager
	dialogueManager  *DialogueManager
	rewardManager    *RewardManager

	// Metrics and monitoring
	metrics *QuestMetrics

	// Cleanup
	shutdownCh chan struct{}
	wg         sync.WaitGroup
}

// NewService creates a new quest engine service instance
func NewService(config Config) (*Service, error) {
	logger, err := zap.NewProduction()
	if err != nil {
		return nil, fmt.Errorf("failed to create logger: %w", err)
	}

	sugar := logger.Sugar()

	// Initialize performance optimizations
	rateLimiter := rate.NewLimiter(rate.Limit(config.RateLimitRPM/60.0), config.RateLimitRPM)
	sem := semaphore.NewWeighted(int64(config.MaxConnections))

	workerPool := make(chan func(), config.WorkerPoolSize)

	bufferPool := &sync.Pool{
		New: func() interface{} {
			return make([]byte, config.BufferSize)
		},
	}

	contextPool := &sync.Pool{
		New: func() interface{} {
			ctx, cancel := context.WithTimeout(context.Background(), config.ContextTimeout)
			return &ContextWithCancel{Context: ctx, Cancel: cancel}
		},
	}

	return &Service{
		config:      config,
		logger:      sugar,
		rateLimiter: rateLimiter,
		semaphore:   sem,
		workerPool:  workerPool,
		bufferPool:  bufferPool,
		contextPool: contextPool,
		shutdownCh:  make(chan struct{}),
	}, nil
}

// Initialize initializes service components with performance optimizations
func (s *Service) Initialize(ctx context.Context) error {
	s.logger.Info("Initializing Quest Engine Service...")

	// Initialize database connection with optimized settings
	dbConfig, err := pgxpool.ParseConfig(s.config.DatabaseURL)
	if err != nil {
		return fmt.Errorf("failed to parse database URL: %w", err)
	}

	// Performance optimizations for database
	dbConfig.MaxConns = 25
	dbConfig.MinConns = 5
	dbConfig.MaxConnLifetime = time.Hour
	dbConfig.MaxConnIdleTime = 30 * time.Minute
	dbConfig.HealthCheckPeriod = 1 * time.Minute

	s.db, err = pgxpool.NewWithConfig(ctx, dbConfig)
	if err != nil {
		return fmt.Errorf("failed to connect to database: %w", err)
	}

	// Initialize Kafka writer with optimizations
	s.kafka = &kafka.Writer{
		Addr:         kafka.TCP(s.config.KafkaBrokers),
		Topic:        "quest.events",
		Balancer:     &kafka.LeastBytes{},
		BatchSize:    100,
		BatchTimeout: 10 * time.Millisecond,
		Compression:  kafka.Lz4,
		Async:        true, // Async for performance
	}

	// Initialize quest system managers with optimized memory layout
	s.questManager = NewQuestManager(s.db, s.kafka, s.logger)
	s.objectiveManager = NewObjectiveManager(s.db, s.kafka, s.logger)
	s.dialogueManager = NewDialogueManager(s.db, s.kafka, s.logger)
	s.rewardManager = NewRewardManager(s.db, s.kafka, s.logger)

	// Initialize metrics
	if s.config.EnableMetrics {
		s.metrics = NewQuestMetrics()
	}

	// Start worker pool
	for i := 0; i < s.config.WorkerPoolSize; i++ {
		s.wg.Add(1)
		go s.worker(i)
	}

	// Start background cleanup routines
	s.wg.Add(1)
	go s.cleanupRoutine()

	s.logger.Info("Quest Engine Service initialized successfully")
	return nil
}

// HTTPServer returns the HTTP server instance
func (s *Service) HTTPServer() *fasthttp.Server {
	if s.httpServer == nil {
		s.httpServer = &fasthttp.Server{
			Handler:            s.handleHTTPRequest,
			ReadTimeout:        s.config.ReadTimeout,
			WriteTimeout:       s.config.WriteTimeout,
			MaxRequestBodySize: int(s.config.MaxRequestSize),
			Concurrency:        s.config.MaxConnections,
			DisableKeepalive:   false,
			TCPKeepalive:       true,
			TCPKeepalivePeriod: 60 * time.Second,
			MaxConnsPerIP:      50,
			MaxRequestsPerConn: 1000,
			ReduceMemoryUsage:  true, // Performance optimization
			Logger:             zap.NewStdLog(s.logger.Desugar()),
		}
	}
	return s.httpServer
}

// WebSocketServer returns the WebSocket server instance
func (s *Service) WebSocketServer() *fasthttp.Server {
	if s.wsServer == nil {
		s.wsServer = &fasthttp.Server{
			Handler:            s.handleWebSocketRequest,
			ReadTimeout:        s.config.ReadTimeout,
			WriteTimeout:       s.config.WriteTimeout,
			Concurrency:        s.config.MaxConnections,
			DisableKeepalive:   false,
			TCPKeepalive:       true,
			TCPKeepalivePeriod: 60 * time.Second,
			MaxConnsPerIP:      50,
			ReduceMemoryUsage:  true,
			Logger:             zap.NewStdLog(s.logger.Desugar()),
		}
	}
	return s.wsServer
}

// MetricsServer returns the metrics HTTP server
func (s *Service) MetricsServer() *http.Server {
	return &http.Server{
		Addr:    fmt.Sprintf(":%d", s.config.PrometheusPort),
		Handler: s.metrics.Handler(),
	}
}

// Config returns service configuration
func (s *Service) Config() Config {
	return s.config
}

// Shutdown gracefully shuts down the service
func (s *Service) Shutdown(ctx context.Context) error {
	s.logger.Info("Shutting down Quest Engine Service...")

	// Signal shutdown
	close(s.shutdownCh)

	// Close worker pool
	close(s.workerPool)

	// Shutdown HTTP servers
	if s.httpServer != nil {
		if err := s.httpServer.Shutdown(); err != nil {
			s.logger.Errorf("HTTP server shutdown error: %v", err)
		}
	}

	if s.wsServer != nil {
		if err := s.wsServer.Shutdown(); err != nil {
			s.logger.Errorf("WebSocket server shutdown error: %v", err)
		}
	}

	// Close database connections
	if s.db != nil {
		s.db.Close()
	}

	// Close Kafka writer
	if s.kafka != nil {
		if err := s.kafka.Close(); err != nil {
			s.logger.Errorf("Kafka writer close error: %v", err)
		}
	}

	// Wait for all goroutines to finish
	done := make(chan struct{})
	go func() {
		s.wg.Wait()
		close(done)
	}()

	select {
	case <-done:
		s.logger.Info("All goroutines finished")
	case <-ctx.Done():
		s.logger.Warn("Shutdown timeout exceeded")
		return ctx.Err()
	}

	return nil
}

// handleHTTPRequest handles HTTP requests with performance optimizations
func (s *Service) handleHTTPRequest(ctx *fasthttp.RequestCtx) {
	// Rate limiting
	if !s.rateLimiter.Allow() {
		ctx.SetStatusCode(fasthttp.StatusTooManyRequests)
		return
	}

	// Semaphore for connection limiting
	if !s.semaphore.TryAcquire(1) {
		ctx.SetStatusCode(fasthttp.StatusServiceUnavailable)
		return
	}
	defer s.semaphore.Release(1)

	// Get pooled context
	pctx := s.getPooledContext()
	defer s.putPooledContext(pctx)

	// Route to appropriate handler
	path := string(ctx.Path())
	method := string(ctx.Method())

	switch {
	case path == "/health" && method == "GET":
		s.handleHealthCheck(ctx)
	case path == "/metrics" && method == "GET" && s.config.EnableMetrics:
		s.handleMetrics(ctx)
	default:
		// Route to OpenAPI handlers
		s.routeToAPIHandler(ctx)
	}
}

// handleWebSocketRequest handles WebSocket upgrade requests
func (s *Service) handleWebSocketRequest(ctx *fasthttp.RequestCtx) {
	// WebSocket upgrade logic would go here
	// For now, return not implemented
	ctx.SetStatusCode(fasthttp.StatusNotImplemented)
}

// handleHealthCheck handles health check requests
func (s *Service) handleHealthCheck(ctx *fasthttp.RequestCtx) {
	ctx.SetContentType("application/json")
	ctx.SetStatusCode(fasthttp.StatusOK)
	ctx.SetBodyString(`{"status":"healthy","service":"quest-engine"}`)
}

// handleMetrics handles metrics requests
func (s *Service) handleMetrics(ctx *fasthttp.RequestCtx) {
	if s.metrics != nil {
		// Prometheus metrics would be served here
		ctx.SetContentType("text/plain")
		ctx.SetStatusCode(fasthttp.StatusOK)
		ctx.SetBodyString("# Quest Engine Metrics\n")
	} else {
		ctx.SetStatusCode(fasthttp.StatusNotFound)
	}
}

// routeToAPIHandler routes requests to generated OpenAPI handlers
func (s *Service) routeToAPIHandler(ctx *fasthttp.RequestCtx) {
	// TODO: Integrate with generated OpenAPI handlers
	ctx.SetStatusCode(fasthttp.StatusNotImplemented)
	ctx.SetBodyString("API handlers not yet implemented")
}

// worker processes tasks from the worker pool
func (s *Service) worker(id int) {
	defer s.wg.Done()

	s.logger.Infof("Worker %d started", id)

	for {
		select {
		case task, ok := <-s.workerPool:
			if !ok {
				s.logger.Infof("Worker %d stopping", id)
				return
			}
			task()
		case <-s.shutdownCh:
			s.logger.Infof("Worker %d shutting down", id)
			return
		}
	}
}

// cleanupRoutine performs periodic cleanup tasks
func (s *Service) cleanupRoutine() {
	defer s.wg.Done()

	ticker := time.NewTicker(5 * time.Minute)
	defer ticker.Stop()

	for {
		select {
		case <-ticker.C:
			s.performCleanup()
		case <-s.shutdownCh:
			s.logger.Info("Cleanup routine stopping")
			return
		}
	}
}

// performCleanup performs maintenance cleanup tasks
func (s *Service) performCleanup() {
	// Cleanup expired quest instances
	if err := s.questManager.CleanupExpiredQuests(); err != nil {
		s.logger.Errorf("Failed to cleanup expired quests: %v", err)
	}

	// Cleanup old dialogue states
	if err := s.dialogueManager.CleanupOldDialogues(); err != nil {
		s.logger.Errorf("Failed to cleanup old dialogues: %v", err)
	}

	s.logger.Debug("Cleanup completed")
}

// getPooledContext gets a context from the pool
func (s *Service) getPooledContext() *ContextWithCancel {
	return s.contextPool.Get().(*ContextWithCancel)
}

// putPooledContext returns a context to the pool
func (s *Service) putPooledContext(c *ContextWithCancel) {
	c.Cancel() // Cancel the context
	s.contextPool.Put(c)
}

// ContextWithCancel wraps context with cancel function for pooling
type ContextWithCancel struct {
	context.Context
	Cancel context.CancelFunc
}
