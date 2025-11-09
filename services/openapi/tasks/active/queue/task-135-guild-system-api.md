# Task ID: API-TASK-135
**Тип:** API Generation  
**Приоритет:** высокий  
**Статус:** queued  
**Создано:** 2025-11-07 10:30  
**Создатель:** AI Agent  
**Зависимости:** API-TASK-133

---

## 📋 Краткое описание
Создать OpenAPI-спецификацию гильдий: управление составом, рангами, банком, прогрессом и событиями.

**Что нужно сделать:** описать API `social-service` на основе `.BRAIN/05-technical/backend/guild-system-backend.md`.

---

## 🎯 Цель задания
Дать сквозной контракт для гильдий с поддержкой PvE/PvP контента, экономики (банк) и событийного планирования.

**Зачем это нужно:**
- Управление кланами и их ресурсами.  
- Поддержка социальных механик (вступления, войны, ивенты).  
- Интеграция с экономикой, world и progression подсистемами.

---

## 📚 Источники информации

### Основной источник
**Репозиторий:** `.BRAIN`  
**Путь:** `.BRAIN/05-technical/backend/guild-system-backend.md`  
**Версия:** v1.0.0  
**Обновлено:** 2025-11-07  
**Статус:** ready  

**Ключевые моменты:**
- Уровни гильдий, перки, расширение лимита участников.  
- Guild bank (валюта, предметы, разрешения).  
- Система рангов и разрешений, события (календарь, приглашения).  
- Поддержка Guild Wars, territory control.

### Дополнительные источники
- `.BRAIN/05-technical/backend/territory-control.md` — владение территориями.  
- `.BRAIN/05-technical/backend/guild-wars.md` — механики войн.  
- `.BRAIN/05-technical/backend/economy-guild-bank.md` — финансовые операции.  
- `API-SWAGGER/api/v1/social/party/party-system.yaml` (взаимодействие между группами и гильдиями).

### Связанные документы
- `.BRAIN/02-gameplay/social/guild-features.md` — UX/функции.  
- `.BRAIN/02-gameplay/economy/guild-perks.md` — бонусы уровней.  
- `.BRAIN/05-technical/backend/event-bus-overview.md` — события гильдий.

---

## 📁 Целевая структура API
### Репозиторий: `API-SWAGGER`
**Целевой файл:** `api/v1/social/guilds/guild-system.yaml`  
> ⚠️ Серверы: `https://api.necp.game/v1/social` и `http://localhost:8080/api/v1/social`.

**Тип:** OpenAPI 3.0.3  
**Версия:** v1

```
API-SWAGGER/
└── api/
    └── v1/
        └── social/
            └── guilds/
                └── guild-system.yaml
```

---

## 🏗️ Целевая архитектура (⚠️ ОБЯЗАТЕЛЬНО)

### Backend
- **Микросервис:** social-service  
- **Порт:** 8084  
- **API Base:** `/api/v1/social/guilds`  
- **Интеграции:** economy-service (банк), world-service (территории), combat-service (guild wars), notification-service, analytics-service.  
- **Комментарий в спецификации:**
  ```yaml
  # Target Architecture:
  # - Microservice: social-service (port 8084)
  # - API Base: /api/v1/social/guilds
  # - Dependencies: economy-service, world-service, combat-service, notification-service
  # - Frontend Module: modules/social/guilds
  # - UI: GuildDashboard, GuildMemberTable, GuildBankPanel
  # - Forms: GuildCreationForm, GuildInviteForm, GuildBankTransferForm
  # - Hooks: useSocialStore, useRealtime, useGuildPermissions
  ```

### OpenAPI требования
- `info.x-microservice`:
  ```yaml
  x-microservice:
    name: social-service
    port: 8084
    domain: social
    base-path: /api/v1/social/guilds
    directory: api/v1/social/guilds
    package: com.necpgame.socialservice
  ```
- `servers` как выше, WebSocket `x-websocket`: `wss://api.necp.game/v1/social/guilds/{guildId}/stream` (ивенты, банк).  
- События: `guildUpdated`, `memberJoined`, `bankChanged`, `warDeclared`.

### Frontend
- **Модуль:** `modules/social/guilds`.  
- **State Store:** `useSocialStore` (`guild`, `members`, `bank`, `events`, `wars`).  
- **UI:** GuildDashboard, GuildMemberTable, PermissionsMatrix, GuildBankPanel, GuildEventCalendar.  
- **Формы:** GuildCreationForm, GuildInviteForm, GuildRankForm, GuildBankTransferForm, GuildEventForm.  
- **Хуки:** useRealtime, usePermissions, useTerritoryMap, useEconomyStore.  
- **Layouts:** GameLayout, GuildManagementLayout.

---

## ✅ Что нужно сделать

### Шаг 1. Анализ
- Перечислить ранги, разрешения, лимиты.  
- Учесть механики развития (уровни, опыт), связанные perks.  
- Определить банковские операции, логирование, ограничения.  
- События календаря и участие.

### Шаг 2. Endpoints
1. **POST `/api/v1/social/guilds`** — создание гильдии.  
2. **GET `/api/v1/social/guilds/{guildId}`** — профиль гильдии.  
3. **PATCH `/api/v1/social/guilds/{guildId}`** — редактирование настроек, описания, эмблемы.  
4. **POST `/api/v1/social/guilds/{guildId}/invite`**, **POST `/api/v1/social/guilds/{guildId}/invite/respond`** — приглашения.  
5. **POST `/api/v1/social/guilds/{guildId}/leave`**, **POST `/api/v1/social/guilds/{guildId}/kick`**.  
6. **PATCH `/api/v1/social/guilds/{guildId}/members/{memberId}/rank`** — управление рангами.  
7. **GET `/api/v1/social/guilds/{guildId}/members`** — участники с ролями и статистикой.  
8. **GET `/api/v1/social/guilds/{guildId}/bank`**, **POST `/bank/deposit`**, **POST `/bank/withdraw`**, **GET `/bank/logs`**.  
9. **GET `/api/v1/social/guilds/{guildId}/progress`** — уровень, опыт, перки.  
10. **POST `/api/v1/social/guilds/{guildId}/events`**, **GET `/events`**, **POST `/events/{eventId}/rsvp`**.  
11. **POST `/api/v1/social/guilds/{guildId}/wars/declare`**, **POST `/wars/{warId}/respond`**, **GET `/wars`**.  
12. **GET `/api/v1/social/guilds/search`** — поиск гильдий с фильтрами.  
13. **DELETE `/api/v1/social/guilds/{guildId}`** — роспуск (только лидер).

### Шаг 3. Модели
- `Guild`, `GuildSettings`, `GuildMember`, `GuildRank`, `GuildPermission`, `GuildBank`, `BankTransaction`, `GuildProgress`, `GuildEvent`, `GuildWar`.  
- Ошибки: `GuildError` (`VAL_NAME_TAKEN`, `VAL_BANK_LIMIT`, `BIZ_NOT_LEADER`, `BIZ_PERMISSION_DENIED`, `BIZ_WAR_ALREADY_DECLARED`).  
- WebSocket payload: `guildUpdated`, `bankUpdated`, `eventCreated`, `warDeclared`.

### Шаг 4. OpenAPI
- Разделы `paths`, `components`, `security`.  
- Примеры: создание гильдии, выдача ранга, депозит в банк, объявление войны.  
- Использовать `$ref` на `shared/common`.  
- Указать ограничение размеров банка, rate limiting на invitations.

### Шаг 5. Валидация
- `scripts/validate-swagger.ps1 -ApiDirectory API-SWAGGER/api/v1/social/guilds/`.  
- Проверить, что включены ключевые механики (банк, события, войны).  
- Обновить brain-mapping, `.BRAIN` документ и README каталога `guilds`.

---

## 🔍 Критерии приемки
1. Корректный `info.x-microservice` (`social-service`, 8084, `social`).  
2. Все маршруты лежат под `/api/v1/social/guilds`.  
3. Поддержаны управление составом, рангами, permissions, банк, прогресс.  
4. Журнал банка и истории событий присутствует.  
5. Веб-сокет события описаны в `x-websocket`.  
6. Ошибки используют `shared/common/responses.yaml`.  
7. Примеры данных для ключевых операций.  
8. Валидаторы (`swagger-cli`, скрипт) проходят.  
9. Обновлены brain-mapping и `.BRAIN` с новым путём `api/v1/social/guilds/guild-system.yaml`.  
10. README каталога `guilds` отражает назначение API.  
11. Указаны ограничения (лимиты участников, банк, cooldown войн).

---

## FAQ
- **Можно ли менять название/тег?** Только при наличии пермишенов и уникальности — добавить в спецификацию.  
- **Как работает банк?** Только ранги с разрешением `BANK_WITHDRAW`; операции логируются.  
- **Что если лидер офлайн?** Передача гильдии другим офицерам через endpoint.  
- **Как объявить войну?** Через dedicated endpoint, с подтверждением второй стороны.  
- **Нужны ли территории?** API должно содержать ссылки на territory-service для контроля владений.

---

**Источник:** `.BRAIN/05-technical/backend/guild-system-backend.md` (v1.0.0, ready)

