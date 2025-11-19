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


