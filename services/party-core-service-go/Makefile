.PHONY: all build run test clean deps generate

# Variables
SERVICE_NAME=party-core-service
OPENAPI_SPEC=../../../proto/openapi/social-domain/parties/social-party-core-service.yaml
GENERATED_API=api

# Build the service
build: generate
	go build -o bin/$(SERVICE_NAME) ./cmd

# Run the service
run: build
	./bin/$(SERVICE_NAME)

# Generate API code from OpenAPI spec
generate:
	oapi-codegen -generate types -o $(GENERATED_API)/types.gen.go -package api $(OPENAPI_SPEC)
	oapi-codegen -generate server -o $(GENERATED_API)/server.gen.go -package api $(OPENAPI_SPEC)
	oapi-codegen -generate spec -o $(GENERATED_API)/spec.gen.go -package api $(OPENAPI_SPEC)

# Install dependencies
deps:
	go mod download
	go mod tidy

# Run tests
test:
	go test -v ./...

# Clean build artifacts
clean:
	rm -rf bin/
	rm -rf $(GENERATED_API)/*.gen.go

# Docker build
docker-build:
	docker build -t $(SERVICE_NAME) .

# Docker run
docker-run:
	docker run -p 8084:8084 $(SERVICE_NAME)

# Full development cycle
dev: deps generate build run
