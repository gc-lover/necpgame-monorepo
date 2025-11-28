package main

import (
	"context"
	"database/sql"
	"fmt"
	"os"
	"os/signal"
	"syscall"
	"time"

	_ "github.com/lib/pq"
	"github.com/necpgame/combat-ai-service-go/server"
)

func main() {
	logger := server.GetLogger()

	dbHost := getEnv("DB_HOST", "localhost")
	dbPort := getEnv("DB_PORT", "5432")
	dbUser := getEnv("DB_USER", "postgres")
	dbPassword := getEnv("DB_PASSWORD", "postgres")
	dbName := getEnv("DB_NAME", "necpgame")

	dsn := fmt.Sprintf(
		"host=%s port=%s user=%s password=%s dbname=%s sslmode=disable",
		dbHost, dbPort, dbUser, dbPassword, dbName,
	)

	db, err := sql.Open("postgres", dsn)
	if err != nil {
		logger.WithError(err).Fatal("Failed to connect to database")
	}
	defer db.Close()

	if err := db.Ping(); err != nil {
		logger.WithError(err).Fatal("Failed to ping database")
	}

	logger.Info("Connected to database")

	repo := server.NewRepository(db)
	service := server.NewService(repo)
	handlers := server.NewHandlers(service)

	httpAddr := getEnv("HTTP_ADDR", ":8090")
	httpServer := server.NewHTTPServer(httpAddr, handlers)

	ctx, cancel := context.WithCancel(context.Background())
	defer cancel()

	go func() {
		if err := httpServer.Start(ctx); err != nil {
			logger.WithError(err).Fatal("HTTP server failed")
		}
	}()

	sigChan := make(chan os.Signal, 1)
	signal.Notify(sigChan, syscall.SIGINT, syscall.SIGTERM)
	<-sigChan

	logger.Info("Shutting down...")
	cancel()

	shutdownCtx, shutdownCancel := context.WithTimeout(context.Background(), 30*time.Second)
	defer shutdownCancel()

	if err := httpServer.Shutdown(shutdownCtx); err != nil {
		logger.WithError(err).Error("Failed to shutdown HTTP server")
	}

	logger.Info("Shutdown complete")
}

func getEnv(key, defaultValue string) string {
	if value := os.Getenv(key); value != "" {
		return value
	}
	return defaultValue}

