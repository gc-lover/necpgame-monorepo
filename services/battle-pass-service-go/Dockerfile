FROM golang:1.24-alpine AS builder

WORKDIR /app

RUN apk add --no-cache make nodejs npm git

COPY services/battle-pass-service-go/go.mod services/battle-pass-service-go/go.sum ./
RUN go mod download

RUN go install github.com/oapi-codegen/oapi-codegen/v2/cmd/oapi-codegen@latest

COPY services/battle-pass-service-go ./services/battle-pass-service-go
COPY proto ./proto

WORKDIR /app/services/battle-pass-service-go

RUN make generate-api || true

RUN go mod tidy
RUN CGO_ENABLED=0 GOOS=linux go build -o battle-pass-service -ldflags="-w -s" .

FROM alpine:latest

RUN apk --no-cache add ca-certificates

WORKDIR /app

COPY --from=builder /app/services/battle-pass-service-go/battle-pass-service /app/battle-pass-service

EXPOSE 8091/tcp 9091

ENTRYPOINT ["/app/battle-pass-service"]
