// Issue: #140875766
package main

import (
	"context"
	"database/sql"
	"fmt"
	"log"
	"os"
	"strconv"
	"time"

	"go.uber.org/zap"

	"necpgame/services/sandevistan-service-go/server"

	_ "github.com/jackc/pgx/v5/stdlib"
)

// main является точкой входа для sandevistan-service
func main() {
	// Инициализируем structured logger
	logger, err := zap.NewProduction()
	if err != nil {
		log.Fatalf("Failed to initialize logger: %v", err)
	}
	defer logger.Sync()

	logger.Info("Starting sandevistan-service", zap.String("version", "1.0.0"))

	// Получаем конфигурацию из переменных окружения
	config, err := loadConfig()
	if err != nil {
		logger.Fatal("Failed to load configuration", zap.Error(err))
	}

	// Подключаемся к БД
	db, err := connectDatabase(config.DatabaseURL, logger)
	if err != nil {
		logger.Fatal("Failed to connect to database", zap.Error(err))
	}
	defer db.Close()

	// Создаем HTTP сервер
	server := server.NewSandevistanServer(logger, db, config.JWTSecret)

	// Запускаем сервер
	if err := server.Start(); err != nil {
		logger.Fatal("Server failed to start", zap.Error(err))
	}
}

// Config содержит конфигурацию сервиса
type Config struct {
	DatabaseURL string
	JWTSecret   string
	ServerPort  int
}

// loadConfig загружает конфигурацию из переменных окружения
func loadConfig() (*Config, error) {
	config := &Config{
		DatabaseURL: getEnv("DATABASE_URL", "postgres://necpgame:necpgame@localhost:5432/necpgame?sslmode=disable"),
		JWTSecret:   getEnv("JWT_SECRET", "your-super-secret-jwt-key-change-in-production"),
		ServerPort:  getEnvAsInt("SERVER_PORT", 8084),
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
