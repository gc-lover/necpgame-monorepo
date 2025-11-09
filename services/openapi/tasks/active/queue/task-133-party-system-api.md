# Task ID: API-TASK-133
**Тип:** API Generation  
**Приоритет:** высокий  
**Статус:** queued  
**Создано:** 2025-11-07 10:26  
**Создатель:** AI Agent  
**Зависимости:** API-TASK-129

---

## 📋 Краткое описание
Спроектировать OpenAPI для кооперативных групп: создание party, инвайты, распределение лута и синхронизация прогресса.

**Что нужно сделать:** подготовить спецификацию `social-service` для механики групп по документу `.BRAIN/05-technical/backend/party-system.md`.

---

## 🎯 Цель задания
Обеспечить backend-контракт для управления party (до 5 игроков) с ролями, настройками добычи и обменом прогресса, чтобы фронтенд и gameplay могли работать синхронно.

**Зачем это нужно:**
- Координировать совместный PvE/PvP контент, распределение опыта и трофеев.
- Предоставить UI-инструменты (панель группы, чат, настройки лута).  
- Обеспечить события для аналитики, матчмейкинга и рейдов.

---

## 📚 Источники информации

### Основной источник
**Репозиторий:** `.BRAIN`  
**Путь:** `.BRAIN/05-technical/backend/party-system.md`  
**Версия:** v1.0.0  
**Обновлено:** 2025-11-07  
**Статус:** ready  

**Важно из документа:**
- Максимальный размер группы (5), роли (tank/healer/dps), смена лидера.  
- Режимы добычи (need/greed, personal, master looter) и интеграция с loot-service.  
- Общий прогресс квестов, синхронизация с gameplay событиями.  
- События Event Bus (`party:*`).

### Дополнительные источники
- `.BRAIN/05-technical/backend/loot-system.md` — правила распределения лута.  
- `.BRAIN/05-technical/backend/matchmaking-system.md` — взаимодействие с матчмейкингом.  
- `API-SWAGGER/api/v1/gameplay/combat/combat-session.yaml` — пример использования партий в бою.

### Связанные документы
- `.BRAIN/02-gameplay/social/party-features.md` — UX ожидания.  
- `.BRAIN/02-gameplay/progression/group-bonuses.md` — бонусы группы.  
- `.BRAIN/05-technical/backend/chat-system.md` — party chat интеграция.

---

## 📁 Целевая структура API
### Репозиторий: `API-SWAGGER`
**Целевой файл:** `api/v1/social/party/party-system.yaml`  
> ⚠️ Серверы: `https://api.necp.game/v1/social` и `http://localhost:8080/api/v1/social`. Структуру каталогов соблюдать строго.

**Тип:** OpenAPI 3.0.3  
**Версия:** v1

**Директории:**
```
API-SWAGGER/
└── api/
    └── v1/
        └── social/
            └── party/
                └── party-system.yaml
```

---

## 🏗️ Целевая архитектура (⚠️ ОБЯЗАТЕЛЬНО)

### Backend
- **Микросервис:** social-service  
- **Порт:** 8084  
- **API Base:** `/api/v1/social/party`  
- **Интеграции:** gameplay-service (quest progress, combat sync), economy-service (loot), notification-service (invites), chat-service.  
- **Комментарий в начале спецификации:**
  ```yaml
  # Target Architecture:
  # - Microservice: social-service (port 8084)
  # - API Base: /api/v1/social/party
  # - Dependencies: gameplay-service, economy-service, notification-service, chat-service
  # - Frontend Module: modules/social/party
  # - UI: PartyPanel, PartyMemberCard, LootSettingsModal
  # - Forms: PartyInviteForm, PartySettingsForm
  # - Hooks: useSocialStore, useRealtime, useCharacter
  ```

### OpenAPI требования
- `info.x-microservice`:
  ```yaml
  x-microservice:
    name: social-service
    port: 8084
    domain: social
    base-path: /api/v1/social/party
    directory: api/v1/social/party
    package: com.necpgame.socialservice
  ```
- `servers`:
  ```yaml
  servers:
    - url: https://api.necp.game/v1/social
      description: Production API Gateway
    - url: http://localhost:8080/api/v1/social
      description: Local API Gateway
  ```
- WebSocket (`x-websocket`) для обновлений состояния: `wss://api.necp.game/v1/social/party/{partyId}/stream`.

### Frontend
- **Модуль:** `modules/social/party`.  
- **State Store:** `useSocialStore` (`party`, `members`, `lootSettings`, `invites`).  
- **UI:** PartyPanel, PartyMemberCard, LootSettingsModal, LeaderBadge.  
- **Формы:** PartyInviteForm, PartySettingsForm, RoleAssignmentForm.  
- **Хуки:** useRealtime, useCharacter, useQuestProgress.  
- **Layouts:** GameLayout (sidebar party widget).

---

## ✅ Что нужно сделать

### Шаг 1. Анализ
- Зафиксировать бизнес-правила: лимит участников, роли, условия transfer leadership, kick.  
- Определить логику распределения лута, интеграцию с combat/quest системами.  
- Собрать список событий для WebSocket и event bus.

### Шаг 2. Проектировать endpoints
1. **POST `/api/v1/social/party`** — создание party (инициатор становится лидером).  
2. **GET `/api/v1/social/party`** — текущее состояние своей party.  
3. **POST `/api/v1/social/party/invite`**, **POST `/api/v1/social/party/invite/respond`** — приглашение и ответ (accept/decline).  
4. **POST `/api/v1/social/party/leave`**, **POST `/api/v1/social/party/kick`** — выход/исключение.  
5. **POST `/api/v1/social/party/leader/transfer`** — смена лидера.  
6. **PATCH `/api/v1/social/party/settings`** — смена режима лута, роли участников, master looter.  
7. **GET `/api/v1/social/party/loot-history`** — журнал распределения.  
8. **POST `/api/v1/social/party/role`** — обновление роли участника.  
9. **GET `/api/v1/social/party/invites`** — список входящих/исходящих инвайтов.  
10. **POST `/api/v1/social/party/ready-check`** — запуск ready check, ответы участников.  
11. **POST `/api/v1/social/party/quest-sync`** — синхронизация квестовых этапов.

### Шаг 3. Модели
- `Party`, `PartyMember`, `PartyInvite`, `PartySettings`, `ReadyCheck`, `LootHistoryEntry`.  
- Ошибки: `PartyError` (`VAL_MAX_MEMBERS`, `VAL_INVALID_ROLE`, `BIZ_NOT_LEADER`, `BIZ_INVITE_EXPIRED`).  
- WebSocket payloads: `partyUpdated`, `memberJoined`, `lootDistributed`, `readyCheck`.

### Шаг 4. OpenAPI
- Описать все методы, параметры (`partyId`, `inviteId`, `memberId`).  
- Использовать `shared/common` для ответов, включить `BearerAuth`.  
- Добавить `ServiceToken` при необходимости (например, для матчмейкера).  
- Примеры: создание party, обновление настроек, ready check.  
- Схемы вынести в `components`, предусмотреть reuse.

### Шаг 5. Валидация
- `scripts/validate-swagger.ps1 -ApiDirectory API-SWAGGER/api/v1/social/party/`.  
- Проверить соответствие ключевым механикам и ограничениям (5 игроков).  
- Обновить brain-mapping и `.BRAIN` документ, README в каталоге `party`.

---

## 🔍 Критерии приемки
1. `info.x-microservice` заполнен допустимыми значениями (`social-service`, 8084, `social`).  
2. Все пути начинаются с `/api/v1/social/party`.  
3. Описаны операции создания/инвайтов/лидера/настроек/ready check.  
4. Ограничение 5 участников и проверки ролей отражены в схемах/ответах.  
5. Loot modes (need/greed/personal/master) и журнал распределения описаны.  
6. Shared quest sync и ready check включены в endpoints/WebSocket события.  
7. Ошибки используют `shared/common/responses.yaml`.  
8. Есть примеры запросов/ответов и payloadов WebSocket.  
9. Валидация `swagger-cli` и проектного скрипта проходит без ошибок.  
10. Обновлены brain-mapping и `.BRAIN` (новый путь `api/v1/social/party/party-system.yaml`).  
11. README каталога `party` (при необходимости) содержит назначение и ключевые endpoints.

---

## FAQ
- **Что происходит при выходе лидера?** Автопереназначение на старшего участника или завершение группы — опиши в API поведении.  
- **Как обрабатывать оффлайн участников?** Endpoints должны возвращать флаги состояния; кик возможен только лидером.  
- **Можно ли переименовать группу?** Сейчас нет — добавь как restriction в описании.  
- **Нужен ли party matchmaking?** Не в этой спецификации, но предусмотреть `x-integrations` с матчмейкингом.  
- **Как синхронизировать прогресс?** Через `quest-sync` endpoint и события от gameplay.

---

**Источник:** `.BRAIN/05-technical/backend/party-system.md` (v1.0.0, ready)

