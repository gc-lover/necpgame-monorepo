// Issue: #136
package main

import (
	"context"
	"database/sql"
	"fmt"
	"log"
	"os"
	"strconv"
	"time"

	"github.com/redis/go-redis/v9"
	"go.uber.org/zap"

	"github.com/NECPGAME/auth-service-go/server"

	_ "github.com/jackc/pgx/v5/stdlib"
)

// main является точкой входа для auth-service
func main() {
	// Инициализируем structured logger
	logger, err := zap.NewProduction()
	if err != nil {
		log.Fatalf("Failed to initialize logger: %v", err)
	}
	defer logger.Sync()

	logger.Info("Starting auth-service", zap.String("version", "1.0.0"))

	// Получаем конфигурацию из переменных окружения
	config, err := loadConfig()
	if err != nil {
		logger.Fatal("Failed to load configuration", zap.Error(err))
	}

	// Подключаемся к PostgreSQL
	db, err := connectDatabase(config.DatabaseURL, logger)
	if err != nil {
		logger.Fatal("Failed to connect to database", zap.Error(err))
	}
	defer db.Close()

	// Подключаемся к Redis для сессий
	redisClient := connectRedis(config.RedisURL, logger)
	defer redisClient.Close()

	// Создаем HTTP сервер
	serverConfig := &server.Config{
		JWTSecret:  config.JWTSecret,
		ServerPort: config.ServerPort,
		OAuthConfig: server.OAuthConfig{
			GoogleClientID:      config.OAuthConfig.GoogleClientID,
			GoogleClientSecret:  config.OAuthConfig.GoogleClientSecret,
			GitHubClientID:      config.OAuthConfig.GitHubClientID,
			GitHubClientSecret:  config.OAuthConfig.GitHubClientSecret,
			DiscordClientID:     config.OAuthConfig.DiscordClientID,
			DiscordClientSecret: config.OAuthConfig.DiscordClientSecret,
		},
	}
	httpServer := server.NewHTTPServer(logger, db, redisClient, serverConfig)

	// Issue: #136 - Initialize goroutine monitor for leak detection
	goroutineMonitor := server.NewGoroutineMonitor(500, logger) // Max 500 goroutines for MMOFPS auth service
	ctx, cancel := context.WithCancel(context.Background())
	defer cancel()
	goroutineMonitor.Start(ctx)
	defer goroutineMonitor.Stop()

	// Запускаем сервер
	if err := httpServer.Start(); err != nil {
		logger.Fatal("Server failed to start", zap.Error(err))
	}
}

// Config содержит конфигурацию сервиса
type Config struct {
	DatabaseURL string
	RedisURL    string
	JWTSecret   string
	ServerPort  int
	OAuthConfig OAuthConfig
}

// OAuthConfig содержит конфигурацию OAuth провайдеров
type OAuthConfig struct {
	GoogleClientID      string
	GoogleClientSecret  string
	GitHubClientID      string
	GitHubClientSecret  string
	DiscordClientID     string
	DiscordClientSecret string
}

// loadConfig загружает конфигурацию из переменных окружения
func loadConfig() (*Config, error) {
	config := &Config{
		DatabaseURL: getEnv("DATABASE_URL", "postgres://user:password@localhost:5432/auth_db?sslmode=disable"),
		RedisURL:    getEnv("REDIS_URL", "redis://localhost:6379"),
		JWTSecret:   getEnv("JWT_SECRET", "your-super-secret-jwt-key-change-in-production"),
		ServerPort:  getEnvAsInt("SERVER_PORT", 8081),
		OAuthConfig: OAuthConfig{
			GoogleClientID:      getEnv("GOOGLE_CLIENT_ID", ""),
			GoogleClientSecret:  getEnv("GOOGLE_CLIENT_SECRET", ""),
			GitHubClientID:      getEnv("GITHUB_CLIENT_ID", ""),
			GitHubClientSecret:  getEnv("GITHUB_CLIENT_SECRET", ""),
			DiscordClientID:     getEnv("DISCORD_CLIENT_ID", ""),
			DiscordClientSecret: getEnv("DISCORD_CLIENT_SECRET", ""),
		},
	}

	// Валидация конфигурации
	if config.JWTSecret == "your-super-secret-jwt-key-change-in-production" {
		return nil, fmt.Errorf("JWT_SECRET must be changed in production")
	}

	if config.DatabaseURL == "" {
		return nil, fmt.Errorf("DATABASE_URL is required")
	}

	return config, nil
}

// connectDatabase устанавливает соединение с PostgreSQL
func connectDatabase(databaseURL string, logger *zap.Logger) (*sql.DB, error) {
	logger.Info("Connecting to database")

	db, err := sql.Open("pgx", databaseURL)
	if err != nil {
		return nil, fmt.Errorf("failed to open database connection: %w", err)
	}

	// Настраиваем connection pool для MMOFPS оптимизаций
	db.SetMaxOpenConns(25)                 // Максимум 25 соединений для высокой нагрузки
	db.SetMaxIdleConns(10)                 // 10 idle соединений
	db.SetConnMaxLifetime(5 * time.Minute) // Пересоздаем соединения каждые 5 минут

	// Проверяем соединение с таймаутом
	ctx, cancel := context.WithTimeout(context.Background(), 10*time.Second)
	defer cancel()

	if err := db.PingContext(ctx); err != nil {
		db.Close()
		return nil, fmt.Errorf("failed to ping database: %w", err)
	}

	logger.Info("Database connection established")
	return db, nil
}

// connectRedis устанавливает соединение с Redis
func connectRedis(redisURL string, logger *zap.Logger) *redis.Client {
	logger.Info("Connecting to Redis")

	opt, err := redis.ParseURL(redisURL)
	if err != nil {
		logger.Fatal("Failed to parse Redis URL", zap.Error(err))
	}

	client := redis.NewClient(opt)

	// Проверяем соединение с таймаутом
	ctx, cancel := context.WithTimeout(context.Background(), 5*time.Second)
	defer cancel()

	if err := client.Ping(ctx).Err(); err != nil {
		logger.Fatal("Failed to connect to Redis", zap.Error(err))
	}

	logger.Info("Redis connection established")
	return client
}

// getEnv получает переменную окружения с дефолтным значением
func getEnv(key, defaultValue string) string {
	if value := os.Getenv(key); value != "" {
		return value
	}
	return defaultValue
}

// getEnvAsInt получает переменную окружения как int
func getEnvAsInt(key string, defaultValue int) int {
	if value := os.Getenv(key); value != "" {
		if intValue, err := strconv.Atoi(value); err == nil {
			return intValue
		}
	}
	return defaultValue
}
