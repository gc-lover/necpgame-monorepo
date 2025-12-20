# UE5 Тесты синхронизации

## Быстрый старт

### 1. Через Unreal Editor (самый простой способ)

**Для UE5.7:**

1. Убедитесь, что плагин **Editor Tests** включен: **Edit → Plugins → Testing → Editor Tests**
2. Откройте проект `NECPGAME.uproject` в Unreal Editor
3. Меню: **Window → Test Automation** (в UE5.7 путь изменился!)
4. Найдите категорию: **LyraGame.Network.WebSocket**
5. Выберите тесты и нажмите **Start Tests**

### 2. Через командную строку

```powershell
# Перейдите в директорию проекта
cd client\UE5\NECPGAME

# Запустите все тесты синхронизации
& "C:\Program Files\Epic Games\UE_5.7\Engine\Build\BatchFiles\RunUAT.bat" RunUnreal -Project="$PWD\NECPGAME.uproject" -Test="LyraGame.Network.WebSocket.*"
```

## Доступные тесты

1. **Connection** - проверка создания WebSocket соединения
2. **PlayerInputEncoding** - проверка кодирования/декодирования PlayerInput
3. **GameStateDecoding** - проверка кодирования/декодирования GameState
4. **SynchronizationIntegration** - полный интеграционный тест (требует Gateway)

## Для интеграционного теста

Перед запуском `SynchronizationIntegration` убедитесь, что:

```bash
# Gateway должен быть запущен (сервис называется realtime-gateway)
docker-compose up -d realtime-gateway

# Проверить статус
docker-compose ps realtime-gateway

# Проверить логи
docker-compose logs realtime-gateway
```

## Gauntlet тест (интеграционный с реальным игровым циклом)

Для полного интеграционного теста с реальным игровым циклом используйте Gauntlet:

```powershell
cd client\UE5\NECPGAME
& "C:\Program Files\Epic Games\UE_5.7\Engine\Build\BatchFiles\RunUAT.bat" RunUnreal -Project="$PWD\NECPGAME.uproject" -Test="LyraGame.Network.WebSocket.SynchronizationGauntlet" -Gauntlet
```

**Преимущества:** Работает в реальной игровой среде, обрабатывает асинхронные события WebSocket.

## Подробная документация

- `run-ue5-tests.md` - детальная информация о Automation тестах
- `run-ue5-gauntlet-test.md` - информация о Gauntlet тестах

