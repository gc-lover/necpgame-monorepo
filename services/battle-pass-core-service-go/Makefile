# Issue: #1636
SERVICE=battle-pass-core
PORT=8093

.PHONY: generate-api
generate-api:
	@echo "Bundling OpenAPI spec..."
	npx --yes @redocly/cli bundle ../../proto/openapi/$(SERVICE)-service.yaml -o openapi-bundled.yaml
	@echo "Generating code with ogen..."
	ogen --target pkg/api --package api --clean openapi-bundled.yaml
	@echo "Code generation complete!"

.PHONY: run
run: generate-api
	go run cmd/server/main.go

.PHONY: build
build: generate-api
	go build -o bin/server cmd/server/main.go

.PHONY: test
test:
	go test -v ./...

.PHONY: lint
lint:
	golangci-lint run

.PHONY: bench
bench:
	go test -bench=. -benchmem ./...

.PHONY: clean
clean:
	rm -rf bin/ pkg/api/ openapi-bundled.yaml

.PHONY: docker-build
docker-build:
	docker build -t $(SERVICE):latest .

.PHONY: docker-run
docker-run:
	docker run -p $(PORT):$(PORT) --env-file .env $(SERVICE):latest


