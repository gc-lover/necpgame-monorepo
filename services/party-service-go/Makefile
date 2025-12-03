# Issue: #139 - party-service ogen migration + optimizations
.PHONY: generate-api clean build test bench

SERVICE_NAME := party-service
SPEC_DIR := ../../proto/openapi
API_DIR := pkg/api
BUNDLED_SPEC := openapi-bundled.yaml

generate-api:
	@echo "📦 Bundling OpenAPI spec..."
	npx --yes @redocly/cli bundle $(SPEC_DIR)/$(SERVICE_NAME).yaml -o $(BUNDLED_SPEC)
	@echo "🔨 Generating ogen code..."
	ogen --target $(API_DIR) --package api --clean $(BUNDLED_SPEC)
	@echo "OK ogen code generated!"

clean:
	rm -rf $(API_DIR)/* $(BUNDLED_SPEC)

build:
	go build -o party-service ./...

test:
	go test -v ./...

bench:
	go test -bench=. -benchmem -run=^$$ ./server
