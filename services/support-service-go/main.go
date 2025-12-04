// Issue: #1584
package main

import (
	"context"
	"net/http"
	_ "net/http/pprof" // OPTIMIZATION: Issue #1584 - profiling endpoints
	"os"
	"os/signal"
	"syscall"
	"time"

	"github.com/jackc/pgx/v5/pgxpool"
	"github.com/gc-lover/necpgame-monorepo/services/support-service-go/server"
	"github.com/prometheus/client_golang/prometheus/promhttp"
)

func main() {
	logger := server.GetLogger()
	logger.Info("Support Service (Go) starting...")

	addr := getEnv("ADDR", "0.0.0.0:8087")
	metricsAddr := getEnv("METRICS_ADDR", ":9097")
	
	dbURL := getEnv("DATABASE_URL", "postgresql://necpgame:necpgame@localhost:5432/necpgame?sslmode=disable")
	redisURL := getEnv("REDIS_URL", "redis://localhost:6379/6")
	keycloakURL := getEnv("KEYCLOAK_URL", "http://localhost:8080")
	keycloakRealm := getEnv("KEYCLOAK_REALM", "necpgame")
	authEnabled := getEnv("AUTH_ENABLED", "true") == "true"

	ticketService, err := server.NewTicketService(dbURL, redisURL)
	if err != nil {
		logger.WithError(err).Fatal("Failed to initialize ticket service")
	}

	// Connection pool settings for performance (Issue #1605)
	config, err := pgxpool.ParseConfig(dbURL)
	if err != nil {
		logger.WithError(err).Fatal("Failed to parse database URL")
	}
	config.MaxConns = 25
	config.MinConns = 5
	config.MaxConnLifetime = 5 * time.Minute
	config.MaxConnIdleTime = 10 * time.Minute

	dbPool, err := pgxpool.NewWithConfig(context.Background(), config)
	if err != nil {
		logger.WithError(err).Fatal("Failed to initialize database pool for SLA service")
	}

	slaRepo := server.NewSLARepository(dbPool)
	slaService := server.NewSLAService(slaRepo)

	var jwtValidator *server.JwtValidator
	if authEnabled && keycloakURL != "" {
		issuer := keycloakURL + "/realms/" + keycloakRealm
		jwksURL := keycloakURL + "/realms/" + keycloakRealm + "/protocol/openid-connect/certs"
		jwtValidator = server.NewJwtValidator(issuer, jwksURL, logger)
		logger.WithFields(map[string]interface{}{
			"issuer":  issuer,
			"jwksURL": jwksURL,
		}).Info("JWT authentication enabled")
	} else {
		logger.Info("JWT authentication disabled")
	}

	httpServer := server.NewHTTPServer(addr, ticketService, slaService, jwtValidator, authEnabled)

	// OPTIMIZATION: Issue #1584 - Start pprof server for profiling
	go func() {
		pprofAddr := getEnv("PPROF_ADDR", "localhost:6204")
		logger.WithField("addr", pprofAddr).Info("pprof server starting")
		// Endpoints: /debug/pprof/profile, /debug/pprof/heap, /debug/pprof/goroutine
		if err := http.ListenAndServe(pprofAddr, nil); err != nil {
			logger.WithError(err).Error("pprof server failed")
		}
	}()

	metricsMux := http.NewServeMux()
	metricsMux.Handle("/metrics", promhttp.Handler())

	metricsServer := &http.Server{
		Addr:         metricsAddr,
		Handler:      metricsMux,
		ReadTimeout:  10 * time.Second,
		WriteTimeout: 10 * time.Second,
	}

	go func() {
		logger.WithField("addr", metricsAddr).Info("Metrics server starting")
		if err := metricsServer.ListenAndServe(); err != nil && err != http.ErrServerClosed {
			logger.WithError(err).Fatal("Metrics server failed")
		}
	}()

	ctx, cancel := context.WithCancel(context.Background())
	defer cancel()

	sigChan := make(chan os.Signal, 1)
	signal.Notify(sigChan, os.Interrupt, syscall.SIGTERM)

	go func() {
		<-sigChan
		logger.Info("Shutting down server...")
		cancel()

		shutdownCtx, shutdownCancel := context.WithTimeout(context.Background(), 5*time.Second)
		defer shutdownCancel()
		metricsServer.Shutdown(shutdownCtx)
		httpServer.Shutdown(shutdownCtx)
	}()

	logger.WithField("addr", addr).Info("HTTP server starting")
	if err := httpServer.Start(ctx); err != nil && err != http.ErrServerClosed {
		logger.WithError(err).Fatal("Server error")
	}

	logger.Info("Server stopped")
}

func getEnv(key, defaultValue string) string {
	if value := os.Getenv(key); value != "" {
		return value
	}
	return defaultValue
}

