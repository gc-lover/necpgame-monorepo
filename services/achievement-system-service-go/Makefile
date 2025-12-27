# Achievement System Service Makefile

.PHONY: help build run test clean docker-build docker-run lint fmt deps

# Default target
help: ## Show this help message
	@echo 'Usage: make [target]'
	@echo ''
	@echo 'Targets:'
	@awk 'BEGIN {FS = ":.*?## "} /^[a-zA-Z_-]+:.*?## / {printf "  %-15s %s\n", $$1, $$2}' $(MAKEFILE_LIST)

# Development
deps: ## Download dependencies
	go mod download
	go mod tidy

fmt: ## Format code
	go fmt ./...

lint: ## Run linter
	golangci-lint run

test: ## Run tests
	go test -v -race -coverprofile=coverage.out ./...

test-coverage: test ## Run tests with coverage
	go tool cover -html=coverage.out -o coverage.html

build: ## Build the application
	CGO_ENABLED=0 GOOS=linux go build -a -installsuffix cgo -o bin/achievement-system-service .

run: ## Run the application
	go run main.go

clean: ## Clean build artifacts
	rm -rf bin/
	rm -f coverage.out coverage.html

# Docker
docker-build: ## Build Docker image
	docker build -t achievement-system-service:latest .

docker-run: ## Run Docker container
	docker run -p 8080:8080 \
		-e DATABASE_URL="postgres://user:password@localhost:5432/achievement_system?sslmode=disable" \
		-e REDIS_URL="redis://localhost:6379" \
		-e JWT_SECRET="your-secret-key-change-in-production" \
		achievement-system-service:latest

docker-compose-up: ## Start services with docker-compose
	docker-compose up -d

docker-compose-down: ## Stop services with docker-compose
	docker-compose down

# Database
db-migrate: ## Run database migrations
	@echo "Running Liquibase migrations..."
	liquibase --changeLogFile=../../infrastructure/liquibase/changelog.xml update

db-rollback: ## Rollback last migration
	@echo "Rolling back last migration..."
	liquibase --changeLogFile=../../infrastructure/liquibase/changelog.xml rollbackCount 1

# Kubernetes
k8s-deploy: ## Deploy to Kubernetes
	kubectl apply -f k8s/

k8s-undeploy: ## Remove from Kubernetes
	kubectl delete -f k8s/

# CI/CD
ci: deps fmt lint test build ## Run CI pipeline

# Development helpers
dev-setup: deps ## Setup development environment
	@echo "Development environment setup complete"

logs: ## Show application logs (when running in container)
	docker logs -f achievement-system-service

# Performance testing
bench: ## Run benchmarks
	go test -bench=. -benchmem ./...

# Security
security-scan: ## Run security scan
	gosec ./...

# All-in-one development start
dev: clean deps fmt lint test build docker-build ## Full development cycle
	@echo "Development cycle complete. Run 'make run' to start the service."
