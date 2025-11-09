# Auth & Character Management Package

**Приоритет:** critical  
**Статус:** in_progress  
**Ответственный:** Brain Manager  
**Старт:** 2025-11-09 01:24  
**Связанные документы:**
- `.BRAIN/05-technical/backend/auth/README.md`
- `.BRAIN/05-technical/backend/player-character-mgmt/character-management.md`
- `.BRAIN/05-technical/backend/progression-backend.md`
- `.BRAIN/05-technical/backend/auth/auth-database-registration.md`
- `.BRAIN/05-technical/backend/auth/auth-login-jwt.md`
- `.BRAIN/05-technical/backend/auth/auth-authorization-security.md`

---

## Прогресс
- Подтверждена готовность auth, character-management и progression документов (`ready`, каталоги `api/v1/auth/authentication.yaml`, `api/v1/characters/players.yaml`, `api/v1/gameplay/progression/progression-core.yaml`).
- Собраны зависимости: auth-service (JWT, OAuth, roles), character-service (слоты, переключение), gameplay-service (level/skill progression), session-service (token ↔ session), economy-service (слоты/валюта), analytics-service (мониторинг логинов/прогрессии).
- Зафиксированы ключевые события Event Bus: `ACCOUNT_CREATED`, `LOGIN_SUCCESS`, `CharacterCreated`, `CharacterSwitched`, `character:level-up`, `character:skill-leveled`.
- Определены таблицы БД: `accounts`, `account_oauth`, `account_sessions`, `players`, `characters`, `character_slots`, `character_state_snapshots`, `character_progression`, `skill_experience`.

---

## Сводка готовности

| Документ | Версия | Статус | API каталог | Фронтенд модуль | Ключевые моменты |
| --- | --- | --- | --- | --- | --- |
| auth/README.md | 1.0.1 | ready | `api/v1/auth/authentication.yaml` | `modules/auth` | регистрация, login, OAuth, роли/permissions, события |
| character-management.md | 1.1.0 | ready | `api/v1/characters/players.yaml` | `modules/characters` | создание/удаление, восстановление, переключение, аудит |
| progression-backend.md | 1.0.0 | ready | `api/v1/gameplay/progression/progression-core.yaml` | `modules/progression` | EXP, level, навыки, атрибуты, события |

---

## REST backlog (черновик)

| Приоритет | Endpoint | Источник | Краткое описание |
| --- | --- | --- | --- |
| P0 | `POST /api/v1/auth/register` | auth/README.md | регистрация, подтверждение email, события `ACCOUNT_CREATED` |
| P0 | `POST /api/v1/auth/login` | auth-login-jwt.md | логин, выдача JWT/refresh, антибрютфорс |
| P0 | `POST /api/v1/auth/logout` | auth-login-jwt.md | инвалидация сессии, публикация `LOGOUT` |
| P0 | `POST /api/v1/auth/refresh` | auth-login-jwt.md | обновление токенов, валидация refresh |
| P0 | `POST /api/v1/auth/password/forgot` | auth-database-registration.md | запрос сброса, запись токена восстановления |
| P0 | `POST /api/v1/auth/password/reset` | auth-database-registration.md | сброс пароля, аудит |
| P1 | `GET /api/v1/auth/roles` | auth-authorization-security.md | список ролей/permissions |
| P1 | `POST /api/v1/auth/oauth/{provider}/callback` | auth-database-registration.md | завершение OAuth, связывание аккаунта |
| P1 | `POST /api/v1/players/{accountId}/characters` | character-management.md | создание персонажа, слоты, события `CharacterCreated` |
| P1 | `DELETE /api/v1/players/{accountId}/characters/{characterId}` | character-management.md | soft-delete, очередь восстановления |
| P1 | `POST /api/v1/players/{accountId}/characters/{characterId}/restore` | character-management.md | восстановление soft-delete |
| P1 | `POST /api/v1/players/{accountId}/switch` | character-management.md | переключение активного персонажа |
| P1 | `POST /api/v1/players/{accountId}/slots/purchase` | character-management.md + economy | покупка дополнительных слотов |
| P1 | `GET /api/v1/players/{accountId}/activity` | character-management.md | аудит действий персонажей |
| P1 | `POST /api/v1/gameplay/progression/experience` | progression-backend.md | начисление опыта (source, amount) |
| P1 | `POST /api/v1/gameplay/progression/skills` | progression-backend.md | обновление опыта навыков |
| P1 | `POST /api/v1/gameplay/progression/attributes` | progression-backend.md | распределение атрибутов |
| P2 | `GET /api/v1/gameplay/progression/{characterId}` | progression-backend.md | текущее состояние прогрессии |
| P2 | `POST /api/v1/auth/roles/{roleId}/permissions` | auth-authorization-security.md | управление permissions (админ) |

---

## WebSocket & Streaming

| Канал | Источник | Payload |
| --- | --- | --- |
| `wss://api.necp.game/v1/auth/sessions/{accountId}` | auth-service | статусы сессий, события входа/выхода |
| `wss://api.necp.game/v1/characters/{accountId}` | character-service | изменения списка персонажей, слоты, активный персонаж |
| `wss://api.necp.game/v1/progression/{characterId}` | gameplay-service | level-up, skill-up, начисление атрибутов |

---

## Event Bus

| Topic | Producer | Consumer | Поля |
| --- | --- | --- | --- |
| `ACCOUNT_CREATED` | auth-service | character-service, notification-service, analytics-service | `accountId`, `method`, `timestamp` |
| `LOGIN_SUCCESS` | auth-service | session-service, analytics-service | `accountId`, `ip`, `device`, `timestamp` |
| `LOGOUT` | auth-service | session-service | `accountId`, `sessionId`, `timestamp` |
| `CharacterCreated` | character-service | gameplay-service, inventory-service, analytics | `accountId`, `characterId`, `origin`, `class` |
| `CharacterDeleted` | character-service | analytics-service, notification-service | `characterId`, `deletedAt`, `canRestoreUntil` |
| `CharacterSwitched` | character-service | session-service, gameplay-service | `accountId`, `oldCharacterId`, `newCharacterId`, `timestamp` |
| `character:level-up` | gameplay-service (progression) | analytics-service, achievement-service, notification-service | `characterId`, `newLevel`, `reward` |
| `character:skill-leveled` | gameplay-service | analytics-service, quest-engine | `characterId`, `skill`, `level` |

---

## Зависимости и этапы

1. **Этап A — Auth Core (P0):** регистрация, login/logout, refresh, password reset.
2. **Этап B — Characters (P1):** CRUD персонажей, слоты, переключение, аудит, события Character*.
3. **Этап C — Progression (P1/P2):** начисление опыта, навыков, атрибутов, вебсокет прогрессии.
4. **Shared:** интеграция с session-service (валидировать токены, обновлять активного персонажа), analytics-service (логировать авторизации/прогресс), notification-service (email/sms), economy-service (слоты).

---

## Безопасность, хранение и дополнительные требования (2025-11-09 13:55)
- **JWT/Refresh токены:** хранение в `account_sessions` (`sessionId`, `refreshTokenHash`, `device`, `ip`, `expiresAt`); требуется единый TTL (1d/30d) и принудительная инвалидация через `LOGOUT`.
- **OAuth провайдеры:** таблица `account_oauth` (provider, providerAccountId, scope, linkedAt); endpoints обязаны проверять привязку и дубли.
- **Парольная политика:** `auth-database-registration.md` описывает сложность (12+ символов, символы разных групп) и историю паролей. API `password/reset` должен сохранять запись в `account_security_audit`.
- **Role/Permission хранение:** `auth-authorization-security.md` вводит таблицы `roles`, `role_permissions`, `account_roles`; REST `GET /auth/roles` и `POST /auth/roles/{roleId}/permissions` работают только для admin роли, события `ROLE_UPDATED`.
- **Character slots:** `character_slots` содержит `slotType`, `limit`, `purchasedWith`; покупка слота должна публиковать событие `CharacterSlotPurchased` (учесть economy-service).
- **Progression snapshots:** `character_progression`, `skill_experience`, `attribute_allocations` + `character_state_snapshots` для отката. Все апдейты должны логироваться в `progression_audit`.
- **Антифрод входов:** `LOGIN_SUCCESS` дополняется полями risk score, требуются интеграции с security-service для анализа IP/Device fingerprint.
- **GDPR/Удаление:** soft-delete аккаунта вызывает каскадное помечание персонажей и прогрессии, хранится предел восстановления (`accounts.deleted_until`).

---

## Следующие действия
- Подготовить черновик задач ДУАПИТАСК на базе REST/WS/EventBus backlog (Auth, Characters, Progression — волнами A→B→C).
- Проверить согласование с `combat wave` и `quest engine` (использование shooter-проверок и событий прогрессии).
- Наметить обновление `implementation-tracker.yaml` после открытия слота для auth/characters/progression задач.
- Отражать прогресс в `ready.md`, `readiness-tracker.yaml`, `current-status.md` и `TODO.md`.

---

## История
- 2025-11-09 01:24 — создана сводка и backlog для Auth/Characters/Progression.
- 2025-11-09 13:55 — добавлены требования к безопасности, хранению и событиям (JWT, OAuth, слоты, прогрессия) для постановки задач.
