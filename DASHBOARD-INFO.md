# Benchmark Dashboard - Information

**Где находится информация на дашборде**

---

## Доступ к дашборду

**URL:** http://localhost:8080

**API Endpoints:**
- `GET /api/data` - Все данные бенчмарков
- `GET /api/runs` - Список всех запусков
- `GET /api/run/{timestamp}` - Данные конкретного запуска

---

## Текущие данные

**На дашборде отображается:**
- OK 2 сервиса с бенчмарками:
  - `loot-service-go` (BenchmarkGetPlayerLootHistory)
  - `quest-core-service-go` (BenchmarkGetQuest)

**Почему только 2 сервиса?**
- Только эти сервисы имеют выполненные бенчмарки
- Файлы результатов находятся в `.benchmarks/results/`
- Дашборд автоматически читает все JSON файлы из этой директории

---

## Как добавить данные

### Вариант 1: Запустить бенчмарки для всех сервисов

```bash
# Linux/macOS/Git Bash
./scripts/run-all-benchmarks.sh
```

### Вариант 2: Запустить для конкретного сервиса

```bash
cd services/loot-service-go
make bench-json
```

Или напрямую:
```bash
cd services/loot-service-go
go test -run=^$ -bench=. -benchmem -json ./server > ../../.benchmarks/results/loot-service_bench.json
```

### Вариант 3: При билде (автоматически)

При выполнении `make build`:
- Запускаются тесты
- Запускаются быстрые бенчмарки (`bench-quick`)
- Результаты сохраняются через pre-commit hook

---

## Формат данных

**Файлы результатов:** `.benchmarks/results/*.json`

**Структура:**
```json
{
  "timestamp": "20251204_230210",
  "services": [
    {
      "service": "loot-service-go",
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

## Что показывает дашборд

1. **Список сервисов** - все сервисы с бенчмарками
2. **Графики производительности** - изменения по времени
3. **Таблица результатов** - последние значения
4. **Фильтры** - по сервисам и бенчмаркам
5. **Статистика** - средние значения, тренды

---

## Автоматический сбор данных

**При билде сервисов:**
- `make build` → запускает `test bench-quick`
- Pre-commit hook → сохраняет результаты в `.benchmarks/results/pre-commit_*.json`
- GitHub Actions → запускает полные бенчмарки и сохраняет результаты

**Дашборд автоматически:**
- Читает все файлы из `.benchmarks/results/`
- Обновляет данные при перезагрузке страницы
- Показывает историю всех запусков

---

## Проверка данных

```bash
# Проверить файлы результатов
ls .benchmarks/results/

# Проверить API дашборда
curl http://localhost:8080/api/data

# Проверить список запусков
curl http://localhost:8080/api/runs
```

---

## Запуск дашборда

```bash
cd infrastructure/benchmark-dashboard
go run main.go
```

Дашборд запустится на http://localhost:8080

---

**Статус:** OK Дашборд работает
**Данные:** 2 сервиса (нужно запустить бенчмарки для остальных)
**Автоматизация:** OK При билде и pre-commit

