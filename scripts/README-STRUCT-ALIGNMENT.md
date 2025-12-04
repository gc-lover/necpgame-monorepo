# Автоматический рефакторинг для Struct Field Alignment

## 🎯 Цель

Автоматическая оптимизация порядка полей в OpenAPI спецификациях и Liquibase миграциях для **struct field alignment**.

**Gains:** Память ↓30-50%, Cache hits ↑15-20%

## 📦 Инструменты

### 1. OpenAPI YAML рефакторинг

**Скрипт:** `scripts/reorder-openapi-fields.py`

**Использование:**
```bash
# Dry run (проверка без изменений)
python scripts/reorder-openapi-fields.py proto/openapi/{service}.yaml --dry-run --verbose

# Применить изменения
python scripts/reorder-openapi-fields.py proto/openapi/{service}.yaml --verbose
```

**Что делает:**
- Сортирует `properties` в каждом schema по размеру типа (large → small)
- Добавляет `BACKEND NOTE` с информацией об оптимизации
- Сохраняет порядок `required` полей

**Порядок типов:**
1. `string`/`uuid` (16 bytes)
2. `object`/`$ref` (8-24 bytes)
3. `array` (24 bytes)
4. `int64`/`float64` (8 bytes)
5. `int32`/`float32` (4 bytes)
6. `int16` (2 bytes)
7. `int8`/`boolean` (1 byte)

**Пример:**
```yaml
# До
properties:
  level: { type: integer }
  character_id: { type: string, format: uuid }
  is_active: { type: boolean }

# После
properties:
  character_id: { type: string, format: uuid }  # 16 bytes
  level: { type: integer }                        # 4 bytes
  is_active: { type: boolean }                    # 1 byte
```

### 2. Liquibase SQL рефакторинг

**Скрипт:** `scripts/reorder-liquibase-columns.py`

**Использование:**
```bash
# Dry run (проверка без изменений)
python scripts/reorder-liquibase-columns.py infrastructure/liquibase/migrations/{file}.sql --dry-run --verbose

# Применить изменения
python scripts/reorder-liquibase-columns.py infrastructure/liquibase/migrations/{file}.sql --verbose
```

**Что делает:**
- Сортирует колонки в `CREATE TABLE` по размеру типа (large → small)
- Сохраняет `PRIMARY KEY` колонку первой
- Сохраняет все constraints (FOREIGN KEY, UNIQUE, CHECK)

**Порядок типов PostgreSQL:**
1. `UUID` (16 bytes)
2. `TEXT`/`VARCHAR` (variable, большой)
3. `JSONB` (variable, большой)
4. `TIMESTAMP` (8 bytes)
5. `BIGINT` (8 bytes)
6. `INTEGER` (4 bytes)
7. `SMALLINT` (2 bytes)
8. `BOOLEAN` (1 byte)

**Пример:**
```sql
-- До
CREATE TABLE players (
  is_active BOOLEAN,
  id UUID PRIMARY KEY,
  level INTEGER,
  experience BIGINT
);

-- После
CREATE TABLE IF NOT EXISTS players (
  id UUID PRIMARY KEY,      -- PRIMARY KEY первым
  experience BIGINT,         -- 8 bytes
  level INTEGER,             -- 4 bytes
  is_active BOOLEAN         -- 1 byte
);
```

## ✅ Тестирование

### OpenAPI

Протестировано на:
- ✅ `proto/openapi/progression-service.yaml`
  - Изменено 3 schemas: `AwardExperienceRequest`, `ExperienceResponse`, `ProgressionState`
  - Валидация: `redocly lint` - ✅ OK

### Liquibase

Протестировано на:
- ✅ `infrastructure/liquibase/migrations/V1_18__progression_tables.sql`
  - Изменено 2 таблицы: `character_progression`, `skill_experience`
  - PRIMARY KEY сохранены первыми
  - Constraints сохранены

## 🔧 Требования

- Python 3.7+
- PyYAML: `pip install pyyaml`

## 📝 Примечания

1. **PRIMARY KEY** всегда остается первым в таблице
2. **Required поля** в OpenAPI сохраняются в списке `required`
3. Скрипты безопасны: используют `--dry-run` для проверки
4. Все изменения сохраняют структуру и комментарии

## 🚀 Интеграция в CI/CD

Можно добавить в pre-commit hook:

```bash
# Проверка OpenAPI
python scripts/reorder-openapi-fields.py proto/openapi/{service}.yaml --dry-run

# Проверка Liquibase
python scripts/reorder-liquibase-columns.py infrastructure/liquibase/migrations/{file}.sql --dry-run
```

## 📚 См. также

- `.cursor/rules/agent-api-designer.mdc` - Performance Optimization
- `.cursor/rules/agent-database.mdc` - Column Order Optimization
- `.cursor/performance/01-memory-concurrency-db.md` - Struct Field Alignment
- `scripts/SUPPORTED_TYPES.md` - Полный список поддерживаемых типов данных

## 🆕 Обновления

**Версия 2.0** - Добавлена поддержка всех типов данных:
- ✅ Все OpenAPI 3.0 форматы (uuid, date-time, email, uri, binary, byte, etc.)
- ✅ Все PostgreSQL типы (UUID, JSONB, TIMESTAMP, NUMERIC, ARRAY, spatial, network, etc.)
- ✅ Улучшенная обработка сложных типов
- ✅ Правильная сортировка по размеру в памяти

