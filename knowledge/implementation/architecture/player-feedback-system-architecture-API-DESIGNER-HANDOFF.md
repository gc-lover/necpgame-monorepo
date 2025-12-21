# Передача задачи API Designer

## Issue #1335: Система обратной связи от игроков

**Статус:** [OK] Архитектура готова, передано API Designer

**Дата передачи:** 2025-11-23

## Что нужно сделать API Designer

### 1. Создание OpenAPI спецификации

Создать детальную OpenAPI 3.0 спецификацию для всех API endpoints системы обратной связи.

#### 1.1. Endpoints для спецификации (8 endpoints):

1. **POST /api/v1/feedback/submit**
   - Создание нового обращения
   - Request body: тип, категория, заголовок, описание, приоритет, скриншоты, контекст игры
   - Response: ID обращения, статус, ссылка на GitHub Issue

2. **GET /api/v1/feedback/{id}**
   - Получение обращения по ID
   - Response: полная информация об обращении

3. **GET /api/v1/feedback/player/{player_id}**
   - История обращений игрока
   - Query params: status, type, limit, offset
   - Response: список обращений с пагинацией

4. **POST /api/v1/feedback/{id}/update-status**
   - Обновление статуса обращения
   - Request body: статус, комментарий (опционально)
   - Response: обновленный статус

5. **GET /api/v1/feedback/board**
   - Доска идей (публичные предложения)
   - Query params: category, status, sort, limit, offset, search
   - Response: список публичных предложений

6. **POST /api/v1/feedback/{id}/vote**
   - Голосование за предложение
   - Response: обновленный счетчик голосов

7. **DELETE /api/v1/feedback/{id}/vote**
   - Отзыв голоса
   - Response: обновленный счетчик голосов

8. **GET /api/v1/feedback/stats**
   - Статистика обращений (для админов)
   - Response: статистика по обращениям

#### 1.2. Схемы данных:

- **Feedback** - основная модель обращения
- **Vote** - модель голоса
- **Author** - модель автора
- **GameContext** - контекст игры (JSONB)
- **Error** - модель ошибки
- **Pagination** - модель пагинации

#### 1.3. Аутентификация:

- Session token для игроков
- JWT токены для агентов/админов
- Описание в Security Schemes

#### 1.4. Rate Limiting:

- Описание лимитов в документации
- Примеры HTTP 429 ответов

### 2. Детали для проработки

#### Request/Response схемы:
- Детальные схемы для всех endpoints
- Валидация полей (min/max length, required, enum)
- Примеры запросов и ответов
- Описание ошибок

#### Типы данных:
- Enum для типов обращений (feature_request, bug_report, wishlist, feedback)
- Enum для категорий (gameplay, balance, content, technical, lore, ui, other)
- Enum для статусов (pending_moderation, pending, in_review, assigned, in_progress, resolved, rejected, duplicate, merged)
- Enum для приоритетов (low, medium, high, critical)

#### Интеграции:
- Описание интеграции с GitHub API (в документации)
- Описание интеграции с session-service
- Описание интеграции с character-service

### 3. Связанные документы

- **Архитектура:** `knowledge/implementation/architecture/player-feedback-system-architecture.yaml`
- **Концепция:** `knowledge/analysis/tasks/ideas/2025-11-23-IDEA-player-feedback-system.yaml`
- **GitHub Issue:** #1335
- **Коммиты:**
  - `[architect] docs: добавить архитектуру системы обратной связи от игроков`
  - `[architect] chore: завершить архитектурную проработку, готово к передаче API Designer`

### 4. Решения, принятые на этапе Architect

1. [OK] Микросервисная архитектура с отдельным feedback-service
2. [OK] PostgreSQL для хранения данных
3. [OK] REST API для взаимодействия
4. [OK] GitHub API для создания Issues
5. [OK] 8 API endpoints для полной функциональности
6. [OK] 4 таблицы в БД (player_feedback, feedback_votes, feedback_authors, feedback_moderation_queue)

### 5. Следующие этапы после API Designer

1. **Database Engineer** - проектирование детальной БД схемы и миграций (может работать параллельно)
2. **Backend Developer** - реализация микросервиса по OpenAPI спецификации
3. **UE5 Developer** - реализация клиентской части
4. **Создание агента** - реализация `agent:player-feedback`

### 6. Расположение файла

OpenAPI спецификация должна быть создана в:
```
services/feedback-service-go/
├── api/
│   └── openapi.yaml  # OpenAPI 3.0 спецификация
```

Или в общем каталоге API спецификаций проекта (если есть).

---

**Примечание:** GitHub Issue #1335 будет обновлен с метками `agent:api-designer` и `stage:api-design` после снятия rate limit.

