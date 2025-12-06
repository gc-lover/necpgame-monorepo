# Bulk Quest Import Workflow

Workflow для массового импорта квестов при первом запуске.

## Когда использовать

- Первый импорт всех квестов (>10 квестов)
- Массовое обновление структуры квестов
- Миграция на новую версию схемы БД

## Workflow

### 1. Content Writer → Backend

**Content Writer:**
- Создает/обновляет квесты в YAML
- Валидирует YAML: `/content-writer-validate-quest-yaml #123`
- Передает в `Backend - Todo` с labels `canon`, `lore`, `quest`

**Комментарий:**
```markdown
OK Quest YAML ready. Bulk import needed.
Total quests: {count}
Issue: #{number}
```

### 2. Backend → Database

**Backend:**
- Запускает генератор: `scripts/generate-content-migrations.sh` (или `.ps1`)
- Проверяет сгенерированную миграцию
- Передает в `Database - Todo`

**Комментарий:**
```markdown
OK SQL migration generated for {count} quests.
Migration: V*__content_quests_initial_import.sql
Issue: #{number}
```

### 3. Database → QA

**Database:**
- Применяет миграцию: `liquibase update` или прямой SQL
- Проверяет импорт: `SELECT COUNT(*) FROM gameplay.quest_definitions;`
- Передает в `QA - Todo`

**Комментарий:**
```markdown
OK Content quests migration applied. {count} quests imported.
Migration: V*__content_quests_initial_import.sql
Issue: #{number}
```

### 4. QA → Release

**QA:**
- Тестирует импортированные квесты
- Проверяет доступность через API
- Передает в `Release - Todo` или закрывает Issue

## Альтернатива: API Batch Import

Если нужны обновления без миграций:

1. **Backend** реализует batch endpoint: `POST /api/v1/gameplay/quests/content/batch-reload`
2. **Backend** использует: `scripts/import-quests-batch.sh` (если есть)
3. Прямо в `QA - Todo`

## Связанные команды

- `/backend-import-quest-to-db` - для одиночных квестов
- `/database-apply-content-migration` - для применения миграции

