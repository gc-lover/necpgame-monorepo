package main

import (
	"context"
	"net/http"
	"os"
	"os/signal"
	"syscall"
	"time"

	"necpgame/services/auth-service-go/config"
	"necpgame/services/auth-service-go/server"

	"github.com/prometheus/client_golang/prometheus/promhttp"
	"github.com/sirupsen/logrus"
)

func main() {
	logger := logrus.New()
	logger.SetFormatter(&logrus.JSONFormatter{
		TimestampFormat: time.RFC3339,
	})
	logger.SetLevel(logrus.InfoLevel)

	config := &config.AuthServiceConfig{
		HTTPServerConfig: config.HTTPServerConfig{
			HTTPAddr:       getEnv("HTTP_ADDR", ":8086"),
			HealthAddr:     getEnv("HEALTH_ADDR", ":8087"),
			ReadTimeout:    30 * time.Second,
			WriteTimeout:   30 * time.Second,
			MaxHeaderBytes: 1 << 20, // 1MB
		},
		JWTConfig: config.JWTConfig{
			JWTSecret:     getEnv("JWT_SECRET", "your-super-secret-jwt-key-here"),
			JWTExpiry:     15 * time.Minute, // OPTIMIZATION: Short-lived tokens for security
			RefreshExpiry: 24 * time.Hour,
		},
		SecurityConfig: config.SecurityConfig{
			SessionTimeout:   24 * time.Hour,
			MaxLoginAttempts: 5, // OPTIMIZATION: Brute force protection
		},
		DatabaseConfig: config.DatabaseConfig{
			DatabaseURL: getEnv("DATABASE_URL", "postgres://user:pass@localhost/auth?sslmode=disable"),
			RedisURL:    getEnv("REDIS_URL", "redis://localhost:6379"),
		},
		OAuthConfig: config.OAuthConfig{
			GoogleClientID:     getEnv("GOOGLE_CLIENT_ID", ""),
			GoogleClientSecret: getEnv("GOOGLE_CLIENT_SECRET", ""),
			GoogleRedirectURI:  getEnv("GOOGLE_REDIRECT_URI", ""),
		},
		MetricsAddr: getEnv("METRICS_ADDR", ":8088"),
	}

	// TODO: Add pprof server when config supports it
	// OPTIMIZATION: Issue #1998 - Start pprof server for auth performance profiling
	// go func() {
	// 	logger.WithField("addr", ":8089").Info("pprof server starting")
	// 	// Endpoints: /debug/pprof/profile, /debug/pprof/heap, /debug/pprof/goroutine
	// 	if err := http.ListenAndServe(":8089", nil); err != nil {
	// 		logger.WithError(err).Error("pprof server failed")
	// 	}
	// }()

	// Initialize auth service with security optimizations
	srv, err := server.NewAuthServer(config, logger)
	if err != nil {
		logger.WithError(err).Fatal("failed to initialize auth server")
	}

	// OPTIMIZATION: Issue #1998 - Health check endpoint with auth metrics
	healthMux := http.NewServeMux()
	healthMux.HandleFunc("/health", srv.HealthCheck)
	healthMux.Handle("/metrics", promhttp.Handler())

	go func() {
		logger.WithField("addr", config.HealthAddr).Info("health/metrics server starting")
		if err := http.ListenAndServe(config.HealthAddr, healthMux); err != nil {
			logger.WithError(err).Error("health server failed")
		}
	}()

	// Start main HTTP server
	httpSrv := &http.Server{
		Addr:           config.HTTPAddr,
		Handler:        srv.Router(),
		ReadTimeout:    config.ReadTimeout,
		WriteTimeout:   config.WriteTimeout,
		MaxHeaderBytes: config.MaxHeaderBytes,
	}

	// Start main HTTP server
	go func() {
		logger.WithField("addr", config.HTTPAddr).Info("auth service starting")
		if err := httpSrv.ListenAndServe(); err != nil && err != http.ErrServerClosed {
			logger.WithError(err).Fatal("auth server failed")
		}
	}()

	// OPTIMIZATION: Issue #1998 - Graceful shutdown with timeout
	quit := make(chan os.Signal, 1)
	signal.Notify(quit, syscall.SIGINT, syscall.SIGTERM)
	<-quit

	logger.Info("shutting down auth service...")

	ctx, cancel := context.WithTimeout(context.Background(), 30*time.Second)
	defer cancel()

	if err := httpSrv.Shutdown(ctx); err != nil {
		logger.WithError(err).Error("server forced to shutdown")
	}

	logger.Info("auth service stopped")
}

func getEnv(key, defaultValue string) string {
	if value := os.Getenv(key); value != "" {
		return value
	}
	return defaultValue
}
