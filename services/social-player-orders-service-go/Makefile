.PHONY: generate-api clean verify-api check-deps install-deps bundle-core bundle-multi-exec bundle-all verify-all

OAPI_CODEGEN := oapi-codegen
REDOCLY_CLI := npx -y @redocly/cli
ROUTER_TYPE := chi-server

SPEC_DIR := ../../proto/openapi
CORE_SPEC := social-player-orders-core-service
MULTI_EXEC_SPEC := social-player-orders-multi-executor-service
SCHEMAS_SPEC := social-player-orders-schemas

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

verify-all: check-deps
	@echo "Verifying Player Orders OpenAPI specs..."
	@$(REDOCLY_CLI) lint $(SPEC_DIR)/$(CORE_SPEC).yaml || { echo "WARNING  $(CORE_SPEC) validation failed"; exit 1; }
	@$(REDOCLY_CLI) lint $(SPEC_DIR)/$(MULTI_EXEC_SPEC).yaml || { echo "WARNING  $(MULTI_EXEC_SPEC) validation failed"; exit 1; }
	@$(REDOCLY_CLI) lint $(SPEC_DIR)/$(SCHEMAS_SPEC).yaml || { echo "WARNING  $(SCHEMAS_SPEC) validation failed"; exit 1; }
	@echo "OK All Player Orders specs are valid"

bundle-core: check-deps
	@echo "Bundling $(CORE_SPEC)..."
	@$(REDOCLY_CLI) bundle $(SPEC_DIR)/$(CORE_SPEC).yaml --output $(SPEC_DIR)/$(CORE_SPEC)-bundled.yaml
	@echo "OK $(CORE_SPEC) bundled"

bundle-multi-exec: check-deps
	@echo "Bundling $(MULTI_EXEC_SPEC)..."
	@$(REDOCLY_CLI) bundle $(SPEC_DIR)/$(MULTI_EXEC_SPEC).yaml --output $(SPEC_DIR)/$(MULTI_EXEC_SPEC)-bundled.yaml
	@echo "OK $(MULTI_EXEC_SPEC) bundled"

bundle-all: bundle-core bundle-multi-exec
	@echo "OK All specs bundled"

generate-api: check-deps bundle-all
	@echo "Generating Go code from OpenAPI specs..."
	@mkdir -p pkg/api
	@$(OAPI_CODEGEN) -config oapi-codegen.yaml $(SPEC_DIR)/$(CORE_SPEC)-bundled.yaml > pkg/api/core_api.gen.go
	@$(OAPI_CODEGEN) -config oapi-codegen.yaml $(SPEC_DIR)/$(MULTI_EXEC_SPEC)-bundled.yaml > pkg/api/multi_exec_api.gen.go
	@echo "OK Go code generated in pkg/api/"

verify-api: verify-all
	@echo "OK All OpenAPI specs verified"

clean:
	@echo "Cleaning generated files..."
	@rm -rf pkg/api/*.gen.go
	@rm -f $(SPEC_DIR)/*-bundled.yaml
	@echo "OK Cleaned"

