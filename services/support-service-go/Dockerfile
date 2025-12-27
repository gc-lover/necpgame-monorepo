# Multi-stage build for support-service-go
FROM golang:1.24-alpine AS builder

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

# Build optimized binary with performance flags
RUN CGO_ENABLED=0 GOOS=linux go build \
    -a -installsuffix cgo \
    -ldflags='-w -s -extldflags "-static"' \
    -tags netgo \
    -o support-service .

# Final stage
FROM alpine:latest

# Install ca-certificates for HTTPS
RUN apk --no-cache add ca-certificates

# Create non-root user
RUN adduser -D -s /bin/sh support

# Set working directory
WORKDIR /app

# Copy binary from builder
COPY --from=builder /app/support-service .

# Change ownership
RUN chown support:support support-service

# Switch to non-root user
USER support

# Expose port
EXPOSE 8087

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
    CMD wget --no-verbose --tries=1 --spider http://localhost:8087/health || exit 1

# Run the binary
CMD ["./support-service"]

# Issue: #288

