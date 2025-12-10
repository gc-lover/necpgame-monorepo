# Issue: #1636
# Stage 1: Builder
FROM golang:1.24-alpine AS builder

WORKDIR /app

# Install Node.js and npm for redocly/cli
RUN apk add --no-cache nodejs npm

# Copy go.mod and go.sum to download dependencies
COPY go.mod go.sum ./

# Download Go dependencies
RUN go mod download
RUN go mod tidy

# Copy the rest of the application code
COPY . .

# Install ogen
RUN go install github.com/ogen-go/ogen/cmd/ogen@v1.18.0

# Bundle OpenAPI spec and generate ogen code
# Use npx directly as make is not available during Docker build
RUN npx --yes @redocly/cli bundle openapi-bundled.yaml -o openapi-bundled.yaml
RUN ogen --target pkg/api --package api --clean openapi-bundled.yaml

# Build the application
RUN go build -o /battle-pass-core-service-go ./cmd/server

# Stage 2: Final
FROM alpine:latest

WORKDIR /app

# Copy the compiled binary from the builder stage
COPY --from=builder /battle-pass-core-service-go .

# Expose the port the service listens on
EXPOSE 8080

# Run the application
CMD ["/app/battle-pass-core-service-go"]

