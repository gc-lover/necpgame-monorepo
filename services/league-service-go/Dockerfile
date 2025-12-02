# Issue: #44
FROM golang:1.24-alpine AS builder

WORKDIR /app

RUN apk add --no-cache make nodejs npm git

COPY go.mod go.sum ./
RUN go mod download

COPY . .

# Generate API code from OpenAPI spec
RUN make generate-api || true

RUN go mod tidy
RUN CGO_ENABLED=0 GOOS=linux go build -o league-service -ldflags="-w -s" .

FROM alpine:latest

RUN apk --no-cache add ca-certificates

WORKDIR /app

RUN addgroup -S appuser && adduser -S appuser -G appuser
USER appuser

COPY --from=builder /app/league-service /app/

EXPOSE 8093/tcp 9093

HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8093/health || exit 1

ENTRYPOINT ["/app/league-service"]

