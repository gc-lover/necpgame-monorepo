package main

import (
	"context"
	"net/http"
	"os"
	"os/signal"
	"syscall"
	"time"

	"github.com/jmoiron/sqlx"
	_ "github.com/lib/pq"
	"github.com/necpgame/world-service-go/server"
	"github.com/prometheus/client_golang/prometheus/promhttp"
	"github.com/redis/go-redis/v9"
)

func main() {
	logger := server.GetLogger()
	logger.Info("World Service (Go) starting...")

	addr := getEnv("ADDR", "0.0.0.0:8090")
	metricsAddr := getEnv("METRICS_ADDR", ":9090")

	dbURL := getEnv("DATABASE_URL", "postgresql://necpgame:necpgame@localhost:5432/necpgame?sslmode=disable")
	redisURL := getEnv("REDIS_URL", "redis://localhost:6379/8")

	db, err := sqlx.Connect("postgres", dbURL)
	if err != nil {
		logger.WithError(err).Fatal("Failed to connect to database")
	}
	defer db.Close()

	opt, err := redis.ParseURL(redisURL)
	if err != nil {
		logger.WithError(err).Fatal("Failed to parse Redis URL")
	}
	redisClient := redis.NewClient(opt)
	defer redisClient.Close()

	worldRepo := server.NewWorldRepository(db)
	eventBus := server.NewRedisEventBus(redisClient)
	worldService := server.NewWorldService(worldRepo, logger, eventBus)

	worldEventsRepo := server.NewWorldEventsRepository(db)
	worldEventsService := server.NewWorldEventsService(worldEventsRepo)

	worldStateRepo := server.NewWorldStateRepository(db)
	worldStateService := server.NewWorldStateService(worldStateRepo)

	jwtIssuer := getEnv("JWT_ISSUER", "")
	jwksURL := getEnv("JWKS_URL", "")
	authEnabled := jwtIssuer != "" && jwksURL != ""

	var jwtValidator *server.JwtValidator
	if authEnabled {
		jwtValidator = server.NewJwtValidator(jwtIssuer, jwksURL, logger)
	}

	httpServer := server.NewHTTPServer(addr, worldService, worldEventsService, worldStateService, jwtValidator, authEnabled)

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
		logger.WithField("addr", addr).Info("HTTP server starting")
		if err := httpServer.Start(ctx); err != nil && err != http.ErrServerClosed {
			logger.WithError(err).Fatal("HTTP server failed")
		}
	}()

	<-sigChan
	logger.Info("Shutting down...")

	shutdownCtx, shutdownCancel := context.WithTimeout(context.Background(), 30*time.Second)
	defer shutdownCancel()

	if err := httpServer.Shutdown(shutdownCtx); err != nil {
		logger.WithError(err).Error("HTTP server shutdown error")
	}

	if err := metricsServer.Shutdown(shutdownCtx); err != nil {
		logger.WithError(err).Error("Metrics server shutdown error")
	}

	logger.Info("World Service stopped")
}

func getEnv(key, defaultValue string) string {
	if value := os.Getenv(key); value != "" {
		return value
	}
	return defaultValue
}

