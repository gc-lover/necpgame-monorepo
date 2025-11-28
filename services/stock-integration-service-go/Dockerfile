FROM golang:1.23-alpine AS builder

WORKDIR /build

RUN apk add --no-cache git nodejs npm make bash

COPY go.mod go.sum ./
RUN go mod download

COPY . .

RUN npm install -g @redocly/cli@latest
RUN go install github.com/oapi-codegen/oapi-codegen/v2/cmd/oapi-codegen@latest

RUN make bundle-api generate-api

RUN CGO_ENABLED=0 GOOS=linux go build -a -installsuffix cgo -o stock-integration-service .

FROM alpine:latest

RUN apk --no-cache add ca-certificates

WORKDIR /root/

COPY --from=builder /build/stock-integration-service .

EXPOSE 8080

CMD ["./stock-integration-service"]

