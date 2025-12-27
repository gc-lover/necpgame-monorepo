.PHONY: build run test clean docker-build docker-run

# Build the application
build:
	go build -o bin/combat-hacking-service main.go

# Run the application
run:
	go run main.go

# Run tests
test:
	go test ./...

# Clean build artifacts
clean:
	rm -rf bin/

# Build Docker image
docker-build:
	docker build -t combat-hacking-service-go .

# Run Docker container
docker-run:
	docker run -p 8084:8084 combat-hacking-service-go

# Development run with hot reload
dev:
	air

# Format code
fmt:
	go fmt ./...

# Lint code
lint:
	golangci-lint run

# Issue: #143875347, #143875814

