# Makefile for ml-ai-domain service

.PHONY: build run test clean deps

# Build the service
build:
	go build -o ml-ai-service .

# Run the service
run:
	go run main.go

# Run tests
test:
	go test ./...

# Clean build artifacts
clean:
	rm -rf ml-ai-service

# Install dependencies
deps:
	go mod tidy
	go mod download

# Format code
fmt:
	go fmt ./...

# Generate API code (if spec changes)
generate-api:
	npx --yes @redocly/cli bundle ../../proto/openapi/ml-ai-domain/main.yaml -o bundled.yaml
	ogen --target api --package api --clean bundled.yaml
