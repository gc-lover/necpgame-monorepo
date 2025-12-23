# Issue: #140889798
# Multi-stage Dockerfile for optimized Go service

# Build stage
FROM golang:1.25-alpine AS builder

# Install build dependencies
RUN apk add --no-cache git ca-certificates tzdata

# Set working directory
WORKDIR /app

# Copy go mod files
COPY go.mod go.sum ./

# Download dependencies
RUN go mod download

# Copy source code
COPY . .

# Build optimized binary
RUN CGO_ENABLED=0 GOOS=linux go build -a -installsuffix cgo \
    -ldflags="-w -s -X main.version=1.0.0" \
    -o session-service \
    -trimpath \
    -tags=netgo \
    ./main.go

# Runtime stage
FROM alpine:latest

# Install runtime dependencies
RUN apk --no-cache add ca-certificates tzdata \
    && adduser -D -s /bin/sh appuser

# Set working directory
WORKDIR /app

# Copy binary from builder stage
COPY --from=builder /app/session-service .

# Create logs directory
RUN mkdir -p /app/logs && chown -R appuser:appuser /app

# Switch to non-root user
USER appuser

# Expose port
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=5s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8080/health || exit 1

# Run the service
CMD ["./session-service"]
