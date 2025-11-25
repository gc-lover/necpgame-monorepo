FROM golang:1.24-alpine AS builder

WORKDIR /app/services/feedback-service-go

RUN apk add --no-cache make nodejs npm git

COPY services/feedback-service-go/go.mod services/feedback-service-go/go.sum ./
RUN go mod download

RUN go install github.com/oapi-codegen/oapi-codegen/v2/cmd/oapi-codegen@latest

COPY proto/ ../../proto/
COPY services/feedback-service-go/ ./

RUN make generate-api || true

RUN go mod tidy
RUN CGO_ENABLED=0 GOOS=linux go build -o feedback-service -ldflags="-w -s" .

FROM alpine:latest

RUN apk --no-cache add ca-certificates

WORKDIR /app

COPY --from=builder /app/services/feedback-service-go/feedback-service /app/

EXPOSE 8080/tcp 9090

ENTRYPOINT ["/app/feedback-service"]