package main

import (
	"context"
	"fmt"
	"net/http"
	_ "net/http/pprof" // OPTIMIZATION: Issue #1584
	"os"
	"os/signal"
	"syscall"
	"time"

	"github.com/jackc/pgx/v5/pgxpool"
	"github.com/necpgame/clan-war-service-go/server"
	"github.com/prometheus/client_golang/prometheus/promhttp"
	"github.com/redis/go-redis/v9"
)

func main() {
	logger := server.GetLogger()

	dbURL := os.Getenv("DATABASE_URL")
	if dbURL == "" {
		logger.Fatal("DATABASE_URL environment variable is required")
	}

	// Issue: #1605 - DB Connection Pool configuration
	config, err := pgxpool.ParseConfig(dbURL)
	if err != nil {
		logger.WithError(err).Fatal("Failed to parse database URL")
	}
	config.MaxConns = 50
	config.MinConns = 10
	config.MaxConnLifetime = 5 * time.Minute
	config.MaxConnIdleTime = 1 * time.Minute
	
	dbPool, err := pgxpool.NewWithConfig(context.Background(), config)
	if err != nil {
		logger.WithError(err).Fatal("Failed to connect to database")
	}
	defer dbPool.Close()

	redisURL := os.Getenv("REDIS_URL")
	if redisURL == "" {
		redisURL = "redis://localhost:6379"
	}

	redisOpts, err := redis.ParseURL(redisURL)
	if err != nil {
		logger.WithError(err).Fatal("Failed to parse Redis URL")
	}

	redisClient := redis.NewClient(redisOpts)
	defer redisClient.Close()

	keycloakIssuer := os.Getenv("KEYCLOAK_ISSUER")
	if keycloakIssuer == "" {
		keycloakIssuer = "http://localhost:8080/realms/necpgame"
	}

	keycloakJWKSURL := os.Getenv("KEYCLOAK_JWKS_URL")
	if keycloakJWKSURL == "" {
		keycloakJWKSURL = fmt.Sprintf("%s/protocol/openid-connect/certs", keycloakIssuer)
	}

	authEnabled := os.Getenv("AUTH_ENABLED") != "false"
	jwtValidator := server.NewJwtValidator(keycloakIssuer, keycloakJWKSURL, logger)

	clanWarRepo := server.NewClanWarRepository(dbPool, logger)
	clanWarService := server.NewClanWarService(clanWarRepo, redisClient, logger)

	httpAddr := os.Getenv("HTTP_ADDR")
	if httpAddr == "" {
		httpAddr = ":8092"
	}

	httpServer := server.NewHTTPServer(httpAddr, clanWarService, jwtValidator, authEnabled)

	metricsAddr := os.Getenv("METRICS_ADDR")
	if metricsAddr == "" {
		metricsAddr = ":9092"
	}

	metricsMux := http.NewServeMux()
	metricsMux.Handle("/metrics", promhttp.Handler())

	metricsServer := &http.Server{
		Addr: metricsAddr,
		Handler: metricsMux,
	}

	ctx, cancel := context.WithCancel(context.Background())
	defer cancel()

	go func() {
		logger.WithField("addr", httpAddr).Info("Starting HTTP server")
		if err := httpServer.Start(ctx); err != nil {
			logger.WithError(err).Fatal("HTTP server failed")
		}
	}()

	go func() {
		logger.WithField("addr", metricsAddr).Info("Starting metrics server")
		if err := metricsServer.ListenAndServe(); err != nil && err != http.ErrServerClosed {
			logger.WithError(err).Fatal("Metrics server failed")
		}
	}()

	// OPTIMIZATION: Issue #1584 - Start pprof server for profiling
	go func() {
		pprofAddr := getEnv("PPROF_ADDR", "localhost:6469")
		logger.WithField("addr", pprofAddr).Info("pprof server starting")
		// Endpoints: /debug/pprof/profile, /debug/pprof/heap, /debug/pprof/goroutine
		if err := http.ListenAndServe(pprofAddr, nil); err != nil {
			logger.WithError(err).Error("pprof server failed")
		}
	}()

	sigChan := make(chan os.Signal, 1)
	signal.Notify(sigChan, syscall.SIGINT, syscall.SIGTERM)

	<-sigChan
	logger.Info("Shutting down...")

	cancel()
	httpServer.Shutdown(context.Background())
	metricsServer.Shutdown(context.Background())
}

func getEnv(key, defaultValue string) string {
	if value := os.Getenv(key); value != "" {
		return value
	}
	return defaultValue
}

