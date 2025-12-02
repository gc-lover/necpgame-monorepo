# /database-refactor-schema - Рефакторинг database schema

**Аудит и оптимизация существующих таблиц для performance**

---

## Описание

Команда анализирует существующие таблицы БД и создает план оптимизации (column order, indexes, partitioning).

---

## Синтаксис

```
/database-refactor-schema {table-name}
```

**Параметры:**
- `{table-name}` - имя таблицы (например: `players`, `inventory`)

---

## Примеры

```
/database-refactor-schema players
/database-refactor-schema combat_logs
/database-refactor-schema guild_members
```

---

## Алгоритм

### 1. Анализ текущей схемы

```sql
-- Получить текущую структуру
\d+ players

-- Проверить размер
SELECT 
    pg_size_pretty(pg_total_relation_size('players')) as total_size,
    pg_size_pretty(pg_relation_size('players')) as table_size,
    pg_size_pretty(pg_indexes_size('players')) as indexes_size;

-- Проверить индексы
\di+ players*

-- Slow queries
SELECT query, mean_exec_time, calls
FROM pg_stat_statements
WHERE query LIKE '%players%'
ORDER BY mean_exec_time DESC
LIMIT 10;
```

### 2. Создание оптимизационного плана

**Создать:** `infrastructure/liquibase/refactor/players-optimization-plan.md`

```markdown
# Optimization Plan: players table

## Current State

```sql
CREATE TABLE players (
    id BIGINT PRIMARY KEY,
    name VARCHAR(255),
    is_active BOOLEAN,      -- ❌ Bad order (padding!)
    level INTEGER,
    health INTEGER,
    position_x REAL,
    position_y REAL,
    position_z REAL,
    created_at TIMESTAMP
);

-- Indexes
CREATE INDEX idx_players_level ON players(level);
```

**Issues:**
- ❌ Column order not optimized (padding waste)
- ❌ Missing covering index for hot query
- ❌ No partial index (only active players)
- WARNING Table size: 500MB (1M rows)
- WARNING No partitioning (single large table)

## Optimized Schema

```sql
-- Optimized column order (large → small)
CREATE TABLE players_new (
    id BIGINT PRIMARY KEY,              -- 8 bytes
    position_x REAL,                    -- 4 bytes
    position_y REAL,                    -- 4 bytes
    position_z REAL,                    -- 4 bytes
    health INTEGER,                     -- 4 bytes
    level INTEGER,                      -- 4 bytes
    created_at TIMESTAMP,               -- 8 bytes
    is_active BOOLEAN,                  -- 1 byte (last!)
    name VARCHAR(255)                   -- Variable
);

-- Covering index (no table lookup)
CREATE INDEX idx_players_level_covering 
ON players_new(level, is_active, id, health)
WHERE is_active = true;

-- Partial index (only active)
CREATE INDEX idx_active_players_position
ON players_new USING GIST (point(position_x, position_y))
WHERE is_active = true;
```

## Migration Strategy

**Step 1: Create new table**
```sql
-- With optimized structure
CREATE TABLE players_new (...);
```

**Step 2: Copy data**
```sql
-- Copy existing data
INSERT INTO players_new 
SELECT * FROM players;
```

**Step 3: Swap tables**
```sql
-- Atomic swap
BEGIN;
ALTER TABLE players RENAME TO players_old;
ALTER TABLE players_new RENAME TO players;
COMMIT;
```

**Step 4: Cleanup**
```sql
-- After verification
DROP TABLE players_old;
```

## Expected Gains

**Memory:**
- Row size: 48 bytes → 40 bytes (-17%)
- For 1M players: 48MB → 40MB

**Query Performance:**
- Hot query (level + active): 50ms → 5ms (-90%)
- Covering index: No table lookups
- Partial index: 60% smaller (only active players)

## Validation

```sql
-- Check row size
SELECT pg_column_size(ROW(p.*)) FROM players p LIMIT 1;

-- Check query plans
EXPLAIN ANALYZE
SELECT id, health FROM players
WHERE level = 10 AND is_active = true;
```
```

### 3. Создание Liquibase миграции

```sql
-- Issue: #{issue_number}
-- liquibase formatted sql
-- changeset: database:optimize-players-table

-- Step 1: Create optimized table
CREATE TABLE players_new (
    id BIGINT PRIMARY KEY,
    position_x REAL NOT NULL,
    position_y REAL NOT NULL,
    position_z REAL NOT NULL,
    health INTEGER NOT NULL DEFAULT 100,
    level INTEGER NOT NULL DEFAULT 1,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    is_active BOOLEAN NOT NULL DEFAULT true,
    name VARCHAR(255) NOT NULL
);

-- Step 2: Create optimized indexes
CREATE INDEX CONCURRENTLY idx_players_level_covering 
ON players_new(level, is_active, id, health)
WHERE is_active = true;

CREATE INDEX CONCURRENTLY idx_active_players_position
ON players_new USING GIST (point(position_x, position_y))
WHERE is_active = true;

-- Step 3: Copy data
INSERT INTO players_new 
SELECT id, position_x, position_y, position_z, health, level, created_at, is_active, name
FROM players;

-- Step 4: Swap tables (in separate changeset for safety)
-- changeset: database:swap-players-table

BEGIN;
ALTER TABLE players RENAME TO players_old;
ALTER TABLE players_new RENAME TO players;
COMMIT;

-- Step 5: Cleanup (manual, after verification)
-- DROP TABLE players_old;
```

### 4. Создание Issue

```javascript
mcp_github_issue_write({
  method: 'create',
  owner: 'gc-lover',
  repo: 'necpgame-monorepo',
  title: '[REFACTOR] Optimize {table-name} table schema',
  body: `## Goal

Optimize {table-name} table for Performance Bible compliance.

## Current Issues

- ❌ Column order not optimized (padding waste)
- ❌ Missing covering indexes
- ❌ No partial indexes
- WARNING Table size: {size}

## Expected Gains

- Memory: -{X}% per row
- Query speed: -{Y}% for hot queries
- Index size: -{Z}%

## Migration Plan

See: infrastructure/liquibase/refactor/{table-name}-optimization-plan.md

## Validation

- [ ] Row size optimized
- [ ] Query plans use covering indexes
- [ ] All queries <10ms P95
- [ ] No regressions in tests
`,
  labels: ['refactor', 'database', 'performance']
});
```

---

## Output

Команда создает:
1. OK Рефакторинг план в `infrastructure/liquibase/refactor/`
2. OK Liquibase migration для оптимизации
3. OK GitHub Issue с планом
4. OK Список expected gains

---

## Когда использовать

**Используй когда:**
- Работаешь с существующей таблицей
- Нашел неоптимизированную schema
- Profiling показал DB bottleneck
- Таблица растет и тормозит

**Проверь перед рефакторингом:**
- Таблица активно используется
- Downtime недопустим (используй online migration)
- Есть бэкапы

---

## Безопасность

**ВАЖНО при рефакторинге production tables:**

1. **Создавай новую таблицу** (не ALTER существующую)
2. **Копируй данные** с минимальным downtime
3. **Atomic swap** через RENAME
4. **Держи старую таблицу** до полной верификации
5. **Rollback plan** готов

---

## См. также

- `.cursor/GO_BACKEND_PERFORMANCE_BIBLE.md` - Part 5A, 7A (Database optimizations)
- `/database-validate-result` - валидация после рефакторинга

