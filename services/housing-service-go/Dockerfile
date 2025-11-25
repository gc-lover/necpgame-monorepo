FROM golang:1.24-alpine AS builder

WORKDIR /app

RUN apk add --no-cache make nodejs npm git

COPY services/housing-service-go/go.mod services/housing-service-go/go.sum ./
RUN go mod download

RUN go install github.com/oapi-codegen/oapi-codegen/v2/cmd/oapi-codegen@latest

COPY services/housing-service-go ./services/housing-service-go
COPY proto ./proto

WORKDIR /app/services/housing-service-go

RUN make generate-api || true

RUN go mod tidy
RUN CGO_ENABLED=0 GOOS=linux go build -o housing-service -ldflags="-w -s" .

FROM alpine:latest

RUN apk --no-cache add ca-certificates

WORKDIR /app

COPY --from=builder /app/services/housing-service-go/housing-service /app/housing-service

EXPOSE 8090/tcp 9090

ENTRYPOINT ["/app/housing-service"]
