package config

type Config struct {
	Port        int
	DatabaseURL string
	RedisURL    string
	Environment string
	ServiceName string
}
