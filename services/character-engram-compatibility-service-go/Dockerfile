# Character Engram Compatibility Service Go - Multi-stage Docker build for enterprise-grade performance
FROM golang:1.21-alpine AS builder

# Install build dependencies
RUN apk update && apk add --no-cache git ca-certificates tzdata && update-ca-certificates

# Create appuser
RUN adduser -D -g '' appuser

# Set working directory
WORKDIR /build

# Copy go mod and sum files
COPY go.mod go.sum ./

# Download dependencies
RUN go mod download
RUN go mod verify

# Copy source code
COPY . .

# Build the binary with optimizations
RUN CGO_ENABLED=0 GOOS=linux GOARCH=amd64 go build \
    -ldflags='-w -s -extldflags "-static"' -a \
    -installsuffix cgo -o character-engram-compatibility-service-go .

# Final stage
FROM scratch

# Import from builder
COPY --from=builder /etc/ssl/certs/ca-certificates.crt /etc/ssl/certs/
COPY --from=builder /etc/passwd /etc/passwd

# Copy our static executable
COPY --from=builder /build/character-engram-compatibility-service-go /go/bin/character-engram-compatibility-service-go

# Use an unprivileged user
USER appuser

# Expose port
EXPOSE 8087

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
  CMD ["/go/bin/character-engram-compatibility-service-go", "--health-check"]

# Run the binary
ENTRYPOINT ["/go/bin/character-engram-compatibility-service-go"]
