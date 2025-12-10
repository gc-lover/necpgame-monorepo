FROM golang:1.24-alpine AS builder

WORKDIR /app

RUN apk add --no-cache make git ca-certificates tzdata

COPY services/combat-sandevistan-service-go/go.mod services/combat-sandevistan-service-go/go.sum ./
RUN go mod download
RUN go mod tidy

COPY services/combat-sandevistan-service-go ./services/combat-sandevistan-service-go

WORKDIR /app/services/combat-sandevistan-service-go

RUN CGO_ENABLED=0 GOOS=linux go build -o combat-sandevistan-service -ldflags="-w -s -extldflags '-static'" .

FROM alpine:latest

RUN apk --no-cache add ca-certificates tzdata

WORKDIR /app

RUN addgroup -g 1000 appuser && \
    adduser -D -u 1000 -G appuser appuser

COPY --from=builder /app/services/combat-sandevistan-service-go/combat-sandevistan-service /app/
COPY --from=builder /usr/share/zoneinfo /usr/share/zoneinfo

RUN chown -R appuser:appuser /app

USER appuser

EXPOSE 8080/tcp 9090

HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:9090/metrics || exit 1

ENTRYPOINT ["/app/combat-sandevistan-service"]

