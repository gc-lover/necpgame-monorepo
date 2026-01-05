package main

import (
	"fmt"
	"log"

	"github.com/gc-lover/necpgame/services/support-service-go/internal/config"
	"github.com/gc-lover/necpgame/services/support-service-go/internal/database"
	"go.uber.org/zap"
)

func main() {
	// Initialize logger
	logger, err := zap.NewProduction()
	if err != nil {
		log.Fatalf("Failed to initialize logger: %v", err)
	}
	defer logger.Sync()

	// Load configuration
	cfg, err := config.Load()
	if err != nil {
		logger.Fatal("Failed to load configuration", zap.Error(err))
	}

	// Initialize database
	db, err := database.NewConnection(cfg.Database)
	if err != nil {
		logger.Fatal("Failed to connect to database", zap.Error(err))
	}
	defer db.Close()

	logger.Info("Support service initialized successfully",
		zap.Int("port", cfg.Server.Port),
		zap.String("database", cfg.Database.DBName))

	fmt.Println("Support Service Go is running successfully!")
}
