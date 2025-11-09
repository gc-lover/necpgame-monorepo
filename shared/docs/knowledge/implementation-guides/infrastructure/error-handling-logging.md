# Error Handling & Logging - Обработка ошибок и логирование

**Статус:** draft  
**Версия:** 1.0.0  
**Дата создания:** 2025-11-06  
**Последнее обновление:** 2025-11-07 (обновлено для микросервисов)  
**Приоритет:** критический (Production)

**api-readiness:** in-review  
**api-readiness-check-date:** 2025-11-07

---

## Краткое описание

Централизованная система обработки ошибок и логирования для микросервисной архитектуры.

**Микрофича:** Error handling, logging, monitoring, alerting

---

## Микросервисная архитектура

### Centralized Logging (Планируется - ELK Stack)

**Проблема:** Логи разбросаны по 6+ микросервисам  
**Решение:** Централизованное хранилище логов

**Stack:**
```
Микросервисы
  ↓ (Logstash)
Elasticsearch (хранилище)
  ↓
Kibana (визуализация, поиск)
```

**Каждый микросервис отправляет логи:**
```
auth-service (8081) → Logstash → Elasticsearch
character-service (8082) → Logstash → Elasticsearch
gameplay-service (8083) → Logstash → Elasticsearch
social-service (8084) → Logstash → Elasticsearch
economy-service (8085) → Logstash → Elasticsearch
world-service (8086) → Logstash → Elasticsearch
```

### Distributed Tracing (Zipkin/Jaeger)

**Проблема:** Запрос проходит через несколько сервисов  
**Решение:** Trace ID для отслеживания

**Пример:**
```
Client → API Gateway (trace_id: abc-123)
  ↓
auth-service (trace_id: abc-123) validates token
  ↓
character-service (trace_id: abc-123) creates character
  ↓
economy-service (trace_id: abc-123) creates inventory
  ↓
Response (trace_id: abc-123)
```

**Все логи с одинаковым trace_id = один запрос!**

---

## 📝 Logging Levels

```
TRACE: Детальная отладка (development only)
DEBUG: Отладочная информация
INFO: Обычные события (player login, quest complete)
WARN: Предупреждения (slow query, deprecated API)
ERROR: Ошибки (failed request, exception)
FATAL: Критические ошибки (server crash, database down)
```

**Production logging:**
```
Only: INFO, WARN, ERROR, FATAL
DEBUG/TRACE: Disabled (performance)
```

---

## 🔍 Log Structure

**JSON format:**
```json
{
  "timestamp": "2025-11-06T23:00:00Z",
  "level": "ERROR",
  "service": "character-service",
  "trace_id": "abc-123-def",
  "player_id": "player-uuid",
  "message": "Failed to save character",
  "error": {
    "type": "DatabaseException",
    "message": "Connection timeout",
    "stack": "..."
  },
  "context": {
    "character_id": "char-uuid",
    "action": "update_inventory"
  }
}
```

---

## 🚨 Error Handling

### Client Errors (400s)

```
400 Bad Request: Invalid input
401 Unauthorized: Missing/invalid token
403 Forbidden: No permission
404 Not Found: Resource doesn't exist
429 Too Many Requests: Rate limit exceeded

Response:
{
  "error": {
    "code": "INVALID_INPUT",
    "message": "Character name must be 3-20 characters",
    "field": "name"
  }
}
```

### Server Errors (500s)

```
500 Internal Server Error: Unexpected error
502 Bad Gateway: Service down
503 Service Unavailable: Maintenance
504 Gateway Timeout: Slow response

Response:
{
  "error": {
    "code": "INTERNAL_ERROR",
    "message": "An unexpected error occurred. Please try again.",
    "trace_id": "abc-123" // For support
  }
}

Never expose internal details to client!
```

---

## 📊 Monitoring

**Metrics:**
```
Request rate: 1,234 req/s
Error rate: 0.5% (5 errors/1000 requests)
P50 latency: 45ms
P95 latency: 250ms
P99 latency: 850ms

Alerts:
⚠️ Error rate > 1% (alert)
🚨 Error rate > 5% (critical)
🚨 P95 latency > 500ms (slow)
```

**Tools:**
- Prometheus (metrics)
- Grafana (dashboards)
- Sentry (error tracking)
- Elastic Stack (log aggregation)

---

## 🔔 Alerting

```
Alert: Error rate spike
Condition: error_rate > 1% for 5 minutes
Actions:
- Slack notification
- Email to on-call engineer
- PagerDuty incident

Alert: Database down
Condition: database connection failed
Actions:
- CRITICAL alert
- Wake up on-call (phone call!)
- Failover to replica
```

---

## 🔗 Связанные документы

- `api-gateway-architecture.md`
- `database-architecture.md`

---

## История изменений

- v1.0.0 (2025-11-06 23:00) - Создание error handling системы

