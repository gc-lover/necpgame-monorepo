# Тестирование UE5 Dedicated Server

## Быстрый способ: PIE (Play In Editor)

PIE позволяет запустить сервер прямо в редакторе без сборки отдельного исполняемого файла.

### Шаги:

1. **Убедитесь, что Go Gateway запущен:**
   ```bash
   docker-compose ps realtime-gateway
   ```

2. **Откройте UE5 Editor** и загрузите проект `NECPGAME.uproject`

3. **Настройте PIE для запуска сервера:**
   - В редакторе нажмите на стрелку рядом с кнопкой **Play**
   - Выберите **Number of Players: 2** (или больше)
   - Выберите **Net Mode: Play As Listen Server** или **Play As Dedicated Server**
   - Нажмите **Play**

4. **Проверьте логи:**
   - В окне **Output Log** ищите сообщение:
     ```
     LyraServerGatewayConnection: Connected to gateway at 127.0.0.1:18080
     ```
   - В логах Docker Gateway должны появиться сообщения о подключении сервера:
     ```bash
     docker-compose logs -f realtime-gateway
     ```

5. **Тестирование:**
   - В окне PIE управляйте персонажем
   - PlayerInput должен отправляться через Gateway на Dedicated Server
   - Проверьте логи сервера на обработку PlayerInput

### Альтернатива: Запуск отдельного Dedicated Server

Если нужно протестировать отдельный процесс сервера:

1. **Соберите сервер** (после исправления ошибок компиляции):
   ```batch
   cd client\UE5\NECPGAME
   BuildServer.bat
   ```

2. **Запустите сервер:**
   ```batch
   scripts\run\ue5-server.cmd
   ```

3. **Запустите клиент отдельно** и подключитесь через WebSocket

## Проверка работы

### Логи Go Gateway должны показывать:
- Подключение Dedicated Server к `/server` endpoint
- Получение PlayerInput от клиентов
- Маршрутизацию сообщений между клиентами и сервером

### Логи UE5 Dedicated Server должны показывать:
- Подключение к Gateway
- Получение PlayerInput через `OnPlayerInputReceived`
- Обработку игровой логики

## Отладка

Если сервер не подключается к Gateway:
1. Проверьте, что Gateway запущен: `docker-compose ps realtime-gateway`
2. Проверьте адрес и порт в `DefaultServer.ini`
3. Проверьте логи Gateway на наличие ошибок подключения



