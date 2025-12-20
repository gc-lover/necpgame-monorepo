// Package server Issue: #1
package server

import (
	"os"
	"strconv"

	_ "github.com/jackc/pgx/v5/stdlib"
)

// main является точкой входа для auth-login-service

// Config содержит конфигурацию сервиса
type Config struct {
	DatabaseURL string
	JWTSecret   string
	ServerPort  int
}

// loadConfig загружает конфигурацию из переменных окружения

// connectDatabase устанавливает соединение с PostgreSQL

// getEnv получает переменную окружения с дефолтным значением
func getEnv(key, defaultValue string) string {
	if value := os.Getenv(key); value != "" {
		return value
	}
	return defaultValue
}

// getEnvAsInt получает переменную окружения как int
func getEnvAsInt(key string, defaultValue int) int {
	if value := os.Getenv(key); value != "" {
		if intValue, err := strconv.Atoi(value); err == nil {
			return intValue
		}
	}
	return defaultValue
}
