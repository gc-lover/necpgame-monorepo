# Task ID: API-TASK-350
**Тип:** API Generation  
**Приоритет:** высокий  
**Статус:** queued  
**Создано:** 2025-11-08 19:40  
**Создатель:** AI Task Creator Agent  
**Зависимости:** API-TASK-349 (общие контракты)

---

## 📋 Краткое описание

Подготовить спецификацию `NPC Workforce Management API`, обеспечивающую управление нанятыми NPC, их задачами, расписаниями и KPI.  
**Целевой файл:** `api/v1/social/npc-hiring/workforce.yaml`

---

## 🎯 Цель задания

Предоставить social-service API, которое:
- формирует группы NPC (команды, смены, отряды), распределяет задачи и ресурсы;  
- отслеживает производительность, настроение, лояльность, рост навыков и автоматизацию;  
- поддерживает планирование расписаний, смен, ротаций и сценариев автоматизации;  
- интегрируется с npc-service (performance updates), world-service (локации), gameplay-service (миссии), economy-service (расходы) и notification-service (alerts).

---

## 📚 Источники информации

### Основной документ

**Репозиторий:** `.BRAIN`  
**Путь:** `.BRAIN/02-gameplay/social/npc-hiring-system-детально.md`  
**Версия:** 1.0.0  
**Дата обновления:** 2025-11-08 10:27  
**Статус документа:** approved (api-readiness: ready)

**Ключевые разделы:**  
- §4–8: панели управления, задачи, команды, автоматизация, развитие NPC.  
- §9–11: влияние на мир, интерфейсы, UX требования.  
- §12–13: REST макеты (workforce hire, dashboard) и Kafka (`npc.hiring.performance.changed`).  
- §14: метрики (NpcPerformanceIndex, LoyaltyTrend, HiringLeadTime).

### Дополнительные источники

- `.BRAIN/02-gameplay/social/npc-hiring-effectiveness.md` — KPI, прогресс, рост навыков.  
- `.BRAIN/02-gameplay/social/npc-hiring-limits.md` — лимиты, инфраструктура, лицензии.  
- `.BRAIN/02-gameplay/social/npc-relationships-system-детально.md` — эмоции/лояльность.  
- `.BRAIN/03-lore/_03-lore/visual-guides/visual-style-locations-детально.md` — интерфейсы и визуализация.  
- `.BRAIN/05-technical/automation/npc-task-scheduler.md` — автоматизация и расписания.

---

## 📁 Целевая структура API

**Репозиторий:** `API-SWAGGER`  
**Файл:** `api/v1/social/npc-hiring/workforce.yaml`  
**Тип:** OpenAPI 3.0.3 (YAML)

**Структура директории:**
```
API-SWAGGER/
└── api/
    └── v1/
        └── social/
            └── npc-hiring/
                ├── components/
                │   ├── schemas/
                │   ├── responses/
                │   └── examples/
                └── workforce.yaml
```

---

## 🏗️ Целевая архитектура (⚠️ ОБЯЗАТЕЛЬНО)

### Backend:
- **Микросервис:** social-service (port 8084)  
- **Интеграции:** npc-service (performance, level-ups), world-service (локации и миссии), gameplay-service (боевые/экономические задания), economy-service (расходы/ROI), notification-service (alerts), analytics-service (dashboards).  
- **Kafka:** `npc.hiring.performance.changed`, `social.npc-hiring.alert`, `social.npc-hiring.schedule.updated`, `world.event.generated`, `economy.npc-hiring.payroll.processed`.

### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

### Frontend:
- **Модуль:** modules/social/npc-hiring  
- **State Store:** `useSocialStore(npcWorkforce)`  
- **UI:** `NpcWorkforceBoard`, `NpcSchedulePlanner`, `NpcTaskAutomation`, `NpcMoodMonitor`, `NpcPerformanceCharts`  
- **Формы:** `NpcWorkforceCreateForm`, `NpcTaskAssignmentForm`, `NpcScheduleForm`, `NpcWorkforceAlertResponseForm`  
- **Layouts:** `NpcWorkforceLayout`, `NpcAutomationLayout`  
- **Hooks:** `useNpcWorkforce`, `useNpcWorkforceMember`, `useNpcSchedules`, `useNpcAlerts`, `useNpcPerformance`

**Комментарий в YAML:**
```
# Target Architecture:
# - Microservice: social-service (port 8084)
# - Frontend Module: modules/social/npc-hiring
# - State Store: useSocialStore(npcWorkforce)
# - UI: NpcWorkforceBoard, NpcSchedulePlanner, NpcTaskAutomation, NpcMoodMonitor, NpcPerformanceCharts
# - Forms: NpcWorkforceCreateForm, NpcTaskAssignmentForm, NpcScheduleForm, NpcWorkforceAlertResponseForm
# - Layouts: NpcWorkforceLayout, NpcAutomationLayout
# - Hooks: useNpcWorkforce, useNpcWorkforceMember, useNpcSchedules, useNpcAlerts, useNpcPerformance
# - Events: npc.hiring.performance.changed, social.npc-hiring.alert, social.npc-hiring.schedule.updated, world.event.generated, economy.npc-hiring.payroll.processed
# - API Base: /api/v1/social/npc-hiring/*
```

---

## ✅ Детальный план

1. **Определить сущность workforce:** состав, роли, задачи, расписания, привязка к контрактам.  
2. **Спроектировать схемы:** `NpcWorkforce`, `NpcWorkforceMember`, `NpcWorkforceTask`, `NpcSchedule`, `NpcAutomationRule`, `NpcWorkforceAlert`, `NpcPerformanceSnapshot`, `NpcWorkforceDashboard`.  
3. **Эндпоинты:** создание/управление workforce, назначение задач, расписания, автоматизация, мониторинг, алерты, отчёты.  
4. **Интеграции:** ссылки на контракты (API-TASK-349), payroll (API-TASK-351), world events, mentorship (обучение).  
5. **Документировать KPI и метрики, форматы dashboards, экспорт (при необходимости).**  
6. **Kafka:** описать события производительности, расписаний, алертов; очередь `npc-hiring-contract-review` для согласования заданий (если требуется).  
7. **Примеры:** боевой отряд, торговая смена, автоматизированная команда, alert по низкой лояльности, расписание недели.  
8. **Подключить shared security/responses/pagination; вынести схемы/примеры, соблюдать лимит 400 строк.**  
9. **Обозначить зависимости на другие API (contracts, payroll, npc-relationships, mentorship).**  
10. **Прогнать `scripts/validate-swagger.ps1`, обновить README.**

---

## 🔌 Эндпоинты

1. **POST `/social/npc-hiring/workforce`** — создание workforce (список NPC, цели, роли).  
2. **GET `/social/npc-hiring/workforce/{workforceId}`** — детальная карточка (состав, KPI, задачи).  
3. **GET `/social/npc-hiring/workforce`** — список/фильтры (тип, роль, статус, локация, лицензии).  
4. **PATCH `/social/npc-hiring/workforce/{workforceId}`** — обновление состава, ролей, автоматизации.  
5. **POST `/social/npc-hiring/workforce/{workforceId}/assign`** — назначение задач/миссий.  
6. **POST `/social/npc-hiring/workforce/{workforceId}/schedule`** — управление расписанием и сменами.  
7. **GET `/social/npc-hiring/workforce/{workforceId}/schedule`** — просмотр расписаний и ротаций.  
8. **POST `/social/npc-hiring/workforce/{workforceId}/automation`** — настройка автоматизации (rules, triggers).  
9. **GET `/social/npc-hiring/workforce/{workforceId}/dashboard`** — агрегаты KPI, настроение, лояльность, alerts.  
10. **GET `/social/npc-hiring/workforce/{workforceId}/alerts`** — активные предупреждения (loyalty, лицензии, ресурсы).  
11. **POST `/social/npc-hiring/workforce/{workforceId}/alerts/ack`** — обработка alert.  
12. **GET `/social/npc-hiring/workforce/stats`** — глобальные метрики (NpcPerformanceIndex, LoyaltyTrend).  
13. **GET `/social/npc-hiring/workforce/export`** — экспорт (CSV/JSON) при необходимости (опционально, оговорить).

---

## 🧱 Модели данных

- **NpcWorkforce** — `workforceId`, `name`, `type`, `purpose`, `employerId`, `contracts[]`, `members[]`, `location`, `status`, `automation`, `resources`, `metadata`.  
- **NpcWorkforceMember** — `npcId`, `role`, `skillProfile`, `loyalty`, `mood`, `performance`, `contractId`, `equipment`.  
- **NpcWorkforceTask** — `taskId`, `taskType`, `objectives`, `priority`, `duration`, `dependencies`, `status`.  
- **NpcSchedule** — `scheduleId`, `workforceId`, `timeslots[]`, `timezone`, `rotation`, `restPolicy`.  
- **NpcAutomationRule** — `ruleId`, `trigger`, `conditions`, `actions`, `cooldown`, `enabled`.  
- **NpcPerformanceSnapshot** — KPI (efficiency, successRate, downtime, incidents).  
- **NpcWorkforceAlert** — `alertId`, `severity`, `message`, `npcIds[]`, `actionRequired`, `createdAt`.  
- **NpcWorkforceDashboard** — агрегаты (NpcPerformanceIndex, LoyaltyTrend, PayrollBurnRate, HiringLeadTime).  
- **PaginatedNpcWorkforce** — стандартная пагинация.

---

## 📏 Принципы и правила

- OpenAPI 3.0.3; ≤400 строк, схемы/примеры вынести в `components`.  
- Использовать `shared/common/security.yaml`, `shared/common/responses.yaml`, `shared/common/pagination.yaml`.  
- Ошибки (`x-error-code`): `VAL_NPC_WORKFORCE_INVALID`, `BIZ_NPC_WORKFORCE_LIMIT_REACHED`, `BIZ_NPC_WORKFORCE_SCHEDULE_CONFLICT`, `BIZ_NPC_WORKFORCE_AUTOMATION_DISABLED`, `INT_NPC_WORKFORCE_PIPELINE_FAILURE`.  
- `info.description` — указать `.BRAIN` источники, UX подтверждения и интеграции.  
- Теги: `NPC Hiring`, `Workforce`, `Automation`, `Schedules`, `Performance`.  
- Обозначить зависимость на `npc-hiring/contracts.yaml`, `npc-hiring/payroll.yaml`, `npc-relationships/status.yaml`, `mentorship/programs.yaml`.

---

## ✅ Критерии приемки

1. Файл `api/v1/social/npc-hiring/workforce.yaml` создан/обновлён, проходит `scripts/validate-swagger.ps1`.  
2. В начале файла присутствует `Target Architecture` блок.  
3. Реализованы все эндпоинты, модели и примеры из задания.  
4. Подключены общие компоненты безопасности/ответов/пагинации.  
5. Добавлены примеры (боевой отряд, торговая смена, автоматизация, alert).  
6. Kafka события и интеграции с npc-service/world-service описаны.  
7. README в каталоге обновлён (в рамках реализации).  
8. Task добавлен в `brain-mapping.yaml`.  
9. `.BRAIN` документ обновлён (API Tasks Status).  
10. Указаны зависимости на контракты, payroll и mentorship.  
11. Учтены лимиты, лицензии и автоматизация.

---

## ❓ FAQ

**Q:** Как связать workforce с контрактами?  
A: Через `contracts[]` (IDs из `contracts.yaml`); при увольнении член удаляется из workforce.  

**Q:** Поддерживаются ли NPC, нанимающие других NPC?  
A: Да, предусмотреть `automation` и `delegation` поля; детализировать в схеме `NpcAutomationRule`.  

**Q:** Нужно ли учитывать инфраструктуру (жильё, рабочие места)?  
A: Да, добавить `resources` и `infrastructureRefs[]`; ошибки `BIZ_NPC_WORKFORCE_LIMIT_REACHED` при нехватке.  

**Q:** Как отображать настроение/лояльность?  
A: В `NpcWorkforceMember` и `NpcWorkforceDashboard` вернуть соответствующие показатели и тенденции.  

---

**Следующие шаги исполнителя:** создать OpenAPI-файл, вынести компоненты, описать интеграции, подготовить примеры и прогнать проверки.

