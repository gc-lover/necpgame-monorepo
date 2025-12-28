# Optimized Dockerfile for NECPGAME Go Services
# Performance optimized for MMOFPS workloads
# Issue: Docker optimization for all Go services

# Build stage - optimized for fast builds and small images
FROM golang:1.24-alpine AS builder

# Performance: Install only essential build dependencies
RUN apk add --no-cache \
    git \
    ca-certificates \
    tzdata \
    && update-ca-certificates

# Security: Create non-root user for build
RUN addgroup -g 1001 -S appgroup && \
    adduser -u 1001 -S appuser -G appgroup

# Performance: Set working directory
WORKDIR /build

# Dependencies: Copy go mod files first for better caching
COPY go.mod go.sum ./

# Performance: Download dependencies with caching
RUN --mount=type=cache,target=/go/pkg/mod \
    go mod download && \
    go mod verify

# Security: Copy source with proper ownership
COPY --chown=appuser:appgroup . .

# Performance: Build optimized binary
# -ldflags for smaller binary size and security
# CGO_ENABLED=0 for static linking (smaller, faster startup)
RUN CGO_ENABLED=0 GOOS=linux GOARCH=amd64 \
    go build \
    -a \
    -installsuffix cgo \
    -ldflags="-w -s -extldflags '-static' \
             -X main.version=$(git describe --tags --always --dirty) \
             -X main.buildTime=$(date -u +'%Y-%m-%dT%H:%M:%SZ') \
             -X main.gitCommit=$(git rev-parse --short HEAD)" \
    -o /app/main \
    .

# Performance: Strip debug symbols (smaller binary)
RUN strip /app/main

# Runtime stage - optimized for production
FROM alpine:3.20 AS runtime

# Performance: Install only runtime dependencies
RUN apk add --no-cache \
    ca-certificates \
    tzdata \
    && update-ca-certificates \
    && rm -rf /var/cache/apk/*

# Security: Create non-root user
RUN addgroup -g 1001 -S appgroup && \
    adduser -u 1001 -S appuser -G appgroup

# Security: Don't run as root
USER appuser:appgroup

# Performance: Set working directory
WORKDIR /app

# Copy binary from builder stage
COPY --from=builder --chown=appuser:appgroup /app/main .

# Performance: Expose port
EXPOSE 8080

# Health check - optimized for MMOFPS monitoring
HEALTHCHECK --interval=15s --timeout=3s --start-period=5s --retries=2 \
    CMD wget --no-verbose --tries=1 --spider --timeout=3 \
        http://localhost:8080/health || exit 1

# Performance: Use exec form for proper signal handling
CMD ["./main"]

# Labels for better container management
LABEL org.opencontainers.image.title="NECPGAME Go Service" \
      org.opencontainers.image.description="Optimized Go microservice for NECPGAME MMOFPS" \
      org.opencontainers.image.vendor="NECPGAME" \
      org.opencontainers.image.version="1.0.0" \
      org.opencontainers.image.created="${BUILD_DATE}" \
      com.necpgame.service.type="microservice" \
      com.necpgame.performance.optimized="true"
