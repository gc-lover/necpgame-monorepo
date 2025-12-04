// Issue: #44, #1584
package main

import (
	"context"
	"database/sql"
	"fmt"
	"log"
	"net/http"
	_ "net/http/pprof" // OPTIMIZATION: Issue #1584 - profiling endpoints
	"os"
	"os/signal"
	"syscall"
	"time"

	_ "github.com/lib/pq"
	"github.com/redis/go-redis/v9"
	"github.com/robfig/cron/v3"
	"github.com/segmentio/kafka-go"
	"go.uber.org/zap"

	"github.com/gc-lover/necpgame-monorepo/services/world-events-scheduler-service-go/server"
)

func main() {
	logger, err := zap.NewProduction()
	if err != nil {
		log.Fatalf("Failed to initialize logger: %v", err)
	}
	defer logger.Sync()

	// Database connection
	dbHost := getEnv("DB_HOST", "localhost")
	dbPort := getEnv("DB_PORT", "5432")
	dbUser := getEnv("DB_USER", "postgres")
	dbPassword := getEnv("DB_PASSWORD", "password")
	dbName := getEnv("DB_NAME", "necpgame")

	dsn := fmt.Sprintf("host=%s port=%s user=%s password=%s dbname=%s sslmode=disable",
		dbHost, dbPort, dbUser, dbPassword, dbName)

	db, err := sql.Open("postgres", dsn)
	if err != nil {
		logger.Fatal("Failed to connect to database", zap.Error(err))
	}
	defer db.Close()

	// Connection pool settings for performance (Issue #1605)
	db.SetMaxOpenConns(25)
	db.SetMaxIdleConns(25)
	db.SetConnMaxLifetime(5 * time.Minute)
	db.SetConnMaxIdleTime(10 * time.Minute)

	ctx, cancel := context.WithTimeout(context.Background(), 5*time.Second)
	defer cancel()

	if err := db.PingContext(ctx); err != nil {
		logger.Fatal("Failed to ping database", zap.Error(err))
	}

	// Redis connection
	redisAddr := getEnv("REDIS_ADDR", "localhost:6379")
	redisClient := redis.NewClient(&redis.Options{
		Addr: redisAddr,
	})
	defer redisClient.Close()

	// Kafka writer
	kafkaBroker := getEnv("KAFKA_BROKER", "localhost:9092")
	kafkaWriter := &kafka.Writer{
		Addr:     kafka.TCP(kafkaBroker),
		Topic:    "world.events.scheduler",
		Balancer: &kafka.LeastBytes{},
	}
	defer kafkaWriter.Close()

	// Initialize cron scheduler
	cronScheduler := cron.New()
	defer cronScheduler.Stop()

	// Initialize repository, service
	repo := server.NewRepository(db, logger)
	svc := server.NewService(repo, redisClient, kafkaWriter, cronScheduler, logger)

	// Start cron scheduler
	cronScheduler.Start()

	// Create HTTP server
	serverPort := getEnv("SERVER_PORT", "8090")
	httpServer := server.NewHTTPServer(":"+serverPort, svc, logger)

	// OPTIMIZATION: Issue #1584 - Start pprof server for profiling
	go func() {
		pprofAddr := getEnv("PPROF_ADDR", "localhost:6699")
		logger.Info("pprof server starting", zap.String("addr", pprofAddr))
		// Endpoints: /debug/pprof/profile, /debug/pprof/heap, /debug/pprof/goroutine
		if err := http.ListenAndServe(pprofAddr, nil); err != nil {
			logger.Error("pprof server failed", zap.Error(err))
		}
	}()

	// Start server
	go func() {
		logger.Info("Starting World Events Scheduler Service", zap.String("port", serverPort))
		if err := httpServer.Start(); err != nil && err != http.ErrServerClosed {
			logger.Fatal("Server failed", zap.Error(err))
		}
	}()

	// Graceful shutdown
	quit := make(chan os.Signal, 1)
	signal.Notify(quit, syscall.SIGINT, syscall.SIGTERM)
	<-quit

	logger.Info("Shutting down server...")

	shutdownCtx, shutdownCancel := context.WithTimeout(context.Background(), 10*time.Second)
	defer shutdownCancel()

	if err := httpServer.Shutdown(shutdownCtx); err != nil {
		logger.Error("Server forced to shutdown", zap.Error(err))
	}

	logger.Info("Server exited")
}

func getEnv(key, defaultValue string) string {
	if value := os.Getenv(key); value != "" {
		return value
	}
	return defaultValue
}









