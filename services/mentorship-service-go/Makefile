# Makefile for mentorship-service-go
.PHONY: build run test clean deps generate-api fmt lint docker-build docker-run

# Define the domain for API generation
DOMAIN_PATH=specialized-domain/mentorship-service-go

# Build the service
build:
	go build -o bin/mentorship-service-go .

# Run the service
run:
	go run main.go

# Run tests
test:
	go test ./...

# Clean build artifacts
clean:
	rm -rf bin/

# Install dependencies
deps:
	go mod tidy
	go mod download

# Generate API code (if spec changes)
generate-api:
	npx --yes @redocly/cli bundle ../../proto/openapi/$(DOMAIN_PATH)/main.yaml -o openapi-bundled.yaml
	ogen --target pkg/api --package api --clean openapi-bundled.yaml

# Format code
fmt:
	go fmt ./...

# Lint code
lint:
	golangci-lint run

# Docker build
docker-build:
	docker build -t mentorship-service-go .

# Docker run
docker-run:
	docker run -p 8081:8081 mentorship-service-go

# Development run with hot reload
dev:
	go run github.com/cosmtrek/air -c .air.toml



