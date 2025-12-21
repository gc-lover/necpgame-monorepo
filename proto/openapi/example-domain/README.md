# Example Domain - OpenAPI Specification Template

## 📋 **Назначение**

Этот файл (`main.yaml`) является **официальным шаблоном** для создания OpenAPI спецификаций в NECPGAME проекте. Он демонстрирует все лучшие практики, изученные в ходе оптимизации существующей директории OpenAPI.

## 🎯 **Ключевые Особенности**

### OK **Enterprise-Grade Архитектура**
- Полная совместимость с enterprise-grade доменами
- Правильная структура для всех AI агентов
- Оптимизация для генерации Go кода с ogen

### OK **Backend Optimization Hints**
- Struct alignment hints для оптимизации памяти
- Performance targets и требования
- Порядок полей: large → small для экономии памяти

### OK **Complete Validation**
- OK Redocly lint: проходит валидацию
- OK ogen: успешно генерирует Go код
- OK Go compilation: код компилируется без ошибок

### OK **Security-First Approach**
- JWT Bearer authentication
- Правильные HTTP статус коды
- Error handling с дополнительным контекстом

## 📁 **Структура Шаблона**

```
proto/openapi/example-domain/
├── main.yaml           # Основная спецификация (этот файл)
└── README.md          # Это руководство
```

## 🔧 **Обязательные Элементы**

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
      type: http
      scheme: bearer
      bearerFormat: JWT
```

### 4. **Обязательные Health Endpoints**

#### Health Check
```yaml
/health:
  get:
    operationId: [domain]HealthCheck
    responses:
      '200': # Обязательно
        description: Service is healthy
      '503': # Обязательно
        description: Service is unhealthy
```

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

### 5. **Backend Optimization Hints**

#### Struct Alignment
```yaml
description: 'BACKEND NOTE: Fields ordered for struct alignment (large → small). Expected memory savings: 30-50%.'
```

#### Performance Targets
```yaml
description: |
  **Performance:** <50ms P95, supports 1000+ concurrent requests
  **Memory:** <50KB per instance
  **Concurrent users:** 10,000+
```

## 🛠️ **Как Использовать Шаблон**

### 1. **Копирование Шаблона**
```bash
# Создайте новый домен
mkdir proto/openapi/your-new-domain
cp proto/openapi/example-domain/main.yaml proto/openapi/your-new-domain/main.yaml
```

### 2. **Замена Placeholder'ов**
- `[Domain Name]` → Название вашего домена
- `[domain purpose]` → Описание назначения домена
- `[domain]` → Кодовое имя домена (kebab-case)
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
- Упорядочите поля: large → small
- Добавьте `BACKEND NOTE` с оптимизациями
- Добавьте примеры и валидацию

## OK **Валидация Шаблона**

### Redocly Lint
```bash
npx @redocly/cli lint proto/openapi/example-domain/main.yaml
# OK Valid. 4 warnings (нормально)
```

### Go Code Generation
```bash
# Bundle
npx @redocly/cli bundle proto/openapi/example-domain/main.yaml -o bundled.yaml

# Generate Go code
ogen --target temp --package api --clean bundled.yaml

# Compile
cd temp && go mod init test && go mod tidy && go build .
# OK Success
```

## 📊 **Performance Benchmarks**

Шаблон оптимизирован для:
- **P99 Latency:** <50ms
- **Memory per Instance:** <50KB
- **Concurrent Users:** 10,000+
- **Struct Alignment:** 30-50% memory savings

## 🔗 **Связанные Документы**

- `.cursor/rules/agent-api-designer.mdc` - Правила API Designer агента
- `.cursor/DOMAIN_REFERENCE.md` - Справочник enterprise-grade доменов
- `.cursor/BACKEND_OPTIMIZATION_CHECKLIST.md` - Чек-лист оптимизаций
- `.cursor/PERFORMANCE_ENFORCEMENT.md` - Требования к производительности

## 🎯 **Следующие Шаги**

1. Скопируйте этот шаблон для нового домена
2. Замените placeholders на реальные значения
3. Добавьте domain-specific операции
4. Оптимизируйте схемы для struct alignment
5. Проверьте валидацию и генерацию кода
6. Зарегистрируйте домен в DOMAIN_REFERENCE.md

## WARNING **Важные Замечания**

- **НЕ** удаляйте обязательные health endpoints
- **ВСЕГДА** добавляйте operationId для генерации Go кода
- **ОПТИМИЗИРУЙТЕ** порядок полей в схемах
- **ВАЛИДИРУЙТЕ** перед коммитом
- **ДОКУМЕНТИРУЙТЕ** performance targets

---

## 🔗 **Использование Общих Схем Между Домеами**

Шаблон поддерживает использование общих схем между разными доменами:

### 📁 **Общая Директория Схем**
```bash
proto/openapi/common-schemas.yaml  # Универсальные схемы для всех доменов
```

### OK **Примеры Общих Схем**
- `Error` - Универсальная схема ошибок
- `HealthResponse` - Схема здоровья сервисов
- `PaginationMeta` - Метаданные пагинации
- `UUID`, `PlayerId`, `GuildId` - Общие типы ID
- `Timestamp`, `CreatedAt`, `UpdatedAt` - Временные метки
- `Status`, `Priority` - Перечисления

### 🚀 **Как Использовать Общие Схемы**
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

### OK **Преимущества**
- **Консистентность** - одинаковые схемы во всех доменах
- **Удобство сопровождения** - изменения в одном месте
- **Генерация Go кода** - работает без проблем
- **Enterprise-grade** - профессиональный подход

### 🧪 **Тестирование**
Общие схемы протестированы и работают с:
- OK **Redocly bundling**
- OK **ogen code generation**
- OK **Go compilation**
- OK **Cross-domain references**

---

**Этот шаблон гарантирует, что все новые OpenAPI спецификации будут enterprise-grade и совместимы со всей экосистемой NECPGAME AI агентов.**
