FROM golang:1.23-alpine AS builder

WORKDIR /app

RUN apk add --no-cache make nodejs npm git

COPY services/stock-analytics-charts-service-go/go.mod services/stock-analytics-charts-service-go/go.sum ./
RUN go mod download

RUN go install github.com/oapi-codegen/oapi-codegen/v2/cmd/oapi-codegen@latest

COPY proto/ ../../proto/
COPY services/stock-analytics-charts-service-go/ ./

RUN npx -y @redocly/cli bundle ../../proto/openapi/stock-analytics-charts-service.yaml -o pkg/api/bundled.yaml || true
RUN oapi-codegen -package api -generate types,chi-server -o pkg/api/api.gen.go pkg/api/bundled.yaml || true

RUN go mod tidy
RUN CGO_ENABLED=0 GOOS=linux go build -o stock-analytics-charts-service -ldflags="-w -s" .

FROM alpine:latest

RUN apk --no-cache add ca-certificates

WORKDIR /app

COPY --from=builder /app/stock-analytics-charts-service /app/

EXPOSE 8080/tcp 9090

ENTRYPOINT ["/app/stock-analytics-charts-service"]

