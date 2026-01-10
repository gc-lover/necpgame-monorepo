package config

import (
	"time"
)

// ProductionConfig returns optimized configuration for production environment
func ProductionConfig() *Config {
	return &Config{
		Server: ServerConfig{
			Host:         getEnv("SERVER_HOST", "0.0.0.0"),
			Port:         getEnvAsInt("SERVER_PORT", 8080),
			ReadTimeout:  getEnvAsDuration("SERVER_READ_TIMEOUT", 10*time.Second),  // Reduced for performance
			WriteTimeout: getEnvAsDuration("SERVER_WRITE_TIMEOUT", 10*time.Second), // Reduced for performance
		},
		Database: DatabaseConfig{
			Host:     getEnv("DB_HOST", "localhost"),
			Port:     getEnvAsInt("DB_PORT", 5432),
			User:     getEnv("DB_USER", "battlepass"),
			Password: getEnv("DB_PASSWORD", ""),
			DBName:   getEnv("DB_NAME", "battlepass"),
			SSLMode:  getEnv("DB_SSLMODE", "require"), // SSL required in production
			MaxConns: getEnvAsInt("DB_MAX_CONNS", 50),   // Increased for high load
		},
		Redis: RedisConfig{
			Host:     getEnv("REDIS_HOST", "localhost"),
			Port:     getEnvAsInt("REDIS_PORT", 6379),
			Password: getEnv("REDIS_PASSWORD", ""),
			DB:       getEnvAsInt("REDIS_DB", 0),
		},
		JWT: JWTConfig{
			Secret:     getEnv("JWT_SECRET", ""), // Must be set in production
			ExpiryTime: getEnvAsDuration("JWT_EXPIRY_TIME", 1*time.Hour), // Shorter expiry
		},
		Services: ServicesConfig{
			PlayerServiceURL:    getEnv("PLAYER_SERVICE_URL", "http://player-service:8080"),
			InventoryServiceURL: getEnv("INVENTORY_SERVICE_URL", "http://inventory-service:8080"),
			EconomyServiceURL:   getEnv("ECONOMY_SERVICE_URL", "http://economy-service:8080"),
		},
	}
}