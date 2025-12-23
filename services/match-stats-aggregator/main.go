// Issue: #2214
// Real-time Match Statistics Aggregation Service
// High-performance MMOFPS statistics system with event-driven architecture

package main

import (
	"context"
	"flag"
	"net/http"
	"os"
	"os/signal"
	"syscall"
	"time"

	"github.com/gc-lover/necpgame/services/match-stats-aggregator/internal/aggregator"
	"github.com/gc-lover/necpgame/services/match-stats-aggregator/internal/api"
	"github.com/gc-lover/necpgame/services/match-stats-aggregator/internal/cache"
	"github.com/gc-lover/necpgame/services/match-stats-aggregator/internal/collector"
	"github.com/prometheus/client_golang/prometheus/promhttp"
	"go.uber.org/zap"
)

func main() {
	// Configuration
	var (
		httpAddr    = flag.String("http", ":8080", "HTTP server address")
		redisAddr   = flag.String("redis", "localhost:6379", "Redis server address")
		redisPass   = flag.String("redis-pass", "", "Redis password")
		redisDB     = flag.Int("redis-db", 0, "Redis database")
		logLevel    = flag.String("log-level", "info", "Log level (debug, info, warn, error)")
		workers     = flag.Int("workers", 4, "Number of worker goroutines")
		bufferSize  = flag.Int("buffer-size", 10000, "Event buffer size")
	)
	flag.Parse()

	// Initialize logger
	config := zap.NewProductionConfig()
	if *logLevel == "debug" {
		config.Level = zap.NewAtomicLevelAt(zap.DebugLevel)
	}

	logger, err := config.Build()
	if err != nil {
		panic("Failed to create logger: " + err.Error())
	}
	defer logger.Sync()

	logger.Info("Starting Match Statistics Aggregator",
		zap.String("http_addr", *httpAddr),
		zap.String("redis_addr", *redisAddr),
		zap.Int("workers", *workers),
		zap.Int("buffer_size", *bufferSize))

	// Initialize Redis cache
	cacheConfig := &cache.CacheConfig{
		Addr:         *redisAddr,
		Password:     *redisPass,
		DB:           *redisDB,
		PoolSize:     10,
		MinIdleConns: 5,
		DefaultTTL:   5 * time.Minute,
		MatchTTL:     10 * time.Minute,
		PlayerTTL:    2 * time.Minute,
		SnapshotTTL:  30 * time.Minute,
		MaxRetries:   3,
		DialTimeout:  5 * time.Second,
		ReadTimeout:  3 * time.Second,
		WriteTimeout: 3 * time.Second,
	}

	redisCache, err := cache.NewRedisCache(cacheConfig, logger)
	if err != nil {
		logger.Fatal("Failed to connect to Redis", zap.Error(err))
	}
	defer redisCache.Close()

	// Initialize statistics aggregator
	aggConfig := &aggregator.AggregatorConfig{
		WorkerCount:            *workers,
		UpdateInterval:         5 * time.Second,
		RetentionPeriod:        24 * time.Hour,
		MaxConcurrentMatches:   1000,
		BatchSize:             100,
		CleanupInterval:       1 * time.Hour,
	}

	statsAggregator, err := aggregator.NewStatisticsAggregator(aggConfig, logger)
	if err != nil {
		logger.Fatal("Failed to create statistics aggregator", zap.Error(err))
	}

	// Start aggregator
	if err := statsAggregator.Start(); err != nil {
		logger.Fatal("Failed to start statistics aggregator", zap.Error(err))
	}
	defer statsAggregator.Stop()

	// Initialize event collector
	collectorConfig := &collector.CollectorConfig{
		BufferSize:     *bufferSize,
		WorkerCount:    *workers,
		BatchSize:      50,
		FlushInterval:  1 * time.Second,
		MaxRetries:     3,
		RetryDelay:     100 * time.Millisecond,
	}

	eventCollector := collector.NewEventCollector(collectorConfig, logger)

	// Start collector
	if err := eventCollector.Start(); err != nil {
		logger.Fatal("Failed to start event collector", zap.Error(err))
	}
	defer eventCollector.Stop()

	// Initialize API handlers
	statsHandler := api.NewStatisticsHandler(statsAggregator, redisCache, logger)

	// Setup HTTP routes
	mux := http.NewServeMux()

	// Statistics endpoints
	mux.HandleFunc("/api/v1/match-stats/matches/", func(w http.ResponseWriter, r *http.Request) {
		switch r.Method {
		case http.MethodGet:
			path := r.URL.Path
			if strings.Contains(path, "/players/") {
				statsHandler.GetPlayerStatistics(w, r)
			} else if strings.Contains(path, "/leaderboard/") {
				statsHandler.GetLeaderboard(w, r)
			} else {
				statsHandler.GetMatchStatistics(w, r)
			}
		default:
			http.Error(w, "Method not allowed", http.StatusMethodNotAllowed)
		}
	})

	mux.HandleFunc("/api/v1/match-stats/matches/active", func(w http.ResponseWriter, r *http.Request) {
		if r.Method == http.MethodGet {
			statsHandler.GetActiveMatches(w, r)
		} else {
			http.Error(w, "Method not allowed", http.StatusMethodNotAllowed)
		}
	})

	mux.HandleFunc("/api/v1/match-stats/system/stats", func(w http.ResponseWriter, r *http.Request) {
		if r.Method == http.MethodGet {
			statsHandler.GetSystemStats(w, r)
		} else {
			http.Error(w, "Method not allowed", http.StatusMethodNotAllowed)
		}
	})

	// Health and monitoring
	mux.HandleFunc("/health", func(w http.ResponseWriter, r *http.Request) {
		w.WriteHeader(http.StatusOK)
		w.Write([]byte("OK"))
	})

	mux.HandleFunc("/ready", func(w http.ResponseWriter, r *http.Request) {
		w.WriteHeader(http.StatusOK)
		w.Write([]byte("READY"))
	})

	// Prometheus metrics
	mux.Handle("/metrics", promhttp.Handler())

	// Create HTTP server with timeouts for performance
	server := &http.Server{
		Addr:         *httpAddr,
		Handler:      mux,
		ReadTimeout:  15 * time.Second,
		WriteTimeout: 15 * time.Second,
		IdleTimeout:  60 * time.Second,
	}

	// Start HTTP server in a goroutine
	go func() {
		logger.Info("Starting HTTP server", zap.String("addr", *httpAddr))
		if err := server.ListenAndServe(); err != nil && err != http.ErrServerClosed {
			logger.Fatal("Failed to start HTTP server", zap.Error(err))
		}
	}()

	// Setup graceful shutdown
	shutdownChan := make(chan os.Signal, 1)
	signal.Notify(shutdownChan, os.Interrupt, syscall.SIGTERM)

	// Demo: Start a sample match for testing
	go func() {
		time.Sleep(2 * time.Second) // Wait for services to start

		matchID := "demo-match-001"
		if err := statsAggregator.StartMatch(matchID, "neon_city", "team_deathmatch", 10); err != nil {
			logger.Error("Failed to start demo match", zap.Error(err))
			return
		}

		logger.Info("Started demo match", zap.String("match_id", matchID))

		// End match after 30 seconds
		time.Sleep(30 * time.Second)
		if err := statsAggregator.EndMatch(matchID); err != nil {
			logger.Error("Failed to end demo match", zap.Error(err))
		} else {
			logger.Info("Ended demo match", zap.String("match_id", matchID))
		}
	}()

	// Wait for shutdown signal
	<-shutdownChan
	logger.Info("Received shutdown signal, shutting down gracefully...")

	// Graceful shutdown with timeout
	shutdownCtx, shutdownCancel := context.WithTimeout(context.Background(), 30*time.Second)
	defer shutdownCancel()

	if err := server.Shutdown(shutdownCtx); err != nil {
		logger.Error("HTTP server shutdown error", zap.Error(err))
	}

	logger.Info("Shutdown complete")
}
