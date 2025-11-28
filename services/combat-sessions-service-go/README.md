# Combat Sessions Service

Микросервис для управления боевыми сессиями в NECPGAME.

## API Endpoints

- POST `/api/v1/gameplay/combat/sessions` - создание сессии
- GET `/api/v1/gameplay/combat/sessions/{sessionId}` - информация о сессии
- POST `/api/v1/gameplay/combat/sessions/{sessionId}/start` - старт сессии
- POST `/api/v1/gameplay/combat/sessions/{sessionId}/end` - завершение сессии
- POST `/api/v1/gameplay/combat/sessions/{sessionId}/cancel` - отмена сессии
- POST `/api/v1/gameplay/combat/damage` - расчёт урона

## Разработка

```bash
make generate-api  # Генерация кода
go build .         # Сборка
```

## Метрики

- `combat_sessions_http_requests_total` - HTTP запросы
- Port: 8092

