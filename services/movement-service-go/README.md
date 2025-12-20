# Movement Service (Go)

Микросервис для сохранения позиций игроков в БД (персистентность).

## Назначение

Этот сервис работает **вместе** с `realtime-gateway-go`, который уже обрабатывает синхронизацию движения в реальном
времени через GameState.

**Movement Service** отвечает за:

- Сохранение позиций игроков в БД для персистентности
- Получение сохраненных позиций при входе в игру
- История перемещений (опционально)

## Технологии

- **Go 1.24+**
- **PostgreSQL** - хранение позиций
- **Redis** - кэширование
- **WebSocket** - получение GameState от gateway
- **gRPC/REST** - API для получения позиций

## Архитектура

```
realtime-gateway-go (GameState синхронизация в реальном времени)
    ↓ WebSocket
movement-service-go (сохранение позиций в БД)
    ↓ PostgreSQL
character_positions (таблица с позициями)
```

## API

### REST Endpoints

- `GET /api/v1/movement/:characterId/position` - получить сохраненную позицию персонажа
- `POST /api/v1/movement/:characterId/position` - сохранить позицию персонажа
- `GET /api/v1/movement/:characterId/history` - получить историю перемещений (опционально)

## База данных

Таблица `mvp_core.character_positions` хранит:

- `character_id` - ID персонажа
- `position_x`, `position_y`, `position_z` - координаты
- `yaw` - поворот
- `updated_at` - время обновления

## Интеграция с Gateway

Сервис может получать GameState от gateway через:

1. **WebSocket подписка** - подписаться на GameState от gateway
2. **Периодический опрос** - запрашивать позиции через API gateway
3. **События** - gateway отправляет события о изменении позиций

## Запуск

```bash
cd services/movement-service-go
go mod download
go run .
```

## Конфигурация

- `DATABASE_URL` - URL подключения к PostgreSQL
- `REDIS_URL` - URL подключения к Redis
- `GATEWAY_URL` - URL для подключения к realtime-gateway (WebSocket)
- `UPDATE_INTERVAL` - интервал сохранения позиций (по умолчанию: 5 секунд)

