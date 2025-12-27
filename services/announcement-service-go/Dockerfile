# Announcement Service Dockerfile
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
RUN CGO_ENABLED=0 GOOS=linux go build -a -installsuffix cgo -o announcement-service-go .

# Final stage
FROM alpine:latest

# Install ca-certificates for HTTPS requests
RUN apk --no-cache add ca-certificates

# Create non-root user
RUN addgroup -S announcement && adduser -S announcement -G announcement

WORKDIR /root/

# Copy the binary from builder stage
COPY --from=builder /app/announcement-service-go .

# Change ownership
RUN chown announcement:announcement announcement-service-go

# Switch to non-root user
USER announcement

# Expose the port the app runs on
EXPOSE 8094

# Run the application
ENTRYPOINT ["./announcement-service-go"]



