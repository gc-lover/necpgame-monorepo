# Issue: #136
.PHONY: build clean test run docker-build docker-run generate migrate deps

# Build the service
build:
	go build -o bin/auth-service ./main.go

# Clean build artifacts
clean:
	rm -rf bin/
	go clean

# Run tests
test:
	go test -v ./...

# Run the service locally
run: build
	./bin/auth-service

# Generate OpenAPI code
generate:
	go run github.com/ogen-go/ogen/cmd/ogen@latest --target pkg/api --package api ../../proto/openapi/auth-service.yaml

# Install dependencies
deps:
	go mod download
	go mod tidy

# Docker build
docker-build:
	docker build -t necpgame/auth-service:latest .

# Docker run
docker-run:
	docker run -p 8081:8081 --env-file .env necpgame/auth-service:latest

# Database migration (requires liquibase)
migrate:
	@echo "Running database migrations..."
	# liquibase --changeLogFile=../../infrastructure/liquibase/auth-service-changelog.xml update

# Lint code
lint:
	golangci-lint run

# Format code
fmt:
	go fmt ./...

# Vet code
vet:
	go vet ./...

# All checks
check: fmt vet lint test

# Development setup
dev-setup: deps generate
	@echo "Development environment ready"

# Production build
prod-build:
	CGO_ENABLED=0 GOOS=linux go build -a -installsuffix cgo -o bin/auth-service-linux ./main.go