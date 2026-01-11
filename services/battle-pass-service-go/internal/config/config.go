package config

import (
	"github.com/kelseyhightower/envconfig"
)

// Config содержит всю конфигурацию сервиса battle-pass-service
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
type ServerConfig struct {
	Port    int    `envconfig:"PORT" default:"8080"`
	Host    string `envconfig:"HOST" default:"0.0.0.0"`
	Timeout struct {
		Read  int `envconfig:"READ_TIMEOUT" default:"30"`
		Write int `envconfig:"WRITE_TIMEOUT" default:"30"`
		Idle  int `envconfig:"IDLE_TIMEOUT" default:"60"`
	}
}

// DatabaseConfig конфигурация PostgreSQL
type DatabaseConfig struct {
	Host         string `envconfig:"DB_HOST" default:"localhost"`
	Port         int    `envconfig:"DB_PORT" default:"5432"`
	User         string `envconfig:"DB_USER" default:"battle_pass"`
	Password     string `envconfig:"DB_PASSWORD" required:"true"`
	Database     string `envconfig:"DB_NAME" default:"battle_pass"`
	SSLMode      string `envconfig:"DB_SSLMODE" default:"disable"`
	MaxConns     int    `envconfig:"DB_MAX_CONNS" default:"25"`
	MinConns     int    `envconfig:"DB_MIN_CONNS" default:"5"`
	MaxConnLifetime string `envconfig:"DB_MAX_CONN_LIFETIME" default:"1h"`
	MaxConnIdleTime string `envconfig:"DB_MAX_CONN_IDLE_TIME" default:"30m"`
	HealthCheckPeriod string `envconfig:"DB_HEALTH_CHECK_PERIOD" default:"1m"`
}

// RedisConfig конфигурация Redis
type RedisConfig struct {
	Host         string `envconfig:"REDIS_HOST" default:"localhost"`
	Port         int    `envconfig:"REDIS_PORT" default:"6379"`
	Password     string `envconfig:"REDIS_PASSWORD" default:""`
	DB           int    `envconfig:"REDIS_DB" default:"0"`
	PoolSize     int    `envconfig:"REDIS_POOL_SIZE" default:"10"`
	MinIdleConns int    `envconfig:"REDIS_MIN_IDLE_CONNS" default:"5"`
	MaxConnAge   string `envconfig:"REDIS_MAX_CONN_AGE" default:"1h"`
	IdleTimeout  string `envconfig:"REDIS_IDLE_TIMEOUT" default:"30m"`
	ReadTimeout  string `envconfig:"REDIS_READ_TIMEOUT" default:"3s"`
	WriteTimeout string `envconfig:"REDIS_WRITE_TIMEOUT" default:"3s"`
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