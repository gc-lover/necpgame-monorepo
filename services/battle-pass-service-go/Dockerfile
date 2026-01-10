# Build stage
FROM golang:1.21-alpine AS builder

# Install build dependencies
RUN apk add --no-cache git ca-certificates tzdata

# Set working directory
WORKDIR /app

# Copy go mod files first for better caching
COPY go.mod go.sum ./

# Download dependencies with caching
RUN go mod download && go mod verify

# Copy source code
COPY . .

# Build optimized binary
# -ldflags="-w -s": Remove debug info and symbol table for smaller binary
# -trimpath: Remove file system paths from binary
RUN CGO_ENABLED=0 GOOS=linux GOARCH=amd64 go build \
    -a \
    -installsuffix cgo \
    -ldflags="-w -s -X main.version=$(git describe --tags --always --dirty)" \
    -trimpath \
    -o main \
    .

# Test stage (optional, can be enabled for CI/CD)
FROM builder AS tester
RUN go test -v ./... -coverprofile=coverage.out

# Final production stage
FROM alpine:latest

# Install runtime dependencies only
RUN apk --no-cache add ca-certificates tzdata && \
    addgroup -g 1001 -S appgroup && \
    adduser -u 1001 -S appuser -G appgroup

# Create app directory with correct permissions
WORKDIR /app

# Copy binary from builder stage
COPY --from=builder --chown=appuser:appgroup /app/main .

# Create non-root user and set permissions
USER appuser

# Expose port
EXPOSE 8080

# Environment variables for production optimization
ENV GOGC=50 \
    GOMAXPROCS=2 \
    GODEBUG=madvdontneed=1

# Health check with optimized settings
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
    CMD wget --no-verbose --tries=1 --spider http://localhost:8080/health || exit 1

# Use exec form for proper signal handling
CMD ["./main"]