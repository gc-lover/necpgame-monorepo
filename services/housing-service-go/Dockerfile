# Housing Service Go - Enterprise-Grade Production Container
# Issue: #2254

# Use distroless base image for security and minimal attack surface
FROM gcr.io/distroless/static-debian12:nonroot

# Copy the binary with minimal permissions
COPY bin/housing-service /housing-service

# Use non-root user for security
USER nonroot:nonroot

# Health check for container orchestration
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
    CMD ["/housing-service", "--health-check"]

# Expose port
EXPOSE 8082

# Set security options
ENV GIN_MODE=release

# Run the service
ENTRYPOINT ["/housing-service"]

# Multi-stage build for optimized image size
FROM golang:1.24-alpine AS builder

# Install build dependencies
RUN apk add --no-cache git ca-certificates tzdata

# Set working directory
WORKDIR /app

# Copy go mod files
COPY go.mod go.sum ./

# Download dependencies with caching
RUN go mod download

# Copy source code
COPY . .

# Build with optimizations for production
RUN CGO_ENABLED=0 GOOS=linux go build \
    -a -installsuffix cgo \
    -ldflags="-s -w -X main.version=$(git rev-parse --short HEAD)" \
    -o bin/housing-service \
    ./main.go

# Final stage - minimal runtime image
FROM gcr.io/distroless/static-debian12:nonroot

# Copy CA certificates for HTTPS calls
COPY --from=builder /etc/ssl/certs/ca-certificates.crt /etc/ssl/certs/

# Copy timezone data
COPY --from=builder /usr/share/zoneinfo /usr/share/zoneinfo

# Copy the binary
COPY --from=builder /app/bin/housing-service /housing-service

# Create non-root user
USER nonroot:nonroot

# Health check endpoint
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
    CMD ["/housing-service", "--health-check"] || exit 1

# Expose port
EXPOSE 8082

# Set production environment
ENV GIN_MODE=release \
    PORT=8082

# Run with security features
ENTRYPOINT ["/housing-service"]


