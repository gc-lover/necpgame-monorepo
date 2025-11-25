FROM golang:1.24-alpine AS builder

WORKDIR /app

RUN apk add --no-cache make nodejs npm git

COPY services/inventory-service-go/go.mod services/inventory-service-go/go.sum ./
RUN go mod download

RUN go install github.com/oapi-codegen/oapi-codegen/v2/cmd/oapi-codegen@latest

COPY services/inventory-service-go ./services/inventory-service-go
COPY proto ./proto

WORKDIR /app/services/inventory-service-go

RUN make generate-api || true

RUN go mod tidy
RUN CGO_ENABLED=0 GOOS=linux go build -o inventory-service -ldflags="-w -s" .

FROM alpine:latest

RUN apk --no-cache add ca-certificates

WORKDIR /app

COPY --from=builder /app/services/inventory-service-go/inventory-service /app/inventory-service

EXPOSE 8083/tcp 9093

ENTRYPOINT ["/app/inventory-service"]
