# Content Migrations Guide

## Issue: #50

Генератор SQL миграций из YAML контентных файлов для массового импорта квестов, лора, предметов и т.д.

## WARNING Важно: Формат YAML не изменяется!

**Генератор только ЧИТАЕТ YAML файлы, не изменяя их!**
- Все YAML файлы остаются в исходном формате
- Генератор создает SQL миграции на основе существующих данных
- Можно безопасно запускать многократно

## Использование

### Генерация миграций

**Windows:**
```powershell
.\scripts\generate-content-migrations.ps1
```

**Linux/Mac:**
```bash
./scripts/generate-content-migrations.sh
```

### Что делает генератор

1. Сканирует контентные YAML файлы:
   - Квесты: `knowledge/canon/lore/timeline-author/quests/` → `quest-*.yaml`
   - NPC: `knowledge/canon/narrative/npc-lore/` → `*.yaml`
   - Диалоги: `knowledge/canon/narrative/dialogues/` → `*.yaml`

2. **Формат:** 1 файл YAML = 1 миграция (с версией из `metadata.version`):
   - `quest-001-willis-tower.yaml` (v1.0.0) → `V*__data_quest_..._v1_0_0.sql`
   - `anna-petrova.yaml` (v1.0.0) → `V*__data_npc_..._v1_0_0.sql`
   - `faction-social-lines.yaml` (v1.0.0) → `V*__data_dialogue_..._v1_0_0.sql`

3. Парсит YAML и извлекает данные:
   - `metadata.id` → `quest_id` / `npc_id` / `dialogue_id`
   - `metadata.title` → `title`
   - `metadata.version` → версия в имени файла миграции
   - **Весь YAML** → `content_data` (JSONB) - сохраняется полностью!

4. Генерирует SQL миграции:
   - Квесты: `infrastructure/liquibase/migrations/data/quests/V*__data_quest_*.sql`
   - NPC: `infrastructure/liquibase/migrations/data/npcs/V*__data_npc_*.sql`
   - Диалоги: `infrastructure/liquibase/migrations/data/dialogues/V*__data_dialogue_*.sql`

5. Использует `ON CONFLICT DO UPDATE` для безопасного обновления

**Пример:** 712 квестов → 712 миграций (1 файл = 1 миграция)

### Обработка разных форматов квестов

Генератор поддерживает:
- OK Квесты **с** `quest_definition` (полная структура)
- OK Квесты **без** `quest_definition` (используются дефолты: `quest_type='side'`, пустые массивы)
- OK Разные форматы `quest_id` (`quest-001-willis-tower`, `canon-quest-seoul-gangnam-style`)
- OK Datetime объекты в YAML (автоматически конвертируются в ISO format)

### Применение миграции

**Через Liquibase:**
```bash
liquibase update
```

**Прямой SQL:**
```bash
# Применить все миграции квестов
psql -U postgres -d necpgame -f infrastructure/liquibase/migrations/V*__content_quests_*.sql

# Или конкретную миграцию
psql -U postgres -d necpgame -f infrastructure/liquibase/migrations/V*__content_quests_america_chicago_2020_2029.sql
```

### Проверка импорта

```sql
-- Количество импортированных квестов
SELECT COUNT(*) FROM gameplay.quest_definitions;

-- Примеры квестов
SELECT quest_id, title, quest_type, level_min, level_max 
FROM gameplay.quest_definitions 
LIMIT 10;

-- Проверка конкретного квеста
SELECT quest_id, title, content_data->'metadata'->>'id' as yaml_id
FROM gameplay.quest_definitions 
WHERE quest_id = 'quest-001-willis-tower';

-- Проверка что content_data сохранен полностью
SELECT quest_id, jsonb_pretty(content_data) 
FROM gameplay.quest_definitions 
WHERE quest_id = 'quest-001-willis-tower';
```

## Workflow

**Полный workflow:** См. `scripts/CONTENT-MIGRATION-WORKFLOW.md`

### Первый импорт (массовый)

1. **Content Writer** создает контент в YAML
2. **Backend** запускает генератор: `generate-content-migrations.sh`
3. **Backend** генерирует changelog: `generate-content-changelog.ps1`
4. **Database** применяет миграции (сначала схемные, потом контентные)
5. **QA** тестирует импортированный контент

### Обновления (одиночные)

1. **Content Writer** обновляет YAML файл
2. **Backend** импортирует через API: `import-quest.sh <file>`
3. **QA** тестирует обновленный контент

**Важно:** После первого массового импорта используйте API для обновлений, а не миграции.

## Структура миграции

Каждая миграция соответствует одному YAML файлу:

```sql
-- Issue: #50
-- Import quest from: america/chicago/2020-2029/quest-001-willis-tower.yaml
-- Generated: 2025-12-06T...
-- Version: 1.0.0

BEGIN;

-- Quest: quest-001-willis-tower
INSERT INTO gameplay.quest_definitions (
    quest_id, title, description, quest_type, level_min, level_max,
    requirements, objectives, rewards, branches, content_data
)
VALUES (
    'quest-001-willis-tower',
    'Willis Tower',
    '...',
    'side',
    1,
    50,
    '{}'::jsonb,
    '[]'::jsonb,
    '[]'::jsonb,
    '[]'::jsonb,
    '{...}'::jsonb
)
ON CONFLICT (quest_id) DO UPDATE SET
    title = EXCLUDED.title,
    description = EXCLUDED.description,
    content_data = EXCLUDED.content_data,
    version = EXCLUDED.version,
    updated_at = CURRENT_TIMESTAMP;

COMMIT;
```

**Преимущества формата 1 файл = 1 миграция:**
- OK Легко отслеживать изменения конкретного квеста/NPC/диалога
- OK Проще откатывать отдельные элементы
- OK Версионирование на уровне файла
- OK Понятная структура (имя файла отражает путь и версию)

## Поддержка типов контента

Генератор поддерживает:
- OK **Quests**: `knowledge/canon/lore/timeline-author/quests/` → `gameplay.quest_definitions`
- OK **NPCs**: `knowledge/canon/narrative/npc-lore/` → `narrative.npc_definitions`
- OK **Dialogues**: `knowledge/canon/narrative/dialogues/` → `narrative.dialogue_nodes`

**Таблицы созданы:**
- OK `gameplay.quest_definitions` (миграция `V1_46__quest_definitions_tables.sql`)
- OK `narrative.npc_definitions` (миграция `V1_89__narrative_npc_dialogue_tables.sql`)
- OK `narrative.dialogue_nodes` (миграция `V1_89__narrative_npc_dialogue_tables.sql`)

**Будущие типы:**
- Items: `knowledge/canon/items/` → `mvp_core.item_templates`
- Lore: `knowledge/canon/lore/` → `lore_entries` (если нужна таблица)

## Troubleshooting

### Ошибка: "Python is not installed"
Установите Python 3.x и добавьте в PATH.

### Ошибка: "No quest files found"
Проверьте путь: `knowledge/canon/lore/timeline-author/quests/`

### Ошибка: "Failed to parse YAML"
Проверьте синтаксис YAML файлов:
```bash
yamllint knowledge/canon/lore/.../quest-*.yaml
```

### Ошибка: "Object of type datetime is not JSON serializable"
OK **Исправлено!** Генератор автоматически конвертирует datetime в ISO format.

### Ошибка при применении миграции
- Проверьте что таблица `gameplay.quest_definitions` существует
- Проверьте права доступа к БД
- Проверьте синтаксис SQL

## Связанные скрипты

- `scripts/generate-content-migrations.sh` / `.ps1` - генерация миграций
- `scripts/db/generate-content-changelog.ps1` - генерация changelog для контентных миграций
- `scripts/db/apply-migrations-direct.ps1` - применение миграций напрямую
- `scripts/validate-all-migrations.py` - валидация всех миграций
- `scripts/import-quest.sh` / `import-quest.ps1` - импорт одного квеста через API
- `scripts/import-quests-batch.sh` / `import-quests-batch.ps1` - батч импорт через API

## Полный Workflow

**Детальное описание:** `scripts/CONTENT-MIGRATION-WORKFLOW.md`

## Гарантии

OK **YAML файлы не изменяются** - генератор только читает  
OK **Все данные сохраняются** - полный YAML в `content_data`  
OK **Обратная совместимость** - работает с квестами без `quest_definition`  
OK **Безопасные обновления** - `ON CONFLICT DO UPDATE` не удаляет данные
