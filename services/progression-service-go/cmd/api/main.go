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

	"necpgame/services/progression-service-go/internal/handlers"
	"necpgame/services/progression-service-go/internal/service"
	"necpgame/services/progression-service-go/pkg/api"
)

func main() {
	fmt.Println("Endgame Progression Service Starting...")

	// Initialize service with business logic
	progressionSvc := service.NewProgressionService()

	// Create HTTP handlers with ogen-generated interfaces
	httpHandlers := handlers.NewHandler(progressionSvc)

	// Create security handler
	securityHandler := &handlers.SecurityHandler{}

	// Create ogen server
	server, err := api.NewServer(httpHandlers, securityHandler)
	if err != nil {
		log.Fatalf("Failed to create server: %v", err)
	}

	// Configure HTTP server
	port := os.Getenv("PORT")
	if port == "" {
		port = "8084"
	}

	httpServer := &http.Server{
		Addr:         ":" + port,
		Handler:      server,
		ReadTimeout:  30 * time.Second,
		WriteTimeout: 30 * time.Second,
		IdleTimeout:  120 * time.Second,
	}

	// Start server in goroutine
	go func() {
		fmt.Printf("Endgame Progression Service listening on port %s\n", port)
		if err := httpServer.ListenAndServe(); err != nil && err != http.ErrServerClosed {
			log.Fatalf("Server failed to start: %v", err)
		}
	}()

	// Wait for interrupt signal to gracefully shutdown the server
	quit := make(chan os.Signal, 1)
	signal.Notify(quit, syscall.SIGINT, syscall.SIGTERM)
	<-quit
	fmt.Println("Shutting down server...")

	// Give outstanding requests 30 seconds to complete
	ctx, cancel := context.WithTimeout(context.Background(), 30*time.Second)
	defer cancel()

	if err := httpServer.Shutdown(ctx); err != nil {
		log.Fatalf("Server forced to shutdown: %v", err)
	}

	fmt.Println("Server exited")
}