package main

import (
	"context"
	"net/http"
	"os"
	"os/signal"
	"syscall"
	"time"

	"github.com/necpgame/character-service-go/server"
	"github.com/prometheus/client_golang/prometheus/promhttp"
)

func main() {
	logger := server.GetLogger()
	logger.Info("Character Service (Go) starting...")

	addr := getEnv("ADDR", "0.0.0.0:8087")
	metricsAddr := getEnv("METRICS_ADDR", ":9092")
	
	dbURL := getEnv("DATABASE_URL", "postgresql://necpgame:necpgame@localhost:5432/necpgame?sslmode=disable")
	redisURL := getEnv("REDIS_URL", "redis://localhost:6379/2")
	keycloakURL := getEnv("KEYCLOAK_URL", "http://localhost:8080")
	keycloakRealm := getEnv("KEYCLOAK_REALM", "necpgame")
	authEnabled := getEnv("AUTH_ENABLED", "true") == "true"

	characterService, err := server.NewCharacterService(dbURL, redisURL, keycloakURL)
	if err != nil {
		logger.WithError(err).Fatal("Failed to initialize character service")
	}

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

	httpServer := server.NewHTTPServer(addr, characterService, jwtValidator, authEnabled)

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
