FROM golang:1.24-alpine AS builder

WORKDIR /app/services/leaderboard-service-go

RUN apk add --no-cache make nodejs npm git

COPY services/leaderboard-service-go/go.mod services/leaderboard-service-go/go.sum ./
RUN go mod download

RUN go install github.com/oapi-codegen/oapi-codegen/v2/cmd/oapi-codegen@latest

COPY proto/ ../../proto/
COPY services/leaderboard-service-go/ ./

RUN make generate-api || true

RUN go mod tidy
RUN CGO_ENABLED=0 GOOS=linux go build -o leaderboard-service -ldflags="-w -s" .

FROM alpine:latest

RUN apk --no-cache add ca-certificates

WORKDIR /app

COPY --from=builder /app/services/leaderboard-service-go/leaderboard-service /app/

EXPOSE 8089/tcp 9089

ENTRYPOINT ["/app/leaderboard-service"]