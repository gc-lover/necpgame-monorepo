# Task ID: API-TASK-240
**Тип:** API Generation
**Приоритет:** высокий
**Статус:** queued
**Создано:** 2025-11-07 16:30
**Создатель:** GPT-5 Codex (Brain Manager)
**Зависимости:** API-TASK-181, API-TASK-235, API-TASK-237

---

## 📋 Краткое описание

Создать OpenAPI спецификацию рейда «Quantum Reef Siege» — кооперативного PvE-контента с биолюминесцентными мехокракенами, фазами Sky Insertion / Reef Breach / Titan Lock, тренировочным режимом Skyframe Academy и синхронизацией пилотов и мехов.

**Что нужно сделать:** Подготовить `api/v1/narrative/raids/quantum-reef-siege.yaml`, охватывающий управление рейдом, синхронизацию пилотов, распределение лута и мониторинг прогресса.

---

## 🎯 Цель задания

Дать `gameplay-service` и фронтенду полное API для организации рейда, тренировки, управления фазами, наградами и телеметрией.

**Зачем это нужно:**
- Формализовать трёхфазный бой и условия победы/поражения.
- Поддержать сертификацию пилотов (T1/T2/T3) и тренировки Skyframe Academy.
- Обеспечить распределение редких наград (`Lumina Shards`, легендарные предметы).
- Подключить уведомления, телеметрию и события для UI и аналитики.

---

## 📚 Источники информации

### Основной документ

**Путь:** `.BRAIN/06-tasks/active/CURRENT-WORK/archive/2025-11-07-hybrid-media-references-expansion.md`
**Версия:** v1.1.0 (2025-11-07 16:14)
**Статус:** approved, api-readiness: ready

**Ключевые разделы:**
- «Рейд “Quantum Reef Siege”» (сюжет, фазы, механики).
- «Сертификация пилотов» (уровни допуска, требования, credential).
- «Moodboard и аудио-пакеты» (ID `mood/reef-v1`, `audio/reef-res`).
- «Решённые вопросы» (метрики синхронизации: latency/drift/reconnect).

### Дополнительные источники

- `.BRAIN/02-gameplay/combat/combat-session-backend.md`
- `.BRAIN/02-gameplay/combat/combat-roles-detailed.md`
- `.BRAIN/02-gameplay/world/events/world-events-framework.md`
- `.BRAIN/02-gameplay/economy/loot-tables.md`
- `.BRAIN/05-technical/backend/realtime-server/part1-architecture-zones.md`

### Связанные документы

- `API-SWAGGER/api/v1/narrative/raids/raid-blackwall.yaml`
- `API-SWAGGER/api/v1/combat/combat-session.yaml`
- `API-SWAGGER/api/v1/loot/loot-system.yaml`

---

## 📁 Целевая структура API

- **Файл:** `api/v1/narrative/raids/quantum-reef-siege.yaml`
- **Версия API:** v1
- **Формат:** OpenAPI 3.0.3

```
API-SWAGGER/api/v1/narrative/raids/
 ├── raid-blackwall.yaml
 ├── raid-corpo-tower.yaml
 ├── quantum-reef-siege.yaml     ← создать
 └── ...
```

---

## 🏗️ Целевая архитектура (⚠️ ОБЯЗАТЕЛЬНО)

```yaml
# Target Architecture:
# - Microservice: gameplay-service (raid module, порт 8083)
# - Base Path: /api/v1/gameplay/raids/quantum-reef-siege
# - Components: raid-state manager, pilot-sync controller, loot distributor, telemetry emitter
# - Datastores: Postgres tables raid_instances, raid_phases, pilot_credentials, raid_loot_rolls
# - Cache: Redis raid:state:{instanceId}, raid:sync:{pilotId}
# - Streams: Kafka topics raid.quantum.progress, raid.quantum.telemetry
# - Frontend Module: modules/combat/raids, modules/world/events
# - UI Components: RaidLobby, PhaseProgressTimeline, SyncCalibrationHUD, LootSummaryModal
# - Training: Skyframe Academy simulator (auth-service credential, reused by matchmaking)
```

---

## ✅ Что нужно сделать (детальный план)

1. Описать модели рейда (инстансы, фазы, таймеры, состояние фаз).
2. Реализовать регистрацию и валидацию пилотов (T1/T2/T3, credential expiry 90 дней).
3. Добавить API тренировочного режима Skyframe Academy (3 миссии, 20 мин, сертификация).
4. Определить события и состояние для трёх фаз (Sky Insertion, Reef Breach, Titan Lock).
5. Настроить синхронизацию пилотов/мехов (latency ≤120 ms, drift ≤0.35, reconnect ≤3).
6. Задать правила распределения наград (Lumina Shards, легендарные предметы, титулы).
7. Поддержать мониторинг и телеметрию (progress, wipes, DPS, latency).
8. Задокументировать WebSocket канал для live HUD и уведомлений.

---

## 🔀 Endpoints (минимальный набор)

1. **POST `/raids/quantum-reef-siege/instances`** – создать рейдовый инстанс (уровень сложности, расписание, состав).
2. **GET `/raids/quantum-reef-siege/instances/{instanceId}`** – текущее состояние (фаза, таймер, здоровье боссов, синхронизация).
3. **POST `/raids/quantum-reef-siege/instances/{instanceId}/join`** – регистрация пилота/штурмового отряда (проверка credential).
4. **POST `/raids/quantum-reef-siege/pilot-certification`** – прохождение Skyframe Academy (результаты, tier).
5. **GET `/raids/quantum-reef-siege/pilot-certification/{playerId}`** – просмотр допуска T1/T2/T3 и срока действия.
6. **POST `/raids/quantum-reef-siege/instances/{instanceId}/phases/{phase}/complete`** – завершение фазы, переход к следующей.
7. **POST `/raids/quantum-reef-siege/instances/{instanceId}/telemetry`** – отправка телеметрии (latency, drift, DPS, shield integrity).
8. **POST `/raids/quantum-reef-siege/instances/{instanceId}/loot`** – распределение лута (Lumina Shards, легендарные предметы, титулы).
9. **GET `/raids/quantum-reef-siege/instances/{instanceId}/loot`** – просмотр истории распределения и шанс выпадения.
10. **POST `/raids/quantum-reef-siege/instances/{instanceId}/events`** – регистрация особых событий (Emergency Evac, Endwave cinematic).
11. **POST `/raids/quantum-reef-siege/instances/{instanceId}/reset`** – сброс инстанса (после wipe, с таймером).
12. **GET `/raids/quantum-reef-siege/leaderboard`** – рейтинг гильдий и пилотов по времени/эффективности.
13. **WS `/raids/quantum-reef-siege/stream`** – события `phase-progress`, `sync-alert`, `loot-drop`, `wipe`, `success`, `cinematic`.

---

## 🧱 Модели данных

- **RaidInstance**: `instanceId`, `difficulty`, `status`, `phase`, `startAt`, `timeElapsed`, `players`, `mechs`, `wipes`.
- **RaidPhaseState**: `phaseId`, `progress`, `objectives`, `bossHealth`, `mechanics`, `timeLimit`.
- **PilotCredential**: `playerId`, `tier`, `score`, `modifiers`, `issuedAt`, `expiresAt`.
- **SyncMetrics**: `pilotId`, `latencyMs`, `drift`, `reconnectAttempts`, `lastSyncAt`, `status`.
- **TelemetryPayload**: `partyId`, `instanceId`, `dps`, `heal`, `shieldIntegrity`, `event`.
- **LootDrop**: `dropId`, `instanceId`, `itemId`, `rarity`, `distribution`, `rolls`, `awardedTo`.
- **CinematicEvent**: `eventId`, `type`, `timestamp`, `dialogueId`, `cutsceneId`.
- **TrainingResult**: `playerId`, `missionScores`, `overallScore`, `certifiedTier`, `attempts`.

---

## 🧭 Принципы и правила

- **Training gate:** T2/T3 допускаются только при успешном завершении тренировок с установленными порогами.
- **Latency guard:** автоматический триггер предупреждения при `latency > 120ms` или `drift > 0.35`.
- **Loot fairness:** поддержать Need/Greed/Priority систему и гарантированный drop Lumina Shards.
- **Cinematic sync:** финальные кат-сцены зависят от исхода (успех/провал) и транслируются через world-service.
- **Analytics export:** все попытки записываются в analytics-service (success_rate, avg_phase_time, wipe_rate).
- **Resilience:** поддержать reconnect (до 3 попыток) с сохранением состояния меха.

---

## 🧪 Примеры

- Гильдия стартует рейд, проходит тренировку Skyframe Academy, получает T2 credential и завершает Phase 1.
- Во время Phase 2 система фиксирует drift 0.4 → генерируется sync alert через WS, предлагается калибровка.
- Успешное уничтожение Kraken Prime → кат-сцена «Echo Collapse», выдача титула, распределение Lumina Shards и легендарного оружия.
- Провал рейда → запускается эвент `Emergency Evac`, фиксируется wipe, таймер до следующей попытки 12 часов.

---

## 🔗 Связности и зависимости

- `matchmaking-service` – подбор состава рейда (при необходимости).
- `auth-service` – хранение credentials (tier, expiry).
- `realtime-service` – синхронизация позиций, боевая телеметрия.
- `loot-service` (API-TASK-235) – генерация и распределение наград.
- `notifications-service` – оповещения о прогрессе/кат-сценах.
- `analytics-service` – экспорт KPI рейда и производительности пилотов.

---

## ✅ Критерии приемки

1. `quantum-reef-siege.yaml` описывает все REST/WS маршруты, модели и примеры из документа .BRAIN.
2. В спецификации отражены три фазы, механики и условия переходов.
3. Реализован API тренировок, включая валидацию сертификации и хранение credential.
4. Определены метрики синхронизации (latency, drift, reconnect) и события их обработки.
5. Добавлены контракты распределения лута, лидербордов и кат-сцен.
6. Задокументированы интеграции с loot-, realtime-, analytics-, notifications-service.
7. Прописаны требования мониторинга и экспорт телеметрии в аналитические дашборды.


### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

