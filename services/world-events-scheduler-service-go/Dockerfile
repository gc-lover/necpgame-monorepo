# Issue: #44
FROM golang:1.24-alpine AS builder

WORKDIR /app

RUN apk add --no-cache make nodejs npm

COPY services/world-events-scheduler-service-go/go.mod services/world-events-scheduler-service-go/go.sum ./
RUN go mod download

COPY proto/ ../../proto/
COPY services/world-events-scheduler-service-go/ ./

RUN make generate-api || true

RUN go mod tidy
RUN CGO_ENABLED=0 GOOS=linux go build -o world-events-scheduler-service -ldflags="-w -s" .

FROM alpine:latest

RUN apk --no-cache add ca-certificates

WORKDIR /app

COPY --from=builder /app/world-events-scheduler-service /app/

RUN adduser -D -u 1000 appuser
USER appuser

EXPOSE 8090/tcp 9090

HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8090/health || exit 1

ENTRYPOINT ["/app/world-events-scheduler-service"]

