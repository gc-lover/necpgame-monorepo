# Task ID: API-TASK-300
**Тип:** API Generation  
**Приоритет:** критический  
**Статус:** queued  
**Создано:** 2025-11-07 23:40  
**Создатель:** AI Agent (GPT-5 Codex)  
**Зависимости:** [API-TASK-241], [API-TASK-247], [API-TASK-258], [API-TASK-299]

---

## 📋 Краткое описание

Спроектировать спецификацию `world-service` + сопряжённые `gameplay-service` эндпоинты для гибридной системы «Living World + Action XP» (Kenshi-inspired). API должно описывать управление фракционным контролем, логистическими маршрутами, хроникой мира и начислением Action XP/усталости.

**Что нужно сделать:** На основе `.BRAIN/02-gameplay/world/world-state/living-world-kenshi-hybrid.md` создать OpenAPI/AsyncAPI файл `api/v1/world/world-state/living-world.yaml`, включая REST для world-state/chronicle/логистики и progression endpoints для action XP/fatigue, а также события (Kafka/WebSocket) для хронологии, маршрутов и XP.

---

## 🎯 Цель задания

Сформировать контракт живого мира, реагирующего на действия игроков и автономных отрядов, и обеспечить гибридную прокачку «использование + ранги» с контролем усталости.

**Зачем это нужно:**
- Синхронизировать фракционные войны, логистику и мировые события между сервисами.
- Дать фронтенду доступ к хронике, состоянию баз и динамике маршрутов.
- Поддержать Action XP, soft cap/усталость и административные инструменты для мониторинга перегрузок.

---

## 📚 Источники информации

### Основной документ

- `.BRAIN/02-gameplay/world/world-state/living-world-kenshi-hybrid.md` (v0.2.0, review, `api-readiness: ready`)

**Ключевые разделы:**  
Слои симуляции (фракционный контроль, базы, логистика), модель Action XP + fatigue, структуры данных (`FactionControl`, `ActionXpRecord`), REST/Events/GQL контуры, UX-потоки и метрики.

### Дополнительные документы

- `.BRAIN/02-gameplay/world/world-state/player-impact-systems.md` – глобальные индексы мира.  
- `.BRAIN/02-gameplay/economy/economy-world-impact.md` – экономические коэффициенты регионов.  
- `.BRAIN/02-gameplay/progression/progression-skills.md` и `progression-skills-mapping.md` – таблицы навыков и рангов.  
- `.BRAIN/02-gameplay/world/world-state/player-impact-persistence.md` – хранение состояния мира.  
- `.BRAIN/02-gameplay/combat/combat-loadouts-system.md` – взаимодействие с Action XP и подготовкой к контенту.  
- `.BRAIN/02-gameplay/world/events/world-events-framework.md` – хуки событий в хронологию мира.

### Связанные задания

- `API-TASK-241` — World Interaction Suite (UI события мира).  
- `API-TASK-247` — Loot Hunt System (логистика и рейды).  
- `API-TASK-258` — Stock Exchange Analytics (экономические метрики).  
- `API-TASK-299` — Combat Loadouts API (Action XP и подготовка билдов).

---

## 📁 Целевая структура API

**Репозиторий:** `API-SWAGGER`  
**Целевые файлы:**

```
api/v1/world/world-state/
├── living-world.yaml             ← основной файл (paths + события)
├── schemas/
│   └── living-world-components.yaml   ← вынести крупные модели
└── events/
    └── living-world-events.yaml      ← при необходимости вынести Async события
```

- Основной файл ≤400 строк; при превышении вынести схемы/события в подпапки.
- Использовать ссылки на существующие `shared/common` компоненты (responses, pagination, security).

---

## 🏗️ Целевая архитектура (⚠️ ОБЯЗАТЕЛЬНО)

### Backend
- **Микросервисы:**  
  - `world-service` (8086) — фракции, базы, логистика, хроника.  
  - `gameplay-service` (8083) — Action XP, fatigue, ранги.  
- **Интеграции:** economy-service (региональные множители), social-service (гильдийные события), notification-service (подписки на хронику), analytics-service (метрики усталости), realtime-service (спавн отрядов), quest-service (хуки событий).

### Frontend
- **Модули:** `modules/world/state`, `modules/progression/action-xp`.  
- **State Stores:** `useLivingWorldStore`, `useActionXpStore`.  
- **UI компоненты:** `WorldChronicleFeed`, `FactionControlMap`, `LogisticsRouteBoard`, `BaseManagementPanel`, `ActionXpMeter`, `FatigueWarningBanner`.  
- **Формы:** `ControlShiftForm`, `LogisticsRouteForm`, `BaseUpgradeForm`, `ActionXpRestForm`.  
- **Хуки:** `useWorldChronicle`, `useFactionControl`, `useLogisticsRoutes`, `useActionXpSummary`, `useFatigueAlerts`.

### YAML комментарий

```yaml
# Target Architecture:
# - Microservices: world-service (port 8086), gameplay-service (port 8083)
# - API Base: /api/v1/world/world-state/*, /api/v1/progression/action-xp/*
# - Dependencies: economy, social, notification, analytics, realtime, quest
# - Frontend Modules: modules/world/state, modules/progression/action-xp
# - UI: WorldChronicleFeed, FactionControlMap, LogisticsRouteBoard, BaseManagementPanel, ActionXpMeter, FatigueWarningBanner
# - Forms: ControlShiftForm, LogisticsRouteForm, BaseUpgradeForm, ActionXpRestForm
# - Hooks: useWorldChronicle, useFactionControl, useLogisticsRoutes, useActionXpSummary, useFatigueAlerts
```

---

## ✅ Что нужно сделать (детальный план)

1. **Фракционный контроль и базы**  
   - Запроектировать REST endpoints для чтения/изменения `FactionControl`, `Settlement`, `AutonomousSquad`, `LogisticsRoute`.  
   - Описать фильтры, сортировку, безопасные мутации (пороговые проверки, контроль триггеров).  
2. **Хроника мира и логистика**  
   - Определить `GET /world/chronicle` с курсорами, фильтрами по типу событий и регионам.  
   - Прописать создание/обновление маршрутов, статусы (`active`, `under_attack`, `completed`).  
3. **Action XP & Fatigue**  
   - REST для батчевого начисления XP, чтения сводок, сброса усталости, интеграции с экономическими предметами.  
   - Валидация soft cap, fatigue modifiers, ограничения по контекстам (safe zone vs combat).  
4. **Async события**  
   - Kafka/WebSocket payload для `world.faction.controlShifted`, `world.logistics.routeCreated`, `world.squad.spawned`, `gameplay.actionXp.gained`, `gameplay.actionXp.softCapReached`.  
   - Указать ключи партиционирования, критичность, ретри.  
5. **Нотификации и подписки**  
   - Описать подписку игроков/гильдий на хронику и маршруты (webhook/topic), интеграцию с notification-service.  
6. **Метрики и SLA**  
   - Добавить схемы/описания для `controlShiftRate`, `fatigueOverflow`, `routeSurvivalRate`.  
   - Отразить, как они используются в analytics-service (refs на `API-TASK-258`).  
7. **Безопасность и аудит**  
   - Включить `BearerAuth`, scopes (`living-world:read`, `living-world:manage`, `action-xp:write`, `chronicle:subscribe`).  
   - Для мутаций — заголовки `Idempotency-Key`, `X-Audit-Id`.  
8. **Примеры**  
   - Привести примеры: смена контроля, создание маршрута, начисление Action XP, soft cap предупреждение, событие хроники.  
9. **Структура файлов**  
   - При необходимости вынести крупные схемы (FactionControl, Settlement, ActionXpRecord) в компоненты, а события — в отдельный файл.  
10. **Документация**  
   - Добавить checklist, FAQ, инструкции по обновлению `living-world-kenshi-hybrid.md` и `brain-mapping.yaml` после реализации.

---

## 🔀 Требуемые эндпоинты (минимум)

### World-service
1. `GET /api/v1/world/factions/{factionId}/control`  
2. `POST /api/v1/world/factions/control-shift`  
3. `GET /api/v1/world/settlements` / `PATCH /api/v1/world/settlements/{id}` (upgrade/status)  
4. `GET /api/v1/world/logistics/routes`  
5. `POST /api/v1/world/logistics/routes` / `PATCH /{routeId}` (status updates)  
6. `GET /api/v1/world/chronicle` (cursor-based feed)  
7. `POST /api/v1/world/chronicle/subscriptions` (подписка на события)  
8. `POST /api/v1/world/squads` (создание/регистрация автономного отряда)  

### Gameplay-service (Action XP)
9. `POST /api/v1/progression/action-xp` (batch)  
10. `GET /api/v1/progression/action-xp/summary`  
11. `POST /api/v1/progression/fatigue/reset`  
12. `GET /api/v1/progression/action-xp/metrics` (агрегаты для аналитики)

### Integration / Ops
13. `GET /api/v1/world/living-world/metrics` (controlShiftRate, fatigueOverflow, routeSurvivalRate)  
14. `POST /api/v1/world/living-world/maintenance/pause` (при необходимости поставить симуляцию на паузу)  
15. `POST /api/v1/world/living-world/maintenance/resume`

Использовать стандартные ответы и пагинацию через `shared/common/`.

---

## 🧱 Модели данных

- `FactionControl`, `Settlement`, `LogisticsRoute`, `AutonomousSquad`, `ChronicleEvent`.  
- `ActionXpRecord`, `ActionXpBatchRequest`, `ActionXpSummary`, `SkillFatigue`.  
- `LivingWorldMetricSnapshot`.  
- `ChronicleSubscription`, `ControlShiftRequest`, `RouteCreateRequest`, `RouteStatusUpdate`.  
- Enums: `ControlTrigger`, `RouteType`, `RouteStatus`, `SquadMission`, `ChronicleEventType`.  
- Async payloads для событий (как в doc).  
- Вынести крупные модели в `schemas/living-world-components.yaml`.

---

## 🧭 Принципы и правила

- Поддерживать систему soft caps и усталости (FatigueModifier, daily limits).  
- Обеспечить идемпотентность мутаций (контроль, создание маршрута, начисление XP).  
- Добавить ссылки на связанные спецификации (world interaction, loadouts, economy/world impact).  
- Включить SLA: не более 3 смен контроля/регион/неделя, указывать задержки в публикации хроники < 5 сек.  
- Переработка маршрутов должна синхронизироваться с notification-service (webhook topics).  
- Файл ≤400 строк; при необходимости вынести схемы/события.  
- Использовать `x-living-world`/`x-action-xp` расширения для пользовательских метрик (если нужно).  
- Прописать проверки прав (GM/admin) для паузы симуляции и ручных изменений.

---

## ✅ Критерии приемки

1. REST эндпоинты (минимум 15) задокументированы с параметрами, телами, кодами ответов и примерами.  
2. Схемы вынесены и переиспользуются через `$ref`.  
3. Async события описаны с примерами и ссылками на каналы (`world.*`, `gameplay.actionXp.*`).  
4. Обозначены требования по безопасности (`BearerAuth`, scopes).  
5. Для мутаций описаны `Idempotency-Key`, `X-Audit-Id`, отказоустойчивость.  
6. Указаны SLA/метрики (controlShiftRate, fatigueOverflow, routeSurvivalRate).  
7. Присутствует `x-target-architecture` комментарий и ссылки на документы `.BRAIN`.  
8. Добавлены сценарии подписок/уведомлений и правила публикации в notification-service.  
9. Примеры покрывают ключевые сценарии (смена контроля, создание маршрута, начисление Action XP, soft cap).  
10. Checklist/FAQ заполнены; указано, как обновить mapping и `.BRAIN`.  
11. Файл(ы) проходят линтер и укладываются в лимит строк.  
12. План действий после реализации (обновить `living-world-kenshi-hybrid.md`, `brain-mapping`, `implementation-tracker`).

---

## 📎 Checklist перед сдачей

- [ ] Проанализированы все разделы документа и связанные источники.  
- [ ] Спецификация мировых эндпоинтов создана и валидируется.  
- [ ] Action XP и fatigue покрыты REST + событиями.  
- [ ] Поддержаны подписки/уведомления и интеграции.  
- [ ] Примеры и ошибки (400/401/403/404/409/422/500) оформлены через `shared/common`.  
- [ ] Обновлены checklist/FAQ + приёмочные критерии.  
- [ ] Указаны шаги по обновлению `.BRAIN` и mapping после реализации.

---

## ❓ FAQ

**Q:** Можно ли запускать `control-shift` вручную GM?  
**A:** Да, через защищённый endpoint с проверкой триггера и audit. Указать scope `living-world:manage` и обязать логировать причину.

**Q:** Как ограничить начисление Action XP ботами?  
**A:** В спецификации прописать `fatigueOverflow` метрику и события `actionXp.softCapReached`, чтобы analytics-service мог ставить алерты и блокировки.

**Q:** Что делать при паузе симуляции?  
**A:** Использовать maintenance endpoints `pause/resume`, отключая генерацию отрядов и начисление XP до завершения обслуживания.

---

## 🔗 Связность и дальнейшие шаги

- После реализации обновить `brain-mapping.yaml`, `.BRAIN/02-gameplay/world/world-state/living-world-kenshi-hybrid.md` (Status → completed), добавить запись в `implementation-tracker.yaml`.  
- Координировать с командами world interaction (task 241), loot hunt (task 247), economy analytics (task 258) и combat loadouts (task 299).  
- Подготовить последующие задачи для UI/analytics (фронт, dashboards) после утверждения API.



### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

