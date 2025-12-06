// Issue: #1584
package main

import (
	"context"
	"net/http"
	_ "net/http/pprof" // OPTIMIZATION: Issue #1584 - profiling endpoints
	"os"
	"os/signal"
	"syscall"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/combat-sessions-service-go/server"
	"github.com/sirupsen/logrus"
)

func main() {
	logger := logrus.New()
	logger.SetFormatter(&logrus.JSONFormatter{})
	logger.SetLevel(logrus.InfoLevel)
	logger.Info("Combat Sessions Service Service starting...")

	addr := getEnv("ADDR", "0.0.0.0:8158")

	// Database connection
	dbConnStr := getEnv("DATABASE_URL", "postgres://necpgame:necpgame@localhost:5432/necpgame?sslmode=disable")
	repo, err := server.NewPostgresRepository(dbConnStr)
	if err != nil {
		logger.WithError(err).Fatal("Failed to connect to database")
	}
	defer repo.Close()
	logger.Info("OK Database connection established")

	// Redis and Kafka addresses (optional - can be empty for basic functionality)
	redisAddr := getEnv("REDIS_ADDR", "")
	kafkaBrokers := getEnv("KAFKA_BROKERS", "")

	// Initialize service
	service := server.NewCombatSessionService(repo, redisAddr, kafkaBrokers)
	logger.Info("OK Combat Session Service initialized")

	httpServer := server.NewHTTPServer(addr, logger, service)

	// OPTIMIZATION: Issue #1584 - Start pprof server for profiling
	go func() {
		pprofAddr := getEnv("PPROF_ADDR", "localhost:6096")
		logger.WithField("addr", pprofAddr).Info("pprof server starting")
		// Endpoints: /debug/pprof/profile, /debug/pprof/heap, /debug/pprof/goroutine
		if err := http.ListenAndServe(pprofAddr, nil); err != nil {
			logger.WithError(err).Error("pprof server failed")
		}
	}()

	// Issue: #1585 - Runtime Goroutine Monitoring
	monitor := server.NewGoroutineMonitor(500, logger) // Max 500 goroutines for combat sessions
	go monitor.Start()
	defer monitor.Stop()
	logger.Info("OK Goroutine monitor started")

	sigChan := make(chan os.Signal, 1)
	signal.Notify(sigChan, os.Interrupt, syscall.SIGTERM)

	go func() {
		<-sigChan
		logger.Info("Shutting down server...")

		shutdownCtx, shutdownCancel := context.WithTimeout(context.Background(), 5*time.Second)
		defer shutdownCancel()
		httpServer.Shutdown(shutdownCtx)
	}()

	logger.WithField("addr", addr).Info("HTTP server starting")
	if err := httpServer.Start(); err != nil {
		logger.WithError(err).Fatal("Server error")
	}

	logger.Info("Server stopped")
}

func getEnv(key, defaultValue string) string {
	if value := os.Getenv(key); value != "" {
		return value
	}
	return defaultValue
}
