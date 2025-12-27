# Issue: #2262 - Cyberspace Easter Eggs Service Makefile
# Enterprise-grade build, test, and deployment automation

.PHONY: all build test clean docker-build docker-run deps lint format

# Default target
all: deps build test

# Dependencies
deps:
	go mod tidy
	go mod download

# Build the service
build:
	go build -o cyberspace-easter-eggs-service .

# Run tests
test:
	go test -v ./...

# Clean build artifacts
clean:
	rm -f cyberspace-easter-eggs-service
	go clean

# Run linter
lint:
	golangci-lint run

# Format code
format:
	go fmt ./...

# Build Docker image
docker-build:
	docker build -t cyberspace-easter-eggs-service:latest .

# Run Docker container
docker-run:
	docker run -p 8080:8080 --env-file .env cyberspace-easter-eggs-service:latest

# Run service locally
run: build
	./cyberspace-easter-eggs-service

# Development run with live reload
dev:
	air

# Generate mocks (if needed)
mocks:
	mockgen -source=internal/service/service.go -destination=internal/service/mocks/service_mock.go -package=mocks

# Database migration
migrate-up:
	migrate -path ./migrations -database $(DATABASE_URL) up

migrate-down:
	migrate -path ./migrations -database $(DATABASE_URL) down

# Import data
import-data:
	python3 ../../scripts/import-cyberspace-easter-eggs.py

# Health check
health-check:
	curl -f http://localhost:8080/health || exit 1

# Load test
load-test:
	vegeta attack -duration=30s -rate=100/s -targets=load-test-targets.txt | vegeta report

# Security scan
security-scan:
	gosec ./...

# Generate API docs
docs:
	swag init -g main.go -o docs

# CI/CD pipeline
ci: deps lint test build docker-build

# Production deployment
deploy: docker-build
	kubectl apply -f k8s/

# Logs
logs:
	kubectl logs -f deployment/cyberspace-easter-eggs-service

# Monitoring
monitor:
	open http://localhost:9090
