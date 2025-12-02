# Economy Player Market Service

Player Market Service управляет торговлей между игроками: объявления, покупки, отзывы, аналитика.

## Issue

Related to: #42, #61

## OpenAPI Specifications

- `proto/openapi/economy-player-market-service.yaml` (499 строк) - Player Market operations
- `proto/openapi/economy-player-market-schemas.yaml` - Data schemas

## API Endpoints

### Player Market

- `GET /api/v1/economy/market/listings` - Список объявлений
- `POST /api/v1/economy/market/listings` - Создать объявление
- `GET /api/v1/economy/market/listings/{listingId}` - Получить объявление
- `POST /api/v1/economy/market/listings/{listingId}/purchase` - Купить товар
- `GET /api/v1/economy/market/transactions` - История транзакций
- `POST /api/v1/economy/market/reviews` - Оставить отзыв
- `GET /api/v1/economy/market/sellers/{sellerId}/stats` - Статистика продавца

## Database Tables

- `player_market_listings` - Объявления на маркете
- `player_market_transactions` - Транзакции
- `player_market_reviews` - Отзывы продавцов
- `player_market_seller_stats` - Статистика продавцов

## Build

```bash
# Generate API code
make generate-api

# Build
go build -o economy-player-market-service .

# Run
./economy-player-market-service

# Docker
docker build -t economy-player-market-service:latest .
```

## Environment Variables

- `DATABASE_URL` - PostgreSQL connection string (default: `postgres://postgres:postgres@localhost:5432/necpgame?sslmode=disable`)
- `PORT` - Service port (default: `8094`)

## Architecture

- **SOLID compliant** - разделение на layers (handlers, service, repository)
- **Generated code** - API types/server сгенерированы через oapi-codegen
- **Health checks** - `/health` endpoint
- **Metrics** - `/metrics` endpoint (Prometheus)

