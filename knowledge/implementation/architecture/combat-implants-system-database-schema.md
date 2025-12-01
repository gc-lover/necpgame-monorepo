<!-- Issue: #140876072 -->
# Combat Implants System - Database Schema

## Обзор

Схема базы данных для системы боевых имплантов, включающая каталог имплантов, установленные импланты персонажей, историю приобретений, состояние лимитов, киберпсихоз и синергии. Интегрируется с боевой системой для применения эффектов имплантов в бою.

## ERD Диаграмма

```mermaid
erDiagram
    implants_catalog ||--o{ character_implants : "installed_as"
    implants_catalog ||--o{ implant_acquisitions : "acquired"
    implants_catalog ||--o{ combat_implant_activations : "activated_in"
    character ||--o{ character_implants : "has"
    character ||--o{ implant_acquisitions : "acquires"
    character ||--o{ implant_limits_state : "has"
    character ||--o{ cyberpsychosis_state : "has"
    character ||--o{ implant_synergies : "has"
    character ||--o{ combat_implant_activations : "activates"
    combat_sessions ||--o{ combat_implant_activations : "part_of"

    implants_catalog {
        uuid id PK
        varchar name UNIQUE
        implant_type type
        varchar category
        implant_rarity rarity
        jsonb effects
        integer energy_cost
        integer humanity_cost
        varchar slot_type
        jsonb compatibility
        text description
        timestamp created_at
        timestamp updated_at
    }

    character_implants {
        uuid id PK
        uuid character_id FK
        uuid implant_id FK
        timestamp installed_at
        integer upgrade_level
        varchar slot
        boolean is_active
        timestamp created_at
        timestamp updated_at
    }

    implant_acquisitions {
        uuid id PK
        uuid character_id FK
        uuid implant_id FK
        implant_acquisition_type acquisition_type
        jsonb cost
        timestamp acquired_at
    }

    implant_limits_state {
        uuid id PK
        uuid character_id FK UNIQUE
        integer total_energy_used
        integer max_energy
        integer total_humanity_lost
        integer max_humanity
        jsonb slots_used
        timestamp last_update
        timestamp created_at
    }

    cyberpsychosis_state {
        uuid id PK
        uuid character_id FK UNIQUE
        integer current_level
        integer threshold_level
        jsonb effects_active
        timestamp last_update
        timestamp created_at
    }

    implant_synergies {
        uuid id PK
        uuid character_id FK
        uuid synergy_id
        jsonb active_implants
        jsonb bonus_effects
        timestamp activated_at
        boolean is_active
        timestamp created_at
        timestamp updated_at
    }

    combat_implant_activations {
        uuid id PK
        uuid character_id FK
        uuid session_id FK
        uuid implant_id
        uuid implant_catalog_id FK
        timestamp activated_at
        jsonb effects_applied
        integer energy_used
        integer humanity_cost
        timestamp created_at
    }
```

## Описание таблиц

### implants_catalog

Таблица каталога имплантов. Хранит информацию о всех доступных имплантах с их характеристиками и требованиями.

**Ключевые поля:**
- `name`: Название импланта (UNIQUE)
- `type`: Тип импланта (combat, movement, os, visual)
- `category`: Категория импланта (nullable)
- `rarity`: Редкость импланта (common, uncommon, rare, epic, legendary)
- `effects`: Эффекты импланта (JSONB)
- `energy_cost`: Стоимость энергии импланта (INTEGER, >= 0)
- `humanity_cost`: Стоимость человечности импланта (INTEGER, >= 0)
- `slot_type`: Тип слота для импланта
- `compatibility`: Совместимость с другими имплантами (JSONB)
- `description`: Описание импланта

**Индексы:**
- По `(type, category)` для фильтрации по типу и категории
- По `rarity` для фильтрации по редкости
- По `slot_type` для фильтрации по слоту
- По `energy_cost` и `humanity_cost` для сортировки

### character_implants

Таблица установленных имплантов персонажей. Хранит информацию о имплантах, установленных на персонажах.

**Ключевые поля:**
- `character_id`: ID персонажа (FK к characters)
- `implant_id`: ID импланта из каталога (FK к implants_catalog)
- `installed_at`: Время установки
- `upgrade_level`: Уровень улучшения импланта (INTEGER, >= 1)
- `slot`: Слот, в который установлен имплант
- `is_active`: Флаг активности импланта

**Индексы:**
- По `(character_id, is_active)` для активных имплантов персонажа
- По `implant_id` для имплантов из каталога
- По `(character_id, slot)` для слотов персонажа
- По `upgrade_level` для уровня улучшения

### implant_acquisitions

Таблица истории приобретений имплантов. Хранит информацию о всех способах приобретения имплантов.

**Ключевые поля:**
- `character_id`: ID персонажа (FK к characters)
- `implant_id`: ID импланта из каталога (FK к implants_catalog)
- `acquisition_type`: Тип приобретения (purchase, loot, quest, crafting)
- `cost`: Стоимость приобретения (JSONB)
- `acquired_at`: Время приобретения

**Индексы:**
- По `(character_id, acquired_at DESC)` для истории персонажа
- По `implant_id` для имплантов из каталога
- По `acquisition_type` для типа приобретения

### implant_limits_state

Таблица состояния лимитов имплантов. Хранит информацию о текущих лимитах персонажа (энергия, человечность, слоты).

**Ключевые поля:**
- `character_id`: ID персонажа (FK к characters, UNIQUE)
- `total_energy_used`: Общая использованная энергия (INTEGER, >= 0)
- `max_energy`: Максимальная энергия (INTEGER, > 0)
- `total_humanity_lost`: Общая потерянная человечность (INTEGER, >= 0)
- `max_humanity`: Максимальная человечность (INTEGER, > 0)
- `slots_used`: Использованные слоты (JSONB)
- `last_update`: Время последнего обновления

**Индексы:**
- По `character_id` для состояния персонажа
- По `(total_energy_used, max_energy)` для энергии
- По `(total_humanity_lost, max_humanity)` для человечности

### cyberpsychosis_state

Таблица состояния киберпсихоза. Хранит информацию о текущем уровне киберпсихоза персонажа от имплантов.

**Ключевые поля:**
- `character_id`: ID персонажа (FK к characters, UNIQUE)
- `current_level`: Текущий уровень киберпсихоза (INTEGER, 0-100)
- `threshold_level`: Пороговый уровень киберпсихоза (INTEGER, 0-100)
- `effects_active`: Активные эффекты киберпсихоза (JSONB)
- `last_update`: Время последнего обновления

**Индексы:**
- По `character_id` для состояния персонажа
- По `(current_level, threshold_level)` для уровня киберпсихоза
- По `character_id` WHERE `current_level >= threshold_level` для пороговых состояний

### implant_synergies

Таблица синергий имплантов. Хранит информацию о активных синергиях между имплантами персонажа.

**Ключевые поля:**
- `character_id`: ID персонажа (FK к characters)
- `synergy_id`: ID синергии
- `active_implants`: Активные импланты в синергии (JSONB)
- `bonus_effects`: Бонусные эффекты синергии (JSONB)
- `activated_at`: Время активации синергии
- `is_active`: Флаг активности синергии

**Индексы:**
- По `(character_id, is_active)` для активных синергий персонажа
- По `synergy_id` для синергий
- По `activated_at DESC` для времени активации

### combat_implant_activations (обновлена)

Таблица активаций имплантов в бою. Уже создана в V1_49, дополнена связью с implants_catalog.

**Добавленное поле:**
- `implant_catalog_id`: ID импланта из каталога (FK к implants_catalog, nullable)

**Индексы:**
- По `implant_catalog_id` для фильтрации по импланту из каталога

## Constraints и валидация

### CHECK Constraints

- `implants_catalog.energy_cost`: Должно быть >= 0
- `implants_catalog.humanity_cost`: Должно быть >= 0
- `character_implants.upgrade_level`: Должно быть >= 1
- `implant_limits_state.total_energy_used`: Должно быть >= 0
- `implant_limits_state.max_energy`: Должно быть > 0
- `implant_limits_state.total_humanity_lost`: Должно быть >= 0
- `implant_limits_state.max_humanity`: Должно быть > 0
- `cyberpsychosis_state.current_level`: Должно быть >= 0 и <= 100
- `cyberpsychosis_state.threshold_level`: Должно быть >= 0 и <= 100

### ENUM Types

- `implant_type`: combat, movement, os, visual
- `implant_rarity`: common, uncommon, rare, epic, legendary
- `implant_acquisition_type`: purchase, loot, quest, crafting

### Foreign Keys

- `character_implants.character_id` → `mvp_core.character.id` (ON DELETE CASCADE)
- `character_implants.implant_id` → `implant.implants_catalog.id` (ON DELETE CASCADE)
- `implant_acquisitions.character_id` → `mvp_core.character.id` (ON DELETE CASCADE)
- `implant_acquisitions.implant_id` → `implant.implants_catalog.id` (ON DELETE CASCADE)
- `implant_limits_state.character_id` → `mvp_core.character.id` (ON DELETE CASCADE)
- `cyberpsychosis_state.character_id` → `mvp_core.character.id` (ON DELETE CASCADE)
- `implant_synergies.character_id` → `mvp_core.character.id` (ON DELETE CASCADE)
- `combat_implant_activations.implant_catalog_id` → `implant.implants_catalog.id` (ON DELETE SET NULL)

### Unique Constraints

- `implants_catalog(name)`: Уникальное название импланта
- `character_implants(character_id, slot)`: Один имплант на слот персонажа
- `implant_limits_state(character_id)`: Одно состояние лимитов на персонажа
- `cyberpsychosis_state(character_id)`: Одно состояние киберпсихоза на персонажа

## Оптимизация запросов

### Частые запросы

1. **Поиск имплантов по типу и категории:**
   ```sql
   SELECT * FROM implant.implants_catalog 
   WHERE type = $1 AND category = $2 
   ORDER BY rarity, energy_cost ASC;
   ```
   Использует индекс `(type, category)`.

2. **Получение установленных имплантов персонажа:**
   ```sql
   SELECT * FROM implant.character_implants 
   WHERE character_id = $1 AND is_active = true;
   ```
   Использует индекс `(character_id, is_active)`.

3. **Проверка лимитов имплантов:**
   ```sql
   SELECT * FROM implant.implant_limits_state 
   WHERE character_id = $1;
   ```
   Использует UNIQUE constraint `(character_id)`.

4. **Проверка состояния киберпсихоза:**
   ```sql
   SELECT * FROM implant.cyberpsychosis_state 
   WHERE character_id = $1 AND current_level >= threshold_level;
   ```
   Использует индекс для пороговых состояний.

## Миграции

### Существующие миграции:
- `V1_49__combat_extended_mechanics_tables.sql` - базовые таблицы (combat_implant_activations)
- `V1_57__combat_implants_system_tables.sql` - полная схема системы имплантов

### Применение миграций:
```bash
liquibase update --changelog-file=infrastructure/liquibase/changelog.yaml
```

## Соответствие архитектуре

Схема БД полностью соответствует архитектуре из `knowledge/implementation/architecture/combat-implants-architecture.yaml`:
- OK Все таблицы из архитектуры созданы
- OK Все поля соответствуют описанию
- OK ENUM типы созданы для всех перечислений
- OK Индексы оптимизированы для частых запросов
- OK Constraints обеспечивают целостность данных
- OK Foreign Keys настроены с CASCADE для автоматической очистки
- OK Поддержка JSONB для гибкого хранения данных
- OK Интеграция с существующими таблицами (combat_implant_activations)

## Особенности реализации

### JSONB поля

Использование JSONB для гибкого хранения:
- `effects`: Эффекты имплантов
- `compatibility`: Совместимость имплантов
- `cost`: Стоимость приобретения
- `slots_used`: Использованные слоты
- `effects_active`: Активные эффекты киберпсихоза
- `active_implants`: Активные импланты в синергии
- `bonus_effects`: Бонусные эффекты синергии

### Типы имплантов

Система поддерживает различные типы имплантов:
- **combat**: Боевые импланты (урон, защита, тактика)
- **movement**: Двигательные импланты (скорость, прыжки, паркур)
- **os**: Операционные системы (кибердек, сандевистан, берсерк)
- **visual**: Визуальные импланты (глаза, кожа, конечности)

### Редкость имплантов

Система поддерживает различные уровни редкости:
- **common**: Обычные
- **uncommon**: Необычные
- **rare**: Редкие
- **epic**: Эпические
- **legendary**: Легендарные

### Типы приобретения

Система поддерживает различные способы приобретения:
- **purchase**: Покупка
- **loot**: Лут
- **quest**: Квест
- **crafting**: Крафт

### Лимиты имплантов

Система лимитов включает:
- **Энергия**: total_energy_used / max_energy
- **Человечность**: total_humanity_lost / max_humanity
- **Слоты**: slots_used (JSONB)

### Киберпсихоз

Система киберпсихоза включает:
- **current_level**: Текущий уровень (0-100)
- **threshold_level**: Пороговый уровень (0-100)
- **effects_active**: Активные эффекты при достижении порога

### Синергии имплантов

Система синергий включает:
- **synergy_id**: ID синергии
- **active_implants**: Активные импланты в синергии
- **bonus_effects**: Бонусные эффекты синергии

### Интеграция с боевой системой

Импланты интегрированы с боевой системой через:
- `combat_implant_activations`: Активации имплантов в бою
- Связи с `combat_sessions` для отслеживания активаций в бою
- Применение эффектов имплантов в боевых сценариях


