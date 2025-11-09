# API Generation Task Template

> Обязательно сверяйся с `GLOBAL-RULES.md`, `API-SWAGGER/CORE.md`, `tasks/config/task-creation-guide.md` и `tasks/config/checklist.md`.  
> Размер задания ≤ 500 строк. При превышении создавай продолжения (`task-XXX-description_0001.md`).

## Metadata
- **Task ID:** API-TASK-099  
- **Type:** API Generation  
- **Priority:** critical  
- **Status:** queued  
- **Created:** 2025-11-09 17:50  
- **Author:** API Task Creator Agent  
- **Dependencies:** none  

## Summary
Подготовить спецификацию `api/v1/auth/auth-core.yaml`, описывающую полный комплекс аутентификации и авторизации: регистрация, логин, JWT, refresh, OAuth, пароль/2FA, управление аккаунтом и роли/permissions, чтобы auth-service обеспечивал вход в игру и защиту всех остальных сервисов.

## Source Documents
| Поле | Значение |
| --- | --- |
| Repository | `.BRAIN` |
| Path | `.BRAIN/05-technical/backend/auth/README.md` |
| Version | v1.0.1 |
| Status | approved |
| API readiness | ready (2025-11-07 00:56) |

**Key points:** регистрация по email/password и OAuth с верификацией и role auto-assign; login с антибрут, lockout, Redis хранением refresh токенов; JWT структура (access 15 минут, refresh 7 дней); reset/forgot password поток; обязательный email verification; optional 2FA с backup кодами; роли PLAYER/MODERATOR/ADMIN/SUPER_ADMIN и permission matrix; события `ACCOUNT_CREATED`, `LOGIN_SUCCESS`, `LOGOUT`, `PASSWORD_CHANGED`.  
**Related docs:** `.BRAIN/05-technical/backend/auth/auth-database-registration.md`, `.BRAIN/05-technical/backend/auth/auth-login-jwt.md`, `.BRAIN/05-technical/backend/auth/auth-authorization-security.md`, `.BRAIN/05-technical/backend/session-management-system.md`, `.BRAIN/05-technical/backend/player-character-mgmt/character-management.md`, `.BRAIN/05-technical/backend/email-service.md`.

## Target Architecture (⚠️ Обязательно)
- **Microservice:** auth-service  
- **Port:** 8081  
- **Domain:** auth  
- **API directory:** `api/v1/auth/auth-core.yaml`  
- **base-path:** `/api/v1/auth`  
- **Java package:** `com.necpgame.auth`  
- **Frontend module:** `modules/auth` (подмодули `modules/auth/register`, `modules/auth/login`, `modules/auth/account`)  
- **Shared UI/Form components:** `@shared/forms/AuthRegisterForm`, `@shared/forms/AuthLoginForm`, `@shared/forms/TwoFactorForm`, `@shared/forms/PasswordResetForm`, `@shared/ui/AuthStatusBanner`, `@shared/ui/OAuthProviderList`, `@shared/state/useAuthStore`.

> Все значения должны соответствовать таблице микросервисов в `GLOBAL-RULES.md`.

## Scope of Work
1. Проработать REST endpoints: регистрация, login/logout, refresh, forgot/reset, email verify/resend, 2FA enable/verify/disable, OAuth authorize/callback, account CRUD.
2. Описать модели данных и payload: `RegisterRequest`, `RegisterResponse`, `LoginRequest`, `LoginResponse`, `RefreshTokenRequest`, `PasswordResetRequest/Response`, `TwoFactorSetup`, `TwoFactorVerify`, `AccountProfile`.
3. Включить схемы ошибок и rate limits: блокировки после 5 неудачных логинов, 3 reset-заказа в час, 3 регистрации с IP в сутки.
4. Документировать события и интеграции: Kafka topics `auth.account.created`, `auth.login.success`, `auth.logout`, `auth.password.changed`; взаимодействие с session-service, email-service, redis, character-service.
5. Зафиксировать security: использование `api/v1/shared/common/security.yaml`, схемы OAuth2/PlayerSession, требования к заголовкам (`X-Client-Version`, `X-Session-Token`).
6. Добавить требования для фронтенда и Orval клиентов; подготовить примеры запросов/ответов, обновить маппинг и очередь, отметить задачу в `.BRAIN`.

## Endpoints
- `POST /register` — регистрация email/password, создание аккаунта, отправка верификации, присвоение роли PLAYER.
- `POST /login` — вход с проверкой блокировок, 2FA, возврат access/refresh и session token.
- `POST /logout` — инвалидировать refresh токен, закрыть сессию в session-service, зафиксировать событие.
- `POST /refresh` — выпуск нового access токена, валидация refresh из Redis.
- `POST /password/forgot` — выдать reset token и отправить письмо (idempotent ответ).
- `POST /password/reset` — сменить пароль по токену, инвалидировать refresh, аудит IP.
- `POST /password/change` — сменить пароль для авторизованного пользователя.
- `POST /verify-email` — подтвердить email по токену.
- `POST /verify-email/resend` — переотправка письма (rate limit).
- `POST /2fa/enable` — инициировать 2FA, вернуть QR/backup codes (временное хранение).
- `POST /2fa/verify` — подтвердить код и активировать 2FA.
- `POST /2fa/disable` — выключить 2FA после подтверждения.
- `GET /oauth/{provider}/authorize` — инициировать OAuth (redirect URL).
- `GET /oauth/{provider}/callback` — завершить OAuth, создать/найти аккаунт, вернуть токены.
- `GET /account` — профиль аккаунта, роли, security метки.
- `PUT /account` — обновить профиль (display name, username, настройки безопасности).
- `DELETE /account` — удалить/архивировать аккаунт (soft delete, события).
- `GET /roles` — список ролей/permissions (для модераторов).
- `POST /roles/{accountId}` — назначить роль (admin scope).

## Data Models
- `RegisterRequest`, `RegisterResponse`
- `LoginRequest` (email, password, optional 2fa code), `LoginResponse` (tokens, session, requiresTwoFactor)
- `RefreshTokenRequest`
- `ForgotPasswordRequest`, `ResetPasswordRequest`, `ChangePasswordRequest`
- `EmailVerificationRequest`, `EmailResendRequest`
- `TwoFactorSetupResponse`, `TwoFactorVerifyRequest`, `TwoFactorDisableRequest`
- `OAuthAuthorizeResponse`, `OAuthCallbackResponse`
- `AccountProfile`, `AccountRole`, `PermissionList`
- `LogoutRequest` с `sessionToken`, причинами
- Common error models: `InvalidCredentialsError`, `AccountLockedError`, `AccountBannedError`, `TwoFactorRequiredError`
- Использовать общие компоненты `api/v1/shared/common/responses.yaml`, `pagination.yaml` (для логов), `security.yaml`.

## Integrations & Events
- Kafka topics: `auth.account.created`, `auth.login.success`, `auth.logout`, `auth.password.changed`, payload включает `accountId`, `timestamp`, `metadata`.
- Session-service REST: `/api/v1/session/create`, `/api/v1/session/terminate`.
- Email-service REST: `/api/v1/notifications/email/verification`, `/email/password-reset`.
- Redis: хранение refresh токенов, 2FA setup, login attempts, rate-limit counters.
- Character-service: слушает `auth.account.created` для создания slots.
- Economy-service: слушает `auth.account.created` для wallets.
- Notification-service: слушает `auth.password.changed`, `auth.account.deleted`.
- Frontend: модуль `modules/auth`, Orval клиент `@api/auth`, состояние `useAuthStore`, компоненты для OAuth и 2FA.

## Acceptance Criteria
1. Создан файл `api/v1/auth/auth-core.yaml` ≤ 500 строк с OpenAPI 3.0.3, корректным `info.x-microservice` для auth-service.
2. Описаны все перечисленные endpoints с параметрами, запросами/ответами, примерами и кодами ошибок.
3. JWT схемы включают TTL, payload structure, security requirements; refresh flow учитывает Redis key storage и revoke.
4. Password reset/2FA endpoints используют rate-limit поля и аудит IP.
5. OAuth endpoints описаны с поддержкой провайдеров (enum: `google`, `discord`, `steam`, `twitch`).
6. Добавлены события Kafka с payload и перечислением потребителей.
7. В спецификации ссылки на `api/v1/shared/common/responses.yaml`, `security.yaml`; не дублируются стандартные ошибки.
8. Документированы требования к фронтенду: формы, компоненты, хуки, обработчики ошибок.
9. `tasks/config/brain-mapping.yaml` содержит запись `API-TASK-099`, статус `queued`, приоритет `critical`.
10. `.BRAIN/05-technical/backend/auth/README.md` обновлён секцией `API Tasks Status` с задачей.
11. `tasks/queues/queued.md` дополнен записью.
12. После реализации спецификации команда запускает `pwsh -NoProfile -File ..\scripts\validate-swagger.ps1 -ApiDirectory API-SWAGGER\api\v1\auth\`.

## FAQ / Notes
- **Почему отдельный файл `auth-core.yaml`?** Держим основную поверхность API в одном файле; OAuth/2FA можно вынести позже, если превысим лимит.
- **Нужны ли отдельные endpoints для модераторов?** `POST /roles/{accountId}` и дополнительные permission endpoints только для ADMIN/SUPER_ADMIN — отразить в security.
- **Как хранить login attempts?** Через Redis counter и поле `failed_login_attempts`; описать логику и ответ ошибки.
- **Что с удалением аккаунта?** Soft delete: статус `DELETED`, событие `auth.account.deleted`, последующий cleanup по расписанию.

## Change Log
- 2025-11-09 17:50 — Задание создано (API Task Creator Agent)


