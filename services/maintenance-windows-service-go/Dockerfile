# Maintenance Windows Service Dockerfile
# PERFORMANCE: Multi-stage build for minimal image size

# Build stage
FROM golang:1.21-alpine AS builder

# Install build dependencies
RUN apk add --no-cache git ca-certificates

# Set working directory
WORKDIR /app

# Copy go mod files
COPY go.mod go.sum ./

# Download dependencies
RUN go mod download

# Copy source code
COPY . .

# Build the application
RUN CGO_ENABLED=0 GOOS=linux go build -a -installsuffix cgo -o maintenance-windows-service-go .

# Final stage
FROM alpine:latest

# Install ca-certificates for HTTPS requests and timezone data
RUN apk --no-cache add ca-certificates tzdata

# Create non-root user
RUN addgroup -S maintenance && adduser -S maintenance -G maintenance

WORKDIR /root/

# Copy the binary from builder stage
COPY --from=builder /app/maintenance-windows-service-go .

# Change ownership
RUN chown maintenance:maintenance maintenance-windows-service-go

# Switch to non-root user
USER maintenance

# Expose the port the app runs on
EXPOSE 8097

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8097/health || exit 1

# Run the application
ENTRYPOINT ["./maintenance-windows-service-go"]
