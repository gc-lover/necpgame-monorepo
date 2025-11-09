# Task ID: API-TASK-266
**Тип:** API Generation
**Приоритет:** высокий
**Статус:** queued
**Создано:** 2025-11-08 00:40
**Создатель:** AI Agent (GPT-5 Codex)
**Зависимости:** API-TASK-264 (city unrest API), API-TASK-265 (helios ops API)

---

## 📋 Краткое описание

Сконструировать API балансировочного слоя между Specter HQ и Helios Ops: расчёт валют (`specter-favor`, `helios-cred`, `underlink-bonds`), модификаторов (`city.unrest`, `specter-prestige`, `helios.alert`), применение наград/расходов и телеметрия.

**Что нужно сделать:** Создать `specter-helios-balance.yaml` в economy-service (с интеграцией world/social), включая REST, события и модели данных.

---

## 🎯 Цель задания

Предоставить единый контракт для:
- Расчёта вознаграждений и расходов Specter контрактов и Helios операций
- Применения модификаторов (unrest, prestige, alert)
- Поддержки отчётности и audit (баланс ресурсов, налогов, транспортных расходов)
- Синхронизации с world-service (City Unrest) и social-service (репутации)
- Наблюдаемости (метрики, логирование)

---

## 📚 Источники информации

- `.BRAIN/02-gameplay/world/economy-specter-helios-balance.md` (v1.0.0)
  - Таблицы валют, коэффициентов, контрактов Specter, операций Helios
  - Формулы модификаторов, API карта, SQL структуры, телеметрия
- Дополнительно: `city-unrest-escalations.md`, `specter-hq.md`, `helios-countermesh-ops.md`

---

## 📁 Целевая структура API

**Файл:** `api/v1/gameplay/economy/specter-helios-balance.yaml`  
**Формат:** OpenAPI 3.0.3 (≤400 строк)

```
API-SWAGGER/
└── api/
    └── v1/
        └── gameplay/
            └── economy/
                └── specter-helios-balance.yaml
```

---

## 🏗️ Целевая архитектура

### Backend
- **Микросервис:** economy-service
- **Порт:** 8085
- **Base path:** `/api/v1/economy/specter-helios/*`
- **Интеграции:** world-service (unrest levels), social-service (репутации), analytics-service, notification-service

### Frontend
- **Модуль:** `modules/economy/factions-balance`
- **State Store:** `useEconomyStore` (`balanceCoefficients`, `rewards`, `costs`, `logs`)
- **UI:** `BalanceDashboard`, `ModifierTimeline`, `RewardLedger`, `FactionImpactChart`
- **Forms:** `RewardApplyForm`, `ModifierOverrideForm`
- **Hooks:** `useRealtime`, `useEconomySimulation`, `useAuditLog`

### Gateway
```yaml
- id: economy-service
  uri: lb://ECONOMY-SERVICE
  predicates:
    - Path=/api/v1/economy/**
```

### Events
- `ECONOMY_CONTRACT_REWARD`, `ECONOMY_CONTRACT_COST`, `ECONOMY_BALANCE_UPDATED`, `ECONOMY_MODIFIER_APPLIED`

---

## 🧩 План выполнения

1. Описать модели валют/коэффициентов и формулы.
2. Реализовать эндпоинты расчёта (`/contracts/balance`, `/helios/reward`).
3. Добавить API для модификаторов (unrest, prestige, alert).
4. Включить аудит (ledger) и отчётность.
5. Связать с world-service (City Unrest) и social-service (репутации).
6. Прописать телеметрию и KPI (specter_favor_spent, helios_cred_earned).
7. Учесть блокировки и лимиты (weekly caps, diminishing returns).
8. Добавить WebSocket/stream (опционально) или webhooks для UI.

---

## 🧪 API Endpoints

- `GET /balance/coefficients` — текущие коэффициенты, модификаторы.
- `POST /contracts/balance` — расчёт вознаграждения Specter контрактов.
- `POST /contracts/cost` — списание ресурсов Specter.
- `POST /helios/reward` — начисление наград Helios Ops.
- `POST /helios/cost` — списание логистики Helios.
- `GET /ledger` — журнал операций (пагинация, фильтр по guild/faction).
- `POST /modifiers/apply` — ручные overrides (админ).
- `GET /modifiers/state` — текущее состояние модификаторов.
- `GET /analytics` — агрегированная статистика (KPI).
- `POST /webhooks/broadcast` — уведомления (optional).

Ошибки через `shared/common/responses.yaml` + `422` (недействительные входные данные).

---

## 🗄️ Схемы

- **BalanceCoefficient**, **SpecterRewardRequest**, **HeliosRewardRequest**, **CostRequest**, **LedgerEntry**, **ModifierState**, **AnalyticsSnapshot**, **OverrideRequest**, **OverrideResponse**.
- SQL таблицы: `contract_balance`, `faction_modifiers`, `economy_unrest_history`.

---

## 🔄 Интеграции

- world-service: `GET /world/city-unrest/state`
- social-service: `POST /social/factions/rep-update`
- notification-service: `POST /notifications/factions/balance`
- analytics-service: `POST /analytics/economy/balance`

---

## 📊 Observability

- Метрики: `specter_favor_spent_total`, `helios_cred_earned_total`, `unrest_tax_impact`, `modifier_override_total`.
- Алерты: `BalanceDrift`, `RewardSpike`, `ModifierStale`.
- Трейсы: `balance-calc`, `reward-apply`, `modifier-update`.

---

## ✅ Критерии приемки

1. Префикс `/api/v1/economy/specter-helios` соблюдён.
2. Target Architecture указан.
3. Вознаграждения/расходы возвращают детализацию (base, modifiers).
4. Применение модификаторов логируется и возвращает `ledgerId`.
5. Ledger поддерживает фильтры (guild, contract, date range).
6. Лимиты (weekly cap) учитываются и возвращают 409 при превышении.
7. Telemetry и KPI соответствуют документу.
8. Интеграции world/social описаны с payload.
9. Overrides требуют `X-Admin-Role` и audit trail.
10. FAQ покрывает edge cases (Cataclysm, emergency override).

---

## ❓ FAQ

- **Что, если одновременно применяются несколько модификаторов?** API должен принимать список и возвращать итоговый коэффициент + разбивку.
- **Как обрабатывать Cataclysm?** Авто-применение кризисных модификаторов; описать флаг `crisisMode`.
- **Можно ли откатить балансировку?** Да — `POST /modifiers/apply` с отрицательным delta и ссылкой на ledgerEntryId.
- **Как интегрировать с Specter HQ магазином?** Возвращать финальный reward, который использует economy сервис магазина.
- **Как учитывать провалы операций?** `HeliosRewardRequest` должен иметь `result=FAILED`, возвращать компенсации (40%).

---


### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

