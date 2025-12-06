# Issue: #1560

# ============================================================================
# Builder stage
# ============================================================================
FROM golang:1.24-alpine AS builder

WORKDIR /app

# Install build dependencies
RUN apk add --no-cache make nodejs npm git

# Copy go modules files
COPY go.mod go.sum ./
RUN go mod download

# Copy source code
COPY . .

# Generate API code from OpenAPI spec
RUN make generate-api || true

# Tidy dependencies
RUN go mod tidy

# Build binary
RUN CGO_ENABLED=0 GOOS=linux go build -o projectile-core-service -ldflags="-w -s" .

# ============================================================================
# Runtime stage
# ============================================================================
FROM alpine:latest

# Install ca-certificates for HTTPS
RUN apk --no-cache add ca-certificates

# Create non-root user
RUN addgroup -g 1000 appuser && \
    adduser -D -u 1000 -G appuser appuser

WORKDIR /app

# Copy binary from builder
COPY --from=builder /app/projectile-core-service /app/

# Change ownership
RUN chown -R appuser:appuser /app

# Switch to non-root user
USER appuser

# Expose ports
EXPOSE 8091/tcp 9091/tcp

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:9091/metrics || exit 1

# Run service
ENTRYPOINT ["/app/projectile-core-service"]





















