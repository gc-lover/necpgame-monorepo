# OPTIMIZATION: Issue #1951 - Multi-stage build for security and minimal image size
FROM golang:1.24-alpine AS builder

# Install build dependencies
RUN apk add --no-cache git ca-certificates tzdata

# Set working directory
WORKDIR /build

# Copy go mod files
COPY services/inventory-service-go/go.mod services/inventory-service-go/go.sum ./

# Download dependencies
RUN go mod download && go mod verify

# Copy source code
COPY . .

# Build the binary with optimizations
ARG VERSION=dev
ARG BUILD_TIME
ARG GIT_COMMIT
WORKDIR /build/services/inventory-service-go
RUN go mod tidy
RUN CGO_ENABLED=0 GOOS=linux GOARCH=amd64 go build \
    -trimpath \
    -mod=readonly \
    -ldflags "-X main.version=${VERSION} -X main.buildTime=${BUILD_TIME} -X main.gitCommit=${GIT_COMMIT} -s -w" \
    -o inventory-service \
    .

# Final stage - minimal runtime image
FROM scratch

# OPTIMIZATION: Issue #1952 - Copy CA certificates for HTTPS
COPY --from=builder /etc/ssl/certs/ca-certificates.crt /etc/ssl/certs/
COPY --from=builder /usr/share/zoneinfo /usr/share/zoneinfo

# Copy the binary
COPY --from=builder /build/inventory-service /inventory-service

# Create non-root user for security
USER 1000:1000

# Expose ports
EXPOSE 8082 8083 6863

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
    CMD ["/inventory-service", "--health-check"] || exit 1

# Run the binary
ENTRYPOINT ["/inventory-service"]