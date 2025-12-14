# Issue: #176
FROM golang:1.21-alpine AS builder

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

# Build optimized binary with performance flags
RUN CGO_ENABLED=0 GOOS=linux go build \
    -a \
    -installsuffix cgo \
    -ldflags="-w -s -X main.version=$(git describe --tags --always --dirty)" \
    -gcflags="all=-l -B" \
    -o quest-engine-service \
    .

# Final stage - minimal runtime image
FROM alpine:latest

# Install ca-certificates for HTTPS
RUN apk --no-cache add ca-certificates tzdata

# Create non-root user
RUN adduser -D -s /bin/sh appuser

WORKDIR /root/

# Copy binary from builder stage
COPY --from=builder /app/quest-engine-service .

# Copy any additional assets if needed
# COPY --from=builder /app/assets ./assets

# Change ownership to non-root user
RUN chown appuser:appuser quest-engine-service

# Switch to non-root user
USER appuser

# Expose ports
EXPOSE 8300 8301 9092

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
    CMD wget --no-verbose --tries=1 --spider http://localhost:8300/health || exit 1

# Run the binary
CMD ["./quest-engine-service"]
