// Auction House Service - Dynamic Pricing Engine
// Issue: #2175 - Dynamic Pricing Auction House mechanics
package main

import (
	"context"
	"log"
	"net/http"
	"os"
	"os/signal"
	"syscall"
	"time"

	"github.com/gorilla/mux"
	"go.uber.org/zap"

	"necpgame/services/auction-house-service-go/internal/models"
	"necpgame/services/auction-house-service-go/internal/service"
)

func main() {
	// Initialize logger
	logger, err := zap.NewProduction()
	if err != nil {
		log.Fatalf("Failed to initialize logger: %v", err)
	}
	defer logger.Sync()

	// Initialize service components
	pricingEngine := service.NewPricingEngine()
	auctionRegistry := models.NewAuctionLotRegistry()

	// Create service instance
	auctionService := &service.Service{
		Registry:       auctionRegistry,
		PricingEngine:  pricingEngine,
		Logger:         logger,
	}

	// Initialize HTTP server
	router := mux.NewRouter()

	// Health check endpoint
	router.HandleFunc("/health", func(w http.ResponseWriter, r *http.Request) {
		w.Header().Set("Content-Type", "application/json")
		w.WriteHeader(http.StatusOK)
		w.Write([]byte(`{"status":"healthy","service":"auction-house"}`))
	}).Methods("GET")

	// API routes
	api := router.PathPrefix("/api/v1").Subrouter()

	// Auction lots
	api.HandleFunc("/lots", auctionService.GetActiveLots).Methods("GET")
	api.HandleFunc("/lots", auctionService.CreateAuctionLot).Methods("POST")
	api.HandleFunc("/lots/{id}", auctionService.GetAuctionLot).Methods("GET")

	// Bids
	api.HandleFunc("/lots/{id}/bids", auctionService.PlaceBid).Methods("POST")
	api.HandleFunc("/lots/{id}/bids", auctionService.GetLotBids).Methods("GET")

	// Market data
	api.HandleFunc("/market/prices", auctionService.GetMarketPrices).Methods("GET")
	api.HandleFunc("/market/analytics", auctionService.GetMarketAnalytics).Methods("GET")

	// Trading history
	api.HandleFunc("/trader/{playerId}/history", auctionService.GetTraderHistory).Methods("GET")

	// System endpoints
	api.HandleFunc("/system/health", auctionService.GetSystemHealth).Methods("GET")
	api.HandleFunc("/system/status", auctionService.GetSystemStatus).Methods("GET")

	// Server configuration
	port := os.Getenv("AUCTION_HOUSE_PORT")
	if port == "" {
		port = "8080"
	}

	srv := &http.Server{
		Addr:         ":" + port,
		Handler:      router,
		ReadTimeout:  15 * time.Second,
		WriteTimeout: 15 * time.Second,
		IdleTimeout:  60 * time.Second,
	}

	// Start server in goroutine
	go func() {
		logger.Info("Starting Auction House Service",
			zap.String("port", port),
			zap.Time("started_at", time.Now()))

		if err := srv.ListenAndServe(); err != nil && err != http.ErrServerClosed {
			logger.Fatal("Failed to start server", zap.Error(err))
		}
	}()

	// Wait for interrupt signal to gracefully shutdown the server
	quit := make(chan os.Signal, 1)
	signal.Notify(quit, syscall.SIGINT, syscall.SIGTERM)
	<-quit

	logger.Info("Shutting down Auction House Service...")

	// Give outstanding requests 30 seconds to complete
	ctx, cancel := context.WithTimeout(context.Background(), 30*time.Second)
	defer cancel()

	if err := srv.Shutdown(ctx); err != nil {
		logger.Fatal("Server forced to shutdown", zap.Error(err))
	}

	logger.Info("Auction House Service stopped")
}