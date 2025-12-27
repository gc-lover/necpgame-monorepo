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
	"github.com/go-chi/cors"
	"github.com/go-chi/jwtauth/v5"
	"github.com/jackc/pgx/v5/pgxpool"
	"github.com/kelseyhightower/envconfig"
	"go.uber.org/zap"

	"github.com/gc-lover/necpgame-monorepo/services/combat-damage-service-go/pkg/api"
	"github.com/gc-lover/necpgame-monorepo/services/combat-damage-service-go/server"
)

// Config holds all configuration for the service
type Config struct {
	Port         string `envconfig:"PORT" default:"8083"`
	DatabaseURL  string `envconfig:"DATABASE_URL" required:"true"`
	JWTSecret    string `envconfig:"JWT_SECRET" required:"true"`
	LogLevel     string `envconfig:"LOG_LEVEL" default:"info"`
	ReadTimeout  time.Duration `envconfig:"READ_TIMEOUT" default:"10s"`
	WriteTimeout time.Duration `envconfig:"WRITE_TIMEOUT" default:"10s"`
}

func main() {
	// Load configuration
	var cfg Config
	if err := envconfig.Process("", &cfg); err != nil {
		log.Fatal("Failed to load config:", err)
	}

	// Initialize logger
	logger, err := initLogger(cfg.LogLevel)
	if err != nil {
		log.Fatal("Failed to initialize logger:", err)
	}
	defer logger.Sync()

	// Initialize database connection
	db, err := initDatabase(cfg.DatabaseURL, logger)
	if err != nil {
		logger.Fatal("Failed to initialize database", zap.Error(err))
	}
	defer db.Close()

	// Initialize JWT auth
	tokenAuth := jwtauth.New("HS256", []byte(cfg.JWTSecret), nil)

	// Initialize server with ogen handlers
	srv := server.NewServer(db, logger, tokenAuth)

	// Create router with ogen
	r := srv.CreateRouter()

	// Start server
	srv := &http.Server{
		Addr:         ":" + cfg.Port,
		Handler:      r,
		ReadTimeout:  cfg.ReadTimeout,
		WriteTimeout: cfg.WriteTimeout,
	}

	// Graceful shutdown
	go func() {
		logger.Info("Starting combat-damage-service-go server", zap.String("port", cfg.Port))
		if err := srv.ListenAndServe(); err != nil && err != http.ErrServerClosed {
			logger.Fatal("Server failed to start", zap.Error(err))
		}
	}()

	// Wait for interrupt signal
	quit := make(chan os.Signal, 1)
	signal.Notify(quit, syscall.SIGINT, syscall.SIGTERM)
	<-quit
	logger.Info("Shutting down server...")

	// Graceful shutdown with timeout
	ctx, cancel := context.WithTimeout(context.Background(), 30*time.Second)
	defer cancel()

	if err := srv.Shutdown(ctx); err != nil {
		logger.Fatal("Server forced to shutdown", zap.Error(err))
	}

	logger.Info("Server exited")
}

func initLogger(level string) (*zap.Logger, error) {
	var zapLevel zap.AtomicLevel
	switch level {
	case "debug":
		zapLevel = zap.NewAtomicLevelAt(zap.DebugLevel)
	case "info":
		zapLevel = zap.NewAtomicLevelAt(zap.InfoLevel)
	case "warn":
		zapLevel = zap.NewAtomicLevelAt(zap.WarnLevel)
	case "error":
		zapLevel = zap.NewAtomicLevelAt(zap.ErrorLevel)
	default:
		zapLevel = zap.NewAtomicLevelAt(zap.InfoLevel)
	}

	config := zap.Config{
		Level:            zapLevel,
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

func initDatabase(databaseURL string, logger *zap.Logger) (*pgxpool.Pool, error) {
	config, err := pgxpool.ParseConfig(databaseURL)
	if err != nil {
		return nil, fmt.Errorf("failed to parse database URL: %w", err)
	}

	// Configure connection pool for high-performance damage calculations
	config.MaxConns = 50
	config.MinConns = 10
	config.MaxConnLifetime = time.Hour
	config.MaxConnIdleTime = 30 * time.Minute

	pool, err := pgxpool.NewWithConfig(context.Background(), config)
	if err != nil {
		return nil, fmt.Errorf("failed to create connection pool: %w", err)
	}

	// Test connection
	if err := pool.Ping(context.Background()); err != nil {
		return nil, fmt.Errorf("failed to ping database: %w", err)
	}

	logger.Info("Database connection established",
		zap.Int32("max_conns", config.MaxConns),
		zap.Int32("min_conns", config.MinConns))

	return pool, nil
}

func setupRouter(handlers *server.Handlers, tokenAuth *jwtauth.JWTAuth, logger *zap.Logger) *chi.Mux {
	r := chi.NewRouter()

	// Middleware
	r.Use(middleware.RequestID)
	r.Use(middleware.RealIP)
	r.Use(middleware.Logger)
	r.Use(middleware.Recoverer)
	r.Use(middleware.Timeout(30 * time.Second))

	// CORS
	r.Use(cors.Handler(cors.Options{
		AllowedOrigins:   []string{"*"}, // Configure for production
		AllowedMethods:   []string{"GET", "POST", "PUT", "DELETE", "OPTIONS"},
		AllowedHeaders:   []string{"Accept", "Authorization", "Content-Type", "X-CSRF-Token"},
		ExposedHeaders:   []string{"Link"},
		AllowCredentials: false,
		MaxAge:           300,
	}))

	// Health check (no auth required)
	r.Get("/health", handlers.HealthCheck)
	r.Get("/ready", handlers.ReadinessCheck)

	// Metrics endpoint (no auth required)
	r.Get("/metrics", handlers.Metrics)

	// pprof endpoints for profiling
	r.Mount("/debug", middleware.Profiler())

	// API routes with authentication
	r.Route("/api/v1", func(r chi.Router) {
		r.Use(jwtauth.Verifier(tokenAuth))
		r.Use(jwtauth.Authenticator)

		// Combat damage routes
		r.Post("/combat/damage/calculate", handlers.CalculateDamage)
		r.Post("/combat/damage/validate", handlers.ValidateDamage)

		// Combat effects routes
		r.Post("/combat/effects/apply", handlers.ApplyEffects)
		r.Get("/combat/effects/{participant_id}/active", handlers.GetActiveEffects)
		r.Delete("/combat/effects/{effect_id}", handlers.RemoveEffect)
	})

	return r
}

// Issue: #2251
