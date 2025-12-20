// Package server Issue: #1
package server

import (
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

// getEnvAsInt получает переменную окружения как int
