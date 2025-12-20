# Issue: #1928 - Multi-stage Docker build for Meta Mechanics Service
# OPTIMIZATION: Minimal final image size, security hardening

# Build stage
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

# Build the binary with optimizations
RUN CGO_ENABLED=0 GOOS=linux go build \
    -a -installsuffix cgo \
    -ldflags="-w -s" \
    -o meta-mechanics-service \
    main.go

# Final stage
FROM alpine:latest

# Install ca-certificates for HTTPS calls
RUN apk --no-cache add ca-certificates

# Create non-root user
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

# Set working directory
WORKDIR /app

# Copy binary from builder stage
COPY --from=builder /app/meta-mechanics-service .

# Change ownership to non-root user
RUN chown appuser:appgroup meta-mechanics-service

# Switch to non-root user
USER appuser

# Expose port
EXPOSE 8170

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
    CMD wget --no-verbose --tries=1 --spider http://localhost:8170/health || exit 1

# Run the service
CMD ["./meta-mechanics-service"]
