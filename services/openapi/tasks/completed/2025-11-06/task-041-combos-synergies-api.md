# Task ID: API-TASK-041
**Тип:** API Generation
**Приоритет:** средний
**Статус:** queued
**Создано:** 2025-11-06 23:20
**Создатель:** AI Agent (API Task Creator)
**Зависимости:** API-TASK-038 (abilities.yaml), API-TASK-039 (abilities-catalog.yaml)

---

## 📋 Краткое описание

Создать API спецификацию для системы комбо и синергий способностей.

**Что нужно сделать:** Создать API для 14+ комбо (8 Solo, 4 Team, 2 Legendary) с системой skill ceiling, damage multipliers и synergy matrices.

---

## 🎯 Цель задания

Создать API для продвинутой системы комбо:
- Solo Combos: Aerial Devastation, Shadow Assassin, Bullet Time Massacre, и др.
- Team Combos: Tank & Spank, Netrunner Setup, Raid Opener
- Legendary Combos: Perfect Heist, Raid Wipe
- Synergy Matrices: Equipment + Ability, Implant + Ability
- Skill Ceiling: Bronze → Diamond difficulty
- Combo Scoring System

---

## 📚 Источники информации

**Путь к документу:** `.BRAIN/02-gameplay/combat/combat-combos-synergies.md`
**Версия:** v1.0.0
**Статус:** Ready for API

---

## 📁 Целевая структура API

**Целевой файл:** `api/v1/gameplay/combat/combos-synergies.yaml`

---

## ✅ Endpoints

1. **GET `/api/v1/gameplay/combat/combos`** - Все комбо
2. **GET `/api/v1/gameplay/combat/combos/{combo_id}`** - Детали комбо
3. **POST `/api/v1/gameplay/combat/combos/execute`** - Попытка выполнить комбо
4. **GET `/api/v1/gameplay/combat/synergies`** - Матрица синергий
5. **POST `/api/v1/gameplay/combat/combos/score`** - Оценить выполнение комбо

---

## ✅ Критерии приемки

1. ✅ 14+ комбо описаны
2. ✅ Synergy matrices включены
3. ✅ Skill ceiling система реализована
4. ✅ Combo scoring work

---

**История выполнения:** 2025-11-06 23:20 - Создано


### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

