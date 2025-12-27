// Issue: #2212 - Anti-Cheat Player Behavior Analytics
// Agent: Security
// Enterprise-grade anti-cheat behavior analytics service for MMOFPS RPG

package main

import (
	"context"
	"flag"
	"net/http"
	"os"
	"os/signal"
	"syscall"
	"time"

	"github.com/go-chi/chi/v5"
	"github.com/go-chi/chi/v5/middleware"
	"github.com/go-chi/cors"
	"github.com/prometheus/client_golang/prometheus/promhttp"
	"github.com/redis/go-redis/v9"
	"github.com/segmentio/kafka-go"
	"go.uber.org/zap"
	"gopkg.in/yaml.v3"

	"necpgame/services/anti-cheat-behavior-analytics-service-go/internal/config"
	"necpgame/services/anti-cheat-behavior-analytics-service-go/internal/handlers"
	"necpgame/services/anti-cheat-behavior-analytics-service-go/internal/repository"
	"necpgame/services/anti-cheat-behavior-analytics-service-go/internal/service"
	"necpgame/services/anti-cheat-behavior-analytics-service-go/internal/analytics"
	"necpgame/services/anti-cheat-behavior-analytics-service-go/internal/detection"
)

func main() {
	// PERFORMANCE: Optimize GC for analytics workloads
	if os.Getenv("GOGC") == "" {
		os.Setenv("GOGC", "75") // Higher GC threshold for analytics
	}

	// Configuration flags
	var (
		configPath = flag.String("config", "config.yaml", "Path to configuration file")
		httpAddr   = flag.String("http", ":8080", "HTTP server address")
		logLevel   = flag.String("log-level", "info", "Log level (debug, info, warn, error)")
	)
	flag.Parse()

	// Initialize structured logging
	logger, err := initLogger(*logLevel)
	if err != nil {
		panic("Failed to initialize logger: " + err.Error())
	}
	defer logger.Sync()

	sugar := logger.Sugar()
	sugar.Info("Starting Anti-Cheat Behavior Analytics Service v1.0.0")

	// Load configuration
	cfg, err := loadConfig(*configPath)
	if err != nil {
		sugar.Fatalf("Failed to load configuration: %v", err)
	}

	// Initialize database connection
	db, err := initDatabase(cfg.Database, sugar)
	if err != nil {
		sugar.Fatalf("Failed to connect to database: %v", err)
	}
	defer db.Close()

	// Initialize Redis cache
	redisClient, err := initRedis(cfg.Redis, sugar)
	if err != nil {
		sugar.Fatalf("Failed to connect to Redis: %v", err)
	}
	defer redisClient.Close()

	// Initialize Kafka consumer
	kafkaReader, err := initKafkaConsumer(cfg.Kafka, sugar)
	if err != nil {
		sugar.Fatalf("Failed to initialize Kafka consumer: %v", err)
	}
	defer kafkaReader.Close()

	// Initialize components
	repo := repository.NewRepository(db, redisClient, sugar)
	analyticsEngine := analytics.NewAnalyticsEngine(cfg.Analytics, sugar)
	detectionEngine := detection.NewDetectionEngine(cfg.Detection, analyticsEngine, sugar)
	antiCheatService := service.NewService(repo, analyticsEngine, detectionEngine, sugar)

	// Initialize handlers
	antiCheatHandlers := handlers.NewHandlers(antiCheatService, sugar)

	// Setup HTTP router
	r := setupRouter(antiCheatHandlers, cfg.Security)

	// Create HTTP server with timeouts for security
	server := &http.Server{
		Addr:         *httpAddr,
		Handler:      r,
		ReadTimeout:  15 * time.Second,
		WriteTimeout: 15 * time.Second,
		IdleTimeout:  60 * time.Second,
	}

	// Start HTTP server in a goroutine
	go func() {
		sugar.Infof("Starting HTTP server on %s", *httpAddr)
		if err := server.ListenAndServe(); err != nil && err != http.ErrServerClosed {
			sugar.Fatalf("Failed to start HTTP server: %v", err)
		}
	}()

	// Start event processing worker
	go startEventProcessing(kafkaReader, antiCheatService, sugar)

	// Setup graceful shutdown
	shutdownChan := make(chan os.Signal, 1)
	signal.Notify(shutdownChan, os.Interrupt, syscall.SIGTERM)

	// Wait for shutdown signal
	<-shutdownChan
	sugar.Info("Received shutdown signal, shutting down gracefully...")

	// Graceful shutdown with timeout
	shutdownCtx, shutdownCancel := context.WithTimeout(context.Background(), 30*time.Second)
	defer shutdownCancel()

	if err := server.Shutdown(shutdownCtx); err != nil {
		sugar.Errorf("HTTP server shutdown error: %v", err)
	}

	sugar.Info("Shutdown complete")
}

func initLogger(level string) (*zap.Logger, error) {
	config := zap.NewProductionConfig()

	switch level {
	case "debug":
		config.Level = zap.NewAtomicLevelAt(zap.DebugLevel)
	case "info":
		config.Level = zap.NewAtomicLevelAt(zap.InfoLevel)
	case "warn":
		config.Level = zap.NewAtomicLevelAt(zap.WarnLevel)
	case "error":
		config.Level = zap.NewAtomicLevelAt(zap.ErrorLevel)
	default:
		config.Level = zap.NewAtomicLevelAt(zap.InfoLevel)
	}

	return config.Build()
}

func loadConfig(path string) (*config.Config, error) {
	file, err := os.Open(path)
	if err != nil {
		return nil, err
	}
	defer file.Close()

	var cfg config.Config
	decoder := yaml.NewDecoder(file)
	if err := decoder.Decode(&cfg); err != nil {
		return nil, err
	}

	return &cfg, nil
}

func initDatabase(cfg config.DatabaseConfig, logger *zap.SugaredLogger) (*sql.DB, error) {
	// Initialize PostgreSQL connection with connection pooling
	db, err := sql.Open("pgx", cfg.ConnectionString)
	if err != nil {
		return nil, err
	}

	// Configure connection pool
	db.SetMaxOpenConns(cfg.MaxOpenConns)
	db.SetMaxIdleConns(cfg.MaxIdleConns)
	db.SetConnMaxLifetime(cfg.ConnMaxLifetime)

	// Test connection
	if err := db.Ping(); err != nil {
		return nil, err
	}

	logger.Info("Connected to PostgreSQL database")
	return db, nil
}

func initRedis(cfg config.RedisConfig, logger *zap.SugaredLogger) (*redis.Client, error) {
	client := redis.NewClient(&redis.Options{
		Addr:         cfg.Addr,
		Password:     cfg.Password,
		DB:           cfg.DB,
		PoolSize:     cfg.PoolSize,
		MinIdleConns: cfg.MinIdleConns,
	})

	// Test connection
	if err := client.Ping(context.Background()).Err(); err != nil {
		return nil, err
	}

	logger.Info("Connected to Redis")
	return client, nil
}

func initKafkaConsumer(cfg config.KafkaConfig, logger *zap.SugaredLogger) (*kafka.Reader, error) {
	reader := kafka.NewReader(kafka.ReaderConfig{
		Brokers:       cfg.Brokers,
		Topic:         cfg.Topic,
		GroupID:       cfg.GroupID,
		StartOffset:   kafka.LastOffset,
		MinBytes:      10e3, // 10KB
		MaxBytes:      10e6, // 10MB
		CommitInterval: time.Second,
	})

	logger.Info("Initialized Kafka consumer")
	return reader, nil
}

func setupRouter(handlers *handlers.Handlers, securityCfg config.SecurityConfig) *chi.Mux {
	r := chi.NewRouter()

	// Middleware
	r.Use(middleware.RequestID)
	r.Use(middleware.RealIP)
	r.Use(middleware.Logger)
	r.Use(middleware.Recoverer)
	r.Use(middleware.Timeout(30 * time.Second))

	// CORS
	r.Use(cors.Handler(cors.Options{
		AllowedOrigins:   securityCfg.AllowedOrigins,
		AllowedMethods:   []string{"GET", "POST", "PUT", "DELETE", "OPTIONS"},
		AllowedHeaders:   []string{"Accept", "Authorization", "Content-Type", "X-CSRF-Token"},
		ExposedHeaders:   []string{"Link"},
		AllowCredentials: true,
		MaxAge:           300, // Maximum value not ignored by any of major browsers
	}))

	// JWT Authentication middleware
	r.Use(handlers.AuthMiddleware)

	// API routes
	r.Route("/api/v1/anticheat", func(r chi.Router) {
		r.Get("/health", handlers.HealthCheck)
		r.Get("/metrics", handlers.GetMetrics)

		// Player behavior analysis
		r.Get("/players/{playerId}/behavior", handlers.GetPlayerBehavior)
		r.Get("/players/{playerId}/risk-score", handlers.GetPlayerRiskScore)
		r.Post("/players/{playerId}/flag", handlers.FlagPlayer)

		// Match analysis
		r.Get("/matches/{matchId}/analysis", handlers.GetMatchAnalysis)
		r.Get("/matches/{matchId}/anomalies", handlers.GetMatchAnomalies)

		// Statistics and reporting
		r.Get("/statistics/summary", handlers.GetStatisticsSummary)
		r.Get("/statistics/trends", handlers.GetStatisticsTrends)
		r.Get("/statistics/top-risky", handlers.GetTopRiskyPlayers)

		// Detection rules management
		r.Get("/rules", handlers.GetDetectionRules)
		r.Post("/rules", handlers.CreateDetectionRule)
		r.Put("/rules/{ruleId}", handlers.UpdateDetectionRule)
		r.Delete("/rules/{ruleId}", handlers.DeleteDetectionRule)

		// Alert management
		r.Get("/alerts", handlers.GetAlerts)
		r.Put("/alerts/{alertId}/acknowledge", handlers.AcknowledgeAlert)
		r.Get("/alerts/{alertId}/details", handlers.GetAlertDetails)
	})

	// Prometheus metrics
	r.Handle("/metrics", promhttp.Handler())

	return r
}

func startEventProcessing(reader *kafka.Reader, svc *service.Service, logger *zap.SugaredLogger) {
	logger.Info("Starting event processing worker")

	for {
		msg, err := reader.ReadMessage(context.Background())
		if err != nil {
			logger.Errorf("Error reading Kafka message: %v", err)
			continue
		}

		// Process event asynchronously
		go func(message kafka.Message) {
			if err := svc.ProcessEvent(message); err != nil {
				logger.Errorf("Error processing event: %v", err)
			}
		}(msg)

		// Commit message
		if err := reader.CommitMessages(context.Background(), msg); err != nil {
			logger.Errorf("Error committing message: %v", err)
		}
	}
}
