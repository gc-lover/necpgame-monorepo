# Issue: #1574
.PHONY: generate-api bundle-api clean verify-api check-deps generate-types generate-server generate-spec check-file-sizes

SERVICE_NAME := weapon-progression-service
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
	@command -v $(OAPI_CODEGEN) >/dev/null 2>&1 || { echo "❌ oapi-codegen not found"; exit 1; }
	@command -v node >/dev/null 2>&1 || { echo "❌ node not found"; exit 1; }

bundle-api: check-deps
	@mkdir -p $(API_DIR)
	@$(REDOCLY_CLI) bundle $(SERVICE_SPEC) -o $(BUNDLED_SPEC) || exit 1

generate-types: bundle-api
	@$(OAPI_CODEGEN) -package api -generate types -o $(TYPES_FILE) $(BUNDLED_SPEC) || exit 1
	@wc -l $(TYPES_FILE) | awk '{print "OK types.gen.go: " $$1 " lines"}'

generate-server: bundle-api
	@$(OAPI_CODEGEN) -package api -generate $(ROUTER_TYPE) -o $(SERVER_FILE) $(BUNDLED_SPEC) || exit 1
	@wc -l $(SERVER_FILE) | awk '{print "OK server.gen.go: " $$1 " lines"}'

generate-spec: bundle-api
	@$(OAPI_CODEGEN) -package api -generate spec -o $(SPEC_FILE) $(BUNDLED_SPEC) || exit 1
	@wc -l $(SPEC_FILE) | awk '{print "OK spec.gen.go: " $$1 " lines"}'

check-file-sizes:
	@for file in $(TYPES_FILE) $(SERVER_FILE) $(SPEC_FILE); do \
		if [ -f "$$file" ]; then \
			lines=$$(wc -l < "$$file"); \
			if [ $$lines -gt 500 ]; then echo "WARNING  $$file: $$lines lines"; \
			else echo "OK $$file: $$lines lines"; fi; \
		fi; \
	done

generate-api: generate-types generate-server generate-spec check-file-sizes

verify-api: check-deps
	@$(REDOCLY_CLI) lint $(SERVICE_SPEC) || exit 1

clean:
	@rm -f $(BUNDLED_SPEC) $(TYPES_FILE) $(SERVER_FILE) $(SPEC_FILE)








