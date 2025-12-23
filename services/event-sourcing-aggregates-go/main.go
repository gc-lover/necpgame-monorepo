// Issue: #2217
// PERFORMANCE: Enterprise-grade event sourcing framework with CQRS and saga patterns
// BACKEND: Event Sourcing Aggregate Implementation for NECPGAME MMOFPS RPG

package main

import (
	"context"
	"log"
	"net/http"
	"os"
	"os/signal"
	"runtime"
	"syscall"
	"time"

	"go.uber.org/zap"

	"event-sourcing-aggregates-go/internal/aggregates"
	"event-sourcing-aggregates-go/internal/commands"
	"event-sourcing-aggregates-go/internal/events"
	"event-sourcing-aggregates-go/internal/projections"
	"event-sourcing-aggregates-go/internal/sagas"
	"event-sourcing-aggregates-go/internal/snapshots"
	"event-sourcing-aggregates-go/internal/store"
)

func main() {
	// PERFORMANCE: Optimize GC for event sourcing workloads
	if os.Getenv("GOGC") == "" {
		os.Setenv("GOGC", "150") // Higher GC threshold for event processing
	}

	// Initialize structured logger
	logger, err := zap.NewProduction()
	if err != nil {
		log.Fatalf("Failed to initialize logger: %v", err)
	}
	defer logger.Sync()

	logger.Info("Starting Event Sourcing Aggregates Service",
		zap.String("version", "1.0.0"),
		zap.String("gogc", os.Getenv("GOGC")))

	// Initialize event store
	eventStore, err := store.NewPostgreSQLEventStore(logger)
	if err != nil {
		logger.Fatal("Failed to initialize event store", zap.Error(err))
	}
	defer eventStore.Close()

	// Initialize snapshot store
	snapshotStore := snapshots.NewRedisSnapshotStore(logger)

	// Initialize aggregate repository
	aggregateRepo := aggregates.NewRepository(eventStore, snapshotStore, logger)

	// Initialize command handlers
	commandBus := commands.NewBus(logger)

	// Initialize event handlers
	eventBus := events.NewBus(logger)

	// Initialize projections
	projectionManager := projections.NewManager(logger)

	// Initialize saga coordinator
	sagaCoordinator := sagas.NewCoordinator(eventStore, logger)

	// Register domain aggregates
	if err := registerAggregates(aggregateRepo, commandBus, eventBus, logger); err != nil {
		logger.Fatal("Failed to register aggregates", zap.Error(err))
	}

	// Register projections
	if err := registerProjections(projectionManager, eventStore, logger); err != nil {
		logger.Fatal("Failed to register projections", zap.Error(err))
	}

	// Register sagas
	if err := registerSagas(sagaCoordinator, eventBus, logger); err != nil {
		logger.Fatal("Failed to register sagas", zap.Error(err))
	}

	// Start projection manager
	if err := projectionManager.Start(context.Background()); err != nil {
		logger.Fatal("Failed to start projection manager", zap.Error(err))
	}
	defer projectionManager.Stop()

	// Start saga coordinator
	if err := sagaCoordinator.Start(context.Background()); err != nil {
		logger.Fatal("Failed to start saga coordinator", zap.Error(err))
	}
	defer sagaCoordinator.Stop()

	// Start HTTP server for health checks and metrics
	healthServer := startHealthServer(logger)
	defer healthServer.Shutdown(context.Background())

	// Graceful shutdown handling
	shutdownCh := make(chan os.Signal, 1)
	signal.Notify(shutdownCh, syscall.SIGINT, syscall.SIGTERM)

	logger.Info("Event Sourcing Aggregates Service started successfully")

	// Wait for shutdown signal
	<-shutdownCh
	logger.Info("Received shutdown signal, initiating graceful shutdown...")

	// Force GC before exit
	runtime.GC()
	logger.Info("Event Sourcing Aggregates Service shutdown complete")
}

// registerAggregates registers all domain aggregates
func registerAggregates(repo *aggregates.Repository, commandBus *commands.Bus, eventBus *events.Bus, logger *zap.Logger) error {
	// For now, skip aggregate registration as implementation is incomplete
	logger.Info("Aggregate registration skipped - implementation in progress")
	return nil
}

// registerProjections registers all read model projections
func registerProjections(manager *projections.Manager, eventStore *store.PostgreSQLEventStore, logger *zap.Logger) error {
	// For now, skip projection registration as implementation is incomplete
	logger.Info("Projection registration skipped - implementation in progress")
	return nil
}

// registerSagas registers distributed transaction sagas
func registerSagas(coordinator *sagas.Coordinator, eventBus *events.Bus, logger *zap.Logger) error {
	// For now, skip saga registration as implementation is incomplete
	logger.Info("Saga registration skipped - implementation in progress")
	return nil
}

// startHealthServer starts a simple health check HTTP server
func startHealthServer(logger *zap.Logger) *http.Server {
	mux := http.NewServeMux()
	mux.HandleFunc("/health", func(w http.ResponseWriter, r *http.Request) {
		w.WriteHeader(http.StatusOK)
		w.Write([]byte(`{"status":"healthy","timestamp":"` + time.Now().Format(time.RFC3339) + `"}`))
	})

	mux.HandleFunc("/ready", func(w http.ResponseWriter, r *http.Request) {
		w.WriteHeader(http.StatusOK)
		w.Write([]byte(`{"status":"ready","timestamp":"` + time.Now().Format(time.RFC3339) + `"}`))
	})

	server := &http.Server{
		Addr:         ":8082",
		Handler:      mux,
		ReadTimeout:  5 * time.Second,
		WriteTimeout: 5 * time.Second,
	}

	go func() {
		if err := server.ListenAndServe(); err != nil && err != http.ErrServerClosed {
			logger.Error("Health server failed", zap.Error(err))
		}
	}()

	return server
}
