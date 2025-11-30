FROM golang:1.24-alpine AS builder

WORKDIR /app

RUN apk add --no-cache make nodejs npm git ca-certificates tzdata dos2unix

COPY services/maintenance-service-go/go.mod services/maintenance-service-go/go.sum ./
RUN go mod download

RUN go install github.com/oapi-codegen/oapi-codegen/v2/cmd/oapi-codegen@latest

COPY services/maintenance-service-go ./services/maintenance-service-go
COPY proto ./proto

WORKDIR /app/services/maintenance-service-go

RUN find . -name '*.go' -exec dos2unix {} \;

RUN go mod tidy
RUN CGO_ENABLED=0 GOOS=linux go build -o maintenance-service -ldflags="-w -s -extldflags '-static'" .

FROM alpine:latest

RUN apk --no-cache add ca-certificates tzdata

WORKDIR /app

RUN addgroup -g 1000 appuser && \
    adduser -D -u 1000 -G appuser appuser

COPY --from=builder /app/services/maintenance-service-go/maintenance-service /app/maintenance-service
COPY --from=builder /usr/share/zoneinfo /usr/share/zoneinfo

RUN chown -R appuser:appuser /app

USER appuser

EXPOSE 8097/tcp 8098/tcp

HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8097/health || exit 1

ENTRYPOINT ["/app/maintenance-service"]