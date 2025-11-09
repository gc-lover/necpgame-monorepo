# Task ID: API-TASK-105
**Тип:** API Generation
**Приоритет:** средний
**Статус:** queued
**Создано:** 2025-11-07 05:15
**Создатель:** AI Agent (API Task Creator)
**Зависимости:** API-TASK-065 (equipment-matrix.yaml)

---

## 📋 Краткое описание

Создать API для минимальных сущностей Equipment Matrix.

**Что нужно сделать:** Создать API для базовых сущностей (Brand, Item, Affix, GenerationRules, Contract, License).

---

## 🎯 Цель задания

Создать API для Equipment Matrix Entities:
- **Brand:** id, name, origin, factionId, signatureBonuses, visualStyle
- **Item:** id, type, brandId, rarity, seed, level, statsCore, statsExtended
- **Affix:** id, name, tier, statModifiers, applicableTo
- **GenerationRules:** baseStatRanges, tierScaling, rarityWeights
- **Contract:** id, itemId, ownerAccountId, terms, restrictions
- **License:** id, brandId, userAccountId, royalty, active

**Интеграция:** Equipment Matrix основная система

---

## 📚 Источники информации

**Путь:** `.BRAIN/05-technical/api-requirements/equipment-matrix-entities.md`
**Версия:** v0.1.0
**Статус:** ready (draft)

---

## 📁 Целевая структура API

**Целевой файл:** `api/v1/technical/equipment-entities.yaml`

---

**История:** 2025-11-07 05:15 - Создано


### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

