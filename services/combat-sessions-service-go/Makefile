# Issue: #130

SERVICE_NAME := combat-sessions-service
ROUTER_TYPE := chi-server
OPENAPI_SPEC := ../../proto/openapi/combat-session-service.yaml
BUNDLED_SPEC := pkg/api/combat-session-service.bundled.yaml

.PHONY: all
all: generate-api build

.PHONY: verify-api
verify-api:
	@echo "Verifying OpenAPI spec..."
	npx -y @redocly/cli lint $(OPENAPI_SPEC)

.PHONY: bundle-api
bundle-api:
	@echo "Bundling OpenAPI spec..."
	npx -y @redocly/cli bundle $(OPENAPI_SPEC) -o $(BUNDLED_SPEC)

.PHONY: generate-api
generate-api: verify-api bundle-api
	@echo "Generating API code..."
	oapi-codegen -config=oapi-codegen.yaml $(BUNDLED_SPEC)
	@echo "API code generated successfully"

.PHONY: build
build:
	@echo "Building $(SERVICE_NAME)..."
	go build -o bin/$(SERVICE_NAME) .

.PHONY: test
test:
	@echo "Running tests..."
	go test ./...

.PHONY: clean
clean:
	@echo "Cleaning..."
	rm -f bin/$(SERVICE_NAME)
	rm -f $(BUNDLED_SPEC)

.PHONY: docker-build
docker-build:
	docker build -t $(SERVICE_NAME):latest .

