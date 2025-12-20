package main

import (
	"context"
	"net/http"
	_ "net/http/pprof" // OPTIMIZATION: Issue #1584 - profiling endpoints
	"os"
	"os/signal"
	"syscall"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/crafting-service-go/server"
	"github.com/jackc/pgx/v5/pgxpool"
	"github.com/prometheus/client_golang/prometheus/promhttp"
	"github.com/redis/go-redis/v9"
)

func main() {
	logger := server.GetLogger()
	logger.Info("Crafting Service (Go) starting...")

	addr := getEnv("ADDR", "0.0.0.0:8210")
	metricsAddr := getEnv("METRICS_ADDR", ":9090")
	dbURL := getEnv("DATABASE_URL", "postgresql://necpgame:necpgame@localhost:5432/necpgame?sslmode=disable")
	redisURL := getEnv("REDIS_URL", "redis://localhost:6379/6")
	keycloakURL := getEnv("KEYCLOAK_URL", "http://localhost:8080")
	keycloakRealm := getEnv("KEYCLOAK_REALM", "necpgame")
	authEnabled := getEnv("AUTH_ENABLED", "true") == "true"

	// OPTIMIZATION: Issue #1584 - Start pprof server for profiling
	go func() {
		pprofAddr := getEnv("PPROF_ADDR", "localhost:6822")
		logger.WithField("addr", pprofAddr).Info("pprof server starting")
		// Endpoints: /debug/pprof/profile, /debug/pprof/heap, /debug/pprof/goroutine, /debug/pprof/allocs
		if err := http.ListenAndServe(pprofAddr, nil); err != nil {
			logger.WithError(err).Error("pprof server failed")
		}
	}()

	// PERFORMANCE: Database connection pool configuration
	config, err := pgxpool.ParseConfig(dbURL)
	if err != nil {
		logger.WithError(err).Fatal("Failed to parse database URL")
	}
	// OPTIMIZATION: Pool sizing for high-throughput crafting operations
	config.MaxConns = 30 // Higher than economy service due to crafting intensity
	config.MinConns = 10
	config.MaxConnLifetime = 5 * time.Minute
	config.MaxConnIdleTime = 10 * time.Minute

	dbPool, err := pgxpool.NewWithConfig(context.Background(), config)
	if err != nil {
		logger.WithError(err).Fatal("Failed to initialize database pool")
	}

	redisOpts, err := redis.ParseURL(redisURL)
	if err != nil {
		logger.WithError(err).Fatal("Failed to parse Redis URL")
	}

	redisClient := redis.NewClient(redisOpts)

	// PERFORMANCE: Issue #1585 - Runtime Goroutine Monitoring
	monitor := server.NewGoroutineMonitor(400) // Max 400 goroutines for crafting service (higher due to async operations)
	go monitor.Start()
	defer monitor.Stop()
	logger.Info("OK Goroutine monitor started")

	// Initialize repositories and services
	recipeRepo := server.NewRecipeRepository(dbPool)
	orderRepo := server.NewOrderRepository(dbPool)
	stationRepo := server.NewStationRepository(dbPool)
	chainRepo := server.NewChainRepository(dbPool)

	recipeService := server.NewRecipeService(recipeRepo, redisClient)
	orderService := server.NewOrderService(orderRepo, recipeRepo, stationRepo, redisClient)
	stationService := server.NewStationService(stationRepo, redisClient)
	chainService := server.NewChainService(chainRepo, orderService, redisClient)

	logger.Info("Recipe service initialized")
	logger.Info("Order service initialized")
	logger.Info("Station service initialized")
	logger.Info("Production chain service initialized")

	// Initialize JWT validator
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

	// Create HTTP server
	craftingHandler := server.NewCraftingHandler(recipeService, orderService, stationService, chainService, logger, jwtValidator, authEnabled)
	httpServer := server.NewHTTPServer(addr, craftingHandler)

	// Metrics server
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

	// Graceful shutdown
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
