# 🗺️ Roadmap оптимизации микросервисов - 2025

**План внедрения оптимизаций на 2 месяца**

---

## 📅 Неделя 1: P0 Critical (BLOCKER)

### День 1-2: Context Timeouts
**Цель:** 100% coverage  
**Сервисы:** 70 сервисов без timeouts

**Задачи:**
- [ ] Создать шаблон с константами
- [ ] Автоматизировать через script
- [ ] Проверить через grep/CI

**Метрика:** 0 goroutine leaks

### День 3-4: DB Pool Config
**Цель:** 100% coverage  
**Сервисы:** 80 сервисов без pool config

**Задачи:**
- [ ] Добавить в NewRepository()
- [ ] Стандартные значения: 25/25/5min/10min
- [ ] Проверить через grep

**Метрика:** 0 connection exhaustion

### День 5: Struct Alignment
**Цель:** 100% coverage  
**Сервисы:** Все сервисы

**Задачи:**
- [ ] Установить fieldalignment
- [ ] Запустить автофикс
- [ ] Добавить в CI/CD

**Метрика:** -30-50% memory

---

## 📅 Неделя 2-3: P1 High (Hot Path)

### Неделя 2: Memory Pooling
**Сервисы:** 20 hot path сервисов
- matchmaking-go OK (уже есть)
- inventory-service-go OK (уже есть)
- combat-* (10 сервисов)
- movement-service-go
- realtime-gateway-go
- voice-chat-service-go
- projectile-core-service-go
- и др.

**Задачи:**
- [ ] Определить hot structs
- [ ] Добавить sync.Pool
- [ ] Benchmarks до/после

**Метрика:** -30-50% allocations

### Неделя 3: Batch Operations + Caching
**Сервисы:** 35 read-heavy сервисов

**Batch Operations (15 сервисов):**
- inventory-service-go OK (уже есть)
- character-service-go
- quest-* (5 сервисов)
- economy-service-go
- social-* (5 сервисов)
- и др.

**Redis Caching (20 сервисов):**
- inventory-service-go OK (уже есть)
- character-service-go
- quest-* (5 сервисов)
- economy-service-go
- social-* (10 сервисов)
- и др.

**Задачи:**
- [ ] Batch queries вместо N queries
- [ ] 3-tier cache (memory → Redis → DB)
- [ ] TTL стратегии

**Метрика:** -90% DB round trips, -95% DB queries

---

## 📅 Неделя 4-8: P2 Medium

### Неделя 4: PGO Setup
**Цель:** CI/CD интеграция

**Задачи:**
- [ ] Добавить в Makefile
- [ ] CI/CD pipeline
- [ ] Мониторинг gains

**Метрика:** +2-14% performance

### Неделя 5-6: Continuous Profiling
**Цель:** Infrastructure setup

**Задачи:**
- [ ] Pyroscope deployment
- [ ] Интеграция в сервисы
- [ ] Grafana dashboards

**Метрика:** -30% production issues

### Неделя 7-8: Adaptive Compression
**Сервисы:** 5 network-heavy
- realtime-gateway-go
- movement-service-go
- voice-chat-service-go
- ws-lobby-go
- projectile-core-service-go

**Задачи:**
- [ ] LZ4 для real-time
- [ ] Zstandard для bulk
- [ ] Dictionary compression

**Метрика:** -40-60% bandwidth

---

## 📅 Месяц 2+: P3 Advanced

### Time-Series Partitioning
**Сервисы:** 3 analytics
- world-events-analytics-service-go
- stock-analytics-* (2 сервиса)

**Задачи:**
- [ ] DB migration
- [ ] Auto retention
- [ ] Query optimization

**Метрика:** Query ↓90%

### Materialized Views
**Сервисы:** 2 сервиса
- leaderboard-service-go
- progression-paragon-service-go

**Задачи:**
- [ ] Create views
- [ ] Refresh strategy
- [ ] Indexes

**Метрика:** 100x speedup

---

## 📊 Отслеживание прогресса

### Метрики недели:
- Context timeouts: X/70 сервисов
- DB pool: X/80 сервисов
- Struct alignment: X/90 сервисов
- Memory pooling: X/20 сервисов
- Batch ops: X/15 сервисов
- Caching: X/20 сервисов

### KPI:
- P99 latency <10ms (hot path)
- Memory <200MB per service
- DB connections <50 per service
- Goroutine leaks: 0
- GC pause <5ms P99

---

## 🎯 Приоритизация сервисов

### Tier 1: Critical (немедленно)
- matchmaking-go OK
- inventory-service-go OK
- combat-* (10 сервисов)
- movement-service-go
- realtime-gateway-go

### Tier 2: High (1-2 недели)
- character-service-go
- economy-service-go
- quest-* (5 сервисов)
- social-* (10 сервисов)

### Tier 3: Medium (1 месяц)
- analytics сервисы
- stock-* сервисы
- world-events-* сервисы

---

**Следующий шаг:** Создать GitHub Issues для P0 задач

