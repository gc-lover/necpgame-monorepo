// Issue: #146073424
package main

import (
	"fmt"
	"log"
	"os"
	"strconv"
	"sync"
	"time"

	"go.uber.org/zap"

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

// Config содержит конфигурацию API Gateway
type Config struct {
	ServerPort              int
	JWTSecret               string
	RateLimitRPM            int
	CircuitBreakerThreshold int
	Services                map[string]*ServiceConfig
}

// ServiceConfig содержит конфигурацию для каждого микросервиса
type ServiceConfig struct {
	URL            string
	HealthCheck    string
	Timeout        time.Duration
	MaxRetries     int
	CircuitBreaker *CircuitBreaker
}

// CircuitBreaker реализует паттерн circuit breaker
type CircuitBreaker struct {
	mu           sync.RWMutex
	failures     int
	lastFailTime time.Time
	state        string // "closed", "open", "half-open"
	threshold    int
	timeout      time.Duration
}

// NewCircuitBreaker создает новый circuit breaker
func NewCircuitBreaker(threshold int, timeout time.Duration) *CircuitBreaker {
	return &CircuitBreaker{
		state:     "closed",
		threshold: threshold,
		timeout:   timeout,
	}
}

// Call выполняет вызов через circuit breaker
func (cb *CircuitBreaker) Call(fn func() error) error {
	cb.mu.Lock()
	defer cb.mu.Unlock()

	switch cb.state {
	case "open":
		if time.Since(cb.lastFailTime) > cb.timeout {
			cb.state = "half-open"
		} else {
			return fmt.Errorf("circuit breaker is open")
		}
	case "half-open":
		// Allow one call to test
	}

	err := fn()
	if err != nil {
		cb.failures++
		cb.lastFailTime = time.Now()
		if cb.failures >= cb.threshold {
			cb.state = "open"
		}
		return err
	}

	// Success - reset failures and close circuit
	cb.failures = 0
	cb.state = "closed"
	return nil
}

// loadConfig загружает конфигурацию из переменных окружения
func loadConfig() (*Config, error) {
	config := &Config{
		ServerPort:              getEnvAsInt("SERVER_PORT", 8080),
		JWTSecret:               getEnv("JWT_SECRET", "your-super-secret-jwt-key-change-in-production"),
		RateLimitRPM:            getEnvAsInt("RATE_LIMIT_RPM", 1000),
		CircuitBreakerThreshold: getEnvAsInt("CIRCUIT_BREAKER_THRESHOLD", 5),
		Services:                make(map[string]*ServiceConfig),
	}

	// Настройка сервисов
	config.Services["auth"] = &ServiceConfig{
		URL:            getEnv("AUTH_SERVICE_URL", "http://auth-service:8080"),
		HealthCheck:    "/health",
		Timeout:        10 * time.Second,
		MaxRetries:     3,
		CircuitBreaker: NewCircuitBreaker(config.CircuitBreakerThreshold, 30*time.Second),
	}

	config.Services["notification"] = &ServiceConfig{
		URL:            getEnv("NOTIFICATION_SERVICE_URL", "http://notification-service:8083"),
		HealthCheck:    "/health",
		Timeout:        5 * time.Second,
		MaxRetries:     2,
		CircuitBreaker: NewCircuitBreaker(config.CircuitBreakerThreshold, 30*time.Second),
	}

	config.Services["combat"] = &ServiceConfig{
		URL:            getEnv("COMBAT_SERVICE_URL", "http://combat-service:8082"),
		HealthCheck:    "/health",
		Timeout:        3 * time.Second,
		MaxRetries:     1,
		CircuitBreaker: NewCircuitBreaker(config.CircuitBreakerThreshold, 30*time.Second),
	}

	config.Services["romance"] = &ServiceConfig{
		URL:            getEnv("ROMANCE_SERVICE_URL", "http://romance-core-service:8084"),
		HealthCheck:    "/health",
		Timeout:        5 * time.Second,
		MaxRetries:     2,
		CircuitBreaker: NewCircuitBreaker(config.CircuitBreakerThreshold, 30*time.Second),
	}

	// Валидация конфигурации
	if config.JWTSecret == "your-super-secret-jwt-key-change-in-production" {
		return nil, fmt.Errorf("JWT_SECRET must be changed in production")
	}

	return config, nil
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
