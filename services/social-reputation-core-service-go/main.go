// Issue: #1595, #1584
package main

import (
	"context"
	"net/http"
	_ "net/http/pprof" // OPTIMIZATION: Issue #1584 - profiling endpoints
	"os"
	"os/signal"
	"syscall"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/social-reputation-core-service-go/server"
)

func main() {
	logger := server.GetLogger()
	logger.Info("Social Reputation Core Service (Go) starting...")

	addr := getEnv("SERVER_ADDR", ":8178")

	repo := server.NewRepository()

	service := server.NewService(repo)
	httpServer := server.NewHTTPServer(addr, service)

	// OPTIMIZATION: Issue #1584 - Start pprof server for profiling
	go func() {
		pprofAddr := getEnv("PPROF_ADDR", "localhost:6658")
		logger.WithField("addr", pprofAddr).Info("pprof server starting")
		// Endpoints: /debug/pprof/profile, /debug/pprof/heap, /debug/pprof/goroutine
		if err := http.ListenAndServe(pprofAddr, nil); err != nil {
			logger.WithError(err).Error("pprof server failed")
		}
	}()

	// Issue: #1585 - Runtime Goroutine Monitoring
	monitor := server.NewGoroutineMonitor(200, logger) // Max 200 goroutines for social-reputation-core service
	go monitor.Start()
	defer monitor.Stop()
	logger.Info("OK Goroutine monitor started")

	stop := make(chan os.Signal, 1)
	signal.Notify(stop, os.Interrupt, syscall.SIGTERM)

	go func() {
		logger.WithField("addr", addr).Info("HTTP server starting")
		if err := httpServer.Start(); err != nil && err != http.ErrServerClosed {
			logger.WithError(err).Fatal("Server failed")
		}
	}()

	<-stop
	logger.Info("Shutting down gracefully...")

	ctx, cancel := context.WithTimeout(context.Background(), 30*time.Second)
	defer cancel()

	if err := httpServer.Shutdown(ctx); err != nil {
		logger.WithError(err).Error("Shutdown error")
	}

	logger.Info("Server stopped")
}

func getEnv(key, defaultValue string) string {
	if value := os.Getenv(key); value != "" {
		return value
	}
	return defaultValue
}
