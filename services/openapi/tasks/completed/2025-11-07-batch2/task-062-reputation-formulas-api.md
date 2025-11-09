# Task ID: API-TASK-062
**Тип:** API Generation
**Приоритет:** высокий
**Статус:** queued
**Создано:** 2025-11-07 01:05
**Создатель:** AI Agent (API Task Creator)
**Зависимости:** API-TASK-060 (relationships.yaml), API-TASK-061 (reputation-tiers.yaml)

---

## 📋 Краткое описание

Создать API спецификацию для репутационных формул.

**Что нужно сделать:** Создать API для расчета репутации с формулами, модификаторами, влиянием на DC, доступы, бонусы, штрафы, heat.

---

## 🎯 Цель задания

Создать API для репутационных формул:
- Базовая формула: `reputationChange = baseChange * (1 + classBonus) * (1 + originBonus) * ...`
- Модификаторы: classBonus (+20%), originBonus (+10%), questBonus (+30%), skillCheckBonus (±50%)
- Влияние на DC: `dcModifier = floor(reputation / 10) * -1`
- Heat система: каждое преступление +1 heat
- Доступы по репутации
- Бонусы и штрафы

---

## 📚 Источники информации

**Путь:** `.BRAIN/02-gameplay/social/reputation-formulas.md`
**Статус:** ready

---

## 📁 Целевая структура API

**Целевой файл:** `api/v1/gameplay/social/reputation-formulas.yaml`

---

## ✅ Endpoints

1. **POST `/api/v1/gameplay/social/reputation-formulas/calculate-change`** - Расчет изменения
2. **POST `/api/v1/gameplay/social/reputation-formulas/calculate-dc-modifier`** - DC модификатор
3. **GET `/api/v1/gameplay/social/reputation-formulas/heat/{character_id}`** - Heat статус

---

**История:** 2025-11-07 01:05 - Создано


### OpenAPI (обязательно)

- Заполни `info.x-microservice` с актуальными данными:
  - name: social-service
  - port: 8084
  - domain: social
  - base-path: /api/v1/gameplay/social
  - package: com.necpgame.socialservice
- В секции `servers` используй gateway:
  - https://api.necp.game/v1/gameplay/social
  - http://localhost:8080/api/v1/gameplay/social
- WebSocket маршруты публикуй только через wss://api.necp.game/v1/gameplay/social/...

