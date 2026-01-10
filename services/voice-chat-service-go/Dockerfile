# Multi-stage build for voice-chat-service-go
# PERFORMANCE: Optimized for size and security
# SECURITY: Non-root user, minimal attack surface

# Build stage
FROM golang:1.21-alpine AS builder

# Install build dependencies
RUN apk add --no-cache git ca-certificates tzdata

# Create non-root user for build
RUN adduser -D -s /bin/sh -u 1001 appuser

# Set working directory
WORKDIR /build

# Copy go mod files
COPY go.mod go.sum ./

# Download dependencies
RUN go mod download

# Copy source code
COPY . .

# Build the application with optimizations
# PERFORMANCE: CGO_ENABLED=0 for static linking, -ldflags for size optimization
RUN CGO_ENABLED=0 GOOS=linux go build \
    -ldflags='-w -s -extldflags "-static"' \
    -a -installsuffix cgo \
    -o voice-chat-service \
    .

# Final stage - distroless for minimal attack surface
FROM gcr.io/distroless/static-debian11

# Copy CA certificates for HTTPS
COPY --from=builder /etc/ssl/certs/ca-certificates.crt /etc/ssl/certs/

# Copy timezone data
COPY --from=builder /usr/share/zoneinfo /usr/share/zoneinfo

# Create non-root user
USER 1001:1001

# Copy binary from builder
COPY --from=builder /build/voice-chat-service /

# Expose port
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
    CMD ["/voice-chat-service", "--health-check"]

# Run the application
ENTRYPOINT ["/voice-chat-service"]