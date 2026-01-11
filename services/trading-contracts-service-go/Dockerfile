# Trading Contracts Service Dockerfile
# Optimized for enterprise-grade trading workloads
# Issue: #2191 - Расширение системы торговых контрактов и заказов

# Build stage
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

# Build optimized binary for trading workloads
RUN CGO_ENABLED=0 GOOS=linux GOARCH=amd64 go build \
    -ldflags='-w -s -extldflags "-static"' \
    -a -installsuffix cgo \
    -o trading-contracts-service \
    ./cmd/server

# Final stage
FROM scratch

# Copy SSL certificates for HTTPS
COPY --from=builder /etc/ssl/certs/ca-certificates.crt /etc/ssl/certs/

# Copy timezone data
COPY --from=builder /usr/share/zoneinfo /usr/share/zoneinfo

# Create non-root user for security
ENV USER=trading
ENV UID=10001

# Create user
RUN adduser \
    --disabled-password \
    --gecos "" \
    --home "/nonexistent" \
    --shell "/sbin/nologin" \
    --no-create-home \
    --uid "${UID}" \
    "${USER}"

# Copy binary
COPY --from=builder /app/trading-contracts-service /trading-contracts-service

# Use non-root user
USER trading:trading

# Expose port
EXPOSE 8088

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
    CMD ["/trading-contracts-service", "--health-check"]

# Run service
ENTRYPOINT ["/trading-contracts-service"]