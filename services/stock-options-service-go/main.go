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

	"github.com/gc-lover/necpgame-monorepo/services/stock-options-service-go/server"
)

// Config holds all configuration for the stock options service
type Config struct {
	Port         string        `envconfig:"PORT" default:"8151"`
	DatabaseURL  string        `envconfig:"DATABASE_URL" required:"true"`
	JWTSecret    string        `envconfig:"JWT_SECRET" required:"true"`
	LogLevel     string        `envconfig:"LOG_LEVEL" default:"info"`
	ReadTimeout  time.Duration `envconfig:"READ_TIMEOUT" default:"15s"`
	WriteTimeout time.Duration `envconfig:"WRITE_TIMEOUT" default:"15s"`
	IdleTimeout  time.Duration `envconfig:"IDLE_TIMEOUT" default:"60s"`

	// Performance tuning for options pricing
	MaxDBConnections    int           `envconfig:"MAX_DB_CONNECTIONS" default:"150"`
	MinDBConnections    int           `envconfig:"MIN_DB_CONNECTIONS" default:"30"`
	DBConnMaxLifetime   time.Duration `envconfig:"DB_CONN_MAX_LIFETIME" default:"1h"`
	DBConnMaxIdleTime   time.Duration `envconfig:"DB_CONN_MAX_IDLE_TIME" default:"30m"`

	// Options specific configuration
	RedisURL               string        `envconfig:"REDIS_URL" default:""`
	CacheTTL               time.Duration `envconfig:"CACHE_TTL" default:"5m"`
	OptionsBatchSize       int           `envconfig:"OPTIONS_BATCH_SIZE" default:"500"`
	PricingProcessingDelay time.Duration `envconfig:"PRICING_PROCESSING_DELAY" default:"200ms"`
	MaxConcurrentPricing   int           `envconfig:"MAX_CONCURRENT_PRICING" default:"25"`
	VolatilityUpdateFreq   time.Duration `envconfig:"VOLATILITY_UPDATE_FREQ" default:"1h"`
	RiskFreeRate           float64       `envconfig:"RISK_FREE_RATE" default:"0.05"`
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

	// Initialize database connection with performance optimizations for options operations
	db, err := initDatabase(cfg, logger)
	if err != nil {
		logger.Fatal("Failed to initialize database", zap.Error(err))
	}
	defer db.Close()

	// Initialize JWT auth for options trading security
	tokenAuth := jwtauth.New("HS256", []byte(cfg.JWTSecret), nil)

	// Initialize server with optimized handlers for options pricing
	serverCfg := server.Config{
		OptionsBatchSize:       cfg.OptionsBatchSize,
		PricingProcessingDelay: cfg.PricingProcessingDelay,
		MaxConcurrentPricing:   cfg.MaxConcurrentPricing,
		CacheTTL:               cfg.CacheTTL,
		RedisURL:               cfg.RedisURL,
		VolatilityUpdateFreq:   cfg.VolatilityUpdateFreq,
		RiskFreeRate:           cfg.RiskFreeRate,
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
		logger.Info("Starting stock-options-service-go server",
			zap.String("port", cfg.Port),
			zap.Int("max_db_conns", cfg.MaxDBConnections),
			zap.Duration("cache_ttl", cfg.CacheTTL),
			zap.Int("options_batch_size", cfg.OptionsBatchSize),
			zap.Int("max_concurrent_pricing", cfg.MaxConcurrentPricing),
			zap.Duration("volatility_update_freq", cfg.VolatilityUpdateFreq),
			zap.Float64("risk_free_rate", cfg.RiskFreeRate))
		if err := httpSrv.ListenAndServe(); err != nil && err != http.ErrServerClosed {
			logger.Fatal("Server failed to start", zap.Error(err))
		}
	}()

	// Wait for interrupt signal
	quit := make(chan os.Signal, 1)
	signal.Notify(quit, syscall.SIGINT, syscall.SIGTERM)
	<-quit
	logger.Info("Shutting down stock options service...")

	// Graceful shutdown with timeout
	ctx, cancel := context.WithTimeout(context.Background(), 30*time.Second)
	defer cancel()

	if err := httpSrv.Shutdown(ctx); err != nil {
		logger.Fatal("Server forced to shutdown", zap.Error(err))
	}

	logger.Info("Stock options service exited gracefully")
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

	// Enterprise-grade logging configuration for financial options operations
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

	// Performance-optimized connection pool for options operations
	config.MaxConns = int32(cfg.MaxDBConnections)
	config.MinConns = int32(cfg.MinDBConnections)
	config.MaxConnLifetime = cfg.DBConnMaxLifetime
	config.MaxConnIdleTime = cfg.DBConnMaxIdleTime

	// Options-specific optimizations
	config.HealthCheckPeriod = 30 * time.Second // Frequent health checks for options pricing consistency
	config.MaxConnLifetimeJitter = 5 * time.Minute // Jitter to avoid pricing calculation storms

	pool, err := pgxpool.NewWithConfig(context.Background(), config)
	if err != nil {
		return nil, fmt.Errorf("failed to create connection pool: %w", err)
	}

	// Test connection with timeout
	ctx, cancel := context.WithTimeout(context.Background(), 15*time.Second)
	defer cancel()

	if err := pool.Ping(ctx); err != nil {
		return nil, fmt.Errorf("failed to ping database: %w", err)
	}

	logger.Info("Database connection established for stock options service",
		zap.Int32("max_conns", config.MaxConns),
		zap.Int32("min_conns", config.MinConns),
		zap.Duration("max_lifetime", config.MaxConnLifetime),
		zap.Int("options_batch_size", cfg.OptionsBatchSize))

	return pool, nil
}

func setupRouter(ogenHandler http.Handler, logger *zap.Logger) http.Handler {
	r := chi.NewRouter()

	// Enterprise-grade middleware stack optimized for options operations
	r.Use(middleware.RequestID)
	r.Use(middleware.RealIP)
	r.Use(middleware.Recoverer)
	r.Use(middleware.Timeout(30 * time.Second)) // Longer timeout for complex options pricing

	// Structured logging middleware with performance metrics for financial operations
	r.Use(func(next http.Handler) http.Handler {
		return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
			start := time.Now()
			ww := middleware.NewWrapResponseWriter(w, r.ProtoMajor)

			defer func() {
				logger.Info("HTTP Request - Stock Options",
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

	// Security headers for financial options operations
	r.Use(middleware.SetHeader("X-Content-Type-Options", "nosniff"))
	r.Use(middleware.SetHeader("X-Frame-Options", "DENY"))
	r.Use(middleware.SetHeader("X-XSS-Protection", "1; mode=block"))
	r.Use(middleware.SetHeader("Strict-Transport-Security", "max-age=31536000; includeSubDomains"))
	r.Use(middleware.SetHeader("Content-Security-Policy", "default-src 'self'"))

	// CORS for trading platforms and options management systems
	r.Use(middleware.SetHeader("Access-Control-Allow-Origin", "*"))
	r.Use(middleware.SetHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS"))
	r.Use(middleware.SetHeader("Access-Control-Allow-Headers", "Content-Type, Authorization, X-Requested-With"))

	// Rate limiting for expensive options pricing operations (placeholder)
	// In production, implement proper rate limiting middleware

	// pprof endpoints for performance profiling of options pricing calculations
	r.Mount("/debug", middleware.Profiler())

	// Mount ogen handlers
	r.Mount("/", ogenHandler)

	return r
}

// Issue: #141889271
