# 🔍 Backend Optimization Checklist

**Чек-лист оптимизаций для Backend Agent перед передачей задачи**

## 📋 Обязательные проверки

### ✅ Уровень 1: Базовые (ВСЕГДА)

**Применяется к:** Все сервисы, все endpoints

- [ ] **Struct Field Alignment** - поля упорядочены по размеру (большие → маленькие)
- [ ] **Context Deadlines** - все внешние вызовы имеют timeout
- [ ] **DB Connection Pool** - настроен правильно (MaxOpenConns: 25-50)
- [ ] **Health/Metrics endpoints** - `/health` и `/metrics` работают
- [ ] **Structured Logging** - JSON формат, нет `fmt.Println`
- [ ] **Error Handling** - все ошибки обработаны, не игнорируются

### ✅ Уровень 2: Hot Path (для частых операций >100 RPS)

**Применяется к:** API endpoints с высокой нагрузкой

- [ ] **Memory Pooling** - используется `sync.Pool` для переиспользования объектов
- [ ] **Preallocation** - slices с known capacity: `make([]T, 0, capacity)`
- [ ] **Batch Operations** - DB queries батчатся где возможно
- [ ] **Lock-Free** - используется `atomic` вместо `mutex` для простых операций
- [ ] **String vs []byte** - в hot path используется `[]byte`
- [ ] **Zero Allocations** - бенчмарки показывают 0 allocs/op для critical path

### ✅ Уровень 3: Game Servers (для real-time сервисов)

**Применяется к:** Game state, matchmaking, voice chat, real-time сервисы

- [ ] **UDP Support** - для game state используется UDP, не WebSocket
- [ ] **Spatial Partitioning** - для >100 игроков реализован spatial grid
- [ ] **Delta Compression** - отправляются только изменения, не full state
- [ ] **Worker Pool** - горутины ограничены через semaphore/worker pool
- [ ] **Adaptive Tick Rate** - тикрейт адаптируется под нагрузку
- [ ] **GC Tuning** - `GOGC` настроен (обычно 50 для game servers)
- [ ] **Profiling Enabled** - `pprof` endpoints доступны (на отдельном порту)

### ✅ Уровень 4: Advanced (опционально, по необходимости)

**Применяется к:** Bottlenecks после профилирования

- [ ] **Ring Buffer** - для event processing вместо channels
- [ ] **Flyweight Pattern** - для shared game objects (weapons, items)
- [ ] **FlatBuffers** - для ultra-low latency вместо Protobuf
- [ ] **Copy-On-Write** - для read-heavy shared state
- [ ] **SIMD/Assembly** - для векторных вычислений (physics)

## 🔍 Как проверять

### Автоматические проверки:

```bash
# 1. Struct alignment
fieldalignment ./...

# 2. Goroutine leaks
go test -v ./... -run TestMain  # С goleak

# 3. Benchmarks
go test -bench=. -benchmem | grep "allocs/op"

# 4. Profiling
curl http://localhost:6060/debug/pprof/allocs > allocs.prof
go tool pprof -top allocs.prof

# 5. Linting
golangci-lint run
```

### Ручные проверки:

**Смотри код на:**
```bash
# Memory pooling
grep -r "sync.Pool" server/

# Batch operations  
grep -r "Batch" server/repository.go

# Context timeouts
grep -r "context.WithTimeout" server/

# Atomic operations
grep -r "atomic\." server/
```

## 📊 Метрики успеха

**После применения оптимизаций проверь:**

| Метрика | Цель | Как измерить |
|---------|------|--------------|
| P99 Latency | <10ms | Prometheus histogram |
| Allocs/op | 0 (hot path) | `go test -benchmem` |
| GC Pause | <1ms | `/debug/pprof/heap` |
| Goroutines | Stable | `/debug/pprof/goroutine` |
| Memory | No leaks | Memory over time (Grafana) |
| DB Queries | <10ms P95 | Slow query log |

## 🎯 Severity Levels

**Насколько критично:**

### 🔴 BLOCKER (задачу нельзя передавать без этого):
- Context deadlines отсутствуют
- DB connection pool не настроен
- Goroutine leaks в тестах
- Нет error handling

### 🟡 WARNING (нужно исправить, но можно передать):
- Memory pooling не используется в hot path
- Batch operations можно добавить
- GC tuning не настроен

### 🟢 OPTIONAL (nice to have):
- FlatBuffers вместо Protobuf
- SIMD optimizations
- Advanced patterns

## 💡 Шаблон комментария при передаче

**Backend → Network/QA:**

```markdown
✅ Backend ready. Handed off to {NextAgent}

**Optimizations applied:**
- [x] Memory pooling for response objects
- [x] Batch DB queries (1 query instead of N)
- [x] Context timeouts (100ms for external calls)
- [x] Struct alignment (checked with fieldalignment)
- [x] Zero allocations in hot path (benchmarks)

**Benchmarks:**
- P99 latency: 8.5ms (target: <10ms) ✅
- Allocations: 0 allocs/op (hot path) ✅
- Throughput: 15,000 req/sec ✅

Issue: #123
```

## 🛠️ Инструменты для валидации

**Добавь в CI/CD:**

```yaml
# .github/workflows/backend-quality.yml
- name: Check struct alignment
  run: fieldalignment ./...

- name: Check benchmarks
  run: |
    go test -bench=. -benchmem > bench.txt
    # Fail если есть allocations в critical path
    
- name: Check goroutine leaks
  run: go test -v -run TestMain ./...
```

## 📚 См. также:

- `.cursor/BACKEND_CODE_TEMPLATES.md` - шаблоны оптимизированного кода
- `.cursor/rules/agent-backend.mdc` - полные правила Backend агента
- `.cursor/commands/backend-validate-optimizations.md` - команда для проверки

