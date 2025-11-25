FROM golang:1.24-alpine AS builder

WORKDIR /app/services/achievement-service-go

RUN apk add --no-cache make nodejs npm git

COPY services/achievement-service-go/go.mod services/achievement-service-go/go.sum ./
RUN go mod download

RUN go install github.com/oapi-codegen/oapi-codegen/v2/cmd/oapi-codegen@latest

COPY proto/ ../../proto/
COPY services/achievement-service-go/ ./

RUN make generate-api || true

RUN go mod tidy
RUN CGO_ENABLED=0 GOOS=linux go build -o achievement-service -ldflags="-w -s" .

FROM alpine:latest

RUN apk --no-cache add ca-certificates

WORKDIR /app

COPY --from=builder /app/services/achievement-service-go/achievement-service /app/

EXPOSE 8083/tcp 9093

ENTRYPOINT ["/app/achievement-service"]