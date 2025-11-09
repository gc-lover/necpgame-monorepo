# Task ID: API-TASK-265
**Тип:** API Generation
**Приоритет:** высокий
**Статус:** queued
**Создано:** 2025-11-08 00:32
**Создатель:** AI Agent (GPT-5 Codex)
**Зависимости:** API-TASK-264 (city unrest API), API-TASK-247 (loot-hunt system API)

---

## 📋 Краткое описание

Необходимо описать OpenAPI контракт для `Helios Countermesh Ops`: расписание операций (`CM-Viper`, `CM-Aegis`, `CM-Phalanx`, `CM-Parallax`), участие игроков, PvE/PvPvE фазы, экономические награды, репутации и телеметрию.

**Что нужно сделать:** Создать спецификацию `helios-countermesh-ops.yaml` для world-service с интеграциями в combat-, economy- и social-сервисы.

---

## 🎯 Цель задания

Сформировать централизованный API, который:
- Управляет планированием, запуском и завершением Helios Ops
- Обеспечивает интеграцию с боевыми инстансами, PvP рейтингом и raid системами
- Расчитывает награды/расходы (`helios-cred`, `countermesh-alloy`, репутации)
- Синхронизируется с `specter-hq` и `city.unrest` показателями
- Собирает телеметрию участия и балансирует PVE/PVP аспекты

---

## 📚 Источники информации

- `.BRAIN/02-gameplay/world/helios-countermesh-ops.md` (v1.0.0, готов к API)
  - Таблицы операций, фаз, экономических наград, репутаций
  - API карта по сервисам, телеметрия и SLA
- Дополнительно:
  - `.BRAIN/02-gameplay/world/city-unrest-escalations.md` — триггеры и последствия
  - `.BRAIN/02-gameplay/world/specter-hq.md` — связка бонусов/штрафов
  - `.BRAIN/04-narrative/quests/raid/2025-11-07-raid-specter-surge.md` — сюжетный контекст

---

## 📁 Целевая структура API

**Файл:** `api/v1/gameplay/world/helios-ops.yaml`  
**Формат:** OpenAPI 3.0.3  
**Версия:** v1 (≤400 строк)

```
API-SWAGGER/
└── api/
    └── v1/
        └── gameplay/
            └── world/
                └── helios-ops.yaml
```

---

## 🏗️ Целевая архитектура (⚠️)

### Backend
- **Микросервис:** world-service (Helios Ops module)
- **Порт:** 8086
- **Base path:** `/api/v1/world/helios-ops/*`
- **Интеграции:** combat-service, economy-service, social-service, analytics-service, guild-service

### Frontend
- **Модуль:** `modules/world/helios-ops`
- **State Store:** `useWorldStore` (`activeOps`, `schedule`, `rewards`, `pvpStats`)
- **UI:** `OpsSchedule`, `OpsPhaseTracker`, `RewardsPanel`, `PvPScoreboard`
- **Forms:** `OpsJoinForm`, `OpsResolveForm`
- **Hooks:** `useRealtime`, `usePhaseState`, `useGuildParticipation`

### Gateway
```yaml
- id: world-service
  uri: lb://WORLD-SERVICE
  predicates:
    - Path=/api/v1/world/helios-ops/**
```

### Events
- `HELIOS_OP_SCHEDULE_CHANGED`, `HELIOS_OP_STARTED`, `HELIOS_OP_PHASE`, `HELIOS_OP_OUTCOME`, `HELIOS_OP_REWARD_GRANTED`

---

## 🧩 План выполнения

1. Смоделировать операции (metadata, prereqs, phases, rewards).
2. Реализовать расписание и условия запуска (`specter.overlay.alertLevel`, `city.unrest`, гильдейский выбор).
3. Добавить эндпоинты участия (join, withdraw, phase transitions).
4. Связать с combat-service (вызов encounters) и economy-service (награды/расходы).
5. Описать репутационные эффекты и социальные события.
6. Встроить PvP элементы (`helios_vs_specter_rank`, `CM-Phalanx`).
7. Добавить телеметрию, KPI, latency и PagerDuty.
8. Включить WebSocket поток фаз и рейтингов.
9. Учесть безопасность (level requirements, lockouts, cooldowns).

---

## 🧪 API Endpoints

- `GET /schedule` / `POST /schedule` — планирование окон.
- `GET /active` — текущие операции (фильтр по регионам/типам).
- `POST /{opId}/join` — участие (валидация уровней, гильдий, lockout).
- `POST /{opId}/phase` — переход фаз (combat telemetry, PvP состояние).
- `POST /{opId}/complete` — фиксация результата, награды.
- `POST /{opId}/abort` — отмена/форсирование (админ).
- `GET /rewards/history` — история наград (пагинация).
- `GET /pvp/leaderboard` — рейтинг `helios_vs_specter_rank`.
- `POST /economy/reward` — ручная корректировка наград (админ).
- `POST /social/broadcast` — уведомления, пропаганда.
- WebSocket `/ws/world/helios-ops` — события фаз/результатов.

Ошибки: shared responses (400/401/403/404/409/422/429/500).

---

## 🗄️ Схемы

- **HeliosOp** — opId, type, prereqs, difficulty, rewards, modifiers.
- **OpSchedule** — scheduleId, opId, startAt, endAt, status.
- **JoinRequest** — playerId, guildId, role, queueType.
- **PhaseEvent** — opId, phase, status, telemetry, combatEncounterId.
- **Outcome** — opId, success, rewards, penalties, cityUnrestDelta.
- **RewardPayload** — currencies, materials, reputation.
- **PvPScoreEntry** — guildId, specterScore, heliosScore, result.
- **TelemetrySnapshot** — completionRate, participation, failReasons.

---

## 🔄 Интеграции

- combat-service (`POST /combat/helios-ops/encounter`)
- economy-service (`POST /economy/helios-ops/rewards`)
- social-service (`POST /social/helios/broadcast`)
- analytics-service (`POST /analytics/helios-ops/event`)
- guild-service (`POST /guilds/helios-support/vote`)

---

## 📊 Observability

- Метрики: `helios_ops_completion_rate`, `helios_ops_participation`, `helios_ops_pvp_balance`, `helios_ops_queue_time`.
- PagerDuty: `HeliosOpsQueueLag`, `HeliosPvpMismatch`.
- Трейсы: `helios-op-join`, `helios-op-phase`, `helios-op-reward`.

---

## ✅ Критерии приемки

1. Префикс `/api/v1/world/helios-ops` соблюдён.
2. Target Architecture блок указан.
3. Расписание поддерживает конфликт-резолв (429/409) и ручные overrides.
4. Участие проверяет lockouts, уровни и возвращает состояние.
5. Фазы включают combat telemetry IDs и PvP данные.
6. Экономические награды детализированы для economy-service.
7. Репутации и социальные эффекты описаны с payload.
8. PvP рейтинг доступен с пагинацией и сортировкой.
9. Telemetry включает KPI из документа.
10. FAQ покрывает edge cases (провал операции, частичная победа, emergency abort).

---

## ❓ FAQ

- **Что делать при emergency abort?** `POST /{opId}/abort` с флагом `emergency`, событие `HELIOS_OP_ABORTED`, компенсации (40% затрат).
- **Как обрабатывать lockouts?** Отдельная схема `helios_ops_lockouts`; API возвращает причины, время разблокировки.
- **Можно ли повторно запустить проваленную операцию?** Да, после cooldown; указать параметры в schedule.
- **Как синхронизировать с City Unrest?** При завершении `Outcome` публикует delta (`+6/-12`) и вызывает API из TASK-264.
- **Как учитывать PvP результат?** Возвращать поле `pvpOutcome` (helios/specter) и публиковать событие `HELIOS_OP_PVP_RESULT`.

---


### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

