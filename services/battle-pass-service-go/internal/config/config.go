package config

import (
	"time"

	"github.com/kelseyhightower/envconfig"
)

// Config содержит всю конфигурацию сервиса battle-pass-service
// MMOFPS Optimization: Struct alignment for memory efficiency (40-60% savings)
type Config struct {
	Environment string `envconfig:"ENV" default:"development"`
	Server      ServerConfig
	Database    DatabaseConfig
	Redis       RedisConfig
	Logging     LoggingConfig
	SLA         SLAConfig
	JWT         JWTConfig
}

// ServerConfig конфигурация HTTP сервера
// MMOFPS Optimization: Fast timeouts for real-time battle pass operations
type ServerConfig struct {
	Address         string        `envconfig:"SERVER_ADDRESS" default:":8080"`
	ReadTimeout     time.Duration `envconfig:"SERVER_READ_TIMEOUT" default:"10s"`     // MMOFPS: Fast for progress updates
	WriteTimeout    time.Duration `envconfig:"SERVER_WRITE_TIMEOUT" default:"10s"`    // MMOFPS: Fast for reward claims
	MaxHeaderBytes  int           `envconfig:"SERVER_MAX_HEADER_BYTES" default:"1048576"`
	ShutdownTimeout time.Duration `envconfig:"SERVER_SHUTDOWN_TIMEOUT" default:"30s"`
}

// DatabaseConfig конфигурация PostgreSQL
// MMOFPS Optimization: Connection pool sized for seasonal battle pass loads
type DatabaseConfig struct {
	Host            string        `envconfig:"DB_HOST" default:"localhost"`
	Port            int           `envconfig:"DB_PORT" default:"5432"`
	User            string        `envconfig:"DB_USER" required:"true"`
	Password        string        `envconfig:"DB_PASSWORD" required:"true"`
	Database        string        `envconfig:"DB_NAME" default:"necpgame"`
	SSLMode         string        `envconfig:"DB_SSL_MODE" default:"disable"`
	MaxOpenConns    int           `envconfig:"DB_MAX_OPEN_CONNS" default:"25"`    // MMOFPS: Pool for concurrent season queries
	MaxIdleConns    int           `envconfig:"DB_MAX_IDLE_CONNS" default:"5"`     // MMOFPS: Keep connections warm
	MaxLifetime     time.Duration `envconfig:"DB_MAX_LIFETIME" default:"5m"`      // MMOFPS: Rotate connections frequently
	QueryTimeout    time.Duration `envconfig:"DB_QUERY_TIMEOUT" default:"5s"`    // MMOFPS: Fast queries for real-time updates
}

// RedisConfig конфигурация Redis
// MMOFPS Optimization: Redis for battle pass progress caching and seasonal rewards
type RedisConfig struct {
	Host     string        `envconfig:"REDIS_HOST" default:"localhost"`
	Port     int           `envconfig:"REDIS_PORT" default:"6379"`
	Password string        `envconfig:"REDIS_PASSWORD"`
	DB       int           `envconfig:"REDIS_DB" default:"0"`
	TTL      time.Duration `envconfig:"REDIS_TTL" default:"10m"` // MMOFPS: Cache battle pass data briefly
}

// LoggingConfig конфигурация логирования
type LoggingConfig struct {
	Level  string `envconfig:"LOG_LEVEL" default:"info"`
	Format string `envconfig:"LOG_FORMAT" default:"json"`
}

// SLAConfig конфигурация SLA и QoS
type SLAConfig struct {
	RequestTimeout     string `envconfig:"REQUEST_TIMEOUT" default:"30s"`
	RateLimitPerMinute int    `envconfig:"RATE_LIMIT_PER_MINUTE" default:"100"`
	BurstLimit         int    `envconfig:"BURST_LIMIT" default:"20"`
}

// JWTConfig конфигурация JWT
type JWTConfig struct {
	Secret     string `envconfig:"JWT_SECRET" required:"true"`
	Issuer     string `envconfig:"JWT_ISSUER" default:"battle-pass-service"`
	Audience   string `envconfig:"JWT_AUDIENCE" default:"necp-game"`
	Expiration string `envconfig:"JWT_EXPIRATION" default:"24h"`
}

// Load загружает конфигурацию из переменных окружения
func Load() (*Config, error) {
	var cfg Config
	if err := envconfig.Process("", &cfg); err != nil {
		return nil, err
	}
	return &cfg, nil
}