# 🏗️ **OpenAPI Specification Template Usage Guide**

## 📋 **Для AI Агентов: Правила Создания Новых Спецификаций**

### 🎯 **Цель**

Этот гайд обеспечивает **консистентность** и **качество** всех OpenAPI спецификаций в NECPGAME проекте.

---

## 🔧 **Обязательные Шаги Создания Новой Спецификации**

### Шаг 1: Выбор Домена

```bash
# Проверьте доступные enterprise-grade домены
cat .cursor/DOMAIN_REFERENCE.md

# Или используйте скрипт
python scripts/list-domains.py
```

### Шаг 2: Копирование Шаблона

```bash
# Создайте директорию домена
DOMAIN_NAME="your-new-domain"  # kebab-case
mkdir proto/openapi/$DOMAIN_NAME

# Скопируйте шаблон
cp proto/openapi/example-domain/main.yaml proto/openapi/$DOMAIN_NAME/main.yaml
cp proto/openapi/example-domain/README.md proto/openapi/$DOMAIN_NAME/README.md
```

### Шаг 3: Настройка Основных Метаданных

```yaml
# ОБЯЗАТЕЛЬНО заменить в info:
info:
  title: "[Domain Name] API"           # Название домена с заглавной буквы
  description: |
    **Enterprise-Grade [Domain Name] API for NECPGAME**

    [Подробное описание назначения домена]
    [Ключевые возможности]
    [Performance targets]

  version: "1.0.0"
  contact:
    name: NECPGAME API Support
    email: api@necpgame.com
  license:
    name: MIT
```

### Шаг 4: Настройка Servers

```yaml
servers:
  - url: https://api.necpgame.com/v1/[domain-name]      # production
    description: Production server
  - url: https://staging-api.necpgame.com/v1/[domain-name]  # staging
    description: Staging server
  - url: http://localhost:8080/api/v1/[domain-name]    # local dev
    description: Local development server
```

### Шаг 5: Добавление Domain-Specific Операций

```yaml
# ЗАМЕНИТЬ примеры на реальные endpoints
paths:
  /health:      # ОСТАВИТЬ ОБЯЗАТЕЛЬНО
  /health/batch: # ОСТАВИТЬ ОБЯЗАТЕЛЬНО
  /health/ws:    # ОСТАВИТЬ ОБЯЗАТЕЛЬНО

  # ДОБАВИТЬ реальные операции домена
  /[resources]:
    get:    # List resources
    post:   # Create resource
  /[resources]/{id}:
    get:    # Get by ID
    put:    # Update
    delete: # Delete
```

---

## 📊 **Обязательные Схемы**

### 1. Health Schemas (ОБЯЗАТЕЛЬНО)

```yaml
components:
  schemas:
    HealthResponse:      # ОСТАВИТЬ
    WebSocketHealthMessage: # ОСТАВИТЬ
    Error:               # ОСТАВИТЬ
```

### 2. Domain Entity Schemas

```yaml
# ДОБАВИТЬ основные сущности домена
YourEntity:
  type: object
  required: [id, name, created_at]
  properties:
    # ПОРЯДОК ПОЛЕЙ: large → small
    id: { type: string, format: uuid }
    name: { type: string, maxLength: 100 }
    description: { type: string, maxLength: 1000 }  # Large fields first
    created_at: { type: string, format: date-time }
    status: { type: string, enum: [...] }
    is_active: { type: boolean }  # Small fields last
  description: 'BACKEND NOTE: Fields ordered for struct alignment (large → small). Expected memory savings: 30-50%.'
```

### 3. Request/Response Schemas

```yaml
CreateYourEntityRequest:
  # Для создания сущностей

UpdateYourEntityRequest:
  # Для обновления (partial updates)

YourEntityResponse:
  type: object
  required: [entity]
  properties:
    entity: { $ref: '#/components/schemas/YourEntity' }

YourEntityListResponse:
  # Для paginated list responses
```

---

## ⚡ **Performance Optimization Requirements**

### ОБЯЗАТЕЛЬНЫЕ Оптимизации

#### 1. Struct Alignment (КРИТИЧНО)

```yaml
# ПРАВИЛЬНЫЙ порядок полей:
properties:
  # 1. Large types first (strings, arrays, objects)
  id: { type: string, format: uuid }
  name: { type: string }
  description: { type: string }
  metadata: { type: object }

  # 2. Medium types (integers, floats)
  created_at: { type: string, format: date-time }
  priority: { type: integer }

  # 3. Small types last (booleans, enums)
  status: { type: string, enum: [...] }
  is_active: { type: boolean }

description: 'BACKEND NOTE: Fields ordered for struct alignment (large → small). Expected memory savings: 30-50%.'
```

#### 2. Pagination для List Operations

```yaml
parameters:
  - name: page
    in: query
    schema: { type: integer, minimum: 1, default: 1 }
  - name: limit
    in: query
    schema: { type: integer, minimum: 1, maximum: 100, default: 20 }

responses:
  '200':
    content:
      application/json:
        schema:
          type: object
          required: [items, total_count, has_more]
          properties:
            items: { type: array, items: { $ref: '#/components/schemas/YourEntity' } }
            total_count: { type: integer }
            has_more: { type: boolean }
```

#### 3. Caching Headers

```yaml
responses:
  '200':
    headers:
      Cache-Control:
        schema:
          type: string
          example: max-age=300, private
      ETag:
        schema:
          type: string
          example: '"entity-123-v1"'
```

---

## 🔒 **Security Requirements**

### ОБЯЗАТЕЛЬНЫЕ Элементы

#### 1. Authentication

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

#### 2. Error Responses

```yaml
responses:
  '400':
    description: Invalid request data
    content:
      application/json:
        schema: { $ref: '#/components/schemas/Error' }
  '401':
    description: Unauthorized
  '403':
    description: Forbidden
  '404':
    description: Not found
  '409':
    description: Conflict
  '422':
    description: Validation error
```

#### 3. Input Validation

```yaml
properties:
  name:
    type: string
    minLength: 1
    maxLength: 100
  email:
    type: string
    format: email
    maxLength: 254
```

---

## 🏷️ **OperationId Requirements**

### ОБЯЗАТЕЛЬНЫЕ Правила

#### 1. Уникальность

```yaml
# ДОЛЖНЫ БЫТЬ уникальными в рамках домена
operationId: createUser          # OK Good
operationId: getUserById         # OK Good
operationId: listUsers           # OK Good
```

#### 2. Согласованность

```yaml
# Использовать camelCase
operationId: createUser          # OK
operationId: get_user_by_id      # ❌ snake_case
operationId: GetUserById         # ❌ PascalCase
```

#### 3. Паттерны

```yaml
# CRUD operations:
operationId: create[Entity]      # createUser
operationId: get[Entity]         # getUser
operationId: list[Entities]      # listUsers
operationId: update[Entity]      # updateUser
operationId: delete[Entity]      # deleteUser

# Custom operations:
operationId: [action][Entity]    # activateUser, deactivateUser
```

---

## OK **Валидация Перед Коммитом**

### ОБЯЗАТЕЛЬНЫЕ Шаги

#### 1. Redocly Lint

```bash
npx @redocly/cli lint proto/openapi/your-domain/main.yaml
# ДОЛЖЕН ПРОХОДИТЬ без ошибок
```

#### 2. Bundle Test

```bash
npx @redocly/cli bundle proto/openapi/your-domain/main.yaml -o test-bundle.yaml
# ДОЛЖЕН создавать bundled файл
```

#### 3. Go Code Generation

```bash
ogen --target test-gen --package api --clean test-bundle.yaml
# ДОЛЖЕН генерировать код без ошибок
```

#### 4. Go Compilation

```bash
cd test-gen
go mod init test && go mod tidy && go build .
# ДОЛЖЕН компилироваться без ошибок
```

#### 5. Domain Validation Script

```bash
python scripts/validate-domains-openapi.py
# ДОЛЖЕН проходить для вашего домена
```

---

## 📋 **Чек-лист Готовности**

### OK **Обязательные Элементы**

- [ ] OpenAPI 3.0.3 header
- [ ] Полная info секция с contact/license
- [ ] 3 сервера (prod, staging, local)
- [ ] BearerAuth security scheme
- [ ] Все 3 health endpoints (/health, /health/batch, /health/ws)
- [ ] Минимум 1 domain-specific endpoint
- [ ] Все operationId уникальны и в camelCase
- [ ] Все схемы имеют BACKEND NOTE с оптимизациями
- [ ] Порядок полей: large → small
- [ ] Error responses для всех операций
- [ ] Pagination для list операций

### OK **Валидация**

- [ ] Redocly lint проходит (warnings разрешены)
- [ ] Bundle создается успешно
- [ ] Go код генерируется без ошибок
- [ ] Код компилируется без ошибок
- [ ] Domain validation script проходит

### OK **Документация**

- [ ] README.md создан и заполнен
- [ ] Performance targets задокументированы
- [ ] Domain зарегистрирован в DOMAIN_REFERENCE.md
- [ ] Issue номер добавлен в начало файла

---

## 🚀 **Примеры Реальных Доментов**

### System Domain (553 файла)

- Назначение: Infrastructure, monitoring, configuration
- Endpoints: `/health`, `/metrics`, `/config`
- Особенности: Batch operations, WebSocket monitoring

### Specialized Domain (157 файлов)

- Назначение: Game mechanics, combat, inventory
- Endpoints: `/combat`, `/inventory`, `/quests`
- Особенности: Real-time operations, complex schemas

### Social Domain (91 файл)

- Назначение: Players interaction, guilds, messaging
- Endpoints: `/guilds`, `/friends`, `/chat`
- Особенности: Social graphs, notifications

---

## 🔗 **Связанные Ресурсы**

### 📋 **Основные Документы**

- `proto/openapi/example-domain/main.yaml` - Полный рабочий шаблон
- `proto/openapi/TEMPLATE_USAGE_GUIDE.md` - Это руководство (текущий файл)
- `proto/openapi/example-domain/README.md` - Детальное объяснение шаблона

### 🔧 **Правила AI Агентов**

- `.cursor/AGENT_SIMPLE_GUIDE.md` - Простой алгоритм работы агентов
- `.cursor/rules/agent-api-designer.mdc` - Правила API Designer агента
- `.cursor/rules/agent-architect.mdc` - Правила Architect агента
- `.cursor/rules/agent-autonomy.mdc` - Правила автономности агентов
- `.cursor/rules/agent-backend.mdc` - Правила Backend агента
- `.cursor/rules/agent-content-writer.mdc` - Правила Content Writer агента
- `.cursor/rules/agent-database.mdc` - Правила Database агента
- `.cursor/rules/agent-devops.mdc` - Правила DevOps агента
- `.cursor/rules/agent-file-placement.mdc` - Правила размещения файлов
- `.cursor/rules/agent-game-balance.mdc` - Правила Game Balance агента
- `.cursor/rules/agent-idea-writer.mdc` - Правила Idea Writer агента
- `.cursor/rules/agent-network.mdc` - Правила Network агента
- `.cursor/rules/agent-performance.mdc` - Правила Performance агента
- `.cursor/rules/agent-qa.mdc` - Правила QA агента
- `.cursor/rules/agent-release.mdc` - Правила Release агента
- `.cursor/rules/agent-security.mdc` - Правила Security агента
- `.cursor/rules/agent-stats.mdc` - Правила Stats агента
- `.cursor/rules/agent-ue5.mdc` - Правила UE5 агента
- `.cursor/rules/agent-ui-ux-designer.mdc` - Правила UI/UX Designer агента

### ⚙️ **Глобальные Правила**

- `.cursor/rules/always.mdc` - Глобальные правила проекта
- `.cursor/rules/linter-emoji-ban.mdc` - Запрет на эмодзи

### 🎯 **Оптимизации и Производительность**

- `.cursor/BACKEND_OPTIMIZATION_CHECKLIST.md` - Чек-лист оптимизаций Backend
- `.cursor/PERFORMANCE_ENFORCEMENT.md` - Требования к производительности
- `.cursor/DOMAIN_REFERENCE.md` - Справочник enterprise-grade доменов

### 📊 **Workflow и Конфигурация**

- `.cursor/CONTENT_WORKFLOW.md` - Workflow для контентных задач
- `.cursor/GITHUB_PROJECT_CONFIG.md` - Конфигурация GitHub проекта
- `.cursor/commands/` - Специфичные команды агентов

### 🛠️ **Скрипты и Инструменты**

- `scripts/validate-domains-openapi.py` - Валидация OpenAPI доменов
- `scripts/generate-all-domains-go.py` - Генерация Go кода для всех доменов
- `scripts/reorder-openapi-fields.py` - Оптимизация порядка полей OpenAPI
- `scripts/reorder-liquibase-columns.py` - Оптимизация колонок БД

---

## WARNING **Критически Важно**

**НЕ** коммитьте спецификацию, которая:

- ❌ Не проходит Redocly lint
- ❌ Не генерирует Go код
- ❌ Не компилируется
- ❌ Не имеет operationId
- ❌ Не имеет BACKEND NOTE оптимизаций
- ❌ Имеет поля в неправильном порядке

**Все спецификации ДОЛЖНЫ быть enterprise-grade и production-ready!**
