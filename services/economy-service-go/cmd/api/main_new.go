package main

import (
	"context"
	"fmt"
	"log"
	"net/http"
	"os"
	"os/signal"
	"syscall"
	"time"

	"go.uber.org/zap"

	"necpgame/services/economy-service-go/internal/handlers"
	_ "necpgame/services/economy-service-go/internal/handlers" // import for side effects
	api "necpgame/services/economy-service-go/pkg/services/economy-service-go/pkg/api"
)

func main() {
	logger, err := zap.NewProduction()
	if err != nil {
		log.Fatalf("Failed to create logger: %v", err)
	}
	defer logger.Sync()

	fmt.Println("Economy Service Starting...")

	// Create handlers with BazaarBot integration
	economyHandlers := handlers.NewEconomyHandlers(logger)

	// Create ogen server
	server, err := api.NewServer(economyHandlers)
	if err != nil {
		log.Fatalf("Failed to create server: %v", err)
	}

	// Configure HTTP server
	port := os.Getenv("PORT")
	if port == "" {
		port = "8083"
	}

	httpServer := &http.Server{
		Addr:         ":" + port,
		Handler:      server,
		ReadTimeout:  10 * time.Second,
		WriteTimeout: 10 * time.Second,
		IdleTimeout:  60 * time.Second,
	}

	// Start server in goroutine
	go func() {
		fmt.Printf("Economy Service listening on port %s\n", port)
		if err := httpServer.ListenAndServe(); err != nil && err != http.ErrServerClosed {
			log.Fatalf("Server failed to start: %v", err)
		}
	}()

	// Wait for interrupt signal to gracefully shutdown
	quit := make(chan os.Signal, 1)
	signal.Notify(quit, syscall.SIGINT, syscall.SIGTERM)
	<-quit

	fmt.Println("Shutting down Economy Service...")

	// Graceful shutdown
	ctx, cancel := context.WithTimeout(context.Background(), 30*time.Second)
	defer cancel()

	if err := httpServer.Shutdown(ctx); err != nil {
		log.Fatalf("Server forced to shutdown: %v", err)
	}

	fmt.Println("Economy Service stopped")
}