# Task ID: API-TASK-257
**Тип:** API Generation
**Приоритет:** высокий (Post-MVP)
**Статус:** queued
**Создано:** 2025-11-07 23:05
**Создатель:** AI Agent (GPT-5 Codex)
**Зависимости:** API-TASK-122 (stock-exchange core API), API-TASK-178 (economy-events API)

---

## 📋 Краткое описание

Нужно описать API для прокидывания влияния событий (квесты, войны, скандалы) на котировки корпораций. Контракт должен покрыть расчёт импактов, историю изменений, симуляции what-if и административное управление маппингами «событие → корпорация».

**Что нужно сделать:** Создать OpenAPI спецификацию `stock-exchange-events.yaml`, описывающую ingest событий, расчёт modifiers, хранение истории и выдачу аннотаций для графиков и аналитики.

---

## 🎯 Цель задания

Предоставить API, которое позволит:
- Получать активные воздействия на рынок с учётом базового процента и modifiers
- Просматривать историю событий, их продолжительность и восстановление цен
- Управлять маппингом типов событий к тикерам, задавать формулы и весовые коэффициенты
- Запускать симуляции перед публикацией крупных ивентов (корпоративные войны, скандалы)

**Зачем это нужно:** сделать экономику реактивной к игровым событиям, обеспечить прозрачность инвесторам и дать администраторам инструменты контроля и отката.

---

## 📚 Источники информации

### Основной источник концепции

**Репозиторий:** `.BRAIN`
**Путь к документу:** `.BRAIN/02-gameplay/economy/stock-exchange/stock-events.md`
**Версия документа:** v1.1.0
**Дата последнего обновления:** 2025-11-07
**Статус документа:** approved

**Ключевые моменты:**
- Типы событий: quests, faction wars, territory control, scandals, breakthroughs, macro events
- Формулы изменения цены с modifiers (`sector_alignment`, `player_actions`, `size_modifier`)
- Таблицы длительностей (immediate, short-term, long-term, permanent)
- SQL структуры `stock_event_impacts` и `stock_event_modifiers`
- Пайплайн (trigger → mapping → modifiers → pricing-engine → decay → analytics)
- Endpoints `/stocks/events/impacts`, `/history`, `/admin/events/mappings`, `/admin/events/simulate`
- Метрики и алерты: `ImpactLatency`, `MaxDrawdown`, `EventAppliedCount`

### Дополнительные источники
- `.BRAIN/02-gameplay/economy/stock-exchange/stock-analytics.md` — отображение аннотаций и heatmap
- `.BRAIN/04-narrative/quests/` (указанные в таблицах) — источники событий
- `.BRAIN/05-technical/backend/realtime-server/part2-protocol-optimization.md` — требования к realtime потокам
- `API-SWAGGER/api/v1/gameplay/economy/economy-events.yaml` — контракты генератора событий
- `API-SWAGGER/api/v1/gameplay/economy/stock-exchange-core.yaml` — ссылки на существующие endpoints для котировок

### Связанные документы
- `.BRAIN/02-gameplay/world/events/live-events-system.md` — глобальные события и расписания
- `.BRAIN/02-gameplay/world/raids/specter-surge-loot.md` — рейды, влияющие на экономику
- `.BRAIN/05-technical/backend/leaderboard/leaderboard-core.md` — используется в modifiers (соревнования)

---

## 📁 Целевая структура API

**Целевой файл:** `api/v1/gameplay/economy/stock-exchange-events.yaml`

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
                └── stock-exchange-events.yaml  ← создать
```

Если файл уже был создан, обновить до версии 1.1.0 с учётом новых типов событий и modifiers.

---

## 🏗️ Целевая архитектура (⚠️ ОБЯЗАТЕЛЬНО)

### Backend
- **Микросервис:** economy-service
- **Порт:** 8085
- **API base:** `/api/v1/gameplay/economy/stocks/events/*`
- **Сервисы-зависимости:**
  - `economy-events` (источник глобальных событий)
  - `quest-service` (исходы квестов)
  - `world-service` (территории, войны)
  - `analytics-service` (аннотации на графиках)
  - `notification-service` (alert инвесторов)

### Frontend
- **Модуль:** `modules/economy/stocks`
- **Feature:** `modules/economy/stocks/events`
- **State Store:** `useEconomyStore` (`activeImpacts`, `eventHistory`, `whatIfResults`)
- **UI компоненты (@shared/ui):** `EventImpactTimeline`, `MarketImpulseCard`, `Heatmap`, `ScenarioSimulator`
- **Формы (@shared/forms):** `EventMappingForm`, `WhatIfScenarioForm`
- **Layouts:** `@shared/layouts/GameLayout`
- **Hooks:** `@shared/hooks/useRealtime`, `@shared/hooks/useScenarioRunner`

### API Gateway маршрут
```yaml
- id: economy-service
  uri: lb://ECONOMY-SERVICE
  predicates:
    - Path=/api/v1/gameplay/economy/stocks/events/**
```

### Event streaming
- Kafka topics: `economy.stock_events.impact_created`, `.impact_updated`, `.impact_expired`, `.impact_reversed`
- WS stream: `/ws/economy/stocks/events`

---

## 🧩 План выполнения

1. Разделить API на публичные (`/impacts`, `/history`) и админские (`/admin/events/*`).
2. Описать параметры фильтрации (eventType, ticker, severity, timeframe) и пагинацию.
3. Спроектировать payload modifiers: sector, playerCount, territory, questOutcome.
4. Добавить endpoints для управления маппингами и коэффициентами (`baseImpact`, `durationHours`, `decayCurve`).
5. Реализовать контракт симуляции `POST /admin/events/simulate` с входными данными (eventType, baseImpact, modifiers) и ожидаемым результатом.
6. Задокументировать Service Communication с `economy-events` и `quest-service`, включая idempotency/референсы.
7. Прописать модели данных и соответствие SQL таблицам (`stock_event_impacts`, `stock_event_modifiers`).
8. Добавить описание метрик и алертов для observability.
9. Пройти чеклист: комментарий `Target Architecture`, ссылки на shared-компоненты, длина файла ≤400 строк.

---

## 🧪 API Endpoints

1. **GET `/api/v1/gameplay/economy/stocks/events/impacts`**
   - Фильтры: `ticker`, `eventType`, `status`, `severity`, `from`, `to`
   - Ответ: активные импакты, значение процента, стадия (IMMEDIATE/SHORT_TERM/LONG_TERM), остаток длительности

2. **GET `/api/v1/gameplay/economy/stocks/events/history`**
   - Параметры: `ticker`, `eventType`, `page`, `size`
   - Ответ: события с датами начала/окончания, фактическим impact curve, recovery status

3. **GET `/api/v1/gameplay/economy/stocks/events/{impactId}`**
   - Детальный просмотр modifiers и участия игроков

4. **POST `/api/v1/gameplay/economy/stocks/admin/events/mappings`**
   - Создание или обновление правила: eventType, corpTickers, baseImpact, modifiers, duration, decayCurve
   - Валидация конфликтов, возврат auditId

5. **PATCH `/api/v1/gameplay/economy/stocks/admin/events/mappings/{mappingId}`**
   - Изменение коэффициентов, деактивация, перевод в `ARCHIVED`

6. **POST `/api/v1/gameplay/economy/stocks/admin/events/simulate`**
   - Тело: eventType, corpTickers, baseImpact, modifiers[], playerCount, territory, severity
   - Ответ: прогноз цен (timeline), heatmap по секторам, предупреждения compliance

7. **POST `/api/v1/gameplay/economy/stocks/events/ingest`** (internal, защищённый)
   - Получение события из `economy-events`/`quest-service`
   - Идемпотентность по `eventInstanceId`

8. **DELETE `/api/v1/gameplay/economy/stocks/admin/events/mappings/{mappingId}`**
   - Удаление правила, если нет активных импактов

9. **GET `/api/v1/gameplay/economy/stocks/events/statistics`**
   - Агрегаты: drawdownBySector, topPositiveEvents, pendingExpirations

10. **WebSocket `/ws/economy/stocks/events`**
    - Транслирует `impact_created`, `impact_updated`, `impact_expired`, `impact_reversed`

---

## 🗄️ Схемы данных

- **EventImpact** — impactId, eventType, severity, ticker, baseImpactPercent, modifiers[], durationHours, decayCurve, status, appliedAt, expiresAt
- **ImpactModifier** — modifierType (`SECTOR`, `PLAYER_COUNT`, `TERRITORY`, `QUEST_OUTCOME`, `FACTION_SCORE`), value, multiplier
- **EventHistoryEntry** — impactId, eventInstanceId, startedAt, endedAt, actualImpactPercent, recoveryCurve
- **EventMapping** — mappingId, eventType, corpTickers[], baseImpactRange, defaultDuration, decayCurve, createdBy, updatedAt
- **SimulationResult** — predictedTimeline[], expectedDrawdown, warnings[]

Сопоставить с таблицами `stock_event_impacts` и `stock_event_modifiers`, добавить требования к индексам (`impact_id`, `event_type`, `ticker`).

---

## 🔄 Интеграции и события

- **Ingress:** `economy.events.*`, `quests.outcomes.*`, `world.wars.*`
- **Egress:** `economy.stock_events.*` (описать payload схемы)
- **Feign:**
  - `quest-service` → `GET /quests/{questId}/summary`
  - `world-service` → `GET /territories/{id}` (контекст территории)
  - `analytics-service` → `POST /analytics/events/annotate`
- **Notifications:** пуш инвесторам (`/notifications/broadcast`) с шаблоном и severity

---

## 🗃️ База данных

- `stock_event_impacts` — поля из документа + индексы на `corporation_id`, `event_type`, `status`
- `stock_event_modifiers` — JSONB для значений, PK `(impact_id, modifier_type)`
- Дополнительно: `stock_event_simulations` (results cache) — хранить what-if сценарии на 24 часа

---

## 📊 Мониторинг

- Метрики: `event_impact_latency_ms`, `event_impact_queue_depth`, `impact_decay_lag`, `mapping_conflict_total`
- Алерты: задержка применения >30 сек, drawdown > ожидаемого на 10%, количество отказов симуляции
- Audit trail: логировать админские изменения (mapping create/update/delete), хранить userId, diff

---

## ✅ Критерии приемки

1. OpenAPI файл проходит валидацию и укладывается в лимит 400 строк.
2. Все endpoints используют единый префикс `/api/v1/gameplay/economy/stocks/events`.
3. В шапке файла указан блок `Target Architecture` с микросервисом и фронтендом.
4. Импакты поддерживают фильтрацию по типу события, тикеру, диапазону дат и статусу.
5. Симуляция возвращает прогноз по времени (array of timestamp+impact) и список предупреждений.
6. Админские операции возвращают auditId и включают 403/409 ошибки.
7. Идемпотентность ingest описана через `Idempotency-Key`/`eventInstanceId`.
8. Описаны все Kafka события с полями из документа, включая decay и reversals.
9. Метрики observability перечислены и связаны с конкретными действиями.
10. FAQ покрывает edge cases (дубли событий, откат, влияние нескольких событий).

---

## ❓ FAQ

**Q:** Что делать, если одно событие влияет на несколько корпораций одновременно?

**A:** Использовать один impact с массивом `affectedCompanies` либо создать отдельные impact записи; опиши стратегию и упомяни, что mapping может содержать несколько тикеров с разными коэффициентами.

**Q:** Как обрабатывать дублирующиеся события от разных источников?

**A:** Требовать `eventInstanceId` и хранить идемпотентность; при повторе возвращать `409 Conflict` с ссылкой на существующий impact.

**Q:** Можно ли вручную ослабить эффект события?

**A:** Да, через `PATCH /admin/events/mappings/{id}` с обновлённым multiplier или `impact_override`; задокументируй поле и audit требования.

**Q:** Как отображать события на графиках?

**A:** Передавать их через `analytics-service` и WebSocket; добавить описание payload с `annotationType`, `label`, `severity`.

**Q:** Что делать, если событие затянулось и не истекает?

**A:** Предусмотреть ручной `impact_reversed` (PATCH/DELETE), а также job, который проверяет `expiresAt`; документируй этот сценарий.

---


### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

