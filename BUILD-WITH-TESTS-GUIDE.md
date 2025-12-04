# Build with Tests and Benchmarks

**Обновление:** Теперь `make build` автоматически запускает тесты и бенчмарки

---

## Что изменилось

### До:
- `make build` только собирал бинарник
- Тесты и бенчмарки запускались отдельно
- Можно было собрать бинарник с багами

### После:
- `make build` запускает тесты перед сборкой
- `make build` запускает быстрые бенчмарки (bench-quick)
- Билд падает если тесты не проходят
- Бенчмарки не блокируют билд (continue-on-error)

---

## Структура Makefile

```makefile
.PHONY: test bench-quick build

# Run tests
test:
	@go test -v ./...

# Quick benchmark (short duration)
bench-quick:
	@if [ -f "server/handlers_bench_test.go" ] || find . -name "*_bench_test.go" | grep -q .; then \
		go test -run=^$$ -bench=. -benchmem -benchtime=100ms ./server; \
	fi

# Build (runs tests and benchmarks first)
build: test bench-quick
	@CGO_ENABLED=0 go build -ldflags="-w -s" -o bin/$(SERVICE_NAME) .
```

---

## Использование

### Локально:
```bash
cd services/my-service-go
make build
# → Запускает тесты
# → Запускает бенчмарки (если есть)
# → Собирает бинарник
```

### В CI/CD:
GitHub Actions автоматически использует `make build`, который запускает тесты и бенчмарки.

---

## Обновление всех сервисов

### Автоматически (PowerShell):
```powershell
.\scripts\update-build-with-tests.ps1
```

### Автоматически (Bash):
```bash
./scripts/add-tests-to-build.sh
```

### Вручную:
1. Добавить `test` и `bench-quick` в зависимости `build:`
2. Убедиться что `test:` и `bench-quick:` targets существуют

---

## Поведение

### Тесты:
- **Блокируют билд** - если тесты падают, билд не соберется
- Запускаются через `go test ./...`

### Бенчмарки:
- **Не блокируют билд** - если бенчмарки падают, билд продолжается
- Запускаются только если есть `*_bench_test.go` файлы
- Используют `benchtime=100ms` для скорости

---

## CI/CD Integration

В `.github/workflows/ci-backend.yml`:
- Использует `make build` если есть Makefile
- Иначе запускает тесты и бенчмарки вручную
- Билд падает только если тесты не проходят

---

**См. также:**
- `AUTOMATIC-BENCHMARKS-GUIDE.md` - полное руководство по бенчмаркам
- `scripts/update-build-with-tests.ps1` - скрипт обновления

