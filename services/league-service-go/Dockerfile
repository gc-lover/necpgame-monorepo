# Multi-stage build for NECPGAME League Service
# Optimized for competitive gaming workloads

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

# Build optimized binary for competitive gaming workloads
RUN CGO_ENABLED=0 GOOS=linux go build \
    -ldflags='-w -s -extldflags "-static"' \
    -a -installsuffix cgo \
    -o league-service \
    .

# Final stage
FROM scratch

# Import certificates and timezone data
COPY --from=builder /etc/ssl/certs/ca-certificates.crt /etc/ssl/certs/
COPY --from=builder /usr/share/zoneinfo /usr/share/zoneinfo

# Create non-root user for security
ENV USER_UID=1001 USER_NAME=league-service
RUN echo "${USER_NAME}:x:${USER_UID}:${USER_UID}::/app:/usr/sbin/nologin" >> /etc/passwd

# Copy binary
COPY --from=builder /app/league-service /app/league-service

# Set ownership
RUN chown -R ${USER_UID}:${USER_UID} /app
USER ${USER_UID}

# Expose ports (HTTP, Metrics, Profiling)
EXPOSE 8082 9092 6062

# Health check for competitive gaming readiness
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
    CMD ["/app/league-service", "--health-check"]

# Run service
ENTRYPOINT ["/app/league-service"]