.PHONY: generate-api bundle-api clean verify-api check-deps install-deps generate-types generate-server generate-spec check-file-sizes

SERVICE_NAME := maintenance-service
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
	@echo "Checking dependencies..."
	@command -v $(OAPI_CODEGEN) >/dev/null 2>&1 || { echo "❌ oapi-codegen not found. Install: go install github.com/oapi-codegen/oapi-codegen/v2/cmd/oapi-codegen@latest"; exit 1; }
	@command -v node >/dev/null 2>&1 || { echo "❌ node not found. Install Node.js"; exit 1; }
	@echo "OK All dependencies are installed"

install-deps:
	@echo "Installing dependencies..."
	@go install github.com/oapi-codegen/oapi-codegen/v2/cmd/oapi-codegen@latest || true
	@echo "OK Dependencies installed"

bundle-api: check-deps
	@echo "Bundling OpenAPI spec: $(SERVICE_SPEC)"
	@if [ ! -f "$(SERVICE_SPEC)" ]; then \
		echo "❌ OpenAPI spec not found: $(SERVICE_SPEC)"; \
		exit 1; \
	fi
	@mkdir -p $(API_DIR)
	@$(REDOCLY_CLI) bundle $(SERVICE_SPEC) -o $(BUNDLED_SPEC) || { echo "❌ Failed to bundle"; exit 1; }
	@echo "OK Bundled spec: $(BUNDLED_SPEC)"

# Generate types separately (models only)
generate-types: bundle-api
	@echo "Generating types from: $(BUNDLED_SPEC)"
	@$(OAPI_CODEGEN) -package api -generate types -o $(TYPES_FILE) $(BUNDLED_SPEC) || { echo "❌ Failed to generate types"; exit 1; }
	@wc -l $(TYPES_FILE) | awk '{print "OK Generated types: $(TYPES_FILE) (" $$1 " lines)"}'

# Generate server interface separately
generate-server: bundle-api
	@echo "Generating server interface from: $(BUNDLED_SPEC)"
	@$(OAPI_CODEGEN) -package api -generate $(ROUTER_TYPE) -o $(SERVER_FILE) $(BUNDLED_SPEC) || { echo "❌ Failed to generate server"; exit 1; }
	@wc -l $(SERVER_FILE) | awk '{print "OK Generated server: $(SERVER_FILE) (" $$1 " lines)"}'

# Generate spec embedding
generate-spec: bundle-api
	@echo "Generating spec embedding from: $(BUNDLED_SPEC)"
	@$(OAPI_CODEGEN) -package api -generate spec -o $(SPEC_FILE) $(BUNDLED_SPEC) || { echo "❌ Failed to generate spec"; exit 1; }
	@wc -l $(SPEC_FILE) | awk '{print "OK Generated spec: $(SPEC_FILE) (" $$1 " lines)"}'

# Check file sizes (500 line limit)
check-file-sizes:
	@echo ""
	@echo "Checking file sizes (max 500 lines)..."
	@for file in $(TYPES_FILE) $(SERVER_FILE) $(SPEC_FILE); do \
		if [ -f "$$file" ]; then \
			lines=$$(wc -l < "$$file" | tr -d ' '); \
			if [ $$lines -gt 500 ]; then \
				echo "WARNING  WARNING: $$file has $$lines lines (exceeds 500 line limit)"; \
			else \
				echo "OK $$file: $$lines lines (OK)"; \
			fi; \
		fi; \
	done

# Generate all files
generate-api: generate-types generate-server generate-spec check-file-sizes
	@echo ""
	@echo "OK Code generation complete!"
	@echo "Files generated:"
	@ls -lh $(API_DIR)/*.gen.go 2>/dev/null || true

verify-api: check-deps
	@echo "Verifying OpenAPI spec: $(SERVICE_SPEC)"
	@$(REDOCLY_CLI) lint $(SERVICE_SPEC) || { echo "❌ Spec validation failed"; exit 1; }
	@echo "OK Spec is valid"

clean:
	@echo "Cleaning generated files"
	@rm -f $(BUNDLED_SPEC) $(TYPES_FILE) $(SERVER_FILE) $(SPEC_FILE)
	@echo "OK Cleaned"
