# syntax=docker/dockerfile:1

FROM golang:1.23-alpine AS builder
WORKDIR /app
COPY go.mod go.sum ./
RUN go mod download
COPY . .
RUN go mod tidy
RUN CGO_ENABLED=0 GOOS=linux go build -o matchmaking -ldflags="-w -s" .

FROM alpine:latest
WORKDIR /app
COPY --from=builder /app/matchmaking /app/
EXPOSE 9090
ENTRYPOINT ["/app/matchmaking"]

