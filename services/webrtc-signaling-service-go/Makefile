# Makefile for WebRTC Signaling Service

.PHONY: build run test clean deps docker-build docker-run k8s-deploy

# Build the service
build:
	go build -o bin/webrtc-signaling-service .

# Run the service
run:
	go run .

# Run tests
test:
	go test ./...

# Run integration tests
test-integration:
	go test -tags=integration ./tests/

# Clean build artifacts
clean:
	rm -rf bin/

# Install dependencies
deps:
	go mod tidy
	go mod download

# Format code
fmt:
	go fmt ./...

# Lint code
lint:
	golangci-lint run

# Run locally with air (hot reload)
dev:
	air

# Build Docker image
docker-build:
	docker build -t webrtc-signaling-service .

# Run Docker container
docker-run:
	docker run -p 8080:8080 --env-file .env webrtc-signaling-service

# Docker compose up
docker-up:
	docker-compose up -d

# Docker compose down
docker-down:
	docker-compose down

# Kubernetes deploy
k8s-deploy:
	kubectl apply -f k8s/

# Performance benchmark
bench:
	go test -bench=. -benchmem

# Profile CPU
profile-cpu:
	go tool pprof -http=:8081 cpu.prof

# Profile memory
profile-mem:
	go tool pprof -http=:8082 mem.prof

# Health check
health:
	curl -f http://localhost:8080/health

# All-in-one setup
setup: deps fmt lint build docker-build