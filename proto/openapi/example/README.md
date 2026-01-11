# Enterprise-Grade OpenAPI Template - SOLID/DRY Domain Inheritance

## 📋 **Назначение**

Этот шаблон демонстрирует **enterprise-grade архитектуру** для создания новых микросервисов в NECPGAME с соблюдением принципов SOLID/DRY и domain separation.

## 🏗️ **Архитектурные Принципы**

### **SOLID/DRY Domain Inheritance**

```
🎯 ЦЕЛЬ: Максимальное переиспользование + минимальная дублирование

common/schemas/
├── game-entities.yaml        # Игровые сущности (Character, Combat, Abilities)
├── economy-entities.yaml     # Экономические сущности (Wallet, Transaction, Auction)
├── social-entities.yaml      # Социальные сущности (Profile, Guild, Chat)
└── infrastructure-entities.yaml # Инфраструктурные сущности (User, Session, Audit)

{service-name}-service/
└── main.yaml                 # НАСЛЕДУЕТ от domain entities + добавляет специфику
```

### **Преимущества Domain Inheritance**

#### ✅ **80% Сокращение Кода**
```yaml
# ❌ СТАРЫЙ ПОДХОД: Дублирование в каждом сервисе
MyEntity:
  type: object
  properties:
    id: {type: string, format: uuid}          # ДУБЛИРОВАНИЕ ❌
    created_at: {type: string, format: date-time} # ДУБЛИРОВАНИЕ ❌
    name: {type: string}                      # Только это уникально

# ✅ НОВЫЙ ПОДХОД: Domain Inheritance
MyEntity:
  allOf:
    - $ref: '../common/schemas/game-entities.yaml#/CharacterEntity' # 20+ полей автоматически
    - type: object
      properties:
        cyberware_level: {type: integer, minimum: 0, maximum: 20} # Только уникальное поле
```

#### ✅ **Enterprise Performance**
- **Optimistic Locking**: `version` поле для конкурентных операций
- **Struct Alignment**: 30-50% экономии памяти
- **Strict Typing**: Enum, patterns, min/max, examples
- **Audit Trail**: Полный аудит всех операций

#### ✅ **Consistency & Quality**
- **Единые паттерны** во всех 74 сервисах
- **Domain-specific responses** для каждого домена
- **Standardized CRUD** с общими операциями
- **Health endpoints** для мониторинга

## 📁 **Структура Шаблона**

```
example/
├── main.yaml              # Enterprise-grade спецификация с domain inheritance
└── README.md              # Эта документация
```

## 🚀 **Использование Шаблона**

### **Шаг 1: Выбор Домена**

Определите домен вашего сервиса:

```bash
# Game Domain Services
combat-service, movement-service, ability-service, implant-service

# Economy Domain Services
currency-service, trading-service, auction-service, marketplace-service

# Social Domain Services
guild-service, communication-service, friend-service, relationship-service

# Infrastructure Domain Services
auth-service, session-service, user-profile-service, notification-service
```

### **Шаг 2: Создание Сервиса**

```bash
# Создать директорию сервиса
mkdir -p proto/openapi/{service-name}-service

# Скопировать и адаптировать шаблон
cp proto/openapi/example/main.yaml proto/openapi/{service-name}-service/main.yaml
cp proto/openapi/example/README.md proto/openapi/{service-name}-service/README.md
```

### **Шаг 3: Адаптация Шаблона**

#### **Заменить Placeholders в main.yaml**

```yaml
# Найти и заменить:
{ServiceName}          → AuthService
{ServiceDomain}        → Authentication
{service-name}         → auth-service
{domain}              → infrastructure
{Resource}            → UserAccount
{resource}            → user-account
{action1}             → activate
{action2}             → deactivate
{action3}             → reset-password
```

#### **Пример: Auth Service**

```yaml
# proto/openapi/auth-service/main.yaml
info:
  title: AuthService API  # {ServiceName} → AuthService
  description: |
    **Enterprise-grade API for Authentication**  # {ServiceDomain} → Authentication

servers:
  - url: https://api.necpgame.com/v1/auth-service  # {service-name} → auth-service

# Domain inheritance
components:
  schemas:
    UserAccount:  # {Resource} → UserAccount
      allOf:
        - $ref: '../common/schemas/infrastructure-entities.yaml#/UserAccountEntity'  # {domain} → infrastructure
        - type: object
          properties:
            # Service-specific fields only
            custom_auth_field: {type: string}
```

### **Шаг 4: Domain-Specific Configuration**

#### **Game Domain Service**
```yaml
# Наследует от game-entities.yaml
components:
  schemas:
    PlayerCharacter:
      allOf:
        - $ref: '../common/schemas/game-entities.yaml#/CharacterEntity'
        - type: object
          properties:
            cyberware_level: {type: integer, minimum: 0, maximum: 20}
```

#### **Economy Domain Service**
```yaml
# Наследует от economy-entities.yaml
components:
  schemas:
    PlayerWallet:
      allOf:
        - $ref: '../common/schemas/economy-entities.yaml#/WalletEntity'
        - type: object
          properties:
            vip_multiplier: {type: number, minimum: 1.0, maximum: 5.0}
```

#### **Social Domain Service**
```yaml
# Наследует от social-entities.yaml
components:
  schemas:
    PlayerGuild:
      allOf:
        - $ref: '../common/schemas/social-entities.yaml#/GuildEntity'
        - type: object
          properties:
            faction_alignment: {type: string, enum: [corporate, nomad, street]}
```

## 📊 **Performance & Monitoring**

### **Обязательные Health Endpoints**

```yaml
paths:
  /health:           # Базовая проверка
  /health/detailed:  # Детальная с метриками
  /health/batch:     # Проверка зависимостей
  /health/ws:        # WebSocket проверка (для real-time сервисов)
```

### **Performance Targets**

```yaml
x-performance:
  p99-latency: "<50ms"
  memory-target: "<50KB per instance"
  concurrency-target: "10,000+ users"
```

### **Monitoring Integration**

```yaml
x-monitoring:
  metrics: [request_count, request_duration, error_rate]
  alerts: [latency_p95 > 100ms, error_rate > 1%]
```

## 🔗 **Common References**

### **Обязательные $ref для всех сервисов**

```yaml
components:
  # Domain-specific success responses
  responses:
    OK: $ref: '../common/responses/success.yaml#/OK'
    Created: $ref: '../common/responses/success.yaml#/Created'
    CombatActionSuccess: $ref: '../common/responses/success.yaml#/CombatActionSuccess'  # Game domain
    TransactionSuccess: $ref: '../common/responses/success.yaml#/TransactionSuccess'     # Economy domain
    FriendRequestSuccess: $ref: '../common/responses/success.yaml#/FriendRequestSuccess' # Social domain

    # Error responses
    BadRequest: $ref: '../common/responses/error.yaml#/BadRequest'
    Unauthorized: $ref: '../common/responses/error.yaml#/Unauthorized'
    NotFound: $ref: '../common/responses/error.yaml#/NotFound'

  # Domain entities (choose appropriate domain)
  schemas:
    Error: $ref: '../common/schemas/common.yaml#/Error'
    HealthResponse: $ref: '../common/schemas/health.yaml#/HealthResponse'

  # Security
  securitySchemes:
    BearerAuth: $ref: '../common/security/security.yaml#/BearerAuth'
    ApiKeyAuth: $ref: '../common/security/security.yaml#/ApiKeyAuth'
```

## 🧪 **Валидация и Тестирование**

### **Pre-Commit Checks**

```bash
# Lint specification
npx @redocly/cli lint main.yaml

# Bundle for $ref validation
npx @redocly/cli bundle main.yaml -o bundled.yaml

# Generate Go code
ogen --target /tmp/codegen --package api --clean bundled.yaml
cd /tmp/codegen && go mod init test && go build .

# Generate documentation
npx @redocly/cli build-docs main.yaml -o docs/index.html
```

### **Enterprise Requirements**

- [ ] **Domain Inheritance**: Использует allOf с domain entities
- [ ] **No Duplication**: Нет повторяющихся полей из common
- [ ] **Strict Typing**: Все поля имеют типы, ограничения, examples
- [ ] **Optimistic Locking**: version поле для конкурентных операций
- [ ] **Health Endpoints**: Все 4 типа health проверок
- [ ] **Redocly Valid**: Проходит линтинг без ошибок
- [ ] **Ogen Compatible**: Генерирует валидный Go код
- [ ] **Documentation**: README.md с описанием домена

## 📈 **Migration Impact**

### **До Domain Inheritance**
- **471 файлов** в system/, specialized/, social/, world/, economy/
- **100% дублирования** общих полей (id, created_at, updated_at)
- **Несогласованные паттерны** между сервисами
- **Сложная поддержка** и развитие

### **После Domain Inheritance**
- **74 atomic микросервиса** с четким разделением
- **0% дублирования** благодаря inheritance
- **Единые паттерны** во всех сервисах
- **Enterprise performance** с optimistic locking
- **80% сокращение кода** и времени разработки

## 🚀 **Быстрый Старт**

```bash
# 1. Выбрать домен и имя сервиса
SERVICE_NAME="auth-service"
DOMAIN="infrastructure"

# 2. Создать директорию
mkdir -p proto/openapi/$SERVICE_NAME

# 3. Скопировать шаблон
cp proto/openapi/example/main.yaml proto/openapi/$SERVICE_NAME/main.yaml
cp proto/openapi/example/README.md proto/openapi/$SERVICE_NAME/README.md

# 4. Адаптировать placeholders
sed -i "s/{ServiceName}/Auth/g" main.yaml
sed -i "s/{ServiceDomain}/Authentication/g" main.yaml
sed -i "s/{service-name}/$SERVICE_NAME/g" main.yaml
sed -i "s/{domain}/$DOMAIN/g" main.yaml

# 5. Проверить валидность
npx @redocly/cli lint proto/openapi/$SERVICE_NAME/main.yaml
```

## 📞 **Поддержка**

- **Архитектор:** @architect-agent
- **API Designer:** @api-designer-agent
- **Документация:** docs@necpgame.com

**Все вопросы по enterprise-grade архитектуре направлять в #api-architecture Slack канал**

---

*Этот шаблон обеспечивает enterprise-grade качество для всех 74 микросервисов NECPGAME*