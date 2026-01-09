#!/usr/bin/env python3
"""
Create basic Go service structure for all enterprise-grade services
"""

import os
import sys
from pathlib import Path

def create_service_structure(service_name, output_dir):
    """Create basic Go service structure"""
    service_dir = output_dir / f"{service_name}-go"

    try:
        # Create directory structure
        service_dir.mkdir(parents=True, exist_ok=True)
        pkg_dir = service_dir / "pkg"
        api_dir = pkg_dir / "api"
        server_dir = service_dir / "server"
        config_dir = service_dir / "config"

        for dir_path in [pkg_dir, api_dir, server_dir, config_dir]:
            dir_path.mkdir(parents=True, exist_ok=True)

        # Create go.mod
        go_mod_content = f"""module {service_name}-go

go 1.21

require (
	github.com/gin-gonic/gin v1.9.1
	github.com/joho/godotenv v1.4.0
	go.uber.org/zap v1.24.0
	github.com/lib/pq v1.10.9
	github.com/go-redis/redis/v8 v8.11.5
)
"""
        (service_dir / "go.mod").write_text(go_mod_content)

        # Create main.go
        main_go_content = f"""package main

import (
	"context"
	"log"
	"net/http"
	"os"
	"os/signal"
	"syscall"
	"time"

	"github.com/gin-gonic/gin"
	"go.uber.org/zap"
	"{service_name}-go/config"
	"{service_name}-go/server"
)

func main() {{
	logger, _ := zap.NewProduction()
	defer logger.Sync()

	cfg := config.Load()

	r := gin.New()
	r.Use(gin.Logger(), gin.Recovery())

	// Health endpoint
	r.GET("/health", func(c *gin.Context) {{
		c.JSON(http.StatusOK, gin.H{{
			"status": "healthy",
			"service": "{service_name}",
			"timestamp": time.Now().UTC(),
		}})
	}})

	// API routes
	api := r.Group("/api/v1")
	server.SetupRoutes(api, logger)

	srv := &http.Server{{
		Addr:    cfg.Port,
		Handler: r,
	}}

	go func() {{
		logger.Info("Starting server", zap.String("port", cfg.Port))
		if err := srv.ListenAndServe(); err != nil && err != http.ErrServerClosed {{
			logger.Fatal("Failed to start server", zap.Error(err))
		}}
	}}()

	// Wait for interrupt signal to gracefully shutdown the server
	quit := make(chan os.Signal, 1)
	signal.Notify(quit, syscall.SIGINT, syscall.SIGTERM)
	<-quit
	logger.Info("Shutting down server...")

	ctx, cancel := context.WithTimeout(context.Background(), 30*time.Second)
	defer cancel()
	if err := srv.Shutdown(ctx); err != nil {{
		logger.Fatal("Server forced to shutdown", zap.Error(err))
	}}

	logger.Info("Server exited")
}}
"""
        (service_dir / "main.go").write_text(main_go_content)

        # Create config.go
        config_go_content = f"""package config

import (
	"os"
	"strconv"
)

type Config struct {{
	Port     string
	Database DatabaseConfig
	Redis    RedisConfig
}}

type DatabaseConfig struct {{
	Host     string
	Port     string
	User     string
	Password string
	DBName   string
	SSLMode  string
}}

type RedisConfig struct {{
	Host     string
	Port     string
	Password string
	DB       int
}}

func Load() *Config {{
	return &Config{{
		Port: getEnv("PORT", ":8080"),
		Database: DatabaseConfig{{
			Host:     getEnv("DB_HOST", "localhost"),
			Port:     getEnv("DB_PORT", "5432"),
			User:     getEnv("DB_USER", "{service_name}"),
			Password: getEnv("DB_PASSWORD", ""),
			DBName:   getEnv("DB_NAME", "{service_name}"),
			SSLMode:  getEnv("DB_SSLMODE", "disable"),
		}},
		Redis: RedisConfig{{
			Host:     getEnv("REDIS_HOST", "localhost"),
			Port:     getEnv("REDIS_PORT", "6379"),
			Password: getEnv("REDIS_PASSWORD", ""),
			DB:       getEnvInt("REDIS_DB", 0),
		}},
	}}
}}

func getEnv(key, defaultValue string) string {{
	if value := os.Getenv(key); value != "" {{
		return value
	}}
	return defaultValue
}}

func getEnvInt(key string, defaultValue int) int {{
	if value := os.Getenv(key); value != "" {{
		if intValue, err := strconv.Atoi(value); err == nil {{
			return intValue
		}}
	}}
	return defaultValue
}}
"""
        (config_dir / "config.go").write_text(config_go_content)

        # Create server.go
        server_go_content = f"""package server

import (
	"github.com/gin-gonic/gin"
	"go.uber.org/zap"
)

func SetupRoutes(router *gin.RouterGroup, logger *zap.Logger) {{
	// {service_name} specific routes
	serviceGroup := router.Group("/{service_name}")

	serviceGroup.GET("/ping", func(c *gin.Context) {{
		c.JSON(200, gin.H{{
			"message": "{service_name} service is running",
		}})
	}})

	// Add more routes here based on OpenAPI specification
	// TODO: Implement CRUD operations from proto/openapi/{service_name}/main.yaml
}
"""
        (server_dir / "server.go").write_text(server_go_content)

        # Create Dockerfile
        dockerfile_content = f"""FROM golang:1.21-alpine AS builder

WORKDIR /app
COPY go.mod go.sum ./
RUN go mod download

COPY . .
RUN CGO_ENABLED=0 GOOS=linux go build -a -installsuffix cgo -o main .

FROM alpine:latest
RUN apk --no-cache add ca-certificates
WORKDIR /root/
COPY --from=builder /app/main .
COPY --from=builder /app/config/ ./config/

EXPOSE 8080
CMD ["./main"]
"""
        (service_dir / "Dockerfile").write_text(dockerfile_content)

        # Create docker-compose.yml
        compose_content = f"""version: '3.8'

services:
  {service_name}:
    build: .
    ports:
      - "8080:8080"
    environment:
      - PORT=:8080
      - DB_HOST=postgres
      - DB_PORT=5432
      - DB_USER={service_name}
      - DB_PASSWORD=password
      - DB_NAME={service_name}
      - REDIS_HOST=redis
      - REDIS_PORT=6379
    depends_on:
      - postgres
      - redis
    networks:
      - necpgame

  postgres:
    image: postgres:15
    environment:
      POSTGRES_USER: {service_name}
      POSTGRES_PASSWORD: password
      POSTGRES_DB: {service_name}
    ports:
      - "5432:5432"
    volumes:
      - postgres_data:/var/lib/postgresql/data
    networks:
      - necpgame

  redis:
    image: redis:7-alpine
    ports:
      - "6379:6379"
    networks:
      - necpgame

volumes:
  postgres_data:

networks:
  necpgame:
    driver: bridge
"""
        (service_dir / "docker-compose.yml").write_text(compose_content)

        print(f"[SUCCESS] Created basic structure for {service_name}")
        return True

    except Exception as e:
        print(f"[ERROR] Failed to create {service_name}: {e}")
        return False

def main():
    if len(sys.argv) < 2:
        print("Usage: python scripts/create-basic-service-structure.py <output_dir>")
        sys.exit(1)

    output_dir = Path(sys.argv[1])

    # Find all service specifications
    proto_dir = Path("proto/openapi")
    services = []

    for item in proto_dir.iterdir():
        if item.is_dir() and item.name.endswith("-service"):
            spec_path = item / "main.yaml"
            if spec_path.exists():
                services.append(item.name)

    print(f"Found {len(services)} services to create")

    success_count = 0
    for service_name in services:
        if create_service_structure(service_name, output_dir):
            success_count += 1

    print(f"\n[SUCCESS] Created {success_count}/{len(services)} services")

if __name__ == "__main__":
    main()














