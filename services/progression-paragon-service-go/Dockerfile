FROM golang:1.24-alpine AS builder

WORKDIR /app/services/progression-paragon-service-go

RUN apk add --no-cache make nodejs npm git

COPY services/progression-paragon-service-go/go.mod services/progression-paragon-service-go/go.sum ./
RUN go mod download

RUN go install github.com/oapi-codegen/oapi-codegen/v2/cmd/oapi-codegen@latest

COPY proto/ ../../proto/
COPY services/progression-paragon-service-go/ ./

RUN make generate-api || true

RUN go mod tidy
RUN CGO_ENABLED=0 GOOS=linux go build -o progression-paragon-service -ldflags="-w -s" .

FROM alpine:latest

RUN apk --no-cache add ca-certificates

WORKDIR /app

COPY --from=builder /app/services/progression-paragon-service-go/progression-paragon-service /app/

EXPOSE 8084/tcp 9084

ENTRYPOINT ["/app/progression-paragon-service"]

