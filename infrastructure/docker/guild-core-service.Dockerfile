# Guild Core Service Dockerfile - Production Optimized
# Multi-stage build for minimal image size and security

# Build stage
FROM golang:1.21-alpine AS builder

# Install build dependencies
RUN apk add --no-cache \
    git \
    ca-certificates \
    tzdata \
    && update-ca-certificates

# Set working directory
WORKDIR /build

# Copy go mod files
COPY go.mod go.sum ./

# Download dependencies
RUN go mod download && go mod verify

# Copy source code
COPY . .

# Build the binary with optimizations
RUN CGO_ENABLED=0 GOOS=linux GOARCH=amd64 go build \
    -ldflags="-w -s -X main.version=v2.0.0 -X main.buildTime=$(date -u +'%Y-%m-%dT%H:%M:%SZ') -extldflags '-static'" \
    -a -installsuffix cgo \
    -o guild-core-service \
    ./cmd/guild-core-service

# Runtime stage
FROM scratch

# Import from builder
COPY --from=builder /etc/ssl/certs/ca-certificates.crt /etc/ssl/certs/
COPY --from=builder /usr/share/zoneinfo /usr/share/zoneinfo

# Create non-root user
COPY --from=builder /etc/passwd /etc/passwd
COPY --from=builder /etc/group /etc/group

# Copy binary
COPY --from=builder /build/guild-core-service /guild-core-service

# Create necessary directories
COPY --from=builder /tmp /tmp

# Set permissions
RUN chmod +x /guild-core-service

# Switch to non-root user
USER 1000:1000

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
    CMD ["/guild-core-service", "--health-check"]

# Expose port
EXPOSE 8084

# Set environment variables
ENV PORT=8084
ENV GIN_MODE=release
ENV GOMEMLIMIT=150MiB

# Run the binary
ENTRYPOINT ["/guild-core-service"]