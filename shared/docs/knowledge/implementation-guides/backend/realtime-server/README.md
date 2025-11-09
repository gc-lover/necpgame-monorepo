# Realtime Server Architecture - Навигация

**Версия:** 1.0.2  
**Дата:** 2025-11-07  
**Статус:** approved  
**api-readiness:** ready

---

## Микросервисная архитектура

**Ответственный микросервис:** world-service  
**Порт:** 8086  
**WebSocket маршрут:** `ws://localhost:8080/ws` (через API Gateway)  
**Статус:** 📋 В планах (Фаза 3)

**Взаимодействие с другими сервисами:**
- gameplay-service: real-time combat events
- character-service: player position updates
- social-service: chat messages relay

**WebSocket topics:**
- `/topic/zone/{zoneId}/players` - игроки в зоне
- `/topic/character/{characterId}/combat` - combat события
- `/topic/zone/{zoneId}/chat` - zone chat
- `/topic/world/events` - мировые события

---

## 📋 Описание

WebSocket server для real-time updates: Game events, Chat, Combat, World state.

---

## 📑 Структура

### Part 1: Architecture & Zones
**Файл:** [part1-architecture-zones.md](./part1-architecture-zones.md)

### Part 2: Protocol & Optimization  
**Файл:** [part2-protocol-optimization.md](./part2-protocol-optimization.md)

### Part 3: Performance Profiles  
**Файл:** [part3-performance-profiles.md](./part3-performance-profiles.md)
  
**Связь с инфраструктурой:** профили используют параметры из `../../infrastructure/caching-strategy.md`, `../../infrastructure/anti-cheat-system.md` и глобальные SLO из `../../infrastructure/error-handling-logging.md`.

---

## История изменений

- v1.0.1 (2025-11-07 02:20) - Разбит на 2 части
- v1.0.0 (2025-11-06) - Создан (926 строк)

