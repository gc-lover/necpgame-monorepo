<!-- Issue: #140890239 -->
# Resources Catalog System - Database Schema

## Обзор

Схема базы данных для каталога ресурсов и материалов, расширяющая существующую таблицу `economy.resources` и добавляющая таблицы для источников добычи, применения, зон добычи и истории цен.

## ERD Диаграмма

```mermaid
erDiagram
    resources ||--o{ resource_sources : "has_sources"
    resources ||--o{ resource_applications : "has_applications"
    resources ||--o{ resource_mining_zones : "found_in_zones"
    resources ||--o{ resource_price_history : "has_price_history"

    resources {
        uuid id PK
        varchar name UNIQUE
        varchar category
        integer tier
        resource_rarity rarity
        decimal base_price
        decimal current_price
        decimal min_price
        decimal max_price
        decimal vendor_price
        decimal player_price
        integer stack_size
        decimal weight
        jsonb sources
        jsonb applications
        boolean is_tradeable
        boolean is_stackable
        varchar icon_path
        text description
        timestamp created_at
        timestamp updated_at
    }

    resource_sources {
        uuid id PK
        uuid resource_id FK
        resource_source_type source_type
        varchar source_name
        uuid source_id
        decimal drop_chance
        integer min_quantity
        integer max_quantity
        integer level_requirement
        text description
        timestamp created_at
    }

    resource_applications {
        uuid id PK
        uuid resource_id FK
        resource_application_type application_type
        varchar target_item_type
        integer target_item_tier
        integer quantity_required
        text description
        timestamp created_at
    }

    resource_mining_zones {
        uuid id PK
        uuid resource_id FK
        varchar zone_name
        uuid region_id
        text location_description
        varchar risk_level
        decimal spawn_rate
        integer respawn_time_minutes
        integer level_requirement
        boolean is_active
        timestamp created_at
        timestamp updated_at
    }

    resource_price_history {
        uuid id PK
        uuid resource_id FK
        decimal price
        varchar price_type
        uuid region_id
        uuid event_id
        decimal supply_factor
        decimal demand_factor
        timestamp recorded_at
    }
```

## Описание таблиц

### resources (расширенная)

Таблица ресурсов. Расширена дополнительными полями для каталога ресурсов.

**Существующие поля:**
- `id`: UUID первичный ключ
- `name`: Название ресурса (VARCHAR(100), NOT NULL, UNIQUE)
- `category`: Категория ресурса (VARCHAR(50), NOT NULL)
- `base_price`: Базовая цена ресурса (DECIMAL(20,2), NOT NULL, default: 0.0, CHECK: >= 0)
- `current_price`: Текущая цена ресурса (DECIMAL(20,2), NOT NULL, default: 0.0, CHECK: >= 0)
- `description`: Описание ресурса (TEXT, nullable)
- `created_at`: Время создания
- `updated_at`: Время последнего обновления

**Новые поля:**
- `tier`: Тиер ресурса (INTEGER, nullable, CHECK: 1-5)
- `rarity`: Редкость ресурса (resource_rarity ENUM, nullable)
- `stack_size`: Максимальный размер стека (INTEGER, default: 1, CHECK: > 0)
- `weight`: Вес единицы ресурса (DECIMAL(10,4), default: 0.0, CHECK: >= 0)
- `min_price`: Минимальная цена ресурса (DECIMAL(20,2), nullable, CHECK: >= 0)
- `max_price`: Максимальная цена ресурса (DECIMAL(20,2), nullable, CHECK: >= 0)
- `vendor_price`: Цена у продавца (DECIMAL(20,2), nullable, CHECK: >= 0)
- `player_price`: Цена при торговле между игроками (DECIMAL(20,2), nullable, CHECK: >= 0)
- `sources`: Источники добычи в JSONB (JSONB, default: '[]')
- `applications`: Применение ресурса в JSONB (JSONB, default: '[]')
- `is_tradeable`: Можно ли торговать ресурсом (BOOLEAN, default: true)
- `is_stackable`: Можно ли складывать ресурсы в стек (BOOLEAN, default: true)
- `icon_path`: Путь к иконке ресурса (VARCHAR(255), nullable)

**Индексы:**
- По `(category, name)` для фильтрации по категории
- По `name` для поиска по названию
- По `current_price` для сортировки по цене
- По `(tier, rarity)` для фильтрации по тиеру и редкости (WHERE оба NOT NULL)
- По `rarity` для фильтрации по редкости (WHERE rarity IS NOT NULL)
- По `tier` для фильтрации по тиеру (WHERE tier IS NOT NULL)
- По `is_tradeable` для торговых ресурсов (WHERE is_tradeable = true)

### resource_sources

Таблица источников добычи ресурсов. Хранит информацию о том, откуда можно получить ресурсы.

**Ключевые поля:**
- `id`: UUID первичный ключ
- `resource_id`: ID ресурса (FK resources, NOT NULL)
- `source_type`: Тип источника (resource_source_type ENUM, NOT NULL)
- `source_name`: Название источника (VARCHAR(255), NOT NULL)
- `source_id`: ID источника (UUID, nullable - NPC, локация, квест, etc.)
- `drop_chance`: Вероятность выпадения в процентах (DECIMAL(5,2), nullable, диапазон: 0.00-100.00)
- `min_quantity`: Минимальное количество при выпадении (INTEGER, default: 1, CHECK: > 0)
- `max_quantity`: Максимальное количество при выпадении (INTEGER, default: 1, CHECK: >= min_quantity)
- `level_requirement`: Требуемый уровень (INTEGER, default: 0, CHECK: >= 0)
- `description`: Описание источника (TEXT, nullable)
- `created_at`: Время создания

**Индексы:**
- По `resource_id` для источников конкретного ресурса
- По `source_type` для фильтрации по типу источника
- По `source_id` для источников конкретного объекта (WHERE source_id IS NOT NULL)

### resource_applications

Таблица применения ресурсов. Хранит информацию о том, как используются ресурсы.

**Ключевые поля:**
- `id`: UUID первичный ключ
- `resource_id`: ID ресурса (FK resources, NOT NULL)
- `application_type`: Тип применения (resource_application_type ENUM, NOT NULL)
- `target_item_type`: Тип предмета (VARCHAR(50), nullable - weapon, armor, cyberware, etc.)
- `target_item_tier`: Тиер предмета (INTEGER, nullable - 1-5)
- `quantity_required`: Количество ресурса, необходимое для применения (INTEGER, default: 1, CHECK: > 0)
- `description`: Описание применения (TEXT, nullable)
- `created_at`: Время создания

**Индексы:**
- По `resource_id` для применений конкретного ресурса
- По `application_type` для фильтрации по типу применения
- По `(target_item_type, target_item_tier)` для применений конкретного типа предмета (WHERE оба NOT NULL)

### resource_mining_zones

Таблица зон добычи ресурсов. Хранит информацию о зонах, где можно добывать ресурсы.

**Ключевые поля:**
- `id`: UUID первичный ключ
- `resource_id`: ID ресурса (FK resources, NOT NULL)
- `zone_name`: Название зоны (VARCHAR(255), NOT NULL)
- `region_id`: ID региона (FK regions, nullable)
- `location_description`: Описание локации (TEXT, nullable)
- `risk_level`: Уровень риска зоны (VARCHAR(20), nullable - low, medium, high, very_high)
- `spawn_rate`: Частота появления ресурса в зоне (DECIMAL(5,2), nullable, диапазон: 0.00-100.00)
- `respawn_time_minutes`: Время возрождения ресурса в минутах (INTEGER, default: 60, CHECK: > 0)
- `level_requirement`: Требуемый уровень (INTEGER, default: 0, CHECK: >= 0)
- `is_active`: Активна ли зона (BOOLEAN, default: true)
- `created_at`: Время создания
- `updated_at`: Время последнего обновления

**Индексы:**
- По `resource_id` для зон конкретного ресурса
- По `region_id` для зон конкретного региона (WHERE region_id IS NOT NULL)
- По `is_active` для активных зон (WHERE is_active = true)
- По `risk_level` для фильтрации по уровню риска

### resource_price_history

Таблица истории цен ресурсов. Хранит историю изменения цен для динамического ценообразования.

**Ключевые поля:**
- `id`: UUID первичный ключ
- `resource_id`: ID ресурса (FK resources, NOT NULL)
- `price`: Цена ресурса (DECIMAL(20,2), NOT NULL, CHECK: >= 0)
- `price_type`: Тип цены (VARCHAR(20), NOT NULL - 'base', 'current', 'vendor', 'player')
- `region_id`: ID региона (FK regions, nullable)
- `event_id`: ID экономического события (FK economic_events, nullable)
- `supply_factor`: Фактор предложения (DECIMAL(5,2), nullable - 0.00-2.00, влияет на цену)
- `demand_factor`: Фактор спроса (DECIMAL(5,2), nullable - 0.00-2.00, влияет на цену)
- `recorded_at`: Время записи цены

**Индексы:**
- По `(resource_id, recorded_at DESC)` для истории цен ресурса
- По `price_type` для фильтрации по типу цены
- По `region_id` для цен конкретного региона (WHERE region_id IS NOT NULL)
- По `event_id` для цен, связанных с событиями (WHERE event_id IS NOT NULL)
- По `recorded_at DESC` для временных запросов

## ENUM типы

### resource_rarity
- `common`: Обычный
- `uncommon`: Необычный
- `rare`: Редкий
- `epic`: Эпический
- `legendary`: Легендарный

### resource_source_type
- `loot`: Лут (выпадение с врагов, контейнеров)
- `mining`: Добыча (извлечение из месторождений)
- `processing`: Переработка (обработка других ресурсов)
- `quest`: Квест (награда за квест)
- `vendor`: Продавец (покупка у NPC)
- `dismantling`: Разборка (разбор предметов)
- `crafting`: Крафт (создание из других ресурсов)
- `event`: Событие (награда за событие)

### resource_application_type
- `weapon_crafting`: Крафт оружия
- `armor_crafting`: Крафт брони
- `cyberware_crafting`: Крафт кибердеков
- `consumable_crafting`: Крафт расходников
- `mod_crafting`: Крафт модов
- `upgrade`: Улучшение предметов
- `trade`: Торговля
- `quest`: Использование в квестах

## Constraints и валидация

### CHECK Constraints

- `resources.tier`: >= 1 AND <= 5
- `resources.base_price`: >= 0
- `resources.current_price`: >= 0
- `resources.min_price`: >= 0
- `resources.max_price`: >= 0
- `resources.vendor_price`: >= 0
- `resources.player_price`: >= 0
- `resources.stack_size`: > 0
- `resources.weight`: >= 0
- `resource_sources.drop_chance`: >= 0.00 AND <= 100.00
- `resource_sources.min_quantity`: > 0
- `resource_sources.max_quantity`: >= min_quantity
- `resource_sources.level_requirement`: >= 0
- `resource_applications.quantity_required`: > 0
- `resource_mining_zones.spawn_rate`: >= 0.00 AND <= 100.00
- `resource_mining_zones.respawn_time_minutes`: > 0
- `resource_mining_zones.level_requirement`: >= 0
- `resource_price_history.price`: >= 0

### Foreign Keys

- `resource_sources.resource_id` → `economy.resources.id` (ON DELETE CASCADE)
- `resource_applications.resource_id` → `economy.resources.id` (ON DELETE CASCADE)
- `resource_mining_zones.resource_id` → `economy.resources.id` (ON DELETE CASCADE)
- `resource_price_history.resource_id` → `economy.resources.id` (ON DELETE CASCADE)

## Оптимизация запросов

### Частые запросы

1. **Получение ресурсов по категории и тиеру:**
   ```sql
   SELECT * FROM economy.resources 
   WHERE category = $1 AND tier = $2 
   ORDER BY current_price ASC;
   ```
   Использует индексы `(category, name)` и `tier`.

2. **Получение источников ресурса:**
   ```sql
   SELECT * FROM economy.resource_sources 
   WHERE resource_id = $1 
   ORDER BY drop_chance DESC;
   ```
   Использует индекс `resource_id`.

3. **Получение применений ресурса:**
   ```sql
   SELECT * FROM economy.resource_applications 
   WHERE resource_id = $1 AND application_type = $2;
   ```
   Использует индексы `resource_id` и `application_type`.

4. **Получение зон добычи ресурса:**
   ```sql
   SELECT * FROM economy.resource_mining_zones 
   WHERE resource_id = $1 AND is_active = true 
   ORDER BY risk_level, spawn_rate DESC;
   ```
   Использует индексы `resource_id` и `is_active`.

5. **Получение истории цен ресурса:**
   ```sql
   SELECT * FROM economy.resource_price_history 
   WHERE resource_id = $1 AND price_type = 'current' 
   ORDER BY recorded_at DESC 
   LIMIT 100;
   ```
   Использует индекс `(resource_id, recorded_at DESC)`.

## Миграции

### Применение миграций:
```bash
liquibase update --changelog-file=infrastructure/liquibase/changelog.yaml
```

**Примечание:** Миграция расширяет существующую таблицу `economy.resources` из `V1_58__economy_basic_mechanics_tables.sql` и добавляет новые таблицы.

## Соответствие архитектуре

Схема БД соответствует механике из `knowledge/mechanics/economy/economy-resources-catalog.yaml`:
- [OK] Расширена таблица `resources` с полями для tier, rarity, stack_size, weight, prices
- [OK] Создана таблица `resource_sources` для источников добычи
- [OK] Создана таблица `resource_applications` для применения ресурсов
- [OK] Создана таблица `resource_mining_zones` для зон добычи
- [OK] Создана таблица `resource_price_history` для истории цен
- [OK] Индексы оптимизированы для частых запросов
- [OK] Foreign Keys настроены с CASCADE для автоматической очистки
- [OK] ENUM типы соответствуют механике

## Особенности реализации

### Tier-система

Ресурсы классифицируются по тиерам (T1-T5):
- **T1**: Базовые ресурсы (Scrap Metal, Circuit Board)
- **T2**: Обычные ресурсы (Steel Ingot, Processor Chip)
- **T3**: Редкие ресурсы (Titanium Alloy, Neural Matrix)
- **T4**: Эпические ресурсы (Tungsten Carbide, Quantum Core)
- **T5**: Легендарные ресурсы (Militech Spec-Ops Tech, AI Fragment)

### Редкость ресурсов

Система поддерживает следующие уровни редкости:
- **common**: Обычный (широко доступен)
- **uncommon**: Необычный (редко встречается)
- **rare**: Редкий (очень редко встречается)
- **epic**: Эпический (крайне редко встречается)
- **legendary**: Легендарный (уникальный)

### Источники добычи

Ресурсы можно получить из различных источников:
- **loot**: Лут с врагов, контейнеров
- **mining**: Добыча из месторождений
- **processing**: Переработка других ресурсов
- **quest**: Награда за квест
- **vendor**: Покупка у NPC
- **dismantling**: Разборка предметов
- **crafting**: Крафт из других ресурсов
- **event**: Награда за событие

### Применение ресурсов

Ресурсы используются для:
- **weapon_crafting**: Крафт оружия
- **armor_crafting**: Крафт брони
- **cyberware_crafting**: Крафт кибердеков
- **consumable_crafting**: Крафт расходников
- **mod_crafting**: Крафт модов
- **upgrade**: Улучшение предметов
- **trade**: Торговля
- **quest**: Использование в квестах

### Зоны добычи

Основные зоны добычи:
- **Watson**: Низкий-средний риск, базовые ресурсы
- **Corpo Plaza**: Средний-высокий риск, редкие ресурсы
- **Badlands**: Высокий риск, эпические ресурсы
- **Pacifica**: Очень высокий риск, легендарные ресурсы

### Динамическое ценообразование

Система ценообразования учитывает:
- **supply_factor**: Фактор предложения (0.00-2.00)
- **demand_factor**: Фактор спроса (0.00-2.00)
- **economic_events**: Влияние экономических событий
- **region**: Региональные различия
- **faction_control**: Контроль фракций над регионами

### Интеграция с другими системами

Каталог ресурсов интегрируется с:
- **Crafting Service**: Использование ресурсов в крафте
- **Inventory Service**: Хранение ресурсов
- **Economy Service**: Динамическое ценообразование
- **World Service**: Зоны добычи и события
- **Quest Service**: Ресурсы как награды и требования
- **Economy Events Service**: Влияние событий на цены

