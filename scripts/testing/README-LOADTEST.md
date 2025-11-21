# Инструмент нагрузочного тестирования WebSocket

Автоматизированный инструмент для нагрузочного тестирования WebSocket Gateway с множественными параллельными клиентами.

## Описание

Инструмент создает N параллельных WebSocket клиентов, каждый из которых:
- Подключается к Gateway
- Отправляет PlayerInput сообщения с заданной частотой (по умолчанию 60 Hz)
- Получает GameState сообщения от сервера
- Собирает метрики производительности

## Требования

- Go 1.24+
- Запущенный realtime-gateway сервис
- (Опционально) UE5 Dedicated Server для получения GameState

## Использование

### Windows (PowerShell)

```powershell
# Базовый тест: 10 клиентов, 60 секунд
.\scripts\testing\run-loadtest.ps1

# Кастомный тест: 50 клиентов, 120 секунд
.\scripts\testing\run-loadtest.ps1 -Clients 50 -DurationSeconds 120

# Тест с другими параметрами
.\scripts\testing\run-loadtest.ps1 `
    -ServerURL "ws://127.0.0.1:18080/ws?token=test" `
    -Clients 100 `
    -DurationSeconds 300 `
    -PlayerInputHz 60 `
    -ReportIntervalSeconds 10
```

### Linux/macOS (Bash)

```bash
# Перейти в директорию сервиса
cd services/realtime-gateway-go

# Собрать инструмент
go build -o loadtest ./cmd/loadtest

# Запустить тест
./loadtest \
    -url "ws://127.0.0.1:18080/ws?token=test" \
    -clients 10 \
    -duration 60s \
    -hz 60 \
    -report 10s

# Очистка
rm loadtest
```

### Параметры командной строки

```
-url string
    WebSocket server URL (default: "ws://127.0.0.1:18080/ws?token=test")

-clients int
    Number of concurrent clients (default: 10)

-duration duration
    Test duration (default: 60s)

-hz int
    PlayerInput frequency in Hz (default: 60)

-report duration
    Report interval (default: 10s)
```

## Примеры сценариев

### Сценарий 1: Базовое тестирование (10 клиентов)

```powershell
.\scripts\testing\run-loadtest.ps1 -Clients 10 -DurationSeconds 60
```

**Цель**: Проверить базовую работоспособность системы.

**Ожидаемые результаты**:
- Все клиенты подключаются успешно
- PlayerInput отправляется с частотой ~60 Hz
- GameState получается (если сервер запущен)
- Нет ошибок соединения

### Сценарий 2: Средняя нагрузка (50 клиентов)

```powershell
.\scripts\testing\run-loadtest.ps1 -Clients 50 -DurationSeconds 120
```

**Цель**: Проверить производительность при средней нагрузке.

**Ожидаемые результаты**:
- Стабильная работа системы
- PlayerInput отправляется корректно
- GameState получается с небольшой задержкой (<100 мс)
- Метрики Prometheus показывают корректные значения

### Сценарий 3: Высокая нагрузка (100 клиентов)

```powershell
.\scripts\testing\run-loadtest.ps1 -Clients 100 -DurationSeconds 300
```

**Цель**: Проверить производительность при высокой нагрузке.

**Ожидаемые результаты**:
- Система работает стабильно
- Возможны небольшие задержки в GameState
- Метрики показывают производительность системы

### Сценарий 4: Длительное тестирование (стабильность)

```powershell
.\scripts\testing\run-loadtest.ps1 -Clients 10 -DurationSeconds 1800 -ReportIntervalSeconds 60
```

**Цель**: Проверить долгосрочную стабильность системы.

**Ожидаемые результаты**:
- Нет утечек памяти
- Соединения остаются стабильными
- Метрики не деградируют со временем

## Метрики, собираемые инструментом

### Общие метрики
- **Connections**: Количество успешных подключений
- **Failed Connections**: Количество неудачных подключений
- **PlayerInput Sent**: Общее количество отправленных PlayerInput
- **GameState Received**: Общее количество полученных GameState
- **Bytes Sent**: Общий объем отправленных данных
- **Bytes Received**: Общий объем полученных данных
- **Errors**: Количество ошибок соединения

### Пропускная способность
- **PlayerInput msg/s**: Количество PlayerInput сообщений в секунду
- **GameState msg/s**: Количество GameState сообщений в секунду
- **Bytes Sent KB/s**: Скорость отправки данных
- **Bytes Received KB/s**: Скорость получения данных

### Размеры сообщений
- **Avg PlayerInput Size**: Средний размер PlayerInput сообщения
- **Avg GameState Size**: Средний размер GameState сообщения

## Интерпретация результатов

### Хорошие результаты
- ✅ Все клиенты подключаются успешно (Failed Connections = 0)
- ✅ PlayerInput отправляется с заданной частотой (±5%)
- ✅ GameState получается регулярно
- ✅ Нет ошибок соединения
- ✅ Размеры сообщений соответствуют ожидаемым (PlayerInput ~47 байт)

### Проблемы и решения

**Проблема**: Высокое количество Failed Connections
- **Причина**: Gateway перегружен или недоступен
- **Решение**: Проверить статус сервиса, увеличить ресурсы

**Проблема**: GameState не получается
- **Причина**: UE5 Dedicated Server не запущен
- **Решение**: Запустить UE5 Dedicated Server перед тестом

**Проблема**: Низкая частота PlayerInput
- **Причина**: Высокая нагрузка на Gateway или сеть
- **Решение**: Проверить метрики Prometheus, оптимизировать Gateway

**Проблема**: Большой размер GameState
- **Причина**: Отправляется полный snapshot для всех игроков
- **Решение**: Реализовать дельта-компрессию

## Интеграция с Prometheus

Инструмент собирает метрики локально. Для полной картины рекомендуется:

1. Мониторить метрики Prometheus во время теста:
   ```
   http://localhost:9090
   ```

2. Проверить метрики Gateway:
   ```
   http://localhost:9093/metrics
   ```

3. Сравнить метрики:
   - `player_input_received_total` в Prometheus vs PlayerInput Sent в отчете
   - `gamestate_broadcasted_total` в Prometheus vs GameState Received в отчете

## Следующие шаги

После проведения нагрузочного тестирования:

1. **Собрать baseline метрики** для текущей реализации
2. **Реализовать дельта-компрессию** для GameState
3. **Оптимизировать частоту обновлений** (адаптивная частота)
4. **Повторить тестирование** для проверки улучшений
5. **Сравнить результаты** до и после оптимизации

## Примечания

- Инструмент не тестирует UE5 Dedicated Server напрямую, только Gateway
- Для полного тестирования цикла синхронизации необходим запущенный UE5 Dedicated Server
- Инструмент генерирует фиктивные PlayerInput (движение вперед, без стрельбы)
- Для более реалистичных тестов можно расширить инструмент с вариациями входных данных

