# Task ID: API-TASK-099
**Тип:** API Generation  
**Приоритет:** critical  
**Статус:** completed  
**Создано:** 2025-11-09 17:50  
**Завершено:** 2025-11-09 20:05  
**Исполнитель:** АПИТАСК

---

## 📋 Краткое описание

Спецификация `auth-service` для ядра аутентификации и авторизации: регистрация, login/logout, refresh, password recovery, email verify, 2FA, OAuth и управление ролями.

---

## ✅ Выполнено

- Создан основной контракт `auth-core.yaml` (≤ 400 строк) с полным набором публичных и защищённых эндпоинтов.
- Подготовлены файлы моделей:
  - `auth-core-models.yaml` — базовые сущности (AccountProfile, TokenPair, JWT descriptor, 2FA setup, permissions).
  - `auth-core-models-operations.yaml` — запросы/ответы, Event payloadы `auth.account.created`, `auth.login.success`, `auth.logout`, `auth.password.changed`.
- Добавлен `README.md` для структуры каталога.
- Примеры охватывают регистрацию, login с 2FA, refresh, password reset, OAuth callback и назначение ролей.
- Зафиксированы rate-limit поля, lockout responses, интеграции с session-service, email-service, Redis и Kafka.
- Валидация `validate-swagger.ps1` выполнена без ошибок.

---

## 🔗 Спецификации

- `api/v1/auth/auth-core/auth-core.yaml`
- `api/v1/auth/auth-core/auth-core-models.yaml`
- `api/v1/auth/auth-core/auth-core-models-operations.yaml`

---

## 🧾 Источники

- `.BRAIN/05-technical/backend/auth/README.md` v1.0.1
- `.BRAIN/05-technical/backend/auth/auth-database-registration.md`
- `.BRAIN/05-technical/backend/auth/auth-login-jwt.md`
- `.BRAIN/05-technical/backend/auth/auth-authorization-security.md`
- `.BRAIN/05-technical/backend/session-management-system.md`
- `.BRAIN/05-technical/backend/email-service.md`

---

## 📈 Передано

- Auth Service (core реализация)
- Session Service (создание/закрытие сессий)
- Email Service (верификации и reset письма)
- Frontend Agent (модуль `modules/auth`, Orval клиент `@api/auth`)

