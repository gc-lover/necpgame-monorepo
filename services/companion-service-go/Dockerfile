FROM golang:1.24-alpine AS builder

WORKDIR /app

RUN apk add --no-cache make nodejs npm git

COPY services/companion-service-go/go.mod services/companion-service-go/go.sum ./
RUN go mod download

RUN go install github.com/oapi-codegen/oapi-codegen/v2/cmd/oapi-codegen@latest

COPY services/companion-service-go ./services/companion-service-go
COPY proto ./proto

WORKDIR /app/services/companion-service-go

RUN make generate-api || true

RUN go mod tidy
RUN CGO_ENABLED=0 GOOS=linux go build -o companion-service -ldflags="-w -s" .

FROM alpine:latest

RUN apk --no-cache add ca-certificates

WORKDIR /app

COPY --from=builder /app/services/companion-service-go/companion-service /app/companion-service

EXPOSE 8091/tcp 9091

ENTRYPOINT ["/app/companion-service"]
