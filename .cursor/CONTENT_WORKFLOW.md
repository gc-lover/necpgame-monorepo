# Content Workflow — Единый Guide

**Единый документ для контентного workflow (квесты, лор, NPC, диалоги)**

## [FORBIDDEN] EMOJI AND SPECIAL CHARACTERS ЗАПРЕТ

**КРИТИЧНО:** Запрещено использовать эмодзи и специальные Unicode символы в коде!

### Почему запрещено:
— [FORBIDDEN] Ломают выполнение скриптов на Windows
- [FORBIDDEN] Могут вызывать ошибки в терминале
- [FORBIDDEN] Создают проблемы с кодировкой
- [FORBIDDEN] Нарушают совместимость между ОС

### Что use вместо:
- [OK] `:smile:` вместо [EMOJI]
- [OK] `[FORBIDDEN]` вместо [FORBIDDEN]
- [OK] `[OK]` вместо [OK]
- [OK] `[ERROR]` вместо [ERROR]
- [OK] `[WARNING]` вместо [WARNING]

### Автоматическая проверка:
- Pre-commit hooks блокируют коммиты с эмодзи
- Git hooks проверяют staged файлы
- Исключения: `.cursor/rules/*` (документация), `.githooks/*`

## Обзор

Контентный workflow отличается от системного: контент создаётся в YAML, затем импортируется в БД через Backend, и только
после этого тестируется QA.

## Унифицированный Workflow

```
Idea Writer (концепция)
    ↓
Content Writer (YAML файлы)
    ↓ [ВСЕГДА]
Backend
    ├── Таблицы есть + 1-10 квестов → API import → QA
    ├── Таблицы есть + >10 квестов → SQL migrations → DB → QA  
    └── Таблиц нет → Создать Issue для DB, Status: Blocked
            ↓
        Database (создаёт таблицы)
            ↓
        Backend (повторно, после разблокировки)
            ↓
        QA → Release
```

## Детальный Workflow

### 1. Content Writer → Backend

**Content Writer:**

- Создаёт YAML файлы в `knowledge/canon/`
- Валидирует YAML: `/content-writer-validate-quest-yaml #123`
- **ВСЕГДА передаёт в Backend** (не в Database или QA напрямую!)
- Добавляет labels: `canon`, `lore`, `quest` (или `npc`, `dialogue`)

**Комментарий:**

```markdown
[OK] Quest YAML ready. For Backend import to DB.
Issue: #{number}
```

**Важно:** Content Writer НЕ передаёт в Database или QA напрямую. Backend решает способ импорта.

### 2. Backend: Решение способа импорта (НОВАЯ ДОМЕННАЯ АРХИТЕКТУРА!)

**Backend теперь работает с enterprise-grade доменами (see .cursor/DOMAIN_REFERENCE.md)**

**Backend проверяет:**

#### Сценарий A: Таблиц нет

- **Действие:** Создать Issue для Database со статусом `Blocked`
- **Комментарий:** "Tables missing: `gameplay.quest_definitions`. Blocked until DB migration applied."
- **Status:** `Blocked` (текущая задача)
- **После создания таблиц:** Database разблокирует задачу, Backend продолжает

#### Сценарий B: Таблицы есть + 1-10 квестов (одиночный импорт через API домена)

- **Действие:** Импорт через API домена `POST /api/v1/gameplay/quests/content/reload`
- **Используемый домен:** `specialized-domain` (механики квестов)
- **Скрипт:** `scripts/import-quest.ps1` или `scripts/import-quest.sh`
- **Валидация:** Проверить что квест в БД через API домена
- **Передача:** Status `Todo`, Agent `QA`

#### Сценарий C: Таблицы есть + >10 квестов (массовый импорт через миграции)

- **Действие:** Генерация SQL миграций через оптимизированные скрипты
- **Скрипты:** `scripts/generate-content-migrations.sh` (Bash) или `scripts/generate-content-migrations.ps1` (PowerShell)
- **Формат:** 1 файл YAML = 1 миграция (с версией из `metadata.version`)
- **Миграции в enterprise-grade структуре:**
  - Квесты: `infrastructure/liquibase/migrations/data/quests/V*__data_quest_*.sql` (→ `specialized-domain`)
  - NPC: `infrastructure/liquibase/migrations/data/npcs/V*__data_npc_*.sql` (→ `specialized-domain`)
  - Диалоги: `infrastructure/liquibase/migrations/data/dialogues/V*__data_dialogue_*.sql` (→ `social-domain`)
- **Связанные enterprise-grade домены:**
  - Квесты → specialized-domain (gameplay mechanics)
  - NPC → specialized-domain (NPC systems)
  - Диалоги → social-domain (social interactions)
  - See .cursor/DOMAIN_REFERENCE.md for complete list
- **Передача:** Status `Todo`, Agent `DB`

**Комментарий (для Database):**

```markdown
[OK] Content migrations generated. Ready for Database agent.
Quest migrations: {count} files
Issue: #{number}
```

### 3. Database → QA (только для массового импорта)

**Database:**

- Применяет миграции: `liquibase update` или через CI/CD
- Валидация: `SELECT COUNT(*) FROM gameplay.quest_definitions`
- **Передача:** Status `Todo`, Agent `QA`

**Комментарий:**

```markdown
[OK] Content quests migrations applied. {count} quests imported.
Migrations: V*__data_quest_*.sql
Issue: #{number}
```

**Важно:** Database передаёт в QA только для контентных задач. Системные задачи → API Designer.

### 4. QA → Release

**QA:**

- **Проверяет:** Контент импортирован в БД (через API, НЕ через labels!)
- **Тестирует:** Доступность через API, корректность данных, игровая механика
- **Если не импортирован:** Status `Returned`, Agent `Backend` (не Content Writer!)

**Комментарий:**

```markdown
[OK] Testing complete. Ready for release.
Issue: #{number}
```

## Ключевые правила

### Разрыв цикла передачи

**Проблема:** Backend → DB → Backend → DB (цикл)

**Решение:**

- Backend НЕ передаёт в DB для создания таблиц напрямую
- Вместо этого создаёт Issue для DB и ставит текущую задачу в `Blocked`
- После создания таблиц DB разблокирует задачу Backend

### Проверка импорта (QA)

**НЕПРАВИЛЬНО:**

- Проверять labels `canon`, `lore`, `quest` → возвращать в Content Writer

**ПРАВИЛЬНО:**

- Проверять через API что контент импортирован в БД
- Если не импортирован → возвращать в Backend (не Content Writer!)

### Различие системных и контентных задач

**Системные задачи (Database):**

- Создание схем БД, миграции структуры
- **Передача:** Database → API Designer

**Контентные задачи (Database):**

- Применение миграций данных (квесты, NPC, диалоги)
- **Передача:** Database → QA

## Таблицы и миграции (Enterprise-Grade Домены)

### Квесты → `specialized-domain` (Gameplay Mechanics)

- **Таблица:** `gameplay.quest_definitions`
- **Связанный домен:** `specialized-domain` (157 файлов, игровые механики)
- **Схемная миграция:** `V1_46__quest_definitions_tables.sql`
- **Данные миграции:** `V*__data_quest_*.sql` в `infrastructure/liquibase/migrations/data/quests/`
- **API домена:** `proto/openapi/specialized-domain/main.yaml`

### NPC → `specialized-domain` (NPC Systems)

- **Таблица:** `narrative.npc_definitions`
- **Связанный домен:** `specialized-domain` (NPC AI, поведение)
- **Схемная миграция:** `V1_89__narrative_npc_dialogue_tables.sql`
- **Данные миграции:** `V*__data_npc_*.sql` в `infrastructure/liquibase/migrations/data/npcs/`
- **API домена:** `proto/openapi/specialized-domain/main.yaml`

### Диалоги → `social-domain` (Social Interactions)

- **Таблица:** `narrative.dialogue_nodes`
- **Связанный домен:** `social-domain` (91 файл, социальные взаимодействия)
- **Схемная миграция:** `V1_89__narrative_npc_dialogue_tables.sql`
- **Данные миграции:** `V*__data_dialogue_*.sql` в `infrastructure/liquibase/migrations/data/dialogues/`
- **API домена:** `proto/openapi/social-domain/main.yaml`

## Обновления квестов (после первого импорта)

После первого массового импорта, обновления делаются через API:

- **Одиночный:** `scripts/import-quest.ps1` или `scripts/import-quest.sh`
- **Batch (если реализован):** `POST /api/v1/gameplay/quests/content/batch-reload`

**Workflow:** Content Writer → Backend (API import) → QA

## Связанные документы

- `.cursor/AGENT_SIMPLE_GUIDE.md` - быстрый старт для агентов
- `.cursor/rules/agent-content-writer.mdc` - правила Content Writer
- `.cursor/rules/agent-backend.mdc` - правила Backend
- `.cursor/rules/agent-database.mdc` - правила Database
- `.cursor/rules/agent-qa.mdc` - правила QA
- `scripts/CONTENT-MIGRATION-WORKFLOW.md` - детали миграций
