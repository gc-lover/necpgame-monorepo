//go:align 64
// Issue: #2293

package main

import (
	"context"
	"database/sql"
	"fmt"
	"log"
	"net/http"
	"os"
	"os/signal"
	"sync"
	"syscall"
	"time"

	"github.com/redis/go-redis/v9"
	"github.com/gorilla/websocket"
	"github.com/prometheus/client_golang/prometheus/promhttp"
	_ "github.com/lib/pq"
	"github.com/ogen-go/ogen/middleware"

	"combat-system-service-go/pkg/api"
	"combat-system-service-go/server"
)

func main() {
	// Initialize configuration
	config := server.NewConfig()

	// Override config from environment variables
	if host := os.Getenv("DB_HOST"); host != "" {
		config.DBHost = host
	}
	if port := os.Getenv("DB_PORT"); port != "" {
		fmt.Sscanf(port, "%d", &config.DBPort)
	}
	if db := os.Getenv("DB_NAME"); db != "" {
		config.DBName = db
	}
	if user := os.Getenv("DB_USER"); user != "" {
		config.DBUser = user
	}
	if pass := os.Getenv("DB_PASSWORD"); pass != "" {
		config.DBPassword = pass
	}
	if pool := os.Getenv("DB_POOL_SIZE"); pool != "" {
		fmt.Sscanf(pool, "%d", &config.DBPoolSize)
	}

	// Initialize database connection
	db, err := initDatabase(config)
	if err != nil {
		log.Fatalf("Failed to initialize database: %v", err)
	}
	defer db.Close()

	// Initialize Redis connection
	redisClient, err := initRedis(config)
	if err != nil {
		log.Fatalf("Failed to initialize Redis: %v", err)
	}
	defer redisClient.Close()

	// Initialize object pools
	damagePool := &sync.Pool{
		New: func() interface{} {
			return &server.DamageCalculation{}
		},
	}
	abilityPool := &sync.Pool{
		New: func() interface{} {
			return &api.AbilityConfiguration{}
		},
	}
	balancePool := &sync.Pool{
		New: func() interface{} {
			return &api.CombatBalanceConfig{}
		},
	}

	// Initialize combat handler
	handler := server.NewCombatHandler(config, damagePool, abilityPool, balancePool)

	// Initialize ogen server
	srv, err := api.NewServer(
		handler,
		api.WithMiddleware(middleware.Logger(log.Default())),
		api.WithMiddleware(performanceMiddleware),
	)
	if err != nil {
		log.Fatalf("Failed to create server: %v", err)
	}

	// Create HTTP server with timeouts
	httpSrv := &http.Server{
		Addr:         fmt.Sprintf("%s:%d", config.ServerHost, config.ServerPort),
		Handler:      srv,
		ReadTimeout:  config.RequestTimeout,
		WriteTimeout: config.RequestTimeout,
		IdleTimeout:  60 * time.Second,
	}

	// Add Prometheus metrics endpoint
	http.Handle("/metrics", promhttp.Handler())

	// Start server in goroutine
	go func() {
		log.Printf("Starting combat system service on %s:%d", config.ServerHost, config.ServerPort)
		if err := httpSrv.ListenAndServe(); err != nil && err != http.ErrServerClosed {
			log.Fatalf("Server failed to start: %v", err)
		}
	}()

	// Wait for interrupt signal to gracefully shutdown the server
	quit := make(chan os.Signal, 1)
	signal.Notify(quit, syscall.SIGINT, syscall.SIGTERM)
	<-quit
	log.Println("Shutting down server...")

	// Give outstanding requests 30 seconds to complete
	ctx, cancel := context.WithTimeout(context.Background(), 30*time.Second)
	defer cancel()

	if err := httpSrv.Shutdown(ctx); err != nil {
		log.Printf("Server forced to shutdown: %v", err)
	}

	log.Println("Server exited")
}

// initDatabase initializes PostgreSQL connection
func initDatabase(config *server.Config) (*sql.DB, error) {
	dsn := fmt.Sprintf("host=%s port=%d user=%s password=%s dbname=%s sslmode=disable pool_max_conns=%d",
		config.DBHost, config.DBPort, config.DBUser, config.DBPassword, config.DBName, config.DBPoolSize)

	db, err := sql.Open("postgres", dsn)
	if err != nil {
		return nil, err
	}

	// Configure connection pool
	db.SetMaxOpenConns(config.DBPoolSize)
	db.SetMaxIdleConns(config.DBPoolSize / 2)
	db.SetConnMaxLifetime(5 * time.Minute)

	// Test connection
	if err := db.Ping(); err != nil {
		return nil, err
	}

	return db, nil
}

// initRedis initializes Redis connection
func initRedis(config *server.Config) (*redis.Client, error) {
	client := redis.NewClient(&redis.Options{
		Addr:     fmt.Sprintf("%s:%d", config.RedisHost, config.RedisPort),
		Password: config.RedisPassword,
		DB:       config.RedisDB,
	})

	// Test connection
	if err := client.Ping(context.Background()).Err(); err != nil {
		return nil, err
	}

	return client, nil
}

// performanceMiddleware adds performance monitoring
func performanceMiddleware(req middleware.Request, next middleware.Next) (middleware.Response, error) {
	start := time.Now()

	resp, err := next(req)

	duration := time.Since(start)
	if duration > 50*time.Millisecond {
		log.Printf("SLOW REQUEST: %s %s took %v", req.Method, req.URL.Path, duration)
	}

	return resp, err
}