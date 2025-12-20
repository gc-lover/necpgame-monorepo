# WebSocket Lobby (Go)

WebSocket сервер для лобби/чата с поддержкой комнат и JWT аутентификации.

## Технологии

- **Go 1.21+**
- **gorilla/websocket** - WebSocket реализация
- **JWT** - аутентификация (базовая валидация)

## Преимущества Go версии

- OK Простая реализация WebSocket
- OK Низкая задержка для чата
- OK Меньше памяти чем Java
- OK Статическая сборка - один бинарник
- OK Простое развертывание в Docker (~20MB образ)

## Сборка

```bash
cd services/ws-lobby-go
go mod download
go build -o ws-lobby .
```

Для Windows:

```bash
go build -o ws-lobby.exe .
```

## Запуск

Локально:

```bash
./ws-lobby
# или
go run .
```

Через скрипт:

```bash
scripts\run\ws-lobby.cmd  # Windows
```

Сервер будет слушать на `0.0.0.0:18081` (WebSocket).

## Docker

Сборка образа:

```bash
docker build -t necpgame-ws-lobby-go:latest services/ws-lobby-go
```

Запуск контейнера:

```bash
docker run -p 18081:18081 necpgame-ws-lobby-go:latest
```

Через docker-compose:

```bash
docker-compose up ws-lobby
```

## Протокол

### Подключение

```
ws://localhost:18081/ws?token=YOUR_JWT
```

### Команды

- `JOIN <room>` - присоединиться к комнате
- `LEAVE` - покинуть комнату (вернуться в general)
- `MSG <text>` - отправить сообщение в текущую комнату
- Любой другой текст - эхо всем в комнате

### Пример

```bash
wscat -c "ws://localhost:18081/ws?token=YOUR_JWT"
> JOIN general
< JOINED general
> MSG hello
< [general] hello
```

## Переменные окружения

- `PORT` - порт сервера (по умолчанию: 18081)
- `KEYCLOAK_ISSUER` - issuer для JWT (по умолчанию: http://localhost:8080/realms/necpgame)
- `KEYCLOAK_JWKS_URL` - URL для получения JWKS (по умолчанию: {issuer}/protocol/openid-connect/certs)

## Архитектура

```
ws-lobby-go/
  main.go              - точка входа
  server/
    lobby_server.go   - WebSocket сервер
    config.go         - конфигурация
    jwt_validator.go  - JWT валидация
  go.mod              - зависимости
  Dockerfile          - сборка образа
```

