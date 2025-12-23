.PHONY: build run test clean docker-build docker-run generate-proto lint check-file-sizes validate-openapi

SERVICE_NAME := realtime-gateway-go
BINARY_NAME := $(SERVICE_NAME)
GO_FILES := $(shell find . -name "*.go" -not -path "./pkg/api/*")
PROTO_DIR := ../../proto/realtime
PROTO_FILES := $(PROTO_DIR)/movement.proto $(PROTO_DIR)/projectile.proto $(PROTO_DIR)/realtime.proto $(PROTO_DIR)/network-messages.proto $(PROTO_DIR)/network-config.proto $(PROTO_DIR)/udp-reliability.proto

# Default to current directory if not specified
PROJECT_ROOT := $(shell pwd)

# Go build
build: generate-proto
	@echo "Building $(SERVICE_NAME)..."
	go mod tidy
	go build -o $(BINARY_NAME) ./main.go

# Go run
run: build
	@echo "Running $(SERVICE_NAME)..."
	./$(BINARY_NAME)

# Go test
test:
	@echo "Running tests for $(SERVICE_NAME)..."
	go test -v ./...

# Clean generated files and binary
clean:
	@echo "Cleaning up..."
	go clean
	rm -f $(BINARY_NAME)
	rm -rf pkg/proto # Remove generated proto code

# Generate protobuf Go code
generate-proto:
	@echo "Generating protobuf Go code..."
	@mkdir -p pkg/proto/movement
	@mkdir -p pkg/proto/projectile
	@mkdir -p pkg/proto/realtime
	@mkdir -p pkg/proto/network

	@echo "Generating movement.proto..."
	@protoc --go_out=pkg/proto/movement --go_opt=paths=source_relative $(PROTO_DIR)/movement.proto

	@echo "Generating projectile.proto..."
	@protoc --go_out=pkg/proto/projectile --go_opt=paths=source_relative $(PROTO_DIR)/projectile.proto

	@echo "Generating realtime.proto..."
	@protoc --go_out=pkg/proto/realtime --go_opt=paths=source_relative $(PROTO_DIR)/realtime.proto

	@echo "Generating network proto files..."
	@protoc --go_out=pkg/proto/network --go_opt=paths=source_relative $(PROTO_DIR)/network-messages.proto $(PROTO_DIR)/network-config.proto $(PROTO_DIR)/udp-reliability.proto

	@echo "[OK] Protobuf generation complete!"

# Docker build
docker-build:
	@echo "Building Docker image for $(SERVICE_NAME)..."
	docker build -t $(SERVICE_NAME):latest .

# Docker run
docker-run: docker-build
	@echo "Running Docker container for $(SERVICE_NAME)..."
	docker run --rm -p 8080:8080 --name $(SERVICE_NAME) $(SERVICE_NAME):latest

# Lint Go files
lint:
	@echo "Running golangci-lint..."
	golangci-lint run ./...

# Validate proto files
validate-proto:
	@echo "Validating proto files..."
	@for proto in $(PROTO_FILES); do \
		echo "  Checking $$proto..."; \
		protoc --descriptor_set_out=/dev/null $$proto || exit 1; \
	done
	@echo "[OK] All proto files valid"

# Check file sizes (excluding generated files)
check-file-sizes:
	@echo "Checking file sizes..."
	@python ../../scripts/check-file-sizes.py $(PROJECT_ROOT) --exclude="pkg/proto"

# Default target
all: build

# Help target
help:
	@echo "Available targets:"
	@echo "  build          - Build the service (includes proto generation)"
	@echo "  run            - Build and run the service"
	@echo "  test           - Run tests"
	@echo "  clean          - Clean generated files and binary"
	@echo "  generate-proto - Generate protobuf Go code"
	@echo "  docker-build   - Build Docker image"
	@echo "  docker-run     - Run Docker container"
	@echo "  lint           - Run linter"
	@echo "  validate-proto - Validate proto files"
	@echo "  check-file-sizes - Check file sizes"
	@echo "  help           - Show this help"
