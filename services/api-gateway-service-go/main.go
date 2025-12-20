// Issue: #146073424
package main

import (
	"fmt"
	"log"
	"os"
	"strconv"
	"time"

	"go.uber.org/zap"

	"necpgame/services/api-gateway-service-go/config"
	"necpgame/services/api-gateway-service-go/server"
)

// main является точкой входа для api-gateway-service
func main() {
	// Инициализируем structured logger
	logger, err := zap.NewProduction()
	if err != nil {
		log.Fatalf("Failed to initialize logger: %v", err)
	}
	defer logger.Sync()

	logger.Info("Starting api-gateway-service", zap.String("version", "1.0.0"))

	// Получаем конфигурацию из переменных окружения
	config, err := loadConfig()
	if err != nil {
		logger.Fatal("Failed to load configuration", zap.Error(err))
	}

	// Создаем HTTP сервер
	srv := server.NewAPIGatewayServer(logger, config)

	// Запускаем сервер
	if err := srv.Start(); err != nil {
		logger.Fatal("Server failed to start", zap.Error(err))
	}
}

// loadConfig загружает конфигурацию из переменных окружения
func loadConfig() (*config.Config, error) {
	cfg := &config.Config{
		ServerPort:              getEnvAsInt("SERVER_PORT", 8080),
		JWTSecret:               getEnv("JWT_SECRET", "your-super-secret-jwt-key-change-in-production"),
		RateLimitRPM:            getEnvAsInt("RATE_LIMIT_RPM", 1000),
		CircuitBreakerThreshold: getEnvAsInt("CIRCUIT_BREAKER_THRESHOLD", 5),
		Services:                make(map[string]*config.ServiceConfig),
	}

	// Настройка сервисов
	cfg.Services["auth"] = &config.ServiceConfig{
		URL:            getEnv("AUTH_SERVICE_URL", "http://auth-service:8080"),
		HealthCheck:    "/health",
		Timeout:        10 * time.Second,
		MaxRetries:     3,
		CircuitBreaker: config.NewCircuitBreaker(cfg.CircuitBreakerThreshold, 30*time.Second),
	}

	cfg.Services["notification"] = &config.ServiceConfig{
		URL:            getEnv("NOTIFICATION_SERVICE_URL", "http://notification-service:8083"),
		HealthCheck:    "/health",
		Timeout:        5 * time.Second,
		MaxRetries:     2,
		CircuitBreaker: config.NewCircuitBreaker(cfg.CircuitBreakerThreshold, 30*time.Second),
	}

	cfg.Services["combat"] = &config.ServiceConfig{
		URL:            getEnv("COMBAT_SERVICE_URL", "http://combat-service:8082"),
		HealthCheck:    "/health",
		Timeout:        3 * time.Second,
		MaxRetries:     1,
		CircuitBreaker: config.NewCircuitBreaker(cfg.CircuitBreakerThreshold, 30*time.Second),
	}

	cfg.Services["romance"] = &config.ServiceConfig{
		URL:            getEnv("ROMANCE_SERVICE_URL", "http://romance-core-service:8084"),
		HealthCheck:    "/health",
		Timeout:        5 * time.Second,
		MaxRetries:     2,
		CircuitBreaker: config.NewCircuitBreaker(cfg.CircuitBreakerThreshold, 30*time.Second),
	}

	// Валидация конфигурации (только в production)
	if cfg.JWTSecret == "your-super-secret-jwt-key-change-in-production" && os.Getenv("ENV") == "production" {
		return nil, fmt.Errorf("JWT_SECRET must be changed in production")
	}

	return cfg, nil
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
