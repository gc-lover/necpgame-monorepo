// Anti-Cheat Behavior Analytics Service Configuration
// Issue: #2212
// Enterprise-grade configuration management for anti-cheat system

package config

import (
	"time"
)

// Config represents the main configuration structure
type Config struct {
	Server    ServerConfig    `yaml:"server"`
	Database  DatabaseConfig  `yaml:"database"`
	Redis     RedisConfig     `yaml:"redis"`
	Kafka     KafkaConfig     `yaml:"kafka"`
	Analytics AnalyticsConfig `yaml:"analytics"`
	Detection DetectionConfig `yaml:"detection"`
	Security  SecurityConfig  `yaml:"security"`
}

// ServerConfig holds HTTP server configuration
type ServerConfig struct {
	Host         string        `yaml:"host"`
	Port         int           `yaml:"port"`
	ReadTimeout  time.Duration `yaml:"read_timeout"`
	WriteTimeout time.Duration `yaml:"write_timeout"`
	IdleTimeout  time.Duration `yaml:"idle_timeout"`
}

// DatabaseConfig holds PostgreSQL configuration
type DatabaseConfig struct {
	Host            string        `yaml:"host"`
	Port            int           `yaml:"port"`
	User            string        `yaml:"user"`
	Password        string        `yaml:"password"`
	Database        string        `yaml:"database"`
	SSLMode         string        `yaml:"ssl_mode"`
	MaxOpenConns    int           `yaml:"max_open_conns"`
	MaxIdleConns    int           `yaml:"max_idle_conns"`
	ConnMaxLifetime time.Duration `yaml:"conn_max_lifetime"`
}

// RedisConfig holds Redis configuration
type RedisConfig struct {
	Addr         string `yaml:"addr"`
	Password     string `yaml:"password"`
	DB           int    `yaml:"db"`
	PoolSize     int    `yaml:"pool_size"`
	MinIdleConns int    `yaml:"min_idle_conns"`
}

// KafkaConfig holds Kafka configuration
type KafkaConfig struct {
	Brokers       []string `yaml:"brokers"`
	Topic         string   `yaml:"topic"`
	GroupID       string   `yaml:"group_id"`
	StartOffset   string   `yaml:"start_offset"`
	MinBytes      int      `yaml:"min_bytes"`
	MaxBytes      int      `yaml:"max_bytes"`
	CommitInterval time.Duration `yaml:"commit_interval"`
}

// AnalyticsConfig holds analytics engine configuration
type AnalyticsConfig struct {
	BatchSize          int           `yaml:"batch_size"`
	ProcessingInterval time.Duration `yaml:"processing_interval"`
	RetentionPeriod    time.Duration `yaml:"retention_period"`
	MaxConcurrentJobs  int           `yaml:"max_concurrent_jobs"`
	EnableRealTime     bool          `yaml:"enable_real_time"`
	AlertThreshold     float64       `yaml:"alert_threshold"`
}

// DetectionConfig holds detection engine configuration
type DetectionConfig struct {
	EnabledRules        []string      `yaml:"enabled_rules"`
	UpdateInterval      time.Duration `yaml:"update_interval"`
	FalsePositiveRate   float64       `yaml:"false_positive_rate"`
	MinConfidence       float64       `yaml:"min_confidence"`
	MaxConcurrentChecks int           `yaml:"max_concurrent_checks"`
	CacheTTL            time.Duration `yaml:"cache_ttl"`
}

// SecurityConfig holds security configuration
type SecurityConfig struct {
	JWTSecret          string        `yaml:"jwt_secret"`
	TokenExpiration    time.Duration `yaml:"token_expiration"`
	AllowedOrigins     []string      `yaml:"allowed_origins"`
	RateLimitRequests  int           `yaml:"rate_limit_requests"`
	RateLimitWindow    time.Duration `yaml:"rate_limit_window"`
	EnableIPWhitelist  bool          `yaml:"enable_ip_whitelist"`
	IPWhitelist        []string      `yaml:"ip_whitelist"`
}

// DefaultConfig returns a default configuration
func DefaultConfig() *Config {
	return &Config{
		Server: ServerConfig{
			Host:         "0.0.0.0",
			Port:         8080,
			ReadTimeout:  15 * time.Second,
			WriteTimeout: 15 * time.Second,
			IdleTimeout:  60 * time.Second,
		},
		Database: DatabaseConfig{
			Host:            "localhost",
			Port:            5432,
			User:            "postgres",
			Password:        "postgres",
			Database:        "necpgame",
			SSLMode:         "disable",
			MaxOpenConns:    25,
			MaxIdleConns:    10,
			ConnMaxLifetime: 5 * time.Minute,
		},
		Redis: RedisConfig{
			Addr:         "localhost:6379",
			Password:     "",
			DB:           0,
			PoolSize:     10,
			MinIdleConns: 5,
		},
		Kafka: KafkaConfig{
			Brokers:         []string{"localhost:9092"},
			Topic:           "game.anticheat.events",
			GroupID:         "anticheat-analytics",
			StartOffset:     "last",
			MinBytes:        10 * 1024,  // 10KB
			MaxBytes:        10 * 1024 * 1024, // 10MB
			CommitInterval:  time.Second,
		},
		Analytics: AnalyticsConfig{
			BatchSize:          1000,
			ProcessingInterval: 30 * time.Second,
			RetentionPeriod:    90 * 24 * time.Hour, // 90 days
			MaxConcurrentJobs:  10,
			EnableRealTime:     true,
			AlertThreshold:     0.8,
		},
		Detection: DetectionConfig{
			EnabledRules: []string{
				"aimbot_detection",
				"speed_hack_detection",
				"wallhack_detection",
				"macro_detection",
				"stat_anomaly_detection",
			},
			UpdateInterval:      5 * time.Minute,
			FalsePositiveRate:   0.05,
			MinConfidence:       0.75,
			MaxConcurrentChecks: 50,
			CacheTTL:            10 * time.Minute,
		},
		Security: SecurityConfig{
			JWTSecret:         "your-secret-key-change-in-production",
			TokenExpiration:   24 * time.Hour,
			AllowedOrigins:    []string{"http://localhost:3000", "https://game.necpgame.com"},
			RateLimitRequests: 100,
			RateLimitWindow:   time.Minute,
			EnableIPWhitelist: false,
			IPWhitelist:       []string{},
		},
	}
}
