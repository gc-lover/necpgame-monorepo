# Task ID: API-TASK-171
**Тип:** API Generation | **Приоритет:** средний | **Статус:** queued
**Создано:** 2025-11-07 11:54 | **Создатель:** AI Agent | **Зависимости:** API-TASK-138

---

## 📋 Описание

Создать API для систем нарративной когерентности (3 документа). Event matrix, player impact, dev integration.

---

## 📚 Источники (3 документа)

- `phase3-event-matrix/architecture.md` - архитектура событийной матрицы
- `phase5-player-impact/hybrid/hybrid-system.md` - гибридная система влияния игроков
- `phase6-documentation/dev-guides/api-integration.md` - интеграция для разработчиков

---

## 📁 Целевой файл

`api/v1/narrative/coherence-systems.yaml`

---

## 🏗️ Целевая архитектура

### Backend (микросервис):

**Микросервис:** narrative-service  
**Порт:** 8087  
**API пути:** /api/v1/narrative/coherence/*

### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

### Frontend (модуль):

**Модуль:** narrative  
**Путь:** modules/narrative/coherence  
**State Store:** useNarrativeStore (eventMatrix, playerImpact, coherenceState)

### Frontend (библиотеки):

**UI компоненты (@shared/ui):**
- Card, MatrixView, ImpactChart, CoherenceIndicator

**Готовые формы (@shared/forms):**
- N/A (системная функция, не требует форм)

**Layouts (@shared/layouts):**
- GameLayout (индикаторы в UI)

**Хуки (@shared/hooks):**
- useRealtime (для синхронизации narrative state)

---

## ✅ Endpoints

1. **GET /api/v1/narrative/coherence/event-matrix** - Событийная матрица
2. **GET /api/v1/narrative/coherence/player-impact** - Система влияния

**Models:** EventMatrix, PlayerImpactSystem, NarrativeCoherence

---

**Источники:** 3 narrative coherence документа

