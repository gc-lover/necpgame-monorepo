# Guild Contract Board — UI/UX & API

**ID документа:** `guild-contract-board`  
**Тип:** ui-spec  
**Статус:** approved  
**Версия:** 1.0.0  
**Дата создания:** 2025-11-07  
**Последнее обновление:** 2025-11-07 23:05  
**Приоритет:** высокий  
**Связанные документы:** `../../02-gameplay/world/specter-hq.md`, `../../02-gameplay/world/helios-countermesh-ops.md`, `../../02-gameplay/world/city-unrest-escalations.md`, `../../06-tasks/active/CURRENT-WORK/active/2025-11-07-world-interaction-ui.md`  
**target-domain:** frontend/ui  
**target-мicroservices:** world-service, economy-service, social-service  
**target-frontend-модули:** modules/guild/raids, modules/events, modules/social  
**api-readiness:** ready  
**api-readiness-check-date:** 2025-11-07 23:05
**api-readiness-notes:** Определены UX-флоу и API Guild Contract Board; готово к реализации фронтенда и бэкенда.

---
- **Status:** queued
- **Last Updated:** 2025-11-08 01:15

---

## 1. Цель и контекст
- Guild Contract Board — центральный UI для Specter HQ и союзных гильдий.
- Борд агрегирует контракты Specter/Helios, показывает прогресс и уровни City Unrest.
- Цель: связать рейдовые валюты (`specter-favor`), контракты Helios Ops и эскалации города.

## 2. Пользовательские роли
- **Guild Leader / Officer:** создаёт заявки, активирует апгрейды HQ, распределяет награды.
- **Member (Specter Support):** принимает контракты, отслеживает таймеры.
- **Helios Collaborator:** альтернативная роль для фракций, поддерживающих Helios (PvPvE контракты).

## 3. UX потоки
### 3.1 Specter HQ Flow
1. Открыть Specter HQ → Contract Board.
2. Просмотреть список контрактов (`Active`, `Pending`, `Completed`).
3. Выбрать контракт (детали: описание, требования, награды, влияние на `city.unrest`).
4. Нажать `Accept` (проверка лимитов, роли, ресурсов).
5. После выполнения — `Submit Results` (таблица участников, распределение `specter-favor`).

### 3.2 Helios Ops Flow
1. Игрок выбирает вкладку `Helios Countermesh`.
2. Появляются операции (`CM-Viper`, `CM-Aegis`, ...). UI показывает риски и влияние.
3. При успешном завершении — UI отображает `helios.alert` и последствия для Specter HQ.

### 3.3 City Unrest Flow
1. Секция справа показывает текущий уровень `city.unrest`, модификатор и активный сценарий.
2. CTA `Mitigate` открывает список мероприятий (например, `neon-riot` участие).
3. После завершения обновляются показатели в реальном времени через WebSocket.

## 4. Состояния(UI State)
- `loading` — данные борда не загружены.
- `idle` — стандартное состояние.
- `contract_taken` — отображается статус выполнения.
- `cooldown` — контракт недоступен (таймер).
- `locked` — требуется апгрейд HQ или репутация.

## 5. ASCII мокап
```
┌────────────────────────────────────────────────────────┐
│ GUILD CONTRACT BOARD           City Unrest: 62 (Crisis)│
├──────────────┬──────────────────────┬──────────────────┤
│ Specter Ops  │ Helios Countermesh   │ City Response    │
├──────────────┴──────────────────────┴──────────────────┤
│ [Active Contracts]                                   ▼ │
│ 1. intel-countermesh (Tier 1)                          │
│    Reward: 2 SF / alloy    Timer: 12:34                │
│ [Accept] [Details]                                     │
│                                                        │
│ 2. neon-riot Response (Crisis)                         │
│    Group: 6 players    Impact: -12 unrest              │
│ [Join] [View Teams]                                    │
│                                                        │
│ Active Scenario: CITY UNREST — NEON RIOT               │
│ Progress: ███████░░░ 67%        Time left: 05:45        │
│ Rewards: +30 Specter Prestige, +5 City Gov             │
│                                                        │
│ Recent Results                                          │
│ - CM-Aegis: Helios Success → Specter Store -1 slot     │
│ - Specter Parade: Social Trust +4                     │
│                                                        │
└────────────────────────────────────────────────────────┘
```

## 6. API требования (frontend)
- **REST:**
  - `GET /api/v1/world/specter-hq/contracts` — список контрактов.
  - `POST /api/v1/world/specter-hq/contracts/{contractId}/accept`.
  - `POST /api/v1/world/specter-hq/contracts/{contractId}/complete`.
  - `GET /api/v1/world/helios-ops/available`.
  - `POST /api/v1/world/helios-ops/{opId}/join`.
  - `GET /api/v1/world/city-unrest/state`.
- **WebSocket / Events:** `CITY_UNREST_UPDATE`, `CONTRACT_PROGRESS_UPDATE`, `HELIOS_OP_UPDATE`.

## 7. API требования (backend)
- Добавить эндпоинты на world-service для агрегации борда.
- Экономика: `POST /api/v1/economy/contract/reward-distribute`.
- Social-service: уведомления (`/social/contracts/notify`).
- RBAC: роль `guild.officer` для управления.

## 8. Persistence
- Таблицы: `guild_contracts`, `guild_contract_assignments`, `guild_contract_history`.
- Индексы по `guild_id`, `contract_type`, `status`.
- Логи действий для аналитики.

## 9. Telemetry и SLA
- **Events:** `contract_viewed`, `contract_accepted`, `contract_completed`, `helios_op_joined`.
- **KPIs:** среднее время принятия контракта ≤ 5 мин, завершение ≥ 80%.
- **Latency:** `contract_fetch ≤ 150 мс`, `contract_action ≤ 200 мс`.
- Grafana: `guild-contract-board`, `contract-success-rate`, `helios-vs-specter`.
- PagerDuty: `ContractBoardQueueLag`.

## 10. История изменений
- 2025-11-07 23:05 — Создан UI/UX и API документ Guild Contract Board.
