# World Events Service Makefile

.PHONY: help build test clean docker-build docker-push deploy lint vet fmt mod-tidy run dev deps

# Variables
SERVICE_NAME=world-events-service
DOCKER_IMAGE=necpgame/$(SERVICE_NAME)
VERSION?=latest
GO_FILES=$(shell find . -name '*.go' -not -path './vendor/*')

# Default target
help: ## Show this help message
	@echo "$(SERVICE_NAME) - World Events Service"
	@echo ""
	@echo "Available targets:"
	@grep -E '^[a-zA-Z_-]+:.*?## .*$$' $(MAKEFILE_LIST) | sort | awk 'BEGIN {FS = ":.*?## "}; {printf "  %-20s %s\n", $$1, $$2}'

# Development
deps: ## Download dependencies
	go mod download
	go mod tidy

fmt: ## Format Go code
	gofmt -s -w $(GO_FILES)

vet: ## Run go vet
	go vet ./...

lint: ## Run golangci-lint
	golangci-lint run

mod-tidy: ## Clean up go modules
	go mod tidy

# Testing
test: ## Run all tests
	go test -v -race -coverprofile=coverage.out ./...

test-short: ## Run tests without race detector
	go test -v -coverprofile=coverage.out ./...

test-coverage: ## Run tests with coverage report
	go test -v -race -coverprofile=coverage.out ./...
	go tool cover -html=coverage.out -o coverage.html
	@echo "Coverage report generated: coverage.html"

bench: ## Run benchmarks
	go test -bench=. -benchmem ./...

# Building
build: ## Build the service binary
	@echo "Building $(SERVICE_NAME)..."
	CGO_ENABLED=0 GOOS=linux go build \
		-a -installsuffix cgo \
		-ldflags="-w -s -X main.version=$(VERSION)" \
		-o bin/$(SERVICE_NAME) \
		.

build-local: ## Build for local development
	go build -o bin/$(SERVICE_NAME)-local .

# Docker
docker-build: ## Build Docker image
	docker build -t $(DOCKER_IMAGE):$(VERSION) .
	docker tag $(DOCKER_IMAGE):$(VERSION) $(DOCKER_IMAGE):latest

docker-push: ## Push Docker image to registry
	docker push $(DOCKER_IMAGE):$(VERSION)
	docker push $(DOCKER_IMAGE):latest

docker-run: ## Run Docker container locally
	docker run -p 8086:8086 \
		-e DATABASE_URL="postgres://necpgame:necpgame@localhost:5432/necpgame?sslmode=disable" \
		-e JWT_SECRET="your-jwt-secret-key" \
		$(DOCKER_IMAGE):$(VERSION)

# Deployment
deploy: docker-build docker-push ## Build and push Docker image

# Development server
run: ## Run the service locally
	go run .

dev: ## Run in development mode with hot reload
	@if command -v air > /dev/null; then \
		air; \
	else \
		echo "Air not found. Install with: go install github.com/cosmtrek/air@latest"; \
		go run .; \
	fi

# Profiling
profile: ## Run pprof profiling server
	go tool pprof -http=:8087 http://localhost:6555/debug/pprof/profile

# Database
db-migrate: ## Run database migrations
	@echo "Running migrations..."
	# Add migration commands here

db-seed: ## Seed database with test data
	@echo "Seeding database..."
	# Add seeding commands here

# Cleanup
clean: ## Clean build artifacts
	rm -rf bin/
	rm -f coverage.out coverage.html

clean-all: clean ## Clean all artifacts including vendor
	rm -rf vendor/

# Quality checks
check: fmt vet lint test ## Run all quality checks

# CI/CD
ci: deps check build docker-build ## Run full CI pipeline

# Database operations for development
db-create: ## Create database
	@echo "Creating database..."
	createdb -h localhost -U necpgame necpgame

db-drop: ## Drop database
	@echo "Dropping database..."
	dropdb -h localhost -U necpgame necpgame

db-reset: db-drop db-create db-migrate db-seed ## Reset database completely

# Service operations
start: ## Start service with systemd (if configured)
	sudo systemctl start $(SERVICE_NAME)

stop: ## Stop service with systemd
	sudo systemctl stop $(SERVICE_NAME)

restart: ## Restart service with systemd
	sudo systemctl restart $(SERVICE_NAME)

status: ## Check service status with systemd
	sudo systemctl status $(SERVICE_NAME)

logs: ## Show service logs with journalctl
	sudo journalctl -u $(SERVICE_NAME) -f

# Monitoring
metrics: ## Show service metrics
	curl -s http://localhost:8086/metrics | jq .

health: ## Check service health
	curl -s http://localhost:8086/health | jq .

ready: ## Check service readiness
	curl -s http://localhost:8086/ready | jq .
