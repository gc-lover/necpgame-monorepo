package config

import (
	"os"
	"strconv"
	"time"
)

// Config holds all configuration for the WebRTC Signaling Service
type Config struct {
	// Server configuration
	ServerAddr string
	ServerPort int

	// Database configuration
	DatabaseURL string

	// Redis configuration
	RedisURL string

	// Guild service configuration
	GuildServiceURL string

	// WebRTC configuration
	WebRTC struct {
		ICEServers []string
		STUNServer string
		TURNServer string
	}

	// Signaling configuration
	Signaling struct {
		MaxMessageSize   int
		MessageTimeout   time.Duration
		HeartbeatTimeout time.Duration
	}

	// Voice channel configuration
	VoiceChannel struct {
		DefaultMaxUsers int
		MaxChannelsPerGuild int
		ChannelTimeout time.Duration
	}

	// Monitoring configuration
	Metrics struct {
		Enabled bool
		Path    string
	}

	// Security configuration
	Security struct {
		JWTSecret string
		CORSOrigins []string
	}

	// Logging configuration
	LogLevel string
}

// Load loads configuration from environment variables
func Load() (*Config, error) {
	cfg := &Config{}

	// Server configuration
	cfg.ServerAddr = getEnv("SERVER_ADDR", ":8080")
	cfg.ServerPort = getEnvAsInt("SERVER_PORT", 8080)

	// Database configuration
	cfg.DatabaseURL = getEnv("DATABASE_URL", "postgres://user:password@localhost:5432/webrtc_signaling?sslmode=disable")

	// Redis configuration
	cfg.RedisURL = getEnv("REDIS_URL", "redis://localhost:6379")

	// Guild service configuration
	cfg.GuildServiceURL = getEnv("GUILD_SERVICE_URL", "http://localhost:8081/api/v1/social-domain/guild")

	// WebRTC configuration
	cfg.WebRTC.ICEServers = []string{
		getEnv("STUN_SERVER", "stun:stun.l.google.com:19302"),
		getEnv("TURN_SERVER", "turn:turn.example.com:3478"),
	}
	cfg.WebRTC.STUNServer = getEnv("STUN_SERVER", "stun:stun.l.google.com:19302")
	cfg.WebRTC.TURNServer = getEnv("TURN_SERVER", "turn:turn.example.com:3478")

	// Signaling configuration
	cfg.Signaling.MaxMessageSize = getEnvAsInt("MAX_MESSAGE_SIZE", 4096)
	cfg.Signaling.MessageTimeout = getEnvAsDuration("MESSAGE_TIMEOUT", 30*time.Second)
	cfg.Signaling.HeartbeatTimeout = getEnvAsDuration("HEARTBEAT_TIMEOUT", 60*time.Second)

	// Voice channel configuration
	cfg.VoiceChannel.DefaultMaxUsers = getEnvAsInt("DEFAULT_MAX_USERS", 50)
	cfg.VoiceChannel.MaxChannelsPerGuild = getEnvAsInt("MAX_CHANNELS_PER_GUILD", 10)
	cfg.VoiceChannel.ChannelTimeout = getEnvAsDuration("CHANNEL_TIMEOUT", 300*time.Second)

	// Monitoring configuration
	cfg.Metrics.Enabled = getEnvAsBool("METRICS_ENABLED", true)
	cfg.Metrics.Path = getEnv("METRICS_PATH", "/metrics")

	// Security configuration
	cfg.Security.JWTSecret = getEnv("JWT_SECRET", "your-secret-key")
	cfg.Security.CORSOrigins = []string{getEnv("CORS_ORIGINS", "*")}

	// Logging configuration
	cfg.LogLevel = getEnv("LOG_LEVEL", "info")

	return cfg, nil
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

func getEnvAsBool(key string, defaultValue bool) bool {
	if value := os.Getenv(key); value != "" {
		if boolValue, err := strconv.ParseBool(value); err == nil {
			return boolValue
		}
	}
	return defaultValue
}

func getEnvAsDuration(key string, defaultValue time.Duration) time.Duration {
	if value := os.Getenv(key); value != "" {
		if duration, err := time.ParseDuration(value); err == nil {
			return duration
		}
	}
	return defaultValue
}