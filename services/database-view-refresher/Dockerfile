# Issue: #1583 - Database View Refresher Docker image
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
RUN CGO_ENABLED=0 GOOS=linux go build -ldflags="-w -s" -o view-refresher .

# Runtime image
FROM alpine:latest

RUN apk --no-cache add ca-certificates tzdata

WORKDIR /app

# Copy binary
COPY --from=builder /app/view-refresher /app/view-refresher

# Run as non-root
RUN adduser -D -u 1000 appuser
USER appuser

ENTRYPOINT ["/app/view-refresher"]



