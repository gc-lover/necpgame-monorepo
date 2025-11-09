## NPC Relationships API

- `npc-relationships.yaml` — основной OpenAPI документ для REST эндпоинтов управления отношениями с NPC, интеграций с world-service и economy-service, а также Kafka событий.
- `npc-relationships-models.yaml` — базовые схемы и параметрические определения (статус отношений, модификаторы классов и фракций, события, метрики, кейсы модерации).
- `npc-relationships-models-operations.yaml` — модели запросов/ответов операций, payload Kafka топиков и определения очередей модерации и романтики.

Лимит в 400 строк соблюдён за счёт разбиения спецификации на отдельные файлы.

