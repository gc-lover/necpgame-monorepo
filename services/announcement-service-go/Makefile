# Makefile for announcement-service-go
.PHONY: build run test clean deps generate-api fmt lint docker-build docker-run

# Build the service
build:
	go build -o bin/announcement-service-go .

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
	npx --yes @redocly/cli bundle ../../proto/openapi/system-domain/ai/content-domain/announcements/main.yaml -o openapi-bundled.yaml
	go run github.com/ogen-go/ogen/cmd/ogen --target pkg/api --package api --clean openapi-bundled.yaml

# Format code
fmt:
	go fmt ./...

# Lint code
lint:
	golangci-lint run

# Docker build
docker-build:
	docker build -t announcement-service-go .

# Docker run
docker run -p 8094:8094 announcement-service-go

# Development run with hot reload
dev:
	go run github.com/cosmtrek/air -c .air.toml




