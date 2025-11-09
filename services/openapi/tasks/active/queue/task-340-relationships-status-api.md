# Task ID: API-TASK-340
**Тип:** API Generation  
**Приоритет:** высокий  
**Статус:** queued  
**Создано:** 2025-11-08 18:55  
**Создатель:** AI Task Creator Agent  
**Зависимости:** none

---

## 📋 Краткое описание

Сформировать спецификацию `Relationships Status API`, описывающую состояние отношений, репутацию и доверие между сущностями (игроки, кланы, фракции, города).  
**Целевой файл:** `api/v1/social/relationships/status.yaml`

---

## 🎯 Цель задания

Обеспечить social-service контрактом, который:
- хранит и возвращает уровни отношений (reputation tiers) и доверия (trust levels) между субъектами;
- фиксирует историю взаимодействий и источники изменений (миссии, торговля, арбитраж, события);
- сообщает об эффектах уровня (скидки, доступы, санкции) и визуализационных данных для UI;
- синхронизируется с world-service (влияние на города и фракции), economy-service (цены, налоги) и gameplay-service (боевые/квестовые бонусы).

---

## 📚 Источники информации

### Основной документ

**Репозиторий:** `.BRAIN`  
**Путь:** `.BRAIN/02-gameplay/social/relationships-system-детально.md`  
**Версия:** 1.0.0  
**Дата обновления:** 2025-11-08 10:40  
**Статус документа:** approved (api-readiness: ready)

**Что важно:**
- Разделы 2–8: уровни отношений (Friends, Allies, Pact, Enemies, Nemesis), репутационные шкалы, доверие, союзы и рейтинги.  
- Раздел 7: история взаимодействий и арбитраж.  
- Раздел 10: REST макеты (`GET /social/relationships/{entityId}`, `POST /social/relationships/update`, `GET /social/relationships/history/{entityId}`).  
- Раздел 11: Kafka события (`social.relationships.changed`, `social.relationships.alert`).  
- JSON схемы (relationship-status, relationship-update, relationship-history).

### Дополнительные источники

- `.BRAIN/02-gameplay/social/player-orders-reputation-детально.md` — пересечение по доверительным договорам и рейтингам.  
- `.BRAIN/02-gameplay/social/player-orders-system-детально.md` — общий workflow заказов и эскалации.  
- `.BRAIN/03-lore/visual-guides/visual-style-locations-детально.md` — визуальные индикаторы в городах.  
- `.BRAIN/05-technical/content-generation/city-life-population-algorithm.md` — влияние на доступность NPC и заказов.

---

## 📁 Целевая структура API

**Репозиторий:** `API-SWAGGER`  
**Файл:** `api/v1/social/relationships/status.yaml`  
**Тип:** OpenAPI 3.0.3 (YAML)

**Структура директории:**
```
API-SWAGGER/
└── api/
    └── v1/
        └── social/
            └── relationships/
                ├── components/
                │   ├── schemas/
                │   ├── responses/
                │   └── examples/
                └── status.yaml  ← создать/обновить
```

---

## 🏗️ Целевая архитектура (⚠️ ОБЯЗАТЕЛЬНО)

### Backend:
- **Микросервис:** social-service (port 8084)  
- **Интеграции:** world-service (alliances, города), economy-service (цены/налоги), gameplay-service (боевые бонусы), notification-service (alerts), analytics-service (метрики).
- **Kafka:** `social.relationships.changed`, `social.relationships.alert`, `social.trust.contract.created`.

### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

### Frontend:
- **Модуль:** modules/social/relationships  
- **State Store:** `useSocialStore(relationships)`  
- **UI:** `RelationshipStatusCard`, `ReputationGauge`, `TrustMeter`, `RelationshipHistoryTable`, `AlertToast`  
- **Формы:** `RelationshipFilterForm`, `RelationshipUpdateForm`  
- **Layouts:** `RelationshipsLayout`, `FactionDiplomacyLayout`  
- **Хуки:** `useRelationshipsQuery`, `useRelationshipHistory`, `useRelationshipAlerts`

**Комментарий в YAML:**
```
# Target Architecture:
# - Microservice: social-service (port 8084)
# - Frontend Module: modules/social/relationships
# - State Store: useSocialStore(relationships)
# - UI: RelationshipStatusCard, ReputationGauge, TrustMeter, RelationshipHistoryTable, AlertToast
# - Forms: RelationshipFilterForm, RelationshipUpdateForm
# - Layouts: RelationshipsLayout, FactionDiplomacyLayout
# - Hooks: useRelationshipsQuery, useRelationshipHistory, useRelationshipAlerts
# - Events: social.relationships.changed, social.relationships.alert, social.trust.contract.created
# - API Base: /api/v1/social/relationships/*
```

---

## ✅ Детальный план

1. **Сбор требований:** подтвердить поля для статуса (reputation, trust, category, effects), историю, источники, alerts.  
2. **Схемы:** `RelationshipStatus`, `RelationshipEffect`, `TrustState`, `RelationshipUpdateRequest`, `RelationshipHistoryEntry`, `RelationshipAlert`.  
3. **Эндпоинты:**  
   - получение статуса по entityId (с поддержкой aggregated и target filters);  
   - обновление (batch, transnational);  
   - история событий;  
   - фильтры (по типу связи, фракции, региону);  
   - alerts/подписки (опционально).  
4. **Интеграция с другими сервисами:** вернуть ссылки на world events, economy modifiers.  
5. **Документировать Kafka события и влияние на UI/экономику.**  
6. **Подключить shared security/responses/pagination, описать ошибки.**  
7. **Подготовить примеры:** союз игроков, конфликт кланов, доверительная связь, история событий, alert при падении репутации.  
8. **Прогнать `scripts/validate-swagger.ps1`, убедиться в лимите строк (компоненты вынести).**

---

## 🔌 Эндпоинты

1. **GET `/social/relationships/{entityId}`** — возвращает статус отношений и доверия с другими сущностями.  
2. **GET `/social/relationships/{entityId}/history`** — история взаимодействий (пагинация, фильтры).  
3. **POST `/social/relationships/update`** — batch изменение (increase/decrease trust/reputation, reasons).  
4. **GET `/social/relationships/summary`** — агрегаты по сегментам (friendly, hostile и т.д.).  
5. **GET `/social/relationships/alerts`** — активные предупреждения (опционально).  
6. **POST `/social/relationships/alerts/ack`** — подтверждение/закрытие alert.

---

## 🧱 Модели данных

- **RelationshipStatus** — `entityId`, `targetId`, `relationshipTier`, `trustLevel`, `effects[]`, `lastInteraction`, `faction`, `city`, `notes`.  
- **TrustState** — текущая шкала доверия, прогресс, decay timers.  
- **RelationshipEffect** — бонусы/штрафы (скидки, доступы, санкции).  
- **RelationshipUpdateRequest** — список изменений (delta, reason, source, metadata).  
- **RelationshipHistoryEntry** — событие (тип, delta, source, timestamp, context).  
- **RelationshipSummary** — агрегаты (counts per tier).  
- **RelationshipAlert** — `alertId`, `severity`, `message`, `involvedParties`, `createdAt`, `acknowledged`.  
- **PaginatedRelationshipHistory** — стандартная пагинация (`shared/common/pagination`).

---

## 📏 Принципы и правила

- OpenAPI 3.0.3; файл ≤400 строк (схемы/примеры вынести).  
- Использовать `shared/common/security.yaml`, `shared/common/responses.yaml`, `shared/common/pagination.yaml`.  
- Ошибки с `x-error-code`: `VAL_INVALID_RELATIONSHIP_REQUEST`, `BIZ_RELATIONSHIP_NOT_FOUND`, `BIZ_RELATIONSHIP_UPDATE_CONFLICT`, `INT_RELATIONSHIP_PIPELINE_FAILURE`.  
- В `info.description` перечислить `.BRAIN` источники, дату готовности, UX подтверждения.  
- Добавить `tags`: `Relationships`, `Trust`, `History`, `Alerts`.

---

## ✅ Критерии приемки

1. Файл `api/v1/social/relationships/status.yaml` создан и проходит `scripts/validate-swagger.ps1`.  
2. В начале присутствует `Target Architecture` блок.  
3. Реализованы endpoints для статуса, истории, обновлений и summary.  
4. Описаны схемы `RelationshipStatus`, `TrustState`, `RelationshipEffect`, `RelationshipUpdateRequest`, `RelationshipHistoryEntry`, `RelationshipAlert`.  
5. Подключены общие компоненты безопасности, ошибок, пагинации.  
6. Задокументированы связанные Kafka события и интеграции.  
7. Добавлены примеры (ally, nemesis, trust drop, alert).  
8. README в `social/relationships` обновлён (в рамках реализации).  
9. Task отражён в `brain-mapping.yaml`.  
10. Указаны зависимости от других API (ratings, contracts, world alliances).

---

## ❓ FAQ

**Q:** Как учитывать города/фракции?  
A: Включить поля `factionId`, `cityId`, `regionId`; указать связи с world-service и `alliances/events`.  

**Q:** Нужно ли хранить историю полностью?  
A: API возвращает с пагинацией; архив может храниться в отдельном хранилище, но поля `context` и `metadata` предусмотрены.  

**Q:** Как обрабатывать массовые изменения (эвенты)?  
A: Через `POST /social/relationships/update` (batch) и события `relationships.changed`.  

**Q:** Требуются ли realtime уведомления?  
A: Alerts публикуются в Kafka; realtime-канал реализуется notification-service (вне scope).  

---

**Следующие шаги исполнителя:** реализовать OpenAPI-файл, вынести схемы/примеры, обновить README, прогнать валидацию и линтеры.

