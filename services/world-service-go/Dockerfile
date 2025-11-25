FROM golang:1.24-alpine AS builder

WORKDIR /app

RUN apk add --no-cache make nodejs npm

COPY go.mod go.sum ./
RUN go mod download

COPY . .

RUN make generate-api || true

RUN go mod tidy
RUN CGO_ENABLED=0 GOOS=linux go build -o world-service -ldflags="-w -s" .

FROM alpine:latest

RUN apk --no-cache add ca-certificates

WORKDIR /app

COPY --from=builder /app/world-service /app/

EXPOSE 8096/tcp 9096

ENTRYPOINT ["/app/world-service"]
