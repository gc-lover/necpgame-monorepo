# Makefile for realtime-gateway-service-go

.PHONY: help build test clean generate-proto generate-all docker-build docker-run

# Default target
help:
	@echo "Available targets:"
	@echo "  build         - Build the service"
	@echo "  test          - Run tests"
	@echo "  clean         - Clean build artifacts"
	@echo "  generate-proto- Generate protobuf code from .proto files"
	@echo "  generate-all  - Generate all code (protobuf, etc.)"
	@echo "  docker-build  - Build Docker image"
	@echo "  docker-run    - Run Docker container"
	@echo "  run           - Run the service locally"

# Build the service
build:
	go build -o bin/realtime-gateway-service ./main.go

# Run tests
test:
	go test -v ./...

# Clean build artifacts
clean:
	rm -rf bin/
	go clean

# Generate protobuf code from .proto files
generate-proto:
	@echo "Generating protobuf code..."
	@command -v protoc >/dev/null 2>&1 || { echo "protoc not found. Please install Protocol Buffers compiler."; exit 1; }
	protoc --go_out=. --go_opt=paths=source_relative \
		--proto_path=../.. \
		proto/realtime/realtime.proto \
		proto/realtime/network-messages.proto \
		proto/realtime/game-state.proto \
		proto/realtime/voice-chat.proto \
		proto/realtime/network-config.proto \
		proto/realtime/udp-reliability.proto
	@echo "Protobuf code generated successfully"

# Generate all code (protobuf, etc.)
generate-all: generate-proto
	@echo "All code generation completed"

# Build Docker image
docker-build:
	docker build -t realtime-gateway-service-go .

# Run Docker container
docker-run:
	docker run --rm -p 8086:8086 -p 8087:8087 -p 7777:7777/udp realtime-gateway-service-go

# Run the service locally
run: build
	./bin/realtime-gateway-service

# Development setup
dev-setup:
	go mod tidy
	go install google.golang.org/protobuf/cmd/protoc-gen-go@latest
	@echo "Development environment setup complete"
	@echo "Make sure protoc is installed: https://grpc.io/docs/protoc-installation/"

# Install protoc (Windows helper)
install-protoc-windows:
	@echo "Installing protoc on Windows..."
	@echo "Please download from: https://github.com/protocolbuffers/protobuf/releases"
	@echo "Extract to C:\protoc and add C:\protoc\bin to PATH"
	@echo "Then run: go install google.golang.org/protobuf/cmd/protoc-gen-go@latest"