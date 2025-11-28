package main

import (
	"context"
	"net/http"
	"os"
	"os/signal"
	"syscall"
	"time"

	"github.com/jackc/pgx/v5/pgxpool"
	"github.com/necpgame/cosmetic-service-go/server"
	"github.com/prometheus/client_golang/prometheus/promhttp"
)

func main() {
	logger := server.GetLogger()
	logger.Info("Cosmetic Service (Go) starting...")

	addr := getEnv("ADDR", "0.0.0.0:8095")
	metricsAddr := getEnv("METRICS_ADDR", ":9105")

	dbURL := getEnv("DATABASE_URL", "postgresql://necpgame:necpgame@localhost:5432/necpgame?sslmode=disable")

	keycloakIssuer := getEnv("KEYCLOAK_ISSUER", "http://localhost:8080/realms/necpgame")
	jwksURL := getEnv("JWKS_URL", keycloakIssuer+"/protocol/openid-connect/certs")
	authEnabled := getEnv("AUTH_ENABLED", "true") == "true"

	dbPool, err := pgxpool.New(context.Background(), dbURL)
	if err != nil {
		logger.WithError(err).Fatal("Failed to connect to database")
	}
	defer dbPool.Close()

	catalogRepo := server.NewCosmeticCatalogRepository(dbPool)
	shopRepo := server.NewCosmeticShopRepository(dbPool)
	purchaseRepo := server.NewCosmeticPurchaseRepository(dbPool)
	equipmentRepo := server.NewCosmeticEquipmentRepository(dbPool)
	inventoryRepo := server.NewCosmeticInventoryRepository(dbPool)

	catalogService := server.NewCosmeticCatalogService(catalogRepo)
	shopService := server.NewCosmeticShopService(shopRepo)
	purchaseService := server.NewCosmeticPurchaseService(purchaseRepo, catalogRepo)
	equipmentService := server.NewCosmeticEquipmentService(equipmentRepo, inventoryRepo)
	inventoryService := server.NewCosmeticInventoryService(inventoryRepo)

	jwtValidator := server.NewJwtValidator(keycloakIssuer, jwksURL, logger)
	httpServer := server.NewHTTPServer(
		addr,
		catalogService,
		shopService,
		purchaseService,
		equipmentService,
		inventoryService,
		jwtValidator,
		authEnabled,
	)

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

