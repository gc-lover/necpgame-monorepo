# Task ID: API-TASK-235
**Тип:** API Generation
**Приоритет:** критический
**Статус:** completed
**Создано:** 2025-11-08 06:22
**Завершено:** 2025-11-08 21:01
**Исполнитель:** GPT-5 Codex (API Executor)
**Зависимости:** API-TASK-227, API-TASK-234, API-TASK-231

---

## 📋 Краткое описание

Сформировать OpenAPI спецификацию базовой системы генерации лута: таблицы, вероятности, распределение по режимам (solo/party/raid), roll системы, личный/общий лут, garant drop.

**Что нужно сделать:** Создать `api/v1/gameplay/loot/loot-generation.yaml`, используя `.BRAIN/05-technical/backend/loot-system/part1-loot-generation.md`.

---

## 🎯 Цель задания

Обеспечить серверный API для генерации и выдачи лута, который интегрирован с боевой системой, инвентарём, квестами и экономикой.

**Зачем это нужно:**
- Управлять таблицами добычи и весами дропа
- Поддерживать разные режимы распределения (personal/shared, need/greed)
- Интегрировать с рейдами, world events, quest rewards
- Предоставить аудит, историю дропа и настройки для live-ops

---

## 📚 Источники информации

### Основной документ

**Путь:** `.BRAIN/05-technical/backend/loot-system/part1-loot-generation.md`
**Версия:** v1.0.1 (2025-11-07)
**Статус:** approved, api-readiness: ready

**Ключевые разделы:**
- Loot Flow, таблицы, весовые коэффициенты
- Drop modes: personal, shared, raid, boss guaranteed
- Roll mechanics: need/greed/pass, threshold
- Quality tiers, rarity curves, pity timers
- Integration hooks (quests, achievements, analytics)

### Дополнительные источники

- `.BRAIN/05-technical/backend/loot-system/part2-advanced-loot.md`
- `.BRAIN/05-technical/backend/combat-session-backend.md`
- `.BRAIN/05-technical/backend/quest-engine-backend.md`
- `.BRAIN/05-technical/backend/inventory-system/part1-core-system.md`
- `.BRAIN/05-technical/backend/progression-backend.md`
- `.BRAIN/05-technical/backend/economy-system.md`

### Связанные документы

- `API-SWAGGER/tasks/active/queue/task-227-combat-session-api.md`
- `API-SWAGGER/tasks/active/queue/task-234-inventory-core-api.md`
- `API-SWAGGER/tasks/active/queue/task-231-party-system-api.md`

---

## 📁 Целевая структура API

- **Файл:** `api/v1/gameplay/loot/loot-generation.yaml`
- **Версия:** v1
- **Формат:** OpenAPI 3.0.3

```
API-SWAGGER/api/v1/loot/
 ├── loot-generation.yaml      ← создать/обновить
 ├── loot-generation-components.yaml
 └── loot-generation-examples.yaml
```

---

## 🏗️ Целевая архитектура (⚠️ ОБЯЗАТЕЛЬНО)

### Backend
- **Микросервис:** loot-service (часть gameplay-service)
- **Порт:** 8097
- **API Base Path:** `/api/v1/loot`
- **Зависимости:**
  - combat-service – события смерти, результаты боёв
  - quest-service – квестовые награды, гарантированные предметы
  - inventory-service – выдача предметов, проверки места
  - progression-service – влияние бонусов drop rate
  - analytics-service – отчёты по луту, дашборды
  - notification-service – сообщения о редком дропе
  - economy-service – балансы, валютные награды
  - party-service – контекст групп/рейдов

### Frontend
- **Модуль:** `modules/gameplay/loot`
- **State Store:** `useLootStore`
- **State:** `recentDrops`, `lootTables`, `rollResults`, `personalRewards`, `raidSummary`
- **UI компоненты:** `LootDropModal`, `RollResultPanel`, `RaidLootSummary`, `LootHistoryList`, `LootProbabilityView`, `GuaranteedDropTracker`
- **Формы:** `LootTableEditorForm` (GM/ops), `RollChoiceForm`, `LootDistributionForm`
- **Хуки:** `useLootDrops`, `useLootHistory`, `useRollSystem`, `useLootTables`

### Комментарий для YAML

```yaml
# Target Architecture:
# - Microservice: loot-service (port 8097)
# - API Base: /api/v1/loot
# - Dependencies: combat, quest, inventory, progression, analytics, notification, economy, party
# - Frontend Module: modules/gameplay/loot (useLootStore)
# - UI: LootDropModal, RollResultPanel, RaidLootSummary, LootHistoryList, LootProbabilityView, GuaranteedDropTracker
# - Forms: LootTableEditorForm, RollChoiceForm, LootDistributionForm
# - Hooks: useLootDrops, useLootHistory, useRollSystem, useLootTables
```

---

## ✅ Что нужно сделать (детальный план)

1. Описать модели loot tables, entries, rarity, modifiers, pity timers.
2. Реализовать эндпоинты генерации лута для различных источников (NPC, chest, quest).
3. Добавить распределение лута по режимам: personal, shared, raid, guaranteed.
4. Реализовать roll систему (need/greed/pass) и threshold rules.
5. Интегрировать с inventory (reserve → grant), quest triggers, achievements.
6. Добавить историю дропа, аудит, live analytics.
7. Настроить WebSocket события для UI и уведомлений.
8. Поддержать GM/ops endpoints для управления таблицами и симуляций.
9. Подготовить примеры, тест-кейсы, чеклист.

---

## 🔀 Endpoints

1. **POST `/api/v1/loot/sources/{sourceId}/generate`** – генерация лута для источника (NPC, босс, контейнер) с указанием параметров боя.
2. **POST `/api/v1/loot/sources/{sourceId}/simulate`** – симуляция дропа (QA/баланс), возвращает распределение вероятностей.
3. **GET `/api/v1/loot/tables`** – список таблиц лута (фильтры по активности, уровню, редкости).
4. **POST `/api/v1/loot/tables`** – создание/обновление таблиц (GM/ops, versioning).
5. **GET `/api/v1/loot/tables/{tableId}`** – детальная структура таблицы, веса, modifiers.
6. **POST `/api/v1/loot/tables/{tableId}/entries`** – управление записями (добавить/обновить/удалить предмет).
7. **POST `/api/v1/loot/distribution/personal`** – выдача персонального лута (результаты генерации → inventory).
8. **POST `/api/v1/loot/distribution/shared`** – распределение общего лута (roll, master looter, threshold).
9. **POST `/api/v1/loot/distribution/raid`** – рейдовый лут с гарантированными предметами и токенами.
10. **POST `/api/v1/loot/distribution/guaranteed`** – выдача гарантированных наград (pity timers, milestones).
11. **POST `/api/v1/loot/rolls`** – обработка roll (need/greed/pass, auto-roll).
12. **GET `/api/v1/loot/rolls/{sessionId}`** – результаты и история голосования.
13. **GET `/api/v1/loot/history`** – история дропа (фильтры: игрок, активность, предмет, редкость).
14. **GET `/api/v1/loot/stats`** – агрегированные метрики (drop rates, rarity distribution, pity triggers).
15. **POST `/api/v1/loot/reserve`** – резервирование предметов до подтверждения выдачи (инвентарь/почта/трейд).
16. **POST `/api/v1/loot/release`** – снятие резерва (при отмене или сбое).
17. **POST `/api/v1/loot/events/notify`** – отправка уведомлений (редкий дроп, мировой ивент).
18. **POST `/api/v1/loot/pity`** – управление pity timers (GM/ops).
19. **GET `/api/v1/loot/config`** – настройки drop rate modifiers, бонусов (сезоны, события).
20. **WS `/api/v1/loot/stream`** – события: `loot-generated`, `loot-distributed`, `roll-updated`, `rare-drop`, `pity-triggered`, `loot-table-updated`.

---

## 🧱 Модели данных

- **LootTable** – `tableId`, `name`, `sourceType`, `levelRange`, `entries[]`, `rarityCurve`, `pity`, `modifiers`.
- **LootEntry** – `entryId`, `itemId`, `weight`, `quantityRange`, `rarity`, `conditions`, `tags`.
- **LootGenerationContext** – `sourceId`, `sourceType`, `participants`, `difficulty`, `luckModifiers`, `eventFlags`.
- **LootResult** – `resultId`, `items[]`, `distributionMode`, `personalAssignments`, `timestamp`.
- **RollSession** – `sessionId`, `participants`, `item`, `rolls[]`, `outcome`, `timeout`.
- **Roll** – `playerId`, `type` (`NEED|GREED|PASS|AUTO`), `value`, `timestamp`.
- **LootHistoryEntry** – `entryId`, `playerId`, `source`, `item`, `rarity`, `quantity`, `distributionMode`, `timestamp`.
- **PityTimer** – `timerId`, `playerId`, `tableId`, `counter`, `threshold`, `guaranteedItem`.
- **RealtimeEventPayload** – `lootGenerated`, `lootDistributed`, `rollUpdated`, `rareDrop`, `pityTriggered`, `lootTableUpdated`.
- **Error Schema (`LootError`)** – codes (`TABLE_NOT_FOUND`, `ENTRY_INVALID`, `ROLL_DUPLICATE`, `NO_ELIGIBLE_PLAYERS`, `RESERVATION_FAILED`, `PITY_DISABLED`, `CONFIG_LOCKED`, `DISTRIBUTION_ERROR`).

---

## 🧭 Принципы и правила

- Авторизация: `ServiceToken` (internal generation), `GMToken` для управления таблицами; публикация результатов игрокам через `BearerAuth` (read-only endpoints).
- Determinism: генерация должна быть детерминированной с seed для воспроизводимости (логировать seed).
- Fairness: поддерживать pity timers, anti-exploit (частота фарма).
- Audit: все выдачи логируются и доступны support/moderation.
- Localization: названия предметов и описания через item-service.
- Scalability: кеширование таблиц, shard по активности, асинхронная выдача.

---

## 🧪 Примеры

- Генерация лута после убийства босса с гарантированным токеном и rare drop.
- Рейдовый loot roll Need/Greed с auto-pass AFK игроков.
- Персональный дроп при соло миссии, автоматическое добавление в инвентарь.
- Срабатывание pity timer и выдача гарантированной награды.
- GM обновляет таблицу лута для сезонного события и запускает симуляцию.

---

## 🔗 Связности и зависимости

- Интеграция с combat, quest, inventory, progression, analytics, notification, economy.
- Используется UI `LootDropModal`, `RaidLootSummary`, `GuaranteedDropTracker`.
- События влияют на achievements, battle pass, daily quests.

---

## ✅ Критерии приемки

1. `loot-generation.yaml` описывает генерацию, распределение, roll систему, историю.
2. Модели покрывают таблицы, результаты, pity timers, события.
3. Прописаны авторизация, аудит, интеграции, события.
4. Примеры и тест-кейсы подготовлены, чеклист выполнен.

---

## 📎 Checklist

- [ ] Использован шаблон `api-generation-task-template.md`
- [ ] Определены микросервис, UI модуль, зависимости
- [ ] Эндпоинты и события покрывают генерацию и распределение лута
- [ ] Добавлены модели, ошибки, примеры, критерии
- [ ] Обновить `tasks/config/brain-mapping.yaml`

---

## 📦 Результат

- Создана спецификация `api/v1/gameplay/loot/loot-generation.yaml` с 20 REST/WS операциями.
- Добавлены файлы `loot-generation-components.yaml`, `loot-generation-management-components.yaml`, `loot-generation-examples.yaml`.
- Задание перенесено в completed, статусы в `brain-mapping.yaml` и `implementation-tracker.yaml` обновлены.

---

## ❓FAQ

**Q:** Как учитывать глобальные бонусы drop rate?**
**A:** Добавить в `LootGenerationContext` поле `globalModifiers`; хранить настройки в `loot/config`. Применять в расчётах и логировать.

**Q:** Нужно ли поддерживать shared loot pools для event?**
**A:** Да, предусмотреть `distributionMode=EVENT_POOL` и описать в разделе интеграций; реализация может быть расширена в part2.



### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.


