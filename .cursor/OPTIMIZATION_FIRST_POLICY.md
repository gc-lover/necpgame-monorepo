# 🚀 Optimization-First Policy

**Новый подход: Оптимизации ОБЯЗАТЕЛЬНЫ, а не опциональны**

---

## 🎯 Философия

### Было (старый подход):
```
Создать функционал → Передать → (может быть) оптимизировать потом
```

**Проблемы:**
- Оптимизации откладываются
- Technical debt накапливается
- Performance issues в production
- Дорогой рефакторинг потом

### Стало (новый подход):
```
Создать функционал С ОПТИМИЗАЦИЯМИ → Валидировать → Передать
```

**Benefits:**
- ✅ Production-ready с первого дня
- ✅ Нет technical debt
- ✅ Performance targets с самого начала
- ✅ Дешевле (оптимизировать сразу проще чем потом)

---

## 📋 Правила для агентов

### Backend Developer:

**НОВЫЕ сервисы:**
```
1. Используй шаблоны из .cursor/templates/backend-*.md
2. Применяй Performance Bible с самого начала
3. Валидируй: /backend-validate-optimizations #123
4. Передавай ТОЛЬКО после ✅
```

**EXISTING сервисы:**
```
1. Аудит: /backend-refactor-service {service}
2. Если проблемы:
   🔴 BLOCKER → исправь немедленно
   🟡 WARNING → создай Issue для рефакторинга
3. Применяй оптимизации при работе с кодом
4. Валидируй перед передачей
```

### Database Engineer:

**НОВЫЕ таблицы:**
```
1. Column order: large → small
2. Indexes: covering + partial
3. Partitioning: для >10M rows
4. JSONB: GIN indexes
```

**EXISTING таблицы:**
```
1. Аудит: /database-refactor-schema {table}
2. Создай optimization plan
3. Online migration (zero downtime)
4. Валидация после миграции
```

### Performance Engineer:

**Проактивный аудит:**
```
1. Профилируй production каждую неделю
2. Нашел bottleneck → создай Issue
3. Назначай Backend/Database для fix
4. PGO profiles для всех сервисов
```

---

## 🔴 BLOCKER System

### Что такое BLOCKER?

**BLOCKER** = критическая проблема, без fix которой задачу НЕЛЬЗЯ передавать.

**Примеры BLOCKER для Backend:**
- ❌ No context timeouts
- ❌ No DB pool config
- ❌ Goroutine leaks
- ❌ No struct alignment
- ❌ No structured logging

**Примеры BLOCKER для Database:**
- ❌ Columns not ordered
- ❌ No indexes для hot queries
- ❌ No covering indexes
- ❌ No partial indexes

### Workflow с BLOCKER:

```
Backend работает →
  Валидация: /backend-validate-optimizations #123 →
    ❌ BLOCKER found? →
      Backend исправляет →
      Повторная валидация →
    ✅ All pass? →
      Backend передает Network
```

**ПРАВИЛО:** BLOCKER = STOP. Исправь и повтори.

---

## 🔄 Рефакторинг Policy

### Обязательный рефакторинг:

**Backend ОБЯЗАН:**
- Аудировать existing сервисы при работе с ними
- Создавать Issues для найденных проблем
- Исправлять BLOCKER немедленно
- Применять оптимизации инкрементально

**Database ОБЯЗАН:**
- Аудировать existing tables при работе с ними
- Создавать optimization plans
- Применять оптимизации через migrations
- Online migrations (zero downtime)

**Performance ОБЯЗАН:**
- Регулярный profiling production
- Создавать Issues для bottlenecks
- Обновлять PGO profiles

### Приоритизация рефакторинга:

| Priority | Критерий | Action |
|----------|----------|--------|
| 🔴 **P0** | BLOCKER в production сервисе | Немедленно |
| 🟡 **P1** | WARNING в hot path | Эта неделя |
| 🟢 **P2** | IMPROVEMENTS | Backlog |

---

## 📊 Metrics & Tracking

### Отслеживай:

**Refactoring Progress:**
```markdown
## Сервисы

| Service | Status | BLOCKERS | WARNINGS | Progress |
|---------|--------|----------|----------|----------|
| companion-service | ✅ Optimized | 0 | 0 | 100% |
| matchmaking-service | 🟡 In Progress | 0 | 3 | 60% |
| voice-chat-service | ❌ Not Optimized | 5 | 10 | 0% |
```

**Tables:**
```markdown
## Database Tables

| Table | Rows | Optimized | Issues | Plan |
|-------|------|-----------|--------|------|
| players | 1M | ✅ Yes | 0 | - |
| inventory | 5M | 🟡 Partial | 2 | Issue #456 |
| combat_logs | 100M | ❌ No | 8 | Issue #457 |
```

---

## 🎯 Goals

### Short-term (1 месяц):
- [ ] Все новые сервисы с оптимизациями (100%)
- [ ] Топ-10 сервисов отрефакторены
- [ ] Все BLOCKER issues закрыты

### Mid-term (3 месяца):
- [ ] 80% сервисов оптимизированы
- [ ] 80% таблиц оптимизированы
- [ ] Performance targets met для всех

### Long-term (6 месяцев):
- [ ] 100% сервисов optimized
- [ ] 100% таблиц optimized
- [ ] Автоматические проверки в CI/CD
- [ ] Pre-commit hooks для validation

---

## 🛠️ Tools

**Команды:**
- `/backend-validate-optimizations #123` - валидация перед передачей
- `/backend-refactor-service {service}` - аудит existing сервиса
- `/database-refactor-schema {table}` - аудит existing таблицы

**Скрипты:**
- `scripts/validate-backend-optimizations.sh` - автоматическая проверка
- `scripts/audit-all-services.sh` - аудит всех сервисов (создать)
- `scripts/audit-all-tables.sh` - аудит всех таблиц (создать)

**Документация:**
- `.cursor/GO_BACKEND_PERFORMANCE_BIBLE.md` - 120+ оптимизаций
- `.cursor/PERFORMANCE_ENFORCEMENT.md` - строгие требования
- `.cursor/BACKEND_OPTIMIZATION_CHECKLIST.md` - чек-лист
- `.cursor/templates/backend-*.md` - шаблоны кода

---

## 💡 Key Principles

1. **Optimization is NOT optional** - это requirement
2. **Validate before handoff** - BLOCKER = STOP
3. **Refactor existing code** - не оставляй technical debt
4. **Measure everything** - benchmarks, profiling, metrics
5. **Performance targets** - определены и обязательны

---

## 🎓 Learning Path

### Для Backend:
1. Читай Performance Bible (все 13 parts)
2. Используй шаблоны
3. Практикуй на новых сервисах
4. Рефакторь existing сервисы
5. Создавай Issues для проблем

### Для Database:
1. Читай Part 5A, 7A Performance Bible
2. Изучай оптимальные схемы
3. Применяй при создании таблиц
4. Рефакторь existing tables
5. Мониторь query performance

### Для Performance:
1. Профилируй регулярно
2. Создавай Issues для bottlenecks
3. Обновляй PGO profiles
4. Мониторь метрики
5. Валидируй fixes

---

**Summary:** Оптимизации теперь ОБЯЗАТЕЛЬНЫ для всех агентов. Валидация автоматическая. Рефакторинг existing кода - часть workflow.

