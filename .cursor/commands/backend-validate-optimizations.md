# Backend: Validate Optimizations

**Команда:** `/backend-validate-optimizations #123`

**Когда использовать:** Перед передачей задачи следующему агенту (Network/QA)

## Описание

Проверяет что код содержит необходимые оптимизации согласно чек-листу.

## Алгоритм

### 1. Определить тип сервиса

```bash
# CRUD API vs Game Server
grep -r "game.*server\|realtime\|udp" services/{service}-go/

# Если найдено → Game Server (строгие требования)
# Если нет → CRUD API (базовые требования)
```

### 2. Автоматические проверки

```bash
cd services/{service}-go

# Struct alignment
fieldalignment ./... 2>&1 | tee alignment.log
# Если есть предложения → исправь

# Goroutine leaks
go test -v -run TestMain ./... 2>&1 | grep -i "leak"
# Не должно быть leaks

# Benchmarks
go test -bench=. -benchmem ./... > bench.log
# Проверь allocations в hot path

# Linting
golangci-lint run --enable=gocritic,gosec,errcheck
```

### 3. Проверка кода (grep паттерны)

**Базовые оптимизации:**

```bash
# Context timeouts
grep -r "context.WithTimeout\|context.WithDeadline" server/
# ДОЛЖНО быть в handlers для external calls

# DB pool settings
grep -r "SetMaxOpenConns\|SetMaxIdleConns" server/
# ДОЛЖНО быть в repository setup

# Structured logging
grep -r "fmt.Println\|log.Println" server/
# НЕ должно быть (используй structured logger)

# sync.Pool usage
grep -r "sync.Pool" server/
# Должно быть для hot path
```

**Для game servers:**

```bash
# Memory pooling
grep -r "sync.Pool" server/ | wc -l
# Должно быть >= 2 (минимум request/response pools)

# Batch operations
grep -r "Batch\|BatchGet\|BatchUpdate" server/repository.go
# Должно быть для DB queries

# Worker pool
grep -r "WorkerPool\|semaphore.*chan" server/
# Должно быть для ограничения горутин

# Spatial partitioning
grep -r "SpatialGrid\|Spatial.*Partition" server/
# Для >100 объектов
```

### 4. Проверка метрик (если сервис запущен)

```bash
# GC pause
curl http://localhost:6060/debug/pprof/heap > heap.prof
go tool pprof -top heap.prof | head -20

# Goroutine count
curl http://localhost:6060/debug/pprof/goroutine?debug=1 | grep "goroutine profile:"

# Allocations
curl http://localhost:6060/debug/pprof/allocs > allocs.prof
go tool pprof -alloc_space -top allocs.prof | head -20
```

### 5. Формирование отчета

**Если ВСЕ проверки OK:**

```markdown
✅ **Optimization validation passed**

**Automatic checks:**
- ✅ Struct alignment: optimized
- ✅ Goroutine leaks: none detected
- ✅ Benchmarks: 0 allocs/op in critical path
- ✅ Linting: no issues

**Code patterns:**
- ✅ Context timeouts: present
- ✅ DB pool: configured (25 connections)
- ✅ sync.Pool: used (3 pools)
- ✅ Batch operations: implemented

**Performance:**
- ✅ P99 latency: 8.5ms (target: <10ms)
- ✅ Throughput: 15k req/sec
- ✅ Memory: stable (no leaks)

**Service type:** Game Server
**Optimization level:** 3 (Game Servers)

Ready for handoff to Network Engineer.
```

**Если проблемы:**

```markdown
⚠️ **Optimization validation FAILED**

**Issues found:**

🔴 **BLOCKER (must fix):**
- No context timeouts in handlers (30 instances)
- DB connection pool not configured
- Goroutine leaks detected (5 leaking goroutines)

🟡 **WARNING (should fix):**
- No sync.Pool for response objects
- Struct alignment can be improved (save 40% memory)
- No batch DB operations (N+1 queries detected)

🟢 **OPTIONAL (consider):**
- Could use FlatBuffers for position updates
- Ring buffer for event processing

**Action:** Fix BLOCKER issues before handoff.
**Status:** Keep `Backend - In Progress`
```

## Чек-лист по типам сервисов

### CRUD API (базовый уровень):

- [ ] Context timeouts
- [ ] DB pool configured
- [ ] Structured logging
- [ ] No goroutine leaks
- [ ] Error handling

### Game Server (полный уровень):

Базовый + дополнительно:
- [ ] Memory pooling (sync.Pool)
- [ ] Batch operations
- [ ] Worker pool
- [ ] Spatial partitioning (>100 objects)
- [ ] Adaptive tick rate
- [ ] GC tuning
- [ ] Profiling enabled

## Автоматизация

**Создай скрипт:** `scripts/validate-backend-optimizations.sh`

```bash
#!/bin/bash
SERVICE_DIR=$1

cd "$SERVICE_DIR"

echo "🔍 Validating optimizations..."

# Struct alignment
echo "Checking struct alignment..."
fieldalignment ./... || echo "⚠️ Alignment issues found"

# Leaks
echo "Checking goroutine leaks..."
go test -v -run TestMain ./... 2>&1 | grep -i "leak" && echo "🔴 Leaks detected!"

# Benchmarks
echo "Running benchmarks..."
go test -bench=. -benchmem ./... | grep "allocs/op" | awk '{if ($5 > 0) print "⚠️ " $1 " has " $5 " allocs/op"}'

echo "✅ Validation complete"
```

**Использование:**

```bash
./scripts/validate-backend-optimizations.sh services/companion-service-go
```

## Интеграция в workflow

**Backend Agent перед передачей:**

1. Запусти `/backend-validate-optimizations #123`
2. Получи отчет
3. Если BLOCKER → исправь и повтори
4. Если OK → передавай задачу с отчетом в комментарии

## См. также

- `.cursor/BACKEND_OPTIMIZATION_CHECKLIST.md` - полный чек-лист
- `.cursor/BACKEND_CODE_TEMPLATES.md` - шаблоны кода с оптимизациями
- `.cursor/rules/agent-backend.mdc` - правила Backend агента

