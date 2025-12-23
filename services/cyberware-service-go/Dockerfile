# Issue: #2226
# PERFORMANCE: Multi-stage build for minimal cyberware service image

# Build stage
FROM golang:1.25-alpine AS builder

# PERFORMANCE: Install build dependencies
RUN apk add --no-cache git ca-certificates tzdata

# PERFORMANCE: Set working directory
WORKDIR /app

# PERFORMANCE: Copy go mod files first for better caching
COPY go.mod go.sum ./

# PERFORMANCE: Download dependencies
RUN go mod download

# PERFORMANCE: Copy source code
COPY . .

# PERFORMANCE: Build optimized binary
RUN CGO_ENABLED=0 GOOS=linux GOARCH=amd64 go build \
    -ldflags="-w -s -X main.version=$(date -u +%Y%m%d-%H%M%S)" \
    -o /app/cyberware-service \
    ./main.go

# Final stage
FROM alpine:latest

# PERFORMANCE: Install runtime dependencies only
RUN apk --no-cache add ca-certificates tzdata

# PERFORMANCE: Create non-root user
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

# PERFORMANCE: Set working directory
WORKDIR /app

# PERFORMANCE: Copy binary from builder
COPY --from=builder /app/cyberware-service .

# PERFORMANCE: Change ownership to non-root user
RUN chown appuser:appgroup cyberware-service

# PERFORMANCE: Switch to non-root user
USER appuser

# PERFORMANCE: Expose port
EXPOSE 8080

# PERFORMANCE: Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
    CMD wget --no-verbose --tries=1 --spider http://localhost:8080/health || exit 1

# PERFORMANCE: Run service
CMD ["./cyberware-service"]
