# 🔍 Локальный стек мониторинга и анализа

**Полный анализ работы сервисов, Docker контейнеров и производительности**

**Дата:** 2025  
**Технологии:** Prometheus, Grafana, Pyroscope, Loki, Tempo

---

## 🎯 Что дает

- ✅ **Метрики в реальном времени** - CPU, память, latency, throughput
- ✅ **Исторические данные** - тренды за дни/недели/месяцы
- ✅ **Continuous profiling** - Pyroscope flame graphs
- ✅ **Логи** - централизованный сбор через Loki
- ✅ **Трассировка** - distributed tracing через Tempo
- ✅ **Автоматическое обнаружение** - все сервисы автоматически

---

## 🚀 Быстрый старт

### Windows (PowerShell):
```powershell
.\scripts\start-monitoring-stack.ps1
```

### Linux/macOS:
```bash
chmod +x scripts/start-monitoring-stack.sh
./scripts/start-monitoring-stack.sh
```

### Или через docker-compose:
```bash
docker-compose up -d prometheus grafana loki tempo pyroscope promtail
```

---

## 📊 Доступ к сервисам

После запуска доступны:

| Сервис | URL | Credentials |
|--------|-----|-------------|
| **Grafana** | http://localhost:3000 | admin / admin |
| **Prometheus** | http://localhost:9090 | - |
| **Pyroscope** | http://localhost:4040 | - |
| **Loki** | http://localhost:3100 | - |
| **Tempo** | http://localhost:3200 | - |

---

## 📈 Дашборды Grafana

### 1. Services Overview
**Путь:** Grafana → Dashboards → Microservices Overview

**Метрики:**
- Request Rate (req/sec)
- P99 Latency (ms)
- Error Rate (%)
- CPU Usage (%)
- Memory Usage (MB)
- Services Status

### 2. Создание своего дашборда

1. Открыть Grafana: http://localhost:3000
2. Login: admin / admin
3. Create → Dashboard → Add panel
4. Выбрать Prometheus как data source
5. Пример запроса:
   ```
   rate(http_requests_total[5m])
   ```

---

## 🔍 Анализ производительности

### 1. Prometheus Queries

**Request rate:**
```promql
sum(rate(http_requests_total[5m])) by (service)
```

**P99 latency:**
```promql
histogram_quantile(0.99, rate(http_request_duration_seconds_bucket[5m])) * 1000
```

**Error rate:**
```promql
rate(http_requests_total{status=~"5.."}[5m]) / rate(http_requests_total[5m]) * 100
```

**CPU usage:**
```promql
rate(container_cpu_usage_seconds_total[1m]) * 100
```

**Memory usage:**
```promql
container_memory_usage_bytes / 1024 / 1024
```

### 2. Pyroscope Profiling

**Flame graphs:**
1. Открыть http://localhost:4040
2. Выбрать application: `necpgame.{service-name}`
3. Выбрать time range
4. Смотреть flame graph

**Сравнение до/после рефакторинга:**
1. До: сделать snapshot
2. После: сравнить с snapshot

---

## 🐳 Docker контейнеры

### Метрики контейнеров

**Все контейнеры:**
```promql
container_cpu_usage_seconds_total
```

**По сервису:**
```promql
container_cpu_usage_seconds_total{name=~".*matchmaking.*"}
```

**Memory:**
```promql
container_memory_usage_bytes{name=~".*inventory.*"}
```

### Логи контейнеров

**В Grafana:**
1. Explore → Loki
2. Query: `{container_name=~".*matchmaking.*"}`
3. Смотреть логи в реальном времени

---

## 🔧 Настройка

### Добавить новый сервис в мониторинг

**1. Убедиться что сервис экспортирует метрики:**
```go
// main.go
metricsMux.Handle("/metrics", promhttp.Handler())
```

**2. Prometheus автоматически обнаружит через Docker labels**

**3. Или добавить вручную в `prometheus.yml`:**
```yaml
- job_name: 'my-service-go'
  static_configs:
    - targets: ['my-service:9090']
      labels:
        service: 'my-service-go'
  metrics_path: /metrics
```

### Настройка Pyroscope

**Добавить в сервис:**
```go
import "github.com/grafana/pyroscope-go"

func init() {
    pyroscope.Start(pyroscope.Config{
        ApplicationName: "necpgame.my-service",
        ServerAddress:   "http://pyroscope:4040",
        ProfileTypes: []pyroscope.ProfileType{
            pyroscope.ProfileCPU,
            pyroscope.ProfileAllocObjects,
            pyroscope.ProfileInuseSpace,
        },
    })
}
```

---

## 📝 Примеры использования

### 1. Найти медленный сервис

**В Prometheus:**
```promql
topk(10, histogram_quantile(0.99, rate(http_request_duration_seconds_bucket[5m])))
```

**В Grafana:**
- Dashboard → Services Overview
- Смотреть "P99 Latency" panel

### 2. Найти сервис с высоким CPU

**В Prometheus:**
```promql
topk(10, rate(container_cpu_usage_seconds_total[1m]) * 100)
```

**В Grafana:**
- Dashboard → Services Overview
- Смотреть "CPU Usage" panel

### 3. Анализ регрессии после рефакторинга

**1. До рефакторинга:**
- Сделать snapshot в Pyroscope
- Записать baseline метрики в Prometheus

**2. После рефакторинга:**
- Сравнить flame graphs в Pyroscope
- Сравнить метрики в Grafana (time range comparison)

### 4. Анализ логов

**В Grafana Explore:**
```
{container_name=~".*error.*"} |= "ERROR"
```

**По времени:**
```
{container_name=~".*matchmaking.*"} [5m]
```

---

## 🛠️ Troubleshooting

### Prometheus не собирает метрики

**Проверить:**
1. Сервис запущен: `docker ps`
2. Метрики доступны: `curl http://localhost:9090/metrics`
3. Prometheus targets: http://localhost:9090/targets

### Grafana не показывает данные

**Проверить:**
1. Data source настроен: Configuration → Data Sources → Prometheus
2. URL правильный: `http://prometheus:9090`
3. Тест connection: Test & Save

### Pyroscope пустой

**Проверить:**
1. Сервис отправляет данные: проверить логи
2. Application name правильный: `necpgame.{service}`
3. Server address: `http://pyroscope:4040`

---

## 📚 Дополнительные ресурсы

**Документация:**
- `.cursor/BENCHMARK_DASHBOARD_SOLUTION.md` - бенчмарки
- `.cursor/GO_BACKEND_PERFORMANCE_BIBLE.md` - оптимизации
- `.cursor/performance/03a-profiling-testing.md` - profiling

**Конфигурация:**
- `infrastructure/observability/prometheus/prometheus.yml`
- `infrastructure/observability/grafana/provisioning/`
- `docker-compose.yml` (prometheus, grafana, pyroscope секции)

---

## 🎯 Workflow для рефакторинга

1. **До рефакторинга:**
   ```bash
   # Запустить мониторинг
   ./scripts/start-monitoring-stack.sh
   
   # Записать baseline
   # В Prometheus: записать текущие метрики
   # В Pyroscope: сделать snapshot
   ```

2. **Во время рефакторинга:**
   - Смотреть метрики в реальном времени
   - Проверять логи на ошибки

3. **После рефакторинга:**
   - Сравнить метрики (Grafana time range comparison)
   - Сравнить flame graphs (Pyroscope diff view)
   - Проверить бенчмарки: `./scripts/run-all-benchmarks.sh`

---

**Готово! Теперь у тебя полный стек для анализа производительности локально! 🚀**

