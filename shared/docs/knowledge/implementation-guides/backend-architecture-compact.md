# Backend Architecture Overview (Compact)

**Версия:** 2.1.0  
**Дата:** 2025-11-08 12:20

---

## Архитектура

**Микросервисы:**
- API Gateway (`https://api.necp.game/v1`)
- Auth Service
- Character Service
- Gameplay Service
- Social Service
- Economy Service
- World Service

**Принципы:**
- Production доступ только через gateway; прямые порты (`8081-8086`) используются в dev.
- Каждая OpenAPI спецификация содержит `info.x-microservice` с именем, портом и доменом сервиса.
- Монолитные модули удалены; новые доменные сервисы проходят архитектурное ревью.

**Детали:** См. файлы в `backend/` и `architecture/`

---

## История изменений

- v2.0.0 (2025-11-07 02:29) - Compact (< 100 строк)
- v1.0.0 (2025-11-06) - Создан (522 строки)

