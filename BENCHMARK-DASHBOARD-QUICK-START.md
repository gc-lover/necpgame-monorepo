# 🚀 Benchmark Dashboard - Quick Start

**Быстрый старт для дашборда бенчмарков**

---

## 📋 Шаги

### 1. Запустить бенчмарки

```powershell
# Через WSL или Git Bash
wsl bash scripts/run-all-benchmarks.sh
```

**Результаты:** `.benchmarks/results/benchmarks_YYYYMMDD_HHMMSS.json`

---

### 2. Экспортировать в Prometheus формат

```powershell
.\scripts\export-benchmarks-to-prometheus.ps1 -UseFile
```

**Создает:** `.benchmarks/metrics.prom`

---

### 3. Запустить HTTP сервер для метрик

```powershell
# В отдельном терминале
.\scripts\benchmark-metrics-server.ps1
```

**Сервер:** http://localhost:9099/metrics

---

### 4. Проверить Prometheus

1. Открой: http://localhost:9090
2. Проверь targets: http://localhost:9090/targets
3. Проверь метрики: http://localhost:9090/graph?g0.expr=benchmark_ns_per_op

---

### 5. Открыть Grafana

1. Открой: http://localhost:3000
2. Логин: `admin` / `admin`
3. Перейди: **Dashboards** → **Benchmarks History**
4. Увидишь графики производительности

---

## 🔄 Автоматизация

**После каждого запуска бенчмарков:**

```powershell
# 1. Экспорт
.\scripts\export-benchmarks-to-prometheus.ps1 -UseFile

# 2. HTTP сервер автоматически подхватит новый файл
# (просто перезагрузи страницу в Prometheus/Grafana)
```

---

## 📊 Что видно в Grafana

- **Benchmark Results Timeline** - таблица всех результатов
- **Latency Trend (ns/op)** - график производительности по времени
- **Allocations Trend** - график аллокаций по времени

---

## 🐛 Troubleshooting

**Проблема:** Дашборд пустой

**Решение:**
1. Проверь, что метрики экспортированы: `Test-Path .benchmarks/metrics.prom`
2. Проверь, что HTTP сервер запущен: `http://localhost:9099/metrics`
3. Проверь Prometheus targets: `http://localhost:9090/targets`
4. Проверь метрики: `http://localhost:9090/graph?g0.expr=benchmark_ns_per_op`

**Проблема:** Нет результатов

**Решение:**
1. Запусти бенчмарки: `wsl bash scripts/run-all-benchmarks.sh`
2. Проверь файлы: `Get-ChildItem .benchmarks\results\`

---

**См. также:** `BENCHMARK-DASHBOARD-GUIDE.md` - полная документация

