# Tournament Service - Enterprise-Grade Tournament Management
# PERFORMANCE: Optimized for MMOFPS tournament operations
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

# Build optimized binary with tournament-specific flags
RUN CGO_ENABLED=0 GOOS=linux go build \
    -ldflags='-w -s -extldflags "-static"' \
    -a -installsuffix cgo \
    -o tournament-service \
    .

# Final stage - minimal runtime image
FROM scratch

# Copy CA certificates for HTTPS calls
COPY --from=builder /etc/ssl/certs/ca-certificates.crt /etc/ssl/certs/

# Copy binary
COPY --from=builder /app/tournament-service /

# Expose tournament service port
EXPOSE 8081

# Health check for tournament operations
HEALTHCHECK --interval=30s --timeout=10s --start-period=5s --retries=3 \
    CMD ["/tournament-service-healthcheck"]

# Run tournament service with optimizations
ENTRYPOINT ["/tournament-service"]
