# Makefile for crafting-service-go

.PHONY: build run test clean deps generate-api fmt lint docker-build docker-run

# Build the service
build:
	go build -o bin/crafting-service-go .

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

# Generate API code from OpenAPI spec
generate-api:
	npx --yes @redocly/cli bundle ../../../proto/openapi/specialized-domain/crafting/main.yaml -o openapi-bundled.yaml
	ogen --target pkg/api --package api --clean openapi-bundled.yaml

# Format code
fmt:
	go fmt ./...

# Lint code
lint:
	golangci-lint run

# Docker build
docker-build:
	docker build -t crafting-service-go .

# Docker run
docker-run:
	docker run -p 8210:8210 crafting-service-go

