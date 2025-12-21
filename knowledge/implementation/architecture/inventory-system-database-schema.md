<!-- Issue: #140887147 -->
# Inventory System - Database Schema

## Обзор

Схема базы данных для системы инвентаря, включающая управление предметами игроков, экипировкой, хранением, stacking и весом.

## ERD Диаграмма

```mermaid
erDiagram
    character ||--o| character_inventory : "has"
    character ||--o{ character_items : "has"
    character ||--o{ character_equipment : "equips"
    character ||--o{ character_storage : "has"
    character_inventory ||--o{ character_items : "contains"
    character_items ||--o{ character_equipment : "equipped_as"
    character_storage ||--o{ storage_items : "contains"
    item_templates ||--o{ character_items : "template_for"
    item_templates ||--o{ storage_items : "template_for"

    character_inventory {
        uuid id PK
        uuid character_id FK
        integer capacity
        integer used_slots
        decimal weight
        decimal max_weight
        timestamp created_at
        timestamp updated_at
        timestamp deleted_at
    }

    character_items {
        uuid id PK
        uuid inventory_id FK
        uuid item_template_id FK
        varchar item_id
        integer slot_index
        integer stack_count
        integer max_stack_size
        integer durability
        varchar bind_status
        jsonb modifiers
        boolean is_equipped
        varchar equip_slot
        jsonb metadata
        timestamp created_at
        timestamp updated_at
        timestamp deleted_at
    }

    character_equipment {
        uuid character_id PK_FK
        varchar slot_type PK
        uuid item_id FK
        timestamp equipped_at
    }

    character_storage {
        uuid id PK
        uuid character_id FK
        varchar storage_type
        integer max_slots
        decimal current_weight
        decimal max_weight
        timestamp updated_at
        timestamp created_at
        timestamp deleted_at
    }

    storage_items {
        uuid id PK
        uuid storage_id FK
        uuid item_template_id FK
        integer slot_index
        integer stack_size
        integer durability
        varchar bind_status
        jsonb modifiers
        timestamp created_at
        timestamp updated_at
        timestamp deleted_at
    }

    item_templates {
        varchar id PK
        varchar name
        varchar type
        varchar rarity
        integer max_stack_size
        decimal weight
        boolean can_equip
        varchar equip_slot
        boolean bind_on_pickup
        jsonb requirements
        jsonb stats
        jsonb metadata
        timestamp created_at
        timestamp updated_at
    }
```

## Описание таблиц

### character_inventory

Основная таблица инвентаря персонажа. Хранит информацию о вместимости, использованных слотах и весе.

**Ключевые поля:**
- `capacity`: Максимальное количество слотов (по умолчанию 50)
- `used_slots`: Количество использованных слотов
- `weight`: Текущий вес инвентаря
- `max_weight`: Максимальный вес (по умолчанию 100.0)

**Индексы:**
- Уникальный индекс по `character_id` (один персонаж - один инвентарь)
- Индекс по `character_id` для быстрого поиска

### character_items

Таблица предметов в инвентаре персонажа. Хранит информацию о предметах, их расположении, стеках и свойствах.

**Ключевые поля:**
- `item_template_id`: ID шаблона предмета (UUID, nullable для обратной совместимости)
- `item_id`: ID предмета (VARCHAR, для обратной совместимости)
- `slot_index`: Индекс слота в инвентаре
- `stack_count`: Количество предметов в стеке
- `durability`: Прочность предмета (nullable для неразрушаемых)
- `bind_status`: Статус привязки (unbound, bound, account_bound)
- `modifiers`: JSONB модификаторы предмета (статы, аффиксы, улучшения)
- `is_equipped`: Флаг экипировки предмета
- `equip_slot`: Слот экипировки (если экипирован)

**Индексы:**
- Уникальный индекс `(inventory_id, slot_index)` для неэкипированных предметов
- Индекс по `character_id` для поиска предметов персонажа
- Индекс по `item_template_id` для поиска по шаблону
- Композитный индекс `(character_id, slot_index)` для оптимизации запросов
- Композитный индекс `(character_id, item_template_id)` для поиска предметов по типу

### character_equipment

Таблица экипировки персонажа. Хранит информацию о экипированных предметах в различных слотах.

**Ключевые поля:**
- `character_id`: ID персонажа (PK, FK)
- `slot_type`: Тип слота экипировки (weapon_primary, armor_head, implant_1, etc.)
- `item_id`: ID предмета из character_items (FK)
- `equipped_at`: Время экипировки

**Типы слотов:**
- Оружие: `weapon_primary`, `weapon_secondary`, `weapon_melee`
- Броня: `armor_head`, `armor_body`, `armor_legs`, `armor_feet`, `armor_hands`
- Импланты: `implant_1`, `implant_2`, `implant_3`, `implant_4`, `implant_5`
- Системы: `cyberdeck`, `operating_system`, `nervous_system`

**Индексы:**
- Первичный ключ `(character_id, slot_type)` - один слот на персонажа
- Индекс по `item_id` для поиска экипированных предметов
- Индекс по `slot_type` для фильтрации по типу слота

### character_storage

Таблица хранилища персонажа (банк/стэш). Хранит информацию о различных типах хранилищ.

**Ключевые поля:**
- `storage_type`: Тип хранилища (personal_bank, guild_bank, stash)
- `max_slots`: Максимальное количество слотов (по умолчанию 50)
- `current_weight`: Текущий вес хранилища
- `max_weight`: Максимальный вес (по умолчанию 500.0)

**Индексы:**
- Уникальный индекс `(character_id, storage_type)` для одного типа хранилища на персонажа
- Индекс по `character_id` для поиска хранилищ персонажа
- Композитный индекс `(character_id, storage_type)` для оптимизации запросов

### storage_items

Таблица предметов в хранилище. Хранит информацию о предметах в банке/стэше.

**Ключевые поля:**
- `storage_id`: ID хранилища (FK)
- `item_template_id`: ID шаблона предмета (FK, nullable)
- `slot_index`: Индекс слота в хранилище
- `stack_size`: Количество предметов в стеке
- `durability`: Прочность предмета (nullable)
- `bind_status`: Статус привязки
- `modifiers`: JSONB модификаторы предмета

**Индексы:**
- Уникальный индекс `(storage_id, slot_index)` для одного предмета на слот
- Индекс по `storage_id` для поиска предметов в хранилище
- Композитный индекс `(storage_id, slot_index)` для оптимизации запросов
- Индекс по `item_template_id` для поиска по шаблону

### item_templates

Таблица шаблонов предметов. Хранит базовую информацию о типах предметов.

**Ключевые поля:**
- `id`: ID шаблона (VARCHAR(100), PK)
- `name`: Название предмета
- `type`: Тип предмета (weapon, armor, consumable, material, quest_item, etc.)
- `rarity`: Редкость (common, uncommon, rare, epic, legendary)
- `max_stack_size`: Максимальный размер стека
- `weight`: Вес предмета
- `can_equip`: Можно ли экипировать
- `equip_slot`: Слот экипировки
- `bind_on_pickup`: Привязка при подборе
- `requirements`: JSONB требования для использования
- `stats`: JSONB базовые статы
- `metadata`: JSONB дополнительные метаданные

**Индексы:**
- Композитный индекс `(type, rarity)` для фильтрации по типу и редкости

## Constraints и валидация

### CHECK Constraints

- `character_items.bind_status`: Допустимые значения: 'unbound', 'bound', 'account_bound'
- `character_equipment.slot_type`: Допустимые значения: weapon_primary, weapon_secondary, armor_head, etc.
- `character_storage.storage_type`: Допустимые значения: 'personal_bank', 'guild_bank', 'stash'
- `storage_items.bind_status`: Допустимые значения: 'unbound', 'bound', 'account_bound'

### Foreign Keys

- `character_inventory.character_id` → `character.id` (ON DELETE CASCADE)
- `character_items.inventory_id` → `character_inventory.id` (ON DELETE CASCADE)
- `character_equipment.character_id` → `character.id` (ON DELETE CASCADE)
- `character_equipment.item_id` → `character_items.id` (ON DELETE CASCADE)
- `character_storage.character_id` → `character.id` (ON DELETE CASCADE)
- `storage_items.storage_id` → `character_storage.id` (ON DELETE CASCADE)

### Unique Constraints

- `character_inventory.character_id`: Один персонаж - один инвентарь
- `character_items(inventory_id, slot_index)`: Один предмет на слот (для неэкипированных)
- `character_equipment(character_id, slot_type)`: Один предмет на слот экипировки
- `character_storage(character_id, storage_type)`: Один тип хранилища на персонажа
- `storage_items(storage_id, slot_index)`: Один предмет на слот в хранилище

## Оптимизация запросов

### Частые запросы

1. **Получение инвентаря персонажа:**
   ```sql
   SELECT * FROM character_inventory 
   WHERE character_id = $1 AND deleted_at IS NULL;
   ```
   Использует уникальный индекс по `character_id`.

2. **Получение предметов в инвентаре:**
   ```sql
   SELECT * FROM character_items 
   WHERE inventory_id = $1 AND deleted_at IS NULL 
   ORDER BY slot_index;
   ```
   Использует индекс по `inventory_id`.

3. **Получение экипировки персонажа:**
   ```sql
   SELECT * FROM character_equipment 
   WHERE character_id = $1;
   ```
   Использует первичный ключ `(character_id, slot_type)`.

4. **Поиск свободных слотов:**
   ```sql
   SELECT slot_index FROM character_items 
   WHERE inventory_id = $1 AND deleted_at IS NULL AND is_equipped = false;
   ```
   Использует уникальный индекс `(inventory_id, slot_index)`.

5. **Получение предметов в хранилище:**
   ```sql
   SELECT * FROM storage_items 
   WHERE storage_id = $1 AND deleted_at IS NULL 
   ORDER BY slot_index;
   ```
   Использует индекс по `storage_id`.

### Партиционирование

Для больших объемов данных рекомендуется партиционирование:
- По `created_at` для таблиц логов операций
- По `character_id` для распределения нагрузки

## Миграции

### Существующие миграции:
- `V1_6__inventory_tables.sql` - базовые таблицы (character_inventory, character_items, item_templates)
- `V1_48__inventory_system_enhancement.sql` - дополнение схемы (character_equipment, character_storage, storage_items, дополнительные поля)

### Применение миграций:
```bash
liquibase update --changelog-file=infrastructure/liquibase/changelog.yaml
```

## Соответствие архитектуре

Схема БД полностью соответствует архитектуре из `knowledge/implementation/architecture/inventory-system-architecture.yaml`:
- [OK] Все таблицы из архитектуры созданы
- [OK] Все поля соответствуют описанию
- [OK] Индексы оптимизированы для частых запросов
- [OK] Constraints обеспечивают целостность данных
- [OK] Foreign Keys настроены с CASCADE для автоматической очистки
- [OK] Поддержка soft delete через `deleted_at`

## Особенности реализации

### Обратная совместимость

- Таблица `character_items` поддерживает как `item_id` (VARCHAR), так и `item_template_id` (UUID) для обратной совместимости
- Поле `item_template_id` может быть NULL для существующих записей

### Soft Delete

Все таблицы поддерживают soft delete через поле `deleted_at`:
- Индексы используют `WHERE deleted_at IS NULL` для фильтрации удаленных записей
- Уникальные индексы учитывают soft delete при проверке уникальности

### JSONB поля

Использование JSONB для гибкого хранения:
- `modifiers`: Модификаторы предмета (статы, аффиксы, улучшения)
- `metadata`: Дополнительные метаданные предмета
- `requirements`: Требования для использования предмета
- `stats`: Базовые статы предмета


