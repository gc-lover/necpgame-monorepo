# Issue: #backend-cosmetic_domain
# PERFORMANCE: Multi-stage build for minimal production image

# Build stage - optimized Go compilation
FROM golang:1.21-alpine AS builder

# PERFORMANCE: Install build dependencies
RUN apk add --no-cache git ca-certificates

# PERFORMANCE: Set working directory
WORKDIR /app

# PERFORMANCE: Copy go mod files first for better caching
COPY go.mod go.sum ./
RUN go mod download

# PERFORMANCE: Copy source code
COPY . .

# PERFORMANCE: Build optimized binary
RUN CGO_ENABLED=0 GOOS=linux go build \
    -a -installsuffix cgo \
    -ldflags="-w -s -X main.version=$(date -u +%Y%m%d-%H%M%S)" \
    -o cosmetic-domain-service \
    main.go

# Production stage - minimal runtime image
FROM alpine:latest

# PERFORMANCE: Install runtime dependencies only
RUN apk --no-cache add ca-certificates tzdata

# PERFORMANCE: Create non-root user
RUN addgroup -g 1001 -S appgroup && \
    adduser -u 1001 -S appuser -G appgroup

# PERFORMANCE: Set working directory
WORKDIR /app

# PERFORMANCE: Copy binary from builder stage
COPY --from=builder /app/cosmetic-domain-service .

# PERFORMANCE: Change ownership to non-root user
RUN chown appuser:appgroup cosmetic-domain-service

# PERFORMANCE: Switch to non-root user
USER appuser

# PERFORMANCE: Expose service port
EXPOSE 8200

# PERFORMANCE: Health check with optimized wget flags
HEALTHCHECK --interval=30s --timeout=10s --start-period=5s --retries=3 \
    CMD wget --no-verbose --tries=1 --spider http://localhost:8200/health || exit 1

# PERFORMANCE: Run with optimized settings
CMD ["./cosmetic-domain-service"]
