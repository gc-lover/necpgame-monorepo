# Task ID: API-TASK-197
**Тип:** API Generation
**Приоритет:** критический
**Статус:** queued
**Создано:** 2025-11-07 20:35
**Создатель:** GPT-5 Codex (API Task Creator)
**Зависимости:** API-TASK-111, API-TASK-196

---

## 📋 Краткое описание

Создать OpenAPI + WebSocket спецификацию протокола realtime сервера (сообщения, лаг-компенсация, оптимизации).

**Что нужно сделать:** Описать REST/WebSocket контракты для протокола realtime (MessagePack, client prediction, delta updates, anti-lag) по документу `part2-protocol-optimization.md`.

---

## 🎯 Цель задания

Задокументировать коммуникационный слой между клиентом и realtime-сервисом, включая форматы сообщений, алгоритмы обработки latency и метрики качества.

**Зачем это нужно:**
- Дать фронтенду и SDK полное описание WebSocket каналов и типов сообщений
- Обеспечить согласованность между client prediction и server reconciliation
- Формализовать механизмы delta-компрессии и приоритизации данных
- Подготовить инструменты мониторинга и отладки протокола

---

## 📚 Источники информации

### Основной источник концепции

**Репозиторий:** `.BRAIN`
**Путь:** `.BRAIN/05-technical/backend/realtime-server/part2-protocol-optimization.md`
**Версия:** v1.0.1
**Дата обновления:** 2025-11-07
**Статус документа:** approved

**Что важно:**
- Типы сообщений Client → Server (`PLAYER_INPUT`, `HEARTBEAT`) и Server → Client (`STATE_UPDATE`, `COMBAT_EVENT`)
- MessagePack сериализация, примеры Java/JS кода
- Client-side prediction, server reconciliation, lag compensation (combat rewind)
- Delta compression, priority system, update rate scaling
- Bandwidth optimisation (interest queues, burst handling)

### Дополнительные источники

- `.BRAIN/05-technical/backend/realtime-server/part1-architecture-zones.md` – распределение зон и tick-rate
- `.BRAIN/05-technical/backend/session-management/part2-reconnection-monitoring.md` – reconnect flow
- `.BRAIN/05-technical/backend/anti-cheat/anti-cheat-compact.md` – валидация input
- `.BRAIN/05-technical/backend/matchmaking/matchmaking-rating.md` – влияние рейтинга на QoS

### Связанные документы

- `.BRAIN/05-technical/backend/global-state/global-state-operations.md`
- `.BRAIN/05-technical/backend/voice-chat/voice-chat-system.md`
- `.BRAIN/05-technical/backend/incident-response/incident-response.md`

---

## 📁 Целевая структура API

- **Репозиторий:** `API-SWAGGER`
- **Файл:** `api/v1/technical/realtime/realtime-protocol.yaml`
- **API версия:** v1
- **Тип:** OpenAPI 3.0.3 + WebSocket `x-websocket`

**Каталог:**
```
API-SWAGGER/api/v1/technical/realtime/
 ├── realtime-server.yaml (core)
 ├── server-zones.yaml  (управление зонами)
 └── realtime-protocol.yaml ← создать/заполнить
```

---

## 🏗️ Целевая архитектура (⚠️ ОБЯЗАТЕЛЬНО)

### Backend
- **Микросервис:** realtime-service
- **Порт:** 8089
- **Base Path:** `/api/v1/technical/realtime`
- **WebSocket endpoint:** `wss://api.necp.game/v1/technical/realtime/ws`
- **Зависимости:** session-service, anti-cheat-service, matchmaking-service, telemetry-service, global-state-service

### Frontend
- **Модуль:** `modules/gameplay/realtime`
- **State Store:** `useRealtimeGameStore`
- **State:** `connection`, `playerPrediction`, `worldSnapshots`, `latencyMetrics`
- **UI:** `LatencyIndicator`, `PredictionDebugPanel`, `CombatPlaybackViewer`
- **Формы:** `DebugMessageSender`, `NetworkSettingsForm`
- **Layouts:** `GameLayout`, `DebugOverlay`
- **Хуки:** `useRealtimeConnection`, `usePrediction`, `useBandwidthProfiler`

### Комментарий для YAML

```yaml
# Target Architecture:
# - Microservice: realtime-service (port 8089)
# - Base Path: /api/v1/technical/realtime
# - WebSocket: wss://api.necp.game/v1/technical/realtime/ws
# - Dependencies: session-service, anti-cheat-service, matchmaking-service, telemetry-service, global-state-service
# - Frontend Module: modules/gameplay/realtime (useRealtimeGameStore)
# - UI: LatencyIndicator, PredictionDebugPanel, CombatPlaybackViewer
# - Forms: DebugMessageSender, NetworkSettingsForm
# - Layouts: GameLayout, DebugOverlay
# - Hooks: useRealtimeConnection, usePrediction, useBandwidthProfiler
```

---

## ✅ Что нужно сделать (детальный план)

1. **Проанализировать протокол** – сформировать словарь сообщений, статусы соединения, коды ошибок.
2. **Описать handshake** – REST endpoint для выдачи токена, параметры WebSocket подключения, ограничения MessagePack.
3. **Специфицировать сообщения** – схемы для client input, state update, combat events, chat relay, system notifications.
4. **Задокументировать алгоритмы** – поля для prediction, reconciliation, sequence IDs, timestamps, lag compensation.
5. **Определить QoS механизмы** – delta compression, priority levels, burst control, update frequency overrides.
6. **Добавить monitoring endpoints** – REST методы для debug, replay, измерений latency.
7. **Описать ошибки/alerts** – коды (`SEQ_OUT_OF_SYNC`, `PREDICTION_DIVERGED`, `BANDWIDTH_LIMIT`), связанные события.
8. **Проверить чеклист** – `tasks/config/checklist.md`, подготовить FAQ и тест-план.

---

## 🔀 Endpoints и WebSocket каналы

### REST (HTTP)
1. `POST /api/v1/technical/realtime/token` – получить WebSocket токен (sessionId, playerId, QoS profile).
2. `POST /api/v1/technical/realtime/debug/replay` – запросить replay по tick диапазону.
3. `GET /api/v1/technical/realtime/metrics/latency` – метрики latency/prediction divergence.
4. `GET /api/v1/technical/realtime/metrics/bandwidth` – delta compression эффективность.
5. `POST /api/v1/technical/realtime/diagnostics/force-sync` – инициировать sync state для игрока.

### WebSocket (`wss://.../ws`)
- Канал `realtime` с MessagePack payload.
- **Client → Server:** `PLAYER_INPUT`, `HEARTBEAT`, `ACTION_ATTACK`, `ACTION_USE_SKILL`, `CHAT_MESSAGE`, `ACK_STATE`, `PING`.
- **Server → Client:** `STATE_UPDATE`, `COMBAT_EVENT`, `SYSTEM_NOTIFICATION`, `ZONE_CHANGED`, `PLAYER_DIED`, `DELTA_UPDATE`, `SYNC_CORRECTION`, `PONG`.
- Поддержать `x-message` описания в YAML (каждое сообщение со schema, примером и priority).

---

## 🧱 Модели данных

- **RealtimeTokenRequest/Response** – sessionId, playerId, deviceInfo, qosProfile, expiresAt.
- **WsMessageEnvelope** – `type`, `sequence`, `timestamp`, `payload` (`oneOf`).
- **PlayerInputMessage** – move vector, rotation, action, `predictionTimestamp`.
- **HeartbeatMessage** – `latencyMs`, `tickRate`, `clientTime`.
- **StateUpdateMessage** – tick, players array (compressed positions), NPC data, world events.
- **DeltaUpdatePatch** – changed entities, removed ids, `compressionStats`.
- **CombatEventMessage** – shooterId, targetId, damage, latencyUsed.
- **SyncCorrectionMessage** – authoritative position, velocity, `divergenceReason`.
- **PredictionMetrics** – sequences processed, divergence count, average reconciliation time.
- **BandwidthMetrics** – bytesSent, bytesSaved, compressionRatio, burstCount.
- **DebugReplayRequest/Response** – tick range, filtered players, binary blob URL.
- **Error schemas** – `RealtimeProtocolError`, с кодами (`SEQ_OUT_OF_SYNC`, `INVALID_MESSAGE_PACK` и т.д.).
- **Events** – `realtime.protocol.latency-spike`, `realtime.protocol.desync`, `realtime.protocol.burst`.

---

## 🧭 Принципы и правила

- Использовать `$ref` на `security.yaml` (`BearerAuth`, `ServiceToken`).
- Общие ошибки из `api/v1/shared/common/responses.yaml`.
- MessagePack описывать через `content: application/x-msgpack`.
- Согласовать sequence IDs (uint32), timestamps (Unix ms), QoS уровни (`LOW`, `NORMAL`, `HIGH`, `CRITICAL`).
- Указать лимиты частоты (`max 120 messages/sec`, `heartbeat every 2s`).

---

## 🧪 Примеры

- Handshake: запрос токена для игрока, ответ с QoS profile `HIGH`.
- WebSocket `PLAYER_INPUT` message (MessagePack + JSON псевдопредставление).
- `STATE_UPDATE` с delta patch и compression stats.
- Combat lag compensation кейс: входящий `ACTION_ATTACK` и ответ `COMBAT_EVENT` с rewind info.
- Alert `realtime.protocol.desync` через REST diagnostics.

---

## 🔗 Связности и зависимости

- Ссылается на `server-zones.yaml` (tick rate, zone assignments).
- Интеграция с session-service (валидировать токен и player session).
- Anti-cheat проверки при server reconciliation.
- Telemetry/incident сервисы для логирования latency spikes.

---

## ✅ Критерии приемки

1. `realtime-protocol.yaml` создан и содержит архитектурный комментарий.
2. Задокументирован REST handshake, debug и метрики.
3. WebSocket канал описан с message schemas (client/server обе стороны).
4. Все модели данных определены в `components/schemas` с примерами.
5. Учтены алгоритмы prediction, reconciliation, lag compensation, delta compression.
6. Добавлены коды ошибок и события мониторинга.
7. Описаны QoS профили, лимиты частоты, требования к heartbeat.
8. Подключены общие компоненты (`security`, `responses`).
9. Подготовлен тест-план (load, latency spikes, desync сценарии) и FAQ в задании.
10. Пройден чеклист `tasks/config/checklist.md`.

---

## ❓ FAQ

- **Как отправлять бинарные сообщения в Swagger?** – Использовать `application/x-msgpack` и приложить JSON-псевдо пример.
- **Что делать при рассинхронизации последовательностей?** – Отправить `force-sync`, клиент обязан сбросить prediction очередь.
- **Как обрабатывать лаг-сценарии?** – Логика `lag compensation`, `prediction` и `reconciliation` описана в схемах; см. `SyncCorrectionMessage`.
- **Можно ли менять частоту обновлений на лету?** – Да, через Parameter `updateRateOverride`, описать в `STATE_UPDATE`.
- **Как вести мониторинг?** – Использовать endpoints `/metrics/*` и события `latency-spike`/`desync`.
- **Что если MessagePack недоступен?** – Возвращать `406 Not Acceptable`, fallback не поддерживается (требовать обновление клиента).

---

## 🕓 История выполнения

- 2025-11-07 20:35 — Задание создано (GPT-5 Codex)

---

**Примечание:** Перед handoff пройти чеклист `tasks/config/checklist.md`.


### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

