<!-- Issue: #140876070 -->
# Combat Hacking System - Database Schema

## Обзор

Схема базы данных для системы боевого взлома, включающая типы взлома, сети, состояние перегрева и доступ к сетям. Интегрируется с боевой системой для выполнения взлома в боевых сценариях.

## ERD Диаграмма

```mermaid
erDiagram
    hacking_types ||--o{ combat_hacking_executions : "used_in"
    hacking_networks ||--o{ combat_hacking_executions : "targets"
    hacking_networks ||--o{ hacking_network_access : "accessed"
    character ||--o{ combat_hacking_executions : "performs"
    character ||--o{ hacking_overheat_state : "has"
    character ||--o{ hacking_network_access : "has_access"
    combat_sessions ||--o{ combat_hacking_executions : "part_of"
    combat_sessions ||--o{ hacking_overheat_state : "session_state"

    hacking_types {
        uuid id PK
        varchar type_name UNIQUE
        hacking_type_category category
        varchar class_requirement
        jsonb skill_requirement
        integer overheat_cost
        integer cooldown_duration
        text description
        timestamp created_at
        timestamp updated_at
    }

    hacking_networks {
        uuid id PK
        varchar network_name UNIQUE
        hacking_network_type network_type
        integer security_level
        hacking_access_method access_method
        jsonb protection_levels
        jsonb available_demons
        text description
        timestamp created_at
        timestamp updated_at
    }

    combat_hacking_executions {
        uuid id PK
        uuid character_id FK
        uuid session_id FK
        uuid hacking_type_id FK
        uuid hacking_network_id FK
        varchar hacking_type
        uuid network_id
        uuid target_id
        varchar target_type
        timestamp executed_at
        jsonb effects_applied
        integer overheat_generated
        timestamp created_at
    }

    hacking_overheat_state {
        uuid id PK
        uuid character_id FK
        uuid session_id FK
        integer current_heat
        integer max_heat
        boolean is_overheated
        integer cooling_applied
        timestamp last_update
        timestamp created_at
    }

    hacking_network_access {
        uuid id PK
        uuid character_id FK
        uuid network_id FK
        integer access_level
        hacking_access_method access_method
        timestamp granted_at
        timestamp expires_at
        boolean is_active
        timestamp created_at
    }
```

## Описание таблиц

### hacking_types

Таблица типов взлома. Хранит каталог типов взлома с требованиями и характеристиками.

**Ключевые поля:**
- `type_name`: Название типа взлома (UNIQUE)
- `category`: Категория типа взлома (enemy, device, infrastructure, combat_scenario)
- `class_requirement`: Требуемый класс для использования (nullable)
- `skill_requirement`: Требования к навыкам (JSONB)
- `overheat_cost`: Стоимость перегрева при использовании (INTEGER, >= 0)
- `cooldown_duration`: Длительность кулдауна в секундах (INTEGER, >= 0)
- `description`: Описание типа взлома

**Индексы:**
- По `category` для фильтрации по категории
- По `class_requirement` для фильтрации по классу
- По `overheat_cost` для сортировки по стоимости

### hacking_networks

Таблица сетей для взлома. Хранит детальную информацию о сетях (локальные, корпоративные, городские, персональные).

**Ключевые поля:**
- `network_name`: Название сети (UNIQUE)
- `network_type`: Тип сети (local, corporate, city, personal)
- `security_level`: Уровень защиты сети (INTEGER, 0-100)
- `access_method`: Метод доступа (remote, physical, hybrid)
- `protection_levels`: Уровни защиты сети (JSONB)
- `available_demons`: Доступные демоны для взлома (JSONB)
- `description`: Описание сети

**Индексы:**
- По `network_type` для фильтрации по типу
- По `security_level` для сортировки по уровню защиты
- По `access_method` для фильтрации по методу доступа

### hacking_overheat_state

Таблица состояния перегрева. Хранит информацию о состоянии перегрева системы взлома персонажа в боевой сессии.

**Ключевые поля:**
- `character_id`: ID персонажа (FK к characters)
- `session_id`: ID боевой сессии (FK к combat_sessions, nullable)
- `current_heat`: Текущий уровень нагрева (INTEGER, >= 0)
- `max_heat`: Максимальный уровень нагрева (INTEGER, > 0)
- `is_overheated`: Флаг перегрева системы (BOOLEAN)
- `cooling_applied`: Количество примененного охлаждения (INTEGER, >= 0)
- `last_update`: Время последнего обновления

**Индексы:**
- По `(character_id, is_overheated)` для состояния персонажа
- По `session_id` для состояния в сессии
- По `(is_overheated, current_heat DESC)` для перегретых систем

### hacking_network_access

Таблица доступа к сетям. Хранит информацию о доступе персонажей к сетям для взлома.

**Ключевые поля:**
- `character_id`: ID персонажа (FK к characters)
- `network_id`: ID сети (FK к hacking_networks)
- `access_level`: Уровень доступа к сети (INTEGER, >= 0)
- `access_method`: Метод доступа (remote, physical, hybrid)
- `granted_at`: Время предоставления доступа
- `expires_at`: Время истечения доступа (nullable)
- `is_active`: Флаг активности доступа

**Индексы:**
- По `(character_id, is_active)` для активных доступов персонажа
- По `(network_id, is_active)` для активных доступов к сети
- По `expires_at` для истекающих доступов

### combat_hacking_executions (обновлена)

Таблица выполнений взлома в бою. Уже создана в V1_49, дополнена связями с hacking_types и hacking_networks.

**Добавленные поля:**
- `hacking_type_id`: ID типа взлома (FK к hacking_types, nullable)
- `hacking_network_id`: ID сети (FK к hacking_networks, nullable)
- `overheat_generated`: Количество сгенерированного перегрева (INTEGER, >= 0)

**Индексы:**
- По `hacking_type_id` для фильтрации по типу взлома
- По `hacking_network_id` для фильтрации по сети

## Constraints и валидация

### CHECK Constraints

- `hacking_types.overheat_cost`: Должно быть >= 0
- `hacking_types.cooldown_duration`: Должно быть >= 0
- `hacking_networks.security_level`: Должно быть >= 0 и <= 100
- `hacking_overheat_state.current_heat`: Должно быть >= 0
- `hacking_overheat_state.max_heat`: Должно быть > 0
- `hacking_overheat_state.cooling_applied`: Должно быть >= 0
- `hacking_network_access.access_level`: Должно быть >= 0
- `combat_hacking_executions.overheat_generated`: Должно быть >= 0

### ENUM Types

- `hacking_type_category`: enemy, device, infrastructure, combat_scenario
- `hacking_network_type`: local, corporate, city, personal
- `hacking_access_method`: remote, physical, hybrid

### Foreign Keys

- `hacking_overheat_state.character_id` → `mvp_core.character.id` (ON DELETE CASCADE)
- `hacking_overheat_state.session_id` → `mvp_core.combat_sessions.id` (ON DELETE CASCADE)
- `hacking_network_access.character_id` → `mvp_core.character.id` (ON DELETE CASCADE)
- `hacking_network_access.network_id` → `hacking.hacking_networks.id` (ON DELETE CASCADE)
- `combat_hacking_executions.hacking_type_id` → `hacking.hacking_types.id` (ON DELETE SET NULL)
- `combat_hacking_executions.hacking_network_id` → `hacking.hacking_networks.id` (ON DELETE SET NULL)

### Unique Constraints

- `hacking_types(type_name)`: Уникальное название типа взлома
- `hacking_networks(network_name)`: Уникальное название сети
- `hacking_overheat_state(character_id, session_id)`: Одно состояние перегрева на персонажа и сессию
- `hacking_network_access(character_id, network_id)`: Один доступ на персонажа и сеть

## Оптимизация запросов

### Частые запросы

1. **Поиск типов взлома по категории:**
   ```sql
   SELECT * FROM hacking.hacking_types 
   WHERE category = $1 
   ORDER BY overheat_cost ASC;
   ```
   Использует индекс `category`.

2. **Поиск доступных сетей:**
   ```sql
   SELECT * FROM hacking.hacking_networks 
   WHERE network_type = $1 AND security_level <= $2 
   ORDER BY security_level ASC;
   ```
   Использует индекс `(network_type, security_level)`.

3. **Проверка состояния перегрева:**
   ```sql
   SELECT * FROM hacking.hacking_overheat_state 
   WHERE character_id = $1 AND session_id = $2;
   ```
   Использует UNIQUE constraint `(character_id, session_id)`.

4. **Поиск активных доступов к сетям:**
   ```sql
   SELECT * FROM hacking.hacking_network_access 
   WHERE character_id = $1 AND is_active = true 
   AND (expires_at IS NULL OR expires_at > NOW());
   ```
   Использует индекс `(character_id, is_active)`.

## Миграции

### Существующие миграции:
- `V1_49__combat_extended_mechanics_tables.sql` - базовые таблицы взлома (combat_hacking_executions, combat_hacking_networks)
- `V1_56__combat_hacking_system_tables.sql` - дополнение схемы (hacking_types, hacking_networks, hacking_overheat_state, hacking_network_access)

### Применение миграций:
```bash
liquibase update --changelog-file=infrastructure/liquibase/changelog.yaml
```

## Соответствие архитектуре

Схема БД полностью соответствует архитектуре из `knowledge/implementation/architecture/combat-hacking-integration-architecture.yaml`:
- OK Все таблицы из архитектуры созданы
- OK Все поля соответствуют описанию
- OK ENUM типы созданы для всех перечислений
- OK Индексы оптимизированы для частых запросов
- OK Constraints обеспечивают целостность данных
- OK Foreign Keys настроены с CASCADE для автоматической очистки
- OK Поддержка JSONB для гибкого хранения данных
- OK Интеграция с существующими таблицами (combat_hacking_executions, combat_hacking_networks)

## Особенности реализации

### JSONB поля

Использование JSONB для гибкого хранения:
- `skill_requirement`: Требования к навыкам для типов взлома
- `protection_levels`: Уровни защиты сетей
- `available_demons`: Доступные демоны для взлома
- `effects_applied`: Примененные эффекты взлома

### Типы взлома

Система поддерживает различные категории типов взлома:
- **enemy**: Взлом врагов
- **device**: Взлом устройств
- **infrastructure**: Взлом инфраструктуры
- **combat_scenario**: Взлом в боевых сценариях

### Сети для взлома

Система поддерживает различные типы сетей:
- **local**: Локальные сети
- **corporate**: Корпоративные сети
- **city**: Городские сети
- **personal**: Персональные сети

### Методы доступа

Система поддерживает различные методы доступа:
- **remote**: Удаленный доступ
- **physical**: Физический доступ
- **hybrid**: Гибридный доступ

### Перегрев системы

Система перегрева включает:
- **current_heat**: Текущий уровень нагрева
- **max_heat**: Максимальный уровень нагрева
- **is_overheated**: Флаг перегрева (блокирует взлом)
- **cooling_applied**: Количество примененного охлаждения

### Интеграция с боевой системой

Взлом интегрирован с боевой системой через:
- `combat_hacking_executions`: Выполнения взлома в бою
- `hacking_overheat_state`: Состояние перегрева в боевой сессии
- Связи с `combat_sessions` для отслеживания взлома в бою


