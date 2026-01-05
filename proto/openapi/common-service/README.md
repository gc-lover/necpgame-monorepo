# NECPGAME Common Architecture Guide

## SOLID/DRY Foundation for Enterprise-Grade APIs

### Цель Архитектуры

Создать масштабируемую, поддерживаемую и стандартизированную систему API, которая обеспечивает:

- **SOLID принципы**: Каждый сервис имеет единственную ответственность
- **DRY подход**: Максимальное переиспользование общих компонентов
- **Enterprise-grade**: Производительность, безопасность, наблюдаемость
- **Расширяемость**: Легкое добавление новых сервисов и функциональности

---

## Структура Common Архитектуры

```
proto/openapi/common/
├── schemas/
│   ├── common.yaml           # Базовые entity схемы
│   ├── game-entities.yaml    # Игровые сущности
│   ├── economy-entities.yaml # Экономические сущности
│   └── social-entities.yaml  # Социальные сущности
├── responses/
│   ├── success.yaml          # Успешные ответы
│   └── error.yaml           # Ошибки
├── operations/
│   └── crud.yaml            # CRUD операции
├── security/
│   └── security.yaml        # Безопасность
└── info/
    └── info.yaml           # Метаданные
```

---

## 🏗️ **Базовые Entity Схемы**

### **BaseEntity** - Основа всех сущностей

```yaml
BaseEntity:
  required: [id, created_at, updated_at]
  properties:
    id: {type: string, format: uuid}
    created_at: {type: string, format: date-time}
    updated_at: {type: string, format: date-time}
```

**Использование:**

```yaml
MyEntity:
  allOf:
    - $ref: '../common-service/schemas/common.yaml#/BaseEntity'
    - type: object
      properties:
        myField: {type: string}
```

### **AuditableEntity** - Для сущностей с аудитом

```yaml
AuditableEntity:
  allOf:
    - $ref: '#/BaseEntity'
    - type: object
      properties:
        created_by: {type: string, format: uuid}
        updated_by: {type: string, format: uuid}
```

**Использование:** Админские сущности, финансовые транзакции.

### **VersionedEntity** - Для оптимистичной блокировки

```yaml
VersionedEntity:
  allOf:
    - $ref: '#/BaseEntity'
    - type: object
      required: [version]
      properties:
        version: {type: integer, minimum: 1}
```

**Использование:** Сущности с конкурентным доступом.

### **SoftDeletableEntity** - Для мягкого удаления

```yaml
SoftDeletableEntity:
  allOf:
    - $ref: '#/BaseEntity'
    - type: object
      properties:
        deleted_at: {type: string, format: date-time, nullable: true}
```

**Использование:** Восстанавливаемые сущности.

---

## 🎮 **Доменные Entity Схемы**

### **Game Entities** (`game-entities.yaml`)

- `CharacterEntity` - Персонажи игроков
- `ItemEntity` - Игровые предметы
- `WeaponEntity` - Оружие
- `CombatSessionEntity` - Боевые сессии
- `QuestEntity` - Квесты
- `LocationEntity` - Локации

### **Economy Entities** (`economy-entities.yaml`)

- `WalletEntity` - Кошельки игроков
- `TransactionEntity` - Финансовые транзакции
- `MarketplaceListingEntity` - Листинги маркетплейса
- `AuctionEntity` - Аукционы

### **Social Entities** (`social-entities.yaml`)

- `UserProfileEntity` - Профили пользователей
- `FriendshipEntity` - Дружеские связи
- `GuildEntity` - Гильдии
- `ChatMessageEntity` - Сообщения чата

---

## 🔄 **Стандартизированные CRUD Операции**

### **Паттерны Операций**

Каждый сервис должен реализовывать стандартные операции:

```yaml
# CREATE
POST /{entity}

# READ
GET /{entity}/{id}      # Получить по ID
GET /{entity}           # Список с пагинацией

# UPDATE
PUT /{entity}/{id}      # Обновить по ID

# DELETE
DELETE /{entity}/{id}   # Мягкое удаление

# BULK
POST /{entity}/bulk     # Массовые операции

# SEARCH
POST /{entity}/search   # Продвинутый поиск
```

### **Обязательные Query Parameters**

```yaml
GET /{entity}
  ?limit=20          # Размер страницы (1-100)
  ?offset=0          # Смещение
  ?sort_by=created_at # Сортировка
  ?sort_order=desc   # Порядок сортировки
```

### **Optimistic Locking**

```yaml
PUT /{entity}/{id}
{
  "id": "uuid",
  "version": 1,      # Текущая версия для проверки
  "field": "value"   # Обновляемые поля
}
```

---

## 📊 **Стандартизированные Ответы**

### **Успешные Ответы**

```yaml
# 200 OK
{
  "success": true,
  "message": "Operation completed"
}

# 201 Created
{
  "id": "uuid",
  "created_at": "2025-12-28T10:00:00Z"
}

# 200 Paginated
{
  "items": [...],
  "total": 150,
  "limit": 20,
  "offset": 0
}
```

### **Доменные Ответы**

- `CombatActionSuccess` - Результаты боя
- `TransactionSuccess` - Финансовые операции
- `FriendRequestSuccess` - Социальные действия

---

## 🔧 **Использование в Сервисах**

### **Шаблон Нового Сервиса**

```yaml
# {service-name}-service/main.yaml
openapi: 3.0.3
info:
  title: "{ServiceName} Service API"
  description: "**Enterprise-grade API for {Domain}**"
  version: "1.0.0"

servers:
  - url: https://api.necpgame.com/v1/{service-name}

security:
  - BearerAuth: []

tags:
  - name: "{Domain} Management"
  - name: Health Monitoring

paths:
  # Health endpoints (обязательно)
  /health: {...}
  /health/batch: {...}
  /health/ws: {...}

  # Domain-specific paths
  /characters:
    $ref: './characters.yaml#/paths/characters'

components:
  # Обязательные $ref на common
  responses:
    OK: $ref: '../common-service/responses/success.yaml#/OK'
    BadRequest: $ref: '../common-service/responses/error.yaml#/BadRequest'

  schemas:
    Error: $ref: '../common-service/schemas/common.yaml#/Error'
    HealthResponse: $ref: '../common-service/schemas/health.yaml#/HealthResponse'

  securitySchemes:
    BearerAuth: $ref: '../common-service/security/security.yaml#/BearerAuth'

  # Domain entities
  schemas:
    Character:
      allOf:
        - $ref: '../common-service/schemas/game-entities.yaml#/CharacterEntity'
        - type: object
          properties:
            custom_field: {type: string}
```

### **Наследование Entity**

```yaml
# Правильно: Используем composition
MyEntity:
  allOf:
    - $ref: '../common-service/schemas/common.yaml#/BaseEntity'
    - type: object
      properties:
        domain_field: {type: string}

# Неправильно: Дублируем общие поля
MyEntity:
  type: object
  properties:
    id: {type: string, format: uuid}        # ДУБЛИРОВАНИЕ!
    created_at: {type: string, format: date-time}  # ДУБЛИРОВАНИЕ!
    domain_field: {type: string}
```

---

## 🧪 **Валидация и Качество**

### **Обязательные Проверки**

```bash
# Линтинг
npx @redocly/cli lint main.yaml

# Бандлинг (проверка $ref)
npx @redocly/cli bundle main.yaml -o bundled.yaml

# Генерация Go кода
ogen --target /tmp/test --package api --clean bundled.yaml

# Компиляция
cd /tmp/test && go mod init test && go build .
```

### **Performance Benchmarks**

- **P99 Latency**: <50ms для всех операций
- **Memory per Instance**: <50KB
- **Concurrent Users**: 10,000+

---

## 🔄 **Миграция Существующих Сервисов**

### **Шаг 1: Анализ Текущих Схем**

```bash
# Найти дублированные поля
grep -r "created_at\|updated_at\|id.*uuid" proto/openapi/system/
```

### **Шаг 2: Замена на Common $ref**

```yaml
# Было:
MySchema:
  type: object
  properties:
    id: {type: string, format: uuid}
    created_at: {type: string, format: date-time}
    custom_field: {type: string}

# Стало:
MySchema:
  allOf:
    - $ref: '../common-service/schemas/common.yaml#/BaseEntity'
    - type: object
      properties:
        custom_field: {type: string}
```

### **Шаг 3: Обновление Операций**

- Заменить кастомные CRUD на стандартные из `crud.yaml`
- Добавить optimistic locking где нужно
- Обновить responses на common

---

## 📈 **Преимущества Новой Архитектуры**

### **Для Разработчиков**

- **30-50% меньше кода** благодаря переиспользованию
- **Стандартизированные паттерны** - меньше ошибок
- **Быстрое создание сервисов** - copy-paste из шаблонов
- **Автоматическая генерация** Go кода

### **Для Архитектуры**

- **SOLID compliance** - каждый сервис имеет одну ответственность
- **DRY principle** - нет дублирования схем
- **Микросервисная масштабируемость**
- **API consistency** - единообразные интерфейсы

### **Для Производительности**

- **Struct alignment** - оптимизированная память
- **Connection pooling** - эффективное использование ресурсов
- **Caching strategies** - встроенные кэширующие заголовки
- **Monitoring** - стандартизированная наблюдаемость

---

## 🚨 **Критические Правила**

### **ЗАПРЕЩЕНО**

- [ ] Создавать схемы без наследования от BaseEntity
- [ ] Дублировать общие поля (id, timestamps)
- [ ] Использовать нестандартные HTTP методы
- [ ] Пропускать health endpoints
- [ ] Нарушать optimistic locking для конкурентных сущностей

### **ОБЯЗАТЕЛЬНО**

- [ ] Все $ref указывают на `../common-service/`
- [ ] Валидация проходит без ошибок
- [ ] Go код генерируется успешно
- [ ] Документация обновлена
- [ ] Performance benchmarks соблюдены

---

## 🛠️ **Инструменты и Автоматизация**

### **Генерация Шаблонов**

```bash
# Создать новый сервис
./scripts/create-service.sh {service-name} {domain}

# Валидировать все сервисы
./scripts/validate-all-services.sh

# Сгенерировать документацию
./scripts/generate-docs.sh
```

### **CI/CD Pipeline**

```yaml
# .github/workflows/api-validation.yml
- name: Lint OpenAPI
  run: npx @redocly/cli lint proto/openapi/**/*.yaml

- name: Generate Go Code
  run: ./scripts/generate-go-clients.sh

- name: Performance Tests
  run: ./scripts/run-performance-tests.sh
```

---

## 📞 **Поддержка**

- **Архитектура**: Задавайте вопросы в #api-architecture
- **Common Components**: Обновления в #common-schemas
- **Performance**: Метрики в Grafana dashboard

**Помните**: Эта архитектура - живой организм. Улучшайте и расширяйте её по мере роста проекта!

---

*Документация обновляется с каждым major релизом API.*
