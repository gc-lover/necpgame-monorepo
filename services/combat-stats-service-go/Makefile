# Issue: #2250
# Makefile for Combat Stats Service

.PHONY: build test clean docker-build docker-run deploy

# Build the application
build:
	go build -o bin/combat-stats-service main.go

# Run tests
test:
	go test ./...

# Run tests with coverage
test-coverage:
	go test -coverprofile=coverage.out ./...
	go tool cover -html=coverage.out -o coverage.html

# Clean build artifacts
clean:
	rm -rf bin/
	rm -f coverage.out coverage.html

# Format code
fmt:
	go fmt ./...

# Lint code
lint:
	golangci-lint run

# Run locally
run:
	go run main.go

# Build Docker image
docker-build:
	docker build -t combat-stats-service:latest .

# Run Docker container
docker-run:
	docker run -p 8080:8080 \
		-e DATABASE_URL="postgres://user:password@localhost:5432/necpgame?sslmode=disable" \
		-e REDIS_URL="redis://localhost:6379" \
		-e JWT_SECRET="your-secret-key" \
		combat-stats-service:latest

# Run with docker-compose
docker-compose-up:
	docker-compose up --build

# Deploy to Kubernetes
deploy:
	kubectl apply -f k8s/

# Logs
logs:
	kubectl logs -f deployment/combat-stats-service

# Health check
health:
	curl -f http://localhost:8080/health

# Metrics
metrics:
	curl -f http://localhost:8080/metrics

# All-in-one build and test
all: fmt lint test build

# Development setup
dev-setup:
	go mod tidy
	go mod download

# Generate mocks (if needed)
mocks:
	@echo "No mocks needed for this service"