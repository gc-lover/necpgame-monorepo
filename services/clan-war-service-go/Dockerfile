FROM golang:1.24-alpine AS builder

WORKDIR /app

RUN apk add --no-cache make nodejs npm git ca-certificates tzdata

COPY services/clan-war-service-go/go.mod services/clan-war-service-go/go.sum ./
RUN go mod download

RUN go install github.com/ogen-go/ogen/cmd/ogen@latest || true

COPY services/clan-war-service-go ./services/clan-war-service-go
COPY proto ./proto

WORKDIR /app/services/clan-war-service-go

RUN make generate-api || true

RUN go mod tidy
RUN CGO_ENABLED=0 GOOS=linux go build -o clan-war-service -ldflags="-w -s -extldflags '-static'" .

FROM alpine:latest

RUN apk --no-cache add ca-certificates tzdata

WORKDIR /app

RUN addgroup -g 1000 appuser && \
    adduser -D -u 1000 -G appuser appuser

COPY --from=builder /app/services/clan-war-service-go/clan-war-service /app/
COPY --from=builder /usr/share/zoneinfo /usr/share/zoneinfo

RUN chown -R appuser:appuser /app

USER appuser

EXPOSE 8092/tcp 9092

HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:9092/metrics || exit 1

ENTRYPOINT ["/app/clan-war-service"]
