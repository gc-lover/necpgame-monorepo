# Multi-stage build for cosmetic-service
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
RUN CGO_ENABLED=0 GOOS=linux go build -o cosmetic-service -ldflags="-w -s" .

FROM alpine:latest

RUN apk --no-cache add ca-certificates tzdata

WORKDIR /app

RUN addgroup -g 1000 appuser && \
    adduser -D -u 1000 -G appuser appuser

COPY --from=builder /app/cosmetic-service /app/cosmetic-service
COPY --from=builder /usr/share/zoneinfo /usr/share/zoneinfo

RUN chown -R appuser:appuser /app

USER appuser

EXPOSE 8119/tcp

HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
  CMD wget -q --spider http://localhost:8119/health || exit 1

ENTRYPOINT ["/app/cosmetic-service"]


