# [FAST] Performance Enforcement Policy

**СТРОГИЕ требования к оптимизациям — ОБЯЗАТЕЛЬНО для всех агентов**

## [FORBIDDEN] EMOJI AND SPECIAL CHARACTERS ЗАПРЕТ

**КРИТИЧНО:** Запрещено использовать эмодзи и специальные Unicode символы в коде!

### Почему запрещено:

— [FORBIDDEN] Ломают выполнение скриптов на Windows

- [FORBIDDEN] Могут вызывать ошибки в терминале
- [FORBIDDEN] Создают проблемы с кодировкой
- [FORBIDDEN] Нарушают совместимость между ОС

### Что use вместо:

- [OK] `:smile:` вместо [EMOJI]
- [OK] `[FORBIDDEN]` вместо [FORBIDDEN]
- [OK] `[OK]` вместо [OK]
- [OK] `[ERROR]` вместо [ERROR]
- [OK] `[WARNING]` вместо [WARNING]

### Автоматическая проверка:

- Pre-commit hooks блокируют коммиты с эмодзи
- Git hooks проверяют staged файлы
- Исключения: `.cursor/rules/*` (документация), `.githooks/*`

---

## [TARGET] Философия: Optimization-First

**Оптимизации ОБЯЗАТЕЛЬНЫ, а не опциональны.**

### Было (старый подход):

```
Создать функционал → Передать → (может быть) оптимизировать потом
```

### Стало (новый подход):

```
Создать функционал С ОПТИМИЗАЦИЯМИ → Валидировать → Передать
```

**Benefits:**

- [OK] Production-ready с первого дня
- [OK] Нет technical debt
- [OK] Performance targets с самого начала
- [OK] Дешевле (оптимизировать сразу проще чем потом)

**Цель:**

- Каждый Backend сервис ДОЛЖЕН следовать Performance Bible
- Каждая Database таблица ДОЛЖНА быть оптимизирована
- Рефакторинг неоптимизированного кода ОБЯЗАТЕЛЕН

---

## [SYMBOL] КРИТИЧНО: Backend Agent

### BLOCKER - задачу НЕЛЬЗЯ передавать без этого:

```bash
# Автоматическая проверка перед передачей:
./scripts/validate-backend-optimizations.sh services/{service}-go

# Или для всех enterprise-grade сервисов:
python scripts/generate-all-domains-go.py  # включает валидацию

# Если хоть один BLOCKER → исправь и повтори
# Передавай ТОЛЬКО после: [OK] All checks passed
```

**BLOCKER checklist:**

- [ERROR] No context timeouts
- [ERROR] No DB pool config
- [ERROR] Goroutine leaks
- [ERROR] No struct alignment
- [ERROR] No structured logging
- [ERROR] No profiling endpoints (pprof)
- [ERROR] No health/metrics endpoints
- [ERROR] Unbounded channels (для production)

**Что делать при BLOCKER:**

1. Исправь проблемы
2. Запусти валидацию снова
3. Повтори пока не пройдет
4. ТОЛЬКО после [OK] → передавай задачу

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
    - [SYMBOL] BLOCKER issues → исправь немедленно
    - 🟡 WARNING issues → создай Issue
    - 🟢 IMPROVEMENTS → backlog

**ПРАВИЛО:** Каждый existing сервис должен быть optimized или иметь plan для optimization.

---

## [SYMBOL] КРИТИЧНО: Database Agent

### Column Order Optimization

**ОБЯЗАТЕЛЬНО при создании/рефакторинге таблиц:**

```sql
-- [ERROR] ПЛОХО: random order
CREATE TABLE players (
    is_active BOOLEAN,     -- 1 byte + padding
    id BIGINT,            -- 8 bytes
    level INTEGER         -- 4 bytes
);
-- Row: ~24 bytes (из-за padding)

-- [OK] ХОРОШО: large → small
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

## [GAME] КРИТИЧНО: Performance Agent

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

## [SYMBOL] Enforcement Workflow

### Для новых сервисов:

```
API Designer → Backend → (автоматическая проверка) →
  [ERROR] BLOCKER? → Backend исправляет
  [OK] Pass? → Network
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

## [TRANSPORT]️ Инструменты enforcement

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

## [SYMBOL] Метрики compliance

**Отслеживай:**

| Метрика                    | Цель  | Текущее |
|----------------------------|-------|---------|
| % сервисов с оптимизациями | 100%  | -       |
| % таблиц с оптимизацией    | 100%  | -       |
| Avg validation score       | >90%  | -       |
| Рефакторинг Issues открыто | Track | -       |
| Рефакторинг Issues закрыто | Track | -       |

---

## [ALERT] Escalation Process

### Если Backend пытается передать без оптимизаций:

1. **Автоматическая проверка блокирует:**
   ```
   [ERROR] Validation failed: 3 BLOCKERS found
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

4. **ТОЛЬКО после [OK] → может передавать**

### Если агент игнорирует требования:

- Performance Agent создает Issue
- Tech lead review
- Обязательный рефакторинг

---

## [OK] Success Criteria

**Backend сервис готов когда:**

- [OK] Validation script passed (0 BLOCKERS)
- [OK] Benchmarks show 0 allocs/op (hot path)
- [OK] No goroutine leaks
- [OK] Profiling endpoints enabled
- [OK] Performance targets met

**Database schema готова когда:**

- [OK] Columns ordered (large → small)
- [OK] Covering indexes для hot queries
- [OK] Partial indexes где применимо
- [OK] Row size optimized

---

## [BOOK] References

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

## [IDEA] Key Principle

**"Optimization is NOT optional - it's a requirement"**

Без оптимизаций:

- Сервис не готов к production
- Задача не может быть передана
- Issue не может быть закрыт

С оптимизациями:

- [OK] Production-ready
- [OK] Scalable
- [OK] Cost-effective
- [OK] Player experience: excellent

