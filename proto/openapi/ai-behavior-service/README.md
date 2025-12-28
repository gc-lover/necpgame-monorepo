# AI Behavior Service - OpenAPI Specification

## 📋 **Назначение**

AI Behavior Service предоставляет **enterprise-grade API для управления поведением ИИ** в NECPGAME.
Сервис отвечает за системы принятия решений NPC, адаптивное обучение ИИ и динамические взаимодействия в игровом мире.

## 🎯 **Функциональность**

- **Behavior Trees**: Сложные системы принятия решений для NPC ИИ
- **Adaptive Learning**: Системы ИИ, которые обучаются и адаптируются к паттернам игроков
- **Dynamic Responses**: Корректировки поведения в реальном времени на основе состояния игры
- **Personality Systems**: Уникальные личности ИИ и поведенческие черты
- **Group Coordination**: Координация мультиагентов и роевые поведения
- **Performance Optimized**: Высокопроизводительная обработка ИИ с <30ms P99 latency

## 📁 **Структура**

```
ai-behavior-service/
├── main.yaml           # Основная спецификация
├── README.md          # Эта документация
└── docs/
    └── index.html     # Сгенерированная документация
```

## 🔗 **Зависимости**

- **common**: Общие схемы и ответы
- **game-metrics-service**: Метрики производительности игры
- **procedural-generation-service**: Процедурная генерация контента

## 📊 **Performance**

- **P99 Latency**: <30ms для операций принятия решений ИИ
- **Memory per Instance**: <35KB на активную сущность ИИ
- **Concurrent Users**: 300,000+ одновременных операций ИИ
- **Decision Calculations**: <20ms время отклика

## 🚀 **Использование**

### Валидация
```bash
npx @redocly/cli lint main.yaml
```

### Генерация Go кода
```bash
ogen --target ../../services/ai-behavior-service-go/pkg/api \
     --package api --clean main.yaml
```

### Документация
```bash
npx @redocly/cli build-docs main.yaml -o docs/index.html
```

## Обязательные Элементы

### 1. **OpenAPI Header**

```yaml
openapi: 3.0.3
info:
  title: [Domain Name] API
  description: Enterprise-grade API for [domain purpose]
  version: "1.0.0"
  contact:
    name: NECPGAME API Support
    email: api@necpgame.com
  license:
    name: MIT
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

### 3. **Security Schemes**

```yaml
security:
  - BearerAuth: []

components:
  securitySchemes:
    BearerAuth:
      $ref: '../common/security/security.yaml#/BearerAuth'
    ApiKeyAuth:
      $ref: '../common/security/security.yaml#/ApiKeyAuth'
    ServiceAuth:
      $ref: '../common/security/security.yaml#/ServiceAuth'
```

**Использует по умолчанию:**
- `../common/security/security.yaml` - Bearer JWT, API Key и Service аутентификация

### 4. **Обязательные Health Endpoints**

#### Health Check

```yaml
/health:
  get:
    operationId: [domain]HealthCheck
    responses:
      '200': # Обязательно
        $ref: '../common/responses/success.yaml#/HealthOK'
      '503': # Обязательно
        $ref: '../common/responses/error.yaml#/InternalServerError'
```

**Использует по умолчанию:**
- `../common/responses/success.yaml#/HealthOK` - Ответ здоровья
- `../common/schemas/health.yaml#/HealthResponse` - Схема здоровья
- `../common/responses/error.yaml#/InternalServerError` - Ошибка сервера

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
      $ref: '../common/responses/error.yaml#/BadRequest'
    Unauthorized:
      $ref: '../common/responses/error.yaml#/Unauthorized'
    Forbidden:
      $ref: '../common/responses/error.yaml#/Forbidden'
    NotFound:
      $ref: '../common/responses/error.yaml#/NotFound'
    Conflict:
      $ref: '../common/responses/error.yaml#/Conflict'
    InternalServerError:
      $ref: '../common/responses/error.yaml#/InternalServerError'
```

#### Common Schemas
```yaml
components:
  schemas:
    Error:
      $ref: '../common/schemas/error.yaml#/Error'
    HealthResponse:
      $ref: '../common/schemas/health.yaml#/HealthResponse'
```

**Файлы по умолчанию:**
- `../common/schemas/error.yaml` - Стандартная схема ошибки
- `../common/schemas/health.yaml` - Схема здоровья сервиса
- `../common/responses/error.yaml` - Стандартные HTTP ошибки
- `../common/responses/success.yaml` - Успешные ответы

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

Шаблон автоматически использует следующие общие файлы из `../common/`:

### Security
- `../common/security/security.yaml` - JWT Bearer, API Key, Service аутентификация

### Schemas
- `../common/schemas/error.yaml` - Стандартная схема ошибки
- `../common/schemas/health.yaml` - Детальная схема здоровья сервиса

### Responses
- `../common/responses/error.yaml` - HTTP ошибки (400, 401, 403, 404, 409, 500, 429)
- `../common/responses/success.yaml` - Успешные ответы (200, 201) и health responses

### Готовность к использованию
Все эти файлы:
- Оптимизированы для struct alignment
- Проходят Redocly валидацию
- Генерируют корректный Go код с ogen
- Совместимы с enterprise-grade архитектурой

**Этот шаблон гарантирует, что все новые OpenAPI спецификации будут enterprise-grade и совместимы со всей экосистемой
NECPGAME AI агентов.**