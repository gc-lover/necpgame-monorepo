# 🚀 **NECPGAME OpenAPI Reorganization Instruction for AI Agents**

## 🎯 **Цель Реструктуризации**

Преобразовать хаотичную директорию `proto/openapi` в **enterprise-grade микросервисную архитектуру** с четким разделением по бизнес-доменам, строгим следованием SOLID/DRY принципам и максимальным переиспользованием общих компонентов.

## 📊 **Текущая Проблема**

- **system/**: 471 файл в одном месте (AI, компоненты, инфраструктура, мониторинг, сеть)
- **specialized/**: 178 файлов (бой, крафт, эффекты)
- **social/**: 127 файлов (коммуникации, гильдии, отношения)
- **world/**: 62 файла без структуры
- **Мелкие директории**: 1-2 файла каждая

## 🏗️ **Целевая Архитектура Сервисов**

### **🔐 Core Infrastructure Services**

#### 1. **auth-service** (Аутентификация)
**Ответственность:** Логин, регистрация, базовая аутентификация
```
proto/openapi/auth-service/
├── main.yaml              # Основная спецификация
└── README.md             # Документация сервиса
```

#### 2. **session-service** (Управление Сессиями)
**Ответственность:** Управление пользовательскими сессиями
```
proto/openapi/session-service/
├── main.yaml
└── README.md
```

#### 3. **role-service** (Ролевая Модель)
**Ответственность:** Управление ролями и разрешениями
```
proto/openapi/role-service/
├── main.yaml
└── README.md
```

#### 4. **oauth-service** (OAuth Интеграции)
**Ответственность:** Внешние OAuth провайдеры
```
proto/openapi/oauth-service/
├── main.yaml
└── README.md
```

#### 5. **user-profile-service** (Профили Пользователей)
**Ответственность:** Управление профилями пользователей
```
proto/openapi/user-profile-service/
├── main.yaml
└── README.md
```

#### 6. **user-preference-service** (Настройки Пользователей)
**Ответственность:** Персонализация и предпочтения
```
proto/openapi/user-preference-service/
├── main.yaml
└── README.md
```

#### 7. **avatar-service** (Аватары)
**Ответственность:** Управление аватарами пользователей
```
proto/openapi/avatar-service/
├── main.yaml
└── README.md
```

#### 8. **push-notification-service** (Push Уведомления)
**Ответственность:** Push уведомления
```
proto/openapi/push-notification-service/
├── main.yaml
└── README.md
```

#### 9. **email-notification-service** (Email Уведомления)
**Ответственность:** Email рассылки
```
proto/openapi/email-notification-service/
├── main.yaml
└── README.md
```

#### 10. **in-game-notification-service** (In-Game Уведомления)
**Ответственность:** Игровые уведомления
```
proto/openapi/in-game-notification-service/
├── main.yaml
└── README.md
```

### **🎮 Core Gameplay Services**

#### 11. **combat-service** ✅ (Боевая Система)
**Ответственность:** Основная боевая механика
```
proto/openapi/combat-service/
├── main.yaml              # ✅ SOLID/DRY inheritance from game-entities
├── bundled.yaml           # ✅ Generated for deployment
├── main.yaml.backup       # ✅ Original backup
└── README.md              # ✅ Service documentation
```

#### 12. **movement-service** ✅ (Передвижение)
**Ответственность:** Система передвижения персонажа
```
proto/openapi/movement-service/
├── main.yaml              # ✅ Game domain infrastructure
├── bundled.yaml           # ✅ Generated for deployment
└── README.md              # ✅ Service documentation
```

#### 13. **effect-service** (Эффекты и Баффы)
**Ответственность:** Эффекты, баффы, дебаффы
```
proto/openapi/effect-service/
├── main.yaml
└── README.md
```

#### 14. **ability-service** (Способности)
**Ответственность:** Способности и умения персонажа
```
proto/openapi/ability-service/
├── main.yaml
└── README.md
```

#### 15. **game-mechanic-service** (Игровые Механики)
**Ответственность:** Основные игровые механики
```
proto/openapi/game-mechanic-service/
├── main.yaml
└── README.md
```

#### 16. **implant-service** (Импланты)
**Ответственность:** Кибернетические импланты
```
proto/openapi/implant-service/
├── main.yaml
└── README.md
```

#### 17. **hacking-service** (Хакинг)
**Ответственность:** Система хакинга
```
proto/openapi/hacking-service/
├── main.yaml
└── README.md
```

#### 18. **cyberware-service** (Кибер-Протезирование)
**Ответственность:** Кибернетические протезы
```
proto/openapi/cyberware-service/
├── main.yaml
└── README.md
```

#### 19. **cyberspace-service** (Киберпространство)
**Ответственность:** Навигация в киберпространстве
```
proto/openapi/cyberspace-service/
├── main.yaml
└── README.md
```

#### 20. **neural-link-service** (Нейронные Связи)
**Ответственность:** Нейронные интерфейсы
```
proto/openapi/neural-link-service/
├── main.yaml
└── README.md
```

#### 21. **level-service** (Уровни)
**Ответственность:** Система уровней персонажа
```
proto/openapi/level-service/
├── main.yaml
└── README.md
```

#### 22. **achievement-service** (Достижения)
**Ответственность:** Система достижений
```
proto/openapi/achievement-service/
├── main.yaml
└── README.md
```

#### 23. **skill-service** (Навыки)
**Ответственность:** Навыки и специализации
```
proto/openapi/skill-service/
├── main.yaml
└── README.md
```

#### 24. **experience-service** (Опыт)
**Ответственность:** Система опыта и прогрессии
```
proto/openapi/experience-service/
├── main.yaml
└── README.md
```

#### 25. **leaderboard-service** (Таблица Лидеров)
**Ответственность:** Рейтинги и лидерборды
```
proto/openapi/leaderboard-service/
├── main.yaml
└── README.md
```

### **💰 Economy Services**

#### 26. **currency-service** (Валюты)
**Ответственность:** Управление игровыми валютами
```
proto/openapi/currency-service/
├── main.yaml
└── README.md
```

#### 27. **trading-service** (Торговля)
**Ответственность:** P2P торговля между игроками
```
proto/openapi/trading-service/
├── main.yaml
└── README.md
```

#### 28. **auction-service** (Аукционы)
**Ответственность:** Система аукционов
```
proto/openapi/auction-service/
├── main.yaml
└── README.md
```

#### 29. **marketplace-service** (Маркетплейс)
**Ответственность:** Игровой маркетплейс
```
proto/openapi/marketplace-service/
├── main.yaml
└── README.md
```

#### 30. **transaction-service** (Транзакции)
**Ответственность:** Обработка платежей и транзакций
```
proto/openapi/transaction-service/
├── main.yaml
└── README.md
```

#### 31. **item-service** (Предметы)
**Ответственность:** Управление игровыми предметами
```
proto/openapi/item-service/
├── main.yaml
└── README.md
```

#### 32. **equipment-service** (Экипировка)
**Ответственность:** Система экипировки
```
proto/openapi/equipment-service/
├── main.yaml
└── README.md
```

#### 33. **crafting-service** ✅ (Крафт)
**Ответственность:** Система создания предметов
```
proto/openapi/crafting-service/
├── main.yaml              # ✅ SOLID/DRY inheritance from common entities
├── docs/index.html        # ✅ Generated documentation
└── README.md              # ✅ Service documentation
```

#### 34. **inventory-storage-service** (Хранение Инвентаря)
**Ответственность:** Хранение предметов в инвентаре
```
proto/openapi/inventory-storage-service/
├── main.yaml
└── README.md
```

#### 35. **container-service** (Контейнеры)
**Ответственность:** Система контейнеров и сумок
```
proto/openapi/container-service/
├── main.yaml
└── README.md
```

#### 36. **skin-service** (Скины)
**Ответственность:** Визуальные скины предметов
```
proto/openapi/skin-service/
├── main.yaml
└── README.md
```

#### 37. **customization-service** (Кастомизация)
**Ответственность:** Кастомизация внешнего вида
```
proto/openapi/customization-service/
├── main.yaml
└── README.md
```

#### 38. **appearance-service** (Внешний Вид)
**Ответственность:** Управление внешним видом персонажа
```
proto/openapi/appearance-service/
├── main.yaml
└── README.md
```

#### 39. **collection-service** (Коллекции)
**Ответственность:** Система коллекций и сетов
```
proto/openapi/collection-service/
├── main.yaml
└── README.md
```

### **🌐 World & Social Services**

#### 40. **location-service** (Локации)
**Ответственность:** Географические локации в мире
```
proto/openapi/location-service/
├── main.yaml
└── README.md
```

#### 41. **region-service** (Регионы)
**Ответственность:** Регионы и зоны мира
```
proto/openapi/region-service/
├── main.yaml
└── README.md
```

#### 42. **city-service** (Города)
**Ответственность:** Городские локации
```
proto/openapi/city-service/
├── main.yaml
└── README.md
```

#### 43. **territory-service** (Территории)
**Ответственность:** Захват и контроль территорий
```
proto/openapi/territory-service/
├── main.yaml
└── README.md
```

#### 44. **world-event-service** (Мировые События)
**Ответственность:** Глобальные события мира
```
proto/openapi/world-event-service/
├── main.yaml
└── README.md
```

#### 45. **friend-service** (Друзья)
**Ответственность:** Система друзей
```
proto/openapi/friend-service/
├── main.yaml
└── README.md
```

#### 46. **communication-service** (Коммуникация)
**Ответственность:** Чат и общение между игроками
```
proto/openapi/communication-service/
├── main.yaml
└── README.md
```

#### 47. **community-service** (Сообщества)
**Ответственность:** Сообщества и группы игроков
```
proto/openapi/community-service/
├── main.yaml
└── README.md
```

#### 48. **relationship-service** (Отношения)
**Ответственность:** Социальные отношения между персонажами
```
proto/openapi/relationship-service/
├── main.yaml
└── README.md
```

#### 49. **moderation-service** (Модерация)
**Ответственность:** Модерация контента и поведения
```
proto/openapi/moderation-service/
├── main.yaml
└── README.md
```

#### 50. **guild-service** (Гильдии)
**Ответственность:** Система гильдий
```
proto/openapi/guild-service/
├── main.yaml
└── README.md
```

#### 51. **faction-service** (Фракции)
**Ответственность:** Политические фракции
```
proto/openapi/faction-service/
├── main.yaml
└── README.md
```

#### 52. **alliance-service** (Альянсы)
**Ответственность:** Союзы между гильдиями/фракциями
```
proto/openapi/alliance-service/
├── main.yaml
└── README.md
```

#### 53. **clan-war-service** (Клановые Войны)
**Ответственность:** Военные конфликты между кланами
```
proto/openapi/clan-war-service/
├── main.yaml
└── README.md
```

#### 54. **diplomacy-service** (Дипломатия)
**Ответственность:** Дипломатические отношения
```
proto/openapi/diplomacy-service/
├── main.yaml
└── README.md
```

### **🏟️ Competition Services**

#### 55. **pvp-arena-service** (PvP Арены)
**Ответственность:** PvP бои на аренах
```
proto/openapi/pvp-arena-service/
├── main.yaml
└── README.md
```

#### 56. **tournament-service** (Турниры)
**Ответственность:** Турнирная система
```
proto/openapi/tournament-service/
├── main.yaml
└── README.md
```

#### 57. **matchmaking-service** (Матчинг)
**Ответственность:** Поиск подходящих оппонентов
```
proto/openapi/matchmaking-service/
├── main.yaml
└── README.md
```

#### 58. **ranking-service** (Рейтинги)
**Ответственность:** Система рейтингов игроков
```
proto/openapi/ranking-service/
├── main.yaml
└── README.md
```

#### 59. **spectator-service** (Наблюдение)
**Ответственность:** Режим наблюдателя за боями
```
proto/openapi/spectator-service/
├── main.yaml
└── README.md
```

#### 60. **ai-companion-service** (AI Компаньоны)
**Ответственность:** Искусственный интеллект компаньонов
```
proto/openapi/ai-companion-service/
├── main.yaml
└── README.md
```

#### 61. **pet-service** (Питомцы)
**Ответственность:** Система питомцев
```
proto/openapi/pet-service/
├── main.yaml
└── README.md
```

#### 62. **summon-service** (Призывы)
**Ответственность:** Система призыва существ
```
proto/openapi/summon-service/
├── main.yaml
└── README.md
```

### **📊 Analytics & AI Services**

#### 63. **player-analytics-service** (Аналитика Игроков)
**Ответственность:** Анализ поведения игроков
```
proto/openapi/player-analytics-service/
├── main.yaml
└── README.md
```

#### 64. **game-metrics-service** (Метрики Игры)
**Ответственность:** Сбор игровых метрик
```
proto/openapi/game-metrics-service/
├── main.yaml
└── README.md
```

#### 65. **behavioral-data-service** (Поведенческие Данные)
**Ответственность:** Анализ поведенческих паттернов
```
proto/openapi/behavioral-data-service/
├── main.yaml
└── README.md
```

#### 66. **performance-monitoring-service** (Мониторинг Производительности)
**Ответственность:** Мониторинг производительности системы
```
proto/openapi/performance-monitoring-service/
├── main.yaml
└── README.md
```

#### 67. **ai-behavior-service** (AI Поведение)
**Ответственность:** Поведение ИИ в игре
```
proto/openapi/ai-behavior-service/
├── main.yaml
└── README.md
```

#### 68. **procedural-generation-service** (Процедурная Генерация)
**Ответственность:** Генерация контента алгоритмами
```
proto/openapi/procedural-generation-service/
├── main.yaml
└── README.md
```

#### 69. **machine-learning-service** (Машинное Обучение)
**Ответственность:** ML модели для игры
```
proto/openapi/machine-learning-service/
├── main.yaml
└── README.md
```

#### 70. **adaptive-system-service** (Адаптивные Системы)
**Ответственность:** Адаптация под игроков
```
proto/openapi/adaptive-system-service/
├── main.yaml
└── README.md
```

### **🔗 Integration Services**

#### 71. **referral-service** (Рефералы)
**Ответственность:** Реферальная система
```
proto/openapi/referral-service/
├── main.yaml
└── README.md
```

#### 72. **reward-service** (Награды)
**Ответственность:** Система наград и поощрений
```
proto/openapi/reward-service/
├── main.yaml
└── README.md
```

#### 73. **affiliate-service** (Партнерская Программа)
**Ответственность:** Аффилиат система
```
proto/openapi/affiliate-service/
├── main.yaml
└── README.md
```

#### 74. **tracking-service** (Отслеживание)
**Ответственность:** Трекинг действий пользователей
```
proto/openapi/tracking-service/
├── main.yaml
└── README.md
```

## 📋 **Маппинг Файлов по Сервисам**

### **Из system/ (471 файл)**
- **ai-behavior-service:** `ai/`, `ai_*`, `ai-behavior-*`
- **performance-monitoring-service:** `monitoring/`, `monitoring-*`
- **infrastructure:** → Удалить (не API)
- **core:** → Распределить по соответствующим сервисам

### **Из specialized/ (178 файлов)**
- **combat-service:** `combat/`, `combat-*` (кроме hacking)
- **effect-service:** `effects/`, `effect-*`
- **movement-service:** `movement/`, `movement-*`
- **crafting-service:** `crafting/`, `crafting-*`
- **hacking-service:** `combat/combat-hacking-*`

### **Из social/ (127 файлов)**
- **communication-service:** `communication/`, `chat-*`, `message-*`
- **friend-service:** `friends/`, `friend-*`
- **community-service:** `community/`, `community-*`
- **guild-service:** `guilds/`, `guild-*`
- **moderation-service:** `moderation/`, `moderation-*`

### **Из world/ (62 файла)**
- **location-service:** `locations/`, `location-*`
- **region-service:** `regions/`, `region-*`
- **city-service:** `cities/`, `city-*`
- **world-event-service:** `world-events/`, `world-event-*`

### **Из auth-expansion/ (15 файлов)**
- **auth-service:** `auth-*`, `login-*`, `register-*`
- **session-service:** `session-*`, `auth_session_*`
- **role-service:** `roles-*`, `permissions-*`
- **oauth-service:** `oauth-*`, `social-auth-*`

### **Из cyberpunk/ (30 файлов)**
- **implant-service:** `implant-*`, `cyberware-implant-*`
- **hacking-service:** `hacking-*`, `combat-hacking-*`
- **cyberware-service:** `cyberware-*`
- **cyberspace-service:** `cyberspace-*`
- **neural-link-service:** `neural-*`

### **Из progression/ (16 файлов)**
- **level-service:** `level-*`, `progression-level-*`
- **achievement-service:** `achievement-*`
- **skill-service:** `skill-*`
- **experience-service:** `experience-*`, `xp-*`
- **leaderboard-service:** `leaderboard-*`

### **Из economy/ (100+ файлов)**
- **currency-service:** `currencies/`, `currency-*`
- **trading-service:** `trading/`, `trade-*`
- **auction-service:** `auctions/`, `auction-*`
- **marketplace-service:** `marketplace-*`
- **transaction-service:** `transaction-*`, `payment-*`

### **Из cosmetic/ (15 файлов)**
- **skin-service:** `skin-*`, `cosmetic-skin-*`
- **customization-service:** `customization-*`
- **appearance-service:** `appearance-*`
- **collection-service:** `collection-*`

## 🛠️ **Строгие Правила Реорганизации**

### **📁 Иерархия Директорий (SOLID/DRY Domain Separation)**
```
proto/openapi/
├── common/                    # ✅ SOLID/DRY FOUNDATION (ОБНОВЛЕНА)
│   ├── schemas/
│   │   ├── common.yaml        # BaseEntity, AuditableEntity, VersionedEntity
│   │   ├── game-entities.yaml    # CharacterEntity, CombatActionEntity, AbilityEntity
│   │   ├── economy-entities.yaml # WalletEntity, TransactionEntity, AuctionEntity
│   │   ├── social-entities.yaml  # UserProfileEntity, GuildEntity, ChatMessageEntity
│   │   └── infrastructure-entities.yaml # UserAccountEntity, SessionEntity, AuditLogEntity
│   ├── responses/             # Domain-specific success/error responses
│   ├── operations/crud.yaml   # Standardized CRUD with optimistic locking
│   ├── security/              # Authentication schemes
│   └── README.md             # Architecture documentation
├── {service-name}-service/    # ✅ ATOMIC MICROSERVICES
│   ├── main.yaml             # ОСНОВНАЯ СПЕЦИФИКАЦИЯ С COMMON INHERITANCE
│   └── README.md             # ДОКУМЕНТАЦИЯ СЕРВИСА
├── example/                   # ✅ UPDATED TEMPLATE WITH DOMAIN SEPARATION
│   ├── main.yaml             # Enterprise-grade template with common inheritance
│   └── README.md            # Comprehensive architecture guide
├── MIGRATION_GUIDE.md       # ✅ Domain Separation Migration Strategy
└── REORGANIZATION_INSTRUCTION.md # This file - Updated for SOLID/DRY
```

### **📝 Нейминг Файлов**
```
{service-name}-service/
├── main.yaml                    # Главная спецификация сервиса
├── README.md                    # Документация
├── auth.yaml                    # kebab-case, домен сервиса
├── sessions.yaml                # Конкретные операции
├── user-management.yaml         # Управление ресурсами
├── profile-settings.yaml        # Настройки и конфигурации
└── security-policies.yaml       # Политики безопасности
```

### **🏷️ Нейминг Операций**
- `create{Resource}` - Создание
- `get{Resource}` - Получение по ID
- `list{Resources}` - Список с фильтрами
- `update{Resource}` - Обновление
- `delete{Resource}` - Удаление
- `{Resource}Action` - Специфические действия

## 🔄 **Миграция Существующих Файлов**

### **Шаг 1: Анализ Исходного Файла**
```bash
# Прочитать содержимое файла
cat proto/openapi/system/ai/ai_combat.yaml

# Определить домен
# → gameplay-service/combat/
```

### **Шаг 2: Создание Новой Структуры**
```bash
# Создать директорию сервиса
mkdir -p proto/openapi/gameplay-service/combat

# Скопировать и адаптировать файл
cp proto/openapi/system/ai/ai_combat.yaml \
   proto/openapi/gameplay-service/combat/ai-combat.yaml
```

### **Шаг 3: Адаптация Содержимого**
- Заменить `operationId` на новый формат
- Обновить `$ref` на новые пути
- Добавить недостающие компоненты
- Убедиться в соответствии шаблону

### **Шаг 4: Валидация**
```bash
# Проверить валидность
npx @redocly/cli lint proto/openapi/gameplay-service/main.yaml

# Сгенерировать Go код
ogen --target /tmp/test --package api --clean \
     proto/openapi/gameplay-service/main.yaml
```

## 📚 **Базовая Структура Файла**

### **main.yaml каждого сервиса**
```yaml
openapi: 3.0.3
info:
  title: {ServiceName} API
  description: |
    **Enterprise-grade API for {Service Domain}**

    ## Domain Purpose
    {Describe service responsibility}

    ## Performance Targets
    - P99 Latency: <50ms
    - Memory: <50KB per instance
    - Concurrent users: 10,000+

  version: "1.0.0"
  contact:
    name: NECPGAME API Support
    email: api@necpgame.com
  license:
    name: MIT

servers:
- url: https://api.necpgame.com/v1/{service-name}
  description: Production server
- url: https://staging-api.necpgame.com/v1/{service-name}
  description: Staging server
- url: http://localhost:8080/api/v1/{service-name}
  description: Local development server

security:
- BearerAuth: []

tags:
- name: {Domain}
  description: Core {domain} operations
- name: Health Monitoring
  description: System health and performance monitoring

paths:
  /health:
    get:
      operationId: {serviceName}HealthCheck
      # ... health endpoint implementation

components:
  responses:
    # Use common responses
    OK:
      $ref: ../common/responses/success.yaml#/OK
    BadRequest:
      $ref: ../common/responses/error.yaml#/BadRequest

  schemas:
    # Use common schemas
    Error:
      $ref: ../common/schemas/error.yaml#/Error
    HealthResponse:
      $ref: ../common/schemas/health.yaml#/HealthResponse

  securitySchemes:
    BearerAuth:
      $ref: ../common/security/security.yaml#/BearerAuth
```

### **README.md каждого сервиса**
```markdown
# {ServiceName} Service - OpenAPI Specification

## 📋 **Назначение**

{Описание ответственности сервиса}

## 🎯 **Функциональность**

- **{Feature1}**: {Описание}
- **{Feature2}**: {Описание}

## 📁 **Структура**

```
{service-name}-service/
├── main.yaml           # Основная спецификация
├── README.md          # Эта документация
├── {domain1}.yaml     # {Описание домена}
├── {operation}.yaml   # {Описание операции}
├── {resource}-management.yaml # Управление ресурсами
└── {feature}.yaml     # Дополнительные возможности
```

## 🔗 **Зависимости**

- **common**: Общие схемы и ответы
- **{other-service}**: {Причина зависимости}

## 📊 **Performance**

- **P99 Latency**: <50ms
- **Memory per Instance**: <50KB
- **Concurrent Users**: 10,000+

## 🚀 **Использование**

### Валидация
```bash
npx @redocly/cli lint main.yaml
```

### Генерация Go кода
```bash
ogen --target ../../services/{service-name}-go/pkg/api \
     --package api --clean main.yaml
```

### Документация
```bash
npx @redocly/cli build-docs main.yaml -o docs/index.html
```
```

## 🔗 **SOLID/DRY Domain Separation - Переиспользование Common Компонентов**

### **🎯 Domain-Specific Entity Inheritance (SOLID Principle)**

#### **1. Game Domain Entities**
```yaml
# В combat-service/main.yaml
components:
  schemas:
    CombatSession:
      allOf:
        - $ref: '../common/schemas/game-entities.yaml#/CombatSessionEntity'  # Наследует participants, status, turn_order
        - type: object
          properties:
            combat_rules: {type: string, enum: ['standard', 'hardcore', 'tournament']}
```

#### **2. Economy Domain Entities**
```yaml
# В trading-service/main.yaml
components:
  schemas:
    PlayerTrade:
      allOf:
        - $ref: '../common/schemas/economy-entities.yaml#/TradeSessionEntity'  # Наследует initiator, participants, status
        - type: object
          properties:
            trade_location: {type: string, enum: ['safe_zone', 'combat_zone', 'guild_hall']}
```

#### **3. Social Domain Entities**
```yaml
# В guild-service/main.yaml
components:
  schemas:
    PlayerGuild:
      allOf:
        - $ref: '../common/schemas/social-entities.yaml#/GuildEntity'  # Наследует name, leader, members, reputation
        - type: object
          properties:
            guild_type: {type: string, enum: ['mercenary', 'corporation', 'nomad', 'gang']}
```

#### **4. Infrastructure Domain Entities**
```yaml
# В auth-service/main.yaml
components:
  schemas:
    SecureSession:
      allOf:
        - $ref: '../common/schemas/infrastructure-entities.yaml#/SessionEntity'  # Наследует token, expires_at, ip_address
        - type: object
          properties:
            security_level: {type: string, enum: ['standard', 'elevated', 'maximum']}
```

### **📋 Обязательные $ref для всех сервисов (UPDATED)**
```yaml
components:
  # Domain-specific success responses
  responses:
    OK: $ref: '../common/responses/success.yaml#/OK'
    Created: $ref: '../common/responses/success.yaml#/Created'
    Updated: $ref: '../common/responses/success.yaml#/Updated'
    Deleted: $ref: '../common/responses/success.yaml#/Deleted'

    # Domain-specific responses (use appropriate for your domain)
    CombatActionSuccess: $ref: '../common/responses/success.yaml#/CombatActionSuccess'
    TransactionSuccess: $ref: '../common/responses/success.yaml#/TransactionSuccess'
    FriendRequestSuccess: $ref: '../common/responses/success.yaml#/FriendRequestSuccess'

    # Error responses
    BadRequest: $ref: '../common/responses/error.yaml#/BadRequest'
    Unauthorized: $ref: '../common/responses/error.yaml#/Unauthorized'
    NotFound: $ref: '../common/responses/error.yaml#/NotFound'
    TooManyRequests: $ref: '../common/responses/error.yaml#/TooManyRequests'

  # Common schemas (legacy - prefer domain-specific)
  schemas:
    Error: $ref: '../common/schemas/common.yaml#/Error'
    HealthResponse: $ref: '../common/schemas/health.yaml#/HealthResponse'

  # Security schemes
  securitySchemes:
    BearerAuth: $ref: '../common/security/security.yaml#/BearerAuth'
    ApiKeyAuth: $ref: '../common/security/security.yaml#/ApiKeyAuth'
    ServiceAuth: $ref: '../common/security/security.yaml#/ServiceAuth'
```

### **🏗️ Расширение Domain-Specific Схем (DRY Principle)**
```yaml
# ✅ ПРАВИЛЬНО: Domain inheritance
MyGameEntity:
  allOf:
    - $ref: '../common/schemas/game-entities.yaml#/CharacterEntity'  # 20+ полей автоматически
    - type: object
      properties:
        cyberware_level: {type: integer, minimum: 0, maximum: 20}  # Только уникальные поля

# ✅ ПРАВИЛЬНО: Economy inheritance
MyTransaction:
  allOf:
    - $ref: '../common/schemas/economy-entities.yaml#/TransactionEntity'  # amount, currency, wallets
    - type: object
      properties:
        item_discount: {type: number, minimum: 0, maximum: 1}

# ❌ НЕПРАВИЛЬНО: Дублирование (запрещено)
MyEntity:
  type: object
  properties:
    id: {type: string, format: uuid}        # ДУБЛИРОВАНИЕ ❌
    created_at: {type: string, format: date-time} # ДУБЛИРОВАНИЕ ❌
    name: {type: string}                    # Только это разрешено ✅
```

## ✅ **Валидационные Проверки (UPDATED for SOLID/DRY)**

### **Обязательные для каждого сервиса**
- [ ] `main.yaml` соответствует шаблону `example/main.yaml` с domain inheritance
- [ ] Все entity наследуют от domain-specific common schemas (game-entities, economy-entities, etc.)
- [ ] **НЕТ дублирования** общих полей (id, created_at, updated_at, etc.)
- [ ] Используются domain-specific responses (`CombatActionSuccess`, `TransactionSuccess`, etc.)
- [ ] `operationId` уникальны в рамках сервиса
- [ ] Есть health endpoints (`/health`, `/health/batch`, `/health/ws`)
- [ ] **Строгая типизация**: все поля имеют типы, ограничения, examples
- [ ] **Optimistic locking** для конкурентных операций (VersionedEntity)
- [ ] Redocly lint проходит без ошибок
- [ ] Ogen генерирует валидный Go код
- [ ] Есть `README.md` с документацией domain-specific usage

### **Команды валидации**
```bash
# Линтинг
npx @redocly/cli lint proto/openapi/{service}-service/main.yaml

# Бандлинг
npx @redocly/cli bundle proto/openapi/{service}-service/main.yaml -o bundled.yaml

# Генерация Go
ogen --target /tmp/test --package api --clean bundled.yaml
cd /tmp/test && go mod init test && go build .

# Документация
npx @redocly/cli build-docs proto/openapi/{service}-service/main.yaml -o docs/index.html
```

## 🎯 **Приоритизация Миграции**

### **Фаза 1: Core Infrastructure (3-5 дней)**
1-10. **Базовые сервисы аутентификации и коммуникаций**
- `auth-service`, `session-service`, `role-service`, `oauth-service`
- `user-profile-service`, `user-preference-service`, `avatar-service`
- `push-notification-service`, `email-notification-service`, `in-game-notification-service`

### **Фаза 2: Core Gameplay (5-7 дней)**
11-25. **Основные игровые механики**
- `combat-service`, `movement-service`, `effect-service`, `ability-service`
- `game-mechanic-service`
- `implant-service`, `hacking-service`, `cyberware-service`, `cyberspace-service`, `neural-link-service`
- `level-service`, `achievement-service`, `skill-service`, `experience-service`, `leaderboard-service`

### **Фаза 3: Economy (4-6 дней)**
26-39. **Экономические системы**
- `currency-service`, `trading-service`, `auction-service`, `marketplace-service`, `transaction-service`
- `item-service`, `equipment-service`, `crafting-service`, `inventory-storage-service`, `container-service`
- `skin-service`, `customization-service`, `appearance-service`, `collection-service`

### **Фаза 4: World & Social (4-6 дней)**
40-54. **Мир и социальные взаимодействия**
- `location-service`, `region-service`, `city-service`, `territory-service`, `world-event-service`
- `friend-service`, `communication-service`, `community-service`, `relationship-service`, `moderation-service`
- `guild-service`, `faction-service`, `alliance-service`, `clan-war-service`, `diplomacy-service`

### **Фаза 5: Competition & AI (3-5 дней)**
55-69. **Конкуренция и искусственный интеллект**
- `pvp-arena-service`, `tournament-service`, `matchmaking-service`, `ranking-service`, `spectator-service`
- `ai-companion-service`, `pet-service`, `summon-service`
- `player-analytics-service`, `game-metrics-service`, `behavioral-data-service`, `performance-monitoring-service`
- `ai-behavior-service`, `procedural-generation-service`, `machine-learning-service`, `adaptive-system-service`

### **Фаза 6: Integration (2-3 дня)**
70-74. **Интеграционные сервисы**
- `referral-service`, `reward-service`, `affiliate-service`, `tracking-service`

## 🚨 **Критические Правила**

### **ЗАПРЕЩЕНО**
- [ ] Создавать файлы вне `{service-name}-service/`
- [ ] Дублировать схемы из `common/`
- [ ] Использовать некорректный нейминг
- [ ] Нарушать структуру директорий
- [ ] Удалять оригинальные файлы до полной миграции

### **ОБЯЗАТЕЛЬНО**
- [ ] Следовать шаблону `example/`
- [ ] Использовать `$ref` на `../common/`
- [ ] Проходить валидацию перед коммитом
- [ ] Документировать в `README.md`
- [ ] Тестировать генерацию Go кода

## 🔍 **Мониторинг Прогресса**

### **Метрики Успеха (UPDATED for SOLID/DRY)**
- ✅ Все 74 сервиса имеют валидный `main.yaml` с domain inheritance
- ✅ **0 дублированных схем** - 96.8% сокращение кода
- ✅ **100% domain-specific common usage** (game-entities, economy-entities, etc.)
- ✅ **Строгая типизация** всех entity (enum, patterns, min/max, examples)
- ✅ **Optimistic locking** для конкурентных операций
- ✅ Все файлы проходят Redocly lint + Ogen code generation
- ✅ Каждый сервис атомарен (Single Responsibility) + domain cohesion
- ✅ **SOLID/DRY compliance** - inheritance вместо дублирования

### **Команды проверки**
```bash
# Проверить использование common
find proto/openapi/ -name "*.yaml" -exec grep -l "\$ref.*common" {} \;

# Проверить дубликаты
find proto/openapi/ -name "*.yaml" -exec grep -l "Error:" {} \; | wc -l

# Проверить что все сервисы имеют main.yaml и README.md
find proto/openapi/ -name "*-service" -type d | while read dir; do
  [ -f "$dir/main.yaml" ] && [ -f "$dir/README.md" ] || echo "Missing files in $dir"
done

# Проверить валидность всех сервисов
for service in proto/openapi/*-service/; do
  if [ -f "$service/main.yaml" ]; then
    npx @redocly/cli lint "$service/main.yaml" 2>/dev/null || echo "Lint failed: $service"
  fi
done

# Проверить генерацию Go кода для всех сервисов
for service in proto/openapi/*-service/; do
  if [ -f "$service/main.yaml" ]; then
    ogen --target /tmp/test-$service --package api --clean "$service/main.yaml" 2>/dev/null || echo "Code gen failed: $service"
  fi
done
```

## 🎯 **Финальный Результат (SOLID/DRY Domain Separation)**

После реорганизации с domain separation:

```
proto/openapi/
├── common/                    # ✅ SOLID/DRY FOUNDATION (UPDATED)
│   ├── schemas/
│   │   ├── common.yaml        # BaseEntity, AuditableEntity, VersionedEntity
│   │   ├── game-entities.yaml    # CharacterEntity, CombatActionEntity, AbilityEntity
│   │   ├── economy-entities.yaml # WalletEntity, TransactionEntity, AuctionEntity
│   │   ├── social-entities.yaml  # UserProfileEntity, GuildEntity, ChatMessageEntity
│   │   └── infrastructure-entities.yaml # UserAccountEntity, SessionEntity, AuditLogEntity
│   ├── responses/             # Domain-specific success/error responses
│   ├── operations/crud.yaml   # Standardized CRUD with optimistic locking
│   └── README.md             # Architecture documentation
├── example/                   # ✅ UPDATED TEMPLATE WITH DOMAIN INHERITANCE
├── MIGRATION_GUIDE.md       # ✅ Domain Separation Migration Strategy
├── trading-service/          # ✅ Economy domain - inherits from economy-entities
├── combat-service/           # ✅ Game domain - SOLID/DRY inheritance from game-entities
├── crafting-service/         # ✅ Economy domain - SOLID/DRY inheritance from common entities
├── movement-service/         # ✅ Game domain - infrastructure with common schemas
├── guild-service/            # ✅ Social domain - inherits from social-entities
├── auth-service/             # ✅ Infrastructure domain - inherits from infrastructure-entities
├── auction-service/          # ✅ Economy domain - marketplace logic
├── ability-service/          # ✅ Game domain - character abilities
├── ... (74 atomic services with domain inheritance)
└── REORGANIZATION_INSTRUCTION.md # ✅ This file - Updated for SOLID/DRY
```

**Итого: 74 атомарных микросервиса с domain inheritance = ~148 файлов + 20+ domain entity файлов, вместо 1000+ файлов с дублированием**

---

## 🚀 **Начало Работы**

1. **Выбрать сервис** из списка выше
2. **Создать директорию** `proto/openapi/{service-name}-service/`
3. **Скопировать шаблон** из `example/`
4. **Адаптировать** под специфику сервиса
5. **Перенести файлы** из старых директорий
6. **Провести валидацию**
7. **Закоммитить** с понятным сообщением

**Каждый агент работает над одним сервисом за раз!**

## 🔄 **Детальный Workflow для AI Агентов**

### **Шаг 1: Выбор и Планирование Сервиса**
```bash
# Проверить что сервис еще не создан
ls -la proto/openapi/{service-name}-service/

# Изучить существующие файлы из маппинга
find proto/openapi/ -name "*{keyword}*" -type f | head -10

# Оценить объем работы
find proto/openapi/ -name "*{keyword}*" -type f | wc -l
```

### **Шаг 2: Создание Базовой Структуры**
```bash
# Создать директорию сервиса
mkdir -p proto/openapi/{service-name}-service

# Скопировать и адаптировать шаблон
cp proto/openapi/example/main.yaml proto/openapi/{service-name}-service/main.yaml
cp proto/openapi/example/README.md proto/openapi/{service-name}-service/README.md

# Заменить placeholders в main.yaml
sed -i 's/{ServiceName}/{Actual Service Name}/g' main.yaml
sed -i 's/{service-name}/{actual-service-name}/g' main.yaml
sed -i 's/{Service Domain}/{actual domain}/g' main.yaml
```

### **Шаг 3: Анализ и Миграция Файлов**
```bash
# Найти все релевантные файлы
find proto/openapi/ -name "*{keyword}*" -type f > /tmp/files_to_migrate.txt

# Для каждого файла:
# 1. Прочитать содержимое
# 2. Извлечь полезные части (paths, schemas, responses)
# 3. Адаптировать под новый формат
# 4. Интегрировать в main.yaml
```

### **Шаг 4: Валидация и Тестирование**
```bash
# Линтинг
npx @redocly/cli lint proto/openapi/{service-name}-service/main.yaml

# Бандлинг для проверки $ref
npx @redocly/cli bundle proto/openapi/{service-name}-service/main.yaml -o /tmp/bundled.yaml

# Генерация Go кода
ogen --target /tmp/codegen-test --package api --clean /tmp/bundled.yaml

# Компиляция
cd /tmp/codegen-test && go mod init test && go mod tidy && go build .
```

### **Шаг 5: Финализация Документации**
```bash
# Обновить README.md с реальными данными
# Добавить примеры использования
# Указать зависимости от других сервисов

# Сгенерировать HTML документацию
npx @redocly/cli build-docs proto/openapi/{service-name}-service/main.yaml \
  -o proto/openapi/{service-name}-service/docs/index.html
```

### **Шаг 6: Финальная Проверка**
```bash
# Запустить все проверки
./scripts/validate-service.sh {service-name}-service

# Проверить что сервис соответствует всем требованиям
./scripts/check-service-compliance.sh {service-name}-service
```

### **Шаг 7: Коммит и Отчет**
```bash
# Сделать коммит
git add proto/openapi/{service-name}-service/
git commit -m "[REORG] Add {service-name}-service - {X} endpoints, {Y} schemas

- Migrated from: {old directories}
- Endpoints: {list of endpoints}
- Dependencies: {other services}
- Validation: ✅ Redocly, ✅ Ogen, ✅ Go build"

# Создать отчет для менеджера
./scripts/generate-service-report.sh {service-name}-service > /tmp/service-report.md
```

## 📋 **Примеры Готовых Сервисов**

### **Пример 1: auth-service (Простой Сервис)**
```
proto/openapi/auth-service/
├── main.yaml          # 150 строк - базовая аутентификация
├── README.md          # 50 строк - документация
└── docs/
    └── index.html     # Сгенерированная документация
```

**main.yaml содержит:**
- `/auth/login` (POST) - вход в систему
- `/auth/register` (POST) - регистрация
- `/auth/logout` (POST) - выход
- `/health` - health check

### **Пример 2: combat-service (Сложный Сервис)**
```
proto/openapi/combat-service/
├── main.yaml          # 400 строк - полная боевая система
├── README.md          # 80 строк - детальная документация
└── docs/
    └── index.html
```

**main.yaml содержит:**
- `/combat/initiate` - начать бой
- `/combat/action` - выполнить действие
- `/combat/status` - статус боя
- `/combat/finish` - завершить бой
- Health endpoints

## 🔗 **Обработка Кросс-Сервисных Зависимостей**

### **Типы Зависимостей**

#### **1. Данные (Data Dependencies)**
```yaml
# user-profile-service зависит от auth-service
paths:
  /users/{userId}/profile:
    get:
      parameters:
        - name: userId
          schema:
            $ref: '../auth-service/main.yaml#/components/schemas/UserId'
```

#### **2. Авторизация (Auth Dependencies)**
```yaml
# Все сервисы используют общий BearerAuth из common
security:
  - BearerAuth: []
```

#### **3. Бизнес-логика (Business Logic Dependencies)**
```yaml
# economy-service зависит от inventory-service
components:
  schemas:
    Transaction:
      properties:
        items:
          $ref: '../inventory-service/main.yaml#/components/schemas/ItemList'
```

### **Правила Разрешения Зависимостей**

1. **Избегать циклических зависимостей**
2. **Использовать events для loosely coupled коммуникации**
3. **Документировать все зависимости в README.md**
4. **Тестировать сервисы изолированно**

## 🛡️ **Версионирование API**

### **Semantic Versioning**
```
MAJOR.MINOR.PATCH
├── MAJOR - breaking changes
├── MINOR - new features (backward compatible)
└── PATCH - bug fixes (backward compatible)
```

### **Version Headers**
```yaml
paths:
  /api/v1/resource:
    get:
      parameters:
        - name: X-API-Version
          in: header
          schema:
            type: string
            enum: ["1.0", "1.1", "2.0"]
          required: false
```

### **Version Strategy**
```yaml
# В info.version указывать текущую версию
info:
  version: "1.0.0"

# В URL path для major versions
servers:
  - url: https://api.necpgame.com/v1/{service-name}
  - url: https://api.necpgame.com/v2/{service-name}  # Для breaking changes
```

## 📚 **Генерация и Публикация Документации**

### **Локальная Документация**
```bash
# Генерация HTML для каждого сервиса
for service in proto/openapi/*-service/; do
  if [ -f "$service/main.yaml" ]; then
    npx @redocly/cli build-docs "$service/main.yaml" \
      -o "$service/docs/index.html" \
      --title "$service API Documentation"
  fi
done
```

### **Централизованная Документация**
```bash
# Создать общую документацию всех сервисов
./scripts/generate-full-api-docs.sh

# Опубликовать на внутреннем портале
./scripts/publish-docs.sh
```

### **API Playground**
```bash
# Генерация интерактивной документации
npx @redocly/cli build-docs proto/openapi/{service}-service/main.yaml \
  --template swagger-ui \
  -o docs/playground/{service}-service.html
```

## 🔍 **Мониторинг и Observability**

### **Метрики Производительности**
```yaml
# В каждом health endpoint
/health:
  get:
    responses:
      '200':
        content:
          application/json:
            schema:
              $ref: '../common/schemas/health.yaml#/HealthResponse'
        headers:
          X-Response-Time:
            schema:
              type: integer
              description: Response time in milliseconds
          X-Memory-Usage:
            schema:
              type: integer
              description: Memory usage in KB
```

### **Логирование**
```yaml
# Структурированное логирование
components:
  schemas:
    LogEntry:
      type: object
      properties:
        timestamp: {type: string, format: date-time}
        level: {type: string, enum: [DEBUG, INFO, WARN, ERROR]}
        service: {type: string}
        operation: {type: string}
        user_id: {type: string, format: uuid}
        request_id: {type: string}
        message: {type: string}
        metadata: {type: object}
```

### **Distributed Tracing**
```yaml
# Trace headers
paths:
  /api/endpoint:
    get:
      parameters:
        - name: X-Request-ID
          in: header
          schema: {type: string}
        - name: X-Trace-ID
          in: header
          schema: {type: string}
        - name: X-Parent-Span-ID
          in: header
          schema: {type: string}
```

## 🚨 **Процедура Отката (Rollback)**

### **При обнаружении проблемы**
```bash
# 1. Остановить деплой
kubectl rollout pause deployment/{service-name}

# 2. Откатить код
git revert HEAD --no-edit

# 3. Откатить в Kubernetes
kubectl rollout undo deployment/{service-name}

# 4. Проверить восстановление
kubectl rollout status deployment/{service-name}
```

### **Анализ инцидента**
```bash
# Собрать логи
kubectl logs deployment/{service-name} --previous

# Проверить метрики
kubectl exec -it deployment/{service-name} -- curl http://localhost:9090/metrics

# Создать отчет
./scripts/generate-incident-report.sh {service-name} > incident-report.md
```

## 🧪 **Тестирование Интеграции**

### **Contract Testing**
```bash
# Тестировать контракты между сервисами
./scripts/run-contract-tests.sh

# Проверить compatibility
./scripts/check-api-compatibility.sh {service-a} {service-b}
```

### **End-to-End Testing**
```bash
# Полный цикл тестирования
./scripts/run-e2e-tests.sh --services "{service-list}"

# Performance testing
./scripts/run-performance-tests.sh --service {service-name}
```

### **Chaos Engineering**
```bash
# Симулировать сбои
./scripts/chaos-test.sh --service {service-name} --failure-type network-delay

# Тестировать resilience
./scripts/resilience-test.sh --service {service-name}
```

## 🎯 **Финальные Контрольные Списки**

### **Pre-Commit Checklist**
- [ ] Redocly lint проходит без ошибок
- [ ] Ogen генерирует код без ошибок
- [ ] Go код компилируется
- [ ] Все `$ref` указывают на существующие компоненты
- [ ] README.md заполнен и актуален
- [ ] Нет дублированных схем
- [ ] Все эндпоинты имеют operationId
- [ ] Есть health endpoints

### **Post-Deploy Checklist**
- [ ] Сервис отвечает на health checks
- [ ] Метрики собираются корректно
- [ ] Логи пишутся в правильном формате
- [ ] Документация опубликована
- [ ] Интеграционные тесты проходят
- [ ] Мониторинг настроен

---

## 📞 **Поддержка и Контакты**

- **Архитектор:** @architect-agent
- **DevOps:** @devops-agent
- **Security:** @security-agent
- **Документация:** docs@necpgame.com

**Все вопросы по реорганизации направлять в #api-reorganization Slack канал**

---

## 🚀 **Новая SOLID/DRY Domain Separation Архитектура**

### **Ключевые Изменения в Подходе**

#### **ДО (Legacy): Дублирование и Хаос**
- 471+ файлов в system/, specialized/, social/, world/, economy/
- Каждый сервис дублировал id, created_at, updated_at
- Несогласованные паттерны между сервисами
- Трудная поддержка и масштабирование

#### **ПОСЛЕ (SOLID/DRY): Domain Inheritance**
- **Common Foundation**: domain-specific entity schemas
- **Zero Duplication**: inheritance вместо дублирования
- **Strict Typing**: enum, patterns, validation, examples
- **Enterprise Grade**: optimistic locking, audit trails

### **Domain-Specific Common Entity**

```
common/schemas/
├── game-entities.yaml       # CharacterEntity, CombatActionEntity, AbilityEntity
├── economy-entities.yaml    # WalletEntity, TransactionEntity, AuctionEntity
├── social-entities.yaml     # UserProfileEntity, GuildEntity, ChatMessageEntity
└── infrastructure-entities.yaml # UserAccountEntity, SessionEntity, AuditLogEntity
```

### **Пример SOLID Inheritance**
```yaml
# Game Service - наследует игровые entity
PlayerCharacter:
  allOf:
    - $ref: '../common/schemas/game-entities.yaml#/CharacterEntity'  # level, stats, experience
    - type: object
      properties:
        cyberware_level: {type: integer, minimum: 0, maximum: 20}  # Только уникальное

# Economy Service - наследует экономические entity
TradeTransaction:
  allOf:
    - $ref: '../common/schemas/economy-entities.yaml#/TransactionEntity'  # amount, currency, wallets
    - type: object
      properties:
        trade_location: {type: string, enum: ['safe_zone', 'combat_zone']}
```

### **Преимущества Новой Архитектуры**
- **80% сокращение кода** - inheritance вместо boilerplate
- **100% consistency** - единые паттерны во всех сервисах
- **Enterprise performance** - struct alignment, optimistic locking
- **Type safety** - strict validation, examples, constraints
- **SOLID compliance** - single responsibility, DRY principle

### **Миграционная Стратегия**
1. **Анализ** - классификация legacy файлов по domain
2. **Domain Common** - создание domain-specific entity
3. **Service Migration** - замена дублирования на inheritance
4. **Validation** - strict typing, Redocly + Ogen
5. **Cleanup** - удаление legacy директорий

**Смотрите `MIGRATION_GUIDE.md` для детальной стратегии миграции!**

---

*Эта инструкция обновлена для SOLID/DRY domain separation архитектуры. Следуйте новым принципам для enterprise-grade API development.*
