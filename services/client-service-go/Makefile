.PHONY: generate-api bundle-api clean verify-api check-deps install-deps generate-types generate-server generate-spec check-file-sizes

SERVICE_NAME := client-service
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
	@command -v $(OAPI_CODEGEN) >/dev/null 2>&1 || { echo "❌ oapi-codegen not found"; exit 1; }
	@command -v node >/dev/null 2>&1 || { echo "❌ node not found"; exit 1; }
	@echo "OK Dependencies installed"

install-deps:
	@go install github.com/oapi-codegen/oapi-codegen/v2/cmd/oapi-codegen@latest || true

bundle-api: check-deps
	@echo "Bundling: $(SERVICE_SPEC)"
	@if [ ! -f "$(SERVICE_SPEC)" ]; then echo "❌ Spec not found"; exit 1; fi
	@mkdir -p $(API_DIR)
	@$(REDOCLY_CLI) bundle $(SERVICE_SPEC) -o $(BUNDLED_SPEC) || exit 1
	@echo "OK Bundled"

generate-types: bundle-api
	@echo "Generating types..."
	@$(OAPI_CODEGEN) -package api -generate types -o $(TYPES_FILE) $(BUNDLED_SPEC) || exit 1
	@wc -l $(TYPES_FILE) | awk '{print "OK types.gen.go: " $$1 " lines"}'

generate-server: bundle-api
	@echo "Generating server..."
	@$(OAPI_CODEGEN) -package api -generate $(ROUTER_TYPE) -o $(SERVER_FILE) $(BUNDLED_SPEC) || exit 1
	@wc -l $(SERVER_FILE) | awk '{print "OK server.gen.go: " $$1 " lines"}'

generate-spec: bundle-api
	@echo "Generating spec..."
	@$(OAPI_CODEGEN) -package api -generate spec -o $(SPEC_FILE) $(BUNDLED_SPEC) || exit 1
	@wc -l $(SPEC_FILE) | awk '{print "OK spec.gen.go: " $$1 " lines"}'

check-file-sizes:
	@echo ""
	@echo "File size check (max 500 lines):"
	@for file in $(TYPES_FILE) $(SERVER_FILE) $(SPEC_FILE); do \
		if [ -f "$$file" ]; then \
			lines=$$(wc -l < "$$file" | tr -d ' '); \
			if [ $$lines -gt 500 ]; then \
				echo "WARNING  $$file: $$lines lines (EXCEEDS)"; \
			else \
				echo "OK $$file: $$lines lines"; \
			fi; \
		fi; \
	done

generate-api: generate-types generate-server generate-spec check-file-sizes
	@echo ""
	@echo "OK Generation complete!"

verify-api: check-deps
	@echo "Verifying: $(SERVICE_SPEC)"
	@if [ ! -f "$(SERVICE_SPEC)" ]; then exit 1; fi
	@$(REDOCLY_CLI) lint $(SERVICE_SPEC) || exit 1
	@echo "OK Valid"

clean:
	@rm -f $(BUNDLED_SPEC) $(TYPES_FILE) $(SERVER_FILE) $(SPEC_FILE)








