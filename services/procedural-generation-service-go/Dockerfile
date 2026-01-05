# Issue: #2266 - Refactor system-domain AI services
# PERFORMANCE: Multi-stage Docker build for enterprise-grade procedural generation service

# Build stage
FROM golang:1.21-alpine AS builder

# PERFORMANCE: Install only necessary packages
RUN apk add --no-cache git ca-certificates tzdata

# Set working directory
WORKDIR /app

# Copy go mod files
COPY go.mod go.sum ./

# Download dependencies
RUN go mod download

# Copy source code
COPY . .

# PERFORMANCE: Build with optimizations and static linking
RUN CGO_ENABLED=0 GOOS=linux go build \
    -a -installsuffix cgo \
    -ldflags="-s -w -X main.version=$(git describe --tags --always)" \
    -o procedural-generation-service ./main.go

# Final stage - minimal runtime image
FROM scratch

# PERFORMANCE: Copy only essential runtime files
COPY --from=builder /etc/ssl/certs/ca-certificates.crt /etc/ssl/certs/
COPY --from=builder /usr/share/zoneinfo /usr/share/zoneinfo
COPY --from=builder /app/procedural-generation-service /

# PERFORMANCE: Run as non-root user
USER 1000:1000

# Expose port
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=5s --retries=3 \
    CMD ["/procedural-generation-service", "--health-check"]

# PERFORMANCE: Use exec form for proper signal handling
ENTRYPOINT ["/procedural-generation-service"]
