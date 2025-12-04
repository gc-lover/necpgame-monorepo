// Issue: #1604, #1584
package main

import (
	"context"
	"log"
	"net/http"
	_ "net/http/pprof" // OPTIMIZATION: Issue #1584 - profiling endpoints
	"os"
	"os/signal"
	"syscall"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/loot-service-go/server"
	"github.com/sirupsen/logrus"
)

func main() {
	logger := logrus.New()
	logger.SetFormatter(&logrus.JSONFormatter{})
	logger.SetLevel(logrus.InfoLevel)

	// OPTIMIZATION: Issue #1584 - Start pprof server for profiling
	go func() {
		pprofAddr := getEnv("PPROF_ADDR", "localhost:6350")
		logger.WithField("addr", pprofAddr).Info("Starting pprof server")
		// Endpoints: /debug/pprof/profile, /debug/pprof/heap, /debug/pprof/goroutine
		if err := http.ListenAndServe(pprofAddr, nil); err != nil {
			logger.WithError(err).Error("pprof server error")
		}
	}()

	httpServer := server.NewHTTPServer(":8080", logger)

	go func() {
		logger.Info("Starting HTTP server on :8080")
		if err := httpServer.Start(); err != nil {
			logger.Fatalf("HTTP server failed: %v", err)
		}
	}()

	quit := make(chan os.Signal, 1)
	signal.Notify(quit, syscall.SIGINT, syscall.SIGTERM)
	<-quit

	logger.Info("Shutting down server...")
	ctx, cancel := context.WithTimeout(context.Background(), 5*time.Second)
	defer cancel()

	if err := httpServer.Shutdown(ctx); err != nil {
		log.Fatalf("Server forced to shutdown: %v", err)
	}

	logger.Info("Server exited")
}

func getEnv(key, defaultValue string) string {
	if value := os.Getenv(key); value != "" {
		return value
	}
	return defaultValue
}
