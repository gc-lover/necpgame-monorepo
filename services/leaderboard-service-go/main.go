package main

import (
	"context"
	"net/http"
	_ "net/http/pprof" // OPTIMIZATION: Issue #1584 - profiling
	"os"
	"os/signal"
	"syscall"
	"time"

	"github.com/necpgame/leaderboard-service-go/server"
	"github.com/sirupsen/logrus"
)

func main() {
	logger := logrus.New()
	logger.SetFormatter(&logrus.JSONFormatter{})
	logger.SetLevel(logrus.InfoLevel)
	logger.Info("Leaderboard Service Service starting...")

	addr := getEnv("ADDR", "0.0.0.0:8124")

	httpServer := server.NewHTTPServer(addr, logger)

	// OPTIMIZATION: Issue #1584 - pprof for performance monitoring
	go func() {
		pprofAddr := getEnv("PPROF_ADDR", "localhost:6066")
		logger.WithField("addr", pprofAddr).Info("pprof server starting")
		if err := http.ListenAndServe(pprofAddr, nil); err != nil {
			logger.WithError(err).Error("pprof server failed")
		}
	}()

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
