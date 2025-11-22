# Social Service (Go)

Микросервис для социальных функций игры: уведомления, друзья, гильдии, почта и т.д.

## Порт

- HTTP: `8084`
- Metrics: `9094`

## API Endpoints

### Notifications
- `POST /api/v1/social/notifications` - Создать уведомление
- `GET /api/v1/social/notifications` - Получить список уведомлений
- `GET /api/v1/social/notifications/{id}` - Получить уведомление
- `PUT /api/v1/social/notifications/{id}/status` - Обновить статус уведомления
- `GET /api/v1/social/notifications/preferences` - Получить настройки уведомлений
- `PUT /api/v1/social/notifications/preferences` - Обновить настройки уведомлений

### Friends
- `GET /api/v1/social/friends` - Получить список друзей
- `POST /api/v1/social/friends/request` - Отправить запрос на дружбу
- `PUT /api/v1/social/friends/{id}/accept` - Принять запрос на дружбу
- `DELETE /api/v1/social/friends/{id}` - Удалить друга
- `POST /api/v1/social/friends/{id}/block` - Заблокировать друга

### Guilds
- `GET /api/v1/social/guilds` - Получить список гильдий
- `POST /api/v1/social/guilds` - Создать гильдию
- `GET /api/v1/social/guilds/{id}` - Получить информацию о гильдии
- `PUT /api/v1/social/guilds/{id}` - Обновить гильдию
- `POST /api/v1/social/guilds/{id}/members` - Добавить участника
- `DELETE /api/v1/social/guilds/{id}/members/{memberId}` - Удалить участника

## Переменные окружения

- `ADDR` - Адрес HTTP сервера (по умолчанию: `0.0.0.0:8084`)
- `METRICS_ADDR` - Адрес metrics сервера (по умолчанию: `:9094`)
- `DATABASE_URL` - URL подключения к PostgreSQL
- `REDIS_URL` - URL подключения к Redis
- `KEYCLOAK_URL` - URL Keycloak сервера
- `KEYCLOAK_REALM` - Realm Keycloak
- `AUTH_ENABLED` - Включить JWT аутентификацию (по умолчанию: `true`)
- `LOG_LEVEL` - Уровень логирования (по умолчанию: `info`)

## Запуск

```bash
go run main.go
```

## Docker

```bash
docker build -t social-service-go .
docker run -p 8084:8084 social-service-go
```

