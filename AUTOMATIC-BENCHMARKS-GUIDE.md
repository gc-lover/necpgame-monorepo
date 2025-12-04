# 🚀 Automatic Benchmarks Guide

**Автоматический запуск бенчмарков при разработке**

---

## 🎯 Как это работает

### 1. **При коммите (pre-commit hook)**

**Автоматически запускает быстрые бенчмарки** ТОЛЬКО для измененных сервисов:

```bash
# При git commit запускаются:
# ✅ ТОЛЬКО для измененных сервисов (из staged files)
# ✅ Только если есть handlers_bench_test.go
# ✅ Быстрые бенчмарки (benchtime=100ms)
# ✅ Не блокируют коммит (только предупреждение, continue-on-error)
# ✅ Билд проходит даже если бенчмарков нет
```

**Пропустить бенчмарки:**
```bash
SKIP_BENCHMARKS=1 git commit -m "message"
```

---

### 2. **При билде (Makefile)**

**Каждый сервис имеет bench targets (добавлены автоматически):**

```makefile
# Быстрые бенчмарки (для разработки)
make bench-quick

# Полные бенчмарки
make bench

# JSON output (для CI)
make bench-json
```

**Важно:** Бенчмарки НЕ интегрированы в build по умолчанию!
- Билд проходит даже если бенчмарков нет
- Бенчмарки запускаются отдельно (опционально)
- Можно добавить в build вручную (см. ниже)

---

### 3. **При push (GitHub Actions)**

**Автоматически запускаются бенчмарки:**
- Для измененных сервисов (инкрементально)
- При изменении кода в `server/`
- При изменении `*_bench_test.go`
- Результаты коммитятся в репозиторий

---

### 4. **Ручной запуск**

**Только измененные сервисы:**
```powershell
.\scripts\run-changed-benchmarks.ps1
```

**Все сервисы:**
```powershell
.\scripts\run-changed-benchmarks.ps1 -All
```

**Быстрые бенчмарки:**
```powershell
.\scripts\run-changed-benchmarks.ps1 -Quick
```

---

## 📋 Workflow для агентов

### Backend Agent

**После реализации handler:**
```bash
# 1. Бенчмарки запустятся автоматически при коммите
git add services/my-service-go/server/handlers.go
git commit -m "feat: implement handler"

# 2. Или вручную перед коммитом
cd services/my-service-go
make bench-quick
```

**Результаты:**
- Видны в pre-commit hook
- Сохраняются в `.benchmarks/results/` (через CI)
- Доступны в Grafana (после экспорта)

---

### Performance Engineer

**После оптимизации:**
```bash
# 1. Запустить бенчмарки
make bench

# 2. Сравнить с предыдущими
.\scripts\view-benchmark-history.ps1
# Выбрать "compare"

# 3. Экспортировать в Prometheus
.\scripts\export-benchmarks-to-prometheus.ps1 -UseFile
```

---

## 🔧 Настройка

### Добавить bench target в Makefile

**Автоматически (для всех сервисов):**
```powershell
.\scripts\add-bench-to-makefile.ps1
```

**Вручную:**
```makefile
.PHONY: bench bench-json bench-quick

bench:
	go test -run=^$$ -bench=. -benchmem ./server

bench-json:
	@mkdir -p ../../.benchmarks/results
	go test -run=^$$ -bench=. -benchmem -json ./server > ../../.benchmarks/results/my-service_bench.json

bench-quick:
	go test -run=^$$ -bench=. -benchmem -benchtime=100ms ./server
```

---

### Интеграция в build процесс

**Опция 1: Всегда запускать (рекомендуется для CI)**
```makefile
build: generate-api bench-quick
	go build -o service .
```

**Опция 2: Опционально (для локальной разработки)**
```makefile
build: generate-api
	go build -o service .

build-with-bench: build bench-quick
```

---

## 📊 Результаты

### Где смотреть:

1. **Локально:**
   ```powershell
   .\scripts\view-benchmark-history.ps1
   ```

2. **Grafana:**
   - http://localhost:3000 → Dashboards → Benchmarks History
   - После экспорта: `.\scripts\export-benchmarks-to-prometheus.ps1 -UseFile`

3. **GitHub Actions:**
   - Artifacts: `.benchmarks/` (90 дней)
   - Коммиты: результаты коммитятся в репозиторий

---

## ⚙️ Конфигурация

### Отключить автоматические бенчмарки

**В pre-commit:**
```bash
SKIP_BENCHMARKS=1 git commit -m "message"
```

**В Makefile:**
```makefile
# Убрать bench-quick из build target
build: generate-api
	go build -o service .
```

---

## 🎯 Best Practices

1. **Всегда запускай bench-quick перед коммитом**
   - Быстро (100ms)
   - Показывает регрессии сразу

2. **Полные бенчмарки в CI**
   - Автоматически при push
   - Сохраняются в историю

3. **Сравнивай результаты**
   - После оптимизаций
   - После рефакторинга
   - При подозрении на регрессию

---

## 📈 Примеры использования

### Backend Agent workflow:

```bash
# 1. Реализовал handler
vim services/my-service-go/server/handlers.go

# 2. Запустил быстрые бенчмарки
cd services/my-service-go
make bench-quick

# 3. Коммит (бенчмарки запустятся автоматически)
git add .
git commit -m "feat: implement handler"

# 4. Push (CI запустит полные бенчмарки)
git push
```

### Performance Engineer workflow:

```bash
# 1. Оптимизировал код
vim services/my-service-go/server/handlers.go

# 2. Запустил полные бенчмарки
make bench

# 3. Сравнил с предыдущими
cd ../..
.\scripts\view-benchmark-history.ps1
# Выбрать "compare"

# 4. Экспортировал в Grafana
.\scripts\export-benchmarks-to-prometheus.ps1 -UseFile
```

---

**См. также:**
- `BENCHMARK-DASHBOARD-GUIDE.md` - полная документация
- `BENCHMARK-DASHBOARD-QUICK-START.md` - быстрый старт

