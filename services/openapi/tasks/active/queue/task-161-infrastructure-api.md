# Task ID: API-TASK-161
**Тип:** API Generation | **Приоритет:** средний | **Статус:** queued
**Создано:** 2025-11-07 11:26 | **Создатель:** AI Agent | **Зависимости:** none

---

## 📋 Описание

Создать API для infrastructure систем (6 документов). Anti-cheat, admin tools, gateway, database, caching, CDN.

---

## 📚 Источники (6 документов)

- `.BRAIN/05-technical/infrastructure/anti-cheat-system.md` (v1.0.0)
- `.BRAIN/05-technical/infrastructure/admin-moderation-tools.md` (v1.0.0)
- `.BRAIN/05-technical/infrastructure/api-gateway-architecture.md` (v1.0.0)
- `.BRAIN/05-technical/infrastructure/database-architecture.md` (v1.0.0)
- `.BRAIN/05-technical/infrastructure/caching-strategy.md` (v1.0.0)
- `.BRAIN/05-technical/infrastructure/cdn-asset-delivery.md` (v1.0.0)

**Ключевые механики:**
- Anti-cheat: pattern detection, auto-ban, audit logs
- Admin tools: player management, content moderation, analytics
- API Gateway: routing, load balancing, rate limiting
- Database: sharding, replication, partitioning
- Caching: multi-level (CDN, Redis, app-level)
- CDN: asset delivery, geo-distribution

---

## 📁 Целевая структура

```
api/v1/admin/
├── anti-cheat.yaml
└── moderation.yaml

docs/
├── api-gateway-arch.md
├── database-arch.md
├── caching-strategy.md
└── cdn-delivery.md
```

---

## 🏗️ Целевая архитектура

### Backend (микросервис):

**Микросервис:** admin-service (для anti-cheat/moderation)  
**Порт:** 8088  
**API пути:** /api/v1/admin/*

**Документация:** (gateway, db, cache, cdn - архитектурные документы, не API)

### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

### Frontend (модуль):

**Модуль:** N/A (admin панель, отдельное приложение)  
**Путь:** src/features/admin/  
**State Store:** useAdminStore (reports, moderationQueue)

### Frontend (библиотеки):

**UI компоненты (@shared/ui):**
- Card, Table, Button, Badge (status), Chart (analytics)

**Готовые формы (@shared/forms):**
- ModerationActionForm, BanForm

**Layouts (@shared/layouts):**
- AdminLayout (специальный layout для admin панели)

**Хуки (@shared/hooks):**
- useRealtime (для real-time модерации)

---

## ✅ Задача

Создать admin API (anti-cheat, moderation) и архитектурные документы (gateway, db, cache, cdn).

**Models:** AntiCheatReport, AdminAction, ModerationLog

---

**Источники:** 6 infrastructure документов

