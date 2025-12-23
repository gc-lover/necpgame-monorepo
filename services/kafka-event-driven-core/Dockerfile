# Issue: #2237
# Multi-stage Dockerfile for optimized Go Kafka event-driven service

# Stage 1: Builder
FROM golang:1.22-alpine AS builder

# Install build dependencies
RUN apk add --no-cache git ca-certificates

WORKDIR /app

# Copy go.mod and go.sum first for better caching
COPY go.mod go.sum ./

# Download dependencies
RUN go mod download && go mod verify

# Copy source code
COPY . .

# Build optimized binary
RUN CGO_ENABLED=0 GOOS=linux GOARCH=amd64 go build \
    -a -installsuffix cgo \
    -ldflags="-w -s -X main.version=1.0.0" \
    -o /bin/kafka-event-core \
    -trimpath \
    -tags=netgo \
    ./main.go

# Stage 2: Runtime
FROM alpine:latest

# Install runtime dependencies
RUN apk --no-cache add ca-certificates tzdata

# Create non-root user
RUN addgroup -g 1001 -S kafka && \
    adduser -u 1001 -S kafka -G kafka

WORKDIR /app

# Copy binary from builder
COPY --from=builder /bin/kafka-event-core .

# Set ownership
RUN chown kafka:kafka kafka-event-core

# Switch to non-root user
USER kafka

# Expose ports
EXPOSE 8081 9090

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
    CMD wget --no-verbose --tries=1 --spider http://localhost:8081/health || exit 1

# Set default environment variables
ENV KAFKA_BROKERS=localhost:9092
ENV GOGC=200

# Run the service
ENTRYPOINT ["/app/kafka-event-core"]
