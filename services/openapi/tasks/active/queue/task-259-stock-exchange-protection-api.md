# Task ID: API-TASK-259
**Тип:** API Generation
**Приоритет:** высокий (Post-MVP)
**Статус:** queued
**Создано:** 2025-11-07 23:25
**Создатель:** AI Agent (GPT-5 Codex)
**Зависимости:** API-TASK-122 (stock-exchange core API), API-TASK-161 (anti-cheat infrastructure API)

---

## 📋 Краткое описание

Нужно описать сервис защиты биржи от манипуляций: circuit breakers, инспекция сделок, действия против инсайдов и спуфинга.

**Что нужно сделать:** Создать файл `stock-exchange-protection.yaml`, фиксирующий REST API для обнаружения, расследования и санкций против злоупотреблений.

---

## 🎯 Цель задания

Обеспечить честность биржи, описав:
- Мониторинг триггеров (circuit breaker, price limits, insider flags)
- Подачу алертов, просмотр деталей, изменение статусов
- Создание дисциплинарных действий (предупреждение, бан, конфискация)
- Интеграцию с anti-cheat, guild-system и notification-service
- Метрики ложных срабатываний и отчётность для админов

---

## 📚 Источники информации

### Основной документ
- `.BRAIN/02-gameplay/economy/stock-exchange/stock-protection.md` — circuit breakers, price limits, insider detection, таблицы `surveillance_alerts`, `enforcement_actions`, API `/stocks/protection/*`, метрики

### Дополнительные
- `.BRAIN/05-technical/backend/anti-cheat/anti-cheat-core.md` — интеграция анти-чит
- `.BRAIN/05-technical/backend/guild/guild-system.md` — санкции против гильдий
- `.BRAIN/02-gameplay/economy/economy-events.md` — исключение легитимных событий
- `API-SWAGGER/api/v1/gameplay/economy/stock-exchange-trading.yaml` — структура ордеров для корреляции
- `API-SWAGGER/api/v1/gameplay/economy/anti-cheat.yaml` (если присутствует) — политика расследований

---

## 📁 Целевая структура

**Файл:** `api/v1/gameplay/economy/stock-exchange-protection.yaml`

**Структура:**
```
API-SWAGGER/
└── api/
    └── v1/
        └── gameplay/
            └── economy/
                ├── stock-exchange-core.yaml
                ├── stock-exchange-trading.yaml
                ├── stock-exchange-indices.yaml
                ├── stock-exchange-dividends.yaml
                ├── stock-exchange-events.yaml
                ├── stock-exchange-analytics.yaml
                └── stock-exchange-protection.yaml  ← создать
```

---

## 🏗️ Целевая архитектура (⚠️)

### Backend
- **Микросервис:** economy-service (compliance subdomain)
- **Порт:** 8085
- **API base:** `/api/v1/gameplay/economy/stocks/protection/*`
- **Зависимости:**
  - `anti-cheat-service` — подтверждение нарушений
  - `guild-service` — блокировка гильдий
  - `notification-service` — уведомления игрокам/админам
  - `economy-events` — whitelisting легитимных событий

### Frontend
- **Модуль:** `modules/economy/stocks`
- **Feature:** `modules/economy/stocks/protection`
- **State Store:** `useEconomyStore` (`surveillanceAlerts`, `enforcementActions`, `circuitBreakerStatus`)
- **UI (@shared/ui):** `AlertList`, `SeverityBadge`, `ActionTimeline`, `CircuitStatusCard`
- **Forms (@shared/forms):** `EnforcementActionForm`, `AlertFilterForm`
- **Layouts:** `@shared/layouts/AdminConsole`
- **Hooks:** `@shared/hooks/usePolling`, `@shared/hooks/useAuditTrail`

### API Gateway
```yaml
- id: economy-service
  uri: lb://ECONOMY-SERVICE
  predicates:
    - Path=/api/v1/gameplay/economy/stocks/protection/**
```

### Events
- Kafka: `economy.protection.alert_created`, `.alert_closed`, `.enforcement_issued`
- WebSocket (админ): `/ws/economy/stocks/protection`

---

## 🧩 План

1. Описать circuit breaker мониторинг (thresholds, время блокировки) и API получения статуса.
2. Реализовать REST для списка алертов, детального просмотра, обновления статуса.
3. Добавить административные действия (create enforcement, revoke, escalate).
4. Задокументировать интеграцию с anti-cheat (share alerts, cross-check IP/guild).
5. Учесть price limits и insider detection (модель нарушений и комментарии).
6. Описать схемы данных `SurveillanceAlert`, `EnforcementAction`, `CircuitBreakerState`.
7. Добавить observability: AlertRate, FalsePositiveRate, CircuitBreakerCount.

---

## 🧪 API Endpoints

1. **GET `/api/v1/gameplay/economy/stocks/protection/alerts`** — фильтры `severity`, `status`, `alertType`, `ticker`, `since`.
2. **GET `/api/v1/gameplay/economy/stocks/protection/alerts/{alertId}`** — детали: trigger details, подозрительные сделки, связанные игроки.
3. **PATCH `/api/v1/gameplay/economy/stocks/protection/alerts/{alertId}`** — изменение `status` (OPEN, IN_REVIEW, ESCALATED, CLOSED), добавление комментария.
4. **POST `/api/v1/gameplay/economy/stocks/protection/enforcement`** — создание действия: playerId/guildId, actionType (WARNING, SUSPENSION, BAN, CONFISCATION), duration, reason.
5. **GET `/api/v1/gameplay/economy/stocks/protection/enforcement`** — список действий с фильтрами.
6. **GET `/api/v1/gameplay/economy/stocks/protection/enforcement/{actionId}`** — детали и история апелляций.
7. **PATCH `/api/v1/gameplay/economy/stocks/protection/enforcement/{actionId}`** — эскалация/отмена (role check).
8. **GET `/api/v1/gameplay/economy/stocks/protection/circuit`** — статус circuit breakers по тикерам (active, cooldown, resumeAt).
9. **GET `/api/v1/gameplay/economy/stocks/protection/metrics`** — статистика (alerts per hour, false positive rate, enforcement counts).
10. **POST `/api/v1/gameplay/economy/stocks/protection/whitelist`** — добавление события/тикера в whitelist (чтобы не срабатывал alert).

Ошибки: 400 (invalid threshold), 403 (нет прав), 404 (alert/action не найден), 409 (уже обработано), 500 (internal).

---

## 🗄️ Схемы данных

- **SurveillanceAlert** — id, alertType (`INSIDER`, `SPOOFING`, `WASH_TRADE`, `PUMP_DUMP`), ticker, severity, triggerDetails (JSON), status, createdAt, updatedAt, handledBy.
- **CircuitBreakerState** — ticker, triggerReason (`PRICE_DROP`, `PRICE_SPIKE`, `VOLUME_SPIKE`), thresholdPercent, activatedAt, resumeAt.
- **EnforcementAction** — id, subjectType (`PLAYER`, `GUILD`), subjectId, actionType, reason, issuedBy, issuedAt, expiresAt, status, auditLog.
- **WhitelistEntry** — id, eventId, ticker, expiresAt, createdBy.
- **ProtectionMetrics** — alertRate, falsePositiveRate, averageHaltDuration, openAlerts, activeEnforcements.
- **AlertUpdateRequest** — status, comment, escalationLevel.

---

## 🔄 Интеграции

- **Anti-cheat:** `POST /anti-cheat/alerts/stock` (share case), `GET /anti-cheat/players/{id}/history`.
- **Guild-service:** suspend guild trading privileges (`POST /guilds/{id}/suspension`).
- **Notification-service:** уведомления игрокам о санкциях (`POST /notifications/direct`).
- **Economy-events:** whitelist легитимных событий (`GET /economy/events/{id}` для подтверждения).
- **Logging/Audit:** запись в `surveillance_audit` с userId, diff.

---

## 🗃️ База данных

- `surveillance_alerts` — хранение алертов (PK uuid, индексы по status, ticker, severity).
- `enforcement_actions` — дисциплинарные меры (PK uuid, индексы по player_id, guild_id, status).
- `surveillance_whitelist` — исключения (event_id, ticker, expires_at).
- `surveillance_audit` — журнал изменений.

---

## 📊 Мониторинг

- Метрики: `surveillance_alerts_total`, `alerts_false_positive_ratio`, `circuit_breaker_count`, `enforcement_actions_total`.
- Алерты: spike алертов > 50 за 5 минут, circuit breaker > 3 по одному тикеру, повторные нарушения игрока.
- Observability: OpenTelemetry span `surveillance-evaluation`, логирование тегов `alertType`, `severity`.

---

## ✅ Критерии приемки

1. Файл содержит блок `Target Architecture` и соответствует стилю OpenAPI.
2. Все маршруты используют префикс `/api/v1/gameplay/economy/stocks/protection`.
3. Поддержаны фильтры и пагинация для списков алертов/санкций.
4. Админские операции требуют роли и описывают 403/409 ответы.
5. Circuit breaker API предоставляет остаток времени и причину блокировки.
6. Интеграции с anti-cheat и guild-service описаны с указанием вызываемых методов.
7. Модели alert/enforcement включают обязательные поля из документа (severity, actionType, triggerDetails).
8. Указаны Kafka события и WebSocket канал.
9. Метрики observability перечислены и связаны с операциями.
10. FAQ раскрывает edge cases (обжалование, мульти-аккаунты, false positives).

---

## ❓ FAQ

**Q:** Как отличать легитимные события от манипуляций?

**A:** Проверять через `economy-events` и хранить whitelist; если событие присутствует у economy-events, alert можно закрыть как `LEGIT_EVENT`.

**Q:** Что делать, если игрок обжалует санкцию?

**A:** Добавить PATCH на enforcement с `status=APPEAL_PENDING`; описать workflow и audit requirements.

**Q:** Как обрабатывать гильдейские нарушения?

**A:** В `EnforcementAction` предусмотреть `subjectType=GUILD`, вызывать `guild-service` для блокировки, логировать коллективную ответственность.

**Q:** Можно ли автоматически разблокировать circuit breaker раньше времени?

**A:** Да, через админский PATCH с подтверждением; документируй требование двойного контроля (two-person rule) в разделе безопасности.

**Q:** Как хранить доказательства нарушения?

**A:** Использовать `triggerDetails` (JSON с order ids, timestamps, IP) и ссылку на внешний сторедж; описать формат и максимальный размер.

---


### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

