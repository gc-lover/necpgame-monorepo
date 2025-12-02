# Player Orders Service

Issue: #81

## Описание

Сервис для управления заказами от игроков (Player Orders).

## OpenAPI Спецификации

- `proto/openapi/social-player-orders-core-service.yaml` - основные операции
- `proto/openapi/social-player-orders-multi-executor-service.yaml` - мульти-исполнитель
- `proto/openapi/social-player-orders-schemas.yaml` - схемы данных

## Endpoints

### Core Operations
- `GET /api/v1/social/orders` - список заказов
- `POST /api/v1/social/orders/create` - создать заказ
- `GET /api/v1/social/orders/{orderId}` - детали заказа
- `POST /api/v1/social/orders/{orderId}/accept` - принять заказ
- `POST /api/v1/social/orders/{orderId}/complete` - завершить заказ
- `POST /api/v1/social/orders/{orderId}/cancel` - отменить заказ

### Multi-Executor Operations
- `POST /api/v1/social/orders/multi/create` - создать групповой заказ
- `POST /api/v1/social/orders/multi/{orderId}/join` - присоединиться к заказу
- `POST /api/v1/social/orders/multi/{orderId}/leave` - покинуть заказ

## Сборка

```bash
# Install dependencies
go mod download

# Generate API code
make generate-api

# Build
go build -o social-player-orders-service .

# Run
./social-player-orders-service
```

## Docker

```bash
docker build -t social-player-orders-service:latest .
docker run -p 8090:8090 social-player-orders-service:latest
```

## Порты

- **8090** - HTTP API
- **9090** - Metrics

## База данных

Требуется PostgreSQL с схемой `social.player_orders`.

## Зависимости

- `github.com/go-chi/chi/v5` - HTTP router
- `github.com/oapi-codegen/runtime` - OpenAPI runtime
- `github.com/jackc/pgx/v5` - PostgreSQL driver
- `github.com/prometheus/client_golang` - Metrics

