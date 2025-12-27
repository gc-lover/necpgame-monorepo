package main

import (
	"context"
	"log"
	"net/http"
	"os"
	"os/signal"
	"syscall"
	"time"

	"combat-hacking-service-go/pkg/handlers"
	"combat-hacking-service-go/pkg/repository"
	"combat-hacking-service-go/server"
)

func main() {
	// Initialize logger
	logger := log.New(os.Stdout, "[COMBAT-HACKING] ", log.LstdFlags|log.Lshortfile)

	// Initialize repository
	repo := repository.NewRepository()

	// Initialize handlers
	h := handlers.NewHandlers(repo, logger)

	// Initialize server
	srv := server.NewServer(h, logger)

	// Start server in goroutine
	go func() {
		logger.Println("Starting Combat Hacking Service on :8084")
		if err := srv.Start(":8084"); err != nil && err != http.ErrServerClosed {
			logger.Fatalf("Server failed to start: %v", err)
		}
	}()

	// Wait for interrupt signal to gracefully shutdown the server
	quit := make(chan os.Signal, 1)
	signal.Notify(quit, syscall.SIGINT, syscall.SIGTERM)
	<-quit
	logger.Println("Shutting down server...")

	// Give outstanding requests 30 seconds to complete
	ctx, cancel := context.WithTimeout(context.Background(), 30*time.Second)
	defer cancel()

	if err := srv.Shutdown(ctx); err != nil {
		logger.Fatalf("Server forced to shutdown: %v", err)
	}

	logger.Println("Server exited")
}

// Issue: #143875347, #143875814

