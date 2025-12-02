# Issue: #44
FROM golang:1.24-alpine AS builder

WORKDIR /app

RUN apk add --no-cache make nodejs npm

COPY services/world-events-analytics-service-go/go.mod services/world-events-analytics-service-go/go.sum ./
RUN go mod download

COPY proto/ ../../proto/
COPY services/world-events-analytics-service-go/ ./

RUN make generate-api || true

RUN go mod tidy
RUN CGO_ENABLED=0 GOOS=linux go build -o world-events-analytics-service -ldflags="-w -s" .

FROM alpine:latest

RUN apk --no-cache add ca-certificates

WORKDIR /app

COPY --from=builder /app/world-events-analytics-service /app/

RUN adduser -D -u 1000 appuser
USER appuser

EXPOSE 8091/tcp 9090

HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8091/health || exit 1

ENTRYPOINT ["/app/world-events-analytics-service"]

