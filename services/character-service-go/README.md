# Character Service (Go)

Микросервис для управления персонажами и аккаунтами игроков.

## Назначение

Этот сервис отвечает за:
- Управление аккаунтами игроков (player_account)
- Создание и управление персонажами (character)
- Получение информации о персонажах
- Валидация персонажей для других сервисов

## Технологии

- **Go 1.24+**
- **PostgreSQL** - хранение аккаунтов и персонажей
- **Redis** - кэширование
- **Keycloak** - аутентификация (интеграция)
- **Prometheus** - метрики

## Архитектура

```
client
    ↓ JWT Token
Keycloak (аутентификация)
    ↓ Token validation
character-service-go (управление персонажами)
    ↓ PostgreSQL
player_account, character (таблицы)
```

## API

### REST Endpoints

- `GET /api/v1/characters` - получить список персонажей по account_id
- `GET /api/v1/characters/:characterId` - получить информацию о персонаже
- `POST /api/v1/characters` - создать персонажа
- `PUT /api/v1/characters/:characterId` - обновить персонажа
- `DELETE /api/v1/characters/:characterId` - удалить персонажа
- `GET /api/v1/accounts/:accountId` - получить информацию об аккаунте
- `POST /api/v1/accounts` - создать аккаунт
- `GET /health` - health check

## База данных

Таблицы:
- `mvp_core.player_account` - аккаунты игроков
- `mvp_core.character` - персонажи игроков

## Интеграция

Интегрируется с:
- **Keycloak** - аутентификация и авторизация
- **inventory-service** - валидация персонажей для инвентаря
- **movement-service** - валидация персонажей для позиций

## Запуск

```bash
cd services/character-service-go
go mod download
go run .
```

## Конфигурация

- `ADDR` - адрес HTTP сервера (по умолчанию: `0.0.0.0:8087`)
- `METRICS_ADDR` - адрес метрик (по умолчанию: `:9092`)
- `DATABASE_URL` - URL подключения к PostgreSQL
- `REDIS_URL` - URL подключения к Redis
- `KEYCLOAK_URL` - URL для валидации JWT токенов (опционально)
- `LOG_LEVEL` - уровень логирования (по умолчанию: `info`)

