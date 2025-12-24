# Issue: #1499
.PHONY: all build test clean run fmt vet lint mod-tidy docker-build docker-run docker-compose-up docker-compose-down profile generate db-migrate-up db-migrate-down test-integration test-unit security-scan bench build-linux docker-build-multi health-check logs

# Default target
all: check-file-sizes build test

# Build the application
build:
	go build -o bin/gameplay-restricted-modes-service ./main.go

# Run tests
test: test-unit test-integration

# Unit tests
test-unit:
	go test ./... -v -race -coverprofile=coverage.out

# Integration tests
test-integration:
	go test ./... -tags=integration -v

# Clean build artifacts
clean:
	rm -rf bin/
	rm -f coverage.out
	go clean

# Run the application
run: build
	./bin/gameplay-restricted-modes-service

# Format code
fmt:
	go fmt ./...

# Vet code
vet:
	go vet ./...

# Lint code
lint:
	golangci-lint run

# Tidy dependencies
mod-tidy:
	go mod tidy

# Check file sizes (must be <1000 lines except generated files)
check-file-sizes:
	@echo "Checking file sizes..."
	@find . -name "*.go" -not -path "./pkg/api/*" -not -path "./vendor/*" -exec wc -l {} \; | awk '$$1 > 1000 {print "ERROR: " $$2 " has " $$1 " lines (>1000)"; exit 1}'

# Docker build
docker-build:
	docker build -t gameplay-restricted-modes-service:latest .

# Docker run
docker-run: docker-build
	docker run -p 8080:8080 --env-file .env gameplay-restricted-modes-service:latest

# Docker compose up
docker-compose-up:
	docker-compose up --build

# Docker compose down
docker-compose-down:
	docker-compose down

# Profiling
profile: build
	@echo "Starting profiling server on port 6555..."
	./bin/gameplay-restricted-modes-service &
	@echo "Application started. Use 'go tool pprof http://localhost:6555/debug/pprof/profile' to profile"

# Generate API code
generate:
	npx --yes @redocly/cli bundle openapi-bundled.yaml -o openapi-bundled-updated.yaml
	ogen --target pkg/api --package api --clean openapi-bundled-updated.yaml

# Database migrations (placeholders - implement based on your migration tool)
db-migrate-up:
	@echo "Running database migrations up..."
	# liquibase update

db-migrate-down:
	@echo "Running database migrations down..."
	# liquibase rollback

# Security scan
security-scan:
	gosec ./...
	trivy fs .

# Benchmarks
bench:
	go test -bench=. -benchmem ./...

# Build for Linux
build-linux:
	GOOS=linux GOARCH=amd64 go build -o bin/gameplay-restricted-modes-service-linux ./main.go

# Multi-platform Docker build
docker-build-multi:
	docker buildx build --platform linux/amd64,linux/arm64 -t gameplay-restricted-modes-service:multi .

# Health check
health-check:
	curl -f http://localhost:8080/health || exit 1

# Logs (for docker-compose)
logs:
	docker-compose logs -f

# Development setup
dev-setup: mod-tidy
	go install github.com/golangci/golangci-lint/cmd/golangci-lint@latest
	go install github.com/securecodewarrior/gosec/v2/cmd/gosec@latest
	go install github.com/aquasecurity/trivy/cmd/trivy@latest

# CI pipeline
ci: mod-tidy fmt vet lint security-scan test build

# Production build
prod-build: mod-tidy fmt vet lint security-scan test build-linux docker-build

