FROM golang:1.24-alpine AS builder

WORKDIR /app

RUN apk add --no-cache make nodejs npm git ca-certificates tzdata

COPY services/companion-service-go/go.mod services/companion-service-go/go.sum ./
RUN go mod download

RUN go install github.com/oapi-codegen/oapi-codegen/v2/cmd/oapi-codegen@latest

COPY services/companion-service-go ./services/companion-service-go
COPY proto ./proto

WORKDIR /app/services/companion-service-go

RUN make generate-api || true

RUN go mod tidy
RUN CGO_ENABLED=0 GOOS=linux go build -o companion-service -ldflags="-w -s -extldflags '-static'" .

FROM alpine:latest

RUN apk --no-cache add ca-certificates tzdata

WORKDIR /app

RUN addgroup -g 1000 appuser && \
    adduser -D -u 1000 -G appuser appuser

COPY --from=builder /app/services/companion-service-go/companion-service /app/
COPY --from=builder /usr/share/zoneinfo /usr/share/zoneinfo

RUN chown -R appuser:appuser /app

USER appuser

EXPOSE 8089/tcp 9099

HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:9099/metrics || exit 1

ENTRYPOINT ["/app/companion-service"]
