# Benchmark Results

**Исторические данные по бенчмаркам всех микросервисов**

---

## Структура

```
.benchmarks/
├── results/
│   ├── benchmarks_20250115_020000.json  # Результаты запуска
│   ├── benchmarks_20250116_020000.json
│   └── ...
└── comparison.json                      # Сравнение последних 2 запусков
```

---

## Формат данных

```json
{
  "timestamp": "20250115_020000",
  "services": [
    {
      "service": "matchmaking-go",
      "benchmarks": [
        {
          "name": "server/BenchmarkGetPlayerLootHistory",
          "ns_per_op": 200.2,
          "allocs_per_op": 5,
          "bytes_per_op": 320
        }
      ]
    }
  ]
}
```

---

## Использование

### Локальный запуск:
```bash
./scripts/run-all-benchmarks.sh
```

### Веб-дашборд:
```bash
cd infrastructure/benchmark-dashboard
make dev
# Открыть http://localhost:8080
```

### Сравнение результатов:
```bash
./scripts/compare-benchmarks.sh matchmaking-go
```

### Через benchstat:
```bash
benchstat .benchmarks/results/benchmarks_20250115_020000.json \
          .benchmarks/results/benchmarks_20250116_020000.json
```

---

## Автоматизация

**GitHub Actions** запускает бенчмарки:
- Каждый день в 2:00 UTC
- При изменении кода в `server/`
- При ручном запуске (workflow_dispatch)

**Результаты:**
- Коммитятся в репозиторий
- Сохраняются как artifacts (90 дней)
- Доступны через веб-дашборд на `http://localhost:8080` (локально) или через K8s deployment

---

**См. также:** 
- `.cursor/BENCHMARK_DASHBOARD_SOLUTION.md` - архитектура
- `infrastructure/benchmark-dashboard/README.md` - дашборд
