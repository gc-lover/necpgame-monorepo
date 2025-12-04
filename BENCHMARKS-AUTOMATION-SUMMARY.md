# 📊 Benchmarks Automation - Summary

**Краткое описание автоматизации бенчмарков**

---

## ✅ Что настроено

### 1. **Pre-commit hook** (только измененные!)

**Запускается:** При `git commit`

**Что делает:**
- ✅ Определяет измененные сервисы из staged files
- ✅ Запускает `bench-quick` ТОЛЬКО для измененных
- ✅ Только если есть `handlers_bench_test.go`
- ✅ Не блокирует коммит (continue-on-error)
- ✅ Билд проходит даже если бенчмарков нет

**Пример:**
```bash
# Изменил только loot-service-go
git add services/loot-service-go/server/handlers.go
git commit -m "feat: update handler"
# → Запустится bench-quick ТОЛЬКО для loot-service-go
```

---

### 2. **Makefile targets** (во всех сервисах!)

**Добавлено в 81 Makefile автоматически:**

```makefile
.PHONY: bench bench-json bench-quick

bench:
	go test -run=^$$ -bench=. -benchmem ./server

bench-json:
	@mkdir -p ../../.benchmarks/results
	go test -run=^$$ -bench=. -benchmem -json ./server > ../../.benchmarks/results/{service}_bench.json

bench-quick:
	go test -run=^$$ -bench=. -benchmem -benchtime=100ms ./server
```

**Проверка:**
```powershell
# Сколько Makefiles имеют bench-quick
Get-ChildItem services -Directory | Where-Object { 
    $makefile = Join-Path $_.FullName "Makefile"
    if (Test-Path $makefile) {
        (Get-Content $makefile -Raw) -match 'bench-quick:'
    }
} | Measure-Object | Select-Object -ExpandProperty Count
# Результат: 81
```

---

### 3. **GitHub Actions** (инкрементально!)

**Запускается:** При push/PR

**Что делает:**
- ✅ Определяет измененные сервисы (git diff)
- ✅ Запускает полные бенчмарки ТОЛЬКО для измененных
- ✅ Сохраняет результаты в `.benchmarks/results/`
- ✅ Коммитит результаты в репозиторий

---

## 🔧 Билд и бенчмарки

### ❌ Бенчмарки НЕ блокируют билд!

**По умолчанию:**
```makefile
build: generate-api
	go build -o service .
# ✅ Билд проходит даже если бенчмарков нет
```

**Если хочешь добавить бенчмарки в build (опционально):**
```makefile
build: generate-api bench-quick
	go build -o service .
# ⚠️ Билд будет ждать бенчмарки (но они не обязательны)
```

**Рекомендация:** НЕ добавляй в build по умолчанию, запускай отдельно:
```bash
make build        # Билд
make bench-quick  # Бенчмарки (опционально)
```

---

## 📋 Ответы на вопросы

### Q: Для всех или только измененных?

**A: ТОЛЬКО измененных!**

- Pre-commit: только измененные сервисы из staged files
- GitHub Actions: только измененные сервисы (git diff)
- Ручной запуск: `.\scripts\run-changed-benchmarks.ps1` - только измененные

### Q: Билд пройдет если бенчмарков нет?

**A: ДА!**

- Бенчмарки НЕ интегрированы в build по умолчанию
- Билд проходит независимо от бенчмарков
- Бенчмарки запускаются отдельно (опционально)

### Q: Почему только в одном Makefile правки?

**A: Нет, во всех 81 Makefile!**

Скрипт `add-bench-to-makefile.ps1` добавил bench targets во все Makefiles автоматически.

**Проверка:**
```powershell
# Найти Makefiles БЕЗ bench-quick
Get-ChildItem services -Directory | Where-Object { 
    $makefile = Join-Path $_.FullName "Makefile"
    if (Test-Path $makefile) {
        $content = Get-Content $makefile -Raw
        $content -notmatch 'bench-quick:'
    }
} | Select-Object Name
```

---

## 🎯 Workflow

### При разработке:

```bash
# 1. Изменил код
vim services/my-service-go/server/handlers.go

# 2. Коммит (бенчмарки запустятся автоматически ТОЛЬКО для my-service-go)
git add services/my-service-go/server/handlers.go
git commit -m "feat: update handler"
# → bench-quick для my-service-go (если есть handlers_bench_test.go)

# 3. Билд (проходит независимо)
cd services/my-service-go
make build  # ✅ Проходит даже без бенчмарков
```

### При push:

```bash
git push
# → CI запустит полные бенчмарки ТОЛЬКО для измененных сервисов
# → Результаты сохранятся в .benchmarks/results/
```

---

## ✅ Итог

- ✅ **Только измененные** сервисы (не все!)
- ✅ **Билд проходит** даже без бенчмарков
- ✅ **81 Makefile** обновлен (не один!)
- ✅ **Не блокирует** коммиты/билды
- ✅ **Опционально** - можно пропустить (`SKIP_BENCHMARKS=1`)

