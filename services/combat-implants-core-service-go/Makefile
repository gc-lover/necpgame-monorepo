.PHONY: generate-api bundle-api clean verify-api check-deps

SERVICE_NAME := combat-implants-core-service
OAPI_CODEGEN := oapi-codegen
REDOCLY_CLI := npx -y @redocly/cli
ROUTER_TYPE := chi-server

SPEC_DIR := ../../proto/openapi
API_DIR := pkg/api
SERVICE_SPEC := $(SPEC_DIR)/$(SERVICE_NAME).yaml
BUNDLED_SPEC := $(API_DIR)/$(SERVICE_NAME).bundled.yaml
OUTPUT_FILE := $(API_DIR)/api.gen.go

check-deps:
	@echo "Checking dependencies..."
	@command -v $(OAPI_CODEGEN) >/dev/null 2>&1 || { echo "❌ oapi-codegen not found"; exit 1; }
	@command -v node >/dev/null 2>&1 || { echo "❌ node not found"; exit 1; }
	@echo "✅ All dependencies are installed"

bundle-api: check-deps
	@echo "Bundling OpenAPI spec: $(SERVICE_SPEC)"
	@mkdir -p $(API_DIR)
	@$(REDOCLY_CLI) bundle $(SERVICE_SPEC) -o $(BUNDLED_SPEC)
	@echo "✅ Bundled spec: $(BUNDLED_SPEC)"

generate-api: bundle-api
	@echo "Generating Go code from: $(BUNDLED_SPEC)"
	@$(OAPI_CODEGEN) -package api -generate types,$(ROUTER_TYPE) -o $(OUTPUT_FILE) $(BUNDLED_SPEC)
	@echo "✅ Generated code: $(OUTPUT_FILE)"

verify-api: check-deps
	@echo "Verifying OpenAPI spec: $(SERVICE_SPEC)"
	@$(REDOCLY_CLI) lint $(SERVICE_SPEC)
	@echo "✅ OpenAPI spec is valid"

clean:
	@echo "Cleaning generated files"
	@rm -f $(BUNDLED_SPEC) $(OUTPUT_FILE)
	@echo "✅ Cleaned generated files"

