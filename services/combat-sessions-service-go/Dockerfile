# Issue: #130

FROM golang:1.24-alpine AS builder

WORKDIR /app

# Install dependencies for code generation
RUN apk add --no-cache make nodejs npm

# Copy go mod files
COPY go.mod go.sum* ./
RUN go mod download || true

# Copy source
COPY . .

# Generate API code
RUN make generate-api || true

# Tidy dependencies
RUN go mod tidy

# Build
RUN CGO_ENABLED=0 GOOS=linux go build -o combat-sessions-service -ldflags="-w -s" .

FROM alpine:latest

RUN apk --no-cache add ca-certificates

WORKDIR /app

COPY --from=builder /app/combat-sessions-service /app/

EXPOSE 8090/tcp 9090/tcp

HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:9090/metrics || exit 1

USER nobody

ENTRYPOINT ["/app/combat-sessions-service"]

