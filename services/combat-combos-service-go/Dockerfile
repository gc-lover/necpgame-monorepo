FROM golang:1.24-alpine AS builder
WORKDIR /app
RUN apk add --no-cache make nodejs npm git
COPY go.mod go.sum ./
RUN go mod download
RUN go install github.com/oapi-codegen/oapi-codegen/v2/cmd/oapi-codegen@latest
COPY . .
COPY ../../proto/openapi/combat-combos-service.yaml ../../proto/openapi/
RUN make generate-api || true
RUN go mod tidy
RUN CGO_ENABLED=0 GOOS=linux go build -o combat-combos-service -ldflags="-w -s" .

FROM alpine:latest
RUN apk --no-cache add ca-certificates
WORKDIR /app
COPY --from=builder /app/combat-combos-service /app/
EXPOSE 8091/tcp 9091
ENTRYPOINT ["/app/combat-combos-service"]
