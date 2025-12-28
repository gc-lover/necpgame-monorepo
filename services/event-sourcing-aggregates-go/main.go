// Issue: #2217
package main

import (
	"context"
	"fmt"
	"log"
	"net/http"
	"os"
	"os/signal"
	"runtime"
	"syscall"
	"time"

	"github.com/go-chi/chi/v5"
	"github.com/go-chi/chi/v5/middleware"
	"github.com/go-chi/cors"
	"github.com/prometheus/client_golang/prometheus/promhttp"
	"go.uber.org/zap"

	"event-sourcing-aggregates-go/internal/config"
	"event-sourcing-aggregates-go/internal/handlers"
	"event-sourcing-aggregates-go/internal/service"
	"event-sourcing-aggregates-go/internal/repository"
	"event-sourcing-aggregates-go/internal/metrics"
)

func main() {
	// Optimize GC for event processing workloads
	if os.Getenv("GOGC") == "" {
		os.Setenv("GOGC", "100") // Higher threshold for event sourcing
	}

	// Initialize structured logger
	logger, err := zap.NewProduction()
	if err != nil {
		log.Fatalf("Failed to initialize logger: %v", err)
	}
	defer logger.Sync()

	sugar := logger.Sugar()

	// Load configuration
	cfg, err := config.Load()
	if err != nil {
		sugar.Fatalf("Failed to load configuration: %v", err)
	}

	// Initialize database connection
	db, err := repository.NewConnection(cfg.DatabaseURL)
	if err != nil {
		sugar.Fatalf("Failed to connect to database: %v", err)
	}
	defer db.Close()

	// Initialize Redis connection
	redisClient, err := repository.NewRedisClient(cfg.RedisURL)
	if err != nil {
		sugar.Fatalf("Failed to connect to Redis: %v", err)
	}
	defer redisClient.Close()

	// Initialize Kafka producer/consumer
	kafkaProducer, err := repository.NewKafkaProducer(cfg.KafkaBrokers)
	if err != nil {
		sugar.Fatalf("Failed to create Kafka producer: %v", err)
	}
	defer func() {
		if closer, ok := kafkaProducer.(interface{ Close() error }); ok {
			closer.Close()
		}
	}()

	kafkaConsumer, err := repository.NewKafkaConsumer(cfg.KafkaBrokers, cfg.ConsumerGroup)
	if err != nil {
		sugar.Fatalf("Failed to create Kafka consumer: %v", err)
	}
	defer func() {
		if closer, ok := kafkaConsumer.(interface{ Close() error }); ok {
			closer.Close()
		}
	}()

	// Initialize metrics
	metricsCollector := metrics.NewCollector()

	// Initialize repository layer
	repo := repository.NewEventSourcingRepository(db, redisClient, kafkaConsumer, sugar)

	// Initialize service layer
	eventService := service.NewEventSourcingService(repo, metricsCollector, sugar)

	// Initialize handlers
	eventHandlers := handlers.NewEventSourcingHandlers(eventService, sugar)

	// Setup HTTP server
	r := setupRouter(eventHandlers, metricsCollector, sugar)

	srv := &http.Server{
		Addr:         fmt.Sprintf(":%d", cfg.Port),
		Handler:      r,
		ReadTimeout:  15 * time.Second,
		WriteTimeout: 15 * time.Second,
		IdleTimeout:  60 * time.Second,
	}

	// Start event processing in background
	go eventService.StartEventProcessing(context.Background())

	// Start server in goroutine
	go func() {
		sugar.Infof("Starting Event Sourcing Aggregates Service on port %d (GOGC=%s)", cfg.Port, os.Getenv("GOGC"))
		if err := srv.ListenAndServe(); err != nil && err != http.ErrServerClosed {
			sugar.Fatalf("Server failed to start: %v", err)
		}
	}()

	// Setup graceful shutdown
	quit := make(chan os.Signal, 1)
	signal.Notify(quit, syscall.SIGINT, syscall.SIGTERM)
	<-quit

	sugar.Info("Shutting down server...")

	ctx, cancel := context.WithTimeout(context.Background(), 30*time.Second)
	defer cancel()

	if err := srv.Shutdown(ctx); err != nil {
		sugar.Errorf("Server forced to shutdown: %v", err)
	}

	runtime.GC()
	sugar.Info("Server exited gracefully")
}

func setupRouter(handlers *handlers.EventSourcingHandlers, metrics *metrics.Collector, logger *zap.SugaredLogger) *chi.Mux {
	r := chi.NewRouter()

	// Middleware
	r.Use(middleware.RequestID)
	r.Use(middleware.RealIP)
	r.Use(middleware.Logger)
	r.Use(middleware.Recoverer)
	r.Use(middleware.Timeout(30 * time.Second))

	// CORS
	r.Use(cors.Handler(cors.Options{
		AllowedOrigins:   []string{"*"},
		AllowedMethods:   []string{"GET", "POST", "PUT", "DELETE", "OPTIONS"},
		AllowedHeaders:   []string{"Accept", "Authorization", "Content-Type", "X-CSRF-Token"},
		ExposedHeaders:   []string{"Link"},
		AllowCredentials: false,
		MaxAge:           300,
	}))

	// Health and metrics
	r.Get("/health", handlers.Health)
	r.Get("/ready", handlers.Ready)
	r.Handle("/metrics", promhttp.Handler())

	// API routes
	r.Route("/api/v1/events", func(r chi.Router) {
		r.Use(handlers.AuthMiddleware)

		// Event store operations
		r.Get("/stream/{aggregateId}", handlers.GetEventStream)
		r.Get("/aggregates/{aggregateType}", handlers.GetAggregatesByType)
		r.Get("/aggregates/{aggregateType}/{aggregateId}", handlers.GetAggregate)
		r.Post("/events", handlers.AppendEvent)
		r.Get("/events", handlers.GetEvents)

		// Aggregate operations
		r.Post("/aggregates/{aggregateType}/{aggregateId}/rebuild", handlers.RebuildAggregate)
		r.Get("/aggregates/{aggregateType}/{aggregateId}/snapshot", handlers.GetAggregateSnapshot)
		r.Post("/aggregates/{aggregateType}/{aggregateId}/snapshot", handlers.CreateAggregateSnapshot)

		// Event processing
		r.Get("/processing/status", handlers.GetProcessingStatus)
		r.Post("/processing/retry/{eventId}", handlers.RetryEventProcessing)
		r.Get("/projections/{projectionName}", handlers.GetProjection)

		// CQRS read models
		r.Get("/read-models/{modelName}", handlers.GetReadModel)
		r.Get("/read-models/{modelName}/{id}", handlers.GetReadModelById)

		// Event sourcing analytics
		r.Get("/analytics/events-per-day", handlers.GetEventsPerDayAnalytics)
		r.Get("/analytics/processing-latency", handlers.GetProcessingLatencyAnalytics)
		r.Get("/analytics/aggregate-sizes", handlers.GetAggregateSizesAnalytics)
	})

	return r
}