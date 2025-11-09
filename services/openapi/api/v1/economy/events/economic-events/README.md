## Economic Events API

- `economic-events.yaml` — основной контракт: CRUD событий, анонсы, активация/отмена, метрики, feed и планировщик.
- `economic-events-models.yaml` — базовые схемы событий, эффектов, расписаний, объявлений, отмен и метрик.
- `economic-events-models-operations.yaml` — запросы/ответы, WebSocket/long polling payload, Kafka события `economy.events.*`.

Все файлы соответствуют лимиту ≤ 400 строк и описывают структуру для economy-service.
