# Применение всех миграций в Docker контейнере

## Быстрый старт

### Вариант 1: Через скрипт (рекомендуется)

**PowerShell:**

```powershell
.\scripts\db\apply-all-migrations.ps1
```

**Bash:**

```bash
./scripts/db/apply-all-migrations.sh
```

### Вариант 2: Через Docker Compose

```bash
cd infrastructure/docker/postgres
docker compose -f docker-compose.yml -f docker-compose.migrations.yml up liquibase
```

### Вариант 3: Через Liquibase CLI

Если у вас установлен Liquibase CLI:

```bash
liquibase \
  --url=jdbc:postgresql://localhost:5432/necpgame \
  --username=postgres \
  --password=postgres \
  --changeLogFile=infrastructure/liquibase/changelog.yaml \
  update
```

## Структура миграций

- **Схемные миграции**: `infrastructure/liquibase/migrations/V*.sql`
- **Контентные миграции**:
    - `infrastructure/liquibase/migrations/data/quests/` (711 миграций)
    - `infrastructure/liquibase/migrations/data/npcs/` (170 миграций)
    - `infrastructure/liquibase/migrations/data/dialogues/` (19 миграций)

**Всего: ~900 контентных миграций + ~170 схемных**

## Проверка статуса

После применения миграций проверьте статус:

```bash
docker exec necpgame-postgres psql -U postgres -d necpgame -c "
SELECT 
    COUNT(*) as total_changesets,
    MAX(EXECUTEDAT) as last_migration
FROM databasechangelog;
"
```

## Генерация changelog для контентных миграций

Если добавили новые контентные миграции, обновите changelog:

```powershell
.\scripts\db\generate-content-changelog.ps1
```

Это создаст/обновит `infrastructure/liquibase/changelog-content.yaml`.

## Troubleshooting

### Контейнер не найден

```bash
cd infrastructure/docker/postgres
docker compose up -d
```

### Ошибка подключения

Проверьте, что PostgreSQL запущен:

```bash
docker ps | grep necpgame-postgres
```

### Ошибка миграции

Проверьте логи:

```bash
docker compose -f docker-compose.yml -f docker-compose.migrations.yml logs liquibase
```

## Issue

Related: #50

