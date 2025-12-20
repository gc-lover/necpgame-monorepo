# OPTIMIZATION: Issue #2030 - Multi-stage build for voice chat security and minimal image size
FROM golang:1.21-alpine AS builder

# Install build dependencies
RUN apk add --no-cache git ca-certificates tzdata

# Set working directory
WORKDIR /build

# Copy go mod files
COPY go.mod go.sum ./

# Download dependencies
RUN go mod download && go mod verify

# Copy source code
COPY . .

# Build the binary with voice chat optimizations
ARG VERSION=dev
ARG BUILD_TIME
ARG GIT_COMMIT
RUN CGO_ENABLED=0 GOOS=linux GOARCH=amd64 go build \
    -trimpath \
    -mod=readonly \
    -ldflags "-X main.version=${VERSION} -X main.buildTime=${BUILD_TIME} -X main.gitCommit=${GIT_COMMIT} -s -w" \
    -o voice-chat-service \
    ./main.go

# Final stage - minimal runtime image
FROM scratch

# OPTIMIZATION: Issue #2030 - Copy CA certificates for HTTPS and voice streaming
COPY --from=builder /etc/ssl/certs/ca-certificates.crt /etc/ssl/certs/
COPY --from=builder /usr/share/zoneinfo /usr/share/zoneinfo

# Copy the binary
COPY --from=builder /build/voice-chat-service /voice-chat-service

# Create non-root user for security
USER 1000:1000

# Expose ports
EXPOSE 8088 8089 8090 6868

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
    CMD ["/voice-chat-service", "--health-check"] || exit 1

# Run the binary
ENTRYPOINT ["/voice-chat-service"]