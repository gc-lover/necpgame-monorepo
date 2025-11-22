# Character Service (Go)

Микросервис для управления аккаунтами игроков и персонажами.

## Функциональность

- Создание и управление аккаунтами игроков
- Создание и управление персонажами
- Интеграция с Keycloak для аутентификации
- Кэширование в Redis
- Метрики Prometheus

## API Endpoints

### Accounts
- `GET /api/v1/accounts/{accountId}` - Получить аккаунт по ID
- `POST /api/v1/accounts` - Создать новый аккаунт (требует JWT токен)

### Characters
- `GET /api/v1/characters?account_id={accountId}` - Получить список персонажей аккаунта
- `GET /api/v1/characters/{characterId}` - Получить персонажа по ID
- `POST /api/v1/characters` - Создать нового персонажа (требует JWT токен)
- `PUT /api/v1/characters/{characterId}` - Обновить персонажа (требует JWT токен)
- `DELETE /api/v1/characters/{characterId}` - Удалить персонажа (требует JWT токен)
- `GET /api/v1/characters/{characterId}/validate` - Проверить валидность персонажа

### Health
- `GET /health` - Проверка здоровья сервиса

## Аутентификация

Сервис поддерживает JWT аутентификацию через Keycloak. Для защиты endpoints необходимо:

1. Получить JWT токен от Keycloak
2. Передать токен в заголовке `Authorization: Bearer <token>`

### Отключение аутентификации (для разработки)

Установить переменную окружения:
```bash
AUTH_ENABLED=false
```

## Переменные окружения

- `ADDR` - Адрес HTTP сервера (по умолчанию: `0.0.0.0:8087`)
- `METRICS_ADDR` - Адрес метрик (по умолчанию: `:9092`)
- `DATABASE_URL` - URL подключения к PostgreSQL
- `REDIS_URL` - URL подключения к Redis
- `KEYCLOAK_URL` - URL Keycloak сервера (по умолчанию: `http://localhost:8080`)
- `KEYCLOAK_REALM` - Имя realm в Keycloak (по умолчанию: `necpgame`)
- `AUTH_ENABLED` - Включить/выключить аутентификацию (по умолчанию: `true`)
- `LOG_LEVEL` - Уровень логирования (по умолчанию: `info`)

## Запуск

### Локально

```bash
go run main.go
```

### Docker

```bash
docker-compose up character-service
```

## Метрики

Метрики доступны по адресу `http://localhost:9096/metrics` (Prometheus формат).

### Доступные метрики

- `character_requests_total` - Общее количество запросов
- `character_request_duration_seconds` - Длительность обработки запросов

## Зависимости

- PostgreSQL - основная база данных
- Redis - кэширование
- Keycloak - аутентификация (опционально, если AUTH_ENABLED=false)
