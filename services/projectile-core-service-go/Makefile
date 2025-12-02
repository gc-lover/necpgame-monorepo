# Issue: #1560

SERVICE_NAME := projectile-core-service
ROUTER_TYPE := chi-server
OPENAPI_SPEC := ../../proto/openapi/projectile-core-service.yaml
BUNDLED_SPEC := projectile-core-service-bundled.yaml
GENERATED_FILE := pkg/api/api.gen.go

.PHONY: all verify-api bundle-api generate-api build test clean help

all: generate-api build

## verify-api: Verify OpenAPI spec validity
verify-api:
	@echo "🔍 Verifying OpenAPI spec..."
	@npx --yes @redocly/cli lint $(OPENAPI_SPEC)

## bundle-api: Bundle OpenAPI spec (resolve $ref)
bundle-api:
	@echo "📦 Bundling OpenAPI spec..."
	@npx --yes @redocly/cli bundle $(OPENAPI_SPEC) -o $(BUNDLED_SPEC)

## generate-api: Generate API code from OpenAPI spec
generate-api: verify-api bundle-api
	@echo "⚙️ Generating API code..."
	@oapi-codegen -config oapi-codegen.yaml $(BUNDLED_SPEC)
	@echo "OK API code generated: $(GENERATED_FILE)"

## build: Build service
build:
	@echo "🔨 Building $(SERVICE_NAME)..."
	@go build -o $(SERVICE_NAME) .

## test: Run tests
test:
	@echo "🧪 Running tests..."
	@go test -v ./...

## clean: Clean generated files and binaries
clean:
	@echo "🧹 Cleaning..."
	@rm -f $(SERVICE_NAME) $(SERVICE_NAME).exe
	@rm -f $(BUNDLED_SPEC)
	@rm -f $(GENERATED_FILE)

## help: Show this help
help:
	@echo "Available targets:"
	@sed -n 's/^##//p' Makefile

