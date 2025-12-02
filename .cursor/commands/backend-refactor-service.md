# /backend-refactor-service - Рефакторинг существующего сервиса

**Автоматический аудит и рефакторинг сервиса для применения оптимизаций**

---

## Описание

Команда анализирует существующий Go сервис и создает план рефакторинга для применения всех оптимизаций из Performance Bible.

---

## Синтаксис

```
/backend-refactor-service {service-name}
```

**Параметры:**
- `{service-name}` - имя сервиса (например: `companion-service`)

---

## Примеры использования

```
/backend-refactor-service companion-service
/backend-refactor-service matchmaking-service
/backend-refactor-service voice-chat-service
```

---

## Алгоритм

### 1. Анализ текущего состояния

```bash
cd services/{service-name}-go

# Проверка структуры
ls -la server/

# Проверка struct alignment
fieldalignment ./... 2>&1 | tee alignment-issues.txt

# Проверка goroutine leaks
go test -v -run TestMain ./... 2>&1 | grep -i "leak"

# Проверка benchmarks
go test -bench=. -benchmem 2>&1 | grep "allocs/op"

# Проверка imports
grep -r "sync.Pool" server/
grep -r "atomic\." server/
grep -r "context.WithTimeout" server/
grep -r "Batch" server/repository.go
```

### 2. Создание рефакторинг плана

**Создать файл:** `services/{service-name}-go/REFACTOR_PLAN.md`

```markdown
# Refactoring Plan: {service-name}

**Created:** {date}
**Service:** {service-name}
**Current state:** Not optimized
**Target:** Full Performance Bible compliance

## Issues Found

### 🔴 BLOCKERS (Critical):
- [ ] No context timeouts (found in: handlers.go:45, service.go:78)
- [ ] No DB pool config (main.go)
- [ ] Goroutine leaks detected (TestMain fails)
- [ ] Struct alignment issues (Player: 32 → 16 bytes possible)

### 🟡 WARNINGS (High Priority):
- [ ] No memory pooling (handlers.go)
- [ ] No batch operations (repository.go)
- [ ] No lock-free counters (metrics.go)
- [ ] No benchmarks

### 🟢 IMPROVEMENTS (Nice to have):
- [ ] Add pprof endpoints
- [ ] Add Prometheus metrics
- [ ] Improve logging (use zap)

## Refactoring Steps

### Phase 1: Fix Blockers (Day 1)
1. Add context timeouts to all external calls
2. Configure DB pool (MaxOpenConns: 25)
3. Fix goroutine leaks (add cleanup in Stop())
4. Fix struct alignment (use fieldalignment tool)

### Phase 2: Hot Path (Day 2-3)
1. Add memory pooling for Response objects
2. Implement batch DB queries
3. Replace mutex with atomic for counters
4. Add benchmarks with 0 allocs/op

### Phase 3: Advanced (Day 4-5)
1. Add pprof endpoints
2. Add Prometheus metrics
3. Switch to zap logging
4. Add integration tests

## Expected Gains

**Current performance (estimated):**
- Throughput: ~2k req/sec
- P99 Latency: ~150ms
- Memory: ~500MB

**After refactoring:**
- Throughput: ~10-15k req/sec (+500-700%)
- P99 Latency: ~10-20ms (-90%)
- Memory: ~150-200MB (-60%)

## Files to Modify

- [ ] `main.go` - DB pool config
- [ ] `server/handlers.go` - context timeouts, memory pooling
- [ ] `server/service.go` - batch operations, atomic counters
- [ ] `server/repository.go` - batch queries, prepared statements
- [ ] `server/metrics.go` - NEW FILE - Prometheus metrics
- [ ] `server/models.go` - struct alignment
- [ ] `server/handlers_test.go` - benchmarks, goleak

## Validation

After refactoring run:
```bash
./scripts/validate-backend-optimizations.sh services/{service-name}-go
```

All checks must pass ✅
```

### 3. Создание Issues для рефакторинга

```javascript
// Создать Issue для рефакторинга
mcp_github_issue_write({
  method: 'create',
  owner: 'gc-lover',
  repo: 'necpgame-monorepo',
  title: '[REFACTOR] Optimize {service-name} for Performance Bible compliance',
  body: `## Goal

Apply all Performance Bible optimizations to {service-name}-go service.

## Current State

- No performance optimizations applied
- Estimated throughput: ~2k req/sec
- Estimated P99 latency: ~150ms
- Memory usage: ~500MB

## Target State

- Full Performance Bible compliance
- Throughput: >10k req/sec (+500%)
- P99 latency: <20ms (-90%)
- Memory: <200MB (-60%)

## Refactoring Plan

See: services/{service-name}-go/REFACTOR_PLAN.md

## Acceptance Criteria

- [ ] All BLOCKER issues fixed
- [ ] All WARNING issues fixed
- [ ] Validation script passes
- [ ] Benchmarks show 0 allocs/op (hot path)
- [ ] No goroutine leaks
- [ ] Performance targets met

## References

- Performance Bible: .cursor/GO_BACKEND_PERFORMANCE_BIBLE.md
- Checklist: .cursor/BACKEND_OPTIMIZATION_CHECKLIST.md
- Templates: .cursor/templates/backend-*.md
`,
  labels: ['refactor', 'backend', 'performance', 'priority-high']
});
```

### 4. Обновление статуса

```javascript
// Добавить к Project
const newIssue = await mcp_github_issue_write(...);

// Установить статус Backend - Todo
mcp_github_update_project_item({
  owner_type: 'user',
  owner: 'gc-lover',
  project_number: 1,
  item_id: project_item_id,
  updated_field: {
    id: 239690516,
    value: '72d37d44'  // Backend - Todo
  }
});
```

---

## Output

**Команда создает:**
1. ✅ `REFACTOR_PLAN.md` в сервисе
2. ✅ GitHub Issue с планом рефакторинга
3. ✅ Обновленный статус в Project
4. ✅ Детальный список проблем и решений

---

## Когда использовать

**Используй эту команду когда:**
- Работаешь с существующим сервисом без оптимизаций
- Нашел неоптимизированный код
- Нужно обновить старый сервис до новых стандартов
- Профилирование показало проблемы

**НЕ используй:**
- Для новых сервисов (сразу пиши с оптимизациями)
- Если сервис уже оптимизирован

---

## См. также

- `.cursor/GO_BACKEND_PERFORMANCE_BIBLE.md` - все оптимизации
- `.cursor/BACKEND_OPTIMIZATION_CHECKLIST.md` - чек-лист
- `/backend-validate-optimizations` - валидация после рефакторинга

