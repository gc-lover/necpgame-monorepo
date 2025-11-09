# API Generation Task Template

> Обязательно сверяйся с `GLOBAL-RULES.md`, `API-SWAGGER/CORE.md`, `tasks/config/task-creation-guide.md` и `tasks/config/checklist.md`.  
> Размер задания ≤ 500 строк. При превышении создавай продолжения (`task-XXX-description_0001.md`).

## Metadata
- **Task ID:** API-TASK-113  
- **Type:** API Generation  
- **Priority:** critical  
- **Status:** queued  
- **Created:** 2025-11-09 21:15  
- **Author:** API Task Creator Agent  
- **Dependencies:** API-TASK-382 (combat ballistics metrics & projectiles)

## Summary
Разработать OpenAPI спецификацию `api/v1/gameplay/combat/combat-session.yaml`, описывающую управление боевыми сессиями: запуск, синхронизацию состояния, обработку экшенов (атаки, навыки, предметы, бегство), журналы событий, WebSocket обновления и Kafka события жизненного цикла. Спецификация должна покрыть как PvE, так и PvP сценарии, а также интеграцию с античит и наградными подсистемами.

## Source Documents
| Поле | Значение |
| --- | --- |
| Repository | `.BRAIN` |
| Path | `.BRAIN/05-technical/backend/combat-session-backend.md` |
| Version | v1.0.0 |
| Status | approved |
| API readiness | ready (2025-11-09 02:47) |

**Key points:** lifecycle combat sessions (creation, turn order, rewards); таблицы `combat_sessions`, `combat_logs`; события `combat:started`, `combat:ended`, `combat:enemy-killed`, `combat:player-died`, `combat:damage-dealt`; пересечения с turn-based и real-time боями; anti-cheat хуки; интеграции с character-service, economy-service и quest-service.  
**Related docs:** `.BRAIN/02-gameplay/combat/combat-shooting.md`, `.BRAIN/02-gameplay/combat/combat-shooter-core.md`, `.BRAIN/05-technical/backend/inventory-system/part1-core-system.md`, `.BRAIN/05-technical/backend/trade-system.md` (loot escrow), `.BRAIN/05-technical/backend/progression-backend.md`.

## Target Architecture (⚠️ Обязательно)
- **Microservice:** gameplay-service  
- **Port:** 8083  
- **Domain:** gameplay.combat  
- **API directory:** `api/v1/gameplay/combat/combat-session.yaml`  
- **base-path:** `/api/v1/gameplay/combat/sessions`  
- **Java package:** `com.necpgame.gameplay.combat.session`  
- **Frontend module:** `modules/gameplay/combat/sessions`  
- **Shared clients:** `@api/gameplay/combat`, `@shared/state/useCombatStore`, `@shared/ws/CombatSessionChannel`

> Все значения должны соответствовать таблице микросервисов в `GLOBAL-RULES.md`.

## Scope of Work
1. Сформировать REST контракт для создания боевой сессии, подключения участников и завершения боя (PvE, PvP, рейды).
2. Описать эндпоинты действий (`attack`, `use-skill`, `use-item`, `flee`, `end-turn`) с валидацией очередности ходов и ответами с результатами.
3. Определить ресурсы чтения состояния (`GET /{sessionId}`, `GET /{sessionId}/timeline`, `GET /{sessionId}/participants`) и контроль сердцебиения клиента (`POST /{sessionId}/sync`).
4. Задокументировать WebSocket канал `/ws/gameplay/combat/sessions/{sessionId}` (push обновления) и SSE fallback.
5. Спроектировать JSON схемы: `CombatSession`, `CombatParticipant`, `CombatActionRequest`, `DamageResolution`, `TurnState`, `CombatLogEntry`, `RewardSummary`.
6. Зафиксировать Kafka события (`combat.started`, `combat.action.processed`, `combat.ended`, `combat.player.died`, `combat.enemy.killed`, `combat.damage.dealt`) с payload и маршрутизацией.
7. Описать механизмы безопасности: JWT + пермишены `combat:session:*`, anti-cheat сигнатуры, лимиты действия, защита от повторных запросов.
8. Указать зависимости от внешних сервисов: character-service (статы), inventory-service (предметы), economy-service (награды), quest-service (обновление прогресса), telemetry-service (боевые метрики).
9. Прописать метрики (`combat_session_active_total`, `combat_action_latency_ms`, `combat_desync_detected_total`) и audit trail.
10. Обновить очереди и `brain-mapping.yaml`, убедиться в прохождении `validate-swagger.ps1`.

## Endpoints
- `POST /` — создать новую боевую сессию (PvE/PvP), сформировать участников, инициировать turn order.
- `PATCH /{sessionId}/participants` — подключить/отключить участника (для late join, companion summon).
- `POST /{sessionId}/actions/attack` — провести атаку (supports melee/ranged/magic, использует данные из combat ballistics).
- `POST /{sessionId}/actions/use-skill` — применить навык/способность с учётом кулдаунов и ресурсов.
- `POST /{sessionId}/actions/use-item` — использовать предмет (инъекция, граната, deployable).
- `POST /{sessionId}/actions/flee` — инициировать попытку бегства; включает проверку условий.
- `POST /{sessionId}/actions/end-turn` — завершить ход вручную (turn-based режим).
- `POST /{sessionId}/sync` — heartbeat/синхронизация клиента, сообщает latency, desync флаги.
- `GET /{sessionId}` — текущее состояние боевой сессии (композиция состояния, таймеры).
- `GET /{sessionId}/timeline` — журналы событий с пагинацией и фильтрами.
- `GET /{sessionId}/participants` — состав сторон, статы, статус подключения.
- `GET /{sessionId}/rewards/preview` — расчёт потенциальных наград до завершения боя.

## Data Models
- `CombatSession` — id, combatType, mode (real-time/turn-based), zone, status, turnState, start/ended timestamps.
- `CombatParticipant` — entityId, entityType (PLAYER/NPC/ALLY_DRONE), team, statsSnapshot, controlState.
- `CombatActionRequest` — actionType, actorId, targetIds, payload (skillId, itemId, projectileData), antiCheatToken.
- `DamageResolution` — baseDamage, modifiers, mitigations, finalDamage, critical, statusEffects.
- `TurnState` — currentActorId, turnOrder array, round number, remainingTimeMs.
- `CombatLogEntry` — sequenceId, timestamp, actor, target, actionType, result, metadata JSON.
- `RewardSummary` — xpGranted, lootTableRolls, currency, reputationChanges, questTriggers.
- Общие компоненты: ссылки на `@components/schemas/Pagination`, `@components/responses/StandardError`, `security.yaml`.

## Integrations & Events
- REST зависимости: `character-service` (`GET /internal/characters/{id}/combat-stats`), `inventory-service` (`POST /internal/inventory/consume`), `economy-service` (`POST /internal/rewards/grant`), `quest-service` (`POST /internal/quests/progress`), `anti-cheat-service` (`POST /internal/anticheat/validate-action`).
- WebSocket: `/ws/gameplay/combat/sessions/{sessionId}` (events `state.update`, `action.resolved`, `desync.alert`); fallback SSE `/stream/gameplay/combat/sessions/{sessionId}`.
- Kafka topics: `gameplay.combat.started`, `gameplay.combat.action.processing`, `gameplay.combat.action.resolved`, `gameplay.combat.ended`, `gameplay.combat.player.died`, `gameplay.combat.enemy.killed`, `gameplay.combat.damage.dealt`. Указать producer (gameplay-service) и consumers (telemetry-service, quest-service, economy-service, notification-service).
- Metrics & tracing: OpenTelemetry spans `CombatSession.start`, `CombatAction.resolve`, `CombatSession.end`; Prometheus метрики и распределённые трейсы в Jaeger.

## Acceptance Criteria
1. Файл `api/v1/gameplay/combat/combat-session.yaml` (≤ 500 строк) создан и проходит `OpenAPI 3.0.3` валидацию.
2. Раздел `info.x-microservice` указывает `gameplay-service`, порт `8083`, base-path `/api/v1/gameplay/combat/sessions`.
3. Все перечисленные REST эндпоинты описаны с запросами/ответами, кодами 2xx/4xx/5xx, примерами и ссылками на общие компоненты.
4. Схемы данных (`CombatSession`, `CombatParticipant`, `CombatActionRequest`, `DamageResolution`, `TurnState`, `CombatLogEntry`, `RewardSummary`) вынесены в `components.schemas` и имеют required поля.
5. WebSocket и SSE каналы задокументированы через `x-stream`/`webhooks` раздел (см. `tasks/config/checklist.md` пример).
6. Kafka события перечислены в `x-events` с описанием payload, partition key, retention, producer/consumer.
7. Безопасность: указаны схемы `bearerAuth`, пермишены `combat:session:*`, лимиты частоты (через `x-rate-limit`), anti-cheat токен.
8. Метрики и audit trail описаны в разделах `x-observability` и `x-audit`.
9. Обновлены `tasks/config/brain-mapping.yaml` и `tasks/queues/queued.md` с новой задачей.
10. `pwsh -NoProfile -File ..\scripts\validate-swagger.ps1 -ApiSpec api/v1/gameplay/combat/combat-session.yaml` выполняется без ошибок.

## FAQ / Notes
- Режимы боя: real-time (шутер) и turn-based (D&D проверки); спецификация должна позволять `mode: REALTIME | TURN_BASED`.
- Для рейдов поддержать многоволновые стадии: `waveIndex`, `stageId`.
- Anti-cheat токен формируется клиентом из EAC модуля — требовать заголовок `X-AntiCheat-Signature`.
- Учесть десинхронизацию: эндпоинты возвращают `desyncHints` массив для корректировок клиента.
- PvP требует аудита: логировать `reason` при принудительном завершении или кике игрока.

