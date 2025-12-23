# Issue: #140889770
# Multi-stage Dockerfile for optimized Go service

# Build stage
FROM golang:1.25-alpine AS builder

# PERFORMANCE: Install only necessary packages
RUN apk add --no-cache git ca-certificates tzdata

# Set working directory
WORKDIR /app

# PERFORMANCE: Copy go mod files first for better caching
COPY go.mod go.sum ./
RUN go mod download

# Copy source code
COPY . .

# PERFORMANCE: Build optimized binary
RUN CGO_ENABLED=0 GOOS=linux go build \
    -a \
    -installsuffix cgo \
    -ldflags="-w -s -X main.version=1.0.0" \
    -o narrative-service \
    -trimpath \
    -tags=netgo \
    ./main.go

# Final stage
FROM alpine:latest

# PERFORMANCE: Install only runtime dependencies
RUN apk --no-cache add ca-certificates tzdata \
    && adduser -D -s /bin/sh appuser

# Set working directory
WORKDIR /app

# Copy binary from builder
COPY --from=builder /app/narrative-service .

# PERFORMANCE: Use non-root user
USER appuser

# Expose port
EXPOSE 8080

# HEALTHCHECK: Docker health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=5s --retries=3 \
    CMD wget --no-verbose --tries=1 --spider http://localhost:8080/health || exit 1

# Run the binary
CMD ["./narrative-service"]
