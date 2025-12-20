# World Cities Service Dockerfile
# Issue: #140875381
# Multi-stage build for optimized production image

# Build stage
FROM golang:1.21-alpine AS builder

# Install git and ca-certificates (needed for Go modules)
RUN apk add --no-cache git ca-certificates

# Set working directory
WORKDIR /app

# Copy go mod files
COPY go.mod go.sum ./

# Download dependencies
RUN go mod download

# Copy source code
COPY . .

# Build the application
RUN CGO_ENABLED=0 GOOS=linux go build -a -installsuffix cgo -o world-cities-service .

# Production stage
FROM alpine:latest

# Install ca-certificates for HTTPS calls
RUN apk --no-cache add ca-certificates

# Create app user
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

# Set working directory
WORKDIR /app

# Copy binary from builder stage
COPY --from=builder /app/world-cities-service .

# Change ownership to app user
RUN chown appuser:appgroup world-cities-service

# Switch to non-root user
USER appuser

# Expose port
EXPOSE 8089

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8089/health || exit 1

# Run the application
CMD ["./world-cities-service"]

# Labels for better container management
LABEL maintainer="Backend Team <backend@necp.game>" \
      service="world-cities-service" \
      version="1.0.0" \
      description="World Cities Service for NECP MMOFPS Game"
