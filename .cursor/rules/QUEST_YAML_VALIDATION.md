# Quest YAML Validation Template

## Обязательные поля для импорта в БД

YAML файл квеста должен содержать следующие обязательные поля для успешного импорта в таблицу `quest_definitions`:

### 1. Metadata (обязательно)
```yaml
metadata:
  id: quest-{location}-{year}-{name}  # Уникальный идентификатор квеста
  title: "Название квеста"  # VARCHAR(255) - максимум 255 символов
  version: "1.0.0"  # Версия квеста
```

### 2. Quest Definition (обязательно)
```yaml
quest_definition:
  quest_type: main|side|daily|weekly|faction|guild|dynamic|event|social|player_created  # ОБЯЗАТЕЛЬНО
  level_min: INTEGER  # ОБЯЗАТЕЛЬНО, минимум 1
  level_max: INTEGER|null  # Может быть null для квестов без ограничения уровня
  
  requirements:  # JSONB - ОБЯЗАТЕЛЬНО (может быть пустым объектом)
    required_quests: []  # Список ID квестов-предшественников
    required_flags: []  # Флаги, необходимые для старта
    required_reputation: {}  # Репутация с фракциями
    required_items: []  # Предметы, необходимые для старта
  
  objectives:  # JSONB - ОБЯЗАТЕЛЬНО, минимум 1 цель
    - id: unique_objective_id  # ОБЯЗАТЕЛЬНО
      text: "Описание цели"  # ОБЯЗАТЕЛЬНО
      type: interact|explore|dialogue|collect|complete|raid|choice|visit  # ОБЯЗАТЕЛЬНО
      target: target_identifier  # ОБЯЗАТЕЛЬНО
      count: INTEGER  # Опционально, по умолчанию 1
      optional: false|true  # ОБЯЗАТЕЛЬНО
  
  rewards:  # JSONB - ОБЯЗАТЕЛЬНО (может быть пустым объектом)
    experience: INTEGER  # Опционально
    currency: INTEGER  # Опционально (может быть отрицательным)
    money: INTEGER  # Опционально (может быть отрицательным)
    items: []  # Опционально
    reputation: {}  # Опционально
    attributes: {}  # Опционально
    unlocks: []  # Опционально
  
  branches: []  # JSONB - ОБЯЗАТЕЛЬНО (может быть пустым массивом)
    # Если есть ветвления:
    - branch_id: unique_branch_id
      condition: условие_ветвления
      objectives: []  # Дополнительные цели для ветки
      rewards: {}  # Дополнительные награды для ветки
```

## Правила валидации

### Критические ошибки (требуют возврата Content Writer)
1. **Отсутствие обязательных полей:**
   - `metadata.id` отсутствует или пустой
   - `metadata.title` отсутствует или пустой
   - `quest_definition.quest_type` отсутствует или не из допустимых значений
   - `quest_definition.level_min` отсутствует или < 1
   - `quest_definition.objectives` отсутствует или пустой массив
   - `quest_definition.requirements` отсутствует
   - `quest_definition.rewards` отсутствует
   - `quest_definition.branches` отсутствует

2. **Неправильные типы данных:**
   - `quest_type` не из списка: main, side, daily, weekly, faction, guild, dynamic, event, social, player_created
   - `level_min` не INTEGER или < 1
   - `level_max` не INTEGER, не null и < level_min
   - `title` длиннее 255 символов

3. **Неправильная структура objectives:**
   - Objective без `id`
   - Objective без `text`
   - Objective без `type` или `type` не из допустимых значений
   - Objective без `target`
   - Objective без `optional` (должно быть явно false или true)

4. **Неправильная структура requirements:**
   - Не объект (не массив, не строка, не число)

5. **Неправильная структура rewards:**
   - Не объект (не массив, не строка, не число)

6. **Неправильная структура branches:**
   - Не массив (не объект, не строка, не число)

### Предупреждения (не блокируют импорт, но должны быть исправлены)
1. `level_max` null для квестов с level_min > 1 (рекомендуется указать)
2. Пустой массив `objectives` (должен быть минимум 1)
3. Objective с `optional: true` без альтернативных путей
4. Пустой объект `rewards` (нет наград)
5. `requirements` содержит несуществующие `required_quests`

## Маппинг YAML → БД

```yaml
# YAML структура
metadata:
  id: quest-vegas-2029-strip
  title: "Лас-Вегас 2020-2029 — Прогулка по Стрипу"
  version: "1.1.0"

quest_definition:
  quest_type: side
  level_min: 1
  level_max: null
  requirements: {...}
  objectives: [...]
  rewards: {...}
  branches: [...]
```

```sql
-- БД структура (quest_definitions)
INSERT INTO gameplay.quest_definitions (
  id,                    -- UUID (генерируется из metadata.id)
  quest_type,            -- ENUM из quest_definition.quest_type
  title,                 -- VARCHAR(255) из metadata.title
  description,           -- TEXT из summary.essence (если есть)
  level_min,             -- INTEGER из quest_definition.level_min
  level_max,             -- INTEGER из quest_definition.level_max (NULL если null)
  requirements,          -- JSONB из quest_definition.requirements
  objectives,            -- JSONB из quest_definition.objectives
  rewards,               -- JSONB из quest_definition.rewards
  branches,              -- JSONB из quest_definition.branches
  dialogue_id,          -- UUID (null, если нет диалога)
  version,              -- INTEGER из metadata.version (парсится)
  created_at,           -- TIMESTAMP (текущее время)
  updated_at            -- TIMESTAMP (текущее время)
) VALUES (...);
```

## Пример валидного YAML

```yaml
metadata:
  id: quest-vegas-2029-strip
  title: "Лас-Вегас 2020-2029 — Прогулка по Стрипу"
  version: "1.1.0"

quest_definition:
  quest_type: side
  level_min: 1
  level_max: null
  requirements:
    required_quests: []
    required_flags: []
    required_reputation: {}
    required_items: []
  objectives:
    - id: photo_welcome_sign
      text: "Сделать фото у знака «Welcome to Fabulous Las Vegas»"
      type: interact
      target: welcome_sign
      optional: false
    - id: visit_bellagio_fountains
      text: "Посетить Bellagio и наблюдать фонтанное шоу"
      type: interact
      target: bellagio_fountains
      optional: false
  rewards:
    experience: 1500
    currency: 0
    reputation:
      las_vegas: 10
  branches: []
```

## Процесс валидации

1. **Content Writer** создает YAML файл
2. **Backend Developer** валидирует YAML перед импортом:
   - Проверяет наличие всех обязательных полей
   - Проверяет типы данных
   - Проверяет структуру
3. **При критических ошибках:**
   - Backend Developer возвращает задачу Content Writer с описанием ошибок
4. **При успешной валидации:**
   - Backend Developer импортирует квест в БД через API endpoint
   - Передает задачу QA для тестирования

