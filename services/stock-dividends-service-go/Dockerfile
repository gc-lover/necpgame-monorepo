# Multi-stage build for stock-dividends-service
FROM golang:1.24-alpine AS builder

WORKDIR /app

RUN apk add --no-cache git ca-certificates tzdata

# Copy go modules
COPY go.mod go.sum ./
RUN go mod download
RUN go mod tidy

# Copy source code (with already generated API code)
COPY . .

# Build
RUN CGO_ENABLED=0 GOOS=linux go build -o stock-dividends-service -ldflags="-w -s" .

FROM alpine:latest

RUN apk --no-cache add ca-certificates tzdata

WORKDIR /app

RUN addgroup -g 1000 appuser && \
    adduser -D -u 1000 -G appuser appuser

COPY --from=builder /app/stock-dividends-service /app/stock-dividends-service
COPY --from=builder /usr/share/zoneinfo /usr/share/zoneinfo

RUN chown -R appuser:appuser /app

USER appuser

EXPOSE 8146/tcp

HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8146/health || exit 1

ENTRYPOINT ["/app/stock-dividends-service"]


