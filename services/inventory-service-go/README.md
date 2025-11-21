# Inventory Service (Go)

Микросервис для управления инвентарем игроков.

## Технологии

- **Go 1.24+**
- **PostgreSQL** - хранение инвентаря
- **Redis** - кэширование
- **Prometheus** - метрики
- **Gorilla Mux** - HTTP роутинг

## Архитектура

```
inventory-service-go/
  main.go                    - точка входа
  server/
    logger.go                - структурированное логирование
    metrics.go               - Prometheus метрики
    inventory_repository.go  - работа с PostgreSQL
    inventory_service.go     - бизнес-логика инвентаря
    http_server.go           - HTTP сервер и handlers
  models/
    inventory.go             - модели данных
  go.mod                     - зависимости
  Dockerfile                 - сборка образа
```

## API

### REST Endpoints

- `GET /api/v1/inventory/:characterId` - получить инвентарь персонажа
- `POST /api/v1/inventory/:characterId/items` - добавить предмет
- `DELETE /api/v1/inventory/:characterId/items/:itemId` - удалить предмет
- `POST /api/v1/inventory/:characterId/equip` - экипировать предмет
- `POST /api/v1/inventory/:characterId/unequip/:itemId` - снять предмет
- `GET /health` - health check

### Примеры запросов

**Получить инвентарь:**
```bash
curl http://localhost:8085/api/v1/inventory/{characterId}
```

**Добавить предмет:**
```bash
curl -X POST http://localhost:8085/api/v1/inventory/{characterId}/items \
  -H "Content-Type: application/json" \
  -d '{"item_id": "weapon_001", "stack_count": 1}'
```

**Экипировать предмет:**
```bash
curl -X POST http://localhost:8085/api/v1/inventory/{characterId}/equip \
  -H "Content-Type: application/json" \
  -d '{"item_id": "weapon_001", "equip_slot": "primary"}'
```

## База данных

Миграция `V1_6__inventory_tables.sql` создает таблицы:
- `mvp_core.character_inventory` - основной инвентарь
- `mvp_core.character_items` - предметы в инвентаре
- `mvp_core.item_templates` - шаблоны предметов (reference data)

## Запуск

### Локально

```bash
cd services/inventory-service-go
go mod download
go run .
```

Или через скрипт:
```bash
scripts\run\inventory-service.cmd
```

### Docker

```bash
docker build -t necpgame-inventory-service-go:latest services/inventory-service-go
docker run -p 8085:8085 -p 9094:9090 \
  -e DATABASE_URL=postgresql://postgres:postgres@localhost:5432/necpgame?sslmode=disable \
  -e REDIS_URL=redis://localhost:6379/0 \
  necpgame-inventory-service-go:latest
```

### Docker Compose

```bash
docker-compose up inventory-service
```

## Конфигурация

Переменные окружения:
- `ADDR` - адрес HTTP сервера (по умолчанию: `0.0.0.0:8085`)
- `METRICS_ADDR` - адрес метрик (по умолчанию: `:9090`)
- `DATABASE_URL` - URL подключения к PostgreSQL
- `REDIS_URL` - URL подключения к Redis
- `LOG_LEVEL` - уровень логирования (по умолчанию: `info`)

## Метрики

Prometheus метрики доступны на `/metrics`:
- `inventory_requests_total` - количество запросов
- `inventory_request_duration_seconds` - длительность обработки запросов
- `inventory_items_total` - количество предметов в инвентаре
- `inventory_errors_total` - количество ошибок

## Интеграция

Интегрируется с:
- **PostgreSQL** - хранение данных
- **Redis** - кэширование (TTL 5 минут)
- **Prometheus** - метрики
- **character-service** - получение информации о персонаже (TODO)
- **realtime-gateway** - синхронизация инвентаря в реальном времени (TODO)

## Особенности

- OK Автоматическое создание инвентаря при первом обращении
- OK Кэширование в Redis (5 минут TTL)
- OK Поддержка стеков предметов
- OK Экипировка предметов с автоматической заменой
- OK Soft-delete для предметов
- OK Метрики Prometheus
- OK Структурированное логирование

## TODO

- [ ] WebSocket синхронизация для realtime обновлений
- [ ] Интеграция с character-service для валидации персонажей
- [ ] Интеграция с economy-service для покупки/продажи предметов
- [ ] Банк/стэш для дополнительного хранения
- [ ] Сортировка и фильтрация предметов