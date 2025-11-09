# SQL Migrations for Quest System

**Версия:** 1.0.0  
**Дата:** 2025-11-07 00:28

---

## Краткое описание

Полный набор SQL миграций для развертывания системы квестового ветвления и world state в production.

---

## Список миграций

1. **001-expand-quests-table.sql** - Расширение базовой таблицы quests
2. **002-create-quest-branches.sql** - Создание таблицы ветвей квестов
3. **003-create-dialogue-system.sql** - Создание dialogue nodes и choices
4. **004-create-player-systems.sql** - Создание player choices, flags, objectives
5. **005-create-world-state-system.sql** - Создание world state (3 уровня)

**ИТОГО:** 5 миграций, 24+ таблицы

---

## Как применить

### Linux/Mac

```bash
# Установить переменные окружения
export DB_NAME=necpgame
export DB_USER=postgres
export DB_PASSWORD=your_password
export DB_HOST=localhost
export DB_PORT=5432

# Применить все миграции
chmod +x apply-all-migrations.sh
./apply-all-migrations.sh
```

### Windows

```powershell
# Установить переменные окружения
$env:DB_NAME = "necpgame"
$env:DB_USER = "postgres"
$env:DB_PASSWORD = "your_password"
$env:DB_HOST = "localhost"
$env:DB_PORT = "5432"

# Применить все миграции
.\apply-all-migrations.ps1
```

### Вручную (каждая миграция отдельно)

```bash
psql -d necpgame -U postgres -f 001-expand-quests-table.sql
psql -d necpgame -U postgres -f 002-create-quest-branches.sql
psql -d necpgame -U postgres -f 003-create-dialogue-system.sql
psql -d necpgame -U postgres -f 004-create-player-systems.sql
psql -d necpgame -U postgres -f 005-create-world-state-system.sql
```

---

## Rollback

Если нужно откатить изменения, используйте rollback скрипты в обратном порядке:

```bash
# В обратном порядке!
psql -d necpgame -f rollback/005-rollback-world-state.sql
psql -d necpgame -f rollback/004-rollback-player-systems.sql
psql -d necpgame -f rollback/003-rollback-dialogue.sql
psql -d necpgame -f rollback/002-rollback-branches.sql
psql -d necpgame -f rollback/001-rollback-expand-quests.sql
```

---

## Проверка

После применения миграций:

```sql
-- Проверить созданные таблицы
\dt quest*
\dt player*
\dt server*
\dt dialogue*
\dt territory*

-- Проверить индексы
\di quest*

-- Проверить sample data
SELECT * FROM quest_branches LIMIT 5;
SELECT * FROM server_world_state;
SELECT * FROM territory_control;
```

---

## Что создаётся

**Quest System (4 таблицы):**
- quests (расширенная)
- quest_branches
- dialogue_nodes
- dialogue_choices

**Player Tracking (3 таблицы):**
- player_quest_choices
- player_flags
- player_dialogue_progress

**Quest Objectives:**
- quest_objectives

**World State (5 таблиц):**
- player_world_state
- server_world_state
- world_state_votes
- faction_world_state
- territory_control

**ИТОГО: 13 таблиц + helper functions**

---

## История изменений

- v1.0.0 (2025-11-07 00:28) - Полный набор миграций создан

