# Task ID: API-TASK-026
**Тип:** API Generation | **Приоритет:** высокий | **Статус:** queued
**Создано:** 2025-01-27 12:00 | **Создатель:** AI Agent (API Task Creator) | **Зависимости:** none

---

## 📚 Источник

**Документ:** `.BRAIN/05-technical/ui-character-creation.md`  
**Версия:** v1.1.0  
**Статус:** review (детализация завершена)  
**Ключевые механики:**
- регистрация и вход аккаунта игрока
- управление списком персонажей (список, создание, удаление)
- валидация лимитов (3-5 персонажей) и бизнес-правил
- справочники классов, происхождений, фракций, городов
- требования к безопасности и проверке уникальности

---

## 🏗️ Целевая архитектура

### Backend микросервисы

1. **auth-service (port 8081)**
   - Domain: `auth`
   - Base Path: `/api/v1/auth`
   - Target Directory: `api/v1/auth/onboarding/`
   - Главный файл: `auth-sessions.yaml`
   - package: `com.necpgame.authservice`
   - Ответственность: регистрация, аутентификация, управление refresh-токенами, блокировки, базовые пользовательские данные.
   - info.x-microservice (использовать строго):
     ```yaml
     info:
       x-microservice:
         name: auth-service
         port: 8081
         domain: auth
         base-path: /api/v1/auth
         directory: api/v1/auth/onboarding
         package: com.necpgame.authservice
     ```
   - Storage: `auth_schema.accounts`, `auth_schema.refresh_tokens`.

2. **character-service (port 8082)**
   - Domain: `characters`
   - Base Path: `/api/v1/characters`
   - Target Directory: `api/v1/characters/onboarding/`
   - Главный файл: `character-roster.yaml`
   - package: `com.necpgame.characterservice`
   - Ответственность: управление персонажами аккаунта, контроль лимитов, внешность, справочники классов и происхождений, связи с фракциями и городами.
   - info.x-microservice:
     ```yaml
     info:
       x-microservice:
         name: character-service
         port: 8082
         domain: characters
         base-path: /api/v1/characters
         directory: api/v1/characters/onboarding
         package: com.necpgame.characterservice
     ```
   - Storage: `characters_schema.characters`, `characters_schema.character_classes`, `world_schema.factions`, `world_schema.cities`.

### Межсервисные коммуникации

- character-service использует Feign клиент `auth-service.validateToken(token)` для проверки сессии игрока.
- При успешной регистрации auth-service публикует событие `account:created`, которое character-service обрабатывает для инициализации слотов персонажей.
- Все события идут через Kafka-шину `accounts-topic` / `characters-topic`.

### Frontend интеграция

- **modules/auth/onboarding**
  - API client: Orval из `api/v1/auth/onboarding/auth-sessions.yaml`
  - UI: `@shared/ui` → `AuthCard`, `FormInput`, `CyberpunkButton`
  - Forms: `@shared/forms` → `AuthLoginForm`, `AuthRegisterForm`
  - State: `useAuthStore` (`session`, `registerForm`, `loginStatus`)

- **modules/characters/onboarding**
  - API client: Orval из `api/v1/characters/onboarding/character-roster.yaml`
  - UI: `@shared/ui` → `CharacterCard`, `CharacterSlot`, `ModalConfirm`
  - Forms: `@shared/forms` → `CharacterCreateForm`
  - State: `useCharactersStore` (`roster`, `limits`, `selectedCharacter`)

### Общие требования к OpenAPI

- `openapi: 3.0.3`
- `servers`:
  ```yaml
  servers:
    - url: https://api.necp.game/v1/auth
      description: Production API Gateway
    - url: http://localhost:8080/api/v1/auth
      description: Local API Gateway
  ```
  Для character-service домен заменить на `characters`.
- Использовать общие компоненты:
  - `$ref: ../../shared/common/responses.yaml#/components/responses/*`
  - `$ref: ../../shared/common/security.yaml#/components/securitySchemes/BearerAuth`
  - `$ref: ../../shared/common/pagination.yaml#/components/schemas/*` при необходимости.
- Все ошибки возвращают общую схему `Error`.
- Размер каждого файла ≤ 400 строк. Схемы можно вынести в `*-models.yaml` и `*-requests.yaml` (тот же каталог).

---

## 📁 Целевые файлы

1. `api/v1/auth/onboarding/auth-sessions.yaml` — paths, security, ссылки на components.
2. `api/v1/auth/onboarding/auth-sessions-requests.yaml` — запросы регистрации и логина (schemas).
3. `api/v1/characters/onboarding/character-roster.yaml` — paths для персонажей и справочников.
4. `api/v1/characters/onboarding/character-roster-models.yaml` — схемы персонажа и связанных справочников.
5. `api/v1/characters/onboarding/README.md` — краткое описание структуры (≤ 200 строк).

Разрешено добавлять дополнительные части (`_0001`) если превысим лимиты.

---

## 📌 API Endpoints

### auth-service — `auth-sessions.yaml`

1. **POST /api/v1/auth/register**
   - Request: `RegisterRequest`
   - Валидации: email по RFC, username 3-20 символов, password ≥ 8, подтверждение пароля, согласие с условиями.
   - Responses: `201` (`AccountSummary`), `400`, `409`, `422`.
   - Паблиш событие `account:created` с полезной нагрузкой `{ accountId, email, createdAt }`.

2. **POST /api/v1/auth/login**
   - Request: `LoginRequest`
   - Responses: `200` (`LoginResponse`), `401`, `423` (аккаунт заблокирован), стандартные ошибки.

3. **POST /api/v1/auth/logout**
   - Headers: `Authorization`
   - Responses: `204`, `401`.

4. **POST /api/v1/auth/refresh**
   - Request: `RefreshRequest`
   - Responses: `200` (`LoginResponse`), `401`.

5. **POST /api/v1/auth/password/forgot**
   - Request: `ForgotPasswordRequest`
   - Responses: `202`, `404`.

6. **POST /api/v1/auth/password/reset**
   - Request: `ResetPasswordRequest`
   - Responses: `200`, `400`, `401`.

### character-service — `character-roster.yaml`

1. **GET /api/v1/characters**
   - Headers: `Authorization`
   - Query: `includeInactive` (boolean)
   - Responses: `200` (`CharacterSummaryPage`), `401`.

2. **POST /api/v1/characters**
   - Headers: `Authorization`
   - Request: `CreateCharacterRequest`
   - Responses: `201` (`CharacterDetail`), `400`, `403` (limit reached), `409`.

3. **GET /api/v1/characters/{characterId}**
   - Responses: `200` (`CharacterDetail`), `404`.

4. **DELETE /api/v1/characters/{characterId}**
   - Responses: `204`, `404`, `403` (belongs to another account).

5. **PATCH /api/v1/characters/{characterId}/appearance**
   - Request: `UpdateAppearanceRequest`
   - Responses: `200`, `400`, `404`.

6. **GET /api/v1/characters/classes**
   - Responses: `200` (`CharacterClassList`).

7. **GET /api/v1/characters/origins**
   - Query: `factionId`
   - Responses: `200` (`OriginList`).

8. **GET /api/v1/characters/factions**
   - Query: `origin`
   - Responses: `200` (`FactionList`).

9. **GET /api/v1/characters/cities**
   - Query: `factionId`, `region`
   - Responses: `200` (`CityList`).

10. **GET /api/v1/characters/limits**
    - Headers: `Authorization`
    - Responses: `200` (`CharacterLimits`), `401`.

Все endpoints character-service требуют Bearer токен и валидацию лимитов из auth-service.

---

## 📦 Ключевые схемы

### Auth-service

- `RegisterRequest`
- `LoginRequest`
- `RefreshRequest`
- `ForgotPasswordRequest`
- `ResetPasswordRequest`
- `LoginResponse` (token, refreshToken, expiresAt, account)
- `AccountSummary` (id, email, username, createdAt, status)

### Character-service

- `CharacterSummary` (id, name, classId, level, faction, city, lastLogin)
- `CharacterDetail` (summary + origin, gender, appearance, slots, createdAt)
- `Appearance`
- `CreateCharacterRequest`
- `UpdateAppearanceRequest`
- `CharacterLimits`
- `CharacterClass`
- `CharacterOrigin`
- `Faction`
- `City`
- Общие структуры пагинации (`PageMeta`, `PageLinks`) через `shared/common/pagination.yaml`.

Все поля снабдить форматами (`uuid`, `email`, `date-time`) и enum-ограничениями согласно документу `.BRAIN`.

---

## ✅ Acceptance Criteria

- Для каждого файла заполнен блок `info.x-microservice` с допустимыми значениями (`auth-service`, `character-service`).
- `servers` использует единый домен `https://api.necp.game/v1` и gateway `http://localhost:8080/api/v1` с корректным доменом (`auth`, `characters`).
- Paths описаны только в главных файлах (`auth-sessions.yaml`, `character-roster.yaml`); схемы вынесены в отдельные файлы при необходимости.
- Все стандартные ответы подключены из `shared/common/responses.yaml`; ошибки не дублируются.
- Запросы и ответы содержат примеры.
- Ограничения из `.BRAIN` отражены: валидация имени персонажа, лимит по аккаунту, соответствие происхождения и фракций, фильтрация городов.
- Реализованы коммуникации: событие `account:created`, Feign клиент для проверки токена, обработка лимитов в character-service.
- В каталогах нет TODO и избыточных комментариев.
- Подготовлен `README.md` в `api/v1/characters/onboarding/` с описанием структуры файлов.
- После генерации спецификаций обновлены `brain-mapping_0001.yaml` и `tasks/config/implementation-tracker.yaml` (status → completed, backend/frontend → not_started).

---

## 🛠️ Чеклист исполнителя

1. Прочитать `.BRAIN/05-technical/ui-character-creation.md` и зафиксировать все бизнес-правила.
2. Создать или обновить OpenAPI файлы в целевых директориях.
3. Внести блоки `info.x-microservice` и `servers` с корректными значениями.
4. Подключить общие компоненты (`responses`, `security`, `pagination`) через `$ref`.
5. Добавить примеры для каждого запроса и ответа.
6. Запустить `pwsh -NoProfile -File .\scripts\validate-swagger.ps1 -ApiDirectory api/v1/auth/onboarding/` и аналогичную команду для `api/v1/characters/onboarding/`.
7. Обновить `tasks/config/brain-mapping_0001.yaml` (status: completed, ссылки на спецификации).
8. Добавить записи в `tasks/config/implementation-tracker.yaml` для обоих микросервисов (api_path = `api/v1/auth/onboarding/` и `api/v1/characters/onboarding/`, backend/frontend = `not_started`).

