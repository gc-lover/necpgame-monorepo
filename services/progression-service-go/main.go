// Issue: #79
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

	"github.com/NECPGAME/progression-service-go/server"
	"github.com/redis/go-redis/v9"
	"github.com/segmentio/kafka-go"

	_ "github.com/jackc/pgx/v5/stdlib"
)

// main является точкой входа для progression-service
func main() {
	// Инициализируем structured logger
	logger, err := zap.NewProduction()
	if err != nil {
		log.Fatalf("Failed to initialize logger: %v", err)
	}
	defer logger.Sync()

	logger.Info("Starting progression-service", zap.String("version", "1.0.0"))

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

	// Подключаемся к Redis для кэширования
	redisClient := connectRedis(config.RedisURL, logger)
	defer redisClient.Close()

	// Подключаемся к Kafka для событий
	kafkaWriter := connectKafka(config.KafkaURL, logger)
	defer kafkaWriter.Close()

	// Создаем HTTP сервер
	httpServer := server.NewHTTPServer(logger, db, redisClient, kafkaWriter, config)

	// Запускаем сервер
	if err := httpServer.Start(); err != nil {
		logger.Fatal("Server failed to start", zap.Error(err))
	}
}

// Config содержит конфигурацию сервиса
type Config struct {
	DatabaseURL string
	RedisURL    string
	KafkaURL    string
	ServerPort  int
}

// loadConfig загружает конфигурацию из переменных окружения
func loadConfig() (*Config, error) {
	config := &Config{
		DatabaseURL: getEnv("DATABASE_URL", "postgres://user:password@localhost:5432/progression_db?sslmode=disable"),
		RedisURL:    getEnv("REDIS_URL", "redis://localhost:6379"),
		KafkaURL:    getEnv("KAFKA_URL", "localhost:9092"),
		ServerPort:  getEnvAsInt("SERVER_PORT", 8083),
	}

	// Валидация конфигурации
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

// connectKafka устанавливает соединение с Kafka
func connectKafka(kafkaURL string, logger *zap.Logger) *kafka.Writer {
	logger.Info("Connecting to Kafka", zap.String("brokers", kafkaURL))

	writer := &kafka.Writer{
		Addr:     kafka.TCP(kafkaURL),
		Topic:    "progression.lifecycle",
		Balancer: &kafka.LeastBytes{},
	}

	logger.Info("Kafka connection established")
	return writer
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





