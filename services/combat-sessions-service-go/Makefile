.PHONY: generate-api bundle-api clean verify-api

SERVICE_NAME := combat-sessions-service
ROUTER_TYPE := chi-server
SPEC_DIR := ../../proto/openapi
API_DIR := pkg/api

bundle-api:
	@mkdir -p $(API_DIR)
	npx -y @redocly/cli bundle $(SPEC_DIR)/$(SERVICE_NAME).yaml -o $(API_DIR)/$(SERVICE_NAME).bundled.yaml

generate-api: bundle-api
	oapi-codegen -package api -generate types,$(ROUTER_TYPE) -o $(API_DIR)/api.gen.go $(API_DIR)/$(SERVICE_NAME).bundled.yaml

verify-api:
	npx -y @redocly/cli lint $(SPEC_DIR)/$(SERVICE_NAME).yaml

clean:
	rm -f $(API_DIR)/*.bundled.yaml $(API_DIR)/api.gen.go

