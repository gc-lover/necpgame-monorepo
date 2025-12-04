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

## Использование

### Локальный запуск:
```bash
./scripts/run-all-benchmarks.sh
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
- Доступны в Grafana (если настроен Prometheus)

---

**См. также:** `.cursor/BENCHMARK_DASHBOARD_SOLUTION.md`

