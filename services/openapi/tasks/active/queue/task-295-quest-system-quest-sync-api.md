# Task ID: API-TASK-295
**Тип:** API Generation  
**Приоритет:** высокий  
**Статус:** queued  
**Создано:** 2025-11-08 07:20  
**Создатель:** AI Agent (GPT-5 Codex)  
**Зависимости:** API-TASK-283 (quest branching database API), API-TASK-294 (quest main choose path API), API-TASK-218 (achievement core API), API-TASK-219 (achievement tracking API)

---

## 📋 Краткое описание

Создать OpenAPI спецификацию `api/v1/world/state/quest-sync.yaml` для world-service. Спецификация должна обеспечивать синхронизацию мирового состояния и последствий квестов между gameplay-service, world-service, social-service и economy-service. Документ описывает ответы на технические вопросы по квестовой системе, включая ветвления, мультиплеерные сессии, последствия, телеметрию и конфликты состояний.

---

## 🎯 Цель задания

- Обеспечить REST и WebSocket контуры world-service для применения квестовых последствий к мировому состоянию.
- Синхронизировать флаги игрока, world_state, репутацию и награды через единый API слой.
- Поддержать мультиплеерные квестовые сессии с блокировками, инстансами и откатами.
- Интегрировать Kafka события `world.state.updated`, `quest.session.changed`, `quest.flags.updated`.
- Закрыть потребности UI: World Pulse, Quest Master Dashboard, GM Tools.
- Обеспечить наблюдаемость: метрики, аудит, конфликт-репорты, журнал применения последствий.

---

## 📚 Источники информации

- `.BRAIN/06-tasks/active/CURRENT-WORK/active/quest-system-tech-questions-compact.md`
- `.BRAIN/06-tasks/active/CURRENT-WORK/active/quest-branching-database/README.md`
- `.BRAIN/06-tasks/active/CURRENT-WORK/active/quest-branching-database/part1-analysis-core.md`
- `.BRAIN/06-tasks/active/CURRENT-WORK/active/quest-branching-database/part2-advanced-examples.md`
- `.BRAIN/04-narrative/dialogues/quest-main-002-choose-path.md`
- `.BRAIN/02-gameplay/world/world-state/world-governance-model.md`
- `.BRAIN/02-gameplay/social/reputation-formulas.md`

---

## 📁 Целевая структура API

**Файл:** `api/v1/world/state/quest-sync.yaml`  
**Микросервис:** world-service (port 8086)  
**Интеграции:** gameplay-service (quest progression), social-service (reputation/flags), economy-service (rewards), analytics-service (telemetry), notification-service (player/world alerts)  
**Frontend:** `modules/world/control-center`, `modules/gameplay/quests`, GM panel, Operations HUD

---

## 🧩 Обязательные секции спецификации

1. `GET /api/v1/world/state/quests/{questId}` — получить агрегированное мировое и социальное состояние, связанное с квестом.
2. `POST /api/v1/world/state/quests/{questId}/apply` — применить последствия ветки; атомарная транзакция (world_state, player_flags, reputation, rewards).
3. `POST /api/v1/world/state/quests/{questId}/preview` — dry-run: рассчитать последствия без применения, вернуть deltas и потенциальные конфликты.
4. `POST /api/v1/world/state/quests/{questId}/conflicts/resolve` — предоставить решение конфликтов (optimistic locking, merge стратегии).
5. `POST /api/v1/world/state/quests/{questId}/sessions/{sessionId}/lock` — управление блокировками/инстансами для кооперативных квестов.
6. `DELETE /api/v1/world/state/quests/{questId}/sessions/{sessionId}/lock` — освобождение блокировки/rollback.
7. `GET /api/v1/world/state/quests/{questId}/audit` — история применённых последствий, telemetry linkage.
8. WebSocket `/ws/world/state/quest-sync` — события: `QuestStateApplied`, `QuestConflictDetected`, `QuestSessionLocked`, `QuestSessionReleased`, `QuestFlagsUpdated`.
9. Event Bus: описать публикацию `world.state.updated`, `quest.session.changed`, `quest.flags.updated` (Kafka topics), payload схемы.
10. Observability: модели `QuestSyncMetrics`, `QuestConflictReport`, `QuestTelemetry`.

---

## 🗃️ Модели и схемы

- `QuestStateSnapshot`, `QuestStateDelta`, `QuestConsequenceRequest`
- `WorldStateChange`, `PlayerFlagChange`, `ReputationAdjustment`, `RewardGrant`
- `QuestConflict`, `ConflictResolutionRequest`, `ConflictResolutionResult`
- `QuestSessionLock`, `QuestSessionParticipant`, `QuestSessionStatus`
- `QuestAuditEntry`, `TelemetryEvent`, `MetricSample`

Модели должны ссылаться на общие компоненты (`shared/common/responses.yaml`, `shared/common/pagination.yaml`, `shared/security/security.yaml`), использовать PascalCase для схем и kebab-case для файлов/путей.

---

## 🔄 Интеграции и события

- REST: взаимодействие с `api/v1/gameplay/quests/branching-database.yaml` (обновление прогресса), `api/v1/social/reputation/reputation-formulas.yaml`, `api/v1/economy/rewards/grants.yaml`.
- Kafka: `world.state.updated`, `quest.session.changed`, `quest.flags.updated`, `quest.telemetry.recorded`.
- Redis: `quest-session-cache` для блокировок и TTL.
- GM Overrides: поддержка ручного отката через `DELETE /apply` (обсудить в FAQ).

---

## ✅ Критерии приёмки

1. Все endpoints расположены под `/api/v1/world/state/quests`.
2. `POST /apply` обрабатывает мульти-сервисную транзакцию с rollback сценариями.
3. `POST /preview` возвращает детальный delta-пакет (world_state, flags, reputation, rewards).
4. Поддержана optimistic locking схема (`version`, `updatedAt`) и конфликт-ответ `409`.
5. WebSocket события содержат `questId`, `sessionId`, `locale`, `delta`, `telemetryId`.
6. Event bus описание включает топики, ключи сообщений, повторную доставку, idempotency.
7. Наблюдаемость: метрики и аудит соответствуют документу (branch switch, concurrency, conflicts).
8. Схемы используют общие компоненты (`Error`, `Paging`, security scopes).
9. FAQ покрывает смену владельца сессии, повторное применение последствий, ручной GM override.
10. Документация описывает зависимости на Kafka, Redis, Postgres, а также требования к миграциям (`world_state`, `quest_audit`, `quest_sessions`, `player_flags`).
11. Указаны требования к нагрузочному тестированию и лимитам (rate limiting, очередь конфликтов).
12. Добавлены примеры запросов/ответов для всех endpoints (success, conflict, validation error).

---

## 🧪 Чеклист перед передачей

- [ ] Все обязательные блоки задания заполнены.
- [ ] Ссылки на источники .BRAIN корректны.
- [ ] Указан целевой микросервис и зависимости между сервисами.
- [ ] Описаны схемы событий Kafka и WebSocket.
- [ ] Приведены критерии приёмки и FAQ.
- [ ] Проверена совместимость с существующими API задачами (branching, achievements).

---

## ❓ FAQ

- **Что делать при конфликте world_state?** Использовать `/conflicts/resolve`, указать стратегию merge (override, queue, split instance).
- **Как обрабатывать смену владельца квестовой сессии?** Через `POST /sessions/{sessionId}/lock` с новым owner и публикацией `quest.session.changed`.
- **Можно ли переиграть последствия?** GM вызывает `DELETE /sessions/{sessionId}/lock` + повторный `/apply` с `retryToken`.
- **Как логируются критические ошибки?** Событие `quest.telemetry.recorded` с priority=high, запись в `quest_audit`.
- **Какие UI модули используют API?** World Pulse, Operations HUD, Guild Ops Dashboard, GM Tools, Quest Master Dashboard.

---



### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

