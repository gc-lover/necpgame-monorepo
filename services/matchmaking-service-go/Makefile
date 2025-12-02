.PHONY: generate-api bundle-api clean verify-api check-deps generate-types generate-server generate-spec check-file-sizes

SERVICE_NAME := matchmaking-service
OAPI_CODEGEN := oapi-codegen
REDOCLY_CLI := npx -y @redocly/cli
ROUTER_TYPE := chi-server

SPEC_DIR := ../../proto/openapi
API_DIR := pkg/api
SERVICE_SPEC := $(SPEC_DIR)/$(SERVICE_NAME).yaml
BUNDLED_SPEC := $(API_DIR)/$(SERVICE_NAME).bundled.yaml

# Split output files (SOLID compliance)
TYPES_FILE := $(API_DIR)/types.gen.go
SERVER_FILE := $(API_DIR)/server.gen.go
SPEC_FILE := $(API_DIR)/spec.gen.go

check-deps:
	@command -v $(OAPI_CODEGEN) >/dev/null 2>&1 || { echo "❌ oapi-codegen not found. Install: go install github.com/oapi-codegen/oapi-codegen/v2/cmd/oapi-codegen@latest"; exit 1; }
	@command -v node >/dev/null 2>&1 || { echo "❌ node not found. Install Node.js"; exit 1; }

bundle-api: check-deps
	@mkdir -p $(API_DIR)
	@echo "📦 Bundling OpenAPI spec..."
	@$(REDOCLY_CLI) bundle $(SERVICE_SPEC) -o $(BUNDLED_SPEC) || exit 1
	@echo "OK Bundle complete: $(BUNDLED_SPEC)"

generate-types: bundle-api
	@echo "🔨 Generating types.gen.go..."
	@$(OAPI_CODEGEN) -package api -generate types -o $(TYPES_FILE) $(BUNDLED_SPEC) || exit 1
	@wc -l $(TYPES_FILE) 2>/dev/null || powershell -Command "(Get-Content $(TYPES_FILE)).Count" | awk '{print "OK types.gen.go: " $$1 " lines"}'

generate-server: bundle-api
	@echo "🔨 Generating server.gen.go..."
	@$(OAPI_CODEGEN) -package api -generate $(ROUTER_TYPE) -o $(SERVER_FILE) $(BUNDLED_SPEC) || exit 1
	@wc -l $(SERVER_FILE) 2>/dev/null || powershell -Command "(Get-Content $(SERVER_FILE)).Count" | awk '{print "OK server.gen.go: " $$1 " lines"}'

generate-spec: bundle-api
	@echo "🔨 Generating spec.gen.go..."
	@$(OAPI_CODEGEN) -package api -generate spec -o $(SPEC_FILE) $(BUNDLED_SPEC) || exit 1
	@wc -l $(SPEC_FILE) 2>/dev/null || powershell -Command "(Get-Content $(SPEC_FILE)).Count" | awk '{print "OK spec.gen.go: " $$1 " lines"}'

check-file-sizes:
	@echo "🔍 Checking generated file sizes..."
	@for file in $(TYPES_FILE) $(SERVER_FILE) $(SPEC_FILE); do \
		if [ -f "$$file" ]; then \
			lines=$$(wc -l < "$$file" 2>/dev/null || powershell -Command "(Get-Content $$file).Count"); \
			if [ $$lines -gt 500 ]; then \
				echo "WARNING  $$file: $$lines lines (exceeds 500 limit!)"; \
			else \
				echo "OK $$file: $$lines lines (OK)"; \
			fi; \
		fi; \
	done

generate-api: generate-types generate-server generate-spec check-file-sizes
	@echo "OK All API code generated successfully!"

verify-api: check-deps
	@echo "🔍 Validating OpenAPI spec..."
	@$(REDOCLY_CLI) lint $(SERVICE_SPEC) || exit 1
	@echo "OK OpenAPI spec is valid!"

clean:
	@echo "🧹 Cleaning generated files..."
	@rm -f $(BUNDLED_SPEC) $(TYPES_FILE) $(SERVER_FILE) $(SPEC_FILE)
	@echo "OK Clean complete!"

