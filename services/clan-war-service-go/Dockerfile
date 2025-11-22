FROM golang:1.24-alpine AS builder

WORKDIR /app

COPY go.mod go.sum ./
RUN go mod download

COPY . .

RUN go mod tidy
RUN CGO_ENABLED=0 GOOS=linux go build -o clan-war-service -ldflags="-w -s" .

FROM alpine:latest

RUN apk --no-cache add ca-certificates

WORKDIR /app

COPY --from=builder /app/clan-war-service /app/

EXPOSE 8092/tcp 9092

ENTRYPOINT ["/app/clan-war-service"]

