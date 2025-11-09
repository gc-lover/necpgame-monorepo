# Task ID: API-TASK-141
**Тип:** API Generation  
**Приоритет:** средний  
**Статус:** queued  
**Создано:** 2025-11-07 10:42  
**Создатель:** AI Agent  
**Зависимости:** none

---

## 📋 Краткое описание
Создать OpenAPI для системы ежедневных/еженедельных сбросов лимитов, квестов и наград.

**Что нужно сделать:** оформить спецификацию world-service по `.BRAIN/05-technical/backend/daily-weekly-reset-system.md`.

---

## 🎯 Цель задания
Обеспечить централизованный сервис расписаний, который запускает daily/weekly reset события, публикует уведомления и предоставляет статус таймеров.

**Зачем это нужно:**
- Поддержка ежедневных активностей, сезонных ограничений и наград.  
- Синхронизация всех микросервисов через события `system:daily-reset` и `system:weekly-reset`.  
- UI-отображение таймеров и уведомление игроков.

---

## 📚 Источники информации

### Основной источник
**Путь:** `.BRAIN/05-technical/backend/daily-weekly-reset-system.md`  
**Версия:** v1.0.0 · **Статус:** ready · **Дата:** 2025-11-07  

**Ключевые моменты:**
- Крон-расписания (UTC), поддержка daily/weekly (и future monthly).  
- Список сущностей для сброса: quests, rewards, limits, currencies, bonuses.  
- Механизм уведомлений и публикация событий в Event Bus.

### Дополнительные источники
- `.BRAIN/05-technical/backend/quest-engine-backend.md` — daily quest прогресс.  
- `.BRAIN/05-technical/backend/economy-system.md` — торговые лимиты.  
- `.BRAIN/05-technical/backend/notification-system.md` — push уведомления.  
- `.BRAIN/05-technical/backend/save-system.md` — сохранение статусов reset.

### Связанные документы
- `.BRAIN/02-gameplay/progression/daily-routines.md` — дизайн ежедневных активностей.  
- `.BRAIN/05-technical/backend/realtime-server/part1-architecture-zones.md` — рассылка событий.  
- `.BRAIN/05-technical/backend/analytics-data-lake.md` — логирование reset истории.

---

## 📁 Целевая структура API
### Репозиторий: `API-SWAGGER`
**Целевой файл:** `api/v1/world/system/reset/reset-system.yaml`  
> ⚠️ Серверы: `https://api.necp.game/v1/world` и `http://localhost:8080/api/v1/world`.

**Тип:** OpenAPI 3.0.3 · **Версия:** v1

```
API-SWAGGER/
└── api/
    └── v1/
        └── world/
            └── system/
                └── reset/
                    └── reset-system.yaml
```

---

## 🏗️ Целевая архитектура (⚠️ ОБЯЗАТЕЛЬНО)

### Backend
- **Микросервис:** world-service  
- **Порт:** 8086  
- **API Base:** `/api/v1/world/system/reset`  
- **Интеграции:** gameplay-service (quests), economy-service (limits), social-service (invites), notification-service, analytics-service.  
- **Комментарий:**
  ```yaml
  # Target Architecture:
  # - Microservice: world-service (port 8086)
  # - API Base: /api/v1/world/system/reset
  # - Dependencies: gameplay-service, economy-service, social-service, notification-service, analytics-service
  # - Frontend Module: modules/system/reset
  # - UI: ResetTimerWidget, ResetStatusCard
  # - Hooks: useSystemStore, useRealtime, useTimeZone
  ```

### OpenAPI требования
- `info.x-microservice`:
  ```yaml
  x-microservice:
    name: world-service
    port: 8086
    domain: world
    base-path: /api/v1/world/system/reset
    directory: api/v1/world/system/reset
    package: com.necpgame.worldservice
  ```
- WebSocket (опционально): `wss://api.necp.game/v1/world/system/reset/stream` (broadcast обновлений статуса).

### Frontend
- **Модуль:** `modules/system/reset`.  
- **State Store:** `useSystemStore` (`nextDailyReset`, `nextWeeklyReset`, `status`, `history`).  
- **UI:** ResetTimerWidget, ResetStatusCard, ResetHistoryTable.  
- **Формы:** ResetConfigForm (admin).  
- **Хуки:** useRealtime, useTimeZone, useNotificationStore.  
- **Layouts:** GameLayout (header timer), AdminLayout (управление расписаниями).

---

## ✅ Что нужно сделать

### Шаг 1. Анализ
- Сформировать модель расписаний (cron expressions, timezone).  
- Определить scope сбросов и payload событий.  
- Требования к журналу истории и уведомлениям.

### Шаг 2. Endpoints
1. **GET `/api/v1/world/system/reset/status`** — состояние систем (таймеры, прошлый reset).  
2. **GET `/api/v1/world/system/reset/schedule`** — расписание cron (daily/weekly).  
3. **POST `/api/v1/world/system/reset/trigger`** — ручной запуск (admin/service token).  
4. **POST `/api/v1/world/system/reset/schedule`** — обновление cron (admin).  
5. **GET `/api/v1/world/system/reset/history`** — история запусков (времена, затронутые области).  
6. **GET `/api/v1/world/system/reset/affected-services`** — список сервисов/объектов для сброса.  
7. **GET `/api/v1/world/system/reset/calendar`** — календарь будущих сбросов (30 дней).

### Шаг 3. Модели
- `ResetStatus`, `ResetSchedule`, `ResetTriggerRequest`, `ResetHistoryEntry`, `AffectedService`, `ResetNotification`.  
- Ошибки: `ResetError` (`BIZ_SCHEDULE_LOCKED`, `VAL_INVALID_CRON`, `BIZ_RESET_IN_PROGRESS`).  
- WebSocket payload: `resetScheduled`, `resetStarted`, `resetCompleted`.

### Шаг 4. OpenAPI оформление
- Описать `paths`, параметры (`timezone`, `limit`, `scope`).  
- Использовать `shared/common` для `Error` и `security`.  
- `security`: `BearerAuth` для чтения; `ServiceToken/AdminToken` для изменений.  
- Примеры: получение статуса, ручной запуск, обновление cron.  
- В `components` описать enum `ResetScope` (quests, rewards, limits, currencies).

### Шаг 5. Проверки
- `scripts/validate-swagger.ps1 -ApiDirectory API-SWAGGER/api/v1/world/system/reset/`.  
- Убедиться, что файл ≤ 400 строк; README в каталоге обновлён.  
- Обновить `brain-mapping.yaml`, документ `.BRAIN`, README `world/system/reset`.

---

## 🔍 Критерии приемки
1. Правильный `info.x-microservice` и `base-path` (`world-service`, 8086, `/api/v1/world/system/reset`).  
2. Описаны status, schedule, trigger, history, affected services.  
3. Поддержка ручного запуска и обновления cron (с `ServiceToken`).  
4. WebSocket / уведомления задокументированы.  
5. Ошибки используют `shared/common/responses.yaml`.  
6. Примеры покрывают ключевые операции.  
7. Валидаторы проходят без ошибок.  
8. `brain-mapping` и `.BRAIN` обновлены.  
9. README раздела содержит описание и примеры использования.  
10. Учтена timezone (UTC) и локальные отображения.  
11. История reset включает затронутые сервисы и длительность.

---

## FAQ
- **Можно ли запускать только определённые области?** Да, `trigger` принимает `scope` список.  
- **Что если reset уже идёт?** Возвращаем `BIZ_RESET_IN_PROGRESS`.  
- **Как уведомляются игроки?** Через notification-service и WebSocket broadcast.  
- **Поддерживаются monthly reset?** Предусмотреть расширение (`monthlySchedule`).  
- **Как синхронизировать сервера в разных регионах?** Все расчёты в UTC, локальное время возвращается в payload.

---

**Источник:** `.BRAIN/05-technical/backend/daily-weekly-reset-system.md` (v1.0.0, ready)

