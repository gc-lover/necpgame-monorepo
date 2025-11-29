.PHONY: generate-api bundle-api clean verify-api check-deps install-deps generate-weapon-combinations-api

OAPI_CODEGEN := oapi-codegen
REDOCLY_CLI := npx -y @redocly/cli
ROUTER_TYPE := gorilla-server

SPEC_DIR := ../../proto/openapi

TRADE_SERVICE_SPEC := economy-trade-sessions-service
WEAPON_COMBINATIONS_SPEC := economy-weapon-combinations-service

TRADE_API_DIR := pkg/api
WEAPON_COMBINATIONS_API_DIR := pkg/weaponcombinationsapi

TRADE_SPEC := $(SPEC_DIR)/$(TRADE_SERVICE_SPEC).yaml
TRADE_BUNDLED_SPEC := $(TRADE_API_DIR)/$(TRADE_SERVICE_SPEC).bundled.yaml
TRADE_OUTPUT_FILE := $(TRADE_API_DIR)/api.gen.go

WEAPON_COMBINATIONS_SPEC_FILE := $(SPEC_DIR)/$(WEAPON_COMBINATIONS_SPEC).yaml
WEAPON_COMBINATIONS_BUNDLED_SPEC := $(WEAPON_COMBINATIONS_API_DIR)/$(WEAPON_COMBINATIONS_SPEC).bundled.yaml
WEAPON_COMBINATIONS_OUTPUT_FILE := $(WEAPON_COMBINATIONS_API_DIR)/api.gen.go

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
	@echo "Bundling trade OpenAPI spec: $(TRADE_SPEC)"
	@if [ ! -f "$(TRADE_SPEC)" ]; then \
		echo "❌ OpenAPI spec not found: $(TRADE_SPEC)"; \
		exit 1; \
	fi
	@mkdir -p $(TRADE_API_DIR)
	@$(REDOCLY_CLI) bundle $(TRADE_SPEC) -o $(TRADE_BUNDLED_SPEC) || { echo "❌ Failed to bundle OpenAPI spec"; exit 1; }
	@echo "OK Bundled spec: $(TRADE_BUNDLED_SPEC)"

generate-api: bundle-api
	@echo "Generating Go code from: $(TRADE_BUNDLED_SPEC)"
	@if [ ! -f "$(TRADE_BUNDLED_SPEC)" ]; then \
		echo "❌ Bundled spec not found: $(TRADE_BUNDLED_SPEC)"; \
		exit 1; \
	fi
	@$(OAPI_CODEGEN) -package api -generate types,$(ROUTER_TYPE) -o $(TRADE_OUTPUT_FILE) $(TRADE_BUNDLED_SPEC) || { echo "❌ Failed to generate code"; exit 1; }
	@echo "OK Generated code: $(TRADE_OUTPUT_FILE)"

generate-weapon-combinations-api: check-deps
	@echo "Generating weapon combinations API..."
	@mkdir -p $(WEAPON_COMBINATIONS_API_DIR)
	@$(REDOCLY_CLI) bundle $(WEAPON_COMBINATIONS_SPEC_FILE) -o $(WEAPON_COMBINATIONS_BUNDLED_SPEC) || { echo "❌ Failed to bundle $(WEAPON_COMBINATIONS_SPEC)"; exit 1; }
	@$(OAPI_CODEGEN) -package weaponcombinationsapi -generate types,$(ROUTER_TYPE) -o $(WEAPON_COMBINATIONS_OUTPUT_FILE) $(WEAPON_COMBINATIONS_BUNDLED_SPEC) || { echo "❌ Failed to generate $(WEAPON_COMBINATIONS_SPEC)"; exit 1; }
	@echo "OK Generated $(WEAPON_COMBINATIONS_SPEC)"

verify-api: check-deps
	@echo "Verifying OpenAPI specs..."
	@$(REDOCLY_CLI) lint $(TRADE_SPEC) || { echo "WARNING  $(TRADE_SERVICE_SPEC) validation failed"; exit 1; }
	@$(REDOCLY_CLI) lint $(WEAPON_COMBINATIONS_SPEC_FILE) || { echo "WARNING  $(WEAPON_COMBINATIONS_SPEC) validation failed"; exit 1; }
	@echo "OK All OpenAPI specs are valid"

clean:
	@echo "Cleaning generated files"
	@rm -f $(TRADE_BUNDLED_SPEC) $(TRADE_OUTPUT_FILE)
	@rm -rf $(WEAPON_COMBINATIONS_API_DIR)
	@echo "OK Cleaned generated files"
