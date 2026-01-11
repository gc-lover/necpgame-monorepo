//go:align 64
// Issue: #2293

package server

import (
	"time"
)

// Config contains combat system configuration with MMOFPS optimizations
// PERFORMANCE: Struct aligned for memory efficiency (pointers first, then values)
type Config struct {
	// Database configuration
	DBHost     string
	DBPort     int
	DBName     string
	DBUser     string
	DBPassword string
	DBPoolSize int

	// Redis configuration
	RedisHost     string
	RedisPort     int
	RedisPassword string
	RedisDB       int

	// Server configuration
	ServerHost string
	ServerPort int

	// Combat system configuration
	MaxWorkers          int
	CalculationTimeout  time.Duration
	RequestTimeout      time.Duration
	CacheExpiration     time.Duration
	HealthCheckInterval time.Duration

	// Performance tuning
	WorkerPoolSize      int
	ObjectPoolSize      int
	BatchSize           int

	// Padding for alignment
	_pad [64]byte
}

// NewConfig creates default combat system configuration
func NewConfig() *Config {
	return &Config{
		// Database defaults
		DBHost:     "localhost",
		DBPort:     5432,
		DBName:     "combat_system",
		DBUser:     "combat_user",
		DBPassword: "password",
		DBPoolSize: 50,

		// Redis defaults
		RedisHost:     "localhost",
		RedisPort:     6379,
		RedisPassword: "",
		RedisDB:       0,

		// Server defaults
		ServerHost: "0.0.0.0",
		ServerPort: 8080,

		// Combat defaults
		MaxWorkers:          100,
		CalculationTimeout:  50 * time.Millisecond,
		RequestTimeout:      100 * time.Millisecond,
		CacheExpiration:     5 * time.Minute,
		HealthCheckInterval: 30 * time.Second,

		// Performance defaults
		WorkerPoolSize: 100,
		ObjectPoolSize: 1000,
		BatchSize:      100,
	}
}