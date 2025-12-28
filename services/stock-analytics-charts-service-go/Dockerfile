# Build stage
FROM golang:1.24-alpine AS builder

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

# Build the application with optimizations for financial data processing
RUN CGO_ENABLED=0 GOOS=linux go build \
    -a -installsuffix cgo \
    -ldflags '-extldflags "-static"' \
    -tags netgo \
    -o stock-analytics-charts-service \
    -trimpath \
    .

# Final stage
FROM alpine:latest

# Install runtime dependencies
RUN apk --no-cache add ca-certificates tzdata && \
    adduser -D -s /bin/sh app

# Set working directory
WORKDIR /app

# Copy binary from builder stage
COPY --from=builder /app/stock-analytics-charts-service .

# Change ownership to non-root user
RUN chown app:app stock-analytics-charts-service

# Switch to non-root user
USER app

# Expose port
EXPOSE 8150

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
    CMD wget --no-verbose --tries=1 --spider http://localhost:8150/health || exit 1

# Run the application
CMD ["./stock-analytics-charts-service"]

