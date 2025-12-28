# Multi-stage build for heavy analytical computations
FROM golang:1.24-alpine AS builder

# Install build dependencies for scientific computing
RUN apk add --no-cache git ca-certificates tzdata \
    && apk add --no-cache --virtual .build-deps gcc g++ musl-dev

# Set working directory
WORKDIR /app

# Copy go mod files
COPY go.mod go.sum ./

# Download dependencies (including heavy math libraries)
RUN go mod download

# Copy source code
COPY . .

# Build the application with optimizations for mathematical computations
RUN CGO_ENABLED=1 GOOS=linux go build \
    -a -installsuffix cgo \
    -ldflags '-extldflags "-static" -s -w' \
    -tags netgo \
    -o stock-analytics-tools-service \
    -trimpath \
    .

# Final stage with minimal runtime dependencies
FROM alpine:latest

# Install runtime dependencies for scientific computing
RUN apk --no-cache add ca-certificates tzdata \
    && adduser -D -s /bin/sh app

# Set working directory
WORKDIR /app

# Copy binary from builder stage
COPY --from=builder /app/stock-analytics-tools-service .

# Create directory for ML models (if needed)
RUN mkdir -p /models

# Change ownership to non-root user
RUN chown -R app:app /app

# Switch to non-root user
USER app

# Expose port
EXPOSE 8151

# Health check with longer timeout for analytical operations
HEALTHCHECK --interval=60s --timeout=30s --start-period=120s --retries=3 \
    CMD wget --no-verbose --tries=1 --spider http://localhost:8151/health || exit 1

# Resource limits for heavy computations
ENV GOMAXPROCS=4

# Run the application
CMD ["./stock-analytics-tools-service"]


