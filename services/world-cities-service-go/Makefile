# World Cities Service Makefile
# Issue: #140875381

.PHONY: build run test clean docker-build docker-run migrate-up migrate-down

# Переменные
SERVICE_NAME=world-cities-service
DOCKER_IMAGE=necpgame/$(SERVICE_NAME):latest
GO_VERSION=1.21

# Сборка приложения
build:
	@echo "Building $(SERVICE_NAME)..."
	go build -o bin/$(SERVICE_NAME) ./main.go

# Запуск приложения
run: build
	@echo "Starting $(SERVICE_NAME)..."
	./bin/$(SERVICE_NAME)

# Запуск в режиме разработки
dev:
	@echo "Starting $(SERVICE_NAME) in development mode..."
	go run main.go

# Тестирование
test:
	@echo "Running tests..."
	go test -v -race -cover ./...

# Очистка
clean:
	@echo "Cleaning build artifacts..."
	rm -rf bin/
	go clean

# Docker сборка
docker-build:
	@echo "Building Docker image..."
	docker build -t $(DOCKER_IMAGE) .

# Docker запуск
docker-run: docker-build
	@echo "Running Docker container..."
	docker run --rm -p 8089:8089 \
		-e DATABASE_URL="postgres://necpgame:necpgame@localhost:5432/necpgame?sslmode=disable" \
		-e JWT_SECRET="your-super-secret-jwt-key-change-in-production" \
		-e SERVER_PORT=8089 \
		$(DOCKER_IMAGE)

# Миграции базы данных (предполагается наличие liquibase)
migrate-up:
	@echo "Running database migrations..."
	liquibase --changeLogFile=infrastructure/liquibase/migrations/V2_9__world_cities_system_tables.sql update

migrate-down:
	@echo "Rolling back database migrations..."
	liquibase --changeLogFile=infrastructure/liquibase/migrations/V2_9__world_cities_system_tables.sql rollbackCount 1

# Линтинг и форматирование
lint:
	@echo "Running linter..."
	golangci-lint run

fmt:
	@echo "Formatting code..."
	go fmt ./...

# Зависимости
deps:
	@echo "Downloading dependencies..."
	go mod download
	go mod tidy

# Генерация кода (если есть)
generate:
	@echo "Generating code..."
	go generate ./...

# Health check
health-check:
	@echo "Checking service health..."
	curl -f http://localhost:8089/health || echo "Service is not healthy"

# Логи
logs:
	@echo "Showing recent logs..."
	docker logs $(SERVICE_NAME) 2>/dev/null || echo "No Docker container logs available"

# Полная установка и запуск
install: deps build
	@echo "$(SERVICE_NAME) installation completed"

# CI/CD команды
ci: deps lint test build
	@echo "CI pipeline completed successfully"

# Development helpers
watch:
	@echo "Watching for file changes..."
	@if command -v air >/dev/null 2>&1; then \
		air; \
	else \
		echo "air not installed. Install with: go install github.com/cosmtrek/air@latest"; \
		exit 1; \
	fi

# Профилирование
profile:
	@echo "Starting profiling server..."
	go tool pprof http://localhost:8089/debug/pprof/profile

# Помощь
help:
	@echo "Available commands:"
	@echo "  build         - Build the application"
	@echo "  run           - Build and run the application"
	@echo "  dev           - Run in development mode"
	@echo "  test          - Run tests"
	@echo "  clean         - Clean build artifacts"
	@echo "  docker-build  - Build Docker image"
	@echo "  docker-run    - Run Docker container"
	@echo "  migrate-up    - Run database migrations"
	@echo "  migrate-down  - Rollback database migrations"
	@echo "  lint          - Run linter"
	@echo "  fmt           - Format code"
	@echo "  deps          - Download dependencies"
	@echo "  generate      - Generate code"
	@echo "  health-check  - Check service health"
	@echo "  logs          - Show Docker logs"
	@echo "  install       - Full installation"
	@echo "  ci            - CI/CD pipeline"
	@echo "  watch         - Watch for file changes (requires air)"
	@echo "  profile       - Run profiling"
	@echo "  help          - Show this help"
