# Отчет о миграции realtime-gateway с Java на Go

## Дата миграции
17 ноября 2025

## Причина миграции

Java версия имела проблемы:
- Нативная библиотека QUIC (`netty-incubator-codec-native-quic`) доступна только для Linux
- Сложности с созданием fat JAR, включающего нативные библиотеки
- Проблемы с кроссплатформенной сборкой
- Зависимости от внешних нативных компонентов

## Решение

Миграция на **Go + quic-go**:
- ✅ Чистая реализация QUIC на Go
- ✅ Статическая сборка - один бинарник
- ✅ Нет проблем с нативными библиотеками
- ✅ Простое развертывание в Docker
- ✅ Отличная производительность

## Что было сделано

1. ✅ Создана структура Go проекта `services/realtime-gateway-go/`
2. ✅ Реализован базовый QUIC сервер на Go с `quic-go`
3. ✅ Создан Dockerfile для статической сборки
4. ✅ Обновлен `docker-compose.yml` для использования Go версии
5. ✅ Обновлены скрипты запуска
6. ✅ Удалена Java версия `services/realtime-gateway/`
7. ✅ Обновлен `services/settings.gradle.kts` (удален realtime-gateway из мультипроекта)

## Структура проекта

```
services/realtime-gateway-go/
  main.go              - точка входа, инициализация сервера
  server/
    quic_server.go    - QUIC сервер с TLS конфигурацией
    handler.go        - обработка соединений и потоков
  go.mod              - зависимости Go
  go.sum              - checksums зависимостей
  Dockerfile          - многоэтапная сборка (builder + runtime)
  README.md           - документация
  .gitignore          - игнорируемые файлы
  .dockerignore       - игнорируемые файлы для Docker
```

## Технологии

- **Go 1.21+**
- **quic-go v0.40.1** - QUIC реализация
- **Alpine Linux** - минимальный Docker образ

## Результаты тестирования

✅ Сборка Go проекта: **УСПЕШНО**
✅ Сборка Docker образа: **УСПЕШНО**
✅ Запуск контейнера: **УСПЕШНО**
✅ QUIC сервер работает: **УСПЕШНО**

## Преимущества новой версии

1. **Простота развертывания**
   - Один бинарник, работает везде
   - Статическая сборка, нет зависимостей

2. **Производительность**
   - Отличная производительность для игрового сервера
   - Низкая задержка

3. **Размер образа**
   - ~20MB (vs ~200MB+ для Java версии)
   - Быстрая загрузка и развертывание

4. **Поддержка**
   - Зрелая библиотека quic-go
   - Активная поддержка сообществом

## Следующие шаги

- [ ] Интеграция Protobuf для обработки сообщений
- [ ] Интеграция с UE5 Dedicated Server (маршрутизация пакетов)
- [ ] Добавление метрик (Prometheus)
- [ ] Структурированное логирование
- [ ] Graceful shutdown
- [ ] Обработка Heartbeat/Echo сообщений через Protobuf

## Примечание

**Combat-Sim прототип удален** - игровая логика боя теперь реализуется на **UE5 Dedicated Server** (авторитетный сервер, физика, репликация из коробки). См. `knowledge/implementation/LANGUAGE_CHOICE_STRATEGY.md`.

## Команды для работы

### Локальная сборка
```bash
cd services/realtime-gateway-go
go build -o realtime-gateway .
```

### Запуск локально
```bash
./realtime-gateway
# или
go run .
```

### Docker сборка
```bash
docker build -t necpgame-realtime-gateway-go:latest services/realtime-gateway-go
```

### Docker запуск
```bash
docker run -p 18080:18080/udp necpgame-realtime-gateway-go:latest
```

### Docker Compose
```bash
docker-compose up realtime-gateway
```

