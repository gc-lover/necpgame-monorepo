# 📊 Benchmark Dashboard Guide

**Как использовать дашборд бенчмарков**

---

## 🚀 Быстрый старт

### 1. Запустить бенчмарки

**Windows (PowerShell):**
```powershell
# Запустить все бенчмарки
cd C:\NECPGAME
.\scripts\run-all-benchmarks.ps1  # Если есть, или используй bash версию через WSL
```

**Linux/macOS:**
```bash
./scripts/run-all-benchmarks.sh
```

**Результаты сохраняются в:** `.benchmarks/results/benchmarks_YYYYMMDD_HHMMSS.json`

---

### 2. Просмотр результатов

**PowerShell:**
```powershell
.\scripts\view-benchmark-history.ps1
```

**Что можно:**
- Просмотреть конкретный запуск
- Сравнить последние 2 запуска
- Увидеть изменения в производительности

---

### 3. Экспорт в Prometheus (для Grafana)

**Вариант 1: Через файл (рекомендуется)**
```powershell
.\scripts\export-benchmarks-to-prometheus.ps1 -UseFile
```

**Вариант 2: Через Pushgateway**
```powershell
# Сначала запусти Pushgateway (если есть в docker-compose)
.\scripts\export-benchmarks-to-prometheus.ps1 -PushgatewayUrl "http://localhost:9091"
```

---

### 4. Просмотр в Grafana

1. Открой Grafana: http://localhost:3000
2. Логин: `admin` / `admin`
3. Перейди в **Dashboards** → **Benchmarks History**
4. Увидишь графики производительности

---

## 📋 Текущее состояние

### ✅ Что работает:

- ✅ **Запуск бенчмарков:** `scripts/run-all-benchmarks.sh`
- ✅ **Просмотр истории:** `scripts/view-benchmark-history.ps1`
- ✅ **Grafana дашборд:** `infrastructure/observability/grafana/dashboards/benchmarks-history.json`
- ✅ **Экспорт в Prometheus:** `scripts/export-benchmarks-to-prometheus.ps1` (новый!)

### ⚠️ Что нужно настроить:

1. **Prometheus file-based scraping:**
   - Добавить в `prometheus.yml`:
   ```yaml
   scrape_configs:
     - job_name: 'benchmarks'
       file_sd_configs:
         - files:
           - '.benchmarks/metrics.prom'
   ```

2. **Или Pushgateway:**
   - Добавить Pushgateway в `docker-compose.yml`
   - Настроить Prometheus для scraping Pushgateway

---

## 🔄 Автоматизация

### GitHub Actions

**Workflow:** `.github/workflows/benchmarks.yml`

**Запускается:**
- Каждый день в 2:00 UTC
- При изменении кода в `server/`
- При ручном запуске

**Что делает:**
1. Запускает все бенчмарки
2. Сохраняет результаты в `.benchmarks/results/`
3. Коммитит результаты в репозиторий
4. Создает artifacts (90 дней)

---

## 📊 Формат данных

**JSON структура:**
```json
{
  "timestamp": "20250115_020000",
  "services": [
    {
      "service": "loot-service-go",
      "benchmarks": [
        {
          "name": "server/BenchmarkGetPlayerLootHistory",
          "ns_per_op": 207.0,
          "allocs_per_op": 5,
          "bytes_per_op": 320
        }
      ]
    }
  ]
}
```

**Prometheus метрики:**
```
benchmark_ns_per_op{service="loot-service-go",benchmark="BenchmarkGetPlayerLootHistory"} 207.0
benchmark_allocs_per_op{service="loot-service-go",benchmark="BenchmarkGetPlayerLootHistory"} 5
benchmark_bytes_per_op{service="loot-service-go",benchmark="BenchmarkGetPlayerLootHistory"} 320
```

---

## 🎯 Использование

### Локальный запуск:
```powershell
# 1. Запустить бенчмарки
.\scripts\run-all-benchmarks.sh  # или через WSL

# 2. Экспортировать в Prometheus
.\scripts\export-benchmarks-to-prometheus.ps1 -UseFile

# 3. Перезапустить Prometheus (если нужно)
docker-compose restart prometheus

# 4. Открыть Grafana
# http://localhost:3000 → Dashboards → Benchmarks History
```

### Сравнение результатов:
```powershell
.\scripts\view-benchmark-history.ps1
# Выбери "compare" для сравнения последних 2 запусков
```

---

## 🔧 Настройка Prometheus

**Добавить в `infrastructure/observability/prometheus/prometheus.yml`:**

```yaml
scrape_configs:
  # ... existing configs ...
  
  - job_name: 'benchmarks'
    file_sd_configs:
      - files:
        - '/benchmarks/metrics.prom'
    scrape_interval: 1m
    metrics_path: '/metrics'
```

**Или через volume в docker-compose:**
```yaml
prometheus:
  volumes:
    - ./infrastructure/observability/prometheus:/etc/prometheus
    - ./.benchmarks:/benchmarks:ro  # Добавить эту строку
```

---

## 📈 Grafana Dashboard

**Панели:**
1. **Benchmark Results Timeline** - таблица всех результатов
2. **Latency Trend (ns/op)** - график производительности
3. **Allocations Trend** - график аллокаций

**Запросы:**
```promql
# Все бенчмарки
benchmark_ns_per_op

# Конкретный сервис
benchmark_ns_per_op{service="loot-service-go"}

# Конкретный бенчмарк
benchmark_ns_per_op{service="loot-service-go",benchmark="BenchmarkGetPlayerLootHistory"}
```

---

## 🐛 Troubleshooting

**Проблема:** Дашборд пустой в Grafana

**Решение:**
1. Проверь, что метрики экспортированы: `Test-Path .benchmarks/metrics.prom`
2. Проверь Prometheus targets: http://localhost:9090/targets
3. Проверь метрики: http://localhost:9090/graph?g0.expr=benchmark_ns_per_op

**Проблема:** Нет результатов бенчмарков

**Решение:**
1. Запусти бенчмарки: `.\scripts\run-all-benchmarks.sh`
2. Проверь файлы: `Get-ChildItem .benchmarks\results\`

---

**См. также:**
- `.cursor/BENCHMARK_DASHBOARD_SOLUTION.md` - полное решение
- `.benchmarks/README.md` - структура данных

