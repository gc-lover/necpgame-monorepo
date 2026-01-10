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

	"technology_progression/internal/handlers"
	"technology_progression/internal/service"
	api "technology_progression"
)

func main() {
	fmt.Println("Technology Progression Service Starting...")

	// Initialize service with business logic
	techProgressionSvc := service.NewTechnologyProgressionService()

	// Create HTTP handlers with ogen-generated interfaces
	httpHandlers := handlers.NewTechnologyProgressionHandlers(techProgressionSvc)

	// Create security handler (mock implementation)
	securityHandler := &MockSecurityHandler{}

	// Create ogen server
	server, err := api.NewServer(httpHandlers, securityHandler)
	if err != nil {
		log.Fatalf("Failed to create server: %v", err)
	}

	// Configure HTTP server
	port := os.Getenv("PORT")
	if port == "" {
		port = "8160"
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
		fmt.Printf("Technology Progression Service listening on port %s\n", port)
		if err := httpServer.ListenAndServe(); err != nil && err != http.ErrServerClosed {
			log.Fatalf("Server failed to start: %v", err)
		}
	}()

	// Wait for interrupt signal to gracefully shutdown the server
	quit := make(chan os.Signal, 1)
	signal.Notify(quit, syscall.SIGINT, syscall.SIGTERM)
	<-quit
	fmt.Println("Shutting down Technology Progression Service...")

	// Graceful shutdown with timeout
	ctx, cancel := context.WithTimeout(context.Background(), 30*time.Second)
	defer cancel()

	if err := httpServer.Shutdown(ctx); err != nil {
		log.Fatalf("Server forced to shutdown: %v", err)
	}

	fmt.Println("Technology Progression Service stopped")
}

// MockSecurityHandler implements the SecurityHandler interface for development
type MockSecurityHandler struct{}

func (m *MockSecurityHandler) HandleBearerAuth(ctx context.Context, operationName api.OperationName, t api.BearerAuth) (context.Context, error) {
	// Mock implementation - accept any token
	return ctx, nil
}