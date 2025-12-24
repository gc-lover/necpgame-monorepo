# Jackie Welles NPC Service Dockerfile
# PERFORMANCE: Multi-stage build for minimal image size

# Build stage
FROM golang:1.21-alpine AS builder

# Install build dependencies
RUN apk add --no-cache git

# Set working directory
WORKDIR /app

# Copy go mod files
COPY go.mod go.sum ./

# Download dependencies
RUN go mod download

# Copy source code
COPY . .

# Build the application
RUN CGO_ENABLED=0 GOOS=linux go build -a -installsuffix cgo -o jackie-welles-service-go .

# Final stage
FROM alpine:latest

# Install ca-certificates for HTTPS requests
RUN apk --no-cache add ca-certificates

# Create non-root user
RUN addgroup -S jackie && adduser -S jackie -G jackie

WORKDIR /root/

# Copy the binary from builder stage
COPY --from=builder /app/jackie-welles-service-go .

# Change ownership
RUN chown jackie:jackie jackie-welles-service-go

# Switch to non-root user
USER jackie

# Expose port
EXPOSE 8089

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8089/health || exit 1

# Run the application
CMD ["./jackie-welles-service-go"]
