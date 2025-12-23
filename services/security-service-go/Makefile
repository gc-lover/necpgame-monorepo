# Security Service Makefile
.PHONY: all build test clean docker-build docker-run lint fmt vet mod-tidy

# Go parameters
GOCMD=go
GOBUILD=$(GOCMD) build
GOCLEAN=$(GOCMD) clean
GOTEST=$(GOCMD) test
GOGET=$(GOCMD) get
GOMOD=$(GOCMD) mod
BINARY_NAME=security-service
BINARY_UNIX=$(BINARY_NAME)_unix

# Build the project
all: clean mod-tidy fmt vet lint test build

build:
	$(GOBUILD) -o $(BINARY_NAME) -v ./...

test:
	$(GOTEST) -v -race -coverprofile=coverage.out ./...

clean:
	$(GOCLEAN)
	rm -f $(BINARY_NAME)
	rm -f $(BINARY_UNIX)
	rm -f coverage.out

# Run the service
run:
	$(GOCMD) run .

# Development helpers
fmt:
	$(GOCMD) fmt ./...

vet:
	$(GOCMD) vet ./...

lint:
	golangci-lint run

mod-tidy:
	$(GOMOD) tidy

# Docker commands
docker-build:
	docker build -t security-service:latest .

docker-run:
	docker run -p 8080:8080 --env-file .env security-service:latest

docker-compose-up:
	docker-compose up --build

docker-compose-down:
	docker-compose down

# Profiling
profile:
	$(GOCMD) build -o $(BINARY_NAME) -v ./...
	./$(BINARY_NAME) &
	sleep 2
	go tool pprof http://localhost:6555/debug/pprof/profile
	kill %1

# Code generation
generate:
	$(GOCMD) generate ./...

# Database migrations (placeholder - implement as needed)
db-migrate-up:
	@echo "Running database migrations..."
	# Add migration commands here

db-migrate-down:
	@echo "Rolling back database migrations..."
	# Add migration commands here

# Testing helpers
test-integration:
	$(GOTEST) -v -tags=integration ./...

test-unit:
	$(GOTEST) -v -short ./...

# Security scanning
security-scan:
	gosec ./...

# Benchmarking
bench:
	$(GOTEST) -bench=. -benchmem ./...

# Cross compilation
build-linux:
	CGO_ENABLED=0 GOOS=linux GOARCH=amd64 $(GOBUILD) -o $(BINARY_UNIX) -v

# Docker multi-stage build
docker-build-multi:
	docker build --target production -t security-service:prod .

# Health check
health-check:
	curl -f http://localhost:8080/health || exit 1

# Logs
logs:
	docker-compose logs -f security-service
