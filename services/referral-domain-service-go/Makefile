.PHONY: build run test clean docker-build docker-run

# Build the application
build:
	go build -o bin/referral-service ./main.go

# Run the application
run:
	go run ./main.go

# Run tests
test:
	go test -v ./...

# Clean build artifacts
clean:
	rm -rf bin/

# Build Docker image
docker-build:
	docker build -t referral-domain-service .

# Run Docker container
docker-run:
	docker run -p 8080:8080 --env-file .env referral-domain-service

# Run with hot reload (requires air)
dev:
	air

# Lint code
lint:
	golangci-lint run

# Format code
fmt:
	go fmt ./...

# Run migrations (placeholder)
migrate-up:
	@echo "Running migrations..."
	# Add migration commands here

migrate-down:
	@echo "Rolling back migrations..."
	# Add migration rollback commands here
