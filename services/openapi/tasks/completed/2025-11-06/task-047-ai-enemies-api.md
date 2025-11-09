# Task ID: API-TASK-047
**Тип:** API Generation
**Приоритет:** средний
**Статус:** queued
**Создано:** 2025-11-06 23:50
**Создатель:** AI Agent (API Task Creator)
**Зависимости:** API-TASK-037 (shooting.yaml), API-TASK-038 (abilities.yaml)

---

## 📋 Краткое описание

Создать API спецификацию для AI противников и врагов.

**Что нужно сделать:** Создать API для 10+ типов врагов, 3 детальных боссов, AI тактик (10+), adaptive learning, emotion/morale system, difficulty scaling.

---

## 🎯 Цель задания

Создать API для AI системы:
- 10+ типов врагов: Arasaka Security, Militech, банды (6th Street, Maelstrom, Tyger Claws, Valentinos, Voodoo Boys), Scavengers, mechs/robots, cyberpsychos
- 3 детальных босса: Adam Smasher (raid boss, 50,000 HP), Blackwall Guardian (AI boss в киберпространстве), Royce/Sasquatch/Placide (story bosses)
- AI тактики: Flanking, Kiting, Swarm, Hacker Disable, Suppressive Fire, Cover-to-Cover, и др.
- Adaptive Learning: AI учится на действиях игрока
- Emotion/Morale System: High → Normal → Low → Broken
- Communication System: radio chatter, callouts, intel
- 5 tier system: Civilian → Street Thug → Gang Member → Professional → Elite → Boss
- Difficulty scaling: динамическая система

---

## 📚 Источники информации

**Путь к документу:** `.BRAIN/02-gameplay/combat/combat-ai-enemies.md`
**Версия:** v1.0.0
**Статус:** Ready for API

---

## 📁 Целевая структура API

**Целевой файл:** `api/v1/gameplay/combat/ai-enemies.yaml`

---

## ✅ Endpoints

1. **GET `/api/v1/gameplay/combat/ai-enemies/types`** - Типы врагов
2. **GET `/api/v1/gameplay/combat/ai-enemies/{enemy_id}`** - Детали врага
3. **GET `/api/v1/gameplay/combat/ai-enemies/bosses`** - Боссы
4. **GET `/api/v1/gameplay/combat/ai-enemies/tactics`** - AI тактики
5. **POST `/api/v1/gameplay/combat/ai-enemies/behavior`** - Получить поведение AI
6. **GET `/api/v1/gameplay/combat/ai-enemies/difficulty-scaling`** - Scaling факторы

---

## ✅ Критерии приемки

1. ✅ 10+ типов врагов
2. ✅ 3 босса детализированы
3. ✅ 10+ тактик AI
4. ✅ Adaptive learning
5. ✅ Morale system
6. ✅ Difficulty scaling

---

**История выполнения:** 2025-11-06 23:50 - Создано


### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

