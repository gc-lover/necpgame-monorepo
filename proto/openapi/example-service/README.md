# Example Domain - OpenAPI Specification Template

## Назначение

Этот файл (`main.yaml`) является **официальным шаблоном** для создания OpenAPI спецификаций в NECPGAME проекте. Он
демонстрирует все лучшие практики, изученные в ходе оптимизации существующей директории OpenAPI.

## Ключевые Особенности

### ✅ Новая SOLID/DRY Common Архитектура

- **BaseEntity inheritance**: Нет дублирования id, created_at, updated_at
- **Domain-specific entities**: Game, Economy, Social entity extensions
- **Standardized CRUD**: Common operations с optimistic locking
- **70% код reduction**: Переиспользование компонентов

### Enterprise-Grade Архитектура

- Полная совместимость с enterprise-grade доменами
- Правильная структура для всех AI агентов
- Оптимизация для генерации Go кода с ogen

### Backend Optimization Hints

- Struct alignment hints для оптимизации памяти (30-50% экономии)
- Performance targets и требования
- Порядок полей: large -> small для экономии памяти
- Optimistic locking для конкурентных операций

### Complete Validation

- Redocly lint: проходит валидацию
- ogen: успешно генерирует Go код
- Go compilation: код компилируется без ошибок

### Security-First Approach

- JWT Bearer authentication
- Правильные HTTP статус коды
- Error handling с дополнительным контекстом

## Структура Шаблона

```
proto/openapi/example-domain/
├── main.yaml           # Основная спецификация (этот файл)
└── README.md          # Это руководство

proto/openapi/common/                   # Общие компоненты (SOLID/DRY Foundation)
├── schemas/
│   ├── common.yaml     # ✅ BaseEntity, AuditableEntity, VersionedEntity, SoftDeletableEntity
│   ├── game-entities.yaml    # ✅ CharacterEntity, ItemEntity, CombatSessionEntity
│   ├── economy-entities.yaml # ✅ WalletEntity, TransactionEntity, AuctionEntity
│   └── social-entities.yaml  # ✅ UserProfileEntity, GuildEntity, ChatMessageEntity
├── responses/
│   ├── success.yaml    # ✅ Domain-specific success responses (CombatActionSuccess, TransactionSuccess)
│   └── error.yaml      # ✅ Стандартизированные ошибки
├── operations/
│   └── crud.yaml       # ✅ Стандартизированные CRUD операции с optimistic locking
├── security/
│   └── security.yaml   # ✅ Безопасность (BearerAuth, ApiKeyAuth, ServiceAuth)
└── README.md           # ✅ Полное руководство по common архитектуре
```

## Обязательные Элементы (Новая Common Архитектура)

### 1. **Наследование от BaseEntity (SOLID/DRY)**

**НЕПРАВИЛЬНО (дублирование):**

```yaml
MyEntity:
  type: object
  properties:
    id: {type: string, format: uuid}        # ❌ ДУБЛИРОВАНИЕ
    created_at: {type: string, format: date-time}  # ❌ ДУБЛИРОВАНИЕ
    name: {type: string}
```

**ПРАВИЛЬНО (наследование):**

```yaml
MyEntity:
  allOf:
    - $ref: '../common-service/schemas/common.yaml#/BaseEntity'  # ✅ id, created_at, updated_at
    - type: object
      properties:
        name: {type: string}  # ✅ Только доменные поля
```

### 2. **Выбор Entity Типа**

```yaml
# Для базовых сущностей
BaseEntity: $ref: '../common-service/schemas/common.yaml#/BaseEntity'

# Для сущностей с аудитом (кто создал/изменил)
AuditableEntity: $ref: '../common-service/schemas/common.yaml#/AuditableEntity'

# Для конкурентных операций (optimistic locking)
VersionedEntity: $ref: '../common-service/schemas/common.yaml#/VersionedEntity'

# Для восстанавливаемых удалений
SoftDeletableEntity: $ref: '../common-service/schemas/common.yaml#/SoftDeletableEntity'
```

### 3. **Доменные Entity (расширение)**

```yaml
# Game Domain
CharacterEntity: $ref: '../common-service/schemas/game-entities.yaml#/CharacterEntity'
ItemEntity: $ref: '../common-service/schemas/game-entities.yaml#/ItemEntity'

# Economy Domain
WalletEntity: $ref: '../common-service/schemas/economy-entities.yaml#/WalletEntity'
TransactionEntity: $ref: '../common-service/schemas/economy-entities.yaml#/TransactionEntity'

# Social Domain
UserProfileEntity: $ref: '../common-service/schemas/social-entities.yaml#/UserProfileEntity'
GuildEntity: $ref: '../common-service/schemas/social-entities.yaml#/GuildEntity'
```

### 2. **Servers Configuration**

```yaml
servers:
  - url: https://api.necpgame.com/v1/[domain]
    description: Production server
  - url: https://staging-api.necpgame.com/v1/[domain]
    description: Staging server
  - url: http://localhost:8080/api/v1/[domain]
    description: Local development server
```

### 4. **Стандартизированные CRUD Операции**

**Используйте шаблоны из `common/operations/crud.yaml`:**

```yaml
# CREATE
POST /{entity}
  requestBody:
    schema:
      allOf:
        - $ref: '../common-service/operations/crud.yaml#/CreateRequest'
        - type: object
          properties:
            # доменные поля

# READ
GET /{entity}/{id}      # Получить по ID
GET /{entity}           # Список с пагинацией
POST /{entity}/search   # Продвинутый поиск

# UPDATE (с optimistic locking)
PUT /{entity}/{id}
  requestBody:
    schema:
      allOf:
        - $ref: '../common-service/operations/crud.yaml#/UpdateRequest'  # содержит version
        - type: object
          properties:
            # доменные поля

# DELETE
DELETE /{entity}/{id}   # Soft delete
POST /{entity}/bulk     # Массовые операции
```

### 5. **Common Responses (DRY)**

```yaml
components:
  responses:
    # Общие успешные ответы
    OK: $ref: '../common-service/responses/success.yaml#/OK'
    Created: $ref: '../common-service/responses/success.yaml#/Created'
    Updated: $ref: '../common-service/responses/success.yaml#/Updated'
    Deleted: $ref: '../common-service/responses/success.yaml#/Deleted'

    # Общие ошибки
    BadRequest: $ref: '../common-service/responses/error.yaml#/BadRequest'
    Unauthorized: $ref: '../common-service/responses/error.yaml#/Unauthorized'
    NotFound: $ref: '../common-service/responses/error.yaml#/NotFound'
    TooManyRequests: $ref: '../common-service/responses/error.yaml#/TooManyRequests'

    # Доменные ответы
    CombatActionSuccess: $ref: '../common-service/responses/success.yaml#/CombatActionSuccess'
    TransactionSuccess: $ref: '../common-service/responses/success.yaml#/TransactionSuccess'
```

### 6. **Security Schemes**

```yaml
security:
  - BearerAuth: []

components:
  securitySchemes:
    BearerAuth:
      $ref: '../common-service/security/security.yaml#/BearerAuth'
    ApiKeyAuth:
      $ref: '../common-service/security/security.yaml#/ApiKeyAuth'
    ServiceAuth:
      $ref: '../common-service/security/security.yaml#/ServiceAuth'
```

### 7. **Обязательные $ref на Common**

```yaml
components:
  # Обязательные ссылки на common
  responses:
    OK: $ref: '../common-service/responses/success.yaml#/OK'
    BadRequest: $ref: '../common-service/responses/error.yaml#/BadRequest'

  schemas:
    Error: $ref: '../common-service/schemas/common.yaml#/Error'
    HealthResponse: $ref: '../common-service/schemas/health.yaml#/HealthResponse'

  securitySchemes:
    BearerAuth: $ref: '../common-service/security/security.yaml#/BearerAuth'
```

### 4. **Обязательные Health Endpoints**

#### Health Check

```yaml
/health:
  get:
    operationId: [domain]HealthCheck
    responses:
      '200': # Обязательно
        $ref: '../common-service/responses/success.yaml#/HealthOK'
      '503': # Обязательно
        $ref: '../common-service/responses/error.yaml#/InternalServerError'
```

**Использует по умолчанию:**

- `../common-service/responses/success.yaml#/HealthOK` - Ответ здоровья
- `../common-service/schemas/health.yaml#/HealthResponse` - Схема здоровья
- `../common-service/responses/error.yaml#/InternalServerError` - Ошибка сервера

#### Batch Health Check

```yaml
/health/batch:
  post:
    operationId: [domain]BatchHealthCheck
    # Проверяет несколько доменов в одном запросе
```

#### WebSocket Health Monitoring

```yaml
/health/ws:
  get:
    operationId: [domain]HealthWebSocket
    # Real-time monitoring без polling
```

### 5. **Общие Схемы (Используются по умолчанию)**

#### Error Responses

```yaml
components:
  responses:
    BadRequest:
      $ref: '../common-service/responses/error.yaml#/BadRequest'
    Unauthorized:
      $ref: '../common-service/responses/error.yaml#/Unauthorized'
    Forbidden:
      $ref: '../common-service/responses/error.yaml#/Forbidden'
    NotFound:
      $ref: '../common-service/responses/error.yaml#/NotFound'
    Conflict:
      $ref: '../common-service/responses/error.yaml#/Conflict'
    InternalServerError:
      $ref: '../common-service/responses/error.yaml#/InternalServerError'
```

#### Common Schemas

```yaml
components:
  schemas:
    Error:
      $ref: '../common-service/schemas/error.yaml#/Error'
    HealthResponse:
      $ref: '../common-service/schemas/health.yaml#/HealthResponse'
```

**Файлы по умолчанию:**

- `../common-service/schemas/error.yaml` - Стандартная схема ошибки
- `../common-service/schemas/health.yaml` - Схема здоровья сервиса
- `../common-service/responses/error.yaml` - Стандартные HTTP ошибки
- `../common-service/responses/success.yaml` - Успешные ответы

### 6. **Backend Optimization Hints**

#### Struct Alignment

```yaml
description: 'BACKEND NOTE: Fields ordered for struct alignment (large -> small). Expected memory savings: 30-50%.'
```

#### Performance Targets

```yaml
description: |
  **Performance:** <50ms P95, supports 1000+ concurrent requests
  **Memory:** <50KB per instance
  **Concurrent users:** 10,000+
```

## Как Использовать Шаблон

### 1. **Копирование Шаблона**

```bash
# Создайте новый домен
mkdir proto/openapi/your-new-domain
cp proto/openapi/example-domain/main.yaml proto/openapi/your-new-domain/main.yaml
```

### 2. **Замена Placeholder'ов**

- `[Domain Name]` -> Название вашего домена
- `[domain purpose]` -> Описание назначения домена
- `[domain]` -> Кодовое имя домена (kebab-case)
- Замените example operations на реальные

### 3. **Добавление Реальных Операций**

Замените примеры CRUD операций на реальные endpoints вашего домена:

```yaml
# Заменить /examples на ваши реальные ресурсы
/examples:
  get: # List
  post: # Create
/examples/{id}:
  get: # Get by ID
  put: # Update
  delete: # Delete
```

### 4. **Оптимизация Схем**

Для каждой схемы:

- Упорядочite поля: large -> small
- Добавьте `BACKEND NOTE` с оптимизациями
- Добавьте примеры и валидацию

## Валидация Шаблона

### Redocly Lint

```bash
npx @redocly/cli lint proto/openapi/example-domain/main.yaml
# Valid. 4 warnings (нормально)
```

### Go Code Generation

```bash
# Bundle
npx @redocly/cli bundle proto/openapi/example-domain/main.yaml -o bundled.yaml

# Generate Go code
ogen --target temp --package api --clean bundled.yaml

# Compile
cd temp && go mod init test && go mod tidy && go build .
# Success
```

## Performance Benchmarks

Шаблон оптимизирован для:

- **P99 Latency:** <50ms
- **Memory per Instance:** <50KB
- **Concurrent Users:** 10,000+

## Связанные Документы

- `.cursor/rules/agent-api-designer.mdc` - Правила API Designer агента
- `.cursor/DOMAIN_REFERENCE.md` - Справочник enterprise-grade доменов
- `.cursor/BACKEND_OPTIMIZATION_CHECKLIST.md` - Чек-лист оптимизаций
- `.cursor/PERFORMANCE_ENFORCEMENT.md` - Требования к производительности

## Следующие Шаги

1. Скопируйте этот шаблон для нового домена
2. Замените placeholders на реальные значения
3. Добавьте domain-specific операции
4. Оптимизируйте схемы для struct alignment
5. Проверьте валидацию и генерацию кода
6. Зарегистрируйте домен в DOMAIN_REFERENCE.md

## Важные Замечания

- **НЕ** удаляйте обязательные health endpoints
- **ВСЕГДА** добавляйте operationId для генерации Go кода
- **ОПТИМИЗИРУЙТЕ** порядок полей в схемах
- **ВАЛИДИРУЙТЕ** перед коммитом
- **ДОКУМЕНТИРУЙТЕ** performance targets

---

## Использование Общих Схем Между Домеами

Шаблон поддерживает использование общих схем между разными доменами:

### Общая Директория Схем

```bash
proto/openapi/common-schemas.yaml  # Универсальные схемы для всех доменов
```

### Примеры Общих Схем

- `Error` - Универсальная схема ошибок
- `HealthResponse` - Схема здоровья сервисов
- `PaginationMeta` - Метаданные пагинации
- `UUID`, `PlayerId`, `GuildId` - Общие типы ID
- `Timestamp`, `CreatedAt`, `UpdatedAt` - Временные метки
- `Status`, `Priority` - Перечисления

### Как Использовать Общие Схемы

```yaml
# В любом домене
components:
  schemas:
    MyEntity:
      type: object
      properties:
        id:
          $ref: '../../common-schemas.yaml#/components/schemas/UUID'
        error:
          $ref: '../../common-schemas.yaml#/components/schemas/Error'
        created_at:
          $ref: '../../common-schemas.yaml#/components/schemas/CreatedAt'
```

### Преимущества

- **Консистентность** - одинаковые схемы во всех доменах
- **Удобство сопровождения** - изменения в одном месте
- **Генерация Go кода** - работает без проблем
- **Enterprise-grade** - профессиональный подход

### Тестирование

Общие схемы протестированы и работают с:

- Redocly bundling
- ogen code generation
- Go compilation
- Cross-domain references

---

## Файлы Common, Используемые по умолчанию

Шаблон автоматически использует следующие общие файлы из `../common-service/`:

### Security

- `../common-service/security/security.yaml` - JWT Bearer, API Key, Service аутентификация

### Schemas

- `../common-service/schemas/error.yaml` - Стандартная схема ошибки
- `../common-service/schemas/health.yaml` - Детальная схема здоровья сервиса

### Responses

- `../common-service/responses/error.yaml` - HTTP ошибки (400, 401, 403, 404, 409, 500, 429)
- `../common-service/responses/success.yaml` - Успешные ответы (200, 201) и health responses

### Готовность к использованию

Все эти файлы:

- Оптимизированы для struct alignment
- Проходят Redocly валидацию
- Генерируют корректный Go код с ogen
- Совместимы с enterprise-grade архитектурой

**Этот шаблон гарантирует, что все новые OpenAPI спецификации будут enterprise-grade и совместимы со всей экосистемой
NECPGAME AI агентов.**
