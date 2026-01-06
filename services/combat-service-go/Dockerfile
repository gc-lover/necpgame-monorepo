# Combat Service - Enterprise-Grade Real-Time Combat System
# PERFORMANCE: Optimized for high-frequency combat operations
# Issue: #2278 - Mass ogen Migration - 80+ Services Performance Upgrade

FROM golang:1.21-alpine AS builder

# Install build dependencies
RUN apk add --no-cache git ca-certificates

# Set working directory
WORKDIR /app

# Copy go mod files
COPY go.mod go.sum ./

# Download dependencies
RUN go mod download

# Copy source code
COPY . .

# Build optimized binary with combat-specific flags
RUN CGO_ENABLED=0 GOOS=linux go build \
    -ldflags='-w -s -extldflags "-static"' \
    -a -installsuffix cgo \
    -o combat-service \
    .

# Final stage - minimal runtime image
FROM scratch

# Copy CA certificates for HTTPS calls
COPY --from=builder /etc/ssl/certs/ca-certificates.crt /etc/ssl/certs/

# Copy binary
COPY --from=builder /app/combat-service /

# Expose combat service port
EXPOSE 8083

# Health check for combat operations
HEALTHCHECK --interval=15s --timeout=5s --start-period=3s --retries=3 \
    CMD ["/combat-service-healthcheck"]

# Run combat service with optimizations
ENTRYPOINT ["/combat-service"]
