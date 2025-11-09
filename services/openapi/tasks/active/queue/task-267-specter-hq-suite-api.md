# Task ID: API-TASK-267
**Тип:** API Generation
**Приоритет:** высокий
**Статус:** queued
**Создано:** 2025-11-08 00:48
**Создатель:** AI Agent (GPT-5 Codex)
**Зависимости:** API-TASK-266 (specter-helios balance API), API-TASK-265 (helios ops API)

---

## 📋 Краткое описание

Сформировать комплексную спецификацию для `Specter HQ`: зоны, магазин, апгрейды, контрактная доска, диалоговые состояния NPC и социальные эффекты.

**Что нужно сделать:** Создать пакет OpenAPI файлов или один файл `specter-hq-suite.yaml` (≤400 строк, при необходимости разбить на модульные файлы) для world-, economy-, social- и narrative-сервисов.

---

## 🎯 Цель задания

Обеспечить играбельный цикл Specter HQ:
- Управление зонами HQ (Command Deck, Armory, Ops Table, Lounge)
- Контракты (`intel-countermesh`, `intel-shieldbreak`, `specter-parade`) с наградами и блокировками
- Магазин и апгрейды (Tier 1–3) с валютами `specter-favor`, `countermesh-alloy`, `specter-prestige`
- Диалоговые состояния NPC Kaori Watanabe и Narrative API
- Социальные и репутационные эффекты, телеметрия и лимиты

---

## 📚 Источники информации

- `.BRAIN/02-gameplay/world/specter-hq.md` (v1.0.0)
  - NPC, зоны, магазин, апгрейды, Intel Board, диалоги, API карта, телеметрия
- Дополнительные документы:
  - `.BRAIN/02-gameplay/world/economy-specter-helios-balance.md`
  - `.BRAIN/02-gameplay/world/helios-countermesh-ops.md`
  - `.BRAIN/04-narrative/dialogues/npc-aisha-frost.md`
  - `.BRAIN/04-narrative/quests/raid/2025-11-07-raid-specter-surge.md`

---

## 📁 Целевая структура API

Предлагаемый вариант (можно разделить на несколько файлов, но минимум один должен быть создан):

```
API-SWAGGER/
└── api/
    └── v1/
        └── gameplay/
            ├── world/
            │   └── specter-hq.yaml
            ├── economy/
            │   └── specter-hq-store.yaml
            ├── social/
            │   └── specter-events.yaml
            └── narrative/
                └── specter-hq-dialogues.yaml
```

Если решите разбить — обеспечить ссылки и синхронизацию, описать в задании.

---

## 🏗️ Целевая архитектура (⚠️)

### World-service
- **Base path:** `/api/v1/world/specter-hq/*`
- Контракты (accept/complete), апгрейды, зоны, telemetry.

### Economy-service
- **Base path:** `/api/v1/economy/specter-hq/*`
- Магазин: список товаров, покупка, лимиты, контроль валют.

### Social-service
- **Base path:** `/api/v1/social/specter/*`
- Парады, репутации, социальные резонансы.

### Narrative-service
- **Base path:** `/api/v1/narrative/dialogues/specter-hq/*`
- Диалоговые состояния NPC, ветвление, проверки навыков.

### Frontend
- **Модули:** `modules/guild/specter-hq`, `modules/economy/specter-store`, `modules/social/events`, `modules/narrative/dialogues`
- **State Stores:** `useGuildStore`, `useEconomyStore`, `useSocialStore`, `useNarrativeStore`
- **UI:** `HqZoneMap`, `SpecterStore`, `IntelBoard`, `UpgradePanel`, `DialogueViewer`
- **Hooks:** `useWeeklyLimits`, `useContractProgress`, `useDialogueState`

---

## 🧩 План выполнения

1. Описать структуры зон HQ и доступов (tiers).
2. Реализовать магазин (товары, валюты, лимиты, cooldown).
3. Добавить API контрактов (accept, progress, complete) и требуемые проверки.
4. Фиксировать апгрейды HQ (tiers) и их эффекты.
5. Описать диалоговые состояния NPC (start, state transitions, checks).
6. Интегрировать социальные события (парады, репутации).
7. Добавить телеметрию (`specter_hq_visit`, `specter_hq_purchase`, `specter_contract_progress`).
8. Учесть зависимости с Helios Ops и City Unrest.

---

## 🧪 API Endpoints (минимум)

### World-service
- `GET /zones` — информация о зонах и их статусе.
- `GET /contracts` / `POST /contracts/accept` / `POST /contracts/{id}/complete`
- `POST /upgrades/apply` — апгрейд HQ.
- `GET /progress` — состояние гильдии (prestige, upgrades).

### Economy-service
- `GET /store/items` — ассортимент.
- `POST /store/purchase` — покупка.
- `GET /store/limits` — лимиты и cooldown.

### Social-service
- `POST /events/parade` — запуск парада.
- `POST /events/broadcast` — социальные уведомления.
- `GET /reputation` — текущие изменения.

### Narrative-service
- `POST /dialogues/start` — начало диалога.
- `PATCH /dialogues/state` — обновление состояния.
- `POST /dialogues/skill-check` — проверка навыков.

### WebSocket (опционально)
- `/ws/specter-hq` — обновления магазина/контрактов/репутаций.

Ошибки: использовать общие ответы (400/401/403/404/409/422/429/500).

---

## 🗄️ Схемы

- **HqZone**, **Contract**, **ContractProgress**, **Upgrade**, **StoreItem**, **PurchaseRequest**, **LimitInfo**, **DialogueState**, **SkillCheck**, **EventPayload**, **ReputationChange**, **TelemetrySnapshot**.
- Таблицы: `specter_hq_upgrades`, `specter_contracts`, `raid_store_limits`.

---

## 🔄 Интеграции

- economy balance (TASK-266)
- helios ops (TASK-265) для штрафов/бонусов
- city unrest (TASK-264) для условий контрактов
- notification-service (пуши по парадам/магазину)

---

## 📊 Observability

- Метрики: `specter_hq_visit_rate`, `contract_completion_rate`, `store_purchase_total`, `upgrade_usage`.
- Алерты: `SpecterHQStoreLag`, `SpecterContractQueueBacklog`.
- Трейсы: `specter-hq-contract`, `specter-hq-purchase`, `specter-hq-dialogue`.

---

## ✅ Критерии приемки

1. Target Architecture описан для всех задействованных сервисов.
2. Магазин учитывает лимиты (409/429), хранит `ledgerId`.
3. Контракты поддерживают статусы `accepted/in-progress/completed/failed`.
4. Апгрейды проверяют требования и возвращают новые эффекты.
5. Диалоги поддерживают skill checks и возвращают outcomes.
6. Социальные события обновляют репутации и публикуют события.
7. Telemetry соответствует документу.
8. Интеграции с Helios Ops и City Unrest задокументированы.
9. FAQ описывает edge cases (reset лимитов, rollback апгрейда, offline purchases).

---

## ❓ FAQ

- **Как сбрасываются недельные лимиты магазина?** Через cron; API должно поддерживать `GET /store/limits/reset-at`.
- **Можно ли откатить апгрейд?** Да — `POST /upgrades/revert` (админ); описать последствия.
- **Как привязать диалоги к рейдовым состояниям?** `DialogueState` включает `raid_progress`, проверка на `specter.overlay.alertLevel`.
- **Что делать при провале контракта?** Возвратить `failureConsequences`, обновить репутации и телеметрию.
- **Как связать с UI?** WebSocket/REST предоставляют обновления; упомянуть события для World Interaction UI.

---


### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

