package main

import (
	"context"
	"log"
	"net/http"
	"os"
	"os/signal"
	"syscall"
	"time"

	"global-state-service-go/internal/handlers"
	"global-state-service-go/internal/repository"
	"global-state-service-go/internal/service"
	"global-state-service-go/pkg/api"

	"github.com/jackc/pgx/v5/pgxpool"
)

func main() {
	// Database connection
	dbURL := os.Getenv("DATABASE_URL")
	if dbURL == "" {
		dbURL = "postgres://necpgame:necpgame_password@localhost:5432/necpgame?sslmode=disable"
	}

	ctx := context.Background()
	dbpool, err := pgxpool.New(ctx, dbURL)
	if err != nil {
		log.Fatalf("Unable to connect to database: %v", err)
	}
	defer dbpool.Close()

	// Initialize layers
	repo := repository.NewRepository(dbpool)
	svc := service.NewService(repo)
	handler := handlers.NewHandler(svc)

	// Create server
	srv, err := api.NewServer(handler, nil)
	if err != nil {
		log.Fatalf("Failed to create server: %v", err)
	}

	httpSrv := &http.Server{
		Addr:    ":8087",
		Handler: srv,
	}

	// Start server in goroutine
	go func() {
		log.Printf("Global State Service starting on :8087")
		if err := httpSrv.ListenAndServe(); err != nil && err != http.ErrServerClosed {
			log.Fatalf("Server failed to start: %v", err)
		}
	}()

	// Wait for interrupt signal to gracefully shutdown the server
	quit := make(chan os.Signal, 1)
	signal.Notify(quit, syscall.SIGINT, syscall.SIGTERM)
	<-quit
	log.Println("Shutting down server...")

	// Graceful shutdown
	ctx, cancel := context.WithTimeout(context.Background(), 30*time.Second)
	defer cancel()
	if err := httpSrv.Shutdown(ctx); err != nil {
		log.Fatalf("Server forced to shutdown: %v", err)
	}

	log.Println("Server exited")
}