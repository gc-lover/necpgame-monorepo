package config

import (
	"fmt"
	"os"
	"strconv"
	"time"

	"github.com/redis/go-redis/v9"
)

// Config holds all configuration for the tournament service
type Config struct {
	Database DatabaseConfig
	Redis    RedisConfig
	Server   ServerConfig
	Tournament TournamentConfig
}

// DatabaseConfig holds database connection configuration
type DatabaseConfig struct {
	Host         string
	Port         int
	User         string
	Password     string
	Database     string
	SSLMode      string
	MaxOpenConns int
	MaxIdleConns int
	MaxLifetime  time.Duration
}

// RedisConfig holds Redis connection configuration
type RedisConfig struct {
	Host         string
	Port         int
	Password     string
	DB           int
	MaxRetries   int
	PoolSize     int
	MinIdleConns int
}

// ServerConfig holds HTTP server configuration
type ServerConfig struct {
	Port         string
	ReadTimeout  time.Duration
	WriteTimeout time.Duration
	IdleTimeout  time.Duration
}

// TournamentConfig holds tournament-specific configuration
type TournamentConfig struct {
	MaxConcurrentTournaments int
	MaxPlayersPerTournament  int
	TournamentTimeout        time.Duration
	MatchmakingTimeout       time.Duration
	LeaderboardCacheTTL      time.Duration
	DefaultEstimatedWait     time.Duration
}

// Load loads configuration from environment variables with sensible defaults
func Load() (*Config, error) {
	cfg := &Config{}

	// Database configuration
	cfg.Database = DatabaseConfig{
		Host:         getEnv("DB_HOST", "localhost"),
		Port:         getEnvAsInt("DB_PORT", 5432),
		User:         getEnv("DB_USER", "tournament"),
		Password:     getEnv("DB_PASSWORD", "password"),
		Database:     getEnv("DB_NAME", "tournament"),
		SSLMode:      getEnv("DB_SSLMODE", "disable"),
		MaxOpenConns: getEnvAsInt("DB_MAX_OPEN_CONNS", 25),
		MaxIdleConns: getEnvAsInt("DB_MAX_IDLE_CONNS", 25),
		MaxLifetime:  time.Duration(getEnvAsInt("DB_MAX_LIFETIME_MIN", 5)) * time.Minute,
	}

	// Redis configuration
	cfg.Redis = RedisConfig{
		Host:         getEnv("REDIS_HOST", "localhost"),
		Port:         getEnvAsInt("REDIS_PORT", 6379),
		Password:     getEnv("REDIS_PASSWORD", ""),
		DB:           getEnvAsInt("REDIS_DB", 0),
		MaxRetries:   getEnvAsInt("REDIS_MAX_RETRIES", 3),
		PoolSize:     getEnvAsInt("REDIS_POOL_SIZE", 10),
		MinIdleConns: getEnvAsInt("REDIS_MIN_IDLE_CONNS", 2),
	}

	// Server configuration
	cfg.Server = ServerConfig{
		Port:         getEnv("PORT", "8080"),
		ReadTimeout:  time.Duration(getEnvAsInt("SERVER_READ_TIMEOUT_SEC", 30)) * time.Second,
		WriteTimeout: time.Duration(getEnvAsInt("SERVER_WRITE_TIMEOUT_SEC", 30)) * time.Second,
		IdleTimeout:  time.Duration(getEnvAsInt("SERVER_IDLE_TIMEOUT_SEC", 120)) * time.Second,
	}

	// Tournament configuration
	cfg.Tournament = TournamentConfig{
		MaxConcurrentTournaments: getEnvAsInt("TOURNAMENT_MAX_CONCURRENT", 100),
		MaxPlayersPerTournament:  getEnvAsInt("TOURNAMENT_MAX_PLAYERS", 1000),
		TournamentTimeout:        time.Duration(getEnvAsInt("TOURNAMENT_TIMEOUT_MIN", 60)) * time.Minute,
		MatchmakingTimeout:       time.Duration(getEnvAsInt("MATCHMAKING_TIMEOUT_SEC", 30)) * time.Second,
		LeaderboardCacheTTL:      time.Duration(getEnvAsInt("LEADERBOARD_CACHE_TTL_MIN", 5)) * time.Minute,
		DefaultEstimatedWait:     time.Duration(getEnvAsInt("DEFAULT_ESTIMATED_WAIT_SEC", 60)) * time.Second,
	}

	return cfg, nil
}

// GetDatabaseDSN returns the database connection string
func (c *DatabaseConfig) GetDatabaseDSN() string {
	return fmt.Sprintf("host=%s port=%d user=%s password=%s dbname=%s sslmode=%s",
		c.Host, c.Port, c.User, c.Password, c.Database, c.SSLMode)
}

// GetRedisOptions returns Redis client options
func (c *RedisConfig) GetRedisOptions() *redis.Options {
	return &redis.Options{
		Addr:         fmt.Sprintf("%s:%d", c.Host, c.Port),
		Password:     c.Password,
		DB:           c.DB,
		MaxRetries:   c.MaxRetries,
		PoolSize:     c.PoolSize,
		MinIdleConns: c.MinIdleConns,
	}
}

// Validate validates the configuration
func (c *Config) Validate() error {
	if c.Database.Host == "" {
		return fmt.Errorf("database host is required")
	}
	if c.Database.Port <= 0 || c.Database.Port > 65535 {
		return fmt.Errorf("database port must be between 1 and 65535")
	}
	if c.Database.User == "" {
		return fmt.Errorf("database user is required")
	}
	if c.Database.Database == "" {
		return fmt.Errorf("database name is required")
	}
	if c.Redis.Host == "" {
		return fmt.Errorf("redis host is required")
	}
	if c.Redis.Port <= 0 || c.Redis.Port > 65535 {
		return fmt.Errorf("redis port must be between 1 and 65535")
	}
	if c.Server.Port == "" {
		return fmt.Errorf("server port is required")
	}
	return nil
}

// Helper functions for environment variable parsing
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