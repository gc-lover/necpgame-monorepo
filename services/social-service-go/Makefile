.PHONY: generate-api bundle-api clean verify-api check-deps install-deps generate-romance-core-api verify-romance-core-api

FRIENDS_SPEC := social-friends-core-service
ROMANCE_CORE_SPEC := social-romance-core-service
OAPI_CODEGEN := oapi-codegen
REDOCLY_CLI := npx -y @redocly/cli
ROUTER_TYPE := gorilla-server

SPEC_DIR := ../../proto/openapi
API_DIR := pkg/api
FRIENDS_SPEC_FILE := $(SPEC_DIR)/$(FRIENDS_SPEC).yaml
ROMANCE_CORE_SPEC_FILE := $(SPEC_DIR)/$(ROMANCE_CORE_SPEC).yaml
FRIENDS_BUNDLED := $(API_DIR)/$(FRIENDS_SPEC).bundled.yaml
ROMANCE_CORE_BUNDLED := $(API_DIR)/romancecore/$(ROMANCE_CORE_SPEC).bundled.yaml
FRIENDS_OUTPUT := $(API_DIR)/api.gen.go
ROMANCE_CORE_OUTPUT := $(API_DIR)/romancecore/api.gen.go

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
	@if [ ! -f "$(FRIENDS_SPEC_FILE)" ]; then \
		echo "❌ OpenAPI spec not found: $(FRIENDS_SPEC_FILE)"; \
		exit 1; \
	fi
	@mkdir -p $(API_DIR)
	@$(REDOCLY_CLI) bundle $(FRIENDS_SPEC_FILE) -o $(FRIENDS_BUNDLED) || { echo "❌ Failed to bundle $(FRIENDS_SPEC)"; exit 1; }
	@echo "OK Bundled spec: $(FRIENDS_BUNDLED)"

generate-romance-core-api: check-deps
	@echo "Generating romance-core API..."
	@if [ ! -f "$(ROMANCE_CORE_SPEC_FILE)" ]; then \
		echo "❌ OpenAPI spec not found: $(ROMANCE_CORE_SPEC_FILE)"; \
		exit 1; \
	fi
	@mkdir -p $(API_DIR)/romancecore
	@$(REDOCLY_CLI) bundle $(ROMANCE_CORE_SPEC_FILE) -o $(ROMANCE_CORE_BUNDLED) || { echo "❌ Failed to bundle $(ROMANCE_CORE_SPEC)"; exit 1; }
	@$(OAPI_CODEGEN) -package romancecore -generate types,$(ROUTER_TYPE) -o $(ROMANCE_CORE_OUTPUT) $(ROMANCE_CORE_BUNDLED) || { echo "❌ Failed to generate $(ROMANCE_CORE_SPEC)"; exit 1; }
	@echo "OK Generated $(ROMANCE_CORE_SPEC)"

generate-api: bundle-api generate-romance-core-api
	@echo "Generating Go code from: $(FRIENDS_BUNDLED)"
	@if [ ! -f "$(FRIENDS_BUNDLED)" ]; then \
		echo "❌ Bundled spec not found: $(FRIENDS_BUNDLED)"; \
		exit 1; \
	fi
	@$(OAPI_CODEGEN) -package api -generate types,$(ROUTER_TYPE) -o $(FRIENDS_OUTPUT) $(FRIENDS_BUNDLED) || { echo "❌ Failed to generate code"; exit 1; }
	@echo "OK Generated code: $(FRIENDS_OUTPUT)"

verify-romance-core-api: check-deps
	@echo "Verifying romance-core OpenAPI spec: $(ROMANCE_CORE_SPEC_FILE)"
	@if [ ! -f "$(ROMANCE_CORE_SPEC_FILE)" ]; then \
		echo "❌ OpenAPI spec not found: $(ROMANCE_CORE_SPEC_FILE)"; \
		exit 1; \
	fi
	@$(REDOCLY_CLI) lint $(ROMANCE_CORE_SPEC_FILE) || { echo "WARNING  $(ROMANCE_CORE_SPEC) validation failed"; exit 1; }
	@echo "OK $(ROMANCE_CORE_SPEC) spec is valid"

verify-api: check-deps verify-romance-core-api
	@echo "Verifying OpenAPI spec: $(FRIENDS_SPEC_FILE)"
	@if [ ! -f "$(FRIENDS_SPEC_FILE)" ]; then \
		echo "❌ OpenAPI spec not found: $(FRIENDS_SPEC_FILE)"; \
		exit 1; \
	fi
	@$(REDOCLY_CLI) lint $(FRIENDS_SPEC_FILE) || { echo "WARNING  OpenAPI spec validation failed"; exit 1; }
	@echo "OK OpenAPI spec is valid"

clean:
	@echo "Cleaning generated files"
	@rm -f $(FRIENDS_BUNDLED) $(FRIENDS_OUTPUT) $(ROMANCE_CORE_BUNDLED) $(ROMANCE_CORE_OUTPUT)
	@echo "OK Cleaned generated files"
