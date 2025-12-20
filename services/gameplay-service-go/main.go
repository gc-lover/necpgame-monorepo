// Issue: #1584, #1525
package main

import (
	"context"
	"fmt"
	"net/http"
	_ "net/http/pprof" // OPTIMIZATION: Issue #1584 - profiling endpoints
	"os"
	"os/signal"
	"runtime"
	"syscall"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/gameplay-service-go/server"
	"github.com/jackc/pgx/v5/pgxpool"
	"github.com/sirupsen/logrus"
)

func main() {
	startTime := time.Now() // For metrics endpoint

	logger := logrus.New()
	logger.SetFormatter(&logrus.JSONFormatter{})
	logger.SetLevel(logrus.InfoLevel)

	port := os.Getenv("PORT")
	if port == "" {
		port = "8097"
	}

	addr := ":" + port
	logger.WithField("address", addr).Info("Starting Gameplay Service")

	// Issue: #1525 - Initialize database connection pool
	var db *pgxpool.Pool
	dbURL := getEnv("DATABASE_URL", "postgres://necpgame:necpgame@localhost:5432/necpgame?sslmode=disable")
	if dbURL != "" {
		config, err := pgxpool.ParseConfig(dbURL)
		if err != nil {
			logger.WithError(err).Fatal("Failed to parse database URL")
		}
		// Performance: Issue #1605 - DB Connection Pool configuration
		config.MaxConns = 25
		config.MinConns = 5
		config.MaxConnLifetime = 5 * time.Minute
		config.MaxConnIdleTime = 10 * time.Minute

		db, err = pgxpool.NewWithConfig(context.Background(), config)
		if err != nil {
			logger.WithError(err).Fatal("Failed to connect to database")
		}
		defer db.Close()
		logger.Info("Database connection pool initialized")
	}

	httpServer := server.NewHTTPServer(addr, logger, db)

	// OPTIMIZATION: Issue #1584 - Start pprof server for profiling
	go func() {
		pprofAddr := getEnv("PPROF_ADDR", "localhost:6355")
		logger.WithField("addr", pprofAddr).Info("pprof server starting")
		// Endpoints: /debug/pprof/profile, /debug/pprof/heap, /debug/pprof/goroutine
		if err := http.ListenAndServe(pprofAddr, nil); err != nil {
			logger.WithError(err).Error("pprof server failed")
		}
	}()

	// Issue: #1585 - Runtime Goroutine Monitoring
	monitor := server.NewGoroutineMonitor(300, logger) // Max 300 goroutines for gameplay service
	go monitor.Start()
	defer monitor.Stop()
	logger.Info("OK Goroutine monitor started")

	// Issue: #1515 - Start affix rotation scheduler (weekly rotation on Monday 00:00 UTC)
	affixScheduler := server.NewAffixScheduler(db, logger)
	if affixScheduler != nil {
		affixScheduler.Start()
		defer affixScheduler.Stop()
	}

	// Issue: #1605 - Health endpoint
	http.HandleFunc("/health", func(w http.ResponseWriter, r *http.Request) {
		w.Header().Set("Content-Type", "application/json")
		w.WriteHeader(http.StatusOK)
		w.Write([]byte(`{"status":"ok","service":"gameplay-service","timestamp":"` + time.Now().Format(time.RFC3339) + `"}`))
	})

	// Issue: #1605 - Metrics endpoint
	http.HandleFunc("/metrics", func(w http.ResponseWriter, r *http.Request) {
		w.Header().Set("Content-Type", "application/json")
		w.WriteHeader(http.StatusOK)
		w.Write([]byte(`{"uptime":"` + time.Since(startTime).String() + `","goroutines":` + fmt.Sprintf("%d", runtime.NumGoroutine()) + `}`))
	})

	// Start metrics/health server on separate port
	go func() {
		metricsAddr := getEnv("METRICS_ADDR", ":9226")
		logger.WithField("address", metricsAddr).Info("Starting metrics/health server")
		if err := http.ListenAndServe(metricsAddr, nil); err != nil {
			logger.WithError(err).Error("Metrics server failed")
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
