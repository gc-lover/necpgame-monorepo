package main

import (
	"context"
	"fmt"
	"log"
	"net/http"
	"os"
	"os/signal"
	"syscall"
	"time"

	"github.com/go-chi/chi/v5"
	"github.com/go-chi/chi/v5/middleware"
	"github.com/go-chi/jwtauth/v5"
	"github.com/jackc/pgx/v5/pgxpool"
	"github.com/kelseyhightower/envconfig"
	"go.uber.org/zap"
	"go.uber.org/zap/zapcore"

	"github.com/gc-lover/necpgame-monorepo/services/stock-events-service-go/server"
)

// Config holds all configuration for the stock events service
type Config struct {
	Port         string        `envconfig:"PORT" default:"8147"`
	DatabaseURL  string        `envconfig:"DATABASE_URL" required:"true"`
	JWTSecret    string        `envconfig:"JWT_SECRET" required:"true"`
	LogLevel     string        `envconfig:"LOG_LEVEL" default:"info"`
	ReadTimeout  time.Duration `envconfig:"READ_TIMEOUT" default:"10s"`
	WriteTimeout time.Duration `envconfig:"WRITE_TIMEOUT" default:"10s"`
	IdleTimeout  time.Duration `envconfig:"IDLE_TIMEOUT" default:"60s"`

	// Performance tuning for high-frequency event processing
	MaxDBConnections    int           `envconfig:"MAX_DB_CONNECTIONS" default:"200"`
	MinDBConnections    int           `envconfig:"MIN_DB_CONNECTIONS" default:"50"`
	DBConnMaxLifetime   time.Duration `envconfig:"DB_CONN_MAX_LIFETIME" default:"1h"`
	DBConnMaxIdleTime   time.Duration `envconfig:"DB_CONN_MAX_IDLE_TIME" default:"30m"`

	// Events specific configuration
	RedisURL               string        `envconfig:"REDIS_URL" default:""`
	CacheTTL               time.Duration `envconfig:"CACHE_TTL" default:"30s"`
	EventBatchSize         int           `envconfig:"EVENT_BATCH_SIZE" default:"1000"`
	EventProcessingDelay   time.Duration `envconfig:"EVENT_PROCESSING_DELAY" default:"50ms"`
	MaxConcurrentEvents    int           `envconfig:"MAX_CONCURRENT_EVENTS" default:"100"`
	EventRetentionDays     int           `envconfig:"EVENT_RETENTION_DAYS" default:"365"`
}

func main() {
	// Load configuration
	var cfg Config
	if err := envconfig.Process("", &cfg); err != nil {
		log.Fatal("Failed to load config:", err)
	}

	// Initialize logger with structured JSON output
	logger, err := initLogger(cfg.LogLevel)
	if err != nil {
		log.Fatal("Failed to initialize logger:", err)
	}
	defer logger.Sync()

	// Initialize database connection with performance optimizations for event processing
	db, err := initDatabase(cfg, logger)
	if err != nil {
		logger.Fatal("Failed to initialize database", zap.Error(err))
	}
	defer db.Close()

	// Initialize JWT auth for event security
	tokenAuth := jwtauth.New("HS256", []byte(cfg.JWTSecret), nil)

	// Initialize server with optimized handlers for event processing
	serverCfg := server.Config{
		EventBatchSize:         cfg.EventBatchSize,
		EventProcessingDelay:   cfg.EventProcessingDelay,
		MaxConcurrentEvents:    cfg.MaxConcurrentEvents,
		CacheTTL:               cfg.CacheTTL,
		RedisURL:               cfg.RedisURL,
		EventRetentionDays:     cfg.EventRetentionDays,
	}
	srv := server.NewServer(db, logger, tokenAuth, serverCfg)

	// Create router with ogen handlers wrapped in middleware
	ogenHandler := srv.CreateRouter()
	r := setupRouter(ogenHandler, logger)

	// Start HTTP server with graceful shutdown
	httpSrv := &http.Server{
		Addr:         ":" + cfg.Port,
		Handler:      r,
		ReadTimeout:  cfg.ReadTimeout,
		WriteTimeout: cfg.WriteTimeout,
		IdleTimeout:  cfg.IdleTimeout,
	}

	// Graceful shutdown handling
	go func() {
		logger.Info("Starting stock-events-service-go server",
			zap.String("port", cfg.Port),
			zap.Int("max_db_conns", cfg.MaxDBConnections),
			zap.Duration("cache_ttl", cfg.CacheTTL),
			zap.Int("event_batch_size", cfg.EventBatchSize),
			zap.Int("max_concurrent_events", cfg.MaxConcurrentEvents),
			zap.Int("event_retention_days", cfg.EventRetentionDays))
		if err := httpSrv.ListenAndServe(); err != nil && err != http.ErrServerClosed {
			logger.Fatal("Server failed to start", zap.Error(err))
		}
	}()

	// Wait for interrupt signal
	quit := make(chan os.Signal, 1)
	signal.Notify(quit, syscall.SIGINT, syscall.SIGTERM)
	<-quit
	logger.Info("Shutting down stock events service...")

	// Graceful shutdown with timeout
	ctx, cancel := context.WithTimeout(context.Background(), 30*time.Second)
	defer cancel()

	if err := httpSrv.Shutdown(ctx); err != nil {
		logger.Fatal("Server forced to shutdown", zap.Error(err))
	}

	logger.Info("Stock events service exited gracefully")
}

func initLogger(level string) (*zap.Logger, error) {
	var zapLevel zapcore.Level
	switch level {
	case "debug":
		zapLevel = zapcore.DebugLevel
	case "info":
		zapLevel = zapcore.InfoLevel
	case "warn":
		zapLevel = zapcore.WarnLevel
	case "error":
		zapLevel = zapcore.ErrorLevel
	default:
		zapLevel = zapcore.InfoLevel
	}

	// Enterprise-grade logging configuration for real-time market events
	config := zap.Config{
		Level:            zap.NewAtomicLevelAt(zapLevel),
		Development:      false,
		DisableCaller:    false,
		DisableStacktrace: false,
		Sampling:         nil,
		Encoding:         "json",
		EncoderConfig: zapcore.EncoderConfig{
			TimeKey:        "timestamp",
			LevelKey:       "level",
			NameKey:        "logger",
			CallerKey:      "caller",
			FunctionKey:    "function",
			MessageKey:     "message",
			StacktraceKey:  "stacktrace",
			EncodeLevel:    zapcore.LowercaseLevelEncoder,
			EncodeTime:     zapcore.ISO8601TimeEncoder,
			EncodeDuration: zapcore.MillisDurationEncoder,
			EncodeCaller:   zapcore.ShortCallerEncoder,
		},
		OutputPaths:      []string{"stdout"},
		ErrorOutputPaths: []string{"stderr"},
	}

	return config.Build()
}

func initDatabase(cfg Config, logger *zap.Logger) (*pgxpool.Pool, error) {
	config, err := pgxpool.ParseConfig(cfg.DatabaseURL)
	if err != nil {
		return nil, fmt.Errorf("failed to parse database URL: %w", err)
	}

	// Performance-optimized connection pool for high-frequency event processing
	config.MaxConns = int32(cfg.MaxDBConnections)
	config.MinConns = int32(cfg.MinDBConnections)
	config.MaxConnLifetime = cfg.DBConnMaxLifetime
	config.MaxConnIdleTime = cfg.DBConnMaxIdleTime

	// Events-specific optimizations
	config.HealthCheckPeriod = 15 * time.Second // Very frequent health checks for real-time events
	config.MaxConnLifetimeJitter = 2 * time.Minute // Jitter to avoid event processing storms

	pool, err := pgxpool.NewWithConfig(context.Background(), config)
	if err != nil {
		return nil, fmt.Errorf("failed to create connection pool: %w", err)
	}

	// Test connection with timeout
	ctx, cancel := context.WithTimeout(context.Background(), 10*time.Second)
	defer cancel()

	if err := pool.Ping(ctx); err != nil {
		return nil, fmt.Errorf("failed to ping database: %w", err)
	}

	logger.Info("Database connection established for stock events service",
		zap.Int32("max_conns", config.MaxConns),
		zap.Int32("min_conns", config.MinConns),
		zap.Duration("max_lifetime", config.MaxConnLifetime),
		zap.Int("event_batch_size", cfg.EventBatchSize),
		zap.Int("event_retention_days", cfg.EventRetentionDays))

	return pool, nil
}

func setupRouter(ogenHandler http.Handler, logger *zap.Logger) http.Handler {
	r := chi.NewRouter()

	// Enterprise-grade middleware stack optimized for event processing
	r.Use(middleware.RequestID)
	r.Use(middleware.RealIP)
	r.Use(middleware.Recoverer)
	r.Use(middleware.Timeout(15 * time.Second)) // Shorter timeout for real-time events

	// Structured logging middleware with performance metrics for event processing
	r.Use(func(next http.Handler) http.Handler {
		return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
			start := time.Now()
			ww := middleware.NewWrapResponseWriter(w, r.ProtoMajor)

			defer func() {
				logger.Info("HTTP Request - Stock Events",
					zap.String("method", r.Method),
					zap.String("path", r.URL.Path),
					zap.String("remote_ip", r.RemoteAddr),
					zap.Int("status", ww.Status()),
					zap.Duration("duration", time.Since(start)),
					zap.Int("response_size", ww.BytesWritten()))
			}()

			next.ServeHTTP(ww, r)
		})
	})

	// Security headers for real-time market event data
	r.Use(middleware.SetHeader("X-Content-Type-Options", "nosniff"))
	r.Use(middleware.SetHeader("X-Frame-Options", "DENY"))
	r.Use(middleware.SetHeader("X-XSS-Protection", "1; mode=block"))
	r.Use(middleware.SetHeader("Strict-Transport-Security", "max-age=31536000; includeSubDomains"))

	// CORS for trading platforms and real-time dashboards
	r.Use(middleware.SetHeader("Access-Control-Allow-Origin", "*"))
	r.Use(middleware.SetHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS"))
	r.Use(middleware.SetHeader("Access-Control-Allow-Headers", "Content-Type, Authorization, X-Requested-With"))

	// Rate limiting for event processing (placeholder)
	// In production, implement proper rate limiting middleware

	// pprof endpoints for performance profiling of event processing
	r.Mount("/debug", middleware.Profiler())

	// Mount ogen handlers
	r.Mount("/", ogenHandler)

	return r
}

// Issue: #141889242

