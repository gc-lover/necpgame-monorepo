// Issue: #146073424
package main

import (
	"fmt"
	"log"
	"os"
	"strconv"
	"time"

	"go.uber.org/zap"

	"necpgame/services/api-gateway-go/server"
)

func main() {
	// Инициализируем structured logger
	logger, err := zap.NewProduction()
	if err != nil {
		log.Fatalf("Failed to initialize logger: %v", err)
	}
	defer logger.Sync()

	logger.Info("Starting API Gateway", zap.String("version", "1.0.0"))

	// Получаем конфигурацию из переменных окружения
	config, err := loadConfig()
	if err != nil {
		logger.Fatal("Failed to load configuration", zap.Error(err))
	}

	// Создаем API Gateway сервер
	gateway := server.NewAPIGateway(logger, config)

	// Запускаем сервер
	if err := gateway.Start(); err != nil {
		logger.Fatal("Gateway failed to start", zap.Error(err))
	}
}

func loadConfig() (*server.Config, error) {
	config := &server.Config{
		ServerPort:             getEnvAsInt("SERVER_PORT", 8080),
		AuthServiceURL:         getEnv("AUTH_SERVICE_URL", "http://auth-service:8081"),
		UserServiceURL:         getEnv("USER_SERVICE_URL", "http://user-service:8082"),
		CombatServiceURL:       getEnv("COMBAT_SERVICE_URL", "http://combat-service:8080"),
		NotificationServiceURL: getEnv("NOTIFICATION_SERVICE_URL", "http://notification-service:8083"),
		RomanceServiceURL:      getEnv("ROMANCE_SERVICE_URL", "http://romance-core-service:8084"),
		QuestServiceURL:        getEnv("QUEST_SERVICE_URL", "http://quest-core-service:8085"),
		InventoryServiceURL:    getEnv("INVENTORY_SERVICE_URL", "http://inventory-service:8086"),
		EconomyServiceURL:      getEnv("ECONOMY_SERVICE_URL", "http://economy-service:8087"),

		RateLimitRPM: getEnvAsInt("RATE_LIMIT_RPM", 1000),
		BurstLimit:   getEnvAsInt("BURST_LIMIT", 100),

		CircuitBreakerTimeout:     time.Duration(getEnvAsInt("CB_TIMEOUT_SEC", 5)) * time.Second,
		CircuitBreakerMaxRequests: getEnvAsInt("CB_MAX_REQUESTS", 3),
		CircuitBreakerInterval:    time.Duration(getEnvAsInt("CB_INTERVAL_SEC", 10)) * time.Second,

		ReadTimeout:  time.Duration(getEnvAsInt("READ_TIMEOUT_SEC", 15)) * time.Second,
		WriteTimeout: time.Duration(getEnvAsInt("WRITE_TIMEOUT_SEC", 15)) * time.Second,
		IdleTimeout:  time.Duration(getEnvAsInt("IDLE_TIMEOUT_SEC", 60)) * time.Second,

		JWTSecret: getEnv("JWT_SECRET", "your-super-secret-jwt-key-change-in-production"),
	}

	// Валидация конфигурации
	if config.JWTSecret == "your-super-secret-jwt-key-change-in-production" {
		return nil, fmt.Errorf("JWT_SECRET must be changed in production")
	}

	return config, nil
}

func getEnv(key, defaultValue string) string {
	if value := os.Getenv(key); value != "" {
		return value
	}
	return defaultValue
}

func getEnvAsInt(key string, defaultValue int) int {
	if value := os.Getenv(key); value != "" {
		if intValue, err := strconv.Atoi(value); err == nil {
			return intValue
		}
	}
	return defaultValue
}
