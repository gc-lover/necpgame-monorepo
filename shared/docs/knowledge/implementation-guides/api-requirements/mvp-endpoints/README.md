# API Endpoints для MVP - Навигация

**Версия:** 1.0.2  
**Дата:** 2025-11-07  
**Статус:** approved  
**api-readiness:** ready

---

## Микросервисная архитектура

**Все endpoints доступны через API Gateway:** http://localhost:8080

**Распределение по микросервисам:**
- `/api/v1/auth/*` → auth-service (8081) ✅ Реализовано
- `/api/v1/characters/*` → character-service (8082) 📋 Планируется
- `/api/v1/gameplay/*` → gameplay-service (8083) 📋 Планируется
- `/api/v1/economy/*` → economy-service (8085) 📋 Планируется
- `/api/v1/social/*` → social-service (8084) 📋 Планируется
- `/api/v1/world/*` → world-service (8086) 📋 Планируется

**Примечание:** Фронтенд всегда делает запросы к API Gateway (8080), который маршрутизирует на нужный микросервис.

---

## 📋 Описание

Детальные API endpoints для MVP текстовой версии NECPGAME. Все необходимые endpoints для аутентификации, персонажей, локаций, инвентаря, квестов, NPC, боя и торговли.

**Файл разбит на части (all < 500 строк):**

---

## 📑 Структура

### Part 1: Auth & Characters
**Файл:** [part1-auth-characters.md](./part1-auth-characters.md)  
**Содержание:** Authentication, Authorization, Character CRUD

### Part 2: World & Inventory
**Файл:** [part2-world-inventory.md](./part2-world-inventory.md)  
**Содержание:** Locations, Inventory, Items

### Part 3: Quests & Interactions
**Файл:** [part3-quests-interactions.md](./part3-quests-interactions.md)  
**Содержание:** Quests, NPC, Combat (текстовый)

### Part 4: Trading & Technical
**Файл:** [part4-trading-technical.md](./part4-trading-technical.md)  
**Содержание:** Trading, Errors, Validation, TODO

---

## ⚡ Quick Start

**Для backend разработчиков:**
1. Part 1 - Auth & Characters
2. Part 2 - World & Inventory
3. Part 3 - Quests & Interactions
4. Part 4 - Trading & Technical

---

- 📅 **Дата:** 2025-11-07
- 🔄 **Статус:** queued
- 📝 **Следующий шаг:** АПИТАСК создаст OpenAPI spec

---

## 🔗 Связанные документы

- [MVP Data Models](../mvp-data-models.md)
- [API Data Models](../../api-specs/api-data-models.md)
- [Authentication System](../../backend/authentication-authorization-system.md)

---

## История изменений

- v1.0.1 (2025-11-07 01:25) - Разбит на 4 части (all < 500)
- v1.0.0 (2025-11-06) - Создан (1510 строк)
