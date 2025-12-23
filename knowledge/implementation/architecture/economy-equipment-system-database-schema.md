<!-- Issue: #140876086 -->

# Economy Equipment System - Database Schema

## Обзор

Схема базы данных для системы оборудования, включающая каталог оборудования, временную линию культового оборудования,
разблокировки, матрицу характеристик и процедурно сгенерированное оборудование.

## ERD Диаграмма

```mermaid
erDiagram
    equipment_catalog ||--o| iconic_equipment_timeline : "has"
    equipment_catalog ||--o{ generated_equipment : "generates"
    iconic_equipment_timeline ||--o{ iconic_unlocks : "unlocked"
    character ||--o{ iconic_unlocks : "unlocks"
    character ||--o{ generated_equipment : "receives"
    equipment_matrix ||--o{ generated_equipment : "generates"

    equipment_catalog {
        uuid id PK
        varchar name
        equipment_category category
        varchar brand
        equipment_rarity rarity
        jsonb stats
        text signature
        text description
        timestamp created_at
        timestamp updated_at
    }

    iconic_equipment_timeline {
        uuid id PK
        uuid equipment_id FK UNIQUE
        integer era_start
        integer era_end
        jsonb unlock_conditions
        iconic_availability availability
        timestamp unlock_date
        boolean is_active
        timestamp created_at
        timestamp updated_at
    }

    iconic_unlocks {
        uuid id PK
        uuid character_id FK
        uuid iconic_id FK
        timestamp unlocked_at
        iconic_unlock_method unlock_method
        jsonb unlock_data
        timestamp created_at
    }

    equipment_matrix {
        uuid id PK
        varchar brand
        equipment_category category
        equipment_rarity rarity
        jsonb stat_pools
        jsonb modifiers
        integer version
        timestamp updated_at
        timestamp created_at
    }

    generated_equipment {
        uuid id PK
        bigint seed
        varchar brand
        equipment_category category
        equipment_rarity rarity
        jsonb stats
        timestamp generated_at
        uuid character_id FK
        uuid item_id
    }
```

## Описание таблиц

### equipment_catalog

Таблица каталога оборудования. Хранит ручной срез ключевых предметов оборудования.

**Ключевые поля:**

- `id`: UUID первичный ключ
- `name`: Название предмета (VARCHAR(100), NOT NULL)
- `category`: Категория оборудования (ENUM: weapon, armor, cyberware, consumable)
- `brand`: Бренд предмета (VARCHAR(50), nullable)
- `rarity`: Редкость предмета (ENUM: common, uncommon, rare, epic, legendary, iconic)
- `stats`: Характеристики предмета (JSONB, NOT NULL, default: {})
- `signature`: Подпись предмета (TEXT, nullable)
- `description`: Описание предмета (TEXT, nullable)
- `created_at`: Время создания
- `updated_at`: Время последнего обновления

**Индексы:**

- По `(category, brand, rarity)` для фильтрации по категории, бренду и редкости
- По `(brand, category)` для поиска по бренду
- По `(rarity, category)` для поиска по редкости
- По `name` для поиска по названию

### iconic_equipment_timeline

Таблица временной линии культового оборудования. Хранит информацию о культовом оборудовании и его доступности по эпохам.

**Ключевые поля:**

- `id`: UUID первичный ключ
- `equipment_id`: ID предмета из equipment_catalog (FK, UNIQUE)
- `era_start`: Год начала эпохи (INTEGER, 2000-2100, NOT NULL)
- `era_end`: Год конца эпохи (INTEGER, nullable, >= era_start)
- `unlock_conditions`: Условия разблокировки (JSONB, NOT NULL, default: {})
- `availability`: Доступность (ENUM: always, seasonal, event, quest)
- `unlock_date`: Дата разблокировки (TIMESTAMP, nullable)
- `is_active`: Флаг активности (BOOLEAN, NOT NULL, default: true)
- `created_at`: Время создания
- `updated_at`: Время последнего обновления

**Индексы:**

- По `equipment_id` для связи с каталогом
- По `(era_start, era_end)` для поиска по эпохе
- По `(availability, is_active)` для активных предметов
- По `unlock_date` для предметов с датой разблокировки

### iconic_unlocks

Таблица разблокировок культового оборудования. Хранит информацию о разблокировках культового оборудования персонажами.

**Ключевые поля:**

- `id`: UUID первичный ключ
- `character_id`: ID персонажа (FK к characters, NOT NULL)
- `iconic_id`: ID культового предмета из iconic_equipment_timeline (FK, NOT NULL)
- `unlocked_at`: Время разблокировки (TIMESTAMP, NOT NULL, default: CURRENT_TIMESTAMP)
- `unlock_method`: Метод разблокировки (ENUM: quest, event, purchase, drop)
- `unlock_data`: Данные разблокировки (JSONB, default: {})
- `created_at`: Время создания

**Индексы:**

- По `(character_id, unlocked_at DESC)` для истории разблокировок персонажа
- По `iconic_id` для поиска по культовому предмету
- По `unlock_method` для фильтрации по методу разблокировки

**Constraints:**

- UNIQUE(character_id, iconic_id): Одна разблокировка на персонажа

### equipment_matrix

Таблица матрицы характеристик оборудования. Хранит пулы характеристик и модификаторы для процедурной генерации.

**Ключевые поля:**

- `id`: UUID первичный ключ
- `brand`: Бренд (VARCHAR(50), NOT NULL)
- `category`: Категория оборудования (ENUM, NOT NULL)
- `rarity`: Редкость (ENUM, NOT NULL)
- `stat_pools`: Пулы характеристик для генерации (JSONB, NOT NULL, default: {})
- `modifiers`: Модификаторы характеристик (JSONB, NOT NULL, default: {})
- `version`: Версия матрицы (INTEGER, NOT NULL, default: 1, >= 1)
- `updated_at`: Время последнего обновления
- `created_at`: Время создания

**Индексы:**

- По `(brand, category, rarity, version)` для поиска матрицы
- По `(category, rarity)` для фильтрации по категории и редкости
- По `version DESC` для последних версий

**Constraints:**

- UNIQUE(brand, category, rarity, version): Одна матрица на комбинацию

### generated_equipment

Таблица процедурно сгенерированного оборудования. Хранит информацию о сгенерированных предметах.

**Ключевые поля:**

- `id`: UUID первичный ключ
- `seed`: Seed для процедурной генерации (BIGINT, NOT NULL)
- `brand`: Бренд (VARCHAR(50), NOT NULL)
- `category`: Категория оборудования (ENUM, NOT NULL)
- `rarity`: Редкость (ENUM, NOT NULL)
- `stats`: Сгенерированные характеристики (JSONB, NOT NULL, default: {})
- `generated_at`: Время генерации (TIMESTAMP, NOT NULL, default: CURRENT_TIMESTAMP)
- `character_id`: ID персонажа, получившего предмет (FK к characters, nullable)
- `item_id`: ID предмета в инвентаре (nullable)

**Индексы:**

- По `(seed, brand, category)` для поиска по seed
- По `(brand, category, rarity)` для фильтрации
- По `character_id` для предметов персонажа
- По `generated_at DESC` для последних генераций

**Constraints:**

- UNIQUE(seed, brand, category): Один предмет на комбинацию seed/brand/category

## ENUM типы

### equipment_category

- `weapon`: Оружие
- `armor`: Броня
- `cyberware`: Кибервар
- `consumable`: Расходники

### equipment_rarity

- `common`: Обычное
- `uncommon`: Необычное
- `rare`: Редкое
- `epic`: Эпическое
- `legendary`: Легендарное
- `iconic`: Культовое

### iconic_availability

- `always`: Всегда доступно
- `seasonal`: Сезонное
- `event`: Событийное
- `quest`: Квестовое

### iconic_unlock_method

- `quest`: Через квест
- `event`: Через событие
- `purchase`: Покупка
- `drop`: Дроп

## Constraints и валидация

### CHECK Constraints

- `iconic_equipment_timeline.era_start`: Должно быть >= 2000 и <= 2100
- `iconic_equipment_timeline.era_end`: Должно быть >= 2000 и <= 2100, и >= era_start (если не NULL)
- `equipment_matrix.version`: Должно быть >= 1

### Foreign Keys

- `iconic_equipment_timeline.equipment_id` → `economy.equipment_catalog.id` (ON DELETE CASCADE)
- `iconic_unlocks.character_id` → `mvp_core.character.id` (ON DELETE CASCADE)
- `iconic_unlocks.iconic_id` → `economy.iconic_equipment_timeline.id` (ON DELETE CASCADE)
- `generated_equipment.character_id` → `mvp_core.character(id)` (ON DELETE SET NULL)

### Unique Constraints

- `iconic_equipment_timeline(equipment_id)`: Одна временная линия на предмет
- `iconic_unlocks(character_id, iconic_id)`: Одна разблокировка на персонажа
- `equipment_matrix(brand, category, rarity, version)`: Одна матрица на комбинацию
- `generated_equipment(seed, brand, category)`: Один предмет на комбинацию seed/brand/category

## Оптимизация запросов

### Частые запросы

1. **Получение каталога по категории и редкости:**
   ```sql
   SELECT * FROM economy.equipment_catalog 
   WHERE category = $1 AND rarity = $2 
   ORDER BY brand, name;
   ```
   Использует индекс `(category, brand, rarity)`.

2. **Поиск культового оборудования по эпохе:**
   ```sql
   SELECT * FROM economy.iconic_equipment_timeline 
   WHERE era_start <= $1 AND (era_end IS NULL OR era_end >= $1) 
   AND is_active = true;
   ```
   Использует индекс `(era_start, era_end)`.

3. **Получение разблокировок персонажа:**
   ```sql
   SELECT * FROM economy.iconic_unlocks 
   WHERE character_id = $1 
   ORDER BY unlocked_at DESC;
   ```
   Использует индекс `(character_id, unlocked_at DESC)`.

4. **Получение матрицы для генерации:**
   ```sql
   SELECT * FROM economy.equipment_matrix 
   WHERE brand = $1 AND category = $2 AND rarity = $3 
   ORDER BY version DESC 
   LIMIT 1;
   ```
   Использует индекс `(brand, category, rarity, version)`.

5. **Поиск сгенерированного оборудования по seed:**
   ```sql
   SELECT * FROM economy.generated_equipment 
   WHERE seed = $1 AND brand = $2 AND category = $3;
   ```
   Использует UNIQUE constraint `(seed, brand, category)`.

## Миграции

### Существующие миграции:

- `V1_60__economy_equipment_system_tables.sql` - полная схема системы оборудования

### Применение миграций:

```bash
liquibase update --changelog-file=infrastructure/liquibase/changelog.yaml
```

## Соответствие архитектуре

Схема БД полностью соответствует архитектуре из
`knowledge/implementation/architecture/equipment-system-architecture.yaml`:

- [OK] Все таблицы из архитектуры созданы
- [OK] Все поля соответствуют описанию
- [OK] ENUM типы созданы для категорий, редкости, доступности и методов разблокировки
- [OK] Индексы оптимизированы для частых запросов
- [OK] Constraints обеспечивают целостность данных
- [OK] Foreign Keys настроены с CASCADE для автоматической очистки
- [OK] Интеграция с существующими таблицами (characters, character_items)

## Особенности реализации

### Каталог оборудования

Система каталога включает:

- **Ручной срез**: ключевые предметы, добавленные вручную
- **Категории**: weapon, armor, cyberware, consumable
- **Редкость**: от common до iconic
- **Характеристики**: JSONB для гибкости
- **Подпись**: уникальная характеристика предмета

### Временная линия культового оборудования

Система временной линии включает:

- **Эпохи**: era_start и era_end для временных рамок
- **Условия разблокировки**: JSONB для гибкости
- **Доступность**: always, seasonal, event, quest
- **Дата разблокировки**: для событийных предметов
- **Активность**: флаг is_active для управления

### Разблокировки культового оборудования

Система разблокировок включает:

- **Методы разблокировки**: quest, event, purchase, drop
- **Данные разблокировки**: JSONB для дополнительной информации
- **Время разблокировки**: для истории
- **Уникальность**: одна разблокировка на персонажа

### Матрица характеристик

Система матрицы включает:

- **Пулы характеристик**: JSONB для гибкости
- **Модификаторы**: JSONB для модификаторов
- **Версионирование**: версия матрицы для обновлений
- **Комбинации**: brand, category, rarity, version

### Процедурная генерация

Система генерации включает:

- **Seed**: для воспроизводимости
- **Характеристики**: JSONB для сгенерированных stats
- **Связь с персонажем**: character_id для отслеживания
- **Связь с инвентарем**: item_id для связи с character_items
- **Уникальность**: один предмет на комбинацию seed/brand/category

### Интеграция с другими системами

Система оборудования интегрируется с:

- **Inventory Service**: через character_items и character_equipment
- **Economy Service**: через торговлю и рынки
- **Gameplay Service**: через расчет характеристик
- **Narrative Service**: через разблокировку культового оборудования

