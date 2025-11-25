FROM golang:1.24-alpine AS builder

WORKDIR /app

RUN apk add --no-cache make git

COPY go.mod go.sum ./
RUN go mod download

COPY . .

RUN go mod tidy

RUN CGO_ENABLED=0 GOOS=linux go build -o maintenance-service -ldflags="-w -s" .

FROM alpine:latest

RUN apk --no-cache add ca-certificates tzdata

WORKDIR /app

COPY --from=builder /app/maintenance-service /app/

EXPOSE 8097/tcp 8098/tcp

ENV PORT=8097

HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8097/health || exit 1

ENTRYPOINT ["/app/maintenance-service"]

