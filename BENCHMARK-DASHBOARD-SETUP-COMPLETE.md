# OK Benchmark Dashboard - Setup Complete

**Что нужно сделать чтобы увидеть данные в дашборде**

---

## 🎯 Проблема

**Дашборд пустой потому что:**
1. ❌ Нет результатов бенчмарков
2. ❌ Метрики не экспортированы в Prometheus
3. ❌ HTTP сервер не запущен
4. ❌ Prometheus не видит метрики

---

## OK Решение (по шагам)

### Шаг 1: Создать тестовые данные

```powershell
.\scripts\setup-benchmark-dashboard.ps1
```

**Или вручную:**
```powershell
# Создать директорию
New-Item -ItemType Directory -Force -Path .benchmarks\results

# Создать тестовый результат
$json = '{"timestamp":"20250115_120000","services":[{"service":"loot-service-go","benchmarks":[{"name":"server/BenchmarkGetPlayerLootHistory","ns_per_op":200.2,"allocs_per_op":5,"bytes_per_op":320}]}]}'
$json | Out-File -FilePath ".benchmarks\results\benchmarks_20250115_120000.json" -Encoding UTF8
```

---

### Шаг 2: Экспортировать в Prometheus

```powershell
.\scripts\export-benchmarks-to-prometheus.ps1 -UseFile
```

**Проверка:**
```powershell
Test-Path .benchmarks\metrics.prom
Get-Content .benchmarks\metrics.prom | Select-Object -First 5
```

---

### Шаг 3: Запустить HTTP сервер

**В отдельном терминале (оставить запущенным):**
```powershell
.\scripts\benchmark-metrics-server.ps1
```

**Проверка:**
```powershell
Invoke-WebRequest http://localhost:9099/metrics
```

---

### Шаг 4: Перезапустить Prometheus

```powershell
docker-compose restart prometheus
```

**Проверка:**
1. Открой: http://localhost:9090/targets
2. Должен быть `benchmarks` job со статусом "UP"

---

### Шаг 5: Проверить метрики в Prometheus

1. Открой: http://localhost:9090
2. Введи запрос: `benchmark_ns_per_op`
3. Должны появиться метрики

---

### Шаг 6: Перезапустить Grafana

```powershell
docker-compose restart grafana
```

**Проверка:**
1. Открой: http://localhost:3000
2. Логин: `admin` / `admin`
3. Перейди: **Dashboards** → **Benchmarks History**
4. Должны появиться данные!

---

## 🔍 Быстрая проверка

```powershell
# Все компоненты
Write-Host "1. Results:" -ForegroundColor Yellow
Get-ChildItem .benchmarks\results\*.json -ErrorAction SilentlyContinue | Measure-Object | Select-Object -ExpandProperty Count

Write-Host "2. Metrics:" -ForegroundColor Yellow
Test-Path .benchmarks\metrics.prom

Write-Host "3. HTTP server:" -ForegroundColor Yellow
try { Invoke-WebRequest http://localhost:9099/metrics -TimeoutSec 2 | Out-Null; Write-Host "   OK" -ForegroundColor Green } catch { Write-Host "   ❌" -ForegroundColor Red }

Write-Host "4. Prometheus:" -ForegroundColor Yellow
try { $r = Invoke-WebRequest "http://localhost:9090/api/v1/query?query=benchmark_ns_per_op" -TimeoutSec 2 | ConvertFrom-Json; if ($r.data.result) { Write-Host "   OK Has data" -ForegroundColor Green } else { Write-Host "   ❌ No data" -ForegroundColor Red } } catch { Write-Host "   ❌ Can't connect" -ForegroundColor Red }
```

---

## 📊 Что должно отображаться

**В Grafana дашборде:**
- Таблица с результатами бенчмарков
- График ns/op по времени
- График allocs/op по времени

**Если пусто:**
- Проверь все шаги выше
- См. `BENCHMARK-DASHBOARD-TROUBLESHOOTING.md`

---

**См. также:**
- `BENCHMARK-DASHBOARD-QUICK-START.md` - быстрый старт
- `BENCHMARK-DASHBOARD-TROUBLESHOOTING.md` - решение проблем

