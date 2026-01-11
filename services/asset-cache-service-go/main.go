package main

import (
	"context"
	"database/sql"
	"log/slog"
	"os"
	"os/signal"
	"syscall"
	"time"

	_ "github.com/lib/pq"
	"necpgame/services/asset-cache-service-go/server"
)

func main() {
	// Configure structured logging
	logger := slog.New(slog.NewJSONHandler(os.Stdout, &slog.HandlerOptions{
		Level: slog.LevelInfo,
	}))
	slog.SetDefault(logger)

	slog.Info("Starting Asset Cache Service", "version", "1.0.0")

	// Database connection with connection pooling
	db, err := sql.Open("postgres", os.Getenv("DATABASE_URL"))
	if err != nil {
		slog.Error("Failed to open database connection", "error", err)
		os.Exit(1)
	}
	defer db.Close()

	// Configure connection pool for high-performance asset caching
	db.SetMaxOpenConns(50)  // Higher for asset cache operations
	db.SetMaxIdleConns(10)
	db.SetConnMaxLifetime(30 * time.Minute)

	// Test database connection
	ctx, cancel := context.WithTimeout(context.Background(), 10*time.Second)
	defer cancel()

	if err := db.PingContext(ctx); err != nil {
		slog.Error("Failed to ping database", "error", err)
		os.Exit(1)
	}

	slog.Info("Database connection established")

	// Initialize memory-mapped cache manager
	cacheManager, err := server.NewMemoryMappedCacheManager(server.CacheConfig{
		MaxMemoryMB:     1024, // 1GB memory limit
		MaxFileSizeMB:   100,  // Max 100MB per file
		CacheDir:        "./cache",
		CompressionEnabled: true,
		PreloadEnabled:     true,
	})
	if err != nil {
		slog.Error("Failed to initialize cache manager", "error", err)
		os.Exit(1)
	}
	defer cacheManager.Close()

	slog.Info("Memory-mapped cache manager initialized",
		"max_memory_mb", 1024,
		"max_file_size_mb", 100)

	// Initialize service components
	repo := server.NewPostgresRepository(db)
	svc := server.NewAssetCacheService(repo, cacheManager)
	middleware := server.NewMiddleware()

	// Start HTTP server
	httpServer := server.NewHTTPServer(svc, middleware)

	// Graceful shutdown
	go func() {
		sigChan := make(chan os.Signal, 1)
		signal.Notify(sigChan, syscall.SIGINT, syscall.SIGTERM)
		sig := <-sigChan

		slog.Info("Received shutdown signal", "signal", sig)

		// Shutdown HTTP server
		shutdownCtx, shutdownCancel := context.WithTimeout(context.Background(), 30*time.Second)
		defer shutdownCancel()

		if err := httpServer.Shutdown(shutdownCtx); err != nil {
			slog.Error("HTTP server shutdown error", "error", err)
		}

		// Close cache manager
		if err := cacheManager.Close(); err != nil {
			slog.Error("Cache manager close error", "error", err)
		}

		slog.Info("Asset Cache Service shutdown complete")
		os.Exit(0)
	}()

	// Start server
	port := os.Getenv("PORT")
	if port == "" {
		port = "8080"
	}

	slog.Info("Starting HTTP server", "port", port)
	if err := httpServer.Start(":" + port); err != nil {
		slog.Error("Failed to start HTTP server", "error", err)
		os.Exit(1)
	}
}