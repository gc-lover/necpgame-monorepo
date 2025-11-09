# Authentication & Authorization System - Навигация

**api-readiness:** ready  
**api-readiness-check-date:** 2025-11-07
**api-readiness-notes:** Система аутентификации, реализована в auth-service

---
**API Tasks Status:**
- Status: completed
- Tasks:
  - API-TASK-099: Auth Core API — `api/v1/auth/auth-core/auth-core.yaml`
    - Создано: 2025-11-09 17:50
    - Завершено: 2025-11-09 20:05
    - Доп. файлы: `auth-core-models.yaml`, `auth-core-models-operations.yaml`, `README.md`
    - Файл задачи: `API-SWAGGER/tasks/completed/2025-11-09/task-099-auth-core-api.md`
- Last Updated: 2025-11-09 20:05
---

**Версия:** 1.0.1  
**Дата:** 2025-11-07  
**Статус:** approved

---

## Микросервисная архитектура

**Ответственный микросервис:** auth-service  
**Порт:** 8081  
**API Gateway маршрут:** `/api/v1/auth/*`  
**Статус:** ✅ Реализовано (Фаза 1)

---

## Описание

Полная система аутентификации и авторизации. Регистрация, login, JWT tokens, OAuth, password recovery, roles/permissions.

---

## Endpoints (через API Gateway)

Все запросы идут через API Gateway (http://localhost:8080):

1. POST `http://localhost:8080/api/v1/auth/register` - регистрация
2. POST `http://localhost:8080/api/v1/auth/login` - вход
3. POST `http://localhost:8080/api/v1/auth/logout` - выход
4. POST `http://localhost:8080/api/v1/auth/refresh` - обновление токена
5. POST `http://localhost:8080/api/v1/auth/password/forgot` - запрос сброса
6. POST `http://localhost:8080/api/v1/auth/password/reset` - сброс пароля
7. GET `http://localhost:8080/api/v1/auth/roles` - получение ролей
8. GET `http://localhost:8080/api/v1/auth/oauth/{provider}/authorize` - OAuth redirect
9. GET `http://localhost:8080/api/v1/auth/oauth/{provider}/callback` - OAuth callback

---

## Структура документов

### Part 1: Database & Registration
**Файл:** [auth-database-registration.md](./auth-database-registration.md)  
**Содержание:** БД схема, регистрация аккаунтов, OAuth

### Part 2: Login & JWT Management
**Файл:** [auth-login-jwt.md](./auth-login-jwt.md)  
**Содержание:** Login flow, JWT tokens, password recovery, 2FA

### Part 3: Authorization & Security
**Файл:** [auth-authorization-security.md](./auth-authorization-security.md)  
**Содержание:** Roles, Permissions, Security best practices

---

## Взаимодействие с другими микросервисами

### Предоставляет (Feign Client):

**character-service вызывает auth-service:**
```java
authClient.validateToken(token) // валидация JWT
authClient.getUserRoles(accountId) // получение ролей
```

**gameplay-service вызывает auth-service:**
```java
authClient.checkPermission(accountId, "gameplay.combat") // проверка прав
```

### Публикует события (Event Bus):

- `ACCOUNT_CREATED` → character-service создает slots
- `LOGIN_SUCCESS` → session-service создает session
- `LOGOUT` → session-service закрывает session
- `PASSWORD_CHANGED` → notification-service отправляет email

---

## Deployment

**Docker:**
```bash
docker-compose -f docker-compose-microservices.yml up auth-service
```

**Зависимости:**
- PostgreSQL (5433) - БД
- Eureka Server (8761) - service discovery
- Config Server (8888) - конфигурации

---

## Связанные документы

- [backend/README.md](../README.md) - распределение всех систем по микросервисам
- [microservices-overview.md](../../microservices-overview.md) - общий обзор микросервисов
- [БЭКТАСК-MICROSERVICES.md](../../../../BACK-GO/docs/БЭКТАСК-MICROSERVICES.md) - руководство

---

## История изменений

- v1.0.1 (2025-11-07) - Добавлена информация о микросервисной архитектуре
- v1.0.0 (2025-11-07 01:46) - Разбит на 3 части

