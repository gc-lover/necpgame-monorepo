# Быстрый старт: Поиск предела Gateway

## Что делает этот инструмент?

Автоматически находит максимальное количество клиентов, которое может обработать Gateway при частоте обновлений 60 Hz.

## Быстрый запуск

### 1. Убедитесь, что Gateway запущен

```powershell
docker-compose up -d realtime-gateway
docker-compose ps realtime-gateway
```

### 2. Запустите поиск предела

```powershell
.\scripts\testing\run-findlimit.ps1
```

### 3. Или с кастомными параметрами

```powershell
.\scripts\testing\run-findlimit.ps1 -StartClients 10 -MaxClients 200 -StepSize 20 -TestDurationSeconds 20 -CooldownSeconds 5
```

## Параметры по умолчанию

- **Starting clients**: 10
- **Maximum clients**: 500
- **Step size**: 20 (увеличивает на 20 каждый тест)
- **Test duration**: 20 секунд на каждый тест
- **PlayerInput Hz**: 60
- **Error threshold**: 1.0% (порог ошибок)
- **Cooldown**: 5 секунд между тестами

## Пример вывода

```
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
Testing with 10 clients...
OK PASSED: 10 clients - Error rate: 0.00%, Throughput: 600.00 msg/s
  Connections: 10 (failed: 0)
  PlayerInput sent: 12000 (600.00 msg/s)
  Errors: 0 (0.00%)
  Latency: avg=0.50 ms, min=0.10 ms, max=2.00 ms

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
Testing with 30 clients...
OK PASSED: 30 clients - Error rate: 0.00%, Throughput: 1800.00 msg/s

...

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
=== LIMIT SEARCH RESULTS ===

Maximum tested: 200 clients
Maximum successful: 180 clients

OK RECOMMENDED LIMIT: 180 clients
   (with error threshold: 1.00%)
```

## Что означает результат?

- **Maximum successful** - максимальное количество клиентов, при котором Gateway работает стабильно
- **Recommended limit** - рекомендуемый предел для production (на 10-20% меньше максимального)
- **Error rate** - процент ошибок при отправке сообщений (должен быть < 1%)
- **Throughput** - пропускная способность (msg/s) - должна расти линейно

## Советы

1. **Для быстрого теста**: Используйте `-StepSize 10` и `-TestDurationSeconds 15`
2. **Для точного результата**: Используйте `-StepSize 5` и `-TestDurationSeconds 30`
3. **Для строгих требований**: Используйте `-ErrorThreshold 0.5`
4. **Для мягких требований**: Используйте `-ErrorThreshold 5.0`

## Устранение неполадок

### Gateway не отвечает

```
❌ Gateway is not available at http://127.0.0.1:9093
```

**Решение**: Запустите Gateway:
```powershell
docker-compose up -d realtime-gateway
```

### Слишком много ошибок

**Возможные причины**:
- Gateway перегружен
- Недостаточно ресурсов

**Решение**: 
- Проверьте логи: `docker-compose logs realtime-gateway`
- Увеличьте `-CooldownSeconds` между тестами
- Проверьте метрики: `curl http://localhost:9093/metrics`

