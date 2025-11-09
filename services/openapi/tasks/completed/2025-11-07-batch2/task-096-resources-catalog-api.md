# Task ID: API-TASK-096
**Тип:** API Generation
**Приоритет:** высокий
**Статус:** queued
**Создано:** 2025-11-07 04:30
**Создатель:** AI Agent (API Task Creator)
**Зависимости:** API-TASK-066 (crafting.yaml)

---

## 📋 Краткое описание

Создать API для каталога ресурсов и материалов.

**Что нужно сделать:** Создать API для всех ресурсов игры (сырье, обработанные, компоненты, данные, спец.ресурсы).

---

## 🎯 Цель задания

Создать API для Resources Catalog:
- **Категории:**
  - Raw Materials (сырьё)
  - Processed (обработанные)
  - Components (компоненты)
  - Data (данные)
  - Special (специальные)
- **Параметры:**
  - Tier (1-5)
  - Rarity (Common → Legendary)
  - Sources (loot/harvest/production/quest)
  - Uses (crafting/trading/quest)
  - Value (vendor sell/buy, player market)
  - Stack size, weight
- **Интеграция:** Крафт, торговля, квесты

---

## 📚 Источники информации

**Путь:** `.BRAIN/02-gameplay/economy/economy-resources-catalog.md`
**Версия:** v2.0.0
**Статус:** ready

---

## 📁 Целевая структура API

**Целевой файл:** `api/v1/gameplay/economy/resources-catalog.yaml`

---

## ✅ Endpoints

1. **GET `/api/v1/gameplay/economy/resources`** - Список ресурсов
2. **GET `/api/v1/gameplay/economy/resources/{resource_id}`** - Детали ресурса
3. **GET `/api/v1/gameplay/economy/resources/by-category/{category}`** - По категории

---

**История:** 2025-11-07 04:30 - Создано


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

