# UE5 Dedicated Server Setup для NECPGAME

## Обзор

UE5 Dedicated Server используется для авторитетной симуляции игровой логики боя. Интеграция с WebSocket-шлюзом позволяет маршрутизировать PlayerInput от клиентов и отправлять GameState обратно.

## Архитектура

```
Клиент (UE5) 
  → WebSocket → Go Gateway (realtime-gateway-go)
  → WebSocket → UE5 Dedicated Server
  → GameState → Go Gateway → Клиент
```

## Сборка Dedicated Server

### Windows

```batch
REM Найти путь к Unreal Engine 5.7
set UE_ROOT=C:\Program Files\Epic Games\UE_5.7\Engine

REM Собрать сервер
"%UE_ROOT%\Build\BatchFiles\Build.bat" LyraServer Win64 Development "C:\NECPGAME\client\UE5\NECPGAME\NECPGAME.uproject" -waitmutex

REM Или через UAT (Unreal Automation Tool)
"%UE_ROOT%\Build\BatchFiles\RunUAT.bat" BuildCookRun -project="C:\NECPGAME\client\UE5\NECPGAME\NECPGAME.uproject" -platform=Win64 -clientconfig=Development -serverconfig=Development -server -cook -allmaps -build -stage -pak -archive -archivedirectory="C:\NECPGAME\builds\server"
```

### Linux

```bash
# Собрать сервер
./Engine/Build/BatchFiles/RunUAT.sh BuildCookRun \
  -project="/path/to/NECPGAME/NECPGAME.uproject" \
  -platform=Linux \
  -clientconfig=Development \
  -serverconfig=Development \
  -server \
  -cook \
  -allmaps \
  -build \
  -stage \
  -pak \
  -archive \
  -archivedirectory="/path/to/builds/server"
```

## Запуск Dedicated Server

### Локальный запуск (Windows)

```batch
REM Запуск сервера с картой
NECPGAMEServer.exe /Game/ShooterMaps/Maps/L_Expanse.L_Expanse?listen -server -log

REM Запуск с указанием порта
NECPGAMEServer.exe /Game/ShooterMaps/Maps/L_Expanse.L_Expanse?listen -server -port=7777 -log

REM Запуск с WebSocket Gateway
NECPGAMEServer.exe /Game/ShooterMaps/Maps/L_Expanse.L_Expanse?listen -server -port=7777 -WebSocketGateway=127.0.0.1:18080 -log
```

### Параметры командной строки

- `-server` - запуск в режиме dedicated server
- `-log` - вывод логов в консоль
- `-port=<port>` - порт для подключения клиентов (по умолчанию 7777)
- `-WebSocketGateway=<address>:<port>` - адрес WebSocket-шлюза
- `-MaxPlayers=<count>` - максимальное количество игроков (по умолчанию 64)
- `-GamePort=<port>` - порт для игрового трафика
- `-QueryPort=<port>` - порт для запросов сервера

## Конфигурация

### DefaultServer.ini

Конфигурация для dedicated server находится в `Config/DefaultServer.ini`:

```ini
[/Script/OnlineSubsystemUtils.IpNetDriver]
NetServerMaxTickRate=60
MaxClientRate=200000
MaxInternetClientRate=200000

[WebSocketGateway]
GatewayAddress=127.0.0.1
GatewayPort=18080
UseTLS=false
```

## Интеграция с WebSocket Gateway

### Инициализация подключения

`ULyraServerGatewayConnection` автоматически подключается к WebSocket-шлюзу при запуске сервера.

### Обработка PlayerInput

1. Клиент отправляет `PlayerInput` через WebSocket на Go Gateway
2. Go Gateway маршрутизирует сообщение на UE5 Dedicated Server
3. `ULyraServerGatewayConnection::OnPlayerInputReceived` получает данные
4. `ALyraGameMode` обрабатывает input и обновляет игровое состояние

### Отправка GameState

1. UE5 Dedicated Server обновляет `ALyraGameState`
2. `ULyraServerGatewayConnection::SendGameStateUpdate` отправляет данные на Gateway
3. Go Gateway маршрутизирует сообщение клиентам
4. Клиенты получают обновления через `URealtimeWebSocketConnection`

## Тестирование

### Локальное тестирование

1. Запустить Go Gateway: `docker-compose up realtime-gateway`
2. Запустить UE5 Dedicated Server с параметрами выше
3. Запустить клиент и подключиться через WebSocket

### Проверка подключения

Логи сервера должны показывать:
```
LyraServerGatewayConnection: Connected to gateway at 127.0.0.1:18080
```

## Docker контейнеризация

TODO: Добавить Dockerfile для UE5 Dedicated Server

## Производительность

- **Tick Rate**: 60 Hz (настраивается в DefaultServer.ini)
- **Max Players**: 64 (настраивается в DefaultServer.ini)
- **Bandwidth**: 200 KB/s на клиента (настраивается в DefaultServer.ini)

## Отладка

### Логи

Логи сервера выводятся в консоль при запуске с флагом `-log`.

### Команды консоли

В консоли сервера доступны команды:
- `stat fps` - FPS сервера
- `stat net` - статистика сети
- `stat game` - статистика игры

## Следующие шаги

1. OK Базовая конфигурация dedicated server
2. OK Интеграция с WebSocket Gateway
3. WARNING Обработка PlayerInput на сервере
4. WARNING Репликация GameState клиентам
5. WARNING Физика стрельбы и попаданий (Chaos Physics)
6. WARNING GAS для способностей и эффектов

