// Event Sourcing Aggregates Service - Enterprise-grade CQRS framework
// Issue: #2217
// Agent: Backend Agent
package main

import (
	"context"
	"log"
	"net/http"
	"os"
	"os/signal"
	"syscall"
	"time"

	"go.uber.org/zap"

	"necpgame/services/event-sourcing-aggregates-go/internal/aggregates"
	"necpgame/services/event-sourcing-aggregates-go/internal/commands"
	"necpgame/services/event-sourcing-aggregates-go/internal/events"
	"necpgame/services/event-sourcing-aggregates-go/internal/projections"
	"necpgame/services/event-sourcing-aggregates-go/internal/sagas"
	"necpgame/services/event-sourcing-aggregates-go/internal/store"
	"necpgame/services/event-sourcing-aggregates-go/internal/snapshots"
)

// Service represents the event sourcing service
type Service struct {
	logger          *zap.Logger
	eventStore      *store.PostgresEventStore
	snapshotStore   *snapshots.RedisSnapshotStore
	commandBus      *commands.CommandBus
	eventBus        *events.EventBus
	projectionMgr   *projections.ProjectionManager
	sagaCoordinator *sagas.Coordinator
	aggregateRepo   *aggregates.Repository
	server          *http.Server
}

// NewService creates a new event sourcing service instance
func NewService() (*Service, error) {
	logger, err := zap.NewProduction()
	if err != nil {
		return nil, err
	}

	// Initialize PostgreSQL event store
	eventStore, err := store.NewPostgresEventStore(os.Getenv("DATABASE_URL"))
	if err != nil {
		return nil, err
	}

	// Initialize Redis snapshot store (optional)
	var snapshotStore *snapshots.RedisSnapshotStore
	if redisURL := os.Getenv("REDIS_URL"); redisURL != "" {
		snapshotStore, err = snapshots.NewRedisSnapshotStore(redisURL)
		if err != nil {
			logger.Warn("Failed to initialize Redis snapshot store", zap.Error(err))
		}
	}

	// Initialize buses
	commandBus := commands.NewCommandBus()
	eventBus := events.NewEventBus()

	// Initialize projection manager
	projectionMgr := projections.NewProjectionManager(eventBus, logger)

	// Initialize saga coordinator
	sagaCoordinator := sagas.NewCoordinator(eventBus, logger)

	// Initialize aggregate repository
	aggregateRepo := aggregates.NewRepository(eventStore, snapshotStore, logger)

	// Initialize HTTP server
	mux := http.NewServeMux()
	server := &http.Server{
		Addr:         ":8080",
		Handler:      mux,
		ReadTimeout:  30 * time.Second,
		WriteTimeout: 30 * time.Second,
	}

	// Register health endpoints
	mux.HandleFunc("/health", func(w http.ResponseWriter, r *http.Request) {
		w.WriteHeader(http.StatusOK)
		w.Write([]byte(`{"status":"healthy","timestamp":"` + time.Now().UTC().Format(time.RFC3339) + `"}`))
	})

	mux.HandleFunc("/ready", func(w http.ResponseWriter, r *http.Request) {
		w.WriteHeader(http.StatusOK)
		w.Write([]byte(`{"status":"ready","timestamp":"` + time.Now().UTC().Format(time.RFC3339) + `"}`))
	})

	return &Service{
		logger:          logger,
		eventStore:      eventStore,
		snapshotStore:   snapshotStore,
		commandBus:      commandBus,
		eventBus:        eventBus,
		projectionMgr:   projectionMgr,
		sagaCoordinator: sagaCoordinator,
		aggregateRepo:   aggregateRepo,
		server:          server,
	}, nil
}

// Start begins the service operation
func (s *Service) Start(ctx context.Context) error {
	s.logger.Info("Starting Event Sourcing Aggregates Service",
		zap.String("version", "1.0.0"),
		zap.Time("started_at", time.Now().UTC()))

	// Start projection manager
	if err := s.projectionMgr.Start(ctx); err != nil {
		return err
	}

	// Start saga coordinator
	if err := s.sagaCoordinator.Start(ctx); err != nil {
		return err
	}

	// Start HTTP server in background
	go func() {
		s.logger.Info("HTTP server starting", zap.String("addr", s.server.Addr))
		if err := s.server.ListenAndServe(); err != nil && err != http.ErrServerClosed {
			s.logger.Fatal("HTTP server failed", zap.Error(err))
		}
	}()

	return nil
}

// Stop gracefully shuts down the service
func (s *Service) Stop(ctx context.Context) error {
	s.logger.Info("Stopping Event Sourcing Aggregates Service")

	// Stop HTTP server
	shutdownCtx, cancel := context.WithTimeout(ctx, 30*time.Second)
	defer cancel()

	if err := s.server.Shutdown(shutdownCtx); err != nil {
		s.logger.Error("HTTP server shutdown failed", zap.Error(err))
	}

	// Stop components
	s.sagaCoordinator.Stop()
	s.projectionMgr.Stop()

	// Close stores
	if s.snapshotStore != nil {
		s.snapshotStore.Close()
	}
	if err := s.eventStore.Close(); err != nil {
		s.logger.Error("Event store close failed", zap.Error(err))
	}

	s.logger.Info("Event Sourcing Aggregates Service stopped")
	return nil
}

func main() {
	// Create service
	service, err := NewService()
	if err != nil {
		log.Fatal("Failed to create service", err)
	}
	defer service.logger.Sync()

	// Setup signal handling for graceful shutdown
	ctx, cancel := context.WithCancel(context.Background())
	defer cancel()

	sigChan := make(chan os.Signal, 1)
	signal.Notify(sigChan, syscall.SIGINT, syscall.SIGTERM)

	// Start service
	if err := service.Start(ctx); err != nil {
		service.logger.Fatal("Failed to start service", zap.Error(err))
	}

	// Wait for shutdown signal
	<-sigChan
	service.logger.Info("Shutdown signal received")

	// Graceful shutdown
	shutdownCtx, cancel := context.WithTimeout(context.Background(), 30*time.Second)
	defer cancel()

	if err := service.Stop(shutdownCtx); err != nil {
		service.logger.Error("Service shutdown failed", zap.Error(err))
		os.Exit(1)
	}
}