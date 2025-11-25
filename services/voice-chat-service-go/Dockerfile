FROM golang:1.24-alpine AS builder

WORKDIR /app/services/voice-chat-service-go

RUN apk add --no-cache make nodejs npm git

COPY services/voice-chat-service-go/go.mod services/voice-chat-service-go/go.sum ./
RUN go mod download

RUN go install github.com/oapi-codegen/oapi-codegen/v2/cmd/oapi-codegen@latest

COPY proto/ ../../proto/
COPY services/voice-chat-service-go/ ./

RUN make generate-api || true

RUN go mod tidy
RUN CGO_ENABLED=0 GOOS=linux go build -o voice-chat-service -ldflags="-w -s" .

FROM alpine:latest

RUN apk --no-cache add ca-certificates

WORKDIR /app

COPY --from=builder /app/services/voice-chat-service-go/voice-chat-service /app/

EXPOSE 8096/tcp 9096

ENTRYPOINT ["/app/voice-chat-service"]