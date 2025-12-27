// Guild Service - Enterprise-grade social guild management
// Issue: #2247
// Agent: Backend

package main

import (
	"context"
	"fmt"
	"log"
	"net/http"
	"os"
	"os/signal"
	"strings"
	"syscall"
	"time"

	"github.com/go-chi/chi/v5"
	"github.com/go-chi/chi/v5/middleware"
	"github.com/golang-jwt/jwt/v4"
	"github.com/prometheus/client_golang/prometheus/promhttp"
	"go.uber.org/zap"

	"github.com/gc-lover/necpgame-monorepo/services/guild-service-go/internal/config"
	"github.com/gc-lover/necpgame-monorepo/services/guild-service-go/internal/repository"
	"github.com/gc-lover/necpgame-monorepo/services/guild-service-go/pkg/api"
	"github.com/gc-lover/necpgame-monorepo/services/guild-service-go/server"
)

// JWTClaims represents JWT token claims
type JWTClaims struct {
	UserID   string `json:"user_id"`
	Username string `json:"username"`
	Role     string `json:"role"`
	jwt.RegisteredClaims
}

// SecurityHandler implements JWT authentication
type SecurityHandler struct {
	jwtSecret []byte
	logger    *zap.SugaredLogger
}

// NewSecurityHandler creates a new security handler with JWT secret
func NewSecurityHandler(jwtSecret string, logger *zap.SugaredLogger) *SecurityHandler {
	return &SecurityHandler{
		jwtSecret: []byte(jwtSecret),
		logger:    logger,
	}
}

func (s *SecurityHandler) HandleBearerAuth(ctx context.Context, operationName api.OperationName, t api.BearerAuth) (context.Context, error) {
	// Extract token from BearerAuth
	tokenString := string(t)

	// Parse and validate token
	token, err := jwt.ParseWithClaims(tokenString, &JWTClaims{}, func(token *jwt.Token) (interface{}, error) {
		if _, ok := token.Method.(*jwt.SigningMethodHMAC); !ok {
			return nil, fmt.Errorf("unexpected signing method: %v", token.Header["alg"])
		}
		return s.jwtSecret, nil
	})

	if err != nil {
		s.logger.Errorf("JWT validation failed: %v", err)
		return ctx, fmt.Errorf("invalid token: %w", err)
	}

	if !token.Valid {
		s.logger.Warn("Invalid JWT token provided")
		return ctx, fmt.Errorf("token is not valid")
	}

	claims, ok := token.Claims.(*JWTClaims)
	if !ok {
		s.logger.Error("Invalid JWT claims format")
		return ctx, fmt.Errorf("invalid token claims")
	}

	// Check token expiration
	if claims.ExpiresAt != nil && claims.ExpiresAt.Before(time.Now()) {
		s.logger.Warn("Expired JWT token used")
		return ctx, fmt.Errorf("token has expired")
	}

	// Add user information to context
	ctx = context.WithValue(ctx, "user_id", claims.UserID)
	ctx = context.WithValue(ctx, "username", claims.Username)
	ctx = context.WithValue(ctx, "role", claims.Role)

	s.logger.Debugf("JWT validated successfully for user: %s", claims.Username)
	return ctx, nil
}

// middlewareWrapper wraps the OpenAPI server with enterprise-grade middleware
func middlewareWrapper(apiHandler http.Handler, metricsHandler http.Handler) http.Handler {
	r := chi.NewRouter()

	// Core middleware
	r.Use(middleware.RequestID)
	r.Use(middleware.RealIP)
	r.Use(middleware.Logger)
	r.Use(middleware.Recoverer)
	r.Use(middleware.Timeout(30 * time.Second))

	// Security middleware
	r.Use(middleware.SetHeader("X-Content-Type-Options", "nosniff"))
	r.Use(middleware.SetHeader("X-Frame-Options", "DENY"))
	r.Use(middleware.SetHeader("X-XSS-Protection", "1; mode=block"))

	// Health and monitoring endpoints
	r.Get("/health", func(w http.ResponseWriter, r *http.Request) {
		w.Header().Set("Content-Type", "application/json")
		w.WriteHeader(http.StatusOK)
		w.Write([]byte(`{"status":"healthy","service":"guild-service-go"}`))
	})
	r.Get("/ready", func(w http.ResponseWriter, r *http.Request) {
		w.Header().Set("Content-Type", "application/json")
		w.WriteHeader(http.StatusOK)
		w.Write([]byte(`{"status":"ready","service":"guild-service-go"}`))
	})
	r.Handle("/metrics", metricsHandler)
	r.Mount("/debug/pprof", middleware.Profiler())

	// Mount OpenAPI routes
	r.Mount("/", apiHandler)

	return r
}

func main() {
	// Initialize structured logging
	logger, err := zap.NewProduction()
	if err != nil {
		log.Fatalf("Failed to initialize logger: %v", err)
	}
	defer logger.Sync()

	sugar := logger.Sugar()
	sugar.Info("Starting Guild Service v1.0.0")

	// Load configuration
	cfg, err := config.Load()
	if err != nil {
		sugar.Fatalf("Failed to load configuration: %v", err)
	}

	// Initialize database connection with connection pooling
	dbConfig := repository.DatabaseConfig{
		Host:     cfg.Database.Host,
		Port:     cfg.Database.Port,
		User:     cfg.Database.User,
		Password: cfg.Database.Password,
		Database: cfg.Database.Database,
		SSLMode:  cfg.Database.SSLMode,
		MaxConns: cfg.Database.MaxConns,
	}
	db, err := repository.NewDatabaseConnection(dbConfig)
	if err != nil {
		sugar.Fatalf("Failed to connect to database: %v", err)
	}
	defer db.Close()

	// Initialize Redis for caching
	redisConfig := repository.RedisConfig{
		Host:     cfg.Redis.Host,
		Port:     cfg.Redis.Port,
		Password: cfg.Redis.Password,
		DB:       cfg.Redis.DB,
	}
	redisClient, err := repository.NewRedisConnection(redisConfig)
	if err != nil {
		sugar.Fatalf("Failed to connect to Redis: %v", err)
	}
	defer redisClient.Close()

	// Initialize repository layer
	repo := repository.NewRepository(db, redisClient, sugar)

	// Initialize service layer with business logic
	guildSvc := server.NewGuildService(logger)
	guildSvc.UpdateRepository(repo) // Connect to database repository

	// Initialize handlers
	h := server.NewHandler(logger, guildSvc)

	// Initialize security handler with JWT secret
	jwtSecret := os.Getenv("JWT_SECRET")
	if jwtSecret == "" {
		jwtSecret = "default-jwt-secret-change-in-production"
		sugar.Warn("Using default JWT secret - change JWT_SECRET environment variable in production")
	}
	sec := NewSecurityHandler(jwtSecret, sugar)

	// Create HTTP server with OpenAPI-generated routes
	srv, err := api.NewServer(h, sec)
	if err != nil {
		sugar.Fatalf("Failed to create API server: %v", err)
	}

	// Create HTTP server with OpenAPI-generated routes and middleware
	httpSrv := &http.Server{
		Addr:         cfg.Server.GetAddr(),
		Handler:      middlewareWrapper(srv, promhttp.Handler()),
		ReadTimeout:  15 * time.Second,
		WriteTimeout: 15 * time.Second,
		IdleTimeout:  60 * time.Second,
	}

	// Start server in goroutine
	go func() {
		sugar.Infof("Server starting on %s", cfg.Server.GetAddr())
		if err := httpSrv.ListenAndServe(); err != nil && err != http.ErrServerClosed {
			sugar.Fatalf("Server failed to start: %v", err)
		}
	}()

	// Wait for interrupt signal to gracefully shutdown the server
	quit := make(chan os.Signal, 1)
	signal.Notify(quit, syscall.SIGINT, syscall.SIGTERM)
	<-quit
	sugar.Info("Shutting down server...")

	// Give outstanding requests 30 seconds to complete
	ctx, cancel := context.WithTimeout(context.Background(), 30*time.Second)
	defer cancel()

	if err := httpSrv.Shutdown(ctx); err != nil {
		sugar.Errorf("Server forced to shutdown: %v", err)
	}

	sugar.Info("Server exited successfully")
}
