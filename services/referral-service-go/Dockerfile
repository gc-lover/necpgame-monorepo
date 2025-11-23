FROM golang:1.24-alpine AS builder

WORKDIR /app

COPY go.mod go.sum ./
RUN go mod download

COPY . .

RUN go mod tidy
RUN CGO_ENABLED=0 GOOS=linux go build -o referral-service -ldflags="-w -s" .

FROM alpine:latest

RUN apk --no-cache add ca-certificates

WORKDIR /app

COPY --from=builder /app/referral-service /app/

EXPOSE 8094/tcp 9094

ENTRYPOINT ["/app/referral-service"]

