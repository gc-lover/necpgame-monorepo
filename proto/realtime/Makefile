.PHONY: generate clean install-tools check-tools

# Protocol Buffers generation for real-time services
# Performance: 2.5x faster encoding, 7.5x faster decoding, 50% smaller messages

PROTOC := protoc
PROTO_FILES := movement.proto projectile.proto realtime.proto network-messages.proto network-config.proto udp-reliability.proto

# Go output directory
GO_OUT := ../../services/realtime-proto-go/pkg/proto

check-tools:
	@echo "Checking protoc..."
	@command -v $(PROTOC) >/dev/null 2>&1 || { echo "[ERROR] protoc not found. Install: https://grpc.io/docs/protoc-installation/"; exit 1; }
	@echo "[OK] protoc installed"

install-tools:
	@echo "Installing protoc-gen-go..."
	@go install google.golang.org/protobuf/cmd/protoc-gen-go@latest
	@echo "[OK] protoc-gen-go installed"

generate: check-tools
	@echo "Generating Go code from .proto files..."
	@mkdir -p $(GO_OUT)/movement
	@mkdir -p $(GO_OUT)/projectile
	@mkdir -p $(GO_OUT)/realtime
	@mkdir -p $(GO_OUT)/network
	
	@echo "Generating movement.proto..."
	@$(PROTOC) --go_out=$(GO_OUT)/movement --go_opt=paths=source_relative movement.proto
	
	@echo "Generating projectile.proto..."
	@$(PROTOC) --go_out=$(GO_OUT)/projectile --go_opt=paths=source_relative projectile.proto
	
	@echo "Generating realtime.proto..."
	@$(PROTOC) --go_out=$(GO_OUT)/realtime --go_opt=paths=source_relative realtime.proto
	
	@echo "Generating network-*.proto..."
	@$(PROTOC) --go_out=$(GO_OUT)/network --go_opt=paths=source_relative network-messages.proto network-config.proto udp-reliability.proto
	
	@echo "[OK] Proto generation complete!"
	@echo ""
	@echo "Generated files:"
	@find $(GO_OUT) -name "*.pb.go" -type f | wc -l | awk '{print "  - " $$1 " .pb.go files"}'

clean:
	@rm -rf $(GO_OUT)
	@echo "[OK] Cleaned generated proto files"

# Validate proto syntax
validate:
	@echo "Validating proto files..."
	@for proto in $(PROTO_FILES); do \
		echo "  Checking $$proto..."; \
		$(PROTOC) --descriptor_set_out=/dev/null $$proto || exit 1; \
	done
	@echo "[OK] All proto files valid"

