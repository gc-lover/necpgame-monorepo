# Issue: #164
FROM golang:1.24-alpine AS builder

WORKDIR /app

RUN apk add --no-cache make nodejs npm

COPY go.mod go.sum ./
RUN go mod download

COPY . .

RUN make generate-api || true

RUN go mod tidy
RUN CGO_ENABLED=0 GOOS=linux go build -o progression-experience-service -ldflags="-w -s" .

FROM alpine:latest

RUN apk --no-cache add ca-certificates
RUN adduser -D -u 1000 appuser

WORKDIR /app

COPY --from=builder /app/progression-experience-service /app/

RUN chown -R appuser:appuser /app

USER appuser

EXPOSE 8135/tcp 9235

HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
  CMD wget -q --spider http://localhost:9235/metrics || exit 1

ENTRYPOINT ["/app/progression-experience-service"]

