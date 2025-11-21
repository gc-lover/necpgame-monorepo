# Запуск Gauntlet теста синхронизации WebSocket

## Что такое Gauntlet тест?

Gauntlet тесты запускаются в реальной игровой среде с активным World и игровым циклом. Это позволяет тестировать асинхронные операции, такие как WebSocket соединения, которые требуют обработки событий в игровом потоке.

**Преимущества Gauntlet теста:**
- OK Работает в реальной игровой среде с активным World
- OK Обрабатывает асинхронные WebSocket события
- OK Тестирует полный цикл синхронизации
- OK Может работать с Dedicated Server

## Подготовка

### 1. Запустить Gateway

```bash
docker-compose up -d realtime-gateway
```

### 2. Проверить статус

```bash
docker-compose ps realtime-gateway
```

## Запуск Gauntlet теста

### Через командную строку (рекомендуется)

Gauntlet тесты запускаются через специальную команду Gauntlet:

```powershell
cd client\UE5\NECPGAME

# Запуск Gauntlet теста синхронизации
& "C:\Program Files\Epic Games\UE_5.7\Engine\Build\BatchFiles\RunUAT.bat" RunUnreal `
  -Project="$PWD\NECPGAME.uproject" `
  -Test="LyraTest.WebSocketSyncTest" `
  -Gauntlet `
  -ReportOutputPath="$PWD\TestResults"
```

**Или через Gauntlet напрямую:**

```powershell
& "C:\Program Files\Epic Games\UE_5.7\Engine\Build\BatchFiles\RunUAT.bat" RunUnreal `
  -Project="$PWD\NECPGAME.uproject" `
  -Test="LyraTest.WebSocketSyncTest" `
  -Gauntlet `
  -GauntletSettingsFile="$PWD\Build\GauntletSettings.xml"
```

**Примечание:** 
- Тест регистрируется через C# класс `LyraTest.WebSocketSyncTest`
- Контроллер называется `WebSocketSyncGauntletTest` (без префикса U)
- Тест автоматически запускается при загрузке карты

## Что тестируется

Gauntlet тест проверяет полный цикл синхронизации в реальной игровой среде:

1. **Подключение к Gateway** - устанавливает WebSocket соединение
2. **Отправка PlayerInput** - отправляет 10 сообщений PlayerInput
3. **Получение GameState** - проверяет получение GameState от сервера
4. **Валидация данных** - проверяет корректность синхронизации

## Преимущества Gauntlet теста

- OK Работает в реальной игровой среде с активным World
- OK Обрабатывает асинхронные WebSocket события
- OK Тестирует полный цикл синхронизации
- OK Может работать с Dedicated Server

## Отличия от Automation теста

| Automation Test | Gauntlet Test |
|----------------|---------------|
| Без активного World | С активным World |
| Ограниченная обработка событий | Полная обработка событий |
| Быстрые unit тесты | Интеграционные тесты |
| Не требует запуска игры | Запускает игру |

## Результаты

Результаты сохраняются в:
- `TestResults/` - JSON отчеты
- Логи в `Saved/Logs/NECPGAME.log` (ищите `LogGauntlet`)

## Отладка

Если тест не запускается:

1. Проверьте, что Gateway запущен: `docker-compose ps realtime-gateway`
2. Проверьте логи: `docker-compose logs realtime-gateway`
3. Проверьте логи UE5: `Saved/Logs/NECPGAME.log`
4. Убедитесь, что проект скомпилирован

## Пример успешного запуска

```
[LogGauntlet] LyraWebSocketSyncGauntletTest: Map loaded, starting test...
[LogGauntlet] LyraWebSocketSyncGauntletTest: Starting WebSocket synchronization test
[LogGauntlet] LyraWebSocketSyncGauntletTest: Connecting to 127.0.0.1:18080
[LogGauntlet] LyraWebSocketSyncGauntletTest: Successfully connected to Gateway
[LogGauntlet] LyraWebSocketSyncGauntletTest: Sent 10 PlayerInput messages
[LogGauntlet] LyraWebSocketSyncGauntletTest: Received GameState #1
[LogGauntlet] LyraWebSocketSyncGauntletTest: Test passed: Sent 10 PlayerInput, Received 1 GameState
```

