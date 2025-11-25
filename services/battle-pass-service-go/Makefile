.PHONY: generate-api bundle-api clean verify-api check-deps install-deps

SERVICE_NAME := battle-pass-core-service
OAPI_CODEGEN := oapi-codegen
REDOCLY_CLI := npx -y @redocly/cli
ROUTER_TYPE := gorilla-server

SPEC_DIR := ../../proto/openapi
API_DIR := pkg/api
SERVICE_SPEC := $(SPEC_DIR)/$(SERVICE_NAME).yaml
BUNDLED_SPEC := $(API_DIR)/$(SERVICE_NAME).bundled.yaml
OUTPUT_FILE := $(API_DIR)/api.gen.go

check-deps:
	@echo "Checking dependencies..."
	@command -v $(OAPI_CODEGEN) >/dev/null 2>&1 || { echo "❌ oapi-codegen not found. Install with: go install github.com/oapi-codegen/oapi-codegen/v2/cmd/oapi-codegen@latest"; exit 1; }
	@command -v node >/dev/null 2>&1 || { echo "❌ node not found. Install Node.js"; exit 1; }
	@command -v npx >/dev/null 2>&1 || { echo "❌ npx not found. Install Node.js"; exit 1; }
	@echo "✅ All dependencies are installed"

install-deps:
	@echo "Installing dependencies..."
	@go install github.com/oapi-codegen/oapi-codegen/v2/cmd/oapi-codegen@latest || true
	@echo "✅ Dependencies installed"

bundle-api: check-deps
	@echo "Bundling OpenAPI spec: $(SERVICE_SPEC)"
	@if [ ! -f "$(SERVICE_SPEC)" ]; then \
		echo "❌ OpenAPI spec not found: $(SERVICE_SPEC)"; \
		exit 1; \
	fi
	@mkdir -p $(API_DIR)
	@$(REDOCLY_CLI) bundle $(SERVICE_SPEC) -o $(BUNDLED_SPEC) || { echo "❌ Failed to bundle OpenAPI spec"; exit 1; }
	@echo "✅ Bundled spec: $(BUNDLED_SPEC)"

generate-api: bundle-api
	@echo "Generating Go code from: $(BUNDLED_SPEC)"
	@if [ ! -f "$(BUNDLED_SPEC)" ]; then \
		echo "❌ Bundled spec not found: $(BUNDLED_SPEC)"; \
		exit 1; \
	fi
	@$(OAPI_CODEGEN) -package api -generate types,$(ROUTER_TYPE) -o $(OUTPUT_FILE) $(BUNDLED_SPEC) || { echo "❌ Failed to generate code"; exit 1; }
	@echo "✅ Generated code: $(OUTPUT_FILE)"

verify-api: check-deps
	@echo "Verifying OpenAPI spec: $(SERVICE_SPEC)"
	@if [ ! -f "$(SERVICE_SPEC)" ]; then \
		echo "❌ OpenAPI spec not found: $(SERVICE_SPEC)"; \
		exit 1; \
	fi
	@$(REDOCLY_CLI) lint $(SERVICE_SPEC) || { echo "⚠️  OpenAPI spec validation failed"; exit 1; }
	@echo "✅ OpenAPI spec is valid"

clean:
	@echo "Cleaning generated files"
	@rm -f $(BUNDLED_SPEC) $(OUTPUT_FILE)
	@echo "✅ Cleaned generated files"
