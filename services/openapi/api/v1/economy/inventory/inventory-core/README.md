## Inventory Core API

- `inventory-core.yaml` — основной контракт economy-service для инвентаря: снапшоты, pickup/drop, move/split, equip/unequip, transfers и аудит.
- `inventory-core-models.yaml` — базовые схемы предметов, слотов, веса, шаблонов и аудита.
- `inventory-core-models-operations.yaml` — структуры запросов/ответов, Kafka события `inventory.item.*`, метрики веса.

Каждый файл ≤ 400 строк: спецификация разбита на три части для соблюдения лимитов.

