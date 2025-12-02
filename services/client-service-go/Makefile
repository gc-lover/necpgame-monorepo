.PHONY: generate-api clean verify-api check-deps install-deps generate-weapon-effects-api

OAPI_CODEGEN := oapi-codegen
REDOCLY_CLI := npx -y @redocly/cli
ROUTER_TYPE := chi-server

SPEC_DIR := ../../proto/openapi

WEAPON_EFFECTS_SPEC := client-weapon-effects-service

WEAPON_EFFECTS_API_DIR := pkg/weaponeffectsapi

WEAPON_EFFECTS_SPEC_FILE := $(SPEC_DIR)/$(WEAPON_EFFECTS_SPEC).yaml
WEAPON_EFFECTS_BUNDLED_SPEC := $(WEAPON_EFFECTS_API_DIR)/$(WEAPON_EFFECTS_SPEC).bundled.yaml
WEAPON_EFFECTS_OUTPUT_FILE := $(WEAPON_EFFECTS_API_DIR)/api.gen.go

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

generate-weapon-effects-api: check-deps
	@echo "Generating weapon effects API..."
	@mkdir -p $(WEAPON_EFFECTS_API_DIR)
	@$(REDOCLY_CLI) bundle $(WEAPON_EFFECTS_SPEC_FILE) -o $(WEAPON_EFFECTS_BUNDLED_SPEC) || { echo "❌ Failed to bundle $(WEAPON_EFFECTS_SPEC)"; exit 1; }
	@$(OAPI_CODEGEN) -package weaponeffectsapi -generate types,$(ROUTER_TYPE) -o $(WEAPON_EFFECTS_OUTPUT_FILE) $(WEAPON_EFFECTS_BUNDLED_SPEC) || { echo "❌ Failed to generate $(WEAPON_EFFECTS_SPEC)"; exit 1; }
	@echo "OK Generated $(WEAPON_EFFECTS_SPEC)"

generate-api: generate-weapon-effects-api
	@echo "OK All APIs generated"

verify-api: check-deps
	@echo "Verifying OpenAPI spec: $(WEAPON_EFFECTS_SPEC_FILE)"
	@if [ ! -f "$(WEAPON_EFFECTS_SPEC_FILE)" ]; then \
		echo "❌ OpenAPI spec not found: $(WEAPON_EFFECTS_SPEC_FILE)"; \
		exit 1; \
	fi
	@$(REDOCLY_CLI) lint $(WEAPON_EFFECTS_SPEC_FILE) || { echo "WARNING  OpenAPI spec validation failed"; exit 1; }
	@echo "OK OpenAPI spec is valid"

clean:
	@echo "Cleaning generated files"
	@rm -rf $(WEAPON_EFFECTS_API_DIR)
	@echo "OK Cleaned generated files"










