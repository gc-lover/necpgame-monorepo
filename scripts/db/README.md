# NECPGAME Database Bootstrap

## Запуск PostgreSQL (Docker)

```bash
cd infrastructure/docker/postgres
docker compose up -d
```

## Применение миграций (Liquibase)

Структура:
- `infrastructure/liquibase/changelog.yaml`
- `infrastructure/liquibase/migrations/*`

Пример запуска (через CLI, настройте переменные окружения):

```bash
liquibase \
  --url=jdbc:postgresql://localhost:5432/necpgame \
  --username=postgres \
  --password=postgres \
  --changeLogFile=infrastructure/liquibase/changelog.yaml \
  update
```

## Начальные данные

- Сиды находятся в `V1_4__seed_reference_data.sql` (плейсхолдеры).

## Python скрипты в миграциях

Liquibase поддерживает выполнение Python скриптов в дополнение к SQL.

**Пример использования:**
- См. `infrastructure/liquibase/migrations/V2_2__python_script_example.xml`
- Документация: `infrastructure/liquibase/migrations/scripts/README.md`

**Требования:**
- Python 3.x установлен
- Зависимости установлены (например, `psycopg2` для PostgreSQL)

**Использование:**
```xml
<executeCommand executable="python">
    <arg value="path/to/script.py"/>
    <arg value="--database-url"/>
    <arg value="${database.url}"/>
</executeCommand>
```


