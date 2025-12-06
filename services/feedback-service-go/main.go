package main

import (
	"context"
	"net/http"
	_ "net/http/pprof" // OPTIMIZATION: Issue #1584 - profiling
	"os"
	"os/signal"
	"syscall"
	"time"

	"github.com/jackc/pgx/v5/pgxpool"
	"github.com/gc-lover/necpgame-monorepo/services/feedback-service-go/server"
	"github.com/prometheus/client_golang/prometheus/promhttp"
)

func main() {
	logger := server.GetLogger()
	logger.Info("Feedback Service (Go) starting...")

	addr := getEnv("ADDR", "0.0.0.0:8090")
	metricsAddr := getEnv("METRICS_ADDR", ":9090")
	
	dbURL := getEnv("DATABASE_URL", "postgresql://necpgame:necpgame@localhost:5432/necpgame?sslmode=disable")
	githubToken := getEnv("GITHUB_TOKEN", "")
	keycloakURL := getEnv("KEYCLOAK_URL", "http://localhost:8080")
	keycloakRealm := getEnv("KEYCLOAK_REALM", "necpgame")
	authEnabled := getEnv("AUTH_ENABLED", "true") == "true"

	// Connection pool settings for performance (Issue #1605)
	config, err := pgxpool.ParseConfig(dbURL)
	if err != nil {
		logger.WithError(err).Fatal("Failed to parse database URL")
	}
	config.MaxConns = 25
	config.MinConns = 5
	config.MaxConnLifetime = 5 * time.Minute
	config.MaxConnIdleTime = 10 * time.Minute

	db, err := pgxpool.NewWithConfig(context.Background(), config)
	if err != nil {
		logger.WithError(err).Fatal("Failed to connect to database")
	}
	defer db.Close()

	if err := db.Ping(context.Background()); err != nil {
		logger.WithError(err).Fatal("Failed to ping database")
	}
	logger.Info("Database connection established")

	feedbackService, err := server.NewFeedbackService(db, githubToken)
	if err != nil {
		logger.WithError(err).Fatal("Failed to initialize feedback service")
	}

	var jwtValidator *server.JwtValidator
	if authEnabled && keycloakURL != "" {
		issuer := keycloakURL + "/realms/" + keycloakRealm
		jwksURL := keycloakURL + "/realms/" + keycloakRealm + "/protocol/openid-connect/certs"
		jwtValidator = server.NewJwtValidator(issuer, jwksURL, logger)
		logger.WithFields(map[string]interface{}{
			"issuer":  issuer,
			"jwksURL": jwksURL,
		}).Info("JWT validation enabled")
	}

	httpServer := server.NewHTTPServer(addr, feedbackService, jwtValidator, authEnabled)

	// OPTIMIZATION: Issue #1584 - Start pprof server for profiling
	go func() {
		pprofAddr := getEnv("PPROF_ADDR", "localhost:6445")
		logger.WithField("addr", pprofAddr).Info("pprof server starting")
		// Endpoints: /debug/pprof/profile, /debug/pprof/heap, /debug/pprof/goroutine
		if err := http.ListenAndServe(pprofAddr, nil); err != nil {
			logger.WithError(err).Error("pprof server failed")
		}
	}()

	// Issue: #1585 - Runtime Goroutine Monitoring
	monitor := server.NewGoroutineMonitor(200, logger) // Max 200 goroutines for feedback service
	go monitor.Start()
	defer monitor.Stop()
	logger.Info("OK Goroutine monitor started")

	metricsRouter := http.NewServeMux()
	metricsRouter.Handle("/metrics", promhttp.Handler())
	metricsServer := &http.Server{
		Addr:    metricsAddr,
		Handler: metricsRouter,
	}

	go func() {
		logger.WithField("addr", metricsAddr).Info("Starting metrics server")
		if err := metricsServer.ListenAndServe(); err != nil && err != http.ErrServerClosed {
			logger.WithError(err).Fatal("Metrics server failed")
		}
	}()

	go func() {
		if err := httpServer.Start(); err != nil && err != http.ErrServerClosed {
			logger.WithError(err).Fatal("HTTP server failed")
		}
	}()

	sigChan := make(chan os.Signal, 1)
	signal.Notify(sigChan, os.Interrupt, syscall.SIGTERM)
	<-sigChan

	logger.Info("Shutting down...")

	ctx, cancel := context.WithTimeout(context.Background(), 30*time.Second)
	defer cancel()

	if err := httpServer.Stop(ctx); err != nil {
		logger.WithError(err).Error("Error stopping HTTP server")
	}

	if err := metricsServer.Shutdown(ctx); err != nil {
		logger.WithError(err).Error("Error stopping metrics server")
	}

	logger.Info("Shutdown complete")
}

func getEnv(key, defaultValue string) string {
	if value := os.Getenv(key); value != "" {
		return value
	}
	return defaultValue
}




















