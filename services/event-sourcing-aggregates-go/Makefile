# Issue: #2217
# Makefile for Event Sourcing Aggregates Service

.PHONY: build test clean docker-build docker-run deploy

# Build the application
build:
	go build -o bin/event-sourcing-aggregates main.go

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
	docker build -t event-sourcing-aggregates:latest .

# Run Docker container
docker-run:
	docker run -p 8082:8082 \
		-e DATABASE_URL="postgres://user:password@localhost:5432/necpgame?sslmode=disable" \
		-e REDIS_URL="redis://localhost:6379" \
		-e KAFKA_BROKERS="localhost:9092" \
		-e JWT_SECRET="your-secret-key" \
		event-sourcing-aggregates:latest

# Run with docker-compose
docker-compose-up:
	docker-compose up --build

# Deploy to Kubernetes
deploy:
	kubectl apply -f k8s/

# Logs
logs:
	kubectl logs -f deployment/event-sourcing-aggregates

# Health check
health:
	curl -f http://localhost:8082/health

# Metrics
metrics:
	curl -f http://localhost:8082/metrics

# All-in-one build and test
all: fmt lint test build

# Development setup
dev-setup:
	go mod tidy
	go mod download

# Generate mocks (if needed)
mocks:
	@echo "No mocks needed for this service"