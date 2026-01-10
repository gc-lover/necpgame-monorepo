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
	Adaptive AdaptiveConfig
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
	Host     string
	Port     string
	Password string
	DB       int
}

type AdaptiveConfig struct {
	LearningRate     float64
	AdaptationWindow time.Duration
	MaxHistorySize   int
	UpdateInterval   time.Duration
}

func Load() *Config {
	return &Config{
		Server: ServerConfig{
			Port:         getEnv("PORT", ":8085"),
			ReadTimeout:  15 * time.Second,
			WriteTimeout: 15 * time.Second,
		},
		Database: DatabaseConfig{
			Host:     getEnv("DB_HOST", "localhost"),
			Port:     getEnv("DB_PORT", "5432"),
			User:     getEnv("DB_USER", "adaptive_user"),
			Password: getEnv("DB_PASSWORD", "adaptive_password"),
			DBName:   getEnv("DB_NAME", "adaptive_db"),
			SSLMode:  getEnv("DB_SSLMODE", "disable"),
			// Enterprise-grade database pool optimization for MMOFPS
			MaxConns:        getEnvInt("DB_MAX_CONNS", 25),
			MinConns:        getEnvInt("DB_MIN_CONNS", 5),
			MaxConnLifetime: time.Duration(getEnvInt("DB_MAX_CONN_LIFETIME_MIN", 60)) * time.Minute,
			MaxConnIdleTime: time.Duration(getEnvInt("DB_MAX_CONN_IDLE_MIN", 30)) * time.Minute,
		},
		JWT: JWTConfig{
			Secret:     getEnv("JWT_SECRET", "your-secret-key-change-in-production"),
			Expiration: time.Duration(getEnvInt("JWT_EXPIRATION_HOURS", 24)) * time.Hour,
		},
		Redis: RedisConfig{
			Host:     getEnv("REDIS_HOST", "localhost"),
			Port:     getEnv("REDIS_PORT", "6379"),
			Password: getEnv("REDIS_PASSWORD", ""),
			DB:       getEnvInt("REDIS_DB", 2),
		},
		Adaptive: AdaptiveConfig{
			LearningRate:     getEnvFloat("ADAPTIVE_LEARNING_RATE", 0.01),
			AdaptationWindow: time.Duration(getEnvInt("ADAPTIVE_WINDOW_MIN", 60)) * time.Minute,
			MaxHistorySize:   getEnvInt("ADAPTIVE_MAX_HISTORY", 10000),
			UpdateInterval:   time.Duration(getEnvInt("ADAPTIVE_UPDATE_SEC", 30)) * time.Second,
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

func getEnvFloat(key string, defaultValue float64) float64 {
	if value := os.Getenv(key); value != "" {
		if floatValue, err := strconv.ParseFloat(value, 64); err == nil {
			return floatValue
		}
	}
	return defaultValue
}