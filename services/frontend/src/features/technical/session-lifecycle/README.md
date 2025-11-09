# Session Lifecycle Feature
Контроль жизненного цикла сессий: heartbeat, AFK, force logout и политики.

**OpenAPI:** technical/session-management/lifecycle.yaml | **Роут:** /technical/session-lifecycle

## UI
- `SessionLifecyclePage` — SPA (380 / flex / 320), фильтр статуса, auto AFK warning toggle
- Компоненты:
  - `SessionCard`
  - `HeartbeatMetricsCard`
  - `AfkWarningCard`
  - `ForceLogoutCard`
  - `SessionPoliciesCard`
  - `SessionDiagnosticsCard`

## Возможности
- Мониторинг сессий (статус, heartbeat, expiry)
- Heartbeat метрики и auto AFK предупреждения
- Планирование force logout для concurrent login
- Просмотр политики и диагностики (SLA, concurrent sessions)
- Киберпанк сетка, умещается на один экран

## Тесты
- Юнит-тесты для карточек в `components/__tests__`
- Написаны, **не запускались**


