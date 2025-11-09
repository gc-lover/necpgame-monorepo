# Task ID: API-TASK-069
**Тип:** API Generation
**Приоритет:** средний
**Статус:** queued
**Создано:** 2025-11-07 01:40
**Создатель:** AI Agent (API Task Creator)
**Зависимости:** API-TASK-065 (equipment-matrix.yaml)

---

## 📋 Краткое описание

Создать API для лут-таблиц.

**Что нужно сделать:** Создать API для лут-таблиц (квесты, события, враги) с редкостью, формулами вероятностей, модификаторами.

---

## 🎯 Цель задания

Создать API для loot tables:
- Редкость: Common (60%), Uncommon (25%), Rare (12%), Epic (2.5%), Legendary (0.5%)
- Формула: `P(rarity) = baseChance * (1 + luckModifier) * (1 + reputationModifier) * (1 + questModifier)`
- Лут по типам: Main Quest, Side Quest, Bosses, Random Enemies
- Модификаторы: LUCK, репутация, квест, zone type

---

## 📚 Источники информации

**Путь:** `.BRAIN/02-gameplay/economy/loot-tables.md`
**Статус:** ready

---

## 📁 Целевая структура API

**Целевой файл:** `api/v1/gameplay/economy/loot-tables.yaml`

---

## ✅ Endpoints

1. **GET `/api/v1/gameplay/economy/loot-tables/{source_type}`** - Лут-таблица
2. **POST `/api/v1/gameplay/economy/loot-tables/roll`** - Бросить лут
3. **POST `/api/v1/gameplay/economy/loot-tables/calculate-probability`** - Расчет вероятности

---

**История:** 2025-11-07 01:40 - Создано


### OpenAPI (обязательно)

- Заполни `info.x-microservice` с актуальными данными:
  - name: economy-service
  - port: 8085
  - domain: economy
  - base-path: /api/v1/gameplay/economy
  - package: com.necpgame.economyservice
- В секции `servers` используй gateway:
  - https://api.necp.game/v1/gameplay/economy
  - http://localhost:8080/api/v1/gameplay/economy
- WebSocket маршруты публикуй только через wss://api.necp.game/v1/gameplay/economy/...

