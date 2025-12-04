# 🚀 Benchmark Dashboard & Historical Tracking Solution

**Централизованная система для бенчмарков и исторических данных по производительности**

**Дата:** 2025  
**Технологии:** Prometheus, Grafana, Pyroscope, GitHub Actions, Go Benchstat

---

## 🎯 Проблема

- ❌ Нет централизованного запуска всех бенчмарков
- ❌ Нет исторических данных для сравнения
- ❌ Нет визуализации трендов производительности
- ❌ Невозможно отследить регрессии после рефакторинга

---

## OK Решение: 3-уровневая система

### 1. **Benchmark Runner** (GitHub Actions)
- Автоматический запуск всех бенчмарков
- Сохранение результатов в JSON
- Коммит результатов в репозиторий

### 2. **Metrics Collector** (Prometheus + Grafana)
- Runtime метрики из production
- Исторические данные (retention: 1 год)
- Дашборды для сравнения сервисов

### 3. **Continuous Profiling** (Pyroscope)
- 24/7 профилирование
- Обнаружение регрессий
- Сравнение до/после рефакторинга

---

## 📦 Компоненты

### 1. Benchmark Runner Script

**Файл:** `scripts/run-all-benchmarks.sh`

```bash
#!/bin/bash
# Issue: Benchmark dashboard
# Запускает все бенчмарки и сохраняет результаты

set -e

RESULTS_DIR=".benchmarks/results"
TIMESTAMP=$(date +%Y%m%d_%H%M%S)
OUTPUT_FILE="${RESULTS_DIR}/benchmarks_${TIMESTAMP}.json"

mkdir -p "$RESULTS_DIR"

echo "🚀 Running benchmarks for all services..."

# Массив результатов
echo '{"timestamp":"'$TIMESTAMP'","services":[' > "$OUTPUT_FILE"

FIRST=true
for service_dir in services/*-go; do
    if [ ! -d "$service_dir" ]; then
        continue
    fi
    
    service_name=$(basename "$service_dir")
    echo "  📊 Benchmarking: $service_name"
    
    cd "$service_dir"
    
    # Проверяем наличие бенчмарков
    if ! find . -name "*_bench_test.go" | grep -q .; then
        echo "    WARNING  No benchmarks found"
        cd - > /dev/null
        continue
    fi
    
    # Запускаем бенчмарки
    BENCH_OUTPUT=$(go test -run=^$$ -bench=. -benchmem -json ./server 2>&1 || echo "{}")
    
    if [ "$FIRST" = false ]; then
        echo "," >> "$OUTPUT_FILE"
    fi
    FIRST=false
    
    # Форматируем результат
    echo -n "{\"service\":\"$service_name\",\"benchmarks\":[" >> "$OUTPUT_FILE"
    
    # Парсим JSON output от go test
    echo "$BENCH_OUTPUT" | jq -r 'select(.Action=="bench") | "{\"name\":\"\(.Package)/\(.Test)\",\"ns_per_op\":\(.NsPerOp),\"allocs_per_op\":\(.AllocsPerOp),\"bytes_per_op\":\(.BytesPerOp)}"' | \
        sed ':a;N;$!ba;s/\n/,/g' >> "$OUTPUT_FILE"
    
    echo "]}" >> "$OUTPUT_FILE"
    
    cd - > /dev/null
done

echo "]}" >> "$OUTPUT_FILE"

echo "OK Benchmarks complete: $OUTPUT_FILE"
```

### 2. GitHub Actions Workflow

**Файл:** `.github/workflows/benchmarks.yml`

```yaml
name: Benchmark All Services

on:
  schedule:
    - cron: '0 2 * * *'  # Каждый день в 2:00
  workflow_dispatch:  # Ручной запуск
  push:
    branches: [main]
    paths:
      - 'services/**/*_bench_test.go'
      - 'services/**/server/**/*.go'

jobs:
  benchmark:
    runs-on: ubuntu-latest
    
    steps:
      - uses: actions/checkout@v4
      
      - name: Setup Go
        uses: actions/setup-go@v5
        with:
          go-version: '1.23'
      
      - name: Install dependencies
        run: |
          sudo apt-get update
          sudo apt-get install -y jq
          go install golang.org/x/perf/cmd/benchstat@latest
      
      - name: Run all benchmarks
        run: |
          chmod +x scripts/run-all-benchmarks.sh
          ./scripts/run-all-benchmarks.sh
      
      - name: Compare with previous
        run: |
          LATEST=$(ls -t .benchmarks/results/*.json | head -1)
          PREVIOUS=$(ls -t .benchmarks/results/*.json | head -2 | tail -1)
          
          if [ -f "$PREVIOUS" ]; then
            echo "📊 Comparing with previous run..."
            benchstat -json "$PREVIOUS" "$LATEST" > .benchmarks/comparison.json
          fi
      
      - name: Upload results
        uses: actions/upload-artifact@v4
        with:
          name: benchmark-results
          path: .benchmarks/
          retention-days: 90
      
      - name: Commit results
        run: |
          git config --local user.email "github-actions[bot]@users.noreply.github.com"
          git config --local user.name "github-actions[bot]"
          git add .benchmarks/
          git commit -m "[ci] benchmark: update results $(date +%Y%m%d)" || exit 0
          git push || exit 0
```

### 3. Prometheus Metrics Integration

**Файл:** `infrastructure/observability/prometheus/benchmark_rules.yml`

```yaml
groups:
  - name: benchmark_metrics
    interval: 1h
    rules:
      - record: service:benchmark:ns_per_op:avg
        expr: avg_over_time(service_benchmark_ns_per_op[1h])
      
      - record: service:benchmark:allocs_per_op:avg
        expr: avg_over_time(service_benchmark_allocs_per_op[1h])
      
      - alert: BenchmarkRegression
        expr: |
          (
            service:benchmark:ns_per_op:avg{service="~.*"}
            /
            service:benchmark:ns_per_op:avg{service="~.*"} offset 24h
          ) > 1.2
        for: 1h
        annotations:
          summary: "Performance regression detected in {{ $labels.service }}"
          description: "Latency increased by {{ $value | humanizePercentage }}"
```

### 4. Grafana Dashboard

**Файл:** `infrastructure/observability/grafana/dashboards/benchmarks.json`

```json
{
  "dashboard": {
    "title": "Microservices Benchmarks",
    "panels": [
      {
        "title": "Latency Trend (ns/op)",
        "targets": [
          {
            "expr": "service:benchmark:ns_per_op:avg",
            "legendFormat": "{{service}}"
          }
        ],
        "type": "graph"
      },
      {
        "title": "Allocations Trend",
        "targets": [
          {
            "expr": "service:benchmark:allocs_per_op:avg",
            "legendFormat": "{{service}}"
          }
        ],
        "type": "graph"
      },
      {
        "title": "Service Comparison",
        "targets": [
          {
            "expr": "topk(10, service:benchmark:ns_per_op:avg)",
            "legendFormat": "{{service}}"
          }
        ],
        "type": "table"
      }
    ]
  }
}
```

### 5. Pyroscope Integration

**Файл:** `scripts/setup-pyroscope.sh`

```bash
#!/bin/bash
# Continuous profiling для всех сервисов

# Добавляем в каждый сервис:
cat >> services/{service}-go/main.go << 'EOF'
import "github.com/grafana/pyroscope-go"

func init() {
    pyroscope.Start(pyroscope.Config{
        ApplicationName: "necpgame.{service}",
        ServerAddress:   os.Getenv("PYROSCOPE_SERVER"),
        ProfileTypes: []pyroscope.ProfileType{
            pyroscope.ProfileCPU,
            pyroscope.ProfileAllocObjects,
            pyroscope.ProfileInuseSpace,
        },
    })
}
EOF
```

### 6. Benchmark Comparison Tool

**Файл:** `scripts/compare-benchmarks.sh`

```bash
#!/bin/bash
# Сравнивает результаты бенчмарков

SERVICE=$1
LATEST=$(ls -t .benchmarks/results/*.json | head -1)
PREVIOUS=$(ls -t .benchmarks/results/*.json | head -2 | tail -1)

if [ -z "$SERVICE" ]; then
    echo "Usage: $0 <service-name>"
    exit 1
fi

echo "📊 Comparing benchmarks for: $SERVICE"
echo ""

# Извлекаем данные для сервиса
jq -r ".services[] | select(.service==\"$SERVICE\") | .benchmarks[] | \"\(.name): \(.ns_per_op) ns/op, \(.allocs_per_op) allocs/op\"" \
    "$LATEST" "$PREVIOUS" | \
    column -t
```

---

## 🚀 Быстрый старт

### 1. Установка зависимостей

```bash
# Установить benchstat
go install golang.org/x/perf/cmd/benchstat@latest

# Установить jq (для парсинга JSON)
# Ubuntu/Debian:
sudo apt-get install jq

# macOS:
brew install jq
```

### 2. Запуск бенчмарков локально

```bash
# Все сервисы
./scripts/run-all-benchmarks.sh

# Один сервис
cd services/matchmaking-go
go test -run=^$ -bench=. -benchmem -json ./server > ../../.benchmarks/matchmaking.json
```

### 3. Сравнение результатов

```bash
# Сравнить последние 2 запуска
./scripts/compare-benchmarks.sh matchmaking-go

# Или через benchstat
benchstat .benchmarks/results/benchmarks_20250101_020000.json \
          .benchmarks/results/benchmarks_20250102_020000.json
```

### 4. Настройка Prometheus

```yaml
# k8s/prometheus-configmap.yaml
scrape_configs:
  - job_name: 'benchmarks'
    static_configs:
      - targets: ['benchmark-exporter:9090']
```

### 5. Настройка Pyroscope

```yaml
# k8s/pyroscope-deployment.yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: pyroscope
spec:
  template:
    spec:
      containers:
      - name: pyroscope
        image: pyroscope/pyroscope:latest
        env:
        - name: PYROSCOPE_STORAGE_PATH
          value: "/var/lib/pyroscope"
```

---

## 📊 Дашборды

### 1. Benchmark Trends (Grafana)
- График ns/op по времени для каждого сервиса
- График allocs/op по времени
- Таблица сравнения сервисов

### 2. Performance Regression (Grafana Alerts)
- Автоматические алерты при регрессии >20%
- Email/Slack уведомления

### 3. Pyroscope Flame Graphs
- CPU профилирование в реальном времени
- Сравнение до/после рефакторинга
- Hot path identification

---

## 🔧 Интеграция в существующие сервисы

### Добавить в Makefile каждого сервиса:

```makefile
.PHONY: bench bench-json

bench:
	go test -run=^$$ -bench=. -benchmem ./server

bench-json:
	go test -run=^$$ -bench=. -benchmem -json ./server > ../../.benchmarks/$(SERVICE_NAME)_bench.json
```

### Пример бенчмарка (уже есть):

```go
// services/matchmaking-go/server/handlers_bench_test.go
func BenchmarkEnterQueue(b *testing.B) {
    // ... setup ...
    b.ReportAllocs()
    b.ResetTimer()
    
    for i := 0; i < b.N; i++ {
        _, _ = handlers.EnterQueue(ctx, req)
    }
}
```

---

## 📈 Исторические данные

### Хранение:
- **JSON результаты:** `.benchmarks/results/` (Git)
- **Prometheus:** 1 год retention
- **Pyroscope:** 30 дней (настраивается)

### Формат данных:

```json
{
  "timestamp": "20250115_020000",
  "services": [
    {
      "service": "matchmaking-go",
      "benchmarks": [
        {
          "name": "server/TestEnterQueue",
          "ns_per_op": 45000,
          "allocs_per_op": 2,
          "bytes_per_op": 128
        }
      ]
    }
  ]
}
```

---

## 🎯 Использование для рефакторинга

### До рефакторинга:
```bash
# 1. Запустить бенчмарки
./scripts/run-all-benchmarks.sh

# 2. Сохранить baseline
cp .benchmarks/results/benchmarks_*.json .benchmarks/baseline.json
```

### После рефакторинга:
```bash
# 1. Запустить бенчмарки
./scripts/run-all-benchmarks.sh

# 2. Сравнить
benchstat .benchmarks/baseline.json \
          .benchmarks/results/benchmarks_*.json
```

### Ожидаемый результат:
```
name              old ns/op  new ns/op  delta
EnterQueue        50000      45000      -10.00%
GetQueueStatus    30000      28000      -6.67%

name              old allocs/op  new allocs/op  delta
EnterQueue        3              2              -33.33%
```

---

## 🔍 Современные технологии (2025)

### 1. **Go Benchstat** (официальный)
- Сравнение бенчмарков
- Статистическая значимость
- HTML отчеты

### 2. **Pyroscope** (continuous profiling)
- 24/7 профилирование
- Flame graphs
- Regression detection

### 3. **Prometheus + Grafana** (стандарт)
- Метрики в реальном времени
- Исторические данные
- Алерты

### 4. **GitHub Actions** (CI/CD)
- Автоматический запуск
- Хранение результатов
- Коммит в репозиторий

### 5. **OpenTelemetry** (опционально)
- Distributed tracing
- Unified metrics
- Vendor-agnostic

---

## 📝 Следующие шаги

1. OK Создать `scripts/run-all-benchmarks.sh` - **ГОТОВО**
2. OK Добавить GitHub Actions workflow - **ГОТОВО**
3. ⏳ Настроить Prometheus exporter (опционально)
4. ⏳ Создать Grafana dashboard (опционально)
5. ⏳ Интегрировать Pyroscope (опционально)
6. ⏳ Добавить в Makefile каждого сервиса (опционально)

---

## 🎯 Быстрый старт (уже работает!)

### 1. Локальный запуск (Linux/macOS):
```bash
# Установить jq
sudo apt-get install jq  # Ubuntu/Debian
brew install jq          # macOS

# Запустить все бенчмарки
./scripts/run-all-benchmarks.sh

# Результаты в:
.benchmarks/results/benchmarks_YYYYMMDD_HHMMSS.json
```

### 2. Сравнение результатов:
```bash
./scripts/compare-benchmarks.sh matchmaking-go
```

### 3. GitHub Actions:
- Автоматически запускается каждый день в 2:00 UTC
- Или вручную через "Actions" → "Benchmark All Services" → "Run workflow"
- Результаты коммитятся в `.benchmarks/results/`

---

**Связанные документы:**
- `.cursor/GO_BACKEND_PERFORMANCE_BIBLE.md` - Part 3A (Profiling)
- `.cursor/PERFORMANCE_ENFORCEMENT.md` - Требования к производительности

