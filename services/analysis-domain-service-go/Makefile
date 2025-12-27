# Issue: #generate-analysis-service
# Makefile for Analysis Domain Service
# Enterprise-grade build and deployment automation

.PHONY: help build run test clean docker-build docker-run docker-push deploy lint format

# Default target
help: ## Show this help message
	@echo 'Analysis Domain Service - Enterprise Build System'
	@echo ''
	@echo 'Available targets:'
	@awk 'BEGIN {FS = ":.*?## "} /^[a-zA-Z_-]+:.*?## / {printf "  %-15s %s\n", $$1, $$2}' $(MAKEFILE_LIST)

# Development targets
build: ## Build the application
	@echo 'Building Analysis Domain Service...'
	go build -o bin/analysis-service main.go

run: ## Run the application locally
	@echo 'Starting Analysis Domain Service...'
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
	docker build -t analysis-domain-service:latest .
	docker tag analysis-domain-service:latest analysis-domain-service:1.0.0

docker-run: ## Run Docker container locally
	@echo 'Running Docker container...'
	docker run -p 8080:8080 --env-file .env analysis-domain-service:latest

docker-push: ## Push Docker image to registry
	@echo 'Pushing Docker image...'
	docker push analysis-domain-service:latest
	docker push analysis-domain-service:1.0.0

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
	kubectl rollout restart deployment/analysis-domain-service

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
generate: ## Generate code from OpenAPI specs
	@echo 'Regenerating OpenAPI code...'
	ogen --target . --package api bundled.yaml

# Database
db-migrate: ## Run database migrations
	@echo 'Running database migrations...'
	# Add migration commands here

# Monitoring
logs: ## Show application logs
	@echo 'Showing application logs...'
	kubectl logs -f deployment/analysis-domain-service

status: ## Show deployment status
	@echo 'Checking deployment status...'
	kubectl get pods -l app=analysis-domain-service
	kubectl get svc -l app=analysis-domain-service

# Security
security-scan: ## Run security scan
	@echo 'Running security scan...'
	trivy image analysis-domain-service:latest

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
