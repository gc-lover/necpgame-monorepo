// Issue: Implement integration-domain-service-go
// Configuration management for integration domain service
// Enterprise-grade configuration with environment-based settings

package config

import (
	"time"

	"github.com/kelseyhightower/envconfig"
)

// Config holds all configuration for the integration domain service
type Config struct {
	// Server configuration
	Port         string        `envconfig:"PORT" default:"8080"`
	ReadTimeout  time.Duration `envconfig:"READ_TIMEOUT" default:"10s"`
	WriteTimeout time.Duration `envconfig:"WRITE_TIMEOUT" default:"10s"`
	IdleTimeout  time.Duration `envconfig:"IDLE_TIMEOUT" default:"60s"`

	// TLS configuration
	TLSEnabled    bool   `envconfig:"TLS_ENABLED" default:"false"`
	TLSCertFile   string `envconfig:"TLS_CERT_FILE" default:""`
	TLSKeyFile    string `envconfig:"TLS_KEY_FILE" default:""`
	TLSServerName string `envconfig:"TLS_SERVER_NAME" default:""`

	// Logging
	LogLevel string `envconfig:"LOG_LEVEL" default:"info"`

	// CORS
	AllowedOrigins []string `envconfig:"ALLOWED_ORIGINS" default:"http://localhost:3000,http://localhost:8080"`

	// Database configuration
	DatabaseURL string `envconfig:"DATABASE_URL" required:"true"`

	// Redis configuration (optional, for caching)
	RedisURL string `envconfig:"REDIS_URL" default:""`

	// Kafka configuration (optional, for events)
	KafkaBrokers []string `envconfig:"KAFKA_BROKERS" default:""`
	KafkaTopic   string   `envconfig:"KAFKA_TOPIC" default:"integration.events"`

	// Performance tuning
	MaxDBConnections    int           `envconfig:"MAX_DB_CONNECTIONS" default:"25"`
	MinDBConnections    int           `envconfig:"MIN_DB_CONNECTIONS" default:"5"`
	DBConnMaxLifetime   time.Duration `envconfig:"DB_CONN_MAX_LIFETIME" default:"1h"`
	DBConnMaxIdleTime   time.Duration `envconfig:"DB_CONN_MAX_IDLE_TIME" default:"30m"`

	// Service discovery (for health checks)
	ServiceEndpoints map[string]string `envconfig:"SERVICE_ENDPOINTS" default:""`

	// Health check configuration
	HealthCheckTimeout time.Duration `envconfig:"HEALTH_CHECK_TIMEOUT" default:"5s"`
	HealthCheckRetries int           `envconfig:"HEALTH_CHECK_RETRIES" default:"3"`

	// WebSocket configuration
	WebSocketReadTimeout  time.Duration `envconfig:"WS_READ_TIMEOUT" default:"60s"`
	WebSocketWriteTimeout time.Duration `envconfig:"WS_WRITE_TIMEOUT" default:"10s"`
	WebSocketPingInterval time.Duration `envconfig:"WS_PING_INTERVAL" default:"30s"`
	WebSocketMaxMessageSize int64       `envconfig:"WS_MAX_MESSAGE_SIZE" default:"4096"`

	// Circuit breaker configuration
	CircuitBreakerTimeout     time.Duration `envconfig:"CIRCUIT_BREAKER_TIMEOUT" default:"10s"`
	CircuitBreakerMaxRequests uint32        `envconfig:"CIRCUIT_BREAKER_MAX_REQUESTS" default:"3"`
	CircuitBreakerInterval    time.Duration `envconfig:"CIRCUIT_BREAKER_INTERVAL" default:"60s"`

	// Metrics configuration
	MetricsEnabled bool   `envconfig:"METRICS_ENABLED" default:"true"`
	MetricsPath    string `envconfig:"METRICS_PATH" default:"/metrics"`

	// Feature flags
	EnableWebSocket    bool `envconfig:"ENABLE_WEBSOCKET" default:"true"`
	EnableBatchHealth  bool `envconfig:"ENABLE_BATCH_HEALTH" default:"true"`
	EnableCircuitBreaker bool `envconfig:"ENABLE_CIRCUIT_BREAKER" default:"true"`

	// Integration-specific configuration
	WebhookTimeout        time.Duration `envconfig:"WEBHOOK_TIMEOUT" default:"30s"`
	CallbackRetryAttempts int           `envconfig:"CALLBACK_RETRY_ATTEMPTS" default:"3"`
	BridgeBufferSize      int           `envconfig:"BRIDGE_BUFFER_SIZE" default:"1000"`
}

// Load loads configuration from environment variables
func Load() (*Config, error) {
	var cfg Config
	if err := envconfig.Process("", &cfg); err != nil {
		return nil, err
	}
	return &cfg, nil
}


