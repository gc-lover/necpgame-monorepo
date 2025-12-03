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

### 4. 🆕 MMO Patterns (для MMO сервисов)

**Файл:** `.cursor/performance/04a-mmo-sessions-inventory.md`

**Содержит:**
- Redis session store (stateless)
- Inventory caching (multi-level)
- Guild action batching
- Trading с optimistic locking

**Используй для:** Player sessions, inventory, guilds, trading

### 5. 🆕 Advanced DB & Cache

**Файлы:**
- `.cursor/performance/05a-database-cache-advanced.md`
- `.cursor/performance/07a-postgresql-advanced.md`
- `.cursor/performance/07b-redis-database-comparison.md`

**Содержит:**
- Time-series partitioning
- Materialized views
- Distributed cache Pub/Sub
- pgBouncer, LISTEN/NOTIFY

**Используй для:** Large-scale БД, distributed cache

### 6. 🆕 Resilience & Compression

**Файл:** `.cursor/performance/06-resilience-compression.md`

**Содержит:**
- Circuit breakers
- Feature flags
- Load shedding
- Adaptive compression (LZ4/Zstandard)
- Fallback strategies

**Используй для:** Production resilience, bandwidth optimization

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

## ✅ Обязательные файлы для всех сервисов

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

**Для MMO servers дополнительно:**
- `session_store.go` - Redis sessions (Part 4A)
- `inventory_cache.go` - Multi-level caching (Part 4A)
- `guild_batcher.go` - Action batching (Part 4A)
- `matchmaking_buckets.go` - O(1) matching (Part 4C)

**Для FPS servers дополнительно:**
- `lag_compensation.go` - Server-side rewind (Part 5B)
- `dead_reckoning.go` - Prediction (Part 5B)
- `visibility_culling.go` - Frustum/Occluder (Part 5B)

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
