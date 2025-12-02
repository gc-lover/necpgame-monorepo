# 🏗️ Backend Code Templates (Optimized)

**Шаблоны оптимизированного кода для Backend Agent**

## 📚 Структура шаблонов

Шаблоны разбиты на 3 файла для удобства:

### 1. API Templates (handlers, service, repository)

**Файл:** `.cursor/templates/backend-api-templates.md`

**Содержит:**
- `handlers.go` - HTTP handlers с memory pooling
- `service.go` - бизнес-логика с lock-free metrics
- `repository.go` - DB access с batch operations

**Используй для:** REST API, CRUD операций, обычных HTTP сервисов

### 2. Game Server Templates (real-time)

**Файл:** `.cursor/templates/backend-game-templates.md`

**Содержит:**
- `game_server.go` - game loop с adaptive tick rate
- `spatial_grid.go` - spatial partitioning для >100 игроков
- `udp_server.go` - UDP server с buffer pooling

**Используй для:** Real-time game servers, matchmaking, voice chat

### 3. Utilities Templates (helpers, tests, metrics)

**Файл:** `.cursor/templates/backend-utils-templates.md`

**Содержит:**
- `worker_pool.go` - ограничение горутин
- `cache.go` - lock-free cache
- `benchmarks_test.go` - тесты с goleak и performance budgets
- `metrics.go` - Prometheus метрики

**Используй для:** Всех сервисов (обязательные utilities)

## 🔧 Как использовать шаблоны

### Шаг 1: Определи тип сервиса

**CRUD API:**
- Используй только API Templates
- Utilities Templates (обязательно)

**Game Server:**
- API Templates + Game Templates + Utilities Templates
- Все 3 категории

### Шаг 2: Копируй и адаптируй

```bash
# 1. Открой нужный template
cat .cursor/templates/backend-api-templates.md

# 2. Копируй код в свой сервис
# 3. Замени {service} на имя сервиса
# 4. Адаптируй типы под OpenAPI spec
```

### Шаг 3: Валидируй

```bash
# Запусти проверку оптимизаций
./scripts/validate-backend-optimizations.sh services/{service}-go
```

## OK Обязательные файлы для всех сервисов

**Минимум:**
- `handlers.go` (из API Templates)
- `service.go` (из API Templates)
- `repository.go` (из API Templates)
- `benchmarks_test.go` (из Utilities Templates)
- `metrics.go` (из Utilities Templates)

**Для game servers дополнительно:**
- `game_server.go` (из Game Templates)
- `spatial_grid.go` (из Game Templates)
- `udp_server.go` (если UDP нужен)
- `worker_pool.go` (из Utilities Templates)
- `cache.go` (из Utilities Templates)

## 📋 Валидация

**Перед передачей задачи запусти:**

```bash
/backend-validate-optimizations #123
```

**Или вручную:**

```bash
./scripts/validate-backend-optimizations.sh services/{service}-go
```

## См. также

- `.cursor/BACKEND_OPTIMIZATION_CHECKLIST.md` - полный чек-лист
- `.cursor/commands/backend-validate-optimizations.md` - команда валидации
- `.cursor/rules/agent-backend.mdc` - правила Backend агента
