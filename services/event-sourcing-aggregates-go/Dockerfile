# Issue: #2217
# Multi-stage Dockerfile for optimized Event Sourcing Aggregates service

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
    -o /bin/event-sourcing-aggregates \
    -trimpath \
    -tags=netgo \
    ./main.go

# Stage 2: Runtime
FROM alpine:latest

# Install runtime dependencies
RUN apk --no-cache add ca-certificates tzdata

# Create non-root user
RUN addgroup -g 1001 -S eventsourcing && \
    adduser -u 1001 -S eventsourcing -G eventsourcing

WORKDIR /app

# Copy binary from builder
COPY --from=builder /bin/event-sourcing-aggregates .

# Set ownership
RUN chown eventsourcing:eventsourcing event-sourcing-aggregates

# Switch to non-root user
USER eventsourcing

# Expose ports
EXPOSE 8082

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
    CMD wget --no-verbose --tries=1 --spider http://localhost:8082/health || exit 1

# Set default environment variables
ENV DATABASE_URL="postgres://user:password@localhost:5432/necpgame_event_sourcing?sslmode=disable"
ENV GOGC=150

# Run the service
ENTRYPOINT ["/app/event-sourcing-aggregates"]
