# Issue: Implement admin-service-go based on OpenAPI specification
# Multi-stage build for optimal image size and security

# Build stage
FROM golang:1.21-alpine AS builder

# Install git and ca-certificates (needed for fetching dependencies)
RUN apk update && apk add --no-cache git ca-certificates && update-ca-certificates

# Create appuser for running the application (security best practice)
ENV USER=appuser
ENV UID=10001

# Create the user
RUN adduser \
    --disabled-password \
    --gecos "" \
    --home "/nonexistent" \
    --shell "/sbin/nologin" \
    --no-create-home \
    --uid "${UID}" \
    "${USER}"

WORKDIR /build

# Copy go mod files first for better caching
COPY go.mod go.sum ./

# Download dependencies (cached if go.mod/go.sum unchanged)
RUN go mod download
RUN go mod verify

# Copy source code
COPY . .

# Build the binary with performance optimizations
# -ldflags: strip debug info, set version, trim paths
# CGO_ENABLED=0: static linking for smaller image
# GOOS/GOARCH: target Linux for containerization
RUN CGO_ENABLED=0 GOOS=linux GOARCH=amd64 go build \
    -ldflags='-w -s -extldflags "-static"' \
    -a -installsuffix cgo \
    -o admin-service \
    .

# Runtime stage (distroless for minimal attack surface)
FROM gcr.io/distroless/static-debian11

# Copy CA certificates for HTTPS requests
COPY --from=builder /etc/ssl/certs/ca-certificates.crt /etc/ssl/certs/

# Copy the binary from builder stage
COPY --from=builder /build/admin-service /admin-service

# Use non-root user for security
USER appuser:appuser

# Expose port
EXPOSE 18085

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=5s --retries=3 \
    CMD ["/admin-service", "--health-check"] || exit 1

# Run the binary
ENTRYPOINT ["/admin-service"]
