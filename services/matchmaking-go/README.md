# Matchmaking (Go)

Сервис матчмейкинга с очередями Redis для формирования команд и аллокации инстансов.

## Технологии

- **Go 1.21+**
- **go-redis/v9** - Redis клиент
- **Redis Streams** - для аллокаций

## Преимущества Go версии

- OK Простая логика очередей
- OK Отличная поддержка Redis (go-redis)
- OK Низкая задержка для матчмейкинга
- OK Меньше памяти чем Java
- OK Статическая сборка - один бинарник
- OK Простое развертывание в Docker (~20MB образ)

## Модель данных (Redis)

- `mm:queue:<mode>` - список билетов (LPUSH/RPOP)
- `mm:ticket:<id>` - hash с полями игрока/пати/предпочтений
- `mm:allocations` - stream записей {instance, address, mode, players}

## Алгоритм

1. Создать билет: `ticketId`, положить в `mm:ticket:*`, LPUSH в `mm:queue:<mode>`
2. Матчер читает из очереди, собирает N игроков, создаёт запись в `mm:allocations`
3. UE5 Dedicated Server подписан на `mm:allocations`, поднимает инстанс и публикует endpoint
4. Клиент получает endpoint и подключается к QUIC-шлюзу с токеном

## Сборка

```bash
cd services/matchmaking-go
go mod download
go build -o matchmaking .
```

Для Windows:
```bash
go build -o matchmaking.exe .
```

## Запуск

Локально:
```bash
./matchmaking
# или
go run .
```

Через скрипт:
```bash
scripts\run\matchmaking.cmd  # Windows
```

## Docker

Сборка образа:
```bash
docker build -t necpgame-matchmaking-go:latest services/matchmaking-go
```

Запуск контейнера:
```bash
docker run -p 6379:6379 -e REDIS_URL=redis://redis:6379 -e MODE=pve8 -e TEAM_SIZE=8 necpgame-matchmaking-go:latest
```

Через docker-compose:
```bash
docker-compose up matchmaking
```

## Переменные окружения

- `REDIS_URL` - URL Redis (по умолчанию: redis://localhost:6379)
- `MODE` - режим матча (по умолчанию: pve8)
- `TEAM_SIZE` - размер команды (по умолчанию: 8)

## Архитектура

```
matchmaking-go/
  main.go              - точка входа
  server/
    matchmaker.go     - логика матчмейкинга
    config.go         - конфигурация
  go.mod              - зависимости
  Dockerfile          - сборка образа
```

## Следующие шаги

- [ ] Интеграция с UE5 Dedicated Server (подписка на аллокации)
- [ ] Добавление метрик (Prometheus)
- [ ] Логирование (структурированные логи)
- [ ] Graceful shutdown

