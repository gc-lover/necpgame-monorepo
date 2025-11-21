# Быстрый запуск UE5 Dedicated Server локально

## Вариант 1: Через скрипт RunServerInEditor.bat (РЕКОМЕНДУЕТСЯ)

### Шаг 1: Проверьте, что Gateway запущен

```powershell
docker-compose ps realtime-gateway
```

Если не запущен:
```powershell
docker-compose up -d realtime-gateway
```

### Шаг 2: Запустите Dedicated Server

Просто запустите скрипт:
```powershell
cd client\UE5\NECPGAME
.\RunServerInEditor.bat
```

Это откроет UE5 Editor в режиме Dedicated Server с автоматическим подключением к Gateway.

### Шаг 3: Проверьте подключение

В логах UE5 Editor (Output Log) должно появиться:
```
LyraServerGatewayConnection: Connected to gateway at 127.0.0.1:18080
```

В логах Gateway (через docker-compose) должно появиться:
```
server connected to /server endpoint
```

## Вариант 2: Через PIE в редакторе (если нужен GUI)

1. Откройте проект `NECPGAME.uproject` в UE5 Editor
2. Нажмите на стрелку рядом с кнопкой **Play**
3. Выберите:
   - **Number of Players**: 2 или больше
   - **Net Mode**: **Play As Listen Server** (или Dedicated Server)
4. Нажмите **Play**
5. В окне PIE управляйте персонажем

## Вариант 3: Собранный исполняемый файл (для production)

Требует сборки сервера:

```powershell
cd client\UE5\NECPGAME
# Собрать сервер (после исправления ошибок компиляции)
# BuildServer.bat (если есть) или через UAT

# Запустить собранный сервер
.\Binaries\Win64\NECPGAMEServer.exe /Game/ShooterMaps/Maps/L_Expanse.L_Expanse?listen -server -port=7777 -WebSocketGateway=127.0.0.1:18080 -log
```

## Проверка работы

После запуска сервера проверьте:

1. **Логи UE5 Editor/Server** должны показывать:
   ```
   LyraServerGatewayConnection: Connected to gateway at 127.0.0.1:18080
   ```

2. **Метрики Gateway** (http://localhost:9093/metrics):
   ```
   active_server_connection 1
   ```

3. **Запустить нагрузочный тест**:
   ```powershell
   cd services\realtime-gateway-go
   go build -o loadtest.exe ./cmd/loadtest
   .\loadtest.exe -url "ws://127.0.0.1:18080/ws?token=test" -clients 10 -duration 60s
   ```

   Теперь должно появиться:
   ```
   GameState Received: X (msg/s)
   ```

## Если сервер не подключается к Gateway

1. Проверьте, что Gateway запущен: `docker-compose ps realtime-gateway`
2. Проверьте логи Gateway: `docker-compose logs realtime-gateway`
3. Проверьте конфигурацию в `Config/DefaultServer.ini`:
   ```
   [WebSocketGateway]
   GatewayAddress=127.0.0.1
   GatewayPort=18080
   ```
4. Проверьте, что порт 18080 доступен: `Test-NetConnection localhost -Port 18080`

## Следующие шаги

После запуска сервера сообщите **"сервер запущен"** и я запущу нагрузочный тест с полным циклом синхронизации (PlayerInput → Server → GameState → Clients)!

