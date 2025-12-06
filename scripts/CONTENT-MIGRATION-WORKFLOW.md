# Content Migration Workflow - Полный Guide

## Issue: #50

Полное описание workflow для работы с контентными миграциями (квесты, NPC, диалоги).

## 📋 Обзор

**Два сценария:**
1. **Первый импорт (массовый)** - генерация SQL миграций → Database → QA
2. **Обновления (одиночные)** - API импорт → QA

## 🔄 Workflow: Первый импорт (массовый)

### Шаг 1: Content Writer → Backend

**Content Writer:**
- Создает/обновляет YAML файлы в `knowledge/canon/`
- Валидирует YAML: `/content-writer-validate-quest-yaml #123`
- Передает в `Backend - Todo` с labels `canon`, `lore`, `quest` (или `npc`, `dialogue`)

**Комментарий:**
```markdown
✅ Content YAML ready. Bulk import needed.
Total files: {count}
Issue: #{number}
```

### Шаг 2: Backend → Database

**Backend:**
1. **Проверка таблиц:**
   - Квесты: `gameplay.quest_definitions` (миграция `V1_46__quest_definitions_tables.sql`)
   - NPC: `narrative.npc_definitions` (миграция `V1_89__narrative_npc_dialogue_tables.sql`)
   - Диалоги: `narrative.dialogue_nodes` (миграция `V1_89__narrative_npc_dialogue_tables.sql`)

2. **Если таблиц нет:**
   - Передать в `Database - Todo` для создания схемных миграций
   - После создания таблиц → вернуться к генерации

3. **Генерация миграций:**
   ```powershell
   # Windows
   .\scripts\generate-content-migrations.ps1
   
   # Linux/Mac
   ./scripts/generate-content-migrations.sh
   ```

4. **Что создается:**
   - **Формат:** 1 файл YAML = 1 миграция (с версией из `metadata.version`)
   - **Квесты:** `infrastructure/liquibase/migrations/data/quests/V*__data_quest_..._v1_0_0.sql`
   - **NPC:** `infrastructure/liquibase/migrations/data/npcs/V*__data_npc_..._v1_0_0.sql`
   - **Диалоги:** `infrastructure/liquibase/migrations/data/dialogues/V*__data_dialogue_..._v1_0_0.sql`

5. **Генерация changelog:**
   ```powershell
   # Windows
   .\scripts\db\generate-content-changelog.ps1
   
   # Linux/Mac
   ./scripts/db/generate-content-changelog.sh
   ```
   - Создает `infrastructure/liquibase/changelog-content.yaml`
   - Автоматически включает все контентные миграции

6. **Проверка changelog:**
   - Убедиться что `changelog-content.yaml` включен в `changelog.yaml`:
   ```yaml
   - include:
       file: changelog-content.yaml
   ```

7. **Передача в Database:**
   - Обновить статус: `Database - Todo`
   - Комментарий:
   ```markdown
   ✅ Content migrations generated. Ready for Database.
   - Quests: {count} migrations
   - NPCs: {count} migrations
   - Dialogues: {count} migrations
   - Changelog: changelog-content.yaml generated
   Issue: #{number}
   ```

### Шаг 3: Database → QA

**Database:**
1. **Проверка порядка миграций:**
   - Схемные миграции должны быть применены ПЕРЕД контентными
   - Порядок в `changelog.yaml`:
     ```yaml
     - include: migrations/V1_46__quest_definitions_tables.sql  # Сначала схемы
     - include: migrations/V1_89__narrative_npc_dialogue_tables.sql
     - include: changelog-content.yaml  # Потом контент
     ```

2. **Валидация миграций:**
   ```powershell
   # Проверка всех миграций
   python scripts/validate-all-migrations.py
   ```

3. **Применение миграций:**
   ```powershell
   # Windows - прямой SQL
   .\scripts\db\apply-migrations-direct.ps1
   
   # Или через Liquibase
   liquibase update
   ```

4. **Проверка импорта:**
   ```sql
   -- Квесты
   SELECT COUNT(*) FROM gameplay.quest_definitions;
   
   -- NPC
   SELECT COUNT(*) FROM narrative.npc_definitions;
   
   -- Диалоги
   SELECT COUNT(*) FROM narrative.dialogue_nodes;
   ```

5. **Передача в QA:**
   - Обновить статус: `QA - Todo`
   - Комментарий:
   ```markdown
   ✅ Content migrations applied successfully.
   - Quests: {count} imported
   - NPCs: {count} imported
   - Dialogues: {count} imported
   Issue: #{number}
   ```

### Шаг 4: QA → Release

**QA:**
- Тестирует импортированный контент
- Проверяет доступность через API
- Передает в `Release - Todo` или закрывает Issue

## 🔄 Workflow: Обновления (одиночные)

### Когда использовать API вместо миграций:
- ✅ Обновление одного квеста/NPC/диалога
- ✅ Изменение версии в YAML (`metadata.version`)
- ✅ После первого массового импорта

### Шаг 1: Content Writer → Backend

**Content Writer:**
- Обновляет YAML файл (может изменить `metadata.version`)
- Валидирует YAML
- Передает в `Backend - Todo`

### Шаг 2: Backend → QA

**Backend:**
1. **Импорт через API:**
   ```powershell
   # Windows
   .\scripts\import-quest.ps1 -QuestFile "path/to/quest.yaml"
   
   # Linux/Mac
   ./scripts/import-quest.sh "path/to/quest.yaml"
   ```

2. **Или batch API (если реализован):**
   ```bash
   POST /api/v1/gameplay/quests/content/batch-reload
   ```

3. **Проверка:**
   - Квест/NPC/диалог обновлен в БД
   - Версия обновлена (если изменилась)

4. **Передача в QA:**
   - Обновить статус: `QA - Todo`
   - Комментарий:
   ```markdown
   ✅ Content updated via API. Ready for QA.
   Quest ID: {quest_id}
   Version: {version}
   Issue: #{number}
   ```

## 📝 Версионирование

### Формат версии в YAML:
```yaml
metadata:
  version: "1.0.0"  # Semantic versioning
```

### Как работает версионирование:

1. **В миграциях:**
   - Версия из `metadata.version` добавляется в имя файла: `V*__data_quest_..._v1_0_0.sql`
   - Если версия изменилась → создается новая миграция с новой версией

2. **В БД:**
   - Поле `version` в таблице обновляется через `ON CONFLICT DO UPDATE`
   - Можно отслеживать историю версий

3. **Обновления:**
   - Если изменили версию в YAML → новая миграция или API обновление
   - Liquibase применяет миграции по порядку (по номеру V*)

## ⚠️ Важные моменты

### Порядок применения миграций:
1. **Сначала схемные миграции:**
   - `V1_46__quest_definitions_tables.sql`
   - `V1_89__narrative_npc_dialogue_tables.sql`
   - Другие схемные миграции

2. **Потом контентные миграции:**
   - `changelog-content.yaml` (включает все контентные миграции)

### Валидация перед применением:
- ✅ Таблицы существуют
- ✅ Миграции валидны (BEGIN/COMMIT, Issue references)
- ✅ JSONB валиден
- ✅ Нет конфликтов версий

### Откат миграций:
```bash
# Откат конкретной миграции
liquibase rollback-count 1

# Или прямой SQL (если нужно)
# Удалить данные из таблицы
DELETE FROM gameplay.quest_definitions WHERE quest_id = 'quest-001';
```

### CI/CD интеграция:
- Генерация миграций в CI/CD pipeline
- Автоматическое применение через Liquibase
- Валидация перед применением
- Rollback при ошибках

## 🔍 Troubleshooting

### Ошибка: "Table does not exist"
- **Решение:** Применить схемные миграции сначала

### Ошибка: "Migration already applied"
- **Решение:** Liquibase отслеживает примененные миграции. Это нормально.

### Ошибка: "Version conflict"
- **Решение:** Проверить что версия в YAML изменилась, если нужна новая миграция

### Ошибка: "Invalid JSONB"
- **Решение:** Проверить валидность YAML, особенно datetime объекты

## 📚 Связанные документы

- `scripts/README-CONTENT-MIGRATIONS.md` - детали генератора
- `.cursor/rules/agent-backend.mdc` - правила Backend агента
- `.cursor/rules/agent-database.mdc` - правила Database агента
- `.cursor/rules/agent-content-writer.mdc` - правила Content Writer агента

