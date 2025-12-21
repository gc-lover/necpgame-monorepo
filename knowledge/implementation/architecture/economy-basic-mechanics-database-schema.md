<!-- Issue: #140876080 -->
# Economy Basic Mechanics - Database Schema

## Обзор

Схема базы данных для базовых экономических механик, включающая обзор экономики, торговые гильдии, валюты, ресурсы и влияние экономики на игровой мир. Интегрируется с системой торговли для обеспечения фундамента экономической системы.

## ERD Диаграмма

```mermaid
erDiagram
    currencies ||--o{ economy_overview : "used_in"
    regions ||--o{ economy_overview : "located_in"
    regions ||--o{ currencies : "has"
    regions ||--o{ world_economy_impact : "affected_by"
    factions ||--o{ world_economy_impact : "influences"
    trading_guilds ||--o{ guild_members : "has"
    character ||--o{ trading_guilds : "leads"
    character ||--o{ guild_members : "is_member"
    character ||--o{ trade_items : "trades"
    trade_sessions ||--o{ trade_items : "contains"

    economy_overview {
        uuid id PK
        uuid region_id
        uuid currency_id FK
        decimal total_volume
        integer active_trades
        integer active_guilds
        timestamp updated_at
        timestamp created_at
    }

    trading_guilds {
        uuid id PK
        varchar name UNIQUE
        uuid leader_id FK
        decimal capital
        integer member_count
        text description
        timestamp created_at
        timestamp updated_at
    }

    guild_members {
        uuid id PK
        uuid guild_id FK
        uuid character_id FK
        varchar role
        timestamp joined_at
        timestamp left_at
        decimal contribution
    }

    currencies {
        uuid id PK
        varchar code UNIQUE
        varchar name
        uuid region_id
        decimal exchange_rate
        boolean is_active
        timestamp updated_at
        timestamp created_at
    }

    resources {
        uuid id PK
        varchar name UNIQUE
        varchar category
        decimal base_price
        decimal current_price
        text description
        timestamp updated_at
        timestamp created_at
    }

    world_economy_impact {
        uuid id PK
        uuid region_id
        uuid faction_id
        decimal economic_power
        decimal trade_volume
        integer influence_level
        timestamp updated_at
        timestamp created_at
    }

    trade_items {
        uuid id PK
        uuid session_id FK
        uuid character_id FK
        uuid item_id
        integer quantity
        integer slot_index
        timestamp created_at
    }

    trade_sessions {
        uuid id PK
        uuid initiator_id FK
        uuid recipient_id FK
        jsonb initiator_offer
        jsonb recipient_offer
        boolean initiator_confirmed
        boolean recipient_confirmed
        varchar status
        uuid zone_id
        timestamp created_at
        timestamp updated_at
        timestamp expires_at
        timestamp completed_at
    }
```

## Описание таблиц

### economy_overview

Таблица обзора экономической системы. Хранит агрегированную информацию об экономике по регионам и валютам.

**Ключевые поля:**
- `region_id`: ID региона (nullable)
- `currency_id`: ID валюты (FK к currencies, nullable)
- `total_volume`: Общий объем торговли (DECIMAL(20,2), >= 0)
- `active_trades`: Количество активных торговых сессий (INTEGER, >= 0)
- `active_guilds`: Количество активных торговых гильдий (INTEGER, >= 0)
- `updated_at`: Время последнего обновления

**Индексы:**
- По `region_id` для фильтрации по региону
- По `currency_id` для фильтрации по валюте
- По `updated_at DESC` для последних обновлений

### trading_guilds

Таблица торговых гильдий. Хранит информацию о торговых гильдиях игроков.

**Ключевые поля:**
- `name`: Название гильдии (UNIQUE)
- `leader_id`: ID лидера гильдии (FK к characters)
- `capital`: Капитал гильдии (DECIMAL(20,2), >= 0)
- `member_count`: Количество членов гильдии (INTEGER, >= 0)
- `description`: Описание гильдии

**Индексы:**
- По `leader_id` для поиска гильдий лидера
- По `name` для поиска по названию
- По `capital DESC` для сортировки по капиталу

### guild_members

Таблица членов торговых гильдий. Хранит информацию о членах торговых гильдий.

**Ключевые поля:**
- `guild_id`: ID гильдии (FK к trading_guilds)
- `character_id`: ID персонажа (FK к characters)
- `role`: Роль в гильдии (member, officer, leader)
- `joined_at`: Время вступления в гильдию
- `left_at`: Время выхода из гильдии (nullable)
- `contribution`: Вклад члена в гильдию (DECIMAL(20,2), >= 0)

**Индексы:**
- По `(guild_id, role)` для фильтрации по гильдии и роли
- По `character_id` для поиска гильдий персонажа
- По `role` для фильтрации по роли

### currencies

Таблица валют. Хранит информацию о валютах экономической системы.

**Ключевые поля:**
- `code`: Код валюты (UNIQUE)
- `name`: Название валюты
- `region_id`: ID региона (nullable)
- `exchange_rate`: Курс обмена валюты (DECIMAL(20,8), > 0)
- `is_active`: Флаг активности валюты

**Индексы:**
- По `code` для поиска по коду
- По `region_id` для фильтрации по региону
- По `is_active` для активных валют
- По `exchange_rate` для сортировки по курсу

### resources

Таблица ресурсов. Хранит информацию о ресурсах экономической системы.

**Ключевые поля:**
- `name`: Название ресурса (UNIQUE)
- `category`: Категория ресурса
- `base_price`: Базовая цена ресурса (DECIMAL(20,2), >= 0)
- `current_price`: Текущая цена ресурса (DECIMAL(20,2), >= 0)
- `description`: Описание ресурса

**Индексы:**
- По `(category, name)` для фильтрации по категории
- По `name` для поиска по названию
- По `current_price` для сортировки по цене

### world_economy_impact

Таблица влияния экономики на мир. Хранит информацию о влиянии экономики на регионы и фракции.

**Ключевые поля:**
- `region_id`: ID региона (nullable)
- `faction_id`: ID фракции (nullable)
- `economic_power`: Экономическая мощь фракции в регионе (DECIMAL(20,2), >= 0)
- `trade_volume`: Объем торговли фракции в регионе (DECIMAL(20,2), >= 0)
- `influence_level`: Уровень влияния фракции (INTEGER, 0-100)
- `updated_at`: Время последнего обновления

**Индексы:**
- По `region_id` для фильтрации по региону
- По `faction_id` для фильтрации по фракции
- По `influence_level DESC` для сортировки по влиянию
- По `economic_power DESC` для сортировки по экономической мощи

### trade_items

Таблица предметов в торговой сессии. Хранит информацию о предметах, участвующих в торговой сессии.

**Ключевые поля:**
- `session_id`: ID торговой сессии (FK к trade_sessions)
- `character_id`: ID персонажа (FK к characters)
- `item_id`: ID предмета
- `quantity`: Количество предметов (INTEGER, > 0)
- `slot_index`: Индекс слота (nullable)

**Индексы:**
- По `(session_id, character_id)` для предметов в сессии
- По `character_id` для предметов персонажа
- По `item_id` для предметов

### trade_sessions (уже создана в V1_15)

Таблица торговых сессий. Уже создана в V1_15, используется для базовой P2P торговли.

## Constraints и валидация

### CHECK Constraints

- `economy_overview.total_volume`: Должно быть >= 0
- `economy_overview.active_trades`: Должно быть >= 0
- `economy_overview.active_guilds`: Должно быть >= 0
- `trading_guilds.capital`: Должно быть >= 0
- `trading_guilds.member_count`: Должно быть >= 0
- `guild_members.role`: Должно быть одним из: member, officer, leader
- `guild_members.contribution`: Должно быть >= 0
- `currencies.exchange_rate`: Должно быть > 0
- `resources.base_price`: Должно быть >= 0
- `resources.current_price`: Должно быть >= 0
- `world_economy_impact.economic_power`: Должно быть >= 0
- `world_economy_impact.trade_volume`: Должно быть >= 0
- `world_economy_impact.influence_level`: Должно быть >= 0 и <= 100
- `trade_items.quantity`: Должно быть > 0

### Foreign Keys

- `economy_overview.currency_id` → `economy.currencies.id` (ON DELETE SET NULL)
- `trading_guilds.leader_id` → `mvp_core.character.id` (ON DELETE CASCADE)
- `guild_members.guild_id` → `economy.trading_guilds.id` (ON DELETE CASCADE)
- `guild_members.character_id` → `mvp_core.character.id` (ON DELETE CASCADE)
- `trade_items.session_id` → `economy.trade_sessions.id` (ON DELETE CASCADE)
- `trade_items.character_id` → `mvp_core.character.id` (ON DELETE CASCADE)

### Unique Constraints

- `economy_overview(region_id, currency_id)`: Один обзор на регион и валюту
- `trading_guilds(name)`: Уникальное название гильдии
- `guild_members(guild_id, character_id)`: Один член на гильдию и персонажа
- `currencies(code)`: Уникальный код валюты
- `resources(name)`: Уникальное название ресурса
- `world_economy_impact(region_id, faction_id)`: Одно влияние на регион и фракцию

## Оптимизация запросов

### Частые запросы

1. **Получение обзора экономики по региону:**
   ```sql
   SELECT * FROM economy.economy_overview 
   WHERE region_id = $1 
   ORDER BY updated_at DESC;
   ```
   Использует индекс `region_id`.

2. **Поиск торговых гильдий:**
   ```sql
   SELECT * FROM economy.trading_guilds 
   WHERE leader_id = $1 OR id IN (
       SELECT guild_id FROM economy.guild_members WHERE character_id = $1
   );
   ```
   Использует индексы `leader_id` и `character_id`.

3. **Получение активных валют:**
   ```sql
   SELECT * FROM economy.currencies 
   WHERE is_active = true 
   ORDER BY exchange_rate DESC;
   ```
   Использует индекс `is_active`.

4. **Поиск ресурсов по категории:**
   ```sql
   SELECT * FROM economy.resources 
   WHERE category = $1 
   ORDER BY current_price ASC;
   ```
   Использует индекс `(category, name)`.

5. **Получение влияния экономики на регион:**
   ```sql
   SELECT * FROM economy.world_economy_impact 
   WHERE region_id = $1 
   ORDER BY influence_level DESC;
   ```
   Использует индекс `region_id`.

## Миграции

### Существующие миграции:
- `V1_15__trade_tables.sql` - базовые таблицы торговли (trade_sessions, trade_history)
- `V1_58__economy_basic_mechanics_tables.sql` - полная схема базовых экономических механик

### Применение миграций:
```bash
liquibase update --changelog-file=infrastructure/liquibase/changelog.yaml
```

## Соответствие архитектуре

Схема БД полностью соответствует архитектуре из `knowledge/implementation/architecture/economy-basic-mechanics-architecture.yaml`:
- [OK] Все таблицы из архитектуры созданы
- [OK] Все поля соответствуют описанию
- [OK] Индексы оптимизированы для частых запросов
- [OK] Constraints обеспечивают целостность данных
- [OK] Foreign Keys настроены с CASCADE для автоматической очистки
- [OK] Интеграция с существующими таблицами (trade_sessions)

## Особенности реализации

### Торговые гильдии

Система торговых гильдий включает:
- **Роли**: member, officer, leader
- **Капитал**: Общий капитал гильдии
- **Вклад**: Вклад каждого члена в гильдию
- **Членство**: Отслеживание вступления и выхода из гильдии

### Валюты

Система валют включает:
- **Код валюты**: Уникальный код (например, USD, EUR)
- **Курс обмена**: Динамический курс обмена
- **Региональность**: Привязка валют к регионам
- **Активность**: Флаг активности валюты

### Ресурсы

Система ресурсов включает:
- **Категории**: Классификация ресурсов
- **Базовая цена**: Начальная цена ресурса
- **Текущая цена**: Динамическая цена ресурса
- **Описание**: Детальное описание ресурса

### Влияние на мир

Система влияния экономики на мир включает:
- **Экономическая мощь**: Мощь фракции в регионе
- **Объем торговли**: Объем торговли фракции
- **Уровень влияния**: Уровень влияния фракции (0-100)
- **Региональность**: Привязка влияния к регионам и фракциям

### Интеграция с торговлей

Базовая экономика интегрирована с торговлей через:
- `trade_sessions`: Торговые сессии между игроками
- `trade_items`: Предметы в торговых сессиях
- `economy_overview`: Агрегированная статистика торговли


