FROM golang:1.24-alpine AS builder

WORKDIR /app

RUN apk add --no-cache make git ca-certificates tzdata

COPY services/stock-futures-service-go/go.mod services/stock-futures-service-go/go.sum ./
RUN go mod download
RUN go mod tidy

COPY services/stock-futures-service-go ./services/stock-futures-service-go

WORKDIR /app/services/stock-futures-service-go

RUN CGO_ENABLED=0 GOOS=linux go build -o stock-futures-service -ldflags="-w -s -extldflags '-static'" .

FROM alpine:latest

RUN apk --no-cache add ca-certificates tzdata

WORKDIR /app

RUN addgroup -g 1000 appuser && \
    adduser -D -u 1000 -G appuser appuser

COPY --from=builder /app/services/stock-futures-service-go/stock-futures-service /app/
COPY --from=builder /usr/share/zoneinfo /usr/share/zoneinfo

RUN chown -R appuser:appuser /app

USER appuser

EXPOSE 8158/tcp 9258

HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
  CMD wget -q --spider http://localhost:9258/metrics || exit 1

ENTRYPOINT ["/app/stock-futures-service"]

