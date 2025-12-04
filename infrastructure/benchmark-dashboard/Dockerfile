FROM golang:1.24-alpine AS builder

WORKDIR /app

COPY go.mod .
COPY main.go .
COPY dashboard.html .

RUN CGO_ENABLED=0 go build -ldflags="-w -s" -o benchmark-dashboard main.go

FROM alpine:latest

RUN apk --no-cache add ca-certificates

WORKDIR /app

COPY --from=builder /app/benchmark-dashboard .
COPY --from=builder /app/dashboard.html .

EXPOSE 8080

CMD ["./benchmark-dashboard"]

