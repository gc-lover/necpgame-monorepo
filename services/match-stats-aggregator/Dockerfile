# Issue: #2214
# Dockerfile for Match Statistics Aggregator Service
# Multi-stage build for optimal image size and performance

# Build stage
FROM golang:1.21-alpine AS builder

# Install build dependencies
RUN apk add --no-cache git ca-certificates tzdata

# Set working directory
WORKDIR /build

# Copy go mod files
COPY go.mod go.sum ./

# Download dependencies
RUN go mod download

# Copy source code
COPY . .

# Build optimized binary
RUN CGO_ENABLED=0 GOOS=linux GOARCH=amd64 go build \
    -a -installsuffix cgo \
    -ldflags="-w -s -extldflags '-static'" \
    -o match-stats-aggregator ./main.go

# Runtime stage
FROM alpine:latest

# Install runtime dependencies
RUN apk --no-cache add ca-certificates tzdata \
    && adduser -D -s /bin/sh appuser

# Set working directory
WORKDIR /app

# Copy binary from builder stage
COPY --from=builder /build/match-stats-aggregator .

# Change ownership to non-root user
RUN chown appuser:appuser match-stats-aggregator

# Switch to non-root user
USER appuser

# Expose port
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
    CMD wget --no-verbose --tries=1 --spider http://localhost:8080/health || exit 1

# Default command with production settings
CMD ["./match-stats-aggregator", \
     "--http=:8080", \
     "--redis=${REDIS_ADDR:-redis:6379}", \
     "--redis-pass=${REDIS_PASSWORD:-}", \
     "--redis-db=${REDIS_DB:-0}", \
     "--log-level=${LOG_LEVEL:-info}", \
     "--workers=${WORKERS:-4}", \
     "--buffer-size=${BUFFER_SIZE:-10000}"]

# Labels for better container management
LABEL maintainer="NECPGAME Team" \
      description="Real-time Match Statistics Aggregation Service for MMOFPS" \
      version="1.0.0" \
      org.opencontainers.image.source="https://github.com/gc-lover/necpgame-monorepo" \
      org.opencontainers.image.description="High-performance match statistics aggregator with Redis caching and event-driven processing"
