# Task ID: API-TASK-042
**Тип:** API Generation
**Приоритет:** высокий
**Статус:** queued
**Создано:** 2025-11-06 23:25
**Создатель:** AI Agent (API Task Creator)
**Зависимости:** API-TASK-037 (shooting.yaml)

---

## 📋 Краткое описание

Создать API спецификацию для детальных классов оружия.

**Что нужно сделать:** Создать API для 7 классов оружия (Pistols, AR, Shotguns, Snipers, SMG, LMG, Melee) с 80+ моделями оружия, 5 брендами, weapon mastery system.

---

## 🎯 Цель задания

Создать полный API для системы оружия:
- 7 классов оружия с подклассами
- 80+ конкретных моделей оружия
- 5 брендов: Arasaka, Militech, Kang Tao, Budget Arms, Constitutional Arms
- Weapon Mastery: 5 ranks (Novice → Legend, 10,000 kills)
- Weapon Mods система
- Exotic/Legendary weapons
- Cyberware weapons: Mantis Blades, Gorilla Arms, Monowire

---

## 📚 Источники информации

**Путь к документу:** `.BRAIN/02-gameplay/combat/combat-weapon-classes-detailed.md`
**Версия:** v1.0.0
**Статус:** Ready for API

---

## 📁 Целевая структура API

**Целевой файл:** `api/v1/gameplay/combat/weapons.yaml`

---

## ✅ Endpoints

1. **GET `/api/v1/gameplay/combat/weapons`** - Каталог оружия
2. **GET `/api/v1/gameplay/combat/weapons/{weapon_id}`** - Детали оружия
3. **GET `/api/v1/gameplay/combat/weapons/brands/{brand}`** - Оружие по бренду
4. **GET `/api/v1/gameplay/combat/weapons/classes/{class}`** - Оружие по классу
5. **GET `/api/v1/gameplay/combat/weapons/mastery/{character_id}`** - Mastery progress
6. **PUT `/api/v1/gameplay/combat/weapons/mastery`** - Обновить mastery
7. **GET `/api/v1/gameplay/combat/weapons/mods`** - Доступные моды
8. **GET `/api/v1/gameplay/combat/weapons/meta/{content_type}`** - Meta weapons (PvE, PvP, Extraction)

---

## ✅ Критерии приемки

1. ✅ 7 классов описаны
2. ✅ 80+ моделей в каталоге
3. ✅ 5 брендов с бонусами
4. ✅ Weapon Mastery система
5. ✅ Mod system реализована

---

**История выполнения:** 2025-11-06 23:25 - Создано


### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

