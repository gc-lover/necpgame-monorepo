# 🔧 Benchmark Dashboard Troubleshooting

**Почему дашборд пустой и как это исправить**

---

## ❌ Проблема: Дашборд пустой

**Причина:** Нет данных в Prometheus

**Цепочка данных:**
```
Бенчмарки → JSON → Prometheus формат → HTTP сервер → Prometheus → Grafana
```

---

## OK Решение по шагам

### Шаг 1: Запустить бенчмарки

**Вариант A: Реальные бенчмарки (рекомендуется)**
```powershell
# Запустить для одного сервиса
cd services\loot-service-go
go test -run=^$ -bench=. -benchmem -benchtime=1s ./server

# Или через Makefile
make bench
```

**Вариант B: Тестовые данные (для проверки)**
```powershell
# Создать тестовый результат
$json = '{"timestamp":"20250115_120000","services":[{"service":"loot-service-go","benchmarks":[{"name":"server/BenchmarkGetPlayerLootHistory","ns_per_op":200.2,"allocs_per_op":5,"bytes_per_op":320}]}]}'
New-Item -ItemType Directory -Force -Path .benchmarks\results | Out-Null
$json | Out-File -FilePath ".benchmarks\results\benchmarks_20250115_120000.json" -Encoding UTF8
```

---

### Шаг 2: Экспортировать в Prometheus формат

```powershell
.\scripts\export-benchmarks-to-prometheus.ps1 -UseFile
```

**Проверка:**
```powershell
Test-Path .benchmarks\metrics.prom
Get-Content .benchmarks\metrics.prom | Select-Object -First 5
```

**Должно быть:**
```
# HELP benchmark_ns_per_op Benchmark nanoseconds per operation
# TYPE benchmark_ns_per_op gauge
benchmark_ns_per_op{service="loot-service-go",benchmark="BenchmarkGetPlayerLootHistory"} 200.2 1737028800
```

---

### Шаг 3: Запустить HTTP сервер для метрик

**В отдельном терминале:**
```powershell
.\scripts\benchmark-metrics-server.ps1
```

**Проверка:**
```powershell
# Должен быть доступен на http://localhost:9099/metrics
Invoke-WebRequest http://localhost:9099/metrics | Select-Object -ExpandProperty Content | Select-Object -First 5
```

---

### Шаг 4: Проверить Prometheus

1. Открой: http://localhost:9090
2. Проверь targets: http://localhost:9090/targets
   - Должен быть `benchmarks` job
   - Status должен быть "UP"
3. Проверь метрики: http://localhost:9090/graph?g0.expr=benchmark_ns_per_op
   - Должны появиться метрики

**Если targets не видно:**
```powershell
# Перезапустить Prometheus
docker-compose restart prometheus
```

---

### Шаг 5: Проверить Grafana

1. Открой: http://localhost:3000
2. Логин: `admin` / `admin`
3. Перейди: **Dashboards** → **Benchmarks History**
4. Если дашборд не виден:
   ```powershell
   # Перезапустить Grafana
   docker-compose restart grafana
   ```

---

## 🔍 Диагностика

### Проверка всех компонентов:

```powershell
# 1. Результаты бенчмарков
Get-ChildItem .benchmarks\results\*.json

# 2. Prometheus метрики
Test-Path .benchmarks\metrics.prom

# 3. HTTP сервер
Invoke-WebRequest http://localhost:9099/metrics -ErrorAction SilentlyContinue

# 4. Prometheus targets
Invoke-WebRequest http://localhost:9090/api/v1/targets -ErrorAction SilentlyContinue | ConvertFrom-Json | Select-Object -ExpandProperty data | Where-Object { $_.activeTargets.job -eq "benchmarks" }

# 5. Prometheus метрики
Invoke-WebRequest "http://localhost:9090/api/v1/query?query=benchmark_ns_per_op" -ErrorAction SilentlyContinue | ConvertFrom-Json | Select-Object -ExpandProperty data
```

---

## 🐛 Частые проблемы

### Проблема 1: "No data"

**Причина:** Нет результатов бенчмарков

**Решение:**
```powershell
# Запустить бенчмарки
cd services\loot-service-go
make bench-quick
```

---

### Проблема 2: "metrics.prom not found"

**Причина:** Не экспортированы метрики

**Решение:**
```powershell
.\scripts\export-benchmarks-to-prometheus.ps1 -UseFile
```

---

### Проблема 3: "HTTP server not running"

**Причина:** Сервер не запущен

**Решение:**
```powershell
# Запустить в отдельном терминале
.\scripts\benchmark-metrics-server.ps1
```

---

### Проблема 4: "Prometheus can't scrape"

**Причина:** Неправильный путь в prometheus.yml

**Решение:**
1. Проверить `infrastructure/observability/prometheus/prometheus.yml`:
   ```yaml
   - job_name: 'benchmarks'
     static_configs:
       - targets: ['host.docker.internal:9099']
   ```

2. Перезапустить Prometheus:
   ```powershell
   docker-compose restart prometheus
   ```

---

### Проблема 5: "Dashboard not visible"

**Причина:** Неправильный путь в dashboards.yml

**Решение:**
1. Проверить `infrastructure/observability/grafana/provisioning/dashboards/dashboards.yml`:
   ```yaml
   options:
     path: /var/lib/grafana/dashboards  # Должно быть так
   ```

2. Перезапустить Grafana:
   ```powershell
   docker-compose restart grafana
   ```

---

## OK Быстрая проверка

```powershell
# Все в одном скрипте
Write-Host "1. Results:" -ForegroundColor Yellow
Get-ChildItem .benchmarks\results\*.json -ErrorAction SilentlyContinue | Measure-Object | Select-Object -ExpandProperty Count

Write-Host "2. Metrics file:" -ForegroundColor Yellow
Test-Path .benchmarks\metrics.prom

Write-Host "3. HTTP server:" -ForegroundColor Yellow
try { Invoke-WebRequest http://localhost:9099/metrics -TimeoutSec 2 | Out-Null; Write-Host "   OK Running" -ForegroundColor Green } catch { Write-Host "   ❌ Not running" -ForegroundColor Red }

Write-Host "4. Prometheus:" -ForegroundColor Yellow
try { $result = Invoke-WebRequest "http://localhost:9090/api/v1/query?query=benchmark_ns_per_op" -TimeoutSec 2 | ConvertFrom-Json; if ($result.data.result) { Write-Host "   OK Has metrics" -ForegroundColor Green } else { Write-Host "   ❌ No metrics" -ForegroundColor Red } } catch { Write-Host "   ❌ Can't connect" -ForegroundColor Red }
```

---

**См. также:**
- `BENCHMARK-DASHBOARD-QUICK-START.md` - быстрый старт
- `BENCHMARK-DASHBOARD-GUIDE.md` - полная документация

