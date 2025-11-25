FROM golang:1.24-alpine AS builder

WORKDIR /app/services/character-service-go

RUN apk add --no-cache make nodejs npm git

COPY services/character-service-go/go.mod services/character-service-go/go.sum ./
RUN go mod download

RUN go install github.com/oapi-codegen/oapi-codegen/v2/cmd/oapi-codegen@latest

COPY proto/ ../../proto/
COPY services/character-service-go/ ./

RUN make generate-api || true

RUN go mod tidy
RUN CGO_ENABLED=0 GOOS=linux go build -o character-service -ldflags="-w -s" .

FROM alpine:latest

RUN apk --no-cache add ca-certificates

WORKDIR /app

COPY --from=builder /app/services/character-service-go/character-service /app/

EXPOSE 8081/tcp 9081

ENTRYPOINT ["/app/character-service"]