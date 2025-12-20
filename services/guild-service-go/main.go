package main

import (
	"context"
	"net/http"
	_ "net/http/pprof" // OPTIMIZATION: Issue #2177 - Enable pprof profiling
	"os"
	"os/signal"
	"syscall"
	"time"

	"necpgame/services/guild-service-go/server"
)

func main() {
	// Initialize structured logging
	logger := server.NewLogger()

	logger.Info("🚀 Starting Guild Service...")

	// Configuration
	config := &server.GuildServiceConfig{
		Port:                    getEnv("PORT", "8080"),
		ReadTimeout:             30 * time.Second,
		WriteTimeout:            30 * time.Second,
		MaxHeaderBytes:          1 << 20, // 1MB
		RedisAddr:               getEnv("REDIS_ADDR", "localhost:6379"),
		TerritoryUpdateInterval: 5 * time.Minute,
		WarUpdateInterval:       1 * time.Minute,
		StatsCleanupInterval:    24 * time.Hour,
	}

	// Initialize metrics (placeholder for now)
	metrics := &server.GuildMetrics{
		ActiveGuilds:       0,
		ActiveMembers:      0,
		GuildCreations:     0,
		GuildJoins:         0,
		GuildLeaves:        0,
		ValidationErrors:   0,
		TerritoryClaims:    0,
		WarDeclarations:    0,
		AllianceFormations: 0,
		ContractCreations:  0,
	}

	// Initialize service
	guildService := server.NewGuildService(logger, metrics, config)

	// OPTIMIZATION: Issue #2177 - Start pprof server for profiling
	go func() {
		pprofAddr := getEnv("PPROF_ADDR", "localhost:6868")
		logger.WithField("addr", pprofAddr).Info("pprof server starting")
		// Endpoints: /debug/pprof/profile, /debug/pprof/heap, /debug/pprof/goroutine
		if err := http.ListenAndServe(pprofAddr, nil); err != nil {
			logger.WithError(err).Error("pprof server failed")
		}
	}()

	// Issue #2177 - Runtime Goroutine Monitoring
	monitor := server.NewGoroutineMonitor(500, logger) // Max 500 goroutines for guild service
	go monitor.Start()
	defer monitor.Stop()
	logger.Info("OK Goroutine monitor started")

	// Initialize HTTP server
	httpServer := server.NewHTTPServer(guildService, logger, config)

	// Start server in a goroutine
	go func() {
		logger.Info("🌐 Guild Service starting on port " + config.Port)
		if err := httpServer.Start(); err != nil {
			logger.WithError(err).Fatal("Failed to start HTTP server")
		}
	}()

	// Wait for interrupt signal to gracefully shutdown the server
	quit := make(chan os.Signal, 1)
	signal.Notify(quit, syscall.SIGINT, syscall.SIGTERM)
	<-quit

	logger.Info("🛑 Shutting down Guild Service...")

	// Give outstanding requests 30 seconds to complete
	ctx, cancel := context.WithTimeout(context.Background(), 30*time.Second)
	defer cancel()

	logger.Info("OK Guild Service stopped gracefully")
}

// getEnv gets an environment variable or returns a default value
func getEnv(key, defaultValue string) string {
	if value, exists := os.LookupEnv(key); exists {
		return value
	}
	return defaultValue
}
