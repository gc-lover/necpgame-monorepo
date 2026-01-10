package config

import (
	"fmt"
	"os"
	"strconv"
	"time"
)

type Config struct {
	Server   ServerConfig
	Database DatabaseConfig
	JWT      JWTConfig
	Redis    RedisConfig
}

type ServerConfig struct {
	Port         string
	ReadTimeout  time.Duration
	WriteTimeout time.Duration
}

type DatabaseConfig struct {
	Host     string
	Port     string
	User     string
	Password string
	DBName   string
	SSLMode  string
	MaxConns int
	MinConns int
	MaxConnLifetime time.Duration
	MaxConnIdleTime time.Duration
}

func (db DatabaseConfig) GetDSN() string {
	return fmt.Sprintf("postgres://%s:%s@%s:%s/%s?sslmode=%s",
		db.User, db.Password, db.Host, db.Port, db.DBName, db.SSLMode)
}

type JWTConfig struct {
	Secret     string
	Expiration time.Duration
}

type RedisConfig struct {
	Host         string
	Port         string
	Password     string
	DB           int
	PoolSize     int // BACKEND NOTE: Redis connection pool size for MMOFPS
	MinIdleConns int // BACKEND NOTE: Minimum idle connections to maintain
}

func Load() *Config {
	return &Config{
		Server: ServerConfig{
			Port:         getEnv("PORT", ":8081"),
			ReadTimeout:  10 * time.Second,
			WriteTimeout: 10 * time.Second,
		},
		Database: DatabaseConfig{
			Host:     getEnv("DB_HOST", "localhost"),
			Port:     getEnv("DB_PORT", "5432"),
			User:     getEnv("DB_USER", "ability_user"),
			Password: getEnv("DB_PASSWORD", "ability_password"),
			DBName:   getEnv("DB_NAME", "ability_db"),
			SSLMode:  getEnv("DB_SSLMODE", "disable"),
			// BACKEND NOTE: Enterprise-grade database pool optimization for MMOFPS (100k+ concurrent users)
			MaxConns:        getEnvInt("DB_MAX_CONNS", 50), // BACKEND NOTE: High pool for ability operations (50 max connections)
			MinConns:        getEnvInt("DB_MIN_CONNS", 10), // BACKEND NOTE: Keep minimum connections ready for instant ability access
			MaxConnLifetime: time.Duration(getEnvInt("DB_MAX_CONN_LIFETIME_MIN", 30)) * time.Minute, // BACKEND NOTE: Shorter lifetime for real-time ability ops
			MaxConnIdleTime: time.Duration(getEnvInt("DB_MAX_CONN_IDLE_MIN", 5)) * time.Minute,      // BACKEND NOTE: Quick cleanup for active ability sessions
		},
		JWT: JWTConfig{
			Secret:     getEnv("JWT_SECRET", "your-secret-key-change-in-production"),
			Expiration: time.Duration(getEnvInt("JWT_EXPIRATION_HOURS", 24)) * time.Hour,
		},
		Redis: RedisConfig{
			Host:         getEnv("REDIS_HOST", "localhost"),
			Port:         getEnv("REDIS_PORT", "6379"),
			Password:     getEnv("REDIS_PASSWORD", ""),
			DB:           getEnvInt("REDIS_DB", 1),
			// BACKEND NOTE: Enterprise-grade Redis pool for MMOFPS ability caching
			PoolSize:     getEnvInt("REDIS_POOL_SIZE", 25),     // BACKEND NOTE: High pool for ability session caching
			MinIdleConns: getEnvInt("REDIS_MIN_IDLE_CONNS", 8), // BACKEND NOTE: Keep connections ready for instant ability access
		},
	}
}

func getEnv(key, defaultValue string) string {
	if value := os.Getenv(key); value != "" {
		return value
	}
	return defaultValue
}

func getEnvInt(key string, defaultValue int) int {
	if value := os.Getenv(key); value != "" {
		if intValue, err := strconv.Atoi(value); err == nil {
			return intValue
		}
	}
	return defaultValue
}