# Issue: #1600 - realtime-gateway-go Makefile
# This service uses WebSocket/UDP for real-time game state (no REST API)
# Protobuf generation for real-time game state (UDP)
# Gains: 2.5x faster encoding, 7.5x faster decoding, 50% smaller

.PHONY: generate-proto clean bench bench-json bench-quick

PROTOC := protoc
PROTO_DIR := ../../proto/realtime
PROTO_OUT_DIR := pkg/proto

generate-proto:
	@echo "Generating protobuf code for real-time game state..."
	@mkdir -p $(PROTO_OUT_DIR)
	@$(PROTOC) --go_out=$(PROTO_OUT_DIR) \
	           --go_opt=paths=source_relative \
	           --proto_path=../../ \
	           $(PROTO_DIR)/realtime.proto \
	           $(PROTO_DIR)/network-messages.proto \
	           $(PROTO_DIR)/network-config.proto \
	           $(PROTO_DIR)/udp-reliability.proto
	@echo "вњ… Protobuf generation complete"
	@echo "Generated files:"
	@ls -lh $(PROTO_OUT_DIR)/*.pb.go || true

clean:

.PHONY: test bench-quick build

# Run tests
test:
	@go test -v ./...

# Quick benchmark (short duration)
bench-quick:
	@if [ -f "server/handlers_bench_test.go" ] || find . -name "*_bench_test.go" | grep -q .; then \
		go test -run=^\$\$ -bench=. -benchmem -benchtime=100ms ./server; \
	fi

# Build (runs tests and benchmarks first)
build: test bench-quick
	@CGO_ENABLED=0 go build -ldflags="-w -s" -o bin/realtime-gateway .
	@rm -rf $(PROTO_OUT_DIR)


.PHONY: bench bench-json bench-quick

# Run benchmarks (human-readable)
bench:
	go test -run=^ -bench=. -benchmem ./server

# Run benchmarks (JSON output for CI)
bench-json:
	@mkdir -p ../../.benchmarks/results
	go test -run=^ -bench=. -benchmem -json ./server > ../../.benchmarks/results/realtime-gateway_bench.json

# Quick benchmark (short duration)
bench-quick:
	go test -run=^ -bench=. -benchmem -benchtime=100ms ./server