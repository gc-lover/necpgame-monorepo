# Issue: #1582 - Database Partition Manager Docker image
# Multi-stage build for minimal image size

FROM golang:1.24-alpine AS builder

WORKDIR /app

# Copy go modules
COPY go.mod go.sum* ./
RUN go mod download
RUN go mod tidy || true

# Copy source
COPY . .

# Build static binary
RUN CGO_ENABLED=0 GOOS=linux go build -ldflags="-w -s" -o partition-manager .

# Runtime image
FROM alpine:latest

RUN apk --no-cache add ca-certificates tzdata

WORKDIR /app

# Copy binary
COPY --from=builder /app/partition-manager /app/partition-manager

# Run as non-root
RUN adduser -D -u 1000 appuser
USER appuser

ENTRYPOINT ["/app/partition-manager"]



