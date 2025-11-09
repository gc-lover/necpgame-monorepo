# Task ID: API-TASK-222
**Тип:** API Generation
**Приоритет:** высокий
**Статус:** queued
**Создано:** 2025-11-08 03:27
**Создатель:** GPT-5 Codex (API Task Creator)
**Зависимости:** API-TASK-212, API-TASK-213

---

## 📋 Краткое описание

Реализовать ядро административных инструментов: управление игроками, модерация, аналитика и GM операции.

**Что нужно сделать:** Создать `api/v1/admin/tools/admin-tools.yaml`, описав эндпоинты, модели и события, описанные в документе `admin-tools-core.md`.

---

## 🎯 Цель задания

Обеспечить админам и модераторам надежный API для управления игроками, расследований и live-ops задач.

**Зачем это нужно:**
- Управлять аккаунтами, персонажами, санкциями, аудитом
- Предоставить аналитические и административные инструменты (инвентаризация, логирование)
- Поддержать модуль админ-панели (API-TASK-212/213)
- Интегрироваться с incident/support системами

---

## 📚 Источники информации

### Основной документ

**Путь:** `.BRAIN/05-technical/backend/admin/admin-tools-core.md`
**Версия:** v1.0.0 (2025-11-07 01:59)
**Статус:** approved, api-readiness: ready

**Ключевые разделы:**
- Admin console архитектура, роли (`SUPER_ADMIN`, `MODERATOR`, `SUPPORT`, `GM`)
- Account/character tools, sanctions, investigations
- Logs & audit trail, evidence attachments
- Player inventory inspection, economy adjustments, GM commands
- Integration with incident/service desk

### Дополнительные источники

- `.BRAIN/05-technical/backend/support/support-ticket-system.md`
- `.BRAIN/05-technical/backend/incident-response/incident-response.md`
- `.BRAIN/05-technical/backend/notification-system.md`
- `.BRAIN/05-technical/backend/progression-backend.md`
- `.BRAIN/05-technical/backend/economy-system.md`

### Связанные документы

- `API-SWAGGER/tasks/active/queue/task-212-admin-panel-ui-api.md`
- `API-SWAGGER/tasks/active/queue/task-213-admin-panel-analytics-ui-api.md`
- `API-SWAGGER/tasks/active/queue/task-200-support-ticket-system-api.md`

---

## 📁 Целевая структура API

- **Файл:** `api/v1/admin/tools/admin-tools.yaml`
- **Версия API:** v1
- **Формат:** OpenAPI 3.0.3

```
API-SWAGGER/api/v1/admin/tools/
 └── admin-tools.yaml  ← создать/заполнить
```

---

## 🏗️ Целевая архитектура (⚠️ ОБЯЗАТЕЛЬНО)

### Backend
- **Микросервис:** admin-service
- **Порт:** 8087
- **API Base Path:** `/api/v1/admin/tools`
- **Зависимости:**
  - auth-service – авторизация, MFA
  - character-service – данные персонажей
  - session-service – управление активными сессиями
  - inventory-service – инспекция/изъятие предметов
  - economy-service – корректировки валюты/цен
  - incident-service – расследования и кейсы
  - support-service – тикеты и обращения
  - notification-service – уведомления/alerts для админов
  - analytics-service – отчётность, метрики

### Frontend
- **Модуль:** `modules/admin/tools`
- **State Store:** `useAdminToolsStore`
- **State:** `playerProfile`, `sanctions`, `logs`, `inventoryInspect`, `analytics`, `commandQueue`
- **UI компоненты:** `PlayerInspector`, `SanctionPanel`, `IncidentTimeline`, `GMCommandConsole`, `InventoryViewer`, `AuditLogTable`
- **Формы:** `PlayerSanctionForm`, `IncidentNoteForm`, `EconomyAdjustmentForm`, `GMCommandForm`
- **Layouts:** `AdminLayout`
- **Хуки:** `useAdminPlayerData`, `useSanctions`, `useIncidentTools`, `useGMCommands`

### Комментарий для YAML

```yaml
# Target Architecture:
# - Microservice: admin-service (port 8087)
# - API Base: /api/v1/admin/tools
# - Dependencies: auth, character, session, inventory, economy, incident, support, notification, analytics
# - Frontend Module: modules/admin/tools (useAdminToolsStore)
# - UI: PlayerInspector, SanctionPanel, IncidentTimeline, GMCommandConsole, InventoryViewer, AuditLogTable
# - Forms: PlayerSanctionForm, IncidentNoteForm, EconomyAdjustmentForm, GMCommandForm
# - Hooks: useAdminPlayerData, useSanctions, useIncidentTools, useGMCommands
```

---

## ✅ Что нужно сделать (детальный план)

1. Описать API игроков/аккаунтов: поиск, детальный просмотр, notes, history.
2. Реализовать модерационные действия: warn, mute, ban, kick, sanction templates.
3. Добавить инструменты инвентаря: просмотр, изъятие, возврат, логирование.
4. Ввести экономические команды: adjust currency, rollback transactions, audit trail.
5. Сформировать исследовательские инструменты: incident timeline, evidence uploads, связи с тикетами.
6. Описать GM команды: spawn item, teleport, reset progression, с подтверждениями.
7. Настроить события (`admin:action-performed`, `admin:command-executed`).
8. Определить требования к безопасности: MFA, auditId, role enforcement.
9. Добавить примеры запросов/ответов, тестовые сценарии, чеклист.

---

## 🔀 Endpoints

1. **GET `/api/v1/admin/tools/players`** – поиск игроков (filters: nickname, accountId, status, sanction)
2. **GET `/api/v1/admin/tools/players/{playerId}`** – профиль игрока + связанные данные.
3. **POST `/api/v1/admin/tools/players/{playerId}/notes`** – добавить заметку/заметку расследования.
4. **GET `/api/v1/admin/tools/players/{playerId}/sanctions`** – активные/исторические санкции.
5. **POST `/api/v1/admin/tools/players/{playerId}/sanctions`** – применить санкцию (`WARN|MUTE|BAN|SUSPEND`), с `auditId`.
6. **POST `/api/v1/admin/tools/players/{playerId}/sanctions/{sanctionId}/revoke`** – снять санкцию.
7. **GET `/api/v1/admin/tools/players/{playerId}/inventory`** – инспекция инвентаря и банка.
8. **POST `/api/v1/admin/tools/players/{playerId}/inventory/actions`** – изъять/вернуть/заблокировать предмет.
9. **POST `/api/v1/admin/tools/players/{playerId}/economy-adjust`** – корректировка валюты/ресурсов.
10. **GET `/api/v1/admin/tools/incidents`** – список расследований (фильтры, статус).
11. **POST `/api/v1/admin/tools/incidents/{incidentId}/timeline`** – добавить событие/заметку.
12. **POST `/api/v1/admin/tools/gm/commands`** – выполнение GM команды (spawn, teleport, reset) с подтверждением.
13. **GET `/api/v1/admin/tools/logs`** – аудит действий (пагинация, фильтры по оператору, типу).
14. **GET `/api/v1/admin/tools/analytics`** – метрики (санкции, отчёты, economy adjustments).
15. **WS `/api/v1/admin/tools/stream`** – события: `player-updated`, `sanction-created`, `incident-updated`, `gm-command-result`, `alert-triggered`.

---

## 🧱 Модели данных

- **AdminPlayerProfile** – `playerId`, `accountId`, `status`, `roles`, `playtime`, `characters[]`, `recentIncidents`, `notes[]`.
- **Sanction** – `sanctionId`, `type`, `reason`, `issuedBy`, `issuedAt`, `expiresAt`, `status`, `evidence[]`.
- **InventoryInspection** – `items[]`, `bank[]`, `lockedItems[]`, `notes`.
- **EconomyAdjustmentRequest** – `currency`, `amount`, `reason`, `auditId`, `requiresApproval`.
- **IncidentTimelineEntry** – `entryId`, `incidentId`, `timestamp`, `author`, `type`, `summary`, `attachments[]`.
- **GMCommandRequest** – `commandType`, `parameters`, `targetPlayerId`, `requiresConfirmation`, `auditId`.
- **GMCommandResponse** – `commandId`, `status`, `result`, `logs[]`.
- **AdminAuditLogEntry** – `timestamp`, `operator`, `action`, `target`, `payload`, `result`, `incidentId`.
- **RealtimeEventPayload** – типизированные события (`playerUpdated`, `sanctionCreated`, `incidentUpdated`, `gmCommandResult`, `alertTriggered`).
- **Error Schema (`AdminToolsError`)** – codes (`PERMISSION_DENIED`, `MFA_REQUIRED`, `AUDIT_ID_REQUIRED`, `EVIDENCE_REQUIRED`, `COMMAND_REJECTED`, `INCIDENT_LOCKED`).

---

## 🧭 Принципы и правила

- Авторизация: `BearerAuth` + обязательная роль; критичные действия требуют MFA и `X-Audit-Id`.
- Approval flow: для экономических/GM команд предусмотреть двухэтапное подтверждение.
- Audit trail: все POST/DELETE оформляются в журнал.
- Rate limiting: ограничить массовые команды (например, 10 команд/мин).
- Evidence: санкции/команды могут требовать приложения файлов (ссылки в storage).
- Инциденты: связь с incident-service, в случае ошибок — автоматическое создание тикета.
- DRY: общие компоненты (`responses.yaml`, `security.yaml`).

---

## 🧪 Примеры

- Инспекция игрока и применение mute на 24 часа.
- Экономическая корректировка с подтверждением и уведомлением.
- Добавление заметки в расследование и публикация события.
- GM команда teleport с ответом и WS событием.
- Событие `sanction-created` в real-time потоке для UI.

---

## 🔗 Связности и зависимости

- Интегрируется с admin-panel UI (Tasks 212, 213).
- Использует support/incident сервисы для кейсов, notification для alerts.
- Связан с session/character/inventory/economy сервисами.

---

## ✅ Критерии приемки

1. Файл `admin-tools.yaml` создан с полным описанием эндпоинтов, моделей, событий.
2. Прописаны требования к безопасности, MFA, auditId, approvals.
3. Добавлены примеры и тестовые сценарии; чеклист выполнен.
4. Связи с инцидентами/тикетами/уведомлениями документированы.

---

## 📎 Checklist

- [ ] Использован шаблон `api-generation-task-template.md`
- [ ] Определены микросервис, модуль, зависимости, UI компоненты
- [ ] Эндпоинты и события покрывают инструменты администратора
- [ ] Добавлены модели, ошибки, примеры, критерии
- [ ] После сохранения обновить `tasks/config/brain-mapping.yaml`

---

## ❓FAQ

**Q:** Как отличить GM команды от модераторских?**
**A:** Через поле `commandType` и проверку роли (`GM`, `SUPER_ADMIN`). GM команды требуют дополнительного подтверждения/аудита.

**Q:** Можно ли массово выдавать санкции?**
**A:** Да, через batch endpoint (возможное расширение) с лимитом и audit trail; в текущем задании отметить ограничения.



### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

