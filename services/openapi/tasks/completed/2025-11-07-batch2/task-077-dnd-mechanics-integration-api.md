# Task ID: API-TASK-077
**Тип:** API Generation
**Приоритет:** высокий
**Статус:** queued
**Создано:** 2025-11-07 02:20
**Создатель:** AI Agent (API Task Creator)
**Зависимости:** API-TASK-075 (dnd-checks.yaml)

---

## 📋 Краткое описание

Создать API для применения D&D проверок в игровых механиках.

**Что нужно сделать:** Создать API для D&D проверок в хакерстве, крафте, торговле, социальных механиках, исследованиях, рейдах.

---

## 🎯 Цель задания

Создать API для D&D в механиках:
- **Хакерство:** скан → взлом → удержание; INT/TECH + Hacking vs DC узла; крит-успех/провал
- **Крафт:** TECH/INT + Crafting; DC по редкости; качество результата
- **Торговля:** EMP/INT + Trading; DC по статусу; улучшенные цены
- **Социальные:** EMP/COOL + Persuasion/Deception/Intimidation
- **Исследование:** REF/INT + Perception/Analysis
- **Рейды:** командные проверки, распределение ролей, суммирование

---

## 📚 Источники информации

**Путь:** `.BRAIN/02-gameplay/combat/combat-dnd-mechanics-integration.md`
**Статус:** approved

---

## 📁 Целевая структура API

**Целевой файл:** `api/v1/gameplay/mechanics/dnd-mechanics-integration.yaml`

---

## ✅ Endpoints

1. **POST `/api/v1/gameplay/mechanics/dnd-integration/hacking-check`** - Проверка взлома
2. **POST `/api/v1/gameplay/mechanics/dnd-integration/crafting-check`** - Проверка крафта
3. **POST `/api/v1/gameplay/mechanics/dnd-integration/trading-check`** - Проверка торговли
4. **POST `/api/v1/gameplay/mechanics/dnd-integration/social-check`** - Социальная проверка

---

**История:** 2025-11-07 02:20 - Создано


### OpenAPI (обязательно)

- Заполни `info.x-microservice` с актуальными данными:
  - name: gameplay-service
  - port: 8083
  - domain: gameplay
  - base-path: /api/v1/gameplay/mechanics
  - package: com.necpgame.gameplayservice
- В секции `servers` используй gateway:
  - https://api.necp.game/v1/gameplay/mechanics
  - http://localhost:8080/api/v1/gameplay/mechanics
- WebSocket маршруты публикуй только через wss://api.necp.game/v1/gameplay/mechanics/...

> ⚠️ 2025-11-09 — **Отменено.** Гибридные DnD механики сняты с roadmap; спецификация оставлена в архиве.

