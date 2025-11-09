# Task ID: API-TASK-102
**Тип:** API Generation
**Приоритет:** высокий
**Статус:** queued
**Создано:** 2025-11-07 05:00
**Создатель:** AI Agent (API Task Creator)
**Зависимости:** API-TASK-048 (classes.yaml), API-TASK-072 (quest-system.yaml)

---

## 📋 Краткое описание

Создать API для системы Origin Stories.

**Что нужно сделать:** Создать API для уникальных предысторий классов (3 квеста на создании персонажа, permanent perks, branching choices).

---

## 🎯 Цель задания

Создать API для Origin Stories:
- **Концепция:** Уникальная предыстория для каждого класса (level 1-3)
- **Механика:**
  - Автоматическая активация при создании персонажа
  - 3 квеста (tutorial + backstory)
  - Permanent perks и title
  - Branching choices влияют на репутацию/фракции
- **Классы:** 13 классов = 13 origin stories
- **Perks:** Permanent (+2 checks, +1 AC, +20 reputation, title)
- **Branching:** Разные пути для каждого класса
- **Интеграция:** Quest system, reputation, factions

---

## 📚 Источники информации

**Путь:** `.BRAIN/05-technical/start-content/origin-stories/origin-system-overview.md`
**Версия:** v1.0.0
**Статус:** ready

---

## 📁 Целевая структура API

**Целевой файл:** `api/v1/narrative/origin-stories.yaml`

---

## ✅ Endpoints

1. **GET `/api/v1/narrative/origin-stories/{class_id}`** - Origin story класса
2. **POST `/api/v1/narrative/origin-stories/activate`** - Активировать при создании
3. **POST `/api/v1/narrative/origin-stories/{quest_id}/choice`** - Выбор в квесте

---

**История:** 2025-11-07 05:00 - Создано


### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

