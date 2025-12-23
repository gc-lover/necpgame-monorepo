<!-- Issue: #194 -->

# Архитектура: Session Management System

## Цели

- Жизненный цикл игровых сессий: создание, heartbeat, таймауты, завершение.
- Надёжное переподключение (fast reconnect ≤5 мин), контроль конкурентных сессий.
- Наблюдаемость: метрики/логи/алерты, аудит операций.

## Контекст и нагрузки

- Клиенты: UE5, backend сервисы (auth-service, gameplay-service, world-service).
- Пик: 10k RPS на heartbeat, 1k RPS на create/reconnect/logout, P99 < 50ms.
- Горизонтальное масштабирование auth-service + session-service, Redis кластер, Postgres primary + replicas.

## Компоненты

- API Gateway/Auth Service: внешние REST/WebSocket точки, валидация токенов, rate limiting.
- Session Service (core): бизнес-логика сессий, переходы состояний, heartbeat, reconnect, admin API.
- Session Store (Postgres): `player_sessions`, `session_audit_log`.
- Session Cache (Redis): быстрый lookup токена, активные игроки, окна reconnect.
- Event Bus (Kafka/NATS): события `session.created|heartbeat|expired|terminated|reconnected|logout`.
- Monitoring: Prometheus + Grafana; логирование в Loki; алерты в Alertmanager.
- Cleanup Worker: крон каждые 5 минут — закрытие просроченных/idle, чистка кешей.

## Высокоуровневые потоки

- Create: client → `/session/create` → валидация → запись в DB (tx) → запись в Redis → publish `session.created`.
- Heartbeat: client → `/session/heartbeat` (batch friendly) → обновление TTL в Redis и `last_seen` в DB (batched) →
  метрики.
- Reconnect: client → `/session/reconnect` с токеном/refresh → проверка окна 5 мин → восстановление статуса → publish
  `session.reconnected`.
- Logout/Terminate: client/admin → `/session/logout` → закрытие в DB, очистка Redis, publish `session.terminated`.
- Admin/Monitoring: `/admin/sessions/*` — чтение состояния, принудительное завершение, метрики/health.

## API (высокоуровневые)

- POST `/api/v1/auth/session/create` — body: player_id, device_fingerprint, client_version, region.
- POST `/api/v1/auth/session/heartbeat` — body: session_id, token, latency_ms, payload; поддержка batch до 100 записей.
- POST `/api/v1/auth/session/reconnect` — body: player_id, session_id, token/refresh_token.
- POST `/api/v1/auth/session/logout` — body: session_id, reason (user/idle/kick/admin).
- GET `/api/v1/auth/session/status` — query: session_id.
- Admin: GET `/api/v1/auth/admin/sessions` (filter by shard/region/status), POST
  `/api/v1/auth/admin/sessions/{id}/terminate`.

## Модель данных (DB, Postgres)

- `player_sessions`:
    - id (UUID, PK), player_id (UUID, idx), token (TEXT, unique), status (ENUM), region, shard_id, created_at,
      last_seen_at, expires_at, reconnect_until, client_version, device_fp, ip, user_agent.
    - Индексы: (player_id, status), (status, last_seen_at), (expires_at), (reconnect_until).
- `session_audit_log`:
    - id (BIGSERIAL, PK), session_id (UUID, FK), event (created/heartbeat/reconnect/logout/expired/terminated), meta
      JSONB, created_at.
    - Индексы: (session_id), (event, created_at desc).

## Кеш (Redis)

- `session:{token}` → session_id, player_id, status, expires_at, reconnect_until, shard_id; TTL = expires_at.
- `active_players:{shard}` → set of session_id; TTL 10m (обновляется heartbeat).
- `reconnect:{player_id}` → session_id, window 5m.
- Rate limit keys per player/device for create/reconnect.

## Состояния и переходы

- CREATED → ACTIVE → (RECONNECTING|IDLE) → CLOSED (EXPIRED/LOGOUT/TERMINATED).
- Heartbeat восстанавливает ACTIVE; отсутствие heartbeat → IDLE → EXPIRED.
- RECONNECTING допускается только в окне reconnect_until.

## Надёжность и таймауты

- Все внешние handler’ы с context timeout ≤ 150ms (read) / 500ms (write).
- Базовые retry с jitter для Redis/DB (идемпотентные операции).
- Batch heartbeat: upsert пачками 50-100, single tx.
- Cleanup Worker: каждые 5 минут — закрытие EXPIRED/IDLE, чистка Redis, событие `session.cleanup`.

## Безопасность

- Token binding: ip/ua/region; rotate on reconnect; опциональный device_fingerprint.
- Защита от захвата: revoke on suspicious IP change, optional step-up (re-auth).
- Rate limiting: 5 create/час, 20 reconnect/день на игрока; глобальные лимиты.
- Audit: все административные действия в `session_audit_log`.

## Наблюдаемость

- Метрики Prometheus: `session_active`, `session_heartbeat_rps`, `session_reconnect_success_total`,
  `session_reconnect_failed_total`, `session_expired_total`, latency гистограммы per endpoint.
- Логи: структурированные, кореляция по session_id/player_id, уровень warn на drop heartbeat.
- Алерты: P99 heartbeat >100ms 5m; reconnect_fail_rate >5% 5m; expired surge >20% за 10м; Redis/DB error rate >1%.

## Интеграции

- Auth-service ↔ Session-service: REST + события.
- Gameplay/world/chat: подписка на `session.terminated`/`session.logout` для очистки state.
- Analytics-service: consume `session.created|reconnected|terminated` для DAU/retention.

## Разбиение на подзадачи (handoff)

- API Designer: подготовить OpenAPI для create/heartbeat/reconnect/logout/admin (base spec) с common.yaml.
- Backend: реализовать session-service (handlers + DB/Redis), batch heartbeat, cleanup worker, события.
- DB: миграции для `player_sessions`, `session_audit_log` (колонки отсортированы large→small, индексы).
- Security: правила token binding, rate limits, audit.
- DevOps: dashboards + алерты, Redis/PG pool config.
- QA: сценарии reconnect/window/timeout/cleanup, нагрузочное на heartbeat (10k RPS).

## Ограничения и size budget

- Файл <500 строк; хранилища: Redis TTL ≤ session lifetime; DB P99 < 10ms на primary idx запросы.

