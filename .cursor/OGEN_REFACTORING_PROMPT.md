# Промпт для рефакторинга сервисов на ogen-go

## 🎯 Цель

Рефакторинг Go сервисов с `oapi-codegen` на `ogen` для получения:
- **90% улучшение latency** (191 ns/op vs 1994 ns/op)
- **95% меньше памяти** (320 B/op vs 6528 B/op)
- **80% меньше allocations** (5 allocs/op vs 25 allocs/op)

---

## 👤 Роли агентов

**Основной агент:** `Backend Developer`

**Вспомогательные (при необходимости):**
- `API Designer` - если нужно обновить OpenAPI spec
- `Performance Engineer` - для валидации benchmarks

---

## 📚 Целевая документация

### Обязательная (читать перед началом):

1. **`.cursor/OGEN_MIGRATION_GUIDE.md`** - главный гайд
   - Quick start
   - Performance gains
   - Reference implementation

2. **`.cursor/ogen/01-OVERVIEW.md`** - обзор и стратегия
   - Executive summary
   - Benchmark results
   - Service priority list
   - Migration strategy

3. **`.cursor/ogen/02-MIGRATION-STEPS.md`** - пошаговая инструкция
   - Complete migration checklist (7 phases)
   - Code generation setup
   - Handler migration guide
   - Service layer updates
   - Testing and deployment

4. **`.cursor/ogen/03-TROUBLESHOOTING.md`** - решение проблем
   - Breaking changes overview
   - Common issues and solutions
   - Mistakes to avoid

### Справочная:

5. **`.cursor/CODE_GENERATION_TEMPLATE.md`** - шаблоны Makefile
6. **`.cursor/BACKEND_OPTIMIZATION_CHECKLIST.md`** - валидация оптимизаций
7. **`.cursor/OGEN_MIGRATION_STATUS.md`** - статус миграции (какие сервисы уже мигрированы)

---

## 🏗️ Reference Implementation

**Используй как шаблон:**
- `services/combat-combos-service-ogen-go/` - **полный пример**

**Ключевые файлы для изучения:**
- `server/handlers.go` - Typed ogen handlers
- `server/service.go` - Service layer с OptX types
- `server/security.go` - SecurityHandler implementation
- `server/http_server.go` - ogen server setup
- `server/handlers_bench_test.go` - Benchmarks
- `Makefile` - Code generation

**Недавно мигрированные (для сравнения):**
- `services/combat-actions-service-go/`
- `services/combat-damage-service-go/`
- `services/combat-ai-service-go/`

---

## 📋 Процесс рефакторинга

### Шаг 1: Подготовка (Backend Developer)

1. **Проверь OpenAPI spec:**
   ```bash
   cd proto/openapi/
   redocly lint {service}.yaml
   ```

2. **Создай ветку:**
   ```bash
   git checkout -b feat/migrate-{service}-to-ogen
   ```

3. **Проверь статус миграции:**
   - Открой `.cursor/OGEN_MIGRATION_STATUS.md`
   - Убедись что сервис не мигрирован

### Шаг 2: Генерация кода (Backend Developer)

1. **Обнови Makefile** (см. `.cursor/CODE_GENERATION_TEMPLATE.md`):
   ```makefile
   generate-api:
       npx --yes @redocly/cli bundle ../../proto/openapi/{service}.yaml -o openapi-bundled.yaml
       ogen --target pkg/api --package api --clean openapi-bundled.yaml
   ```

2. **Обнови go.mod:**
   ```go
   require (
       github.com/ogen-go/ogen v1.18.0
       go.opentelemetry.io/otel v1.38.0
       go.opentelemetry.io/otel/metric v1.38.0
       go.opentelemetry.io/otel/trace v1.38.0
       golang.org/x/sync v0.18.0
       golang.org/x/net v0.47.0
   )
   ```

3. **Сгенерируй код:**
   ```bash
   cd services/{service}-go/
   make generate-api
   ```

### Шаг 3: Миграция handlers (Backend Developer)

**Ключевые изменения:**

1. **Замени interface{} на typed responses:**
   ```go
   // ❌ СТАРОЕ (oapi-codegen):
   func (h *Handlers) GetPlayer(w http.ResponseWriter, r *http.Request, id string) {
       player, err := h.service.GetPlayer(r.Context(), id)
       respondJSON(w, 200, player)  // ← interface{} boxing!
   }
   
   // ✅ НОВОЕ (ogen):
   func (h *Handlers) GetPlayer(ctx context.Context, params api.GetPlayerParams) (api.GetPlayerRes, error) {
       player, err := h.service.GetPlayer(ctx, params.Id.String())
       return player, nil  // ← Typed response!
   }
   ```

2. **Используй OptX типы для optional полей:**
   ```go
   // OptString, OptInt, OptBool для optional
   if params.Name.Set {
       name := params.Name.Value
   }
   ```

3. **Реализуй SecurityHandler:**
   ```go
   func (h *Handlers) HandleBearerAuth(ctx context.Context, token string) (context.Context, error) {
       // Validate JWT token
       return ctx, nil
   }
   ```

4. **Обнови server setup:**
   ```go
   srv, err := api.NewServer(h, api.WithMiddleware(middleware...))
   ```

**См. детали:** `.cursor/ogen/02-MIGRATION-STEPS.md` (Phase 3)

### Шаг 4: Тестирование (Backend Developer)

1. **Проверь сборку:**
   ```bash
   go build ./...
   ```

2. **Запусти тесты:**
   ```bash
   go test ./... -v
   ```

3. **Создай benchmarks:**
   ```go
   func BenchmarkHandler(b *testing.B) {
       b.ResetTimer()
       b.ReportAllocs()
       // ... benchmark code
   }
   ```

4. **Проверь результаты:**
   - Latency должен быть <300 ns/op (цель: 191 ns/op)
   - Memory должна быть <500 B/op (цель: 320 B/op)
   - Allocations должны быть <10 allocs/op (цель: 5 allocs/op)

### Шаг 5: Валидация (Backend Developer)

**ОБЯЗАТЕЛЬНО перед передачей:**

1. **Запусти валидацию оптимизаций:**
   ```bash
   /backend-validate-optimizations #{issue_number}
   ```

2. **Проверь чек-лист:**
   - [ ] Build passes
   - [ ] Tests pass
   - [ ] Benchmarks показывают >70% improvement
   - [ ] Все handlers используют typed responses
   - [ ] SecurityHandler реализован
   - [ ] Нет `interface{}` в hot path
   - [ ] Context timeouts настроены
   - [ ] DB pool настроен

**См. детали:** `.cursor/BACKEND_OPTIMIZATION_CHECKLIST.md`

---

## ⚠️ Критически важно

### НЕ делай:
- ❌ НЕ используй `interface{}` в handlers
- ❌ НЕ создавай helper функции с `interface{}`
- ❌ НЕ забывай про context timeouts
- ❌ НЕ пропускай benchmarks
- ❌ НЕ передавай задачу без валидации

### ВСЕГДА делай:
- ✅ Используй typed responses из ogen
- ✅ Реализуй SecurityHandler
- ✅ Добавь context timeouts
- ✅ Создай benchmarks
- ✅ Валидируй перед передачей

---

## 📊 Success Criteria

**Сервис готов когда:**
- [ ] Build passes (`go build ./...`)
- [ ] Tests pass (`go test ./...`)
- [ ] Benchmarks показывают >70% improvement
- [ ] Все handlers используют typed responses
- [ ] SecurityHandler реализован
- [ ] Нет `interface{}` в hot path
- [ ] PR создан с benchmark results

---

## 🔄 Workflow

1. **Найди задачу:** `Status:"Backend - Todo"` с меткой `ogen` или `migration`
2. **Обнови статус:** `Backend - In Progress`
3. **Выполни рефакторинг:** следуй шагам выше
4. **Валидируй:** `/backend-validate-optimizations #{issue}`
5. **Передай:** `QA - Todo` или `Network - Todo`
6. **Комментарий:** `✅ Migrated to ogen. Benchmarks: +90% latency, +95% memory. Issue: #{number}`

---

## 📞 Если проблемы

1. **Читай:** `.cursor/ogen/03-TROUBLESHOOTING.md`
2. **Смотри:** `services/combat-combos-service-ogen-go/` (reference)
3. **Проверь:** `.cursor/OGEN_MIGRATION_STATUS.md` (похожие сервисы)

---

## 🎯 Пример промпта для агента

```
Рефакторинг сервиса {service-name} на ogen-go.

Роль: Backend Developer
Документация:
- .cursor/OGEN_MIGRATION_GUIDE.md
- .cursor/ogen/02-MIGRATION-STEPS.md
- .cursor/CODE_GENERATION_TEMPLATE.md

Reference: services/combat-combos-service-ogen-go/

Требования:
1. Сгенерировать ogen код из OpenAPI spec
2. Мигрировать handlers на typed responses
3. Реализовать SecurityHandler
4. Создать benchmarks
5. Валидировать оптимизации

Issue: #{number}
```

