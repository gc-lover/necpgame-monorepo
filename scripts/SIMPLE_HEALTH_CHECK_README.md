# NECPGAME Simple Health Check Script

## Overview

Простой и надежный скрипт мониторинга здоровья для всех сервисов NECPGAME. Работает без внешних зависимостей, используя только стандартную библиотеку Python.

## Features

### ✅ Core Health Checks
- **TCP Port Connectivity**: Проверка доступности портов сервисов
- **HTTP API Responses**: Валидация health endpoints и основных API
- **Response Time Monitoring**: Измерение latency и производительности
- **Status Code Validation**: Проверка HTTP статусов (200, 4xx, 5xx)

### 📊 Reporting & Analytics
- **JSON Report Generation**: Структурированные отчеты для CI/CD
- **Health Status Classification**:
  - 🟢 **HEALTHY**: <500ms response time
  - 🟡 **WARNING**: 500-2000ms response time
  - 🔴 **CRITICAL**: >2000ms, timeouts, connection errors
- **Exit Codes**: Для интеграции с мониторингом (0=healthy, 1=critical, 2=warning)

### 🔧 Configuration
```bash
# Environment Variables
AUTH_SERVICE_HOST=localhost
AUTH_SERVICE_PORT=8080
ABILITY_SERVICE_HOST=localhost
ABILITY_SERVICE_PORT=8081
COMBAT_SERVICE_HOST=localhost
COMBAT_SERVICE_PORT=8084
ECONOMY_SERVICE_HOST=localhost
ECONOMY_SERVICE_PORT=8083
MATCHMAKING_SERVICE_HOST=localhost
MATCHMAKING_SERVICE_PORT=8082
```

## Usage

### Basic Health Check
```bash
python simple-health-check.py
```

**Output:**
```
NECPGAME Health Check Starting...
  Checking auth-service...
    [CRIT] critical
  Checking ability-service...
    [CRIT] critical

=== Health Check Summary ===
   Total Services: 5
   Healthy: 0
   Warning: 0
   Critical: 5
   Health Percentage: 0.0%
   Overall Status: CRITICAL

*** CRITICAL ISSUES FOUND ***

Report saved to: health_check_report.json
```

### Custom Output File
```bash
python simple-health-check.py --output /path/to/custom/report.json
```

### Quiet Mode (for CI/CD)
```bash
python simple-health-check.py --quiet
```

## Services Monitored

| Service | Default Port | Health Endpoints |
|---------|-------------|------------------|
| Auth Service | 8080 | `/health`, `/auth/health` |
| Ability Service | 8081 | `/health`, `/ability/health` |
| Combat Service | 8084 | `/health`, `/combat/health` |
| Economy Service | 8083 | `/health`, `/economy/health` |
| Matchmaking Service | 8082 | `/health`, `/matchmaking/health` |

## Report Format

### JSON Structure
```json
{
  "timestamp": "2024-01-10T12:00:00.000000",
  "check_duration_seconds": 5.23,
  "services": {
    "auth-service": {
      "service_name": "auth-service",
      "overall_status": "critical",
      "checks": [
        {
          "service_name": "auth-service",
          "endpoint": "http://localhost:8080/health",
          "status": "critical",
          "response_time_ms": 0.0,
          "status_code": null,
          "error_message": "Port not accessible",
          "timestamp": "2024-01-10T12:00:01.123456"
        }
      ],
      "uptime_percentage": 100.0,
      "total_checks": 0,
      "successful_checks": 0
    }
  },
  "summary": {
    "total_services": 5,
    "healthy_services": 0,
    "warning_services": 0,
    "critical_services": 5,
    "overall_status": "critical",
    "health_percentage": 0.0
  }
}
```

## Integration Examples

### CI/CD Pipeline (GitHub Actions)
```yaml
- name: Health Check
  run: |
    python scripts/simple-health-check.py --quiet
    exit_code=$?
    if [ $exit_code -eq 1 ]; then
      echo "Critical health issues found!"
      exit 1
    fi
```

### Docker Health Check
```dockerfile
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
  CMD python /app/scripts/simple-health-check.py --quiet || exit 1
```

### Cron Job Monitoring
```bash
# /etc/cron.d/necpgame-health
*/5 * * * * root cd /opt/necpgame && python scripts/simple-health-check.py --quiet && curl -X POST https://health-monitoring.example.com/webhook
```

### Nagios/Icinga Integration
```bash
#!/bin/bash
# check_necpgame_health.sh
python /opt/necpgame/scripts/simple-health-check.py --quiet >/dev/null 2>&1
exit $?
```

## Performance Characteristics

- **Execution Time**: <10 секунд для всех 5 сервисов
- **Memory Usage**: <10MB RAM
- **Network Usage**: Минимальный трафик
- **CPU Usage**: <1% загрузка процессора
- **Dependencies**: Только стандартная библиотека Python

## Error Handling

### Connection Errors
- **Timeout**: 10 секунд на запрос
- **DNS Resolution**: Graceful handling
- **SSL/TLS**: Поддержка HTTPS endpoints
- **Proxy Support**: Через environment variables

### Status Classification
- **Healthy**: HTTP 200, <500ms
- **Warning**: HTTP 200, 500-2000ms
- **Critical**: Connection errors, timeouts, HTTP 5xx

## Troubleshooting

### Common Issues

#### "Port not accessible"
```
Cause: Service not running or firewall blocking
Solution: Check service status, firewall rules, port bindings
```

#### "Timeout after 10s"
```
Cause: Service overloaded or network issues
Solution: Check service logs, network connectivity, load balancer
```

#### High Response Times
```
Cause: Database slow queries, high CPU usage
Solution: Check database performance, scale resources, optimize queries
```

### Debug Information
```bash
# Verbose output
python simple-health-check.py 2>&1 | tee health_debug.log

# Manual endpoint testing
curl -v http://localhost:8080/health
```

## Security Considerations

- **No Authentication**: Health checks are public endpoints
- **Information Disclosure**: Avoid exposing sensitive data in health responses
- **Rate Limiting**: Consider implementing rate limits on health endpoints
- **Network Security**: Run on internal networks, use VPN for remote access

---

**Этот скрипт предоставляет надежный foundation для production monitoring NECPGAME services с enterprise-grade reliability и zero external dependencies.**