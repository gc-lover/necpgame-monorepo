# Cyberware Service Makefile

.PHONY: help build test clean docker-build docker-push deploy lint vet fmt mod-tidy run dev deps

# Variables
SERVICE_NAME=cyberware-service
DOCKER_IMAGE=necpgame/$(SERVICE_NAME)
VERSION?=latest
GO_FILES=$(shell find . -name '*.go' -not -path './vendor/*')

# Default target
help: ## Show this help message
	@echo "$(SERVICE_NAME) - Advanced Cyberware Integration System"
	@echo ""
	@echo "Available targets:"
	@grep -E '^[a-zA-Z_-]+:.*?## .*$$' $(MAKEFILE_LIST) | sort | awk 'BEGIN {FS = ":.*?## "}; {printf "  %-20s %s\\n", $$1, $$2}'

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

test-integration: ## Run integration tests
	go test -v -tags=integration ./...

bench: ## Run benchmarks
	go test -bench=. -benchmem ./...

coverage: ## Show test coverage
	go tool cover -html=coverage.out -o coverage.html
	@echo "Coverage report generated: coverage.html"

# Building
build: ## Build the service
	CGO_ENABLED=0 GOOS=linux GOARCH=amd64 go build -o bin/$(SERVICE_NAME) .

build-local: ## Build for local development
	go build -o bin/$(SERVICE_NAME)-local .

# Docker
docker-build: ## Build Docker image
	docker build -t $(DOCKER_IMAGE):$(VERSION) .

docker-run: ## Run Docker container locally
	docker run --rm -p 8086:8086 \
		-e DATABASE_URL="postgres://user:password@localhost:5432/cyberware?sslmode=disable" \
		-e JWT_SECRET="your-jwt-secret" \
		-e SERVER_PORT="8086" \
		$(DOCKER_IMAGE):$(VERSION)

docker-push: ## Push Docker image
	docker push $(DOCKER_IMAGE):$(VERSION)

# Running
run: ## Run the service
	go run .

dev: ## Run in development mode with hot reload
	air

# Deployment
deploy: ## Deploy to production (customize for your environment)
	@echo "Deploying $(SERVICE_NAME)..."
	# Add your deployment commands here
	# kubectl apply -f k8s/
	# docker-compose up -d

# Cleanup
clean: ## Clean build artifacts
	rm -rf bin/
	rm -f coverage.out coverage.html

# Code quality
check: fmt vet lint ## Run all code quality checks

# CI/CD
ci: deps check test ## Run CI pipeline locally

# Database
db-migrate: ## Run database migrations (customize for your migration tool)
	@echo "Running database migrations..."
	# Add your migration commands here

# Profiling
profile: ## Run with profiling enabled
	go run -cpuprofile=cpu.prof -memprofile=mem.prof .

# Validation
validate: ## Validate service configuration
	@echo "Validating $(SERVICE_NAME)..."
	go run . --validate-config

# Health check
health-check: ## Check service health
	curl -f http://localhost:8086/health || exit 1

# Logs
logs: ## Show service logs (for local development)
	docker logs $(SERVICE_NAME) 2>/dev/null || echo "Service not running in Docker"

# All-in-one development setup
setup: deps ## Setup development environment
	@echo "$(SERVICE_NAME) development environment ready!"
	@echo "Run 'make dev' to start development server"
	@echo "Run 'make test' to run tests"
	@echo "Run 'make docker-build' to build Docker image"
