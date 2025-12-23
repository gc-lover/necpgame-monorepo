# Makefile for legend-templates-service-go
.PHONY: build run test clean deps

# Build the service
build:
	go build -o bin/legend-templates-service-go .

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
	npx --yes @redocly/cli bundle ../../proto/openapi/social-domain/legend-templates/legend-templates-service-go/main.yaml -o openapi-bundled.yaml
	ogen --target pkg/api --package api --clean openapi-bundled.yaml

# Format code
fmt:
	go fmt ./...

# Lint code
lint:
	golangci-lint run

# Docker build
docker-build:
	docker build -t legend-templates-service-go .

# Docker run
docker-run:
	docker run -p 8080:8080 legend-templates-service-go