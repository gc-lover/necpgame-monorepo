FROM golang:1.24-alpine AS builder

WORKDIR /app

# Copy go mod files
COPY go.mod go.sum ./
RUN go mod download

# Copy source code
COPY . .

# Build the binary
RUN CGO_ENABLED=0 GOOS=linux go build -a -installsuffix cgo -o matchmaking-service ./cmd/api

FROM alpine:latest

RUN apk --no-cache add ca-certificates
WORKDIR /root/

COPY --from=builder /app/matchmaking-service .
COPY --from=builder /app/internal/config/ ./internal/config/

EXPOSE 8082

CMD ["./matchmaking-service"]