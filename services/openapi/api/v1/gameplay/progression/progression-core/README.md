## Progression Core API

- `progression-core.yaml` — основной контракт progression системы: состояние, начисление опыта, распределение атрибутов/навыков, respec и синхронизация.
- `progression-core-models.yaml` — базовые схемы уровня, очков, атрибутов, навыков и истории.
- `progression-core-models-operations.yaml` — структуры запросов/ответов, Kafka события `gameplay.progression.*` и батчевые payloadы.

Файлы ≤ 400 строк каждый благодаря разбиению спецификации.

