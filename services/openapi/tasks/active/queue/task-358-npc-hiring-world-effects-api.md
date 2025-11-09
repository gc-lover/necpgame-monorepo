# Task ID: API-TASK-358
**Тип:** API Generation  
**Приоритет:** высокий  
**Статус:** queued  
**Создано:** 2025-11-08 20:22  
**Создатель:** AI Task Creator Agent  
**Зависимости:** none

---

## 📋 Краткое описание

Подготовить спецификацию `NPC Hiring World Effects API`, агрегирующую влияние найма NPC на мир: индексы занятости, кризисы, миграцию и события.  
**Целевой файл:** `api/v1/world/player-orders/effects.yaml`

---

## 🎯 Цель задания

Обеспечить world-service API, которое:
- рассчитывает и публикует показатели (`EmploymentStabilityIndex`, `LaborDemandIndex`, `StrikeRiskScore`, `NPCMigrationFlow`);  
- инициирует world-events (забастовки, похищения, кризисы) и синхронизируется с social-service и economy-service;  
- предоставляет дашборды для модулей `modules/world/insights` и `modules/social/npc-hiring`, включая карту занятости и HR-оповещения;  
- интегрируется с telemetry/analytics для мониторинга и прогнозов.

---

## 📚 Источники информации

### Основной документ

**Репозиторий:** `.BRAIN`  
**Путь:** `.BRAIN/02-gameplay/social/npc-hiring-world-impact-детально.md`  
**Версия:** 1.0.0  
**Дата обновления:** 2025-11-08 10:12  
**Статус документа:** approved (api-readiness: ready)

**Ключевые разделы:**  
- §1–4: социальная экосистема, экономическое влияние, фракционные эффекты, геймплей.  
- §5: события и кризисы (забастовки, похищения, миграция, чёрный рынок).  
- §6: UX (карта найма, HR-дэшборд, сводки, уведомления).  
- §9: REST макеты (`GET /world/player-orders/effects`, `POST /world/player-orders/effects/recalculate`).  
- §10–11: Kafka события (`world.npc-hiring.impact`, `world.npc-hiring.crisis`) и метрики.

### Дополнительные источники

- `.BRAIN/02-gameplay/social/npc-hiring-system-детально.md` — данные контрактов и workforce.  
- `.BRAIN/02-gameplay/social/relationships-system-детально.md` — влияние репутации и доверия работодателя.  
- `.BRAIN/02-gameplay/social/player-orders-world-impact-детально.md` — совместные эффекты заказов.  
- `.BRAIN/03-lore/_03-lore/visual-guides/visual-style-locations-детально.md` — визуализация городов и бирж.  
- `.BRAIN/05-technical/telemetry/hr-analytics-pipeline.md` — аналитические пайплайны.

---

## 📁 Целевая структура API

**Репозиторий:** `API-SWAGGER`  
**Файл:** `api/v1/world/player-orders/effects.yaml`  
**Тип:** OpenAPI 3.0.3 (YAML)

**Структура:**
```
API-SWAGGER/
└── api/
    └── v1/
        └── world/
            └── npc-hiring/
                ├── components/
                │   ├── schemas/
                │   ├── responses/
                │   └── examples/
                └── effects.yaml
```

---

## 🏗️ Целевая архитектура (⚠️ ОБЯЗАТЕЛЬНО)

### Backend:
- **Микросервис:** world-service (port 8092)  
- **Интеграции:** social-service (alerts, отношения), economy-service (индекс занятости, прогнозы), notification-service (уведомления), analytics-service (дашборды), telemetry-service, quest-service (world-events).  
- **Kafka:** `world.npc-hiring.impact`, `world.npc-hiring.crisis`, `economy.npc-hiring.index`, `social.npc-hiring.alert`.

### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

### Frontend:
- **Модуль:** modules/world/insights  
- **State Store:** `useWorldStore(npcHiringImpact)`  
- **UI:** `NpcHiringMap`, `NpcHiringDashboard`, `NpcHiringCrisisPanel`, `NpcHiringForecastWidget`, `NpcHiringAlertToast`  
- **Формы:** `NpcHiringRecalculateForm`, `NpcHiringCrisisResolutionForm`  
- **Layouts:** `WorldHiringLayout`, `NpcHiringInsightsLayout`  
- **Hooks:** `useNpcHiringImpact`, `useNpcHiringForecast`, `useNpcHiringCrises`, `useNpcHiringAlerts`

**Комментарий в YAML:**
```
# Target Architecture:
# - Microservice: world-service (port 8092)
# - Frontend Module: modules/world/insights
# - State Store: useWorldStore(npcHiringImpact)
# - UI: NpcHiringMap, NpcHiringDashboard, NpcHiringCrisisPanel, NpcHiringForecastWidget, NpcHiringAlertToast
# - Forms: NpcHiringRecalculateForm, NpcHiringCrisisResolutionForm
# - Layouts: WorldHiringLayout, NpcHiringInsightsLayout
# - Hooks: useNpcHiringImpact, useNpcHiringForecast, useNpcHiringCrises, useNpcHiringAlerts
# - Events: world.npc-hiring.impact, world.npc-hiring.crisis, economy.npc-hiring.index, social.npc-hiring.alert
# - API Base: /api/v1/world/player-orders/*
```

---

## ✅ Детальный план

1. **Определить индикаторы:** EmploymentStabilityIndex, LaborDemandIndex, RetentionRate, StrikeRiskScore, NPCMigrationFlow.  
2. **Спроектировать схемы:** `NpcHiringImpact`, `NpcHiringRegionImpact`, `NpcHiringRecalculateRequest`, `NpcHiringCrisis`, `NpcHiringForecast`, `NpcHiringAlert`, `NpcHiringSummary`.  
3. **Эндпоинты:** получение текущих значений, пересчёт, кризисы, прогнозы, алерты, история.  
4. **Интеграции:** ссылки на economy indices, social alerts, npc-hiring workforce/contract data.  
5. **Документировать Kafka события и очереди (`npc-hiring-crisis-response`).**  
6. **Примеры:** рост рынка труда, забастовка, похищение NPC, миграция, прогноз.  
7. **Shared components:** security/responses/pagination, вынести схемы/примеры, соблюдать лимит 400 строк.  
8. **Коды ошибок:** пересчёт в процессе, отсутствующие данные, блокировки кризисов.  
9. **Прописать метрики и их использование в аналитике.**  
10. **Прогнать `scripts/validate-swagger.ps1`, обновить README в `world/player-orders`.**

---

## 🔌 Эндпоинты

1. **GET `/world/player-orders/effects`** — текущие индексы по регионам/фракциям.  
2. **GET `/world/player-orders/effects/{regionId}`** — детализация региона (impact, crises, forecast).  
3. **POST `/world/player-orders/effects/recalculate`** — пересчёт индексов и генерация world-events.  
4. **GET `/world/player-orders/effects/history`** — временные ряды (пагинация).  
5. **GET `/world/player-orders/effects/crises`** — активные кризисы, статусы, действия.  
6. **POST `/world/player-orders/effects/crises/{crisisId}/ack`** — подтверждение обработки кризиса.  
7. **GET `/world/player-orders/effects/forecast`** — прогнозы занятости/спроса.  
8. **GET `/world/player-orders/effects/alerts`** — алерты для UI и уведомлений.  
9. **GET `/world/player-orders/effects/summary`** — агрегаты (по городам, фракциям, секторам).  
10. **GET `/world/player-orders/effects/export`** — экспорт данных (CSV/JSON).

---

## 🧱 Модели данных

- **NpcHiringImpact** — `regionId`, `cityId`, `employmentStabilityIndex`, `laborDemandIndex`, `retentionRate`, `strikeRiskScore`, `npcMigrationFlow`, `economyImpact`, `worldImpact`, `updatedAt`.  
- **NpcHiringRegionImpact** — расширенная модель (factionControl, workforceSize, crises[], forecasts[]).  
- **NpcHiringRecalculateRequest** — параметры пересчёта (regions[], force, horizon).  
- **NpcHiringCrisis** — `crisisId`, `type`, `severity`, `status`, `requiredActions`, `deadline`.  
- **NpcHiringForecast** — прогноз по индикаторам (trend, variance, confidence).  
- **NpcHiringAlert** — `alertId`, `severity`, `message`, `regionId`, `actions`.  
- **NpcHiringImpactHistoryEntry** — временная запись (timestamp, indicators, triggers).  
- **NpcHiringSummary** — агрегированные показатели (topCities, crisisCount, migrationFlow).  
- **PaginatedNpcHiringImpactHistory** — стандартная пагинация.

---

## 📏 Принципы и правила

- OpenAPI 3.0.3; ≤400 строк, вынести схемы/примеры.  
- Использовать `shared/common/security.yaml`, `shared/common/responses.yaml`, `shared/common/pagination.yaml`.  
- Ошибки (`x-error-code`): `VAL_NPC_HIRING_EFFECTS_INVALID`, `BIZ_NPC_HIRING_RECALCULATION_LOCKED`, `BIZ_NPC_HIRING_CRISIS_ACTIVE`, `BIZ_NPC_HIRING_DATA_UNAVAILABLE`, `INT_NPC_HIRING_PIPELINE_FAILURE`.  
- `info.description` — указать `.BRAIN` источники, UX подтверждения, интеграции.  
- Теги: `NPC Hiring`, `World`, `Analytics`, `Crises`, `Forecast`.  
- Указать зависимости на `npc-hiring/workforce.yaml`, `npc-hiring/payroll.yaml`, `economy/npc-hiring/index.yaml`, `social/npc-hiring/alerts.yaml`.

---

## ✅ Критерии приемки

1. Файл `api/v1/world/player-orders/effects.yaml` создан/обновлён и проходит `scripts/validate-swagger.ps1`.  
2. В начале файла присутствует `Target Architecture` блок.  
3. Реализованы все указанные эндпоинты, схемы и примеры.  
4. Подключены shared security/responses/pagination.  
5. Документированы Kafka события и очередь кризисов.  
6. README в `world/player-orders` обновлён (в рамках реализации).  
7. Task отражён в `brain-mapping.yaml`.  
8. `.BRAIN` документ обновлён (API Tasks Status).  
9. Указаны зависимости на social/economy сервисы, mentorship, player orders.  
10. Обозначены метрики (`EmploymentStabilityIndex`, `LaborDemandIndex`, `StrikeRiskScore`, `NPCMigrationFlow`).  
11. Примеры включают кризис, пересчёт, прогноз и экспорт.

---

## ❓ FAQ

**Q:** Как часто запускать пересчёт?  
A: По расписанию (каждый час/день) и вручную; API должно проверять `cooldown`, иначе `BIZ_NPC_HIRING_RECALCULATION_LOCKED`.  

**Q:** Как обрабатывать отсутствие данных?  
A: Возвращать `BIZ_NPC_HIRING_DATA_UNAVAILABLE`; предусмотреть fallback с последним успешным snapshot.  

**Q:** Требуется ли подписка для фронтенда?  
A: Да, описать SSE/WebSocket/Webhook вне scope, но добавить ссылку в документации.  

**Q:** Можно ли связывать с конкретными NPC?  
A: Да, включить `keyNpcIds[]` и ссылки на workforce/relationships для глубокого анализа.  

---

**Следующие шаги исполнителя:** реализовать OpenAPI-файл, вынести компоненты, описать интеграции, подготовить примеры и прогнать проверки.



