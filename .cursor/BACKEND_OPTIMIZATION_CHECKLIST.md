# 🔍 Backend Optimization Checklist

**Чек-лист оптимизаций для Backend Agent перед передачей задачи**

## 📋 Обязательные проверки

### OK Уровень 1: Базовые (ВСЕГДА)

**Применяется к:** Все сервисы, все endpoints

- [ ] **Struct Field Alignment** - поля упорядочены по размеру (большие → маленькие)
- [ ] **Context Deadlines** - все внешние вызовы имеют timeout
- [ ] **DB Connection Pool** - настроен правильно (MaxOpenConns: 25-50)
- [ ] **Health/Metrics endpoints** - `/health` и `/metrics` работают
- [ ] **Structured Logging** - JSON формат, нет `fmt.Println`
- [ ] **Error Handling** - все ошибки обработаны, не игнорируются

### OK Уровень 2: Hot Path (для частых операций >100 RPS)

**Применяется к:** API endpoints с высокой нагрузкой

- [ ] **Memory Pooling** - используется `sync.Pool` для переиспользования объектов
- [ ] **Preallocation** - slices с known capacity: `make([]T, 0, capacity)`
- [ ] **Batch Operations** - DB queries батчатся где возможно
- [ ] **Lock-Free** - используется `atomic` вместо `mutex` для простых операций
- [ ] **String vs []byte** - в hot path используется `[]byte`
- [ ] **Zero Allocations** - бенчмарки показывают 0 allocs/op для critical path

### OK Уровень 3: Game Servers (для real-time сервисов)

**Применяется к:** Game state, matchmaking, voice chat, real-time сервисы

- [ ] **UDP Support** - для game state используется UDP, не WebSocket
- [ ] **Spatial Partitioning** - для >100 игроков реализован spatial grid
- [ ] **Delta Compression** - отправляются только изменения, не full state
- [ ] **Worker Pool** - горутины ограничены через semaphore/worker pool
- [ ] **Adaptive Tick Rate** - тикрейт адаптируется под нагрузку
- [ ] **GC Tuning** - `GOGC` настроен (обычно 50 для game servers)
- [ ] **Profiling Enabled** - `pprof` endpoints доступны (на отдельном порту)

### OK Уровень 4: MMO Patterns (для MMO/FPS игр)

**Применяется к:** MMO сервисы, inventory, guilds, trading

- [ ] **Redis Session Store** - stateless servers, horizontal scaling
- [ ] **Inventory Caching** - multi-level (memory + Redis + DB)
- [ ] **Guild Action Batching** - DB transactions ↓95%
- [ ] **Optimistic Locking** - no deadlocks в trading
- [ ] **Materialized Views** - для leaderboards (100x speedup)
- [ ] **Time-Series Partitioning** - для >10M rows (query ↓90%)

### OK Уровень 5: Advanced (опционально)

**Применяется к:** Bottlenecks после профилирования

- [ ] **Server-Side Rewind** - lag compensation для FPS
- [ ] **Dead Reckoning** - smooth при packet loss
- [ ] **Adaptive Compression** - LZ4/Zstandard
- [ ] **Dictionary Compression** - для game packets
- [ ] **Circuit Breaker** - DB resilience
- [ ] **Feature Flags** - graceful degradation
- [ ] **Load Shedding** - backpressure handling
- [ ] **FlatBuffers** - ultra-low latency (если Protobuf bottleneck)

## 🔍 Как проверять

### ⚡ Используй автоматическую команду:

```bash
# ОБЯЗАТЕЛЬНО перед передачей задачи!
/backend-validate-optimizations #123

# Или вручную:
./scripts/validate-backend-optimizations.sh services/{service}-go
```

**Output:**
```
🔍 Validating optimizations for {service}-go...

OK Struct alignment: OK
OK Goroutine leak tests: OK  
OK Context timeouts: OK
OK DB pool config: OK
OK Structured logging: OK
❌ Memory pooling: NOT FOUND (BLOCKER!)
WARNING  Benchmarks: Missing

━━━━━━━━━━━━━━━━━━━━━━━━
RESULT: ❌ VALIDATION FAILED
BLOCKERS: 1
WARNINGS: 1
━━━━━━━━━━━━━━━━━━━━━━━━

Cannot proceed to next stage.
Fix blockers and run validation again.
```

### Автоматические проверки (в скрипте):

```bash
# 1. Struct alignment
fieldalignment ./...

# 2. Goroutine leaks
go test -v ./... -run TestMain

# 3. Benchmarks
go test -bench=. -benchmem

# 4. Context timeouts
grep -r "context.WithTimeout" server/

# 5. DB pool
grep -r "SetMaxOpenConns" .

# 6. Memory pooling
grep -r "sync.Pool" server/

# 7. Structured logging
grep -r "zap\." server/

# 8. Profiling
grep -r "pprof" main.go
```

### Ручная проверка (опционально):

```bash
# Profiling
curl http://localhost:6060/debug/pprof/allocs > allocs.prof
go tool pprof -top allocs.prof

# Linting
golangci-lint run
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
OK Backend ready. Handed off to {NextAgent}

**Optimizations applied:**
- [x] Memory pooling for response objects
- [x] Batch DB queries (1 query instead of N)
- [x] Context timeouts (100ms for external calls)
- [x] Struct alignment (checked with fieldalignment)
- [x] Zero allocations in hot path (benchmarks)

**Benchmarks:**
- P99 latency: 8.5ms (target: <10ms) OK
- Allocations: 0 allocs/op (hot path) OK
- Throughput: 15,000 req/sec OK

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

## 🔄 Рефакторинг существующих сервисов

**Backend ОБЯЗАН рефакторить неоптимизированный код!**

### Workflow при работе с existing service:

```bash
# 1. Аудит сервиса
/backend-refactor-service {service-name}

# 2. Получишь:
# - Список проблем
# - Рефакторинг план
# - GitHub Issue для рефакторинга
# - Expected gains

# 3. Приоритизируй:
# 🔴 BLOCKER → исправь немедленно (в текущей задаче)
# 🟡 WARNING → создай Issue для отдельного рефакторинга
# 🟢 IMPROVEMENTS → backlog

# 4. Применяй оптимизации:
# - Используй шаблоны из .cursor/templates/backend-*.md
# - Следуй Performance Bible
# - Валидируй после каждого изменения
```

### Правило:

**НЕ оставляй сервисы неоптимизированными!**

- Нашел неоптимизированный код → создай Issue
- Работаешь с existing сервисом → применяй оптимизации
- Каждый коммит → улучшение performance

---

## 📚 См. также:

- `.cursor/GO_BACKEND_PERFORMANCE_BIBLE.md` - **120+ оптимизаций (13 parts)**
- `.cursor/BACKEND_CODE_TEMPLATES.md` - шаблоны оптимизированного кода
- `.cursor/PERFORMANCE_ENFORCEMENT.md` - **СТРОГИЕ требования**
- `.cursor/rules/agent-backend.mdc` - полные правила Backend агента
- `.cursor/commands/backend-validate-optimizations.md` - команда валидации
- `.cursor/commands/backend-refactor-service.md` - команда рефакторинга

