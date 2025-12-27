# Issue: #task-2256
# Makefile for Faction Domain Service
# Enterprise-grade build and deployment automation

.PHONY: help build run test clean docker-build docker-run docker-push deploy lint format

# Default target
help: ## Show this help message
	@echo 'Faction Domain Service - Enterprise Build System'
	@echo ''
	@echo 'Available targets:'
	@awk 'BEGIN {FS = ":.*?## "} /^[a-zA-Z_-]+:.*?## / {printf "  %-15s %s\n", $$1, $$2}' $(MAKEFILE_LIST)

# Development targets
build: ## Build the application
	@echo 'Building Faction Domain Service...'
	go build -o bin/faction-service main.go

run: ## Run the application locally
	@echo 'Starting Faction Domain Service...'
	go run main.go

test: ## Run tests
	@echo 'Running tests...'
	go test -v -race -coverprofile=coverage.out ./...

clean: ## Clean build artifacts
	@echo 'Cleaning build artifacts...'
	rm -rf bin/
	rm -f coverage.out
	go clean

# Docker targets
docker-build: ## Build Docker image
	@echo 'Building Docker image...'
	docker build -t faction-domain-service:latest .
	docker tag faction-domain-service:latest faction-domain-service:1.0.0

docker-run: ## Run Docker container locally
	@echo 'Running Docker container...'
	docker run -p 8080:8080 --env-file .env faction-domain-service:latest

docker-push: ## Push Docker image to registry
	@echo 'Pushing Docker image...'
	docker push faction-domain-service:latest
	docker push faction-domain-service:1.0.0

# Development tools
lint: ## Run linters
	@echo 'Running linters...'
	golangci-lint run

format: ## Format code
	@echo 'Formatting code...'
	go fmt ./...
	goimports -w .

# Deployment
deploy: docker-build docker-push ## Build and deploy to production
	@echo 'Deploying to production...'
	kubectl apply -f k8s/
	kubectl rollout restart deployment/faction-domain-service

# Dependencies
deps: ## Download dependencies
	@echo 'Downloading dependencies...'
	go mod download
	go mod tidy

deps-update: ## Update dependencies
	@echo 'Updating dependencies...'
	go get -u ./...
	go mod tidy

# Testing
test-integration: ## Run integration tests
	@echo 'Running integration tests...'
	go test -v -tags=integration ./...

test-benchmark: ## Run benchmarks
	@echo 'Running benchmarks...'
	go test -bench=. -benchmem ./...

# Code generation
generate: ## Regenerate OpenAPI code
	@echo 'Regenerating OpenAPI code...'
	ogen --target api --package api bundled.yaml

# Database
db-migrate: ## Run database migrations
	@echo 'Running database migrations...'
	# Add migration commands here

# Monitoring
logs: ## Show application logs
	@echo 'Showing application logs...'
	kubectl logs -f deployment/faction-domain-service

status: ## Show deployment status
	@echo 'Checking deployment status...'
	kubectl get pods -l app=faction-domain-service
	kubectl get svc -l app=faction-domain-service

# Security
security-scan: ## Run security scan
	@echo 'Running security scan...'
	trivy image faction-domain-service:latest

# Performance
profile: ## Run performance profiling
	@echo 'Running performance profiling...'
	go tool pprof http://localhost:8080/debug/pprof/profile

# Environment setup
setup: ## Setup development environment
	@echo 'Setting up development environment...'
	go mod download
	go install github.com/golangci/golangci-lint/cmd/golangci-lint@latest
	go install golang.org/x/tools/cmd/goimports@latest

# CI/CD
ci: lint test docker-build ## Run CI pipeline
	@echo 'CI pipeline completed successfully'

cd: docker-push deploy ## Run CD pipeline
	@echo 'CD pipeline completed successfully'
