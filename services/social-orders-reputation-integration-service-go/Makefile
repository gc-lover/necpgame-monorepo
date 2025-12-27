# Social Orders Reputation Integration Service Makefile
# Issue: #140894823

.PHONY: build run test clean docker-build docker-run lint fmt

# Build the service
build:
	go build -o bin/social-orders-reputation-integration-service ./main.go

# Run the service
run:
	go run ./main.go

# Run tests
test:
	go test ./...

# Clean build artifacts
clean:
	rm -rf bin/

# Build Docker image
docker-build:
	docker build -t social-orders-reputation-integration-service .

# Run Docker container
docker-run:
	docker run -p 8080:8080 social-orders-reputation-integration-service

# Run linter
lint:
	golangci-lint run

# Format code
fmt:
	go fmt ./...
	goimports -w .

# Run with hot reload (requires air)
dev:
	air

# Generate mocks (requires mockery)
mocks:
	mockery --all --output ./mocks

# Run integration tests
integration-test:
	go test -tags=integration ./...

# Create release build
release:
	CGO_ENABLED=0 GOOS=linux GOARCH=amd64 go build -a -installsuffix cgo -o bin/social-orders-reputation-integration-service-linux-amd64 ./main.go
	CGO_ENABLED=0 GOOS=darwin GOARCH=amd64 go build -a -installsuffix cgo -o bin/social-orders-reputation-integration-service-darwin-amd64 ./main.go
	CGO_ENABLED=0 GOOS=windows GOARCH=amd64 go build -a -installsuffix cgo -o bin/social-orders-reputation-integration-service-windows-amd64.exe ./main.go
