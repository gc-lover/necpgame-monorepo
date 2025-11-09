# Task ID: API-TASK-039
**Тип:** API Generation
**Приоритет:** средний
**Статус:** queued
**Создано:** 2025-11-06 23:10
**Создатель:** AI Agent (API Task Creator)
**Зависимости:** API-TASK-038 (abilities.yaml)

---

## 📋 Краткое описание

Создать API спецификацию для каталога боевых способностей.

**Что нужно сделать:** Создать API спецификацию для каталога 27+ конкретных способностей с полными характеристиками, требованиями, синергиями и балансировкой.

---

## 🎯 Цель задания

Создать API для полного каталога боевых способностей с детальными характеристиками. Документ содержит 27 конкретных способностей:
- Combat (9): Berserk Mode, Combat Slide, Shockwave Slam, Precision Shot, и др.
- Hacking (6): System Overload, Stealth Daemon, Quickhack Barrage, и др.
- Tech (3): Deploy Turret, EMP Grenade, Repair Drone
- Stealth (3): Optical Camo, Shadow Strike, Smoke Grenade
- Support (3): Combat Stim, Shield Dome, Scan Enemy
- Mobility (3): Sandevistan, Double Jump, Dash
- Medic (2): Healing Field, Combat Revival
- Tactical (2): Recon Drone, Flashbang
- Passive (1): Kerenzikov
- Cyberware: Mantis Blades Execution

---

## 📚 Источники информации

**Путь к документу:** `.BRAIN/02-gameplay/combat/combat-abilities-catalog.md`
**Версия:** v1.0.0
**Статус:** Ready for API

---

## 📁 Целевая структура API

**Целевой файл:** `api/v1/gameplay/combat/abilities-catalog.yaml`
**API версия:** v1

---

## ✅ Что нужно сделать (детальный план)

### Endpoints:

1. **GET `/api/v1/gameplay/combat/abilities-catalog`**
   - Получить каталог всех способностей
   - Query: category, slot, class_affinity, rarity

2. **GET `/api/v1/gameplay/combat/abilities-catalog/{ability_id}`**
   - Детали конкретной способности из каталога

3. **GET `/api/v1/gameplay/combat/abilities-catalog/by-category/{category}`**
   - Способности по категории (Combat, Hacking, Tech, Stealth, Support, Mobility)

4. **GET `/api/v1/gameplay/combat/abilities-catalog/by-class/{class}`**
   - Способности доступные для класса

5. **GET `/api/v1/gameplay/combat/abilities-catalog/synergy-matrix`**
   - Матрица синергий (Equipment + Ability, Implant + Ability)

---

## ✅ Критерии приемки

1. ✅ Файл создан: `api/v1/gameplay/combat/abilities-catalog.yaml`
2. ✅ 27+ способностей описаны в catalog
3. ✅ Все endpoints работают
4. ✅ Синергии включены
5. ✅ Балансировка по тирам (Tier 1-4)

---

**История выполнения:**
- `2025-11-06 23:10` - Задание создано


### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

