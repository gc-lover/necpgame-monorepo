# Event Sourcing Aggregates Service - Enterprise-grade CQRS framework
# Issue: #2217
# Agent: Backend Agent

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

# Build the binary with optimizations
RUN CGO_ENABLED=0 GOOS=linux go build \
    -a -installsuffix cgo \
    -ldflags '-w -s -extldflags "-static"' \
    -o event-sourcing-aggregates \
    ./cmd/server

# Final stage
FROM scratch

# Copy CA certificates for HTTPS calls
COPY --from=builder /etc/ssl/certs/ca-certificates.crt /etc/ssl/certs/

# Copy timezone data
COPY --from=builder /usr/share/zoneinfo /usr/share/zoneinfo

# Copy binary
COPY --from=builder /app/event-sourcing-aggregates /

# Create non-root user
USER 1000:1000

# Expose port
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=5s --retries=3 \
    CMD ["/event-sourcing-aggregates", "--health-check"] || exit 1

# Run the binary
ENTRYPOINT ["/event-sourcing-aggregates"]