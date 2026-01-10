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
	IdleTimeout  time.Duration
	MaxHeaderBytes int
}

type DatabaseConfig struct {
	Host            string
	Port            string
	User            string
	Password        string
	DBName          string
	SSLMode         string
	MaxConns        int
	MinConns        int
	MaxConnLifetime time.Duration
	MaxConnIdleTime time.Duration
	HealthCheckPeriod time.Duration
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

func (db DatabaseConfig) GetDSN() string {
	return fmt.Sprintf("postgres://%s:%s@%s:%s/%s?sslmode=%s",
		db.User, db.Password, db.Host, db.Port, db.DBName, db.SSLMode)
}

func Load() *Config {
	return &Config{
		Server: ServerConfig{
			Port:         getEnv("PORT", ":8083"),
			ReadTimeout:  15 * time.Second,
			WriteTimeout: 15 * time.Second,
			IdleTimeout:  120 * time.Second,
			MaxHeaderBytes: 1 << 20, // 1MB
		},
		Database: DatabaseConfig{
			Host:     getEnv("DB_HOST", "localhost"),
			Port:     getEnv("DB_PORT", "5432"),
			User:     getEnv("DB_USER", "economy_user"),
			Password: getEnv("DB_PASSWORD", "economy_password"),
			DBName:   getEnv("DB_NAME", "economy_db"),
			SSLMode:  getEnv("DB_SSLMODE", "disable"),
			MaxConns: 25,
			MinConns: 5,
			MaxConnLifetime: 1 * time.Hour,
			MaxConnIdleTime: 30 * time.Minute,
			HealthCheckPeriod: 1 * time.Minute,
		},
		JWT: JWTConfig{
			Secret:     getEnv("JWT_SECRET", "economy-secret-key"),
			Expiration: time.Duration(getEnvInt("JWT_EXPIRATION_HOURS", 24)) * time.Hour,
		},
		Redis: RedisConfig{
			Host:     getEnv("REDIS_HOST", "localhost"),
			Port:     getEnv("REDIS_PORT", "6379"),
			Password: getEnv("REDIS_PASSWORD", ""),
			DB:       getEnvInt("REDIS_DB", 1),
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