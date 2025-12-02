# ⚡ Performance Enforcement Policy

**СТРОГИЕ требования к оптимизациям - ОБЯЗАТЕЛЬНО для всех агентов**

---

## 🎯 Цель

**Сделать оптимизации ОБЯЗАТЕЛЬНЫМИ, а не опциональными.**

Каждый Backend сервис ДОЛЖЕН следовать Performance Bible.  
Каждая Database таблица ДОЛЖНА быть оптимизирована.  
Рефакторинг неоптимизированного кода ОБЯЗАТЕЛЕН.

---

## 🔴 КРИТИЧНО: Backend Agent

### BLOCKER - задачу НЕЛЬЗЯ передавать без этого:

```bash
# Автоматическая проверка перед передачей:
./scripts/validate-backend-optimizations.sh services/{service}-go

# Если хоть один BLOCKER → исправь и повтори
# Передавай ТОЛЬКО после: ✅ All checks passed
```

**BLOCKER checklist:**
- ❌ No context timeouts
- ❌ No DB pool config
- ❌ Goroutine leaks
- ❌ No struct alignment
- ❌ No structured logging

**Что делать при BLOCKER:**
1. Исправь проблемы
2. Запусти валидацию снова
3. Повтори пока не пройдет
4. ТОЛЬКО после ✅ → передавай задачу

---

## 🟡 РЕФАКТОРИНГ существующих сервисов

### Обязанность Backend Agent:

**При работе с СУЩЕСТВУЮЩИМ сервисом:**

1. **Проверь оптимизации:**
   ```bash
   /backend-refactor-service {service-name}
   ```

2. **Если нашел проблемы:**
   - Создай рефакторинг план
   - Создай Issue для рефакторинга
   - Пометь label `refactor` + `performance`

3. **Приоритизируй:**
   - 🔴 BLOCKER issues → исправь немедленно
   - 🟡 WARNING issues → создай Issue
   - 🟢 IMPROVEMENTS → backlog

**ПРАВИЛО:** Каждый existing сервис должен быть optimized или иметь plan для optimization.

---

## 💾 КРИТИЧНО: Database Agent

### Column Order Optimization

**ОБЯЗАТЕЛЬНО при создании/рефакторинге таблиц:**

```sql
-- ❌ ПЛОХО: random order
CREATE TABLE players (
    is_active BOOLEAN,     -- 1 byte + padding
    id BIGINT,            -- 8 bytes
    level INTEGER         -- 4 bytes
);
-- Row: ~24 bytes (из-за padding)

-- ✅ ХОРОШО: large → small
CREATE TABLE players (
    id BIGINT,            -- 8 bytes
    level INTEGER,        -- 4 bytes  
    is_active BOOLEAN     -- 1 byte
);
-- Row: ~16 bytes (-33%!)
```

**Для 1M players:** 24MB → 16MB экономии!

### Index Optimization

**ОБЯЗАТЕЛЬНО:**
- Covering indexes для hot queries
- Partial indexes (WHERE is_active = true)
- GIN indexes для JSONB
- GIST indexes для spatial queries

### Рефакторинг существующих таблиц:

```bash
/database-refactor-schema {table-name}
```

**Создает:**
- Optimization plan
- Migration scripts
- Expected gains report
- GitHub Issue

---

## 🎮 КРИТИЧНО: Performance Agent

### Обязанность:

**ПРОАКТИВНЫЙ аудит production сервисов:**

1. **Регулярный профилинг:**
   ```bash
   # CPU profile каждую неделю
   curl http://prod:6060/debug/pprof/profile?seconds=30 > cpu.prof
   go tool pprof -top cpu.prof
   ```

2. **Если нашел bottleneck:**
   - Создай Issue для оптимизации
   - Пометь label `performance` + `priority-high`
   - Назначь Backend или Database

3. **PGO compilation:**
   ```bash
   # Собирай production profiles
   # Создавай default.pgo для каждого сервиса
   ```

---

## 📋 Enforcement Workflow

### Для новых сервисов:

```
API Designer → Backend → (автоматическая проверка) →
  ❌ BLOCKER? → Backend исправляет
  ✅ Pass? → Network
```

### Для существующих сервисов:

```
Backend берет задачу →
  Проверяет оптимизации (`/backend-refactor-service`) →
    Проблемы? → Создает рефакторинг Issues →
    Применяет оптимизации →
  Продолжает основную задачу
```

---

## 🛠️ Инструменты enforcement

### 1. Pre-commit hook (будущее)

```bash
# .git/hooks/pre-commit
./scripts/validate-backend-optimizations.sh $(git diff --name-only --cached | grep "services/.*-go")
```

### 2. CI/CD проверки

```yaml
# .github/workflows/performance-check.yml
- name: Validate Backend Optimizations
  run: |
    for service in services/*-go; do
      ./scripts/validate-backend-optimizations.sh $service
    done
```

### 3. Agent commands

- `/backend-validate-optimizations #123` - перед передачей (ОБЯЗАТЕЛЬНО)
- `/backend-refactor-service {service}` - для existing
- `/database-refactor-schema {table}` - для existing tables

---

## 📊 Метрики compliance

**Отслеживай:**

| Метрика | Цель | Текущее |
|---------|------|---------|
| % сервисов с оптимизациями | 100% | - |
| % таблиц с оптимизацией | 100% | - |
| Avg validation score | >90% | - |
| Рефакторинг Issues открыто | Track | - |
| Рефакторинг Issues закрыто | Track | - |

---

## 🚨 Escalation Process

### Если Backend пытается передать без оптимизаций:

1. **Автоматическая проверка блокирует:**
   ```
   ❌ Validation failed: 3 BLOCKERS found
   → Cannot proceed to next stage
   ```

2. **Backend получает feedback:**
   ```
   Fix these issues:
   - Add context timeouts (handlers.go)
   - Configure DB pool (main.go)
   - Fix goroutine leaks (service.go)
   ```

3. **Backend исправляет → повторяет валидацию**

4. **ТОЛЬКО после ✅ → может передавать**

### Если агент игнорирует требования:

- Performance Agent создает Issue
- Tech lead review
- Обязательный рефакторинг

---

## ✅ Success Criteria

**Backend сервис готов когда:**
- ✅ Validation script passed (0 BLOCKERS)
- ✅ Benchmarks show 0 allocs/op (hot path)
- ✅ No goroutine leaks
- ✅ Profiling endpoints enabled
- ✅ Performance targets met

**Database schema готова когда:**
- ✅ Columns ordered (large → small)
- ✅ Covering indexes для hot queries
- ✅ Partial indexes где применимо
- ✅ Row size optimized

---

## 📚 References

**Для Backend:**
- `.cursor/GO_BACKEND_PERFORMANCE_BIBLE.md` - 120+ оптимизаций
- `.cursor/BACKEND_OPTIMIZATION_CHECKLIST.md` - чек-лист
- `.cursor/templates/backend-*.md` - шаблоны
- `/backend-validate-optimizations` - команда
- `/backend-refactor-service` - рефакторинг

**Для Database:**
- `.cursor/GO_BACKEND_PERFORMANCE_BIBLE.md` - Part 5A, 7A
- `/database-refactor-schema` - рефакторинг

**Для Performance:**
- `.cursor/GO_BACKEND_PERFORMANCE_BIBLE.md` - Part 3 (Profiling)
- Pyroscope, pprof, benchmarks

---

## 💡 Key Principle

**"Optimization is NOT optional - it's a requirement"**

Без оптимизаций:
- Сервис не готов к production
- Задача не может быть передана
- Issue не может быть закрыт

С оптимизациями:
- ✅ Production-ready
- ✅ Scalable
- ✅ Cost-effective
- ✅ Player experience: excellent

