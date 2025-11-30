.PHONY: generate-api bundle-api clean verify-api check-deps install-deps generate-world-state-api verify-world-state-api

WORLD_EVENTS_SPEC := world-events-core-service
WORLD_STATE_SPEC := world-state-core-service
OAPI_CODEGEN := oapi-codegen
REDOCLY_CLI := npx -y @redocly/cli
ROUTER_TYPE := gorilla-server

SPEC_DIR := ../../proto/openapi
API_DIR := pkg/api
WORLD_EVENTS_SPEC_FILE := $(SPEC_DIR)/$(WORLD_EVENTS_SPEC).yaml
WORLD_STATE_SPEC_FILE := $(SPEC_DIR)/$(WORLD_STATE_SPEC).yaml
WORLD_EVENTS_BUNDLED := $(API_DIR)/$(WORLD_EVENTS_SPEC).bundled.yaml
WORLD_STATE_BUNDLED := $(API_DIR)/worldstate/$(WORLD_STATE_SPEC).bundled.yaml
WORLD_EVENTS_OUTPUT := $(API_DIR)/api.gen.go
WORLD_STATE_OUTPUT := $(API_DIR)/worldstate/api.gen.go

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
	@if [ ! -f "$(WORLD_EVENTS_SPEC_FILE)" ]; then \
		echo "❌ OpenAPI spec not found: $(WORLD_EVENTS_SPEC_FILE)"; \
		exit 1; \
	fi
	@mkdir -p $(API_DIR)
	@$(REDOCLY_CLI) bundle $(WORLD_EVENTS_SPEC_FILE) -o $(WORLD_EVENTS_BUNDLED) || { echo "❌ Failed to bundle $(WORLD_EVENTS_SPEC)"; exit 1; }
	@echo "OK Bundled spec: $(WORLD_EVENTS_BUNDLED)"

generate-world-state-api: check-deps
	@echo "Generating world-state API..."
	@if [ ! -f "$(WORLD_STATE_SPEC_FILE)" ]; then \
		echo "❌ OpenAPI spec not found: $(WORLD_STATE_SPEC_FILE)"; \
		exit 1; \
	fi
	@mkdir -p $(API_DIR)/worldstate
	@$(REDOCLY_CLI) bundle $(WORLD_STATE_SPEC_FILE) -o $(WORLD_STATE_BUNDLED) || { echo "❌ Failed to bundle $(WORLD_STATE_SPEC)"; exit 1; }
	@$(OAPI_CODEGEN) -package worldstate -generate types,$(ROUTER_TYPE) -o $(WORLD_STATE_OUTPUT) $(WORLD_STATE_BUNDLED) || { echo "❌ Failed to generate $(WORLD_STATE_SPEC)"; exit 1; }
	@echo "OK Generated $(WORLD_STATE_SPEC)"

generate-api: bundle-api generate-world-state-api
	@echo "Generating Go code from: $(WORLD_EVENTS_BUNDLED)"
	@if [ ! -f "$(WORLD_EVENTS_BUNDLED)" ]; then \
		echo "❌ Bundled spec not found: $(WORLD_EVENTS_BUNDLED)"; \
		exit 1; \
	fi
	@$(OAPI_CODEGEN) -package api -generate types,$(ROUTER_TYPE) -o $(WORLD_EVENTS_OUTPUT) $(WORLD_EVENTS_BUNDLED) || { echo "❌ Failed to generate code"; exit 1; }
	@echo "OK Generated code: $(WORLD_EVENTS_OUTPUT)"

verify-world-state-api: check-deps
	@echo "Verifying world-state OpenAPI spec: $(WORLD_STATE_SPEC_FILE)"
	@if [ ! -f "$(WORLD_STATE_SPEC_FILE)" ]; then \
		echo "❌ OpenAPI spec not found: $(WORLD_STATE_SPEC_FILE)"; \
		exit 1; \
	fi
	@$(REDOCLY_CLI) lint $(WORLD_STATE_SPEC_FILE) || { echo "WARNING  $(WORLD_STATE_SPEC) validation failed"; exit 1; }
	@echo "OK $(WORLD_STATE_SPEC) spec is valid"

verify-api: check-deps verify-world-state-api
	@echo "Verifying OpenAPI spec: $(WORLD_EVENTS_SPEC_FILE)"
	@if [ ! -f "$(WORLD_EVENTS_SPEC_FILE)" ]; then \
		echo "❌ OpenAPI spec not found: $(WORLD_EVENTS_SPEC_FILE)"; \
		exit 1; \
	fi
	@$(REDOCLY_CLI) lint $(WORLD_EVENTS_SPEC_FILE) || { echo "WARNING  OpenAPI spec validation failed"; exit 1; }
	@echo "OK OpenAPI spec is valid"

clean:
	@echo "Cleaning generated files"
	@rm -f $(WORLD_EVENTS_BUNDLED) $(WORLD_EVENTS_OUTPUT) $(WORLD_STATE_BUNDLED) $(WORLD_STATE_OUTPUT)
	@echo "OK Cleaned generated files"
