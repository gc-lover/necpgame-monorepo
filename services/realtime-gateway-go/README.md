# Realtime QUIC Gateway (Go)

QUIC шлюз для боёв/инстансов с поддержкой QUIC/UDP и бинарных Protobuf payload'ов.

## Технологии

- **Go 1.21+**
- **quic-go v0.40.1** - QUIC реализация для Go
- **Protobuf** - бинарная сериализация сообщений

## Преимущества Go версии

- ✅ Нет проблем с нативными библиотеками (все в одном бинарнике)
- ✅ Статическая сборка - один бинарник, работает везде
- ✅ Простое развертывание в Docker (маленький образ ~20MB)
- ✅ Отличная производительность для игрового сервера (60+ TPS)
- ✅ Зрелая библиотека quic-go с активной поддержкой
- ✅ Простая интеграция с Java сервисами через gRPC

## Сборка

```bash
cd services/realtime-gateway-go
go mod download
go build -o realtime-gateway .
```

Для Windows:
```bash
go build -o realtime-gateway.exe .
```

## Запуск

Локально:
```bash
./realtime-gateway
# или
go run .
```

Через скрипт:
```bash
scripts\run\realtime-gateway.cmd  # Windows
```

Сервер будет слушать на `0.0.0.0:18080` (UDP).

## Docker

Сборка образа:
```bash
docker build -t necpgame-realtime-gateway-go:latest services/realtime-gateway-go
```

Запуск контейнера:
```bash
docker run -p 18080:18080/udp necpgame-realtime-gateway-go:latest
```

Через docker-compose:
```bash
docker-compose up realtime-gateway
```

## Протокол

Сообщения: `Heartbeat`, `Echo`, `PlayerInput`, `GameState` (см. `../../proto/realtime/realtime.proto`)

## Настройка UDP буфера (опционально)

Для лучшей производительности рекомендуется увеличить размер UDP буфера:

**Linux:**
```bash
sudo sysctl -w net.core.rmem_max=2097152
sudo sysctl -w net.core.rmem_default=2097152
```

**Docker:**
Добавить в docker-compose.yml:
```yaml
sysctls:
  - net.core.rmem_max=2097152
  - net.core.rmem_default=2097152
```

## Архитектура

```
realtime-gateway-go/
  main.go              - точка входа
  server/
    quic_server.go    - QUIC сервер
    handler.go        - обработка соединений и потоков
  go.mod              - зависимости
  Dockerfile          - сборка образа
```

## Следующие шаги

- [ ] Интеграция Protobuf для обработки сообщений
- [ ] Интеграция с UE5 Dedicated Server (маршрутизация пакетов)
- [ ] Добавление метрик (Prometheus)
- [ ] Логирование (структурированные логи)
- [ ] Graceful shutdown

## Примечание

**Combat-Sim прототип удален** - игровая логика боя теперь реализуется на **UE5 Dedicated Server** (авторитетный сервер, физика, репликация из коробки). См. `knowledge/implementation/LANGUAGE_CHOICE_STRATEGY.md`.

