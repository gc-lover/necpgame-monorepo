<!-- Issue: #315 -->
# Cosmetic System - Database Schema

## Обзор

Схема базы данных для системы косметики, управляющей каталогом косметических предметов, владением косметикой игроками, экипировкой и ротациями магазина.

## ERD Диаграмма

```mermaid
erDiagram
    cosmetic_items ||--o{ player_cosmetics : "owned_by"
    cosmetic_items ||--o{ player_equipped_cosmetics : "equipped_as"
    cosmetic_items ||--o{ cosmetic_telemetry : "tracked"
    cosmetic_shop_rotations ||--o{ cosmetic_items : "contains"
    character ||--o{ player_cosmetics : "owns"
    character ||--o{ player_equipped_cosmetics : "equips"
    character ||--o{ cosmetic_telemetry : "interacts"

    cosmetic_items {
        uuid id PK
        varchar code UNIQUE
        varchar name
        cosmetic_category category
        cosmetic_rarity rarity
        jsonb cost
        jsonb assets
        text description
        boolean is_exclusive
        boolean is_time_limited
        timestamp available_from
        timestamp available_until
        integer level_requirement
        timestamp created_at
        timestamp updated_at
    }

    player_cosmetics {
        uuid id PK
        uuid character_id FK
        uuid cosmetic_id FK
        varchar source
        timestamp acquired_at
        integer usage_count
        timestamp last_used_at
    }

    player_equipped_cosmetics {
        uuid id PK
        uuid character_id FK
        cosmetic_category slot_type
        varchar slot_name
        uuid cosmetic_id FK
        timestamp equipped_at
        timestamp updated_at
    }

    cosmetic_shop_rotations {
        uuid id PK
        cosmetic_rotation_type rotation_type
        timestamp start_date
        timestamp end_date
        jsonb items
        timestamp created_at
    }

    cosmetic_telemetry {
        uuid id PK
        cosmetic_telemetry_event event_type
        uuid character_id FK
        uuid cosmetic_id FK
        jsonb event_data
        timestamp created_at
    }
```

## Таблицы

### cosmetic_items

Каталог косметических предметов.

**Колонки:**
- `id` (UUID, PK) - Уникальный идентификатор
- `code` (VARCHAR(50), UNIQUE) - Код косметики
- `name` (VARCHAR(255)) - Название
- `category` (cosmetic_category) - Категория: character_skin, weapon_skin, emote, title, name_plate
- `rarity` (cosmetic_rarity) - Редкость: common, rare, epic, legendary
- `cost` (JSONB) - Стоимость (валюта, количество)
- `assets` (JSONB) - Ассеты (пути к файлам, ссылки)
- `description` (TEXT) - Описание
- `is_exclusive` (BOOLEAN) - Эксклюзивная косметика
- `is_time_limited` (BOOLEAN) - Ограниченная по времени
- `available_from` (TIMESTAMP) - Доступна с
- `available_until` (TIMESTAMP) - Доступна до
- `level_requirement` (INTEGER) - Требуемый уровень
- `created_at` (TIMESTAMP) - Время создания
- `updated_at` (TIMESTAMP) - Время обновления

**Индексы:**
- `idx_cosmetic_items_code` - По коду
- `idx_cosmetic_items_category` - По категории и редкости
- `idx_cosmetic_items_rarity` - По редкости
- `idx_cosmetic_items_is_exclusive` - По эксклюзивности
- `idx_cosmetic_items_is_time_limited` - По ограничению времени
- `idx_cosmetic_items_level_requirement` - По требуемому уровню

### player_cosmetics

Владение косметикой игроками.

**Колонки:**
- `id` (UUID, PK) - Уникальный идентификатор
- `character_id` (UUID, FK) - ID персонажа
- `cosmetic_id` (UUID, FK) - ID косметики
- `source` (VARCHAR(50)) - Источник получения: shop, event, achievement, battle_pass и т.д.
- `acquired_at` (TIMESTAMP) - Время получения
- `usage_count` (INTEGER) - Количество использований
- `last_used_at` (TIMESTAMP) - Время последнего использования

**Ограничения:**
- UNIQUE (character_id, cosmetic_id) - Один персонаж может владеть одной косметикой один раз

**Индексы:**
- `idx_player_cosmetics_character_id` - По персонажу и времени получения
- `idx_player_cosmetics_cosmetic_id` - По косметике
- `idx_player_cosmetics_source` - По источнику
- `idx_player_cosmetics_last_used_at` - По времени последнего использования

### player_equipped_cosmetics

Экипированная косметика игроков.

**Колонки:**
- `id` (UUID, PK) - Уникальный идентификатор
- `character_id` (UUID, FK) - ID персонажа
- `slot_type` (cosmetic_category) - Тип слота: character_skin, weapon_skin, emote, title, name_plate
- `slot_name` (VARCHAR(50)) - Имя слота (например, primary_weapon, secondary_weapon)
- `cosmetic_id` (UUID, FK) - ID косметики (NULL если слот пуст)
- `equipped_at` (TIMESTAMP) - Время экипировки
- `updated_at` (TIMESTAMP) - Время обновления

**Ограничения:**
- UNIQUE (character_id, slot_type, slot_name) - Один персонаж может экипировать одну косметику в один слот

**Индексы:**
- `idx_player_equipped_cosmetics_character_id` - По персонажу и типу слота
- `idx_player_equipped_cosmetics_cosmetic_id` - По косметике
- `idx_player_equipped_cosmetics_slot_type` - По типу слота и имени

### cosmetic_shop_rotations

Ротации магазина косметики.

**Колонки:**
- `id` (UUID, PK) - Уникальный идентификатор
- `rotation_type` (cosmetic_rotation_type) - Тип ротации: daily, weekly
- `start_date` (TIMESTAMP) - Дата начала
- `end_date` (TIMESTAMP) - Дата окончания
- `items` (JSONB) - Список предметов в ротации
- `created_at` (TIMESTAMP) - Время создания

**Ограничения:**
- CHECK (end_date > start_date) - Дата окончания должна быть позже даты начала

**Индексы:**
- `idx_cosmetic_shop_rotations_type` - По типу ротации и дате начала
- `idx_cosmetic_shop_rotations_dates` - По датам
- `idx_cosmetic_shop_rotations_active` - По активным ротациям

### cosmetic_telemetry

Телеметрия косметики.

**Колонки:**
- `id` (UUID, PK) - Уникальный идентификатор
- `event_type` (cosmetic_telemetry_event) - Тип события: acquired, equipped, unequipped, purchased, viewed
- `character_id` (UUID, FK) - ID персонажа
- `cosmetic_id` (UUID, FK) - ID косметики
- `event_data` (JSONB) - Дополнительные данные события
- `created_at` (TIMESTAMP) - Время события

**Индексы:**
- `idx_cosmetic_telemetry_event_type` - По типу события и времени
- `idx_cosmetic_telemetry_character_id` - По персонажу и времени
- `idx_cosmetic_telemetry_cosmetic_id` - По косметике и времени
- `idx_cosmetic_telemetry_created_at` - По времени события

## ENUM типы

### cosmetic_category
- `character_skin` - Скин персонажа
- `weapon_skin` - Скин оружия
- `emote` - Эмоция
- `title` - Титул
- `name_plate` - Табличка с именем

### cosmetic_rarity
- `common` - Обычная
- `rare` - Редкая
- `epic` - Эпическая
- `legendary` - Легендарная

### cosmetic_rotation_type
- `daily` - Ежедневная
- `weekly` - Еженедельная

### cosmetic_telemetry_event
- `acquired` - Получено
- `equipped` - Экипировано
- `unequipped` - Снято
- `purchased` - Куплено
- `viewed` - Просмотрено

## Связи

- `player_cosmetics.character_id` → `mvp_core.characters.id` (CASCADE при удалении)
- `player_cosmetics.cosmetic_id` → `content.cosmetic_items.id` (CASCADE при удалении)
- `player_equipped_cosmetics.character_id` → `mvp_core.characters.id` (CASCADE при удалении)
- `player_equipped_cosmetics.cosmetic_id` → `content.cosmetic_items.id` (SET NULL при удалении)
- `cosmetic_telemetry.character_id` → `mvp_core.characters.id` (CASCADE при удалении)
- `cosmetic_telemetry.cosmetic_id` → `content.cosmetic_items.id` (CASCADE при удалении)

## Миграция

Файл: `infrastructure/liquibase/migrations/V1_83__cosmetic_system_tables.sql`

