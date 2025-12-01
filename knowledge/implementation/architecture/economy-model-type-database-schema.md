<!-- Issue: #140890244 -->
# Economy Model Type System - Database Schema

## Обзор

Схема базы данных для типа экономической модели, управляющей конфигурацией гибридной экономической системы с глобальными, региональными и фракционными рынками, правилами доступа и ценообразования.

## ERD Диаграмма

```mermaid
erDiagram
    economy_model_config ||--o{ market_types : "configures"
    market_types ||--o{ market_access_rules : "has_access_rules"
    market_types ||--o{ pricing_model_config : "has_pricing_config"
    economy_model_config ||--o{ economy_governance_rules : "has_governance_rules"

    economy_model_config {
        uuid id PK
        economy_model_type model_type
        varchar name UNIQUE
        text description
        boolean base_supply_npc_controlled
        boolean strategic_markets_player_controlled
        boolean regional_modifiers_faction_controlled
        decimal price_volatility_factor
        decimal demand_supply_impact
        boolean rare_items_player_only
        boolean is_active
        timestamp created_at
        timestamp updated_at
    }

    market_types {
        uuid id PK
        market_type market_type
        varchar name
        text description
        uuid region_id
        uuid faction_id
        boolean is_global
        decimal base_tax_rate
        decimal player_tax_rate
        decimal npc_tax_rate
        integer min_reputation_level
        boolean requires_alliance
        boolean is_active
        timestamp created_at
        timestamp updated_at
    }

    market_access_rules {
        uuid id PK
        uuid market_type_id FK
        varchar access_type
        integer requirement_value
        uuid requirement_item_id
        uuid requirement_quest_id
        text description
        boolean is_active
        timestamp created_at
    }

    pricing_model_config {
        uuid id PK
        uuid market_type_id FK
        varchar item_category
        integer item_tier
        pricing_control_type pricing_control_type
        decimal base_price_multiplier
        decimal demand_impact_factor
        decimal supply_impact_factor
        decimal event_impact_factor
        decimal min_price_modifier
        decimal max_price_modifier
        decimal player_control_percentage
        decimal npc_control_percentage
        boolean is_active
        timestamp created_at
        timestamp updated_at
    }

    economy_governance_rules {
        uuid id PK
        governance_entity_type governance_entity_type
        uuid entity_id
        varchar rule_type
        varchar rule_scope
        uuid scope_id
        jsonb rule_config
        integer priority
        boolean is_active
        timestamp effective_from
        timestamp effective_until
        timestamp created_at
        timestamp updated_at
    }
```

## Описание таблиц

### economy_model_config

Таблица конфигурации экономической модели. Хранит глобальную конфигурацию экономической системы.

**Ключевые поля:**
- `id`: UUID первичный ключ
- `model_type`: Тип экономической модели (economy_model_type ENUM, NOT NULL, default: 'hybrid')
- `name`: Название конфигурации (VARCHAR(255), NOT NULL, UNIQUE)
- `description`: Описание конфигурации (TEXT, nullable)
- `base_supply_npc_controlled`: Базовое снабжение контролируется NPC (BOOLEAN, NOT NULL, default: true)
- `strategic_markets_player_controlled`: Стратегические рынки контролируются игроками (BOOLEAN, NOT NULL, default: true)
- `regional_modifiers_faction_controlled`: Региональные модификаторы контролируются фракциями (BOOLEAN, NOT NULL, default: true)
- `price_volatility_factor`: Фактор волатильности цен (DECIMAL(5,2), NOT NULL, default: 1.00, диапазон: 0.00-10.00)
- `demand_supply_impact`: Влияние спроса и предложения на цены (DECIMAL(5,2), NOT NULL, default: 0.50, диапазон: 0.00-1.00)
- `rare_items_player_only`: Редкие предметы только для игроков (BOOLEAN, NOT NULL, default: true)
- `is_active`: Активна ли конфигурация (BOOLEAN, NOT NULL, default: true)
- `created_at`: Время создания
- `updated_at`: Время последнего обновления

**Индексы:**
- По `model_type` для фильтрации по типу модели
- По `is_active` для активных конфигураций (WHERE is_active = true)

### market_types

Таблица типов рынков. Хранит информацию о глобальных, региональных и фракционных рынках.

**Ключевые поля:**
- `id`: UUID первичный ключ
- `market_type`: Тип рынка (market_type ENUM, NOT NULL)
- `name`: Название рынка (VARCHAR(255), NOT NULL)
- `description`: Описание рынка (TEXT, nullable)
- `region_id`: ID региона (FK regions, nullable - для региональных рынков)
- `faction_id`: ID фракции (FK factions, nullable - для фракционных рынков)
- `is_global`: Является ли рынок глобальным (BOOLEAN, NOT NULL, default: false)
- `base_tax_rate`: Базовая ставка налога в процентах (DECIMAL(5,2), NOT NULL, default: 0.00, диапазон: 0.00-100.00)
- `player_tax_rate`: Ставка налога для игроков в процентах (DECIMAL(5,2), NOT NULL, default: 0.00, диапазон: 0.00-100.00)
- `npc_tax_rate`: Ставка налога для NPC в процентах (DECIMAL(5,2), NOT NULL, default: 0.00, диапазон: 0.00-100.00)
- `min_reputation_level`: Минимальный уровень репутации для доступа (INTEGER, default: 0, CHECK: >= 0)
- `requires_alliance`: Требуется ли союз для доступа (BOOLEAN, NOT NULL, default: false)
- `is_active`: Активен ли рынок (BOOLEAN, NOT NULL, default: true)
- `created_at`: Время создания
- `updated_at`: Время последнего обновления

**Индексы:**
- По `market_type` для фильтрации по типу рынка
- По `region_id` для региональных рынков (WHERE region_id IS NOT NULL)
- По `faction_id` для фракционных рынков (WHERE faction_id IS NOT NULL)
- По `is_active` для активных рынков (WHERE is_active = true)
- По `is_global` для глобальных рынков (WHERE is_global = true)

**UNIQUE constraint:** `(market_type, region_id, faction_id)` - уникальная комбинация типа рынка, региона и фракции

### market_access_rules

Таблица правил доступа к рынкам. Хранит требования для доступа к рынкам (репутация, союзы, уровень, квесты).

**Ключевые поля:**
- `id`: UUID первичный ключ
- `market_type_id`: ID типа рынка (FK market_types, NOT NULL)
- `access_type`: Тип требования доступа (VARCHAR(50), NOT NULL - 'reputation', 'alliance', 'guild', 'level', 'quest')
- `requirement_value`: Значение требования (INTEGER, nullable - уровень репутации, уровень игрока, etc.)
- `requirement_item_id`: ID предмета требования (FK items, nullable)
- `requirement_quest_id`: ID квеста требования (FK quests, nullable)
- `description`: Описание правила (TEXT, nullable)
- `is_active`: Активно ли правило (BOOLEAN, NOT NULL, default: true)
- `created_at`: Время создания

**Индексы:**
- По `market_type_id` для правил конкретного рынка
- По `access_type` для фильтрации по типу требования
- По `is_active` для активных правил (WHERE is_active = true)

### pricing_model_config

Таблица конфигурации модели ценообразования. Хранит параметры ценообразования для разных рынков и категорий предметов.

**Ключевые поля:**
- `id`: UUID первичный ключ
- `market_type_id`: ID типа рынка (FK market_types, nullable - NULL для глобальных настроек)
- `item_category`: Категория предметов (VARCHAR(50), nullable)
- `item_tier`: Тиер предметов (INTEGER, nullable - 1-5)
- `pricing_control_type`: Тип контроля ценообразования (pricing_control_type ENUM, NOT NULL, default: 'hybrid')
- `base_price_multiplier`: Множитель базовой цены (DECIMAL(5,2), NOT NULL, default: 1.00, диапазон: 0.00-10.00)
- `demand_impact_factor`: Фактор влияния спроса на цену (DECIMAL(5,2), NOT NULL, default: 0.30, диапазон: 0.00-1.00)
- `supply_impact_factor`: Фактор влияния предложения на цену (DECIMAL(5,2), NOT NULL, default: 0.30, диапазон: 0.00-1.00)
- `event_impact_factor`: Фактор влияния событий на цену (DECIMAL(5,2), NOT NULL, default: 0.20, диапазон: 0.00-1.00)
- `min_price_modifier`: Модификатор минимальной цены (DECIMAL(5,2), NOT NULL, default: 0.50, диапазон: 0.00-1.00 от базовой)
- `max_price_modifier`: Модификатор максимальной цены (DECIMAL(5,2), NOT NULL, default: 2.00, диапазон: 1.00-10.00 от базовой)
- `player_control_percentage`: Процент контроля игроков над ценами (DECIMAL(5,2), NOT NULL, default: 50.00, диапазон: 0.00-100.00)
- `npc_control_percentage`: Процент контроля NPC над ценами (DECIMAL(5,2), NOT NULL, default: 50.00, диапазон: 0.00-100.00)
- `is_active`: Активна ли конфигурация (BOOLEAN, NOT NULL, default: true)
- `created_at`: Время создания
- `updated_at`: Время последнего обновления

**Индексы:**
- По `market_type_id` для конфигураций конкретного рынка (WHERE market_type_id IS NOT NULL)
- По `(item_category, item_tier)` для конфигураций конкретной категории и тиера (WHERE оба NOT NULL)
- По `pricing_control_type` для фильтрации по типу контроля
- По `is_active` для активных конфигураций (WHERE is_active = true)

### economy_governance_rules

Таблица правил управления экономикой. Хранит правила контроля цен, предложения, налогов и доступа для разных сущностей.

**Ключевые поля:**
- `id`: UUID первичный ключ
- `governance_entity_type`: Тип сущности управления (governance_entity_type ENUM, NOT NULL)
- `entity_id`: ID сущности (UUID, nullable - player_id, faction_id, etc.)
- `rule_type`: Тип правила (VARCHAR(50), NOT NULL - 'price_control', 'supply_control', 'tax_control', 'access_control')
- `rule_scope`: Область действия правила (VARCHAR(50), NOT NULL - 'global', 'regional', 'faction', 'market', 'item')
- `scope_id`: ID области действия (UUID, nullable - region_id, market_type_id, item_id, etc.)
- `rule_config`: Конфигурация правила (JSONB, NOT NULL, default: '{}')
- `priority`: Приоритет правила (INTEGER, NOT NULL, default: 0, CHECK: >= 0 - чем выше, тем важнее)
- `is_active`: Активно ли правило (BOOLEAN, NOT NULL, default: true)
- `effective_from`: Действует с (TIMESTAMP, NOT NULL, default: CURRENT_TIMESTAMP)
- `effective_until`: Действует до (TIMESTAMP, nullable)
- `created_at`: Время создания
- `updated_at`: Время последнего обновления

**Индексы:**
- По `(governance_entity_type, entity_id)` для правил конкретной сущности (WHERE entity_id IS NOT NULL)
- По `(rule_type, rule_scope)` для фильтрации по типу и области действия
- По `scope_id` для правил конкретной области (WHERE scope_id IS NOT NULL)
- По `(is_active, effective_from, effective_until)` для активных правил в период действия (WHERE is_active = true)
- По `priority DESC` для сортировки по приоритету

## ENUM типы

### economy_model_type
- `player_driven`: Полностью игроко-управляемая экономика
- `npc_driven`: Полностью NPC-управляемая экономика
- `hybrid`: Гибридная экономика (комбинация игроков, NPC и фракций)

### market_type
- `global`: Глобальный рынок (доступен всем игрокам)
- `regional`: Региональный рынок (доступен игрокам в регионе)
- `faction`: Фракционный рынок (доступен игрокам фракции)

### pricing_control_type
- `system_base`: Базовые ставки системы (полный контроль системы)
- `player_driven`: Игроко-управляемое ценообразование (полный контроль игроков)
- `hybrid`: Гибридное ценообразование (комбинация системы и игроков)

### governance_entity_type
- `player`: Игрок
- `npc`: NPC
- `faction`: Фракция
- `system`: Система

## Constraints и валидация

### CHECK Constraints

- `economy_model_config.price_volatility_factor`: >= 0.00 AND <= 10.00
- `economy_model_config.demand_supply_impact`: >= 0.00 AND <= 1.00
- `market_types.base_tax_rate`: >= 0.00 AND <= 100.00
- `market_types.player_tax_rate`: >= 0.00 AND <= 100.00
- `market_types.npc_tax_rate`: >= 0.00 AND <= 100.00
- `market_types.min_reputation_level`: >= 0
- `pricing_model_config.base_price_multiplier`: >= 0.00 AND <= 10.00
- `pricing_model_config.demand_impact_factor`: >= 0.00 AND <= 1.00
- `pricing_model_config.supply_impact_factor`: >= 0.00 AND <= 1.00
- `pricing_model_config.event_impact_factor`: >= 0.00 AND <= 1.00
- `pricing_model_config.min_price_modifier`: >= 0.00 AND <= 1.00
- `pricing_model_config.max_price_modifier`: >= 1.00 AND <= 10.00
- `pricing_model_config.player_control_percentage`: >= 0.00 AND <= 100.00
- `pricing_model_config.npc_control_percentage`: >= 0.00 AND <= 100.00
- `economy_governance_rules.priority`: >= 0

### Foreign Keys

- `market_access_rules.market_type_id` → `economy.market_types.id` (ON DELETE CASCADE)
- `pricing_model_config.market_type_id` → `economy.market_types.id` (ON DELETE SET NULL)

## Оптимизация запросов

### Частые запросы

1. **Получение активной конфигурации экономической модели:**
   ```sql
   SELECT * FROM economy.economy_model_config 
   WHERE is_active = true 
   ORDER BY updated_at DESC 
   LIMIT 1;
   ```
   Использует индекс `is_active`.

2. **Получение рынков по типу:**
   ```sql
   SELECT * FROM economy.market_types 
   WHERE market_type = $1 AND is_active = true;
   ```
   Использует индексы `market_type` и `is_active`.

3. **Получение правил доступа к рынку:**
   ```sql
   SELECT * FROM economy.market_access_rules 
   WHERE market_type_id = $1 AND is_active = true;
   ```
   Использует индексы `market_type_id` и `is_active`.

4. **Получение конфигурации ценообразования:**
   ```sql
   SELECT * FROM economy.pricing_model_config 
   WHERE market_type_id = $1 AND item_category = $2 AND item_tier = $3 
   AND is_active = true;
   ```
   Использует индексы `(item_category, item_tier)` и `is_active`.

5. **Получение правил управления для сущности:**
   ```sql
   SELECT * FROM economy.economy_governance_rules 
   WHERE governance_entity_type = $1 AND entity_id = $2 
   AND is_active = true 
   AND (effective_until IS NULL OR effective_until > CURRENT_TIMESTAMP)
   ORDER BY priority DESC;
   ```
   Использует индексы `(governance_entity_type, entity_id)` и `(is_active, effective_from, effective_until)`.

## Миграции

### Применение миграций:
```bash
liquibase update --changelog-file=infrastructure/liquibase/changelog.yaml
```

## Соответствие архитектуре

Схема БД соответствует механике из `knowledge/mechanics/economy/economy-type.yaml`:
- OK Гибридная модель экономики (player-driven, NPC-driven, hybrid)
- OK Глобальные, региональные и фракционные рынки
- OK Правила доступа к рынкам (репутация, союзы, уровень, квесты)
- OK Гибридное ценообразование (базовые ставки + динамика спроса/предложения)
- OK Правила управления экономикой (контроль цен, предложения, налогов, доступа)
- OK Индексы оптимизированы для частых запросов
- OK Foreign Keys настроены с правильными действиями (CASCADE, SET NULL)
- OK ENUM типы соответствуют механике

## Особенности реализации

### Гибридная модель экономики

Система поддерживает три типа экономических моделей:
- **player_driven**: Полностью игроко-управляемая экономика (как в EVE Online)
- **npc_driven**: Полностью NPC-управляемая экономика (как в World of Warcraft)
- **hybrid**: Гибридная экономика (комбинация игроков, NPC и фракций)

### Типы рынков

Система поддерживает три типа рынков:
- **global**: Глобальный рынок для массовых товаров (доступен всем игрокам)
- **regional**: Региональный рынок для локальных ресурсов (доступен игрокам в регионе)
- **faction**: Фракционный рынок с требованиями по репутации и союзам

### Правила доступа

Система поддерживает различные типы требований доступа:
- **reputation**: Требование по уровню репутации
- **alliance**: Требование по союзу
- **guild**: Требование по гильдии
- **level**: Требование по уровню игрока
- **quest**: Требование по выполненному квесту

### Ценообразование

Система ценообразования учитывает:
- **Базовые ставки системы**: Задаются платформой
- **Динамика спроса и предложения**: Влияние активности игроков
- **Экономические события**: Влияние событий на цены
- **Контроль игроков и NPC**: Процент контроля над ценами

### Правила управления

Система управления экономикой поддерживает:
- **price_control**: Контроль цен
- **supply_control**: Контроль предложения
- **tax_control**: Контроль налогов
- **access_control**: Контроль доступа

### Интеграция с другими системами

Тип экономической модели интегрируется с:
- **Economy Service**: Применение правил ценообразования и доступа
- **Social Service**: Проверка репутации и союзов для доступа к рынкам
- **World Service**: Региональные модификаторы и события
- **Faction Service**: Фракционные рынки и контроль
- **Guild Service**: Гильдийные бонусы и доступ

