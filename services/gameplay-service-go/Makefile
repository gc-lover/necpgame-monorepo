.PHONY: generate-api clean verify-api check-deps install-deps generate-weapon-core-api generate-weapon-combat-api generate-weapon-effects-api generate-weapon-advanced-api generate-all-weapon-apis verify-weapon-apis

OAPI_CODEGEN := oapi-codegen
REDOCLY_CLI := npx -y @redocly/cli
ROUTER_TYPE := gorilla-server

SPEC_DIR := ../../proto/openapi

WEAPON_CORE_SPEC := gameplay-weapon-special-mechanics-core-service
WEAPON_COMBAT_SPEC := gameplay-weapon-special-mechanics-combat-service
WEAPON_EFFECTS_SPEC := gameplay-weapon-special-mechanics-effects-service
WEAPON_ADVANCED_SPEC := gameplay-weapon-special-mechanics-advanced-service

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

verify-weapon-apis: check-deps
	@echo "Verifying weapon mechanics OpenAPI specs..."
	@$(REDOCLY_CLI) lint $(SPEC_DIR)/$(WEAPON_CORE_SPEC).yaml || { echo "WARNING  $(WEAPON_CORE_SPEC) validation failed"; exit 1; }
	@$(REDOCLY_CLI) lint $(SPEC_DIR)/$(WEAPON_COMBAT_SPEC).yaml || { echo "WARNING  $(WEAPON_COMBAT_SPEC) validation failed"; exit 1; }
	@$(REDOCLY_CLI) lint $(SPEC_DIR)/$(WEAPON_EFFECTS_SPEC).yaml || { echo "WARNING  $(WEAPON_EFFECTS_SPEC) validation failed"; exit 1; }
	@$(REDOCLY_CLI) lint $(SPEC_DIR)/$(WEAPON_ADVANCED_SPEC).yaml || { echo "WARNING  $(WEAPON_ADVANCED_SPEC) validation failed"; exit 1; }
	@echo "OK All weapon mechanics specs are valid"

verify-api: verify-weapon-apis
	@echo "OK All OpenAPI specs verified"

generate-weapon-core-api: check-deps
	@echo "Generating weapon core API..."
	@mkdir -p pkg/weaponcoreapi
	@$(REDOCLY_CLI) bundle $(SPEC_DIR)/$(WEAPON_CORE_SPEC).yaml -o pkg/weaponcoreapi/$(WEAPON_CORE_SPEC).bundled.yaml || { echo "❌ Failed to bundle $(WEAPON_CORE_SPEC)"; exit 1; }
	@$(OAPI_CODEGEN) -package weaponcoreapi -generate types,$(ROUTER_TYPE) -o pkg/weaponcoreapi/api.gen.go pkg/weaponcoreapi/$(WEAPON_CORE_SPEC).bundled.yaml || { echo "❌ Failed to generate $(WEAPON_CORE_SPEC)"; exit 1; }
	@echo "OK Generated $(WEAPON_CORE_SPEC)"

generate-weapon-combat-api: check-deps
	@echo "Generating weapon combat API..."
	@mkdir -p pkg/weaponcombatapi
	@$(REDOCLY_CLI) bundle $(SPEC_DIR)/$(WEAPON_COMBAT_SPEC).yaml -o pkg/weaponcombatapi/$(WEAPON_COMBAT_SPEC).bundled.yaml || { echo "❌ Failed to bundle $(WEAPON_COMBAT_SPEC)"; exit 1; }
	@$(OAPI_CODEGEN) -package weaponcombatapi -generate types,$(ROUTER_TYPE) -o pkg/weaponcombatapi/api.gen.go pkg/weaponcombatapi/$(WEAPON_COMBAT_SPEC).bundled.yaml || { echo "❌ Failed to generate $(WEAPON_COMBAT_SPEC)"; exit 1; }
	@echo "OK Generated $(WEAPON_COMBAT_SPEC)"

generate-weapon-effects-api: check-deps
	@echo "Generating weapon effects API..."
	@mkdir -p pkg/weaponeffectsapi
	@$(REDOCLY_CLI) bundle $(SPEC_DIR)/$(WEAPON_EFFECTS_SPEC).yaml -o pkg/weaponeffectsapi/$(WEAPON_EFFECTS_SPEC).bundled.yaml || { echo "❌ Failed to bundle $(WEAPON_EFFECTS_SPEC)"; exit 1; }
	@$(OAPI_CODEGEN) -package weaponeffectsapi -generate types,$(ROUTER_TYPE) -o pkg/weaponeffectsapi/api.gen.go pkg/weaponeffectsapi/$(WEAPON_EFFECTS_SPEC).bundled.yaml || { echo "❌ Failed to generate $(WEAPON_EFFECTS_SPEC)"; exit 1; }
	@echo "OK Generated $(WEAPON_EFFECTS_SPEC)"

generate-weapon-advanced-api: check-deps
	@echo "Generating weapon advanced API..."
	@mkdir -p pkg/weaponadvancedapi
	@$(REDOCLY_CLI) bundle $(SPEC_DIR)/$(WEAPON_ADVANCED_SPEC).yaml -o pkg/weaponadvancedapi/$(WEAPON_ADVANCED_SPEC).bundled.yaml || { echo "❌ Failed to bundle $(WEAPON_ADVANCED_SPEC)"; exit 1; }
	@$(OAPI_CODEGEN) -package weaponadvancedapi -generate types,$(ROUTER_TYPE) -o pkg/weaponadvancedapi/api.gen.go pkg/weaponadvancedapi/$(WEAPON_ADVANCED_SPEC).bundled.yaml || { echo "❌ Failed to generate $(WEAPON_ADVANCED_SPEC)"; exit 1; }
	@echo "OK Generated $(WEAPON_ADVANCED_SPEC)"

generate-all-weapon-apis: generate-weapon-core-api generate-weapon-combat-api generate-weapon-effects-api generate-weapon-advanced-api
	@echo "OK All weapon mechanics APIs generated"

generate-api: generate-all-weapon-apis
	@echo "OK All APIs generated"

clean:
	@echo "Cleaning generated files"
	@rm -rf pkg/weaponcoreapi pkg/weaponcombatapi pkg/weaponeffectsapi pkg/weaponadvancedapi
	@echo "OK Cleaned generated files"
