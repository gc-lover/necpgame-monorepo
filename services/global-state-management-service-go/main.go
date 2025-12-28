package main

import (
	"context"
	"log"
	"net/http"
	"os"
	"os/signal"
	"syscall"
	"time"

	"github.com/gin-gonic/gin"
	"github.com/jackc/pgx/v5/pgxpool"
	"github.com/gc-lover/necpgame/services/global-state-management-service-go/internal"
	"github.com/prometheus/client_golang/prometheus/promhttp"
	"github.com/redis/go-redis/v9"
	"github.com/segmentio/kafka-go"
	"go.uber.org/zap"
)

func main() {
	// Initialize structured logging
	logger, err := zap.NewProduction()
	if err != nil {
		log.Fatal("Failed to initialize logger:", err)
	}
	defer logger.Sync()

	// Initialize database connections
	pgPool, err := pgxpool.New(context.Background(), os.Getenv("DATABASE_URL"))
	if err != nil {
		logger.Fatal("Failed to connect to PostgreSQL", zap.Error(err))
	}
	defer pgPool.Close()

	// Initialize Redis cluster client
	redisClient := redis.NewClusterClient(&redis.ClusterOptions{
		Addrs:    []string{os.Getenv("REDIS_ADDR")},
		Password: os.Getenv("REDIS_PASSWORD"),
	})
	defer redisClient.Close()

	// Initialize Kafka writer
	kafkaWriter := &kafka.Writer{
		Addr:     kafka.TCP(os.Getenv("KAFKA_BROKERS")),
		Topic:    "global.state.events",
		Balancer: &kafka.LeastBytes{},
	}
	defer kafkaWriter.Close()

	// Create global state manager
	gsm, err := internal.NewGlobalStateManager(logger, redisClient, pgPool, kafkaWriter)
	if err != nil {
		logger.Fatal("Failed to create global state manager", zap.Error(err))
	}
	defer func() {
		if err := gsm.Close(); err != nil {
			logger.Error("Failed to close global state manager", zap.Error(err))
		}
	}()

	// Setup HTTP server
	router := gin.New()
	router.Use(gin.Recovery())

	// Health check endpoint
	router.GET("/health", func(c *gin.Context) {
		c.JSON(http.StatusOK, gin.H{"status": "healthy"})
	})

	// Readiness check endpoint
	router.GET("/ready", func(c *gin.Context) {
		c.JSON(http.StatusOK, gin.H{"status": "ready"})
	})

	// Metrics endpoint
	router.GET("/metrics", gin.WrapH(promhttp.Handler()))

	// API routes
	v1 := router.Group("/api/v1")
	{
		state := v1.Group("/state")
		{
			state.GET("/player/:playerId", gsm.GetPlayerState)
			state.PUT("/player/:playerId", gsm.UpdatePlayerState)
			state.POST("/player/:playerId/sync", gsm.SyncPlayerState)

			state.GET("/match/:matchId", gsm.GetMatchState)
			state.PUT("/match/:matchId", gsm.UpdateMatchState)

			state.GET("/global", gsm.GetGlobalState)
			state.POST("/global/sync", gsm.SyncGlobalState)
		}
	}

	// Start server
	srv := &http.Server{
		Addr:    ":8080",
		Handler: router,
	}

	// Graceful shutdown
	go func() {
		if err := srv.ListenAndServe(); err != nil && err != http.ErrServerClosed {
			logger.Fatal("Failed to start server", zap.Error(err))
		}
	}()

	logger.Info("Global State Management Service started", zap.String("addr", ":8080"))

	// Wait for interrupt signal to gracefully shutdown
	quit := make(chan os.Signal, 1)
	signal.Notify(quit, syscall.SIGINT, syscall.SIGTERM)
	<-quit

	logger.Info("Shutting down server...")

	// Give outstanding requests 30 seconds to complete
	ctx, cancel := context.WithTimeout(context.Background(), 30*time.Second)
	defer cancel()

	if err := srv.Shutdown(ctx); err != nil {
		logger.Fatal("Server forced to shutdown", zap.Error(err))
	}

	// Close resources
	if err := gsm.Close(); err != nil {
		logger.Error("Failed to close global state manager", zap.Error(err))
	}

	logger.Info("Server exited")
}
