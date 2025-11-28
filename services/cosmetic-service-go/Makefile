.PHONY: generate-api bundle-api clean verify-api check-deps install-deps

CATALOG_SPEC := cosmetic-catalog-shop-purchase-service
EQUIPMENT_SPEC := cosmetic-equipment-inventory-service
OAPI_CODEGEN := oapi-codegen
REDOCLY_CLI := npx -y @redocly/cli
ROUTER_TYPE := gorilla-server

SPEC_DIR := ../../proto/openapi
API_DIR := pkg/api
CATALOG_BUNDLED := $(API_DIR)/$(CATALOG_SPEC).bundled.yaml
EQUIPMENT_BUNDLED := $(API_DIR)/$(EQUIPMENT_SPEC).bundled.yaml
OUTPUT_FILE := $(API_DIR)/api.gen.go

check-deps:
	@echo "Checking dependencies..."
	@command -v $(OAPI_CODEGEN) >/dev/null 2>&1 || { echo "❌ oapi-codegen not found. Install with: go install github.com/oapi-codegen/oapi-codegen/v2/cmd/oapi-codegen@latest"; exit 1; }
	@command -v node >/dev/null 2>&1 || { echo "❌ node not found. Install Node.js"; exit 1; }
	@command -v npx >/dev/null 2>&1 || { echo "❌ npx not found. Install Node.js"; exit 1; }
	@echo "OK All dependencies are installed"

install-deps:
	@echo "Installing dependencies..."
	@go install github.com/oapi-codegen/oapi-codegen/v2/cmd/oapi-codegen@latest || true
	@echo "OK Dependencies installed"

bundle-api: check-deps
	@echo "Bundling OpenAPI specs..."
	@mkdir -p $(API_DIR)
	@echo "Bundling catalog service..."
	@$(REDOCLY_CLI) bundle $(SPEC_DIR)/$(CATALOG_SPEC).yaml -o $(CATALOG_BUNDLED) || { echo "❌ Failed to bundle catalog spec"; exit 1; }
	@echo "Bundling equipment service..."
	@$(REDOCLY_CLI) bundle $(SPEC_DIR)/$(EQUIPMENT_SPEC).yaml -o $(EQUIPMENT_BUNDLED) || { echo "❌ Failed to bundle equipment spec"; exit 1; }
	@echo "Merging bundled specs..."
	@$(REDOCLY_CLI) bundle $(SPEC_DIR)/$(CATALOG_SPEC).yaml $(SPEC_DIR)/$(EQUIPMENT_SPEC).yaml -o $(API_DIR)/cosmetic-service.bundled.yaml || { echo "❌ Failed to merge specs"; exit 1; }
	@echo "OK Bundled specs"

generate-api: bundle-api
	@echo "Generating Go code from bundled specs..."
	@if [ ! -f "$(API_DIR)/cosmetic-service.bundled.yaml" ]; then \
		echo "❌ Bundled spec not found"; \
		exit 1; \
	fi
	@$(OAPI_CODEGEN) -package api -generate types,$(ROUTER_TYPE) -o $(OUTPUT_FILE) $(API_DIR)/cosmetic-service.bundled.yaml || { echo "❌ Failed to generate code"; exit 1; }
	@echo "OK Generated code: $(OUTPUT_FILE)"

verify-api: check-deps
	@echo "Verifying OpenAPI specs..."
	@echo "Verifying catalog service..."
	@$(REDOCLY_CLI) lint $(SPEC_DIR)/$(CATALOG_SPEC).yaml || { echo "WARNING  Catalog spec validation failed"; exit 1; }
	@echo "Verifying equipment service..."
	@$(REDOCLY_CLI) lint $(SPEC_DIR)/$(EQUIPMENT_SPEC).yaml || { echo "WARNING  Equipment spec validation failed"; exit 1; }
	@echo "OK OpenAPI specs are valid"

clean:
	@echo "Cleaning generated files"
	@rm -f $(CATALOG_BUNDLED) $(EQUIPMENT_BUNDLED) $(API_DIR)/cosmetic-service.bundled.yaml $(OUTPUT_FILE)
	@echo "OK Cleaned generated files"

