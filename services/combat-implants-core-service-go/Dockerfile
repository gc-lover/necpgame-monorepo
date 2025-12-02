# Issue: #58
FROM golang:1.24-alpine AS builder

WORKDIR /app

RUN apk add --no-cache make nodejs npm

COPY go.mod go.sum* ./
RUN go mod download || true

COPY . .

RUN make generate-api || true
RUN go mod tidy || true
RUN CGO_ENABLED=0 GOOS=linux go build -o combat-implants-core-service -ldflags="-w -s" .

FROM alpine:latest

RUN apk --no-cache add ca-certificates
RUN addgroup -S appuser && adduser -S appuser -G appuser

WORKDIR /app

COPY --from=builder /app/combat-implants-core-service /app/

RUN chown -R appuser:appuser /app
USER appuser

EXPOSE 8083/tcp 9093/tcp

HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:9093/metrics || exit 1

ENTRYPOINT ["/app/combat-implants-core-service"]

