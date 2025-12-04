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

	"github.com/necpgame/world-service-go/server"
	"github.com/sirupsen/logrus"
)

func main() {
	logger := logrus.New()
	logger.SetFormatter(&logrus.JSONFormatter{})
	logger.SetLevel(logrus.InfoLevel)

	port := os.Getenv("PORT")
	if port == "" {
		port = "8097"
	}

	addr := ":" + port
	logger.WithField("address", addr).Info("Starting World Service")

	httpServer := server.NewHTTPServer(addr, logger)

	// OPTIMIZATION: Issue #1584 - Start pprof server for profiling
	go func() {
		pprofAddr := getEnv("PPROF_ADDR", "localhost:6073")
		logger.WithField("addr", pprofAddr).Info("pprof server starting")
		// Endpoints: /debug/pprof/profile, /debug/pprof/heap, /debug/pprof/goroutine
		if err := http.ListenAndServe(pprofAddr, nil); err != nil {
			logger.WithError(err).Error("pprof server failed")
		}
	}()

	go func() {
		logger.Info("HTTP server listening on ", addr)
		if err := httpServer.Start(); err != nil && err != http.ErrServerClosed {
			logger.WithError(err).Fatal("Failed to start HTTP server")
		}
	}()

	quit := make(chan os.Signal, 1)
	signal.Notify(quit, syscall.SIGINT, syscall.SIGTERM)
	<-quit

	logger.Info("Shutting down server...")
	ctx, cancel := context.WithTimeout(context.Background(), 30*time.Second)
	defer cancel()

	if err := httpServer.Shutdown(ctx); err != nil {
		logger.WithError(err).Error("Server forced to shutdown")
	}

	logger.Info("Server exited")
}

func getEnv(key, defaultValue string) string {
	if value := os.Getenv(key); value != "" {
		return value
	}
	return defaultValue
}
