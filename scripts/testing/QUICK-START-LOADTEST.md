# Быстрый старт: Нагрузочное тестирование

## Быстрая проверка (5 минут)

### 1. Убедитесь, что сервисы запущены

```powershell
# Проверить статус Gateway
docker-compose ps realtime-gateway

# Если не запущен, запустить
docker-compose up -d realtime-gateway
```

### 2. Запустить базовый тест (10 клиентов, 60 секунд)

```powershell
cd C:\NECPGAME
.\scripts\testing\run-loadtest.ps1 -Clients 10 -DurationSeconds 60
```

### 3. Проверить результаты

Инструмент автоматически выводит отчет каждые 10 секунд и финальный отчет.

**Ожидаемые результаты**:

- OK Все клиенты подключаются успешно
- OK PlayerInput отправляется ~60 msg/s на клиента
- OK GameState получается (если сервер запущен)
- OK Нет ошибок соединения

## Расширенное тестирование

### Тест средней нагрузки (50 клиентов)

```powershell
.\scripts\testing\run-loadtest.ps1 -Clients 50 -DurationSeconds 120
```

### Тест высокой нагрузки (100 клиентов)

```powershell
.\scripts\testing\run-loadtest.ps1 -Clients 100 -DurationSeconds 300 -ReportIntervalSeconds 15
```

### Длительное тестирование (стабильность)

```powershell
.\scripts\testing\run-loadtest.ps1 -Clients 10 -DurationSeconds 1800 -ReportIntervalSeconds 60
```

## Мониторинг метрик

Во время теста можно мониторить метрики:

1. **Prometheus** (метрики Gateway):
   ```
   http://localhost:9090
   ```

2. **Grafana** (визуализация):
   ```
   http://localhost:3000
   ```

3. **Метрики Gateway напрямую**:
   ```
   http://localhost:9093/metrics
   ```

## Что дальше?

После проведения тестирования:

1. **Собрать baseline метрики** - записать текущие результаты
2. **Реализовать дельта-компрессию** - оптимизировать размер GameState
3. **Повторить тестирование** - сравнить результаты до/после

## Помощь

Подробная документация: `scripts/testing/README-LOADTEST.md`

