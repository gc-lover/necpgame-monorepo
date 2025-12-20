# Запуск UE5 тестов синхронизации

## Способы запуска тестов

### 1. Через Unreal Editor (рекомендуется)

**Важно для UE5.7:**

1. Убедитесь, что плагин **Editor Tests** активирован:
    - **Edit → Plugins**
    - В разделе **Testing** найдите **Editor Tests**
    - Убедитесь, что плагин включен

2. Откройте проект в Unreal Editor

3. Меню: **Window → Test Automation** (в UE5.7 путь изменился!)

4. В панели Test Automation найдите категорию: **LyraGame.Network.WebSocket**

5. Выберите тесты для запуска:
    - `LyraGame.Network.WebSocket.Connection` - тест создания соединения
    - `LyraGame.Network.WebSocket.PlayerInputEncoding` - тест кодирования PlayerInput
    - `LyraGame.Network.WebSocket.GameStateDecoding` - тест декодирования GameState
    - `LyraGame.Network.WebSocket.SynchronizationIntegration` - полный интеграционный тест

6. Нажмите **Start Tests**

### 2. Через командную строку

#### Запуск всех тестов синхронизации:

```bash
cd client/UE5/NECPGAME
"C:\Program Files\Epic Games\UE_5.7\Engine\Build\BatchFiles\RunUAT.bat" RunUnreal -Project="%CD%\NECPGAME.uproject" -Test="LyraGame.Network.WebSocket.*" -ReportOutputPath="%CD%\TestResults"
```

#### Запуск конкретного теста:

```bash
"C:\Program Files\Epic Games\UE_5.7\Engine\Build\BatchFiles\RunUAT.bat" RunUnreal -Project="%CD%\NECPGAME.uproject" -Test="LyraGame.Network.WebSocket.PlayerInputEncoding" -ReportOutputPath="%CD%\TestResults"
```

#### Запуск интеграционного теста:

```bash
"C:\Program Files\Epic Games\UE_5.7\Engine\Build\BatchFiles\RunUAT.bat" RunUnreal -Project="%CD%\NECPGAME.uproject" -Test="LyraGame.Network.WebSocket.SynchronizationIntegration" -ReportOutputPath="%CD%\TestResults"
```

### 3. Через Gauntlet (интеграционные тесты с реальным игровым циклом)

Для тестирования с реальным игровым циклом используйте Gauntlet тест:

```bash
cd client\UE5\NECPGAME
"C:\Program Files\Epic Games\UE_5.7\Engine\Build\BatchFiles\RunUAT.bat" RunUnreal -Project="%CD%\NECPGAME.uproject" -Test="LyraGame.Network.WebSocket.SynchronizationGauntlet" -Gauntlet -ReportOutputPath="%CD%\TestResults"
```

**Преимущества Gauntlet теста:**

- Работает в реальной игровой среде с активным World
- Обрабатывает асинхронные WebSocket события
- Полный цикл синхронизации с реальным игровым циклом

**Подробнее:** См. `run-ue5-gauntlet-test.md`

## Требования для интеграционных тестов

### Для теста SynchronizationIntegration требуется:

1. **realtime-gateway-go должен быть запущен:**
   ```bash
   docker-compose up -d realtime-gateway
   ```
   Или вручную:
   ```bash
   cd services/realtime-gateway-go
   go run main.go
   ```

2. **Для полного теста цикла также нужен UE5 Dedicated Server:**
    - Запустите сервер в PIE режиме или как отдельный процесс
    - Сервер должен быть подключен к Gateway на `ws://127.0.0.1:18080/server`

## Что тестируется

### 1. Connection Test

- Создание объекта `URealtimeWebSocketConnection`
- Проверка, что объект не NULL

### 2. PlayerInputEncoding Test

- Кодирование PlayerInput в protobuf формат
- Декодирование обратно
- Проверка корректности всех полей (PlayerID, Tick, MoveX, MoveY, Shoot, AimX, AimY)

### 3. GameStateDecoding Test

- Кодирование GameState в protobuf формат
- Декодирование обратно
- Проверка корректности Tick и Entity данных

### 4. SynchronizationIntegration Test

- Подключение к WebSocket Gateway
- Отправка PlayerInput сообщений
- Получение GameState сообщений (если сервер запущен)
- Проверка полного цикла синхронизации

## Результаты тестов

Результаты сохраняются в:

- `TestResults/` - JSON отчеты
- Automation Tool в Editor - визуальные результаты

## Отладка

Если тесты не запускаются:

1. Проверьте, что проект скомпилирован
2. Убедитесь, что все зависимости подключены в `LyraGame.Build.cs`
3. Проверьте логи в `Saved/Logs/`
4. Для интеграционных тестов убедитесь, что Gateway запущен

## Примеры использования

### Быстрая проверка кодирования:

```bash
# Запуск только теста кодирования PlayerInput
"C:\Program Files\Epic Games\UE_5.7\Engine\Build\BatchFiles\RunUAT.bat" RunUnreal -Project="%CD%\NECPGAME.uproject" -Test="LyraGame.Network.WebSocket.PlayerInputEncoding"
```

### Полный интеграционный тест:

```bash
# 1. Запустить Gateway
docker-compose up -d realtime-gateway

# 2. Запустить тест
"C:\Program Files\Epic Games\UE_5.7\Engine\Build\BatchFiles\RunUAT.bat" RunUnreal -Project="%CD%\NECPGAME.uproject" -Test="LyraGame.Network.WebSocket.SynchronizationIntegration"
```

