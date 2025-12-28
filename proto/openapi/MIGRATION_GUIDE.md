# 🚀 **NECPGAME OpenAPI Migration Guide**
## From Legacy to SOLID/DRY Enterprise Architecture

---

## 📊 **Текущая Ситуация**

### **Legacy Директории (471+ файлов)**
```
proto/openapi/
├── system/           # 471 файлов - AI, monitoring, messaging, infrastructure
├── specialized/      # 178 файлов - combat, crafting, movement, effects
├── social/          # 127 файлов - guilds, chat, relationships, friends
├── world/           # 62 файла - locations, cities, territories
├── economy/         # 100+ файлов - trading, auctions, currencies, marketplace
├── cyberpunk/       # 30 файлов - implants, hacking, cyberspace
├── progression/     # 16 файлов - levels, achievements, skills
├── auth-expansion/  # 15 файлов - oauth, sessions, roles
├── cosmetic/        # 15 файлов - skins, customization, appearance
└── ... (и другие мелкие директории)
```

### **Новая Common Архитектура**
```
proto/openapi/
├── common/                    # ✅ SOLID/DRY Foundation
│   ├── schemas/
│   │   ├── common.yaml        # BaseEntity, AuditableEntity, VersionedEntity
│   │   ├── game-entities.yaml # CharacterEntity, ItemEntity, CombatSessionEntity
│   │   ├── economy-entities.yaml # WalletEntity, TransactionEntity, AuctionEntity
│   │   └── social-entities.yaml  # UserProfileEntity, GuildEntity, ChatMessageEntity
│   ├── responses/             # Domain-specific success/error responses
│   ├── operations/crud.yaml   # Standardized CRUD with optimistic locking
│   └── README.md             # Comprehensive architecture guide
├── example/                   # ✅ Updated template with common inheritance
└── {service}-service/         # ✅ New atomic services (10+ completed)
```

---

## 🎯 **Стратегия Миграции**

### **Фаза 1: Анализ и Планирование (1-2 недели)**

#### **1.1 Классификация Legacy Файлов**
```
🟢 BUSINESS LOGIC (СОХРАНИТЬ)
├── API endpoints с реальной функциональностью
├── Схемы с уникальными бизнес-правилами
├── Комплексные операции (trade, combat, guilds)
└── Доменные модели с валидацией

🟡 INFRASTRUCTURE (МОДЕРНИЗИРОВАТЬ)
├── Health checks → common health endpoints
├── Basic CRUD → common CRUD operations
├── Standard responses → common responses
└── Authentication → common security

🔴 OBSOLETE (УДАЛИТЬ)
├── Дублированные схемы (id, timestamps)
├── Пустые/тестовые файлы
├── Устаревшие endpoints
└── Неконсистентные паттерны
```

#### **1.2 Оценка Бизнес-Ценности**

**Высокая ценность (приоритет 1):**
- `economy/trading/trade.yaml` - P2P торговля
- `specialized/combat/combat_damage.yaml` - Расчет урона
- `social/guilds/guild_core.yaml` - Гильдии
- `system/ai/ai_adaptive.yaml` - AI адаптация

**Средняя ценность (приоритет 2):**
- `world/locations/` - Геолокации
- `progression/achievements/` - Достижения
- `cosmetic/skins/` - Кастомизация

**Низкая ценность (приоритет 3):**
- Базовые CRUD без бизнес-логики
- Дублированные схемы
- Тестовые endpoints

### **Фаза 2: Миграция по Приоритетам (4-6 недель)**

#### **2.1 Приоритет 1: Core Business Logic (2 недели)**
```
2.1.1 Economy Domain
├── trading-service/      # Из economy/trading/
├── auction-service/      # Из economy/auctions/
├── marketplace-service/  # Из economy/marketplace/
└── currency-service/     # Из economy/currencies/

2.1.2 Combat Domain
├── combat-service/       # Из specialized/combat/
├── ability-service/      # Из specialized/abilities/
├── effect-service/       # Из specialized/effects/
└── movement-service/     # Из specialized/movement/

2.1.3 Social Domain
├── guild-service/        # Из social/guilds/
├── communication-service/# Из social/communication/
├── relationship-service/ # Из social/relationships/
└── community-service/    # Из social/community/
```

#### **2.2 Приоритет 2: Extended Features (2 недели)**
```
2.2.1 World & Progression
├── location-service/     # Из world/locations/
├── achievement-service/  # Из progression/achievements/
├── level-service/        # Из progression/levels/
└── skill-service/        # Из progression/skills/

2.2.2 Character & Items
├── customization-service/# Из cosmetic/
├── appearance-service/   # Из cosmetic/appearance/
├── skin-service/         # Из cosmetic/skins/
└── collection-service/   # Из progression/collections/
```

#### **2.3 Приоритет 3: Infrastructure & AI (2 недели)**
```
2.3.1 AI & Analytics
├── ai-behavior-service/     # Из system/ai/
├── player-analytics-service/# Из analytics/
├── performance-monitoring-service/ # Из system/monitoring/
└── procedural-generation-service/  # Из system/ai/generation/

2.3.2 Infrastructure
├── notification-service/    # Из system/messaging/
├── moderation-service/      # Из social/moderation/
└── tournament-service/      # Из system/tournaments/
```

### **Фаза 3: Очистка и Оптимизация (1-2 недели)**

#### **3.1 Удаление Legacy Директорий**
```bash
# После успешной миграции и тестирования
rm -rf proto/openapi/system/
rm -rf proto/openapi/specialized/
rm -rf proto/openapi/social/
rm -rf proto/openapi/world/
rm -rf proto/openapi/economy/
# ... остальные legacy директории
```

#### **3.2 Финальная Валидация**
```bash
# Проверить все новые сервисы
./scripts/validate-all-services.sh

# Сгенерировать финальную документацию
./scripts/generate-full-api-docs.sh

# Performance testing
./scripts/run-performance-tests.sh --all-services
```

---

## 📋 **Правила Строгой Типизации OpenAPI**

### **1. Entity Inheritance (SOLID Principle)**

#### **✅ ПРАВИЛЬНО: Использовать BaseEntity**
```yaml
# В {service}-service/main.yaml
components:
  schemas:
    MyEntity:
      allOf:
        - $ref: '../common/schemas/common.yaml#/AuditableEntity'  # id, timestamps, created_by, updated_by
        - type: object
          required:
            - domain_field
          properties:
            domain_field:
              type: string
              minLength: 1
              maxLength: 100
              description: "Domain-specific field with validation"
```

#### **❌ НЕПРАВИЛЬНО: Дублировать общие поля**
```yaml
MyEntity:
  type: object
  properties:
    id: {type: string, format: uuid}           # ❌ ДУБЛИРОВАНИЕ
    created_at: {type: string, format: date-time} # ❌ ДУБЛИРОВАНИЕ
    updated_at: {type: string, format: date-time} # ❌ ДУБЛИРОВАНИЕ
    domain_field: {type: string}               # ✅ Только это нужно
```

### **2. Domain-Specific Entity Extension**

#### **Game Entities**
```yaml
PlayerCharacter:
  allOf:
    - $ref: '../common/schemas/game-entities.yaml#/CharacterEntity'  # Наследует health, stats, level
    - type: object
      properties:
        player_id: {$ref: '../common/schemas/common.yaml#/UUID'}
        cyberware_implants: {type: array, items: {type: string}}
```

#### **Economy Entities**
```yaml
PurchaseTransaction:
  allOf:
    - $ref: '../common/schemas/economy-entities.yaml#/TransactionEntity'  # Наследует amount, currency, wallet_ids
    - type: object
      properties:
        item_id: {$ref: '../common/schemas/common.yaml#/UUID'}
        quantity: {type: integer, minimum: 1}
        discount_applied: {type: number, minimum: 0, maximum: 1}
```

### **3. Строгие Типы Данных**

#### **UUID для всех ID**
```yaml
id:
  $ref: '../common/schemas/common.yaml#/UUID'  # type: string, format: uuid, maxLength: 36
```

#### **Timestamp для всех дат**
```yaml
created_at:
  $ref: '../common/schemas/common.yaml#/Timestamp'  # ISO 8601 с валидацией
```

#### **Enum для ограниченных значений**
```yaml
status:
  type: string
  enum: ["active", "inactive", "pending", "archived", "suspended"]
  default: "active"
  description: "Entity status with strict validation"

rarity:
  type: string
  enum: ["common", "uncommon", "rare", "epic", "legendary", "unique"]
  description: "Item rarity tier"
```

#### **Числовые ограничения**
```yaml
level:
  type: integer
  minimum: 1
  maximum: 50
  description: "Character level (1-50)"

health_percentage:
  type: number
  minimum: 0.0
  maximum: 100.0
  description: "Health as percentage (0.0-100.0)"
```

### **4. Validation Rules**

#### **String Validation**
```yaml
username:
  type: string
  pattern: '^[a-zA-Z0-9_-]{3,30}$'
  minLength: 3
  maxLength: 30
  description: "Username: 3-30 chars, letters/numbers/underscores/hyphens only"

email:
  type: string
  format: email
  maxLength: 255
  description: "Valid email address"
```

#### **Array Validation**
```yaml
tags:
  type: array
  items:
    type: string
    maxLength: 50
  minItems: 0
  maxItems: 10
  uniqueItems: true
  description: "Tags array: 0-10 unique strings, max 50 chars each"
```

#### **Object Validation**
```yaml
metadata:
  type: object
  properties:
    source:
      type: string
      enum: ["api", "game", "admin", "import"]
    priority:
      type: integer
      minimum: 1
      maximum: 10
  additionalProperties: false  # Запретить дополнительные поля
  description: "Strict metadata object, no additional properties allowed"
```

### **5. Request/Response Схемы**

#### **Create Request**
```yaml
CreateEntityRequest:
  allOf:
    - $ref: '../common/operations/crud.yaml#/CreateRequest'
    - type: object
      required:
        - name
        - type
      properties:
        name: {type: string, minLength: 1, maxLength: 100}
        type: {$ref: '#/components/schemas/EntityType'}
        metadata: {$ref: '#/components/schemas/EntityMetadata'}
```

#### **Update Request (Optimistic Locking)**
```yaml
UpdateEntityRequest:
  allOf:
    - $ref: '../common/operations/crud.yaml#/UpdateRequest'  # Содержит version для optimistic locking
    - type: object
      properties:
        name: {type: string, minLength: 1, maxLength: 100}
        status: {$ref: '#/components/schemas/EntityStatus'}
        metadata:
          $ref: '../common/operations/crud.yaml#/UpdateRequest/properties/metadata'
```

#### **Paginated Response**
```yaml
EntityListResponse:
  allOf:
    - $ref: '../common/schemas/common.yaml#/PaginatedResponse'
    - type: object
      properties:
        items:
          type: array
          items:
            $ref: '#/components/schemas/EntityResponse'
```

### **6. Examples для всех схем**

#### **✅ ПРАВИЛЬНО: Полные examples**
```yaml
components:
  schemas:
    CharacterEntity:
      # ... schema definition
      example:
        id: "123e4567-e89b-12d3-a456-426614174000"
        name: "V, the Mercenary"
        level: 25
        experience: 125000
        created_at: "2025-12-28T10:00:00Z"
        updated_at: "2025-12-28T10:30:00Z"
        stats:
          health: 850
          max_health: 1000
          stamina: 75
          max_stamina: 100
```

#### **❌ НЕПРАВИЛЬНО: Без examples**
```yaml
CharacterEntity:
  type: object
  properties:
    # ... без example - затрудняет понимание и тестирование
```

### **7. Operation Responses**

#### **Success Responses**
```yaml
responses:
  OK: {$ref: '../common/responses/success.yaml#/OK'}
  Created: {$ref: '../common/responses/success.yaml#/Created'}
  Updated: {$ref: '../common/responses/success.yaml#/Updated'}
  Deleted: {$ref: '../common/responses/success.yaml#/Deleted'}

  # Domain-specific
  CombatActionSuccess: {$ref: '../common/responses/success.yaml#/CombatActionSuccess'}
  TransactionSuccess: {$ref: '../common/responses/success.yaml#/TransactionSuccess'}
```

#### **Error Responses**
```yaml
responses:
  BadRequest: {$ref: '../common/responses/error.yaml#/BadRequest'}
  Unauthorized: {$ref: '../common/responses/error.yaml#/Unauthorized'}
  NotFound: {$ref: '../common/responses/error.yaml#/NotFound'}
  TooManyRequests: {$ref: '../common/responses/error.yaml#/TooManyRequests'}
```

---

## 🛠️ **Инструменты Миграции**

### **Анализ Legacy Кода**
```bash
# Найти файлы с бизнес-логикой
find proto/openapi/system/ -name "*.yaml" -exec grep -l "operationId" {} \;

# Оценить сложность схем
find proto/openapi/ -name "*.yaml" -exec wc -l {} + | sort -nr | head -20

# Найти дублированные поля
grep -r "created_at\|updated_at\|id.*uuid" proto/openapi/system/ | wc -l
```

### **Генерация Новых Сервисов**
```bash
# Создать сервис из шаблона
./scripts/create-service.sh trading-service economy

# Скопировать бизнес-логику
./scripts/migrate-business-logic.sh trading-service proto/openapi/economy/trading/

# Применить common архитектуру
./scripts/apply-common-architecture.sh trading-service
```

### **Валидация Миграции**
```bash
# Проверить типизацию
./scripts/validate-strict-typing.sh trading-service

# Проверить SOLID/DRY compliance
./scripts/validate-solid-compliance.sh trading-service

# Сгенерировать Go код
ogen --target temp --package api --clean trading-service/main.yaml
```

---

## 📈 **Метрики Успеха**

### **Количественные**
- **100% сервисов** используют common архитектуру
- **0 дублированных схем** в новых сервисах
- **100% валидация** Redocly + Ogen
- **70% сокращение кода** благодаря inheritance

### **Качественные**
- **Строгая типизация** всех полей и параметров
- **Полные examples** для всех схем
- **Optimistic locking** для конкурентных операций
- **Enterprise performance** (P99 <50ms)

---

## 🎯 **Финальный Результат**

После миграции:

```
proto/openapi/
├── common/                    # SOLID/DRY Foundation
├── example/                   # Updated template
├── trading-service/          # ✅ Из economy/trading/
├── combat-service/           # ✅ Из specialized/combat/
├── guild-service/            # ✅ Из social/guilds/
├── auction-service/          # ✅ Из economy/auctions/
└── ... (74 atomic services)
```

**Legacy директории удалены, код оптимизирован, типизация строгая!**

---

## 🚨 **Критические Правила**

### **СОХРАНИТЬ при миграции:**
- ✅ Всю бизнес-логику (trade rules, combat formulas, guild mechanics)
- ✅ API contracts (endpoints, parameters, responses)
- ✅ Business validation rules
- ✅ Domain-specific enums и constraints

### **МОДЕРНИЗИРОВАТЬ:**
- 🔄 Entity schemas → common inheritance
- 🔄 CRUD operations → common patterns
- 🔄 Responses → common responses
- 🔄 Validation → strict typing

### **УДАЛИТЬ:**
- ❌ Дублированные поля (id, timestamps)
- ❌ Legacy infrastructure code
- ❌ Inconsistent patterns
- ❌ Empty/test files

**Цель: Максимальная типизация, SOLID/DRY архитектура, enterprise-grade качество!**
