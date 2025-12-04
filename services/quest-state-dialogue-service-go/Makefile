# Issue: #1597-#1599
# Makefile for ogen code generation

.PHONY: generate-api clean

SERVICE_NAME := quest-state-dialogue-service
SPEC_DIR := ../../proto/openapi
BUNDLED_SPEC := openapi-bundled.yaml
API_DIR := pkg/api

generate-api:
	npx --yes @redocly/cli bundle $(SPEC_DIR)/$(SERVICE_NAME).yaml -o $(BUNDLED_SPEC)
	ogen --target $(API_DIR) --package api --clean $(BUNDLED_SPEC)

clean:
	rm -f $(BUNDLED_SPEC)
	rm -rf $(API_DIR)/oas_*_gen.go


