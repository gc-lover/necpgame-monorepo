FROM golang:1.24-alpine AS builder

WORKDIR /app/services/clan-war-service-go

RUN apk add --no-cache make nodejs npm git

COPY services/clan-war-service-go/go.mod services/clan-war-service-go/go.sum ./
RUN go mod download

RUN go install github.com/oapi-codegen/oapi-codegen/v2/cmd/oapi-codegen@latest

COPY proto/ ../../proto/
COPY services/clan-war-service-go/ ./

RUN make generate-api || true

RUN go mod tidy
RUN CGO_ENABLED=0 GOOS=linux go build -o clan-war-service -ldflags="-w -s" .

FROM alpine:latest

RUN apk --no-cache add ca-certificates

WORKDIR /app

COPY --from=builder /app/services/clan-war-service-go/clan-war-service /app/

EXPOSE 8092/tcp 9092

ENTRYPOINT ["/app/clan-war-service"]
